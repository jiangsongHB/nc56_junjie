package nc.bs.ep.dj;

/**
 * 此处插入类型说明。 创建日期：(2001-9-3 17:26:14)
 *
 * @author：邱冬强
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.arap.global.ArapInvokeSettlement;
import nc.bs.arap.pub.PubDAO;
import nc.bs.arap.util.SqlUtils;
import nc.bs.dao.DAOException;
import nc.bs.logging.Log;
import nc.bs.pub.pf.CheckStatusCallbackContext;
import nc.bs.pub.pf.ICheckStatusCallback;
import nc.bs.pub.pf.IPrintDataGetter;
import nc.impl.arap.proxy.Proxy;
import nc.itf.arap.billsplit.IBillSplitDAO;
import nc.jdbc.framework.exception.DbException;
import nc.ui.pub.print.IDataSource;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.mapping.Arap_djfbVOMeta;
import nc.vo.arap.pub.ArapBillMapVOTool;
import nc.vo.arap.pub.ArapBusinessException;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.cmp.BusiInfo;
import nc.vo.cmp.settlement.SettlementBodyVO;
import nc.vo.dap.voucher.MsgAggregatedStruct;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class ApplayBillDMO extends nc.bs.pub.DataManageObject implements
		nc.bs.dmp.outinteface.IDmpGetData, nc.bs.dap.out.IAccountProcMsg,
		nc.bs.pub.pf.IQueryData,IBillSplitDAO, nc.bs.pub.pf.ICheckState3,
		nc.bs.pub.pf.IBackCheckState3, ICheckStatusCallback,nc.bs.dap.out.IAccountProcMsgInBulk,IPrintDataGetter
		,nc.bs.pub.pf.IQueryData2//add by ouyangzhb 2013-10-23 表体参照过滤查询
{

    ApplayBillDAO appdao=new ApplayBillDAO();
	/**
	 * ApplayBillDMO 构造子注解。
	 *
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public ApplayBillDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super();
	}

	/**
	 * ApplayBillDMO 构造子注解。
	 *
	 * @param dbName
	 *            java.lang.String
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public ApplayBillDMO(String dbName) throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super(dbName);
	}

	public void BackFillVoucher(String billTypeOrProc, String ProcMsg,
			String VouchEntryID) {
	}

	/**
	 * 反审为审批进行中 参数：单据Id,审批人ID,反审批日期,反审批语 创建日期：(2002-12-9 15:29:39)
	 *
	 * @param approveId
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *
	 */
	/*
	 * MODIFICATION LOG wangqiang,2004-11-16 revise the sql statement:add the
	 * update column of Sxbz
	 */
	public void backGoing(AggregatedValueObject vo, String approveId,
			String approveDate, String backNote) throws java.lang.Exception {

		ExceptionHandler.debug("################backGoing ####################");

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "backGoing",
				new Object[] { vo, approveId, approveDate, backNote });
		/** ********************************************************** */
		appdao.backGoing(vo,approveId,approveDate,backNote);
		new ArapInvokeSettlement().invokeCmp((DJZBVO)vo,approveDate!=null? new UFDate(approveDate):((DJZBHeaderVO)vo.getParentVO()).getDjrq());
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "backGoing", new Object[] {
				vo, approveId, approveDate, backNote });
		/** ********************************************************** */

	}

	/**
	 * 反审为自由状态 参数：单据Id,审批人ID,反审批日期,反审批语 创建日期：(2002-12-9 15:29:39)
	 *
	 * @param approveId
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                异常说明。 modification log 2004-11-09,wangqiang concrete
	 *                modification:add "sxbz" as one of the items that will be
	 *                updated in this method
	 */
	/*
	 * MODIFICATION LOG wangqiang,2004-11-16 revise the sql statement:add the
	 * update column of Sxbz
	 */

	public void backNoState(AggregatedValueObject vo, String approveId,
			String approveDate, String backNote) throws java.lang.Exception {
		ExceptionHandler.debug("################back no state ####################");

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "backGoing",
				new Object[] { vo, approveId, approveDate, backNote });
		/** ********************************************************** */
		DJZBBO bo=new DJZBBO();
		try{
			DJZBHeaderVO head=(DJZBHeaderVO )vo.getParentVO();
			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 准备更新单据状态********"+head.toString()+"****************");
			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 准备更新单据状态*******"+head.getts().toString()+"****************");
		}catch(Exception e){
		}
		appdao.backNoState(vo,approveId,approveDate,backNote);
		try{
			DJZBHeaderVO head=(DJZBHeaderVO )vo.getParentVO();
			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 更新完单据状态*******"+head.toString()+"****************");
			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 更新完单据状态*******"+head.getts().toString()+"****************");
//			String ts=bo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 查询数据库中的ts*******"+ ts+"****************");
		}catch(Exception e){
		}
		new ArapInvokeSettlement().invokeCmp((DJZBVO)vo, ((DJZBHeaderVO)vo.getParentVO()).getDjrq());
		try{
			DJZBHeaderVO head=(DJZBHeaderVO )vo.getParentVO();
			Log.getInstance(this.getClass()).debug("****ApplayBillDMO6********"+head.toString()+"****************");
			Log.getInstance(this.getClass()).debug("****ApplayBillDMO6*******"+head.getts().toString()+"****************");
//			String ts=bo.getTsByPrimaryKey(head.getVouchid(), "arap_djzb");
//			Log.getInstance(this.getClass()).debug("****ApplayBillDMO 查询数据库中的ts*******"+ ts+"****************");
		}catch(Exception e){
		}
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "backGoing", new Object[] {
				vo, approveId, approveDate, backNote });
		/** ********************************************************** */
	}

	public void noState(CheckStatusCallbackContext cscc) throws BusinessException {
			if(cscc.isReject()){
				/** ********************************************************** */
				// 保留的系统管理接口：
				beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "backGoing",
						new Object[] { cscc.getBillVo(), cscc.getApproveId(), cscc.getApproveDate(), cscc.getCheckNote()});
				/** ********************************************************** */
				try {
					appdao.noState( cscc.getBillVo(), cscc.getApproveId(), cscc.getApproveDate(), cscc.getCheckNote());
					new ArapInvokeSettlement().invokeCmp( (DJZBVO)cscc.getBillVo(), ((DJZBHeaderVO)((DJZBVO)cscc.getBillVo()).getParentVO()).getDjrq());
				} catch (Exception e) {
					ExceptionHandler.consume(e);
				}
				//new ArapInvokeSettlement().invokeCmp((DJZBVO)vo, ((DJZBHeaderVO)vo.getParentVO()).getDjrq());
				/** ********************************************************** */
				// 保留的系统管理接口：
				afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "backGoing", new Object[] {
						cscc.getBillVo(), cscc.getApproveId(), cscc.getApproveDate(), cscc.getCheckNote() });
				/** ********************************************************** */
			}
	}

	public boolean checkGoing(nc.vo.pub.AggregatedValueObject vo,
			String ApproveId, String ApproveDate, String checkNote)
			throws Exception {

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "checkGoing",
				new Object[] { vo, ApproveId, ApproveDate, checkNote });
		/** ********************************************************** */
		appdao.checkGoing(vo,ApproveId,ApproveDate,checkNote);
		new ArapInvokeSettlement().invokeCmp((DJZBVO)vo, ((DJZBHeaderVO)vo.getParentVO()).getShrq());
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "checkGoing",
				new Object[] { vo, ApproveId, ApproveDate, checkNote });
		/** ********************************************************** */

		return false;
	}

	public boolean checkNoPass(nc.vo.pub.AggregatedValueObject vo,
			String ApproveId, String ApproveDate, String checkNote)
			throws Exception {

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "checkGoing",
				new Object[] { vo, ApproveId, ApproveDate, checkNote });
		/** ********************************************************** */
		appdao.checkNoPass(vo,ApproveId,ApproveDate,checkNote);
		new ArapInvokeSettlement().invokeCmp((DJZBVO)vo, ((DJZBHeaderVO)vo.getParentVO()).getDjrq());		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "checkGoing",
				new Object[] { vo, ApproveId, ApproveDate, checkNote });
		/** ********************************************************** */

		return false;

	}

	public boolean checkPass(nc.vo.pub.AggregatedValueObject vo,
			String ApproveId, String ApproveDate, String checkNote)
			throws Exception {

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "checkGoing",
				new Object[] { vo, ApproveId, ApproveDate, checkNote });
		/** ********************************************************** */
		try{
			DJZBHeaderVO head=(DJZBHeaderVO )vo.getParentVO();
			Log.getInstance(this.getClass()).error("****ApplayBillDMO1********"+head.toString()+"****************");
			Log.getInstance(this.getClass()).error("****ApplayBillDMO1*******"+head.getts().toString()+"****************");
		}catch(Exception e){
		}
		appdao.checkPass(vo,ApproveId,ApproveDate,checkNote);
		try{
			DJZBHeaderVO head=(DJZBHeaderVO )vo.getParentVO();
			Log.getInstance(this.getClass()).error("****ApplayBillDMO2********"+head.toString()+"****************");
			Log.getInstance(this.getClass()).error("****ApplayBillDMO2*******"+head.getts().toString()+"****************");
		}catch(Exception e){
		}
		new ArapInvokeSettlement().invokeCmp((DJZBVO)vo, ((DJZBHeaderVO)vo.getParentVO()).getShrq());		/** ********************************************************** */
		try{
			DJZBHeaderVO head=(DJZBHeaderVO )vo.getParentVO();
			Log.getInstance(this.getClass()).error("****ApplayBillDMO3********"+head.toString()+"****************");
			Log.getInstance(this.getClass()).error("****ApplayBillDMO3*******"+head.getts().toString()+"****************");
		}catch(Exception e){
		}
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "checkGoing",
				new Object[] { vo, ApproveId, ApproveDate, checkNote });
		/** ********************************************************** */

		return false;
	}

	/**
	 * <p>
	 * 根据表头的主键查询一个VO。
	 * <p>
	 * 创建日期：(2000-10-9)
	 *
	 * @param key
	 *            ??dbFieldType??
	 * @throws SQLException
	 * @throws BusinessException 
	 */
	public DJZBVO findByPrimaryKey(String key) throws SQLException, BusinessException {

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.DJZBDMO", "findByPrimaryKey",
				new Object[] { key });
		/** ********************************************************** */

		DJZBVO vo = new DJZBVO();
		//
		DJZBDAO djzbDMO = null;
		try{
		    djzbDMO = new DJZBDAO();
		}catch(Exception e){
		    throw new SQLException("Failt to create DJZBDMO");
		}
		DJZBHeaderVO header = djzbDMO.findHeaderByPrimaryKey(key);
		DJZBItemVO[] items = null;
		if (header != null) {
			items = djzbDMO.findItemsForHeader(header.getPrimaryKey());
		}
		//for(int i = 0;i<items.length;i++){
		////查询自由项
		//DJFBItemVO[] frees = findFREEForZb(items[i].getFb_oid());
		//items[i].items = frees;
		//}

		//cf2002-06-06 add for 存货分类主键

//		String pk_invcl = null;
//		for (int i = 0; i < items.length; i++) {
//			if (items[i].getChbm_cl() != null
//					&& items[i].getChbm_cl().trim().length() > 1) {
//				if (items[i].getPk_invcl() == null
//						|| items[i].getPk_invcl().trim().length() < 1) {
//					pk_invcl = getPk_invclByPk(items[i].getCinventoryid());
//					items[i].setPk_invcl(pk_invcl);
//
//				}
//			}
//		}
		//cf2002-06-06 end

		vo.setParentVO(header);
		vo.setChildrenVO(items);

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.DJZBDMO", "findByPrimaryKey",
				new Object[] { key });
		/** ********************************************************** */

		return vo;
	}

	/**
	 * <p>
	 * 根据表头的主键查询一个VO。
	 * <p>
	 * 创建日期：(2000-10-9)
	 *
	 * @param key
	 *            ??dbFieldType??
	 * @throws SQLException
	 * @throws DbException
	 */
	public String getPk_invclByPk(String key) throws SQLException, DbException {

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "getPk_invclByPk",
				new Object[] { key });
		/** ********************************************************** */

		BDDAO dao =new BDDAO();
		String ret=dao.getPk_invclByPk(key);
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "getPk_invclByPk",
				new Object[] { key });
		/** ********************************************************** */
		return ret;
	}

	/**
	 * 确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 * @throws DAOException
	 */
	public void qr(nc.vo.ep.dj.DJZBHeaderVO dj) throws java.sql.SQLException, DAOException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "qr", new Object[] { dj });
		/** ********************************************************** */

		appdao.qr(dj);

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "qr", new Object[] { dj });
		/** ********************************************************** */
	}

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key) throws BusinessException
			 {
		DJZBDAO djzbDMO = null;
		try{
		    djzbDMO = new DJZBDAO();
		}catch(Exception e){
		    ExceptionHandler.handleException(this.getClass(), e);
		}

		DJZBItemVO[] itemVOs =null;
		try{
		    itemVOs = djzbDMO.findItemsForHeader(key);
			ARAPDjVOUtil.supplementAreaCategory(itemVOs);
			ARAPDjVOUtil.supplementCunHuo(itemVOs);
			ARAPDjVOUtil.supplementCTZInfo(itemVOs);
		}catch (Exception e){
			ExceptionHandler.handleException(this.getClass(), e);
//		    ExceptionHandler.consume(e);
//		    throw ExceptionHandler.createException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000536")/*@res "补充单据信息失败"*/,e);
        }
		return itemVOs;
	}

	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
			throws BusinessException {
		DJZBHeaderVO[] headers = null;
		try {
			DJZBDAO djzbdmo = new DJZBDAO();
			if(null!=whereString&&whereString.trim().length()>0)
				headers = djzbdmo.queryHead("where " + whereString);
			else
				headers = djzbdmo.queryHead( whereString);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return headers;
	}

	/**
	 * 平台查询接口 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public nc.vo.pub.AggregatedValueObject queryDataByProcId(
			String billTypeOrProc, String procMsg) throws BusinessException{
	    DJZBVO djzbvo=null;
		try{
		    djzbvo= findByPrimaryKey(procMsg);
		    if(djzbvo.getTrans()!=null){
		    	djzbvo= nc.bs.arap.global.SettlementVO2DJZBVOTools.getDJZBVOBySettlementVO(djzbvo, djzbvo.getTrans().getDetailMap());
			}
			ARAPDjVOUtil.supplementAreaCategory(djzbvo);
			ARAPDjVOUtil.supplementCunHuo(djzbvo);
			ARAPDjVOUtil.supplementCTZInfo(djzbvo);
		}catch (Exception e){
			ExceptionHandler.handleException(this.getClass(), e);
//		    ExceptionHandler.consume(e);
//			throw ExceptionHandler.createException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000536")/*@res "补充单据信息失败"*/,e);
        }
		return djzbvo;

	}

	/*
	 * //平台全成本接口 billType 单据类型编码 billId 单据主键 ManaCode 全成本代码
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryDataByProcId(
			String billType, String billId, String ManaCode)
			throws BusinessException {
		try {

	        return new Costcontent().queryVO(billId,ManaCode);

//			return costDMO.queryVO(billId, ManaCode);
			//costDMO.queryVO(billId, "fb_oid", ManaCode);
		} catch (Exception e) {
//			Log.getInstance(this.getClass()).error(e.getMessage(),e);
//			throw ExceptionHandler.createException(
//					"DJZBBO::queryDataByProcId(billType,billId,ManaCode) Exception!",
//					e);

			throw ExceptionHandler.handleException(this.getClass(), e);

		}

	}

	/**
	 * 根据参数信息，获取所需要的主子表数据,其中参数billTypeOrProc为单据类型或业务处理，
	 * procMsg可以为单据的关键字，也可为单据编号，等等 创建日期：(2001-7-30 18:13:39)
	 *
	 * @return nc.vo.dap.voucher.MsgAggregatedStruct
	 * @param procMsg
	 *            java.lang.String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public nc.vo.dap.voucher.MsgAggregatedStruct[] queryDataByProcIds(
			java.lang.String billTypeOrProc, java.lang.String[] procMsg)
			throws BusinessException {
		long t = System.currentTimeMillis();
		nc.vo.dap.voucher.MsgAggregatedStruct[] res = null;
			DJZBDAO dmo = new DJZBDAO();

			Vector<MsgAggregatedStruct> v = new Vector<MsgAggregatedStruct>();
			//
			DJZBVO[] vos = null;

			try {
				if (billTypeOrProc.equalsIgnoreCase("DZ"))
				    vos = dmo.findDjByPrimaryKeys_SS(procMsg);
				else
				    vos = dmo.findDjByPrimaryKeys(procMsg);
				if(vos==null||vos.length<procMsg.length){
					throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006","UPP2006-v56-000078")/*@res "部分收付单据丢失,可能是做过数据卸载,请检查!"*/);
				}
				ARAPDjVOUtil.supplementAllInfos(vos);
				List<BusiInfo> lst=new ArrayList<BusiInfo>();
				for (int i = 0; i < vos.length; i++) {
					DJZBVO djzbvo=vos[i];
					DJZBHeaderVO head=(DJZBHeaderVO)djzbvo.getParentVO();
					if("ys".equals(head.getDjdl())||"yf".equals(head.getDjdl())){
						continue;
					}
					BusiInfo info = new BusiInfo();
					info.setBillid(((DJZBHeaderVO)djzbvo.getParentVO()).getVouchid());
					info.setBilltype(((DJZBHeaderVO)djzbvo.getParentVO()).getDjlxbm());
					info.setCorp(((DJZBHeaderVO)djzbvo.getParentVO()).getDwbm());
					lst.add(info);
				}
				Map<String,Map<String, List<SettlementBodyVO>>> detailMap = Proxy.getISettlement().queryMap(lst);
				for (int i = 0; i < vos.length; i++) {
					DJZBVO djzbvo=vos[i];
					DJZBHeaderVO head=(DJZBHeaderVO)djzbvo.getParentVO();
					if(detailMap.get(head.getVouchid())!=null){
				    	djzbvo= nc.bs.arap.global.SettlementVO2DJZBVOTools.getDJZBVOBySettlementVO(djzbvo, detailMap.get(head.getVouchid()));
					}
					nc.vo.dap.voucher.MsgAggregatedStruct m1 = new nc.vo.dap.voucher.MsgAggregatedStruct(
							djzbvo, djzbvo.getParentVO().getPrimaryKey());

					v.addElement(m1);
				}
				if (v.size() > 0) {
					res = new nc.vo.dap.voucher.MsgAggregatedStruct[v.size()];
					v.copyInto(res);
				}
			} catch (Exception e) {
				throw ExceptionHandler.handleException(this.getClass(), e);

			}

			//m= dmo.findByPrimaryKeys(billTypeOrProc, procMsg);


		ExceptionHandler.debug("收付查询所用时间 :" + (System.currentTimeMillis() - t));
		return res;

	}

	/**
	 * 审核单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[] modification log,2004-11-09 wangqiang:
	 *            add "sxbz" in the items that will be updated in this method
	 *            V31 new requirement
	 * @throws SQLException
	 * @throws DbException
	 */
	public void setFlagBill(nc.vo.ep.dj.DJZBHeaderVO dj)
			throws SQLException, DbException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "auditBill",
				new Object[] { dj });
		/** ********************************************************** */
		appdao.setFlagBill(dj);
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "auditBill",
				new Object[] { dj });
		/** ********************************************************** */
	}


	/**
	 * 审核单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 * @throws SQLException
	 * @throws DbException
	 */
	public void setFlagBill2(nc.vo.ep.dj.DJZBHeaderVO dj)
			throws SQLException, DbException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "auditBill",
				new Object[] { dj });
		/** ********************************************************** */
		appdao.setFlagBill2(dj);
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "auditBill",
				new Object[] { dj });
		/** ********************************************************** */
	}

	/**
	 * 审核单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 * @throws SQLException
	 * @throws DbException
	 */
	public void setunFlagBill(nc.vo.ep.dj.DJZBHeaderVO dj)
			throws SQLException, DbException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "auditBill",
				new Object[] { dj });
		/** ********************************************************** */
		appdao.setunFlagBill(dj);
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "auditBill",
				new Object[] { dj });
		/** ********************************************************** */
	}

	/**
	 * 反审核单据标志加并发锁 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[] 作者：陈飞
	 * @throws ArapBusinessException
	 * @throws SQLException
	 * @throws DbException
	 */
	public void setunFlagBill_distribute(nc.vo.ep.dj.DJZBHeaderVO dj)
			throws BusinessException, SQLException, DbException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO",
				"setunFlagBill_distribute", new Object[] { dj });
		/** ********************************************************** */
		appdao.setunFlagBill_distribute(dj);
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO",
				"setunFlagBill_distribute", new Object[] { dj });
		/** ********************************************************** */
	}

	/**
	 * 审核单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 */
	public String[] unAuditBill(nc.vo.ep.dj.DJZBVO[] djs)
			throws java.sql.SQLException,DbException{
		String[] result = new String[djs.length];

			/** 状态检查 */
			/** 预算 */
			/** 会计平台 */
			/** 打标志 */
			for (int i = 0; i < djs.length; i++) {
				DJZBHeaderVO header = (DJZBHeaderVO) djs[i].getParentVO();
				setunFlagBill(header);
			}

		return result;
	}

	/**
	 * 取消确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 * @throws SQLException
	 * @throws DAOException
	 */
	public void unQr(nc.vo.ep.dj.DJZBHeaderVO dj) throws DAOException, SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "unQr",
				new Object[] { dj });
		/** ********************************************************** */
		appdao.unQr(dj);

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "unQr",
				new Object[] { dj });
		/** ********************************************************** */
	}

	/**
	 * 取消签字确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param djs
	 *            nc.vo.ep.dj.DJZBVO[]
	 * @throws SQLException
	 * @throws BusinessException
	 * @throws DbException
	 */
	public void unYhqr(nc.vo.ep.dj.DJZBHeaderVO dj)
			throws SQLException, BusinessException, DbException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "unYhqr",
				new Object[] { dj });
		/** ********************************************************** */
		appdao.unYhqr(dj);
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "unYhqr",
				new Object[] { dj });
		/** ********************************************************** */
	}

	/**
	 * 签字确认单据 创建日期：(2001-9-3 17:31:51)
	 *
	 * @return java.lang.String[]
	 * @param dj
	 * @param bCanCommissioned:whether the bill can be commissioned(委托付款)
	 *            nc.vo.ep.dj.DJZBVO[]
	 * @throws SQLException
	 * @throws BusinessException
	 * @throws DbException
	 */
	public void yhqr(nc.vo.ep.dj.DJZBHeaderVO dj) throws SQLException, BusinessException, DbException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.ep.dj.ApplayBillDMO", "yhqr",
				new Object[] { dj });
		/** ********************************************************** */
		appdao.updateSignStatus(dj );

		//nc.vo.pub.BusinessException
		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.ep.dj.ApplayBillDMO", "yhqr",
				new Object[] { dj });
		/** ********************************************************** */
	}



	/**
	 * 根据主键数组更新纪录的ts,用于并发控制
	 * @throws SQLException
	 * @throws DbException

	 */
	public void updateForLockByPks(
			String sTabName, String[] pks)
			throws SQLException, DbException {
	  appdao.updateForLockByPks(sTabName,pks);
	}

	public IDataSource getPrintDs(String billId, String billtype,String strCheckman) throws BusinessException {
		// TODO Auto-generated method stub
		return new ArapBsPrDataSource().getPrintDs(billId, billtype,strCheckman);
	}

	public void going(CheckStatusCallbackContext cscc) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void noPass(CheckStatusCallbackContext cscc) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public void pass(CheckStatusCallbackContext cscc) throws BusinessException {
		// TODO Auto-generated method stub

	}

	public Object[] queryAllBillDatas(String where) throws BusinessException {
		Object[] retVos = new Object[3];
		ArrayList<DJZBItemVO> bodyvolist = new ArrayList<DJZBItemVO>();
		DJZBHeaderVO[] headerVOs = new DJZBHeaderVO[]{};
		ArrayList<String> vouchidList = new ArrayList<String>();
		try {
			where=nc.vo.jcom.lang.StringUtil.replaceIgnoreCase(where,"arap_djzb." , "zb.");
			where=nc.vo.jcom.lang.StringUtil.replaceIgnoreCase(where,"arap_djfb." , "fb.");
	    	String sqlFromClause =null;
			sqlFromClause = "  from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid where "+where+" and zb.zgyf = 0";
	    	Arap_djfbVOMeta fbMeta = new Arap_djfbVOMeta();
			String strSQL = getSelectSQL(fbMeta);
	    	strSQL+=sqlFromClause;
	    	List<DJZBItemVO> items=(List<DJZBItemVO>)new PubDAO().queryVOsBySql(DJZBItemVO.class,fbMeta,strSQL,0,5000);

	    	if(items==null || items.size()==0){
	    		return null;
	    	}
	    	for(DJZBItemVO item:items){
	    		if(!isSystemOccupation(item)){
					bodyvolist.add(item);

					if(!vouchidList.contains(item.getVouchid()))
						vouchidList.add(item.getVouchid());
				}
	    	}

	    	headerVOs = new DJZBDAO().queryHead(" where "+SqlUtils.getInStr("zb.vouchid", vouchidList.toArray(new String[]{})));

		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}

		retVos[0]=headerVOs;
		retVos[1]=bodyvolist.toArray(new DJZBItemVO[]{});

		return retVos;
	}

	public static boolean isSystemOccupation(CircularlyAccessibleValueObject object) {
		if (object instanceof DJZBItemVO) {
			DJZBItemVO voobj = (DJZBItemVO) object;
			if(voobj.getYbye()==null || voobj.getYbye().compareTo(new UFDouble(0))==0){
				return true;
			}else if(voobj.getOccupationmny()==null || voobj.getOccupationmny().compareTo(new UFDouble(0))==0 ){
				return voobj.getYbye().equals(new UFDouble(0));
			}else {
				if(voobj.getYbye().multiply(voobj.getOccupationmny()).compareTo(new UFDouble(0))<0)
					return true;
				else if(voobj.getYbye().abs().compareTo(voobj.getOccupationmny().abs())<=0)
					return true;
				else {
					voobj.setYbye(voobj.getYbye().sub(voobj.getOccupationmny()));
					return false;
				}
			}
		}

		return false;
	}

	private String getSelectSQL(Arap_djfbVOMeta meta) {
		StringBuffer buf=new StringBuffer("SELECT DISTINCT fb.").append("fb_oid");
        for(int i=0,size=meta.getColumns().length;i<size;i++)
            if(!"fb_oid".equalsIgnoreCase(meta.getColumns()[i]))
                buf.append(", fb.").append(meta.getColumns()[i]);
        return buf.toString();
	}


	/**
	 * add by ouyangzhb 2013-10-23 增加参照时的表体过滤条件 实现接口：nc.bs.pub.pf.IQueryData2
	 * 
	 */
	public CircularlyAccessibleValueObject[] queryAllBodyData(String key,
			String bodyCondition) throws BusinessException {
		DJZBDAO djzbDMO = null;
		try {
			djzbDMO = new DJZBDAO();
		} catch (Exception e) {
			ExceptionHandler.handleException(this.getClass(), e);
		}

		DJZBItemVO[] itemVOs = null;
		try {
			Vector v = djzbDMO.findItemsByCondition(" where dr=0 and xgbh ="
					+ ArapConstant.UNITACCOUNTSTAT_DEFAULT + " and vouchid = '"
					+ key + "' " + bodyCondition + "");
			// Vector v = findItemsByCondition(
			// " where dr=0 and vouchid = '"+key+"'");
			if (v.size() > 0) {
				itemVOs = new DJZBItemVO[v.size()];
				v.copyInto(itemVOs);
			}
			ARAPDjVOUtil.supplementAreaCategory(itemVOs);
			ARAPDjVOUtil.supplementCunHuo(itemVOs);
			ARAPDjVOUtil.supplementCTZInfo(itemVOs);
		} catch (Exception e) {
			ExceptionHandler.handleException(this.getClass(), e);
			// ExceptionHandler.consume(e);
			// throw
			// ExceptionHandler.createException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("2006030102","UPP2006030102-000536")/*@res
			// "补充单据信息失败"*/,e);
		}
		return itemVOs;
	}
	
}