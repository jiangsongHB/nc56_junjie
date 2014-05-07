package nc.ui.arap.actions;

import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.arap.global.SetTempletRef;
import nc.ui.arap.pub.MyClientEnvironment;
import nc.ui.arap.pubdj.ArapDjBillCardPanel;
import nc.ui.bd.ref.BatchMatchContext;
import nc.ui.ep.dj.DjPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.verifynew.BusinessShowException;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class CopyDjAction extends DefaultAction {
	BillCardPanel billCardPanelDj=null;
	public void copyDj() throws Exception {
		copyDj(null);
	}
	public void copyDj(String xslxbm) throws Exception {
		if(null!=xslxbm){
			List<String> powerList = ((DjPanel) this.getActionRunntimeV0()).getPowerBusitypeList();
			if(powerList!=null){
				if(powerList.size()==1 && powerList.get(0).equals("nopower")){
					//没有启用权限
				}else if(!powerList.contains(xslxbm)){
					this.getParent().showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000213")/*@res "没有当前业务流程的数据权限!"*/);
					return  ;
				}
			}else{
				UIRefPane up=new UIRefPane();
				up.setRefNodeName("业务类型");
				up.setPK(xslxbm);
				if(null==up.getRefModel().getPkValue()){
					this.getParent().showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000213")/*@res "没有当前业务流程的数据权限!"*/);
					return  ;
				}
			}
			powerList = ((DjPanel) this.getActionRunntimeV0()).getPowerBilltypeList();
			if(powerList!=null){
				if(powerList.size()==1 && powerList.get(0).equals("nopower")){
					//没有启用权限
				}else if(!powerList.contains(this.getDataBuffer().getCurrentDjlxbm())){
					this.getParent().showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000086")/*@res "没有当前交易类型的使用权限!"*/);
					return  ;
				}
			} 
		}
		/** BATCH START */
        BatchMatchContext.getShareInstance().setInBatchMatch(true);
        BatchMatchContext.getShareInstance().clear();
        try {
        	

		billCardPanelDj=this.getBillCardPanel();
		int syscode = this.getDjSettingParam().getSyscode();
		billCardPanelDj.transferFocusTo(0);
		// refactoring:get the bill info from data buffer
		nc.ui.ep.dj.ARAPDjDataBuffer dataBuffer = getDataBuffer();
		if(null == dataBuffer.getCurrentDJZBVO()){
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("businessbill","UPPbusinessbill-000216")/*@res "没有可复制单据！"*/);
		}

		DjLXVO vo = (DjLXVO) MyClientEnvironment.getValue(this.getDjSettingParam().getPk_corp(), "DJLX", dataBuffer.getCurrentDjlxbm());

		if(vo != null && vo.getFcbz().booleanValue()){
			throw new BusinessShowException(dataBuffer.getCurrentDjlxmc()+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000214")/*@res "已经被封存不能复制，操作失败！"*/);
		}
		DJZBVO djzbVOCloned = (nc.vo.ep.dj.DJZBVO) dataBuffer.getCurrentDJZBVO().clone();
		// 临时注释 找不到相应方法 st 2006-03-23

		// get current djdl
		String strDjdl = dataBuffer.getCurrentDjdl();
		String strDjlxbm = dataBuffer.getCurrentDjlxbm();

		DJZBHeaderVO header = (DJZBHeaderVO) djzbVOCloned.getParentVO();
		String vouchid=header.getVouchid();
		header.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_NO));
		header.setSxr(null);
		header.setSxkjnd(null);
		header.setSxkjqj(null);
		header.setSxrq(null);
		header.setEnduser(null);
		header.setYhqrr(null);
		header.setTransientFlag(DJZBVOConsts.ACT_COPY_BILL);
		header.setYhqrkjnd(null);
		header.setYhqrkjqj(null);
		header.setYhqrrq(null);
		header.setDjbh(null);
//		header.setDdlx(vouchid);
		header.setFromSaveTotemporarily(UFBoolean.FALSE);
		header.setSscause(null);
		header.setZgyf(null);
		header.setJszxzf(DJZBVOConsts.m_intJSZXZF_NONE);
		header.setM_xtMoreTimes(UFBoolean.TRUE);
		header.setIsreded(UFBoolean.FALSE);
		header.setIsselectedpay(null);
//		header.setVouchid(null);
//		header.setZgyf(new Integer(0));
		header.setSettlenum(null);
		header.setFromSaveTotemporarily(UFBoolean.FALSE);
		// header.setDjzt(new Integer(DJZBVOConsts.m_intDJStatus_Saved));
		// header.setJszxzf(new Integer(DJZBVOConsts.m_intJSZXZF_NONE));
		header.setZzzt(new Integer(DJZBVOConsts.m_intDJZzzt_Default));
		header.setPayman(null);
		header.setSpecflag(null);
		header.setVouchid("");

		DJZBItemVO[] items = djzbVOCloned.items;
		for(DJZBItemVO item:items){
			item.setOccupationmny(null);
			//wanglei 2014-05-07 
			item.setZyx17(null);
			item.setZyx18(null);
			item.setZyx19(null);
			item.setNtotalinvoicenumber(UFDouble.ZERO_DBL);
			//end
		}

		if(null!=xslxbm)
			header.setXslxbm(xslxbm);
		dataBuffer.setCurrentDJZBVO(djzbVOCloned);
		if (getArapDjPanel().getBillCardPanelDj() instanceof ArapDjBillCardPanel) {

			ArapDjBillCardPanel djBillCardPanel = (ArapDjBillCardPanel) getArapDjPanel().getBillCardPanelDj();
			Integer intOldSysCode = djBillCardPanel.getTempletSysCode();
			if (null == intOldSysCode
					|| ((ResMessage.Lybz_AR != intOldSysCode.intValue())
							&& (ResMessage.Lybz_AP != intOldSysCode.intValue())
							&& (ResMessage.Lybz_EP != intOldSysCode.intValue()) && (ResMessage.Lybz_XJLPT != intOldSysCode
							.intValue()))) {

				if ((ResMessage.$SysCode_AR == syscode) || (ResMessage.$SysCode_AP == syscode)// 如果loadDjTemplet的DjSettingParam同步更新，重构本段代码
						|| (ResMessage.$SysCode_EP == syscode) || (ResMessage.$SysCode_XJLPT == syscode)) {
					getArapDjPanel().loadDjTemplet(new Integer(syscode), getDjSettingParam().getPk_corp(), strDjdl,
							strDjlxbm);
				} else {
					getArapDjPanel().loadDjTemplet(null, getDjSettingParam().getPk_corp(), strDjdl, strDjlxbm);
				}

			}
			// djBillCardPanel.setBillValueVO(currentDJZBVO);
			DJZBHeaderVO head = (DJZBHeaderVO) djzbVOCloned.getParentVO();
			if ((ResMessage.$SysCode_AR == syscode) || (ResMessage.$SysCode_AP == syscode)
					|| (ResMessage.$SysCode_EP == syscode) || (ResMessage.$SysCode_XJLPT == syscode)) {
				head.setLybz(new Integer(syscode));
			}
//			getArapDjPanel().setDj(djzbVOCloned);
		}
		getArapDjPanel().setM_DjState(1); // 设置单据为增加状态
		billCardPanelDj.setHeadItem("djbh", null); // 单据号
		billCardPanelDj.setHeadItem("vouchid", null); // 单据id
		billCardPanelDj.setHeadItem("spzt", null); // 审批状态
		billCardPanelDj.setHeadItem("sxbz", new Integer(DJZBVOConsts.m_intSXBZ_NO)); // 转账状态
		billCardPanelDj.setHeadItem("sxbzmc", null);
		billCardPanelDj.setHeadItem("jszxzf", new Integer(DJZBVOConsts.m_intJSZXZF_NONE)); // 转账状态
		billCardPanelDj.setHeadItem("zzzt", new Integer(DJZBVOConsts.m_intDJZzzt_Default)); // 转账状态
		String strZzzt_MC = DJZBHeaderVO.getZzztMc(new Integer(DJZBVOConsts.m_intDJZzzt_Default),null);
		billCardPanelDj.setHeadItem("zzzt_mc", strZzzt_MC); // 转账状态

		billCardPanelDj.setHeadItem("zgyfname", null);
		billCardPanelDj.setHeadItem("sxr", null);
		billCardPanelDj.setHeadItem("sxkjnd", null);
		billCardPanelDj.setHeadItem("sxkjqj", null);
		billCardPanelDj.setHeadItem("sxrq", null);
		billCardPanelDj.setHeadItem("yhqrr", null);
		billCardPanelDj.setHeadItem("yhqrkjnd", null);
		billCardPanelDj.setHeadItem("yhqrkjqj", null);
		billCardPanelDj.setHeadItem("yhqrrq", null);
		billCardPanelDj.setHeadItem("sscause", null);
		billCardPanelDj.setHeadItem("isreded", null);
		// billCardPanelDj.setHeadItem("lybz", new Integer(0)); //来源标志
		if ((ResMessage.$SysCode_AR == syscode) || (ResMessage.$SysCode_AP == syscode)
				|| (ResMessage.$SysCode_EP == syscode) || (ResMessage.$SysCode_XJLPT == syscode) || ResMessage.$SysCode_SS == syscode) {
			billCardPanelDj.setHeadItem("pzglh", syscode);
			billCardPanelDj.setTailItem("lybz", new Integer(syscode)); //
		}
		billCardPanelDj.setHeadItem("djzt", "1"); // 单据状态
		billCardPanelDj.setTailItem("zdr", null); // 制单人
		billCardPanelDj.setHeadItem("zdrq", null); // 制单日期
		if (!strDjdl.equals("ss"))
			billCardPanelDj.setHeadItem("sscause", null); // 托收单
		billCardPanelDj.setHeadItem("fktjbm", null); // 协同
		billCardPanelDj.setHeadItem("transientFlag", DJZBVOConsts.ACT_COPY_BILL); // 复制
		billCardPanelDj.setHeadItem("yhqrr", null); // 签字确认人
		//billCardPanelDj.setHeadItem("effectdate", getDjSettingParam().getLoginDate()); // 启效日期
		billCardPanelDj.setTailItem("enduser", null); // 最终修改人
		billCardPanelDj.setHeadItem("begin_date", getDjSettingParam().getLoginDate()); // 启效日期
		billCardPanelDj.setHeadItem("end_date", getDjSettingParam().getLoginDate()); // 启效日期
        if(null!=billCardPanelDj.getHeadItem("settlenum"))
        	billCardPanelDj.setHeadItem("settlenum", null);

		if (billCardPanelDj.getHeadItem("isselectedpay") != null)
			billCardPanelDj.setHeadItem("isselectedpay", null);
		if (billCardPanelDj.getHeadItem("vouchertypeno") != null)
			billCardPanelDj.setHeadItem("vouchertypeno", null);
		// if(null!=billCardPanelDj.getHeadItem("paymanname"))
		// billCardPanelDj.setHeadItem("paymanname", null);
		if (null != billCardPanelDj.getHeadItem("payman"))
			billCardPanelDj.setHeadItem("payman", null);
		if (null != billCardPanelDj.getHeadItem("paydate"))
			billCardPanelDj.setHeadItem("paydate", null);
		if(null != billCardPanelDj.getHeadItem("isselectedpay")){
			billCardPanelDj.setHeadItem("isselectedpay", null);
		}
		if(null!=xslxbm){
			billCardPanelDj.setHeadItem("xslxbm", null);
//			billCardPanelDj.setHeadItem("xslxbm", xslxbm);
			billCardPanelDj.getHeadItem("xslxbm").setValue(xslxbm);
		}
			
		for (int i = 0; i < billCardPanelDj.getBillModel().getRowCount(); i++) {
			String fb_oid=(String)billCardPanelDj.getBodyValueAt(i, "fb_oid");
            if(billCardPanelDj.getBodyItem("djxtflagname")!=null)
            	billCardPanelDj.setBodyValueAt(null, i, "djxtflagname");
			BillItem ddh=billCardPanelDj.getBodyItem(  "ddh");
			if(ddh!=null){
				billCardPanelDj.setBodyValueAt(null, i, "ddh");
			}

			if (billCardPanelDj.getBodyItem("payman") != null)
				billCardPanelDj.setBodyValueAt(null, i, "payman");
			if (billCardPanelDj.getBodyItem("paymanname") != null)
				billCardPanelDj.setBodyValueAt(null, i, "paymanname");
			if (billCardPanelDj.getBodyItem("paydate") != null)
				billCardPanelDj.setBodyValueAt(null, i, "paydate");

			if (billCardPanelDj.getBodyItem("djxtflagname") != null)
				billCardPanelDj.setBodyValueAt(null, i, "djxtflagname");
			billCardPanelDj.setBodyValueAt(null, i, "vouchid");
			billCardPanelDj.setBodyValueAt(null, i, "item_bill_pk");
			billCardPanelDj.setBodyValueAt(null, i, "item_bill_djbh");
			billCardPanelDj.setBodyValueAt(null, i, "othersysflag");
			//billCardPanelDj.setBodyValueAt(UFBoolean.FALSE, i, "pausetransact");
			getArapDjPanel().getBillCardPanelDj().setBodyValueAt(UFBoolean.FALSE, i, "pausetransact");
			billCardPanelDj.setBodyValueAt(null, i, "tbbh");
//			if(!"ss".equals(header.getDjdl())){
				billCardPanelDj.setBodyValueAt(vouchid, i, "tempddlx"); // 上层来源id
//			}
			billCardPanelDj.setBodyValueAt(null, i, "ddlx");
			billCardPanelDj.setBodyValueAt(null, i, "ddhh");
			billCardPanelDj.setBodyValueAt(null, i, "ph");
			billCardPanelDj.setBodyValueAt(null, i, "jsfsbm");
			billCardPanelDj.setBodyValueAt(null, i, "djbh");
			billCardPanelDj.setBodyValueAt(null, i, "cksqsh");
			billCardPanelDj.setBodyValueAt(null, i, "xyzh");
			billCardPanelDj.setBodyValueAt(fb_oid, i, "tempddhh");
			billCardPanelDj.setBodyValueAt(null, i, "fph");			
			billCardPanelDj.setBodyValueAt(null, i, "tbbh");
			billCardPanelDj.setBodyValueAt(null, i, "occupationmny");
			billCardPanelDj.setBodyValueAt(null, i, "contractno");  //add
			//wanglei 2014-05-07 
			billCardPanelDj.setBodyValueAt(null, i, "zyx17");
			billCardPanelDj.setBodyValueAt(null, i, "zyx18");
			billCardPanelDj.setBodyValueAt(null, i, "zyx19");
			billCardPanelDj.setBodyValueAt(null, i, "totalinvoicenumber");
			//end
			if(!getDjSettingParam().getIsQc()){
				billCardPanelDj.setBodyValueAt(getDjSettingParam().getLoginDate(), i, "qxrq");
			}
			BillItem clbh = billCardPanelDj.getBodyItem("clbh");
			if (clbh != null) {
				billCardPanelDj.setBodyValueAt(null, i, "clbh");
			}
			BillItem fphid = billCardPanelDj.getBodyItem("fphid");
			if (fphid != null) {
				billCardPanelDj.setBodyValueAt(null, i, "fphid");
			}
			BillItem ddhid = billCardPanelDj.getBodyItem("ddhid");
			if (ddhid != null) {
				billCardPanelDj.setBodyValueAt(null, i, "ddhid");
			}
			BillItem ckdid = billCardPanelDj.getBodyItem("ckdid");
			if (ckdid != null) {
				billCardPanelDj.setBodyValueAt(null, i, "ckdid");
			}
			BillItem ckdh = billCardPanelDj.getBodyItem("ckdh");
			if (ckdh != null) {
				billCardPanelDj.setBodyValueAt(null, i, "ckdh");
			}
			BillItem payflag = billCardPanelDj.getBodyItem("payflag");
			if (payflag != null) {
				billCardPanelDj.setBodyValueAt(null, i, "payflag");
			}
			BillItem payflagname = billCardPanelDj.getBodyItem("payflagname");
			if (payflagname != null) {
				billCardPanelDj.setBodyValueAt(null, i, "payflagname");
			}
			BillItem encode = billCardPanelDj.getBodyItem("encode");
			if (encode != null) {
				billCardPanelDj.setBodyValueAt(null, i, "encode");
			}

			BillItem isverifyfinished = billCardPanelDj.getBodyItem("isverifyfinished");
			if (isverifyfinished != null) {
				billCardPanelDj.setBodyValueAt(null, i, "isverifyfinished");
			}
			BillItem verifyfinisheddate = billCardPanelDj.getBodyItem("verifyfinisheddate");
			if (verifyfinisheddate != null) {
				billCardPanelDj.setBodyValueAt("3000-01-01", i, "verifyfinisheddate");
			}
			//add 
			BillItem htbh = billCardPanelDj.getBodyItem("contractno");
			if (htbh != null) {
				billCardPanelDj.setBodyValueAt(null, i, "contractno");
			}

			if (strDjdl.equals("ss")) {
				billCardPanelDj.setBodyValueAt(getDjSettingParam().getLoginDate(), i, "begin_date");
				billCardPanelDj.setBodyValueAt(getDjSettingParam().getLoginDate(), i, "end_date");
				billCardPanelDj.setBodyValueAt(null, i, "closer");
				billCardPanelDj.setBodyValueAt(null, i, "closer_name");
				billCardPanelDj.setBodyValueAt(null, i, "closedate");
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "jfybje"), i, "ybye");
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "jfbbje"), i, "bbye");
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "jffbje"), i, "fbye");
				billCardPanelDj.setBodyValueAt(
						billCardPanelDj.getBodyValueAt(i, "jfshl"), i,
						"shlye");


			} else if (strDjdl.equalsIgnoreCase("ys") || strDjdl.equalsIgnoreCase("fk")) {
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "jfybje"), i, "ybye");
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "jfbbje"), i, "bbye");
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "jffbje"), i, "fbye");
				billCardPanelDj.setBodyValueAt(
						billCardPanelDj.getBodyValueAt(i, "jfshl"), i,
						"shlye");

			} else if (strDjdl.equalsIgnoreCase("yf") || strDjdl.equalsIgnoreCase("sk")) {
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "dfybje"), i, "ybye");
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "dfbbje"), i, "bbye");
				billCardPanelDj.setBodyValueAt(billCardPanelDj.getBodyValueAt(i, "dffbje"), i, "fbye");
				billCardPanelDj.setBodyValueAt(
						billCardPanelDj.getBodyValueAt(i, "dfshl"), i,
						"shlye");

			}


		}
		SetTempletRef.afterCopySetRef(billCardPanelDj);
		if (!getDjSettingParam().getIsQc()) {

			// billCardPanelDj.setHeadItem("djzt", "1"); //单据状态
			billCardPanelDj.setHeadItem("shrq", null); // 审核日期
			billCardPanelDj.setTailItem("lrr", getDjSettingParam().getPk_user()); // 录入人
			billCardPanelDj.setTailItem("shr", null); // 审核人
			billCardPanelDj.setHeadItem("djrq", getDjSettingParam().getLoginDate()); // 单据日期
			billCardPanelDj.setHeadItem("effectdate", getDjSettingParam().getLoginDate()); // 启效日期
		} else // 期初单据
		{
			billCardPanelDj.setTailItem("shr", getDjSettingParam().getPk_user()); // 审核人
			billCardPanelDj.setTailItem("lrr", getDjSettingParam().getPk_user()); // 录入人
			billCardPanelDj.setHeadItem("shrq", getDjSettingParam().getLoginDate()); // 审核日期
			billCardPanelDj.setHeadItem("djrq", getDjSettingParam().getLoginDate()); // 单据日期
			// billCardPanelDj.setHeadItem("djzt","2");//单据状态
			try {
				// nc.vo.pub.lang.UFDate qyrq = getQyrq2(); //启用日期
				nc.vo.pub.lang.UFDate qyrq = getDjSettingParam().getQyrq2(); // 启用日期
				// 期初单据日期在启用日期前一天
				billCardPanelDj.setHeadItem("djrq", qyrq.getDateBefore(1));
			} catch (Throwable e) {
				Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000365")/*
																												 * @res
																												 * "\n**************************************\n根据("
																												 */
						+ getDjSettingParam().getQyrq()[0]
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000366")/*
																											 * @res
																											 * ")账套启用会计年度，会计期间("
																											 */
						+ getDjSettingParam().getQyrq()[1]
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000367")/*
																											 * @res
																											 * ")取账套启用日期,出错:"
																											 */
						+ e + "\n**********************************\n");
			}

		}
		// billCardPanelDj.setHeadItem("djbh", ""); //单据号

		// 设置汇率是否可编辑及汇率小数位数

		try {
			// if (m_Djdl.equals("hj"))
			// set_HlEnable(1);
			// else
			// set_HlEnable(0);
			String pk_currtype = (String) billCardPanelDj.getHeadItem("bzbm").getValueObject();
			String date = (String) billCardPanelDj.getHeadItem("djrq").getValueObject();
			((ArapDjBillCardPanel) getArapDjPanel().getBillCardPanelDj()).getM_cardTreater().changeBzbm_H(pk_currtype,
					date, true);
			billCardPanelDj.execHeadLoadFormulas();
		} catch (Throwable e) {
			Logger.debug(nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102", "UPP2006030102-000368")/*
																											 * @res
																											 * "设置汇率是否可编辑及汇率小数位数出错:"
																											 */
					+ e);
		}

		((ArapDjBillCardPanel) getArapDjPanel().getBillCardPanelDj()).getM_cardTreater().resetAccount();
		this.getArapDjPanel().getBillCardPanelDj().getM_cardTreater(). changeYHZHEdit( null);

        ///***BATCH END***/
        BatchMatchContext.getShareInstance().executeBatch();
	    }finally{
	        BatchMatchContext.getShareInstance().setInBatchMatch(false);
	        
	    }
//	    if(null!=xslxbm&&null!=getBillCardPanel().getHeadItem("xslxbm")){
//			if(null==getBillCardPanel().getHeadItem("xslxbm").getValueObject()){
//				this.getParent().showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2004","UPP2004-000213")/*@res "没有当前业务流程的数据权限!"*/);
////				return false;
//			}
//		}
	}
}