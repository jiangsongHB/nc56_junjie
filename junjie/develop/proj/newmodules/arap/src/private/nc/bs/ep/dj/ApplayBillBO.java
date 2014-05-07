package nc.bs.ep.dj;

/**
 * �Ե��ݵĴ�����ˣ�ȷ�ϣ�ǩ��ȷ�ϣ��� �������ڣ�(2001-9-3 17:24:23)
 *
 * @author����ǿ
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
	 * ApplayBill ������ע�⡣
	 */
	public ApplayBillBO() {
		super();
	}

//	/**
//	 * ���(�����)ʱ�����˻����
//	 *
//	 * ����:�·� �˴����뷽��˵���� �������ڣ�(2001-11-28 20:34:19)
//	 *
//	 * @return nc.vo.ep.dj.ResMessage
//	 * @param dj
//	 *            nc.vo.ep.dj.DJZBVO isShenHe=true ʱΪ��� falseΪ�����
//	 */
//	public ResMessage accountContr(DJZBVO dj) throws BusinessException,
//			ShenheException {
//
//		return shenhe_Sq(dj, true);
//	}

	/**
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public DJZBVO afterShenheInf(BusiTransVO[] busitransvos,
			DJZBVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {

					SystemProfile.getInstance().log("arapbillauditstart aftershenhe 1 "+busitransvos[i].getClassName());

					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("���ýӿ�"+busitransvos[i]
					.getClassName());
					((ArapPubShenheInterface) busitransvos[i]
							.getInfClass()).afterShenheAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("�������ýӿ�"+busitransvos[i]
					     .getClassName()+",��ʱ"+(end1-begin1)+"ms");

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
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO afterEffectInf(BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("���ýӿ�"+busitransvos[i]
					.getClassName());
					((IArapPubEffectInterface) busitransvos[i]
							.getInfClass()).afterEffectAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("�������ýӿ�"+busitransvos[i]
					     .getClassName()+",��ʱ"+(end1-begin1)+"ms");
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
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO afterUnEffectInf(BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("���ýӿ�"+busitransvos[i]
					.getClassName());
					((IArapPubUnEffectInterface) busitransvos[i]
							.getInfClass()).afterUnEffectAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("�������ýӿ�"+busitransvos[i]
					     .getClassName()+",��ʱ"+(end1-begin1)+"ms");
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
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public DJZBVO afterUnShenheInf(
			BusiTransVO[] busitransvos, DJZBVO dJZB)
			throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("���ýӿ�"+busitransvos[i]
					.getClassName());
					((ArapPubUnShenheInterface) busitransvos[i]
							.getInfClass()).afterUnShenheAct(dJZB);
					long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("�������ýӿ�"+busitransvos[i]
					     .getClassName()+",��ʱ"+(end1-begin1)+"ms");
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
	 * ��˵���
	 *
	 * �˻������ƣ�
	 * �����˻����տ��������տ���㵥��������㵥�����˽��㵥�������տ���㵥�����⸶����㵥
	 * ����˵�ά���˻������ĵ�ǰ����ֶ��ҽ����˻����ֿ��ơ�
	 * ���û���뵥�ݱ�����ͬ���˻������ʧ��
	 * �˻���� �˻����ֶ��ʱΪ�Ϸ�
	 * �˻����>�˻����ֶ��ʱȡ�˻��������˻�
	 *
	 * ���ֿ��Ʒ�ʽ��
	 * ���Ʒ�ʽΪ��ʾ��Ϣʱ�������ʾ��Ϣ���Կ���ˡ� J ���Ʒ�ʽΪ���Ʋ���ʱ�������ʾ��Ϣ�Ҳ��ܽ�����ˡ�
	 * ���Ʒ�ʽΪ��Ȩ����ʱ���жϵ�ǰ��¼���Ƿ����˻�������Ȩ�����ˣ��������ʾ��Ϣ���ܹ���ˣ��������ʾ��Ϣ�Ҳ�����ˡ�
	 *
	 * �˻���ǰ��� �跽-,����+
	 * res.intvalue=9999˵����Ҫ��Ȩ,res.intvalue=1ͨ����Ȩ,res.intvalue=2˵����˳ɹ�
	 */
	public ResMessage auditABill(DJZBVO dj) throws BusinessException {
		SystemProfile.getInstance().log("arapbillauditstart"+dj.header.getDjbh());
		ResMessage res = new ResMessage();
		res.isSuccess = true;
//		res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000543")/*@res "��˳ɹ�"*/;

		res.intValue = -1; //res.intValue=9999˵����Ҫ��Ȩ

		DJZBHeaderVO head = (DJZBHeaderVO) dj.getParentVO();//���ݱ�ͷ
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

		ExceptionHandler.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000544")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@���鵥�ݱ���"*/);
		res.djzbvo = dj;

		/** ��ϵͳ���ǰ�ӿ�* */
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
		res.intValue = -1; //res.intValue=9999˵����Ҫ��Ȩ

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
			res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000549")/*@res "���������ݲ��ܺ�̨���."*/;
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
				throw ExceptionHandler.createException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-000040")/*@res "�����Ѿ����ɵ�������������ˣ�����ʧ�ܣ�"*/);
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
		Log.getInstance(this.getClass()).debug("���·���˵�����Ч״̬");
		try {
			dao.updateEffectStatus(head);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw ExceptionHandler.handleException(this.getClass(),e);
		}
		long end=System.currentTimeMillis();
		Log.getInstance(this.getClass()).debug("�������·���˵�����Ч״̬,��ʱ"+(end-begin)+"ms");
		NCLocator.getInstance().lookup(nc.itf.arap.balance.IArapBalanceUpdate.class).updateForBillUnEff(new DJZBVO[]{dj});
		this.sendMessage_del(dj);

	}
	/**
	 * ��˵��� �������ڣ�(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[] 2���˻������ƣ� ��
	 *            �����˻����տ��������տ���㵥��������㵥�����˽��㵥�������տ���㵥�����⸶����㵥
	 *            ����˵�ά���˻������ĵ�ǰ����ֶ��ҽ����˻����ֿ��ơ� ���û���뵥�ݱ�����ͬ���˻������ʧ�� N
	 *            �˻������˻����ֶ��ʱΪ�Ϸ�. N �˻����>�˻����ֶ��ʱȡ�˻��������˻����ֿ��Ʒ�ʽ�� J
	 *            ���Ʒ�ʽΪ��ʾ��Ϣʱ�������ʾ��Ϣ���Կ���ˡ� J ���Ʒ�ʽΪ���Ʋ���ʱ�������ʾ��Ϣ�Ҳ��ܽ�����ˡ� J
	 *            ���Ʒ�ʽΪ��Ȩ����ʱ���жϵ�ǰ��¼���Ƿ����˻�������Ȩ�����ˣ��������ʾ��Ϣ���ܹ���ˣ��������ʾ��Ϣ�Ҳ�����ˡ�
	 *
	 * �˻���ǰ��� �跽-,����+
	 * res.intvalue=9999˵����Ҫ��Ȩ,res.intvalue=1ͨ����Ȩ,res.intvalue=2˵����˳ɹ�
	 */
	public ResMessage doBusi(nc.vo.ep.dj.DJZBVO dj,	nc.vo.arap.global.BusiTransVO[] busitransvos)
			throws BusinessException {
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		res.strMessage = "";
		res.intValue = -1; //res.intValue=9999˵����Ҫ��Ȩ

		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		//���ݱ�ͷ
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
		//��ѯ���ݱ���
		if (items == null) {
			try {
					items = djdmo.findItemsForHeader(head.getPrimaryKey());
			} catch (Exception e) {
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "���鵥�ݱ���ʧ��,���ʧ��"*/;
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

		//��ӿ���˵���ǰ����
		long t1 = System.currentTimeMillis();
		beforeShenheInf(busitransvos, dj);
		ExceptionHandler.debug("��ӿ���˵���ǰ����ǰ����ʱ��:"
				+ (System.currentTimeMillis() - t1));

		SystemProfile.getInstance().log("arapbillauditstart beforeshenhe end"+dj.header.getDjbh());

		res.djzbvo = dj;
		try {

			/** **********************************���¿�ʼ������������ */
			if (head.getSsflag().equals("1")) {
				new LockBillItem().lock_item_bill(items, head.getLrr(), 1);
				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPT2006030102-000051")/*@res "���"*/);
				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), "���");
				Proxy.getIItemConfigPrivatee().ShenHeSave(dj);
//				curdjzbbo.freeLock_item_bill(items, head.getLrr(), 1);

			}

			SystemProfile.getInstance().log("arapbillauditstart sxsp end"+dj.header.getDjbh());

			/** ******************************************����������������* */
			/** **************** Эͬ����********************** */
			ExceptionHandler.debug("Эͬ����...");
			if (head.getXtflag() != null && head.getXtflag().equals("��˻�ǩ��ȷ��")) { /*-=notranslate=-*/

				djzbbo.deleteXTBill(head.getVouchid());
				//�����µ�Эͬ����
				nc.bs.arap.billcooperation.BillCooperateBO bic = new nc.bs.arap.billcooperation.BillCooperateBO();
				bic.doCooperate(dj);

			}

			SystemProfile.getInstance().log("arapbillauditstart xt end"+dj.header.getDjbh());

			/** **************** Эͬ����****************** */
			//��ӿ���˵��ݺ���

			long t2 = System.currentTimeMillis();

			afterShenheInf(busitransvos, dj);
			ExceptionHandler.debug("��ӿ���˵���ǰ����������ʱ��:"
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
		ExceptionHandler.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000548")/*@res "��˷���..."*/);
		return res;
	}

////////	/**
////////	 * ��˵��� �������ڣ�(2001-9-3 17:31:51)
////////	 *
////////	 * @return java.lang.String[]
////////	 * @param djs
////////	 *            nc.vo.ep.dj.DJZBVO[] 2���˻������ƣ� ��
////////	 *            �����˻����տ��������տ���㵥��������㵥�����˽��㵥�������տ���㵥�����⸶����㵥
////////	 *            ����˵�ά���˻������ĵ�ǰ����ֶ��ҽ����˻����ֿ��ơ� ���û���뵥�ݱ�����ͬ���˻������ʧ�� N
////////	 *            �˻������˻����ֶ��ʱΪ�Ϸ�. N �˻����>�˻����ֶ��ʱȡ�˻��������˻����ֿ��Ʒ�ʽ�� J
////////	 *            ���Ʒ�ʽΪ��ʾ��Ϣʱ�������ʾ��Ϣ���Կ���ˡ� J ���Ʒ�ʽΪ���Ʋ���ʱ�������ʾ��Ϣ�Ҳ��ܽ�����ˡ� J
////////	 *            ���Ʒ�ʽΪ��Ȩ����ʱ���жϵ�ǰ��¼���Ƿ����˻�������Ȩ�����ˣ��������ʾ��Ϣ���ܹ���ˣ��������ʾ��Ϣ�Ҳ�����ˡ�
////////	 *
////////	 * �˻���ǰ��� �跽-,����+
////////	 * res.intvalue=9999˵����Ҫ��Ȩ,res.intvalue=1ͨ����Ȩ,res.intvalue=2˵����˳ɹ�
////////	 */
////////	public ResMessage auditABill2_out(nc.vo.ep.dj.DJZBVO dj,
////////			nc.vo.arap.global.BusiTransVO[] busitransvos)
////////			throws BusinessException {
////////		long t = System.currentTimeMillis();
////////		String result = "";
////////		String pk_accid = ""; //�˻�����
////////		String pk_currtype = ""; //���ݱ���
////////		nc.vo.bd.b120.AccidVO accidvo = null; //�˻�vo
////////		ResMessage res = new ResMessage();
////////		res.isSuccess = true;
////////		res.strMessage = "";
////////		DJZBBO curdjzbbo = new DJZBBO();
////////		res.intValue = -1; //res.intValue=9999˵����Ҫ��Ȩ
////////
////////		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
////////				.getParentVO();
////////		//���ݱ�ͷ
////////
////////		DJZBDMO djdmo = null;
////////		try {
////////			djdmo = new DJZBDMO();
////////			//��������
////////			isDistributes(dj);
////////
////////			//�жϵ����Ƿ�����������
////////
////////			if (head.getSpzt() != null
////////					&& (head.getSpzt().trim().equalsIgnoreCase(DJZBVOConsts.m_strStatusVerifying) || head
////////							.getSpzt().trim().equalsIgnoreCase(DJZBVOConsts.m_strStatusVerifiedPass))) {
////////				res.isSuccess = false;
////////				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000550")/*@res "����������ֻ�����ո�ϵͳ�����."*/;
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
////////			///** �����ѷ��͵��������У�����δ��ʼ���� */
////////			//res.isSuccess = false;
////////			//res.strMessage = "���������ݲ��ܺ�̨���.";
////////			//return res;
////////			//}
////////			//if (iStatus ==
////////			// nc.vo.pub.pf.IWorkFlowStatus.ABNORMAL_WORKFLOW_STATUS) {
////////			///** δ֪���쳣״̬ */
////////			//res.isSuccess = false;
////////			//res.strMessage = "���������ݲ��ܺ�̨���.";
////////			//return res;
////////			//}
////////			if (iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW
////////					|| iStatus == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW)
////////				;
////////			else {
////////				/** �õ�������û�����ù����� */
////////				res.isSuccess = false;
////////				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000550")/*@res "����������ֻ�����ո�ϵͳ�����."*/;
////////				return res;
////////
////////			}
////////
////////			//if (head.getXtflag() != null && head.getXtflag().equals("���")) {
////////
////////			////ɾ��Эͬ����
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
////////		//���ݱ���
////////		//cf2002-01-14 add
////////		//��ѯ���ݱ���
////////		if (items == null) {
////////			try {
////////
////////				if (head.getPzglh().intValue() == 3
////////						|| head.getDjdl().equals("ss"))
////////					items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
////////				else
////////					items = djdmo.findItemsForHeader(head.getPrimaryKey());
////////			} catch (Exception e) {
////////				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "���鵥�ݱ���ʧ��,���ʧ��"*/;
////////				res.isSuccess = false;
////////				return res;
////////			}
////////			dj.setChildrenVO(items);
////////		}
////////		//cf2002-01-14 add
////////
////////		//cf2002-06-06 add for �����������
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
////////				.println(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000544")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@���鵥�ݱ���"*/);
////////		res.djzbvo = dj;
////////
////////		//��ӿ���˵���ǰ����
////////		long t1 = System.currentTimeMillis();
////////		//nc.bs.arap.global.ArapExtInfRunBO extbo =
////////		//new nc.bs.arap.global.ArapExtInfRunBO();
////////		//nc.vo.arap.global.BusiTransVO[] busitransvos =
////////		//extbo.initBusiTrans("shenhe", head.getPzglh());
////////		beforeShenheInf(busitransvos, dj);
////////		Log.getInstance(this.getClass()).debug("��ӿ���˵���ǰ����ǰ����ʱ��:"
////////				+ (System.currentTimeMillis() - t1));
////////
////////		//cf2002-06-06 add for �����������
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
////////				.println(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000546")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@������ǰʱ��:"*/
////////						+ (System.currentTimeMillis() - t));
////////		try {
////////
////////			//��д����
//////////			Log.getInstance(this.getClass()).debug("��д����");
//////////			nc.bs.arap.ebmanager.NOTEEBMBO notbo = new nc.bs.arap.ebmanager.NOTEEBMBO();
//////////
//////////			notbo.afterAudit(dj, null);
//////////
//////////			//��д��ͬ
//////////			Log.getInstance(this.getClass()).debug("��д��ͬ");
////////
////////			//curdjzbbo.insertContract(dj);
////////
////////			//��Ϣ
////////			//��Ҫ����Ͷ�ʵĵ������� sk,fk,sj,fj , hj,ws,wf
////////			//Log.getInstance(this.getClass()).debug("��Ϣ");
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
////////			///**״̬���*/
////////
////////			///**Ԥ��*/
////////
////////			/** ���־ */
////////			//���´����˻�������
////////			//t = System.currentTimeMillis();
////////			Log.getInstance(this.getClass()).debug("���´����˻�������...");
////////			res = shenhe_Sq(dj, true);
////////			if (!res.isSuccess)
////////				return res;
////////
////////			res.intValue = 1; //ͨ����Ȩ����
////////			//nc.vo.pub.rs.Debug.println(
////////			//	"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@�����˻�������ʱ��:"
////////			//		+ (System.currentTimeMillis() - t));
////////			//���ϴ����˻�������
////////
////////			////���´���ҵ��Ӧ�գ�����Ӧ�գ��Ͷ���Ӧ��
////////			// Log.getInstance(this.getClass()).debug("���´���ҵ��Ӧ�գ�����Ӧ�գ��Ͷ���Ӧ��");
////////			// updateCoustom(dj, true);
////////			//nc.bs.so.sointerface.SaleToARDMO SaleDMO =
////////			//new nc.bs.so.sointerface.SaleToARDMO();
////////			//String xyzh = ""; //����id
////////			//Hashtable hb = null;
////////			//if (head.getDjdl().equals("ys")) { //��
////////			//if (items[0].getWldx().intValue() == 0) { //������������ǿͻ�
////////			//if (head.getLybz().intValue() == 3
////////			//|| head.getLybz().intValue() == 4) //���ۣ��ɹ�ϵͳ����,��������Ӧ��
////////			//{
////////			//hb = null;
////////			//if (items[0].getXyzh() != null &&
////////			// items[0].getXyzh().trim().length() > 0)
////////			//hb = SaleDMO.getSaleOrderCust(new String[] {
////////			// items[0].getXyzh()}); //ȡ�����ͻ�
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
////////			//} else //����ϵͳ����������
////////			//{
////////			//getCumandocBO().updateMny(
////////			//head.getKsbm_cl(),
////////			//head.getBbje(),
////////			//head.getBbje(),
////////			//head.getBbje());
////////			//}
////////			//}
////////			//} else
////////			//if (head.getDjdl().equals("sk")) { //��
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
////////			////���ϴ���ҵ��Ӧ�գ�����Ӧ�գ��Ͷ���Ӧ��
////////
////////			//���´�����˱�־
////////			Log.getInstance(this.getClass()).debug("���´�����˱�־...");
////////			ApplayBillDMO dm = new ApplayBillDMO();
////////			dm.setFlagBill2((nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO());
////////
////////			res.intValue = 2; //ͨ��������
////////
////////			/** **********************************���¿�ʼ������������ */
////////			if (head.getDjdl().equals("yf") || head.getDjdl().equals("fk")
////////					|| head.getDjdl().equals("fj")) {
////////				nc.bs.ep.itemconfig.ItemconfigBO itemconfigbo = new nc.bs.ep.itemconfig.ItemconfigBO();
////////				curdjzbbo.lock_item_bill(items, head.getLrr(), 1);
////////				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPT2006030102-000051")/*@res "���"*/);
////////				itemconfigbo.checkApprove((DJZBVO) dj.clone(),"���");
////////				curdjzbbo.freeLock_item_bill(items, head.getLrr(), 1);
////////
////////			}
////////
////////			/** ******************************************����������������* */
////////
////////			///*************���½���Ԥ�����***************** */
////////			//t = System.currentTimeMillis();
////////			////���ÿ���
////////			//Log.getInstance(this.getClass()).debug("���ÿ���...");
////////			//ResMessage res2 = shenhe_Ys_Hy(dj, "���",false);
////////			//res.strMessage = res.strMessage + res2.strMessage;
////////			//res.isSuccess = res2.isSuccess;
////////			//nc.vo.pub.rs.Debug.println(
////////			//"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@����Ԥ�����ʱ��:"
////////			//+ (System.currentTimeMillis() - t));
////////			///*************���Ͻ���Ԥ�����***************** */
////////			/** **************** Эͬ����********************** */
////////			Log.getInstance(this.getClass()).debug("Эͬ����...");
////////			if (head.getXtflag() != null && head.getXtflag().equals("��˻�ǩ��ȷ��")) {
////////
////////				//�����µ�Эͬ����
////////				nc.bs.arap.billcooperation.BillCooperateBO bic = new nc.bs.arap.billcooperation.BillCooperateBO();
////////				bic.doCooperate(dj);
////////
////////			}
////////			/** **************** Эͬ����****************** */
////////			//��ӿ���˵��ݺ���
////////			long t2 = System.currentTimeMillis();
////////
////////			afterShenheInf(busitransvos, dj);
////////			Log.getInstance(this.getClass()).debug("��ӿ���˵���ǰ����������ʱ��:"
////////					+ (System.currentTimeMillis() - t2));
////////			if (dj.m_Resmessage != null) {
////////				res.strMessage = res.strMessage + dj.m_Resmessage.strMessage;
////////				res.isSuccess = dj.m_Resmessage.isSuccess;
////////			}
////////
////////			Log.getInstance(this.getClass()).debug("��ƽ̨...");
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
////////		nc.vo.pub.rs.Debug.println(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000548")/*@res "��˷���..."*/);
////////		return res;
////////	}
//////
//////	/**
//////	 * �˴����뷽��˵���� �������ڣ�(2001-11-27 16:55:59)
//////	 *
//////	 * @exception BusinessException
//////	 *                �쳣˵����
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
//////			throw new BusinessShowException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000551")/*@res "�ո��������ʧ�ܣ�"*/, e);
//////		}
//////
//////		return sRe;
//////
//////	}
////
////	/**
////	 * ��˵��� �������ڣ�(2001-9-3 17:31:51)
////	 *
////	 * @return java.lang.String[]
////	 * @param djs
////	 *            nc.vo.ep.dj.DJZBVO[] 2���˻������ƣ� ��
////	 *            �����˻����տ��������տ���㵥��������㵥�����˽��㵥�������տ���㵥�����⸶����㵥
////	 *            ����˵�ά���˻������ĵ�ǰ����ֶ��ҽ����˻����ֿ��ơ� ���û���뵥�ݱ�����ͬ���˻������ʧ�� N
////	 *            �˻������˻����ֶ��ʱΪ�Ϸ�. N �˻����>�˻����ֶ��ʱȡ�˻��������˻����ֿ��Ʒ�ʽ�� J
////	 *            ���Ʒ�ʽΪ��ʾ��Ϣʱ�������ʾ��Ϣ���Կ���ˡ� J ���Ʒ�ʽΪ���Ʋ���ʱ�������ʾ��Ϣ�Ҳ��ܽ�����ˡ� J
////	 *            ���Ʒ�ʽΪ��Ȩ����ʱ���жϵ�ǰ��¼���Ƿ����˻�������Ȩ�����ˣ��������ʾ��Ϣ���ܹ���ˣ��������ʾ��Ϣ�Ҳ�����ˡ�
////	 *
////	 * �˻���ǰ��� �跽-,����+
////	 * res.intvalue=9999˵����Ҫ��Ȩ,res.intvalue=1ͨ����Ȩ,res.intvalue=2˵����˳ɹ�
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
////				    String str = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000552")/*@res "��˵�{0}�ŵ��ݣ���˳ɹ�\n"*/ + res[i].strMessage;
////					res[i].strMessage =NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000552",null,new String[]{String.valueOf(djs[i].listIndex + 1)});
////				}else{
////				    String str = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000510")/*@res "��˵�{0}�ŵ��ݣ����ʧ��\n"*/ + res[i].strMessage;
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
////			    String str = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000510")/*@res "��˵�{0}�ŵ��ݣ����ʧ��\n"*/ + res[i].strMessage;
////				res[i].strMessage =NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000510",null,new String[]{String.valueOf(djs[i].listIndex + 1)});
////				//+ "\n"
////				//+ e.getMessage();
////			}
////			res[i].listIndex = djs[i].listIndex;
////			res[i].vouchid = djs[i].vouchid;
////
////		}
////
////		Log.getInstance(this.getClass()).debug("����˷���...");
////		return res;
////	}
//
//	/**
//	 * ��˵��� �������ڣ�(2001-9-3 17:31:51)
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
//		//		/**״̬���*/
//		//		/**Ԥ��*/
//		//		/**���ƽ̨*/
//		//		/**���־*/
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-3 17:31:51)
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
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public DJZBVO beforeShenheInf(nc.vo.arap.global.BusiTransVO[] busitransvos,
			DJZBVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {

					SystemProfile.getInstance().log("arapbillauditstart beforeshenhe 1 "+busitransvos[i].getClassName());

					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("���ýӿ�"+busitransvos[i]
					.getClassName());
					((nc.bs.arap.outer.ArapPubShenheInterface) busitransvos[i]
							.getInfClass()).beforeShenheAct(dJZB);
					 long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("�������ýӿ�"+busitransvos[i]
					     .getClassName()+",��ʱ"+(end1-begin1)+"ms");

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
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO beforeEffectInf(nc.vo.arap.global.BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("���ýӿ�"+busitransvos[i]
					.getClassName());
					((nc.bs.arap.outer.IArapPubEffectInterface) busitransvos[i]
							.getInfClass()).beforeEffectAct(dJZB);
					 long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("�������ýӿ�"+busitransvos[i]
					     .getClassName()+",��ʱ"+(end1-begin1)+"ms");
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
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
	 * @return nc.vo.ep.dj.DJZBHeaderVO[]
	 */
	public nc.vo.pub.ExAggregatedVO beforeUnEffectInf(nc.vo.arap.global.BusiTransVO[] busitransvos,
	        nc.vo.pub.ExAggregatedVO dJZB) throws BusinessException {

		if (busitransvos != null) {
			for (int i = 0; i < busitransvos.length; i++) {
				try {
					long begin1=System.currentTimeMillis();
					Log.getInstance(this.getClass()).debug("���ýӿ�"+busitransvos[i]
					.getClassName());
					((nc.bs.arap.outer.IArapPubUnEffectInterface) busitransvos[i]
							.getInfClass()).beforeUnEffectAct(dJZB);
					 long end1=System.currentTimeMillis();
					 Log.getInstance(this.getClass()).debug("�������ýӿ�"+busitransvos[i]
					     .getClassName()+",��ʱ"+(end1-begin1)+"ms");

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
	 * ȡ���ݺ� ���ߣ��·�
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-3 17:31:51)
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
	 * ���ߣ���ǿ
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
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
	 * ���ߣ���ǿ
	 *
	 * @version ����޸�����
	 * @see ��Ҫ�μ���������
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
	 * ͨ���������VO����
	 *
	 * �������ڣ�(2001-8-25)
	 *
	 * @return nc.vo.ep.dj.DefdefVO
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                �쳣˵����
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
	 * : ��Ҫ�㷨�������Ƿ�������������
	 * ע�⣺�����־��clbz����-2--�ۿۡ�-1--����ֺ�����0--ͬ���ֺ�����1--������桢2--��Ʊ�Գ塢Ʊ������; 3�����˷���;
	 * 4�������ջ�; 5������ת����6������ת�� �������ڣ�(2001-11-21 20:30:15)
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
	 * ������˵��� 2) ���۹������տ�� ? ��ʽ���������״̬���տ��ϵͳ��ԴΪ���۹��� ?
	 * ������ʽ���ɵ��տ�ĵ��ݱ����о����������־�� ? �տ�����е������־�����ո�ϵͳ����ά���������۹���ϵͳά���� ?
	 * ���ݱ������������־���տ����������Ͳ����������������������ʡ������ջء�
	 *
	 * �������ڣ�(2001-9-3 17:31:51)
	 *
	 * @return j
	 * @param dj
	 *            nc.vo.ep.dj.DJZBVO �����ǽӿ�Ҫ���õ�ֵ ��ͷ(head) head.lybz=3 ����(items)
	 *            items[n].pausetransact=UFboolean(true),items[n].othersysflag="���۹���"
	 *            ���ߣ��·�
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
	 * �жϵ����Ƿ���� �������ڣ�(produid : AR AP EP
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
						throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000563")/*@res "�ѽ����²�����˵���!"*/);
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
	 * �˴����뷽�������� �������ڣ�(2002-7-11 13:25:53)
	 *
	 * @return boolean �·�
	 */
	public ResMessage isDistributes(DJZBVO vo) throws BusinessException {
		try {

			DJZBDAO djdmo = new DJZBDAO();
			//��������
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
	 * �˴����뷽�������� �������ڣ�(2002-7-11 13:25:53)
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
	 * �˴����뷽�������� �������ڣ�(2002-7-11 13:25:53)
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
	 * �˴����뷽�������� �������ڣ�(2002-7-11 13:25:53)
	 *
	 * @return boolean
	 */
//	public boolean lockBill(DJZBVO vo) throws BusinessException {
//		try {
////			KeyLock lock = new KeyLock();
//			if (KeyLock.lockKey(vo.getParentVO().getPrimaryKey(), "123", null)) {
//			} else {
//				throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000564")/*@res "�������ڱ����������²�ѯ��"*/);
//			}
//
//		} catch (Exception e) {
//			throw new BusinessShowException(  e.getMessage());
//		}
//
//		return false;
//	}

	/**
	 * ȷ�ϵ��� �������ڣ�(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public String qr(nc.vo.ep.dj.DJZBVO dj) throws BusinessException {
		String result = "";
		try {
			/** ״̬��� */

			/** ���־ */
			ApplayBillDMO dm = new ApplayBillDMO();
			dm.qr((nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO());
			new LockDJAction().lockDJ(dj);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);

		}
		return result;
	}

	/**
	 * �����ƽ̨ �������ڣ�(2001-9-3 17:31:51)
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
		//twei �޸ĳɴ�ԭʼvo
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
		//���ݱ�ͷ
		long t = System.currentTimeMillis();
//		nc.bs.dap.out.Dap dp = null;
//		try {
			if (head.getDjdl().equals("ss"))
				; //�����������������ƽ̨
			else {
				nc.vo.pub.ExAggregatedVO djVo = new nc.vo.pub.ExAggregatedVO(vo);
			    ArapExtInfRunBO extbo = new ArapExtInfRunBO();
				BusiTransVO[] busitransvos = extbo.initBusiTrans(
						"effect", head.getPzglh());
				this.beforeEffectInf(busitransvos,djVo);
				/////**���ƽ̨
				//��ʼ��ƽ̨VO
				DapMsgVO PfStateVO = DjbsStatTool.getDapmsgVo(dj);
				//��������Ϊ���
				PfStateVO.setMsgType(DapMsgVO.ADDMSG);
				PfStateVO.setRequestNewTranscation(false);
				//����Ϣ�����ƽ̨
				Logger.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000534")/*@res "..................��ƽ̨������...................."*/);
//				dp = getPFA();
//				try{
//					Proxy.getIDapSendMessage().sendMessage(PfStateVO, dj);
//				}catch(ComponentException ex){
//					Logger.debug("û���ҵ����ƽ̨��ejb,��Ӱ������Ĳ���");
//				}
				getFipcallfacade().sendMessage(PfStateVO, dj);

				Log.getInstance(this.getClass()).debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000565")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@������ƽ̨ʱ��:"*/
								+ (System.currentTimeMillis() - t));

				t = System.currentTimeMillis();
				try{
//					getFipcallfacade().sendMessage_dmp(PfStateVO,djVo);
					this.afterEffectInf(busitransvos,djVo);
				}catch(Exception e){
					throw ExceptionHandler.handleException(this.getClass(), e);
				}
				Log.getInstance(this.getClass()).debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000540")/*@res "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@��������Ŀ������ʱ��:"*/
						+ (System.currentTimeMillis() - t));

			}
	}
	public void sendMessage_del(nc.vo.ep.dj.DJZBVO dj)throws BusinessException {
		sendMessage_del(dj,UFBoolean.FALSE);
	}
	/**
	 * �����ƽ̨ �������ڣ�(2001-9-3 17:31:51)
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
			Log.getInstance(this.getClass()).debug("�����ƽ̨ǰ���������Ϣ");
			dj= nc.bs.arap.global.SettlementVO2DJZBVOTools.getDJZBVOBySettlementVO(dj, djzbvo.getTrans().getDetailMap());
			long end1=System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("���������ƽ̨ǰ���������Ϣ,��ʱ"+(end1-begin1)+"ms");
		}
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
//		boolean isAccCanBack = true;
//		boolean isItemCanBack = true;

		//���ݱ�ͷ
//		long t = System.currentTimeMillis();
//		nc.bs.dap.out.Dap dp = null;
//		nc.bs.dmp.out2.Dmp2 dp2 = null;
//		try {
			if (head.getDjdl().equals("ss"))
				; //�����������������ƽ̨
			else {

//				dp = getPFA();
//				dp2 = getPFDMP();
				/////**���ƽ̨
				DapMsgVO PfStateVO = DjbsStatTool.getDapmsgVo(dj);
				PfStateVO.setRequestNewTranscation(false);
				//��������Ϊ���
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
//					throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000566")/*@res "���ƾ֤�����"*/);
//				}
				ArapExtInfRunBO extbo = new ArapExtInfRunBO();
				BusiTransVO[] busitransvos = extbo.initBusiTrans(
						"uneffect", head.getPzglh());
				ExAggregatedVO djvo = new ExAggregatedVO(dj);
				if(((DJZBHeaderVO)dj.getParentVO()).getPrepay()!=null && ((DJZBHeaderVO)dj.getParentVO()).getPrepay().booleanValue() &&
				        (((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("sk")||((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("fk"))){
				    DapMsgVO pfvo = DjbsStatTool.getDapmsgVOforPrePay(dj);
					//��������Ϊ���
				    pfvo.setMsgType(DapMsgVO.DELMSG);

//					isAccCanBack =getFipcallfacade().isEditBillTypeOrProc(head.getDwbm(),pfvo.getSys(), pfvo.getProc(), pfvo.getBusiType(), pfvo.getProcMsg());

//					isItemCanBack = getFipcallfacade().isEditBillTypeOrProc_dmp(head.getDwbm(),pfvo.getSys(), pfvo.getProc(), pfvo.getBusiType(), pfvo.getProcMsg());

//					if (!isAccCanBack) {
//						throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000566")/*@res "���ƾ֤�����"*/);
//					}
//					if (!isItemCanBack) {
//						throw new nc.vo.pub.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000567")/*@res "��Ŀ���������"*/);
//					}

					getFipcallfacade().sendMessage(pfvo, dj);

//					getFipcallfacade().sendMessage_dmp(pfvo,djvo);
				}
				long begin1=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("���÷���ЧBEFORE�ӿ�");
				this.beforeUnEffectInf(busitransvos, djvo);
				 long end1=System.currentTimeMillis();
				 Log.getInstance(this.getClass()).debug("�������÷���ЧBEFORE�ӿ�,��ʱ"+(end1-begin1)+"ms");
				//����Ϣ�����ƽ̨
				 Log.getInstance(this.getClass()).debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000534")/*@res "..................��ƽ̨������...................."*/);
				long begin=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("����˵��ݺ󴫻��ƽ̨");
				getFipcallfacade().sendMessage(PfStateVO, dj);
				long end=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("��������˵��ݺ󴫻��ƽ̨,��ʱ"+(end-begin)+"ms");
				//����Ŀ������
				long begin2=System.currentTimeMillis();
				Log.getInstance(this.getClass()).debug("���÷���ЧAFTER�ӿ�");
//				getFipcallfacade().sendMessage_dmp(PfStateVO, djvo);
				 this.afterUnEffectInf(busitransvos, djvo);
				 long end2=System.currentTimeMillis();
				 Log.getInstance(this.getClass()).debug("�������÷���ЧAFTER�ӿ�,��ʱ"+(end2-begin2)+"ms");
			}
			Log.getInstance(this.getClass()).debug("sendMessage_del is over!");
	}
	public void hasVoucher (nc.vo.ep.dj.DJZBVO dj)throws  BusinessException {

	nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
			.getParentVO();
	boolean isAccCanBack = true;
		if (head.getDjdl().equals("ss"))
			; //�����������������ƽ̨
		else {
			if(((DJZBHeaderVO)dj.getParentVO()).getPrepay()!=null && ((DJZBHeaderVO)dj.getParentVO()).getPrepay().booleanValue() &&
			        (((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("sk")||((DJZBHeaderVO)dj.getParentVO()).getDjdl().equals("fk"))){
				DapMsgVO pfvo = DjbsStatTool.getDapmsgVOforPrePay(dj);
				//��������Ϊ���
			    pfvo.setMsgType(DapMsgVO.DELMSG);
				isAccCanBack =getFipcallfacade().isEditBillTypeOrProc(head.getDwbm(),pfvo.getSys(), pfvo.getProc(), pfvo.getBusiType(), pfvo.getProcMsg());
				if (!isAccCanBack) {
					throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000566")/*@res "���ƾ֤�����"*/);
				}
			}
 		}
}
//	/**
//	 * ���(�����)ʱ�����˻����
//	 *
//	 * ����:�·� �˴����뷽��˵���� �������ڣ�(2001-11-28 20:34:19)
//	 *
//	 * @return nc.vo.ep.dj.ResMessage
//	 * @param dj
//	 *            nc.vo.ep.dj.DJZBVO isShenHe=true ʱΪ��� falseΪ�����
//	 */
//	public ResMessage shenhe_Sq(DJZBVO dj, boolean isShenHe)
//			throws BusinessException, ShenheException {
//
//		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
//				.getParentVO();
//		//���ݱ�ͷ
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
	 * �������ڣ�(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[] 2���˻������ƣ� ��
	 *            �����˻����տ��������տ���㵥��������㵥�����˽��㵥�������տ���㵥�����⸶����㵥
	 *            ����˵�ά���˻������ĵ�ǰ����ֶ��ҽ����˻����ֿ��ơ� ���û���뵥�ݱ�����ͬ���˻������ʧ�� N
	 *            �˻������˻����ֶ��ʱΪ�Ϸ�. N �˻����>�˻����ֶ��ʱȡ�˻��������˻����ֿ��Ʒ�ʽ�� J
	 *            ���Ʒ�ʽΪ��ʾ��Ϣʱ�������ʾ��Ϣ���Կ���ˡ� J ���Ʒ�ʽΪ���Ʋ���ʱ�������ʾ��Ϣ�Ҳ��ܽ�����ˡ� J
	 *            ���Ʒ�ʽΪ��Ȩ����ʱ���жϵ�ǰ��¼���Ƿ����˻�������Ȩ�����ˣ��������ʾ��Ϣ���ܹ���ˣ��������ʾ��Ϣ�Ҳ�����ˡ�
	 *
	 * �˻���ǰ��� �跽-,����+
	 *
	 * ע:������ʱres.intValue=9999˵����Ҫ��Ȩ
	 *
	 * ��˸��ֵ��ݱ��壬���ÿ�е��ʻ���������֧���������Ƿ���Ч������ǣ����޸��ʻ����������ʧ�ܡ�
	 * �޸��ˣ�rocking
	 * �޸�ʱ�䣺2005-08-03
	 */
	private ResMessage shenhe_Sq_All(DJZBVO dj, boolean isShenHe)throws  BusinessException {
		DJZBItemVO[] items = null; //���ݱ���
		String pk_accid = ""; //�˻�����
		String pk_currtype = ""; //���ݱ���
		nc.vo.bd.b120.AccidVO accidvo = null; //�˻�vo
		nc.vo.pub.lang.UFDouble changedValue = new nc.vo.pub.lang.UFDouble(0.00);//�˻���ǰ���(�޸ĺ��)
		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO();//���ݱ�ͷ
		ResMessage res = new ResMessage();
		res.isSuccess = true;
		res.djzbvo = dj;
		ShenheException shenheE = new ShenheException();
		ArapBusinessException shygsqex = new ArapBusinessException();
		res.intValue = -1; //res.intValue=9999˵����Ҫ��Ȩ
		java.util.Vector<String> v = new java.util.Vector<String>();
		try {
			if (dj.getChildrenVO() == null || dj.getChildrenVO().length < 1) {
				//djû�б��������鵥�ݱ���
				DJZBDAO djdmo = new DJZBDAO();
				items = djdmo.findItemsForHeader(head.getVouchid());
			} else{
				items = (DJZBItemVO[]) dj.getChildrenVO();
			}
			//���´����˻�������
			nc.bs.bd.b120.AccidDMO acciddmo = new nc.bs.bd.b120.AccidDMO(); //�˻�dmo
 			Hashtable<String, AccidVO> hAccidVO = new Hashtable<String, AccidVO>();
			for (int i = 0; i < items.length; i++) {
				res.isSuccess = true;
				res.intValue = 1;
				res.strMessage = "";
				pk_accid = items[i].getAccountid(); //�˻�����
				pk_currtype = items[i].getBzbm(); //��������
				//���ݱ���û���˻�-��ά��
				if (pk_accid == null || pk_accid.trim().length() == 0)
					continue;
				String strHashKey = pk_accid + pk_currtype;
				if (hAccidVO.get(strHashKey) == null) {
					accidvo = acciddmo.findByPkandBz(pk_accid, pk_currtype);
					if (accidvo == null || accidvo.getPrimaryKey() == null) //�Ƿ��ж���ָ�����ֵ��˻�
					{
						if("ws".equals(head.getDjdl())||"wf".equals(head.getDjdl()))
						{
							res.isSuccess = false;
							res.intValue = 1;
							res.strMessage = nc.bs.ml.NCLangResOnserver .getInstance().getStrByID("2006030102", "UPP2006030102-000599"); //@res û�������뵥�ݱ�����ͬ���ֵ��˻�,���ʧ��"
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
				//�������ʱ���ж��ʻ��Ƿ񶳽�����������򵥾ݲ�����ˡ�2003.12.09����䶯
				if (accidvo != null)
				{
					if ("1".equals(accidvo.getFrozenflag())) {
 						res.strMessage = nc.bs.ml.NCLangResOnserver .getInstance().getStrByID("2006030102", "UPP2006030102-000569");// @res"�˻��Ѿ�����,���ʧ��"
						res.isSuccess = false;
						res.intValue = 1;
						shenheE.m_ResMessage = res;
						shenheE.setBusiStyle(nc.vo.pub.pf.IPfRetExceptionStyle.NOTDEAL);
						throw shenheE;
					} else if ("2".equals(accidvo.getFrozenflag())) {
						res.strMessage = nc.bs.ml.NCLangResOnserver .getInstance().getStrByID("2006030102", "UPP2006030102-000570");//@res "�˻��Ѿ�����,���ʧ��"
						res.isSuccess = false;
						res.intValue = 1;
						shenheE.m_ResMessage = res;
						throw shenheE;
					}
				}
				//���濪ʼ����ʻ����
				if (accidvo.getCurrmny() == null)
					accidvo.setCurrmny(ArapCommonTool.getZero());
				changedValue = items[i].getYbye();
				//������ʻ��ǡ����ϡ����ǡ���ȥ��ԭ�ؽ��
				int shzb=isShenHe?1:-1;//��˱�־
				int zfbz=getDJZFBZ(head.getDjdl(),items[i]);//֧����־
				changedValue =  accidvo.getCurrmny().add(changedValue.multiply(shzb*zfbz)) 	;
				//��鼰�޸��˻�����޶�
				if (changedValue.compareTo  (accidvo.getDeficit() == null ?  ArapCommonTool.getZero(): accidvo.getDeficit())<0
					&& changedValue.compareTo( accidvo.getCurrmny() )<0 && (accidvo.getIscont().booleanValue()))
				{
					if ("0".equals(accidvo.getContype())) //���Ʒ�ʽΪ:��ʾ
					{
						res.strMessage =NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000600", null, new String[] { String.valueOf(i+1), accidvo.getAccidname()});
						res.itemRow = i; //���õ��ݱ����к�
						res.accidname = accidvo.getAccidname(); //�����˻�����
						res.pk_accid = accidvo.getPk_accid(); //�����˻�����
						v.addElement(res.strMessage);
					} else if ("1".equals(accidvo.getContype())) //���Ʒ�ʽΪ:"��ʾ:("���Ʋ���")
					{
						res.isSuccess = false;
						res.strMessage =  NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000601", null, new String[] {String.valueOf(i+1), accidvo.getAccidname()});
						res.itemRow = i; //���õ��ݱ����к�
						res.accidname = accidvo.getAccidname(); //�����˻�����
						res.pk_accid = accidvo.getPk_accid(); //�����˻�����
						shygsqex = new ArapBusinessException(res.strMessage);
						shygsqex.m_ResMessage = res;
						throw shygsqex;
					} else //���Ʒ�ʽΪ:��Ȩ
					{
						if (items[i].getIsSqed().booleanValue()) {
							res.strMessage =  NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000602", null, new String[] {String.valueOf(i+1), accidvo.getAccidname()});//@res*"�Ѿ���Ȩ:�˻���Ԥ��"
						} else {
							if (items[i].getSqflag().intValue() == 0) {
								res.isSuccess = false;
								res.strMessage = NCLangResOnserver .getInstance().getStrByID( "2006030102", "UPP2006030102-000582", null, new String[] { String .valueOf(i + 1) });//@res"����Ȩ::�����{0}��,�˻���Ԥ��"
								res.intValue = nc.vo.bd.global.ResMessage.$REQUEST_SQ; //��Ҫ��Ȩ
								res.itemRow = i; //���õ��ݱ����к�
								res.accidname = accidvo.getAccidname(); //�����˻�����
								res.pk_accid = accidvo.getPk_accid(); //�����˻�����
								shenheE = new ShenheException(res.strMessage);
								shenheE.setBusiStyle(nc.vo.pub.pf.IPfRetExceptionStyle.DEAL);
								shenheE.m_ResMessage = res;
								throw shenheE;
							} else if (items[i].getSqflag().intValue() == -nc.vo.bd.global.ResMessage.$CANCEL_SQ) {
								res.isSuccess = false;
								res.strMessage = NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000583", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });//@res"����:�����{0}���˻�({1})�û�ȡ����Ȩ"
								res.itemRow = i; //���õ��ݱ����к�
								res.accidname = accidvo.getAccidname(); //�����˻�����
								res.pk_accid = accidvo.getPk_accid(); //�����˻�����
								res.intValue = nc.vo.bd.global.ResMessage.$CANCEL_SQ;
								shygsqex = new ArapBusinessException( res.strMessage);
								shygsqex.m_ResMessage = res;
								throw shygsqex;

							} else {
								res.isSuccess = false;
								res.strMessage = NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000584", null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });// @res"����:�����{0}���˻�({1})�û���Ȩʧ��"
								res.itemRow = i; //���õ��ݱ����к�
								res.accidname = accidvo.getAccidname(); //�����˻�����
								res.pk_accid = accidvo.getPk_accid(); //�����˻�����
								res.intValue = nc.vo.bd.global.ResMessage.$FALSE_SQ;
								shygsqex = new ArapBusinessException( res.strMessage);
								shygsqex.m_ResMessage = res;
								throw shygsqex;
							}
						}
					}
				}
				//��鼰�޸��˻�����޶�
				if (accidvo.getHighmnyiscon().booleanValue()) {
					if (changedValue.compareTo(accidvo.getHighmny() == null ? ArapCommonTool.getZero() : accidvo.getHighmny())>0
							&& changedValue .compareTo( accidvo .getCurrmny())>0 && ( accidvo.getHighmnyiscon().booleanValue()))
					//�˻���Ԥ��
					{
						if ("0".equals(accidvo.getHighmnycontype())) //���Ʒ�ʽΪ:��ʾ
						{
							res.strMessage =  NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000585",
									null, new String[] {  String.valueOf(i + 1), accidvo.getAccidname() }); // @res��ʾ:�����{0}�� �˻�({1})������޶�"
							res.itemRow = i; //���õ��ݱ����к�
							res.accidname = accidvo.getAccidname(); //�����˻�����
							res.pk_accid = accidvo.getPk_accid(); //�����˻�����
							v.addElement(res.strMessage);
						} else if ("1".equals(accidvo.getHighmnycontype())) //���Ʒ�ʽΪ:���Ʋ���
						{
							res.isSuccess = false;
							res.strMessage =NCLangResOnserver.getInstance() .getStrByID( "2006030102", "UPP2006030102-000586",
									null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });//@res"����:�����{0}�� �˻�({1})������޶�"
							res.itemRow = i; //���õ��ݱ����к�
							res.accidname = accidvo.getAccidname(); //�����˻�����
							res.pk_accid = accidvo.getPk_accid(); //�����˻�����
							shygsqex = new ArapBusinessException(res.strMessage);
							shygsqex.m_ResMessage = res;
							throw shygsqex;
						} else //���Ʒ�ʽΪ:��Ȩ
						{
							if (items[i].getIsSqed().booleanValue()) {
								res.strMessage =  NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000587",
										null, new String[] { String.valueOf(i + 1), accidvo.getAccidname() });//@res"�Ѿ���Ȩ:�����{0}���˻�({1})������޶�"
							} else {
								if (items[i].getSqflag().intValue() == 0) {
									res.isSuccess = false;
									res.strMessage =  NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000588",
											null, new String[] { String .valueOf(i + 1), accidvo .getAccidname() });//@res"����Ȩ:�����{0}���˻�({1})������޶�"
									res.intValue = nc.vo.bd.global.ResMessage.$REQUEST_SQ; //��Ҫ��Ȩ
									res.itemRow = i; //���õ��ݱ����к�
									res.accidname = accidvo.getAccidname(); //�����˻�����
									res.pk_accid = accidvo.getPk_accid(); //�����˻�����
									shenheE = new ShenheException(res.strMessage);
									shenheE .setBusiStyle(nc.vo.pub.pf.IPfRetExceptionStyle.DEAL);
									shenheE.m_ResMessage = res;
									throw shenheE;
								} else if (items[i].getSqflag().intValue() == -nc.vo.bd.global.ResMessage.$CANCEL_SQ) {
									res.isSuccess = false;
									res.strMessage =  NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000583",
											null, new String[] { String .valueOf(i + 1), accidvo .getAccidname() });//@res"����:�����{0}���˻�({1})�û�ȡ����Ȩ"
									res.itemRow = i; //���õ��ݱ����к�
									res.accidname = accidvo.getAccidname(); //�����˻�����
									res.pk_accid = accidvo.getPk_accid(); //�����˻�����
									res.intValue = nc.vo.bd.global.ResMessage.$CANCEL_SQ;
									shygsqex = new ArapBusinessException(
											res.strMessage);
									shygsqex.m_ResMessage = res;
									throw shygsqex;

								} else {
									res.isSuccess = false;
									res.strMessage = NCLangResOnserver .getInstance() .getStrByID( "2006030102", "UPP2006030102-000584",
											null, new String[] { String .valueOf(i + 1), accidvo .getAccidname() });//@res"����:�����{0}���˻�({1})�û���Ȩʧ��"
									res.itemRow = i; //���õ��ݱ����к�
									res.accidname = accidvo.getAccidname(); //�����˻�����
									res.pk_accid = accidvo.getPk_accid(); //�����˻�����
									res.intValue = nc.vo.bd.global.ResMessage.$FALSE_SQ;
									shygsqex = new ArapBusinessException(res.strMessage);
									shygsqex.m_ResMessage = res;
									throw shygsqex;
								}
							}
						}
					}
				}
				//�޸��˻����еĵ�ǰ���
				accidvo.setCurrmny(changedValue);
			}
 			Enumeration voKeys = hAccidVO.keys();
			while (voKeys.hasMoreElements()) {
				String nextKey = (String) voKeys.nextElement();
				accidvo = (nc.vo.bd.b120.AccidVO) hAccidVO.get(nextKey);
				acciddmo.update_curYe(accidvo);
			}
 			//���ϴ����˻�������
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		res.strMessage = "";
		if (v != null && v.size() > 0)
			res.strMessage = v.elementAt(0).toString();
		return res;
	}
	/**
	 *���ݵ������ͷ���֧����־��1-�� ��2-֧
	 *�����ˣ�rocking
	 *�������ڣ�2005-8-4
	 */
	private int getDJZFBZ(String djdl,DJZBItemVO item)
	{
		if ("sk".equals(djdl))return 1; //�տ +
		else if ( "fk".equals(djdl))return -1; //��� -
		else if ("sj".equals(djdl))return 1; //�տ���㵥+
		else if ("fj".equals(djdl))return -1; //������㵥-
		else if ( "ws".equals(djdl))return 1; //�����տ���㵥+
		else if ( "wf".equals(djdl))return -1; //���⸶����㵥-
		else if ( "hj".equals(djdl)) //���˽��㵥
		{
			if(item.getFx().intValue()==1)return -1;
			else return 1;
		}
		else return 0;
	}

	/**
	 * ȡ����˵��� �������ڣ�(2001-9-3 17:31:51)
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
		res.intValue = ResMessage.$SHENHE_SUCCESS; //res.intValue=9999˵����Ҫ��Ȩ

		AdjustBillInf info = new AdjustBillInf();
		try {
			UFBoolean flag = info.getAdjustbill_unAudit(dj);
			if(null != flag && !flag.booleanValue()){
				throw ExceptionHandler.createException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-000040")/*@res "�����Ѿ����ɵ�������������ˣ�����ʧ�ܣ�"*/);
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
		DJZBItemVO[] items = (DJZBItemVO[]) dj.getChildrenVO(); //���ݱ���
		//cf2002-01-14 add
		//��ѯ���ݱ���
		if (items == null) {
			try {

				if (head.getPzglh().intValue() == 3
						|| head.getDjdl().equals("ss"))
					items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
				else
					items = djdmo.findItemsForHeader(head.getPrimaryKey());
			} catch (Exception e) {
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "���鵥�ݱ���ʧ��,���ʧ��"*/;
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
					throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000609")/*@res "�����Ѿ�����,���ܷ����"*/);
				}
			}
		try {
		    //		  Update by Songtao 2005-03-23 ������˼����ӵ�������
			ArapDjBsCheckerBO checkbo = new ArapDjBsCheckerBO();
			ResMessage tempres = checkbo.checkUnApproveBill(dj);
//			String ts=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillBO2 ��ѯ���ݿ��е�ts*******"+ ts+"****************");

			if(tempres!=null && tempres.isSuccess==false){
			    throw ExceptionHandler.createException(tempres.strMessage);
			}
//			��ӿڷ���˵���ǰ����
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
		res.intValue = -1; //res.intValue=9999˵����Ҫ��Ȩ

		nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
				.getParentVO();
		if (head.getSpzt() != null && head.getSpzt().trim().equals(DJZBVOConsts.m_strStatusVerifying)) {
			//res.intValue = ResMessage.$SHENHE_SUCCESS;
			// return res;
			res.isSuccess = false;
			res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000611")/*@res "�����е��ݲ��ܺ�̨�����."*/;
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
				/** �õ�������û�����ù����� */
				res.isSuccess = false;
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000612")/*@res "���������ݲ��ܺ�̨�����."*/;
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
	 * ȡ����˵��� �������ڣ�(2001-9-3 17:31:51)
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
		res.intValue = -1; //res.intValue=9999˵����Ҫ��Ȩ

		AdjustBillInf info = new AdjustBillInf();
		try {
			UFBoolean flag = info.getAdjustbill_unAudit(dj);
			if(null != flag && !flag.booleanValue()){
				throw ExceptionHandler.createException( nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v55-000040")/*@res "�����Ѿ����ɵ�������������ˣ�����ʧ�ܣ�"*/);
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

		DJZBItemVO[] items = (DJZBItemVO[]) dj.getChildrenVO(); //���ݱ���
		//cf2002-01-14 add
		//��ѯ���ݱ���
		if (items == null) {
			try {

				if (head.getPzglh().intValue() == 3
						|| head.getDjdl().equals("ss"))
					items = djdmo.findItemsForHeader_SS(head.getPrimaryKey());
				else
					items = djdmo.findItemsForHeader(head.getPrimaryKey());
			} catch (Exception e) {
				res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000148")/*@res "���鵥�ݱ���ʧ��,���ʧ��"*/;
				res.isSuccess = false;
				return res;
			}
			dj.setChildrenVO(items);
		}
		//cf2002-01-14 add

		//cf2002-06-06 add for �����������
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
				//res.strMessage="�����Ѿ�����,���ܷ����";
				//res.isSuccess=false;
				//return res;
				throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000609")/*@res "�����Ѿ�����,���ܷ����"*/);
			}
		}
        ArapDjBsCheckerBO bocheck = new ArapDjBsCheckerBO();
        ResMessage res1;
		try {
			long begin=System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("����˵���ǰУ��");
			res1 = bocheck.checkUnApproveBill(dj);
//			String ts=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillDMO ��ѯ���ݿ��е�ts*******"+ ts+"****************");
			long end=System.currentTimeMillis();
			Log.getInstance(this.getClass()).debug("��������˵���ǰУ��,��ʱ"+(end-begin)+"ms");
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
		//��ӿڷ���˵���ǰ����
		long t1 = System.currentTimeMillis();
		long begin=System.currentTimeMillis();
		Log.getInstance(this.getClass()).debug("����˵���ǰBEFORE�ӿ�");
		beforeUnShenheInf(busitransvos, dj);
//		String ts=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//		Log.getInstance(this.getClass()).debug("****ApplayBillDMO ��ѯ���ݿ��е�ts*******"+ ts+"****************");
		long end=System.currentTimeMillis();
		Log.getInstance(this.getClass()).debug("��������˵���ǰBEFORE�ӿ�,��ʱ"+(end-begin)+"ms");
		Log.getInstance(this.getClass()).debug("��ӿڷ���˵���ǰ����ǰ����ʱ��:"
				+ (System.currentTimeMillis() - t1));

		try {
			dj.setParam_Ext_Save();
			  if(lst.size()>0){
				    djdmo.wszzFB((DJZBItemVO[])lst.toArray(new DJZBItemVO[]{}));
				}
			if (head.getSsflag().equals("1") ) {
				/** **********************************���¿�ʼ������������ */
				new LockBillItem().lock_item_bill(items, head.getLrr(), 1);
				//itemconfigbo.checkApprove((DJZBVO) dj.clone(), "��˷����");
				Proxy.getIItemConfigPrivatee().unShenHe((DJZBVO) dj.clone(),false);

				/** ******************************************����������������* */
//				String tss=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//				Log.getInstance(this.getClass()).debug("****ApplayBillDMO ��ѯ���ݿ��е�tss*******"+ tss+"****************");

			}

			res.intValue = 1; //ͨ����Ȩ����

			//���ϴ����˻�������

			/** **********************Эͬ����*********************** */

			if (head.getXtflag() != null && head.getXtflag().equals("��˻�ǩ��ȷ��")) { /*-=notranslate=-*/
				//ɾ��Эͬ����
				curdjzbbo.deleteXTBill(head.getVouchid());
			}
			/** *********************Эͬ����*********************** */
//			String tss=new DJZBBO().getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillDMO ��ѯ���ݿ��е�ts*******"+ tss+"****************");

			//��ӿ���˵��ݺ���
			long t2 = System.currentTimeMillis();
			afterUnShenheInf(busitransvos, dj);
			Log.getInstance(this.getClass()).debug("��ӿڷ���˵���ǰ����������ʱ��:"
					+ (System.currentTimeMillis() - t2));
			if (dj.m_Resmessage != null) {
				res.strMessage = res.strMessage + dj.m_Resmessage.strMessage;
				res.isSuccess = dj.m_Resmessage.isSuccess;
			}
			res.intValue = 2; //ͨ��������
			res.m_Ts = head.getts().toString();
			head.setShrq(null);
			//
		} catch (Exception e) {

			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return res;
	}


	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-3 17:31:51)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-3 17:31:51)
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

				/** ״̬��� */
				nc.vo.ep.dj.DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj
						.getParentVO();
				DJZBItemVO[] items = (DJZBItemVO[])dj.getChildrenVO();
				List<String> fbpks=new ArrayList<String>();
				for(DJZBItemVO item:items){
					if(null!=item.getDdhh()&&item.getDdhh().length()>0)
						fbpks.add(item.getDdhh());
				}
		        //��ӿ���������ǰ����
				DJZBBO djzbbo = new DJZBBO();
		        long t1 = System.currentTimeMillis();
		        nc.bs.arap.global.ArapExtInfRunBO extbo =
		            new nc.bs.arap.global.ArapExtInfRunBO();
		        nc.vo.arap.global.BusiTransVO[] busitransvos =
		            extbo.initBusiTrans("del", head.getPzglh());
		        djzbbo.beforeDelInf(busitransvos, dj);
		        nc.bs.logging.Log.getInstance(this.getClass()).warn("del��ӿ��޸ĵ���ǰ����ǰ����ʱ��:" + (System.currentTimeMillis() - t1));
		        Proxy.getIWorkflowMachine().deleteCheckFlow(head.getDjlxbm(), head.getVouchid(), head.getShr(), true);
		        /** ���־ */
				//��������
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

	            //���������������ά��

	            return dj;
			}
		/**
		 * ȡ��ȷ�ϵ��� �������ڣ�(2001-9-3 17:31:51)
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
//		  /ɾ��Эͬ���� add by rocking in 2005-10-27 for nc31sp1
			 if (head.getXtflag() != null && head.getXtflag().equals("����")) { /*-=notranslate=-*/
			 	nc.bs.ep.dj.DJZBBO zbbo=new nc.bs.ep.dj.DJZBBO();
	           //ɾ��Эͬ����
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
//			ͬ��ts
			String tablename = "arap_djzb";
			if (head.getPzglh().intValue() == 3 || head.getDjdl().equals("ss"))
				tablename = "arap_item";
		       nc.bs.logging.Log.getInstance(this.getClass()).warn("del��ӿ��޸ĵ���ǰ����������ʱ��:"  );

			try {
                res.m_Ts = djdmo.getTsByPrimaryKey(head.getVouchid(), tablename);
            } catch ( Exception e) {
            	throw ExceptionHandler.handleException(this.getClass(), e);
            }
			return res;
		}


	/**
	 * ȡ��ǩ��ȷ�ϵ��� �������ڣ�(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage unYhqr(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException {
		ResMessage res = new ResMessage();
		try {
			/** ״̬��� */

			/** ���־ */
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
	 * ǩ��ȷ�ϵ��� �������ڣ�(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage sign(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException,nc.vo.pub.BusinessException {
		ResMessage res = new ResMessage();

		res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000616")/*@res "ǩ��ȷ�ϳɹ�"*/;
		try {
			/** ���־ */
			DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO();
	    	DjVOTreaterAid.supplementXTFlag(dj);
			//whether the bill can be commissioned
	    	if(!head.getIsjszxzf().booleanValue()){
		    	Boolean flag = new ArapDjBsCheckerBO().isSettled(head.getDwbm(), head.getPzglh(), dj);
		    	if(flag){
		    		 throw ExceptionHandler.createException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000071")/*@res "�����Ѿ�����,����ʧ��"*/);
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
	 * ǩ��ȷ�ϵ��� �������ڣ�(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public ResMessage unsign(nc.vo.ep.dj.DJZBVO dj)
			throws BusinessException,nc.vo.pub.BusinessException {
		ResMessage res = new ResMessage();

		res.strMessage = NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030102","UPP2006030102-000616")/*@res "ǩ��ȷ�ϳɹ�"*/;
		try {
			/** ���־ */
			DJZBHeaderVO head = (nc.vo.ep.dj.DJZBHeaderVO) dj.getParentVO();
	    	DjVOTreaterAid.supplementXTFlag(dj);
			//whether the bill can be commissioned
			UFBoolean isReded = head.getIsreded()==null?new UFBoolean(false):head.getIsreded();
			if(isReded.booleanValue()){
				throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000231")/*@res "���ݺ�Ϊ"*/+head.getDjbh()+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000237"));
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
					   throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("200656","UPP200656-000017")/*@res "���µ��ݷ�¼֧��״̬ʧ��!"*/);
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