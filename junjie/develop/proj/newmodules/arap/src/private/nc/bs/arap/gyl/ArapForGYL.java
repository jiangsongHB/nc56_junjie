/*
 * 创建日期 2005-12-8
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.bs.arap.gyl;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;

import sun.security.jca.GetInstance;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.arap.change.PubchangeBO;
import nc.bs.arap.global.CurrencyControlBO;
import nc.bs.arap.util.SqlUtils;
import nc.bs.arap.verify.DJCLBDMO;
import nc.bs.arap.verify.SystemProfile;
import nc.bs.arap.verifynew.VerifyBO;
import nc.bs.arap.verifynew.VerifyServiceDMO;
import nc.bs.ep.dj.ARAPDjAlienBill;
import nc.bs.ep.dj.DJZBBO;
import nc.bs.ep.dj.DJZBDAO;
import nc.bs.ep.dj.ModifyEffectBill;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Log;
import nc.bs.pub.SystemException;
import nc.cmp.pub.cache.FiPubDataCache;
import nc.impl.arap.proxy.ProxyBill;
import nc.itf.fi.pub.Accperiod;
import nc.itf.fi.pub.Currency;
import nc.itf.fi.pub.SysInit;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.arap.djlx.DjLXVO;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.arap.global.ArapCommonTool;
import nc.vo.arap.global.ArapDjCalculator;
import nc.vo.arap.global.DjVOTreaterAid;
import nc.vo.arap.global.ResMessage;
import nc.vo.arap.gyl.AdjuestVO;
import nc.vo.arap.gyl.VerifyParamVO;
import nc.vo.arap.pub.ArapConstant;
import nc.vo.arap.transaction.AccountInfo;
import nc.vo.arap.verify.DJCLBVO;
import nc.vo.arap.verifynew.Saver;
import nc.vo.arap.verifynew.VerifyFilter;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ep.dj.DJZBVOConsts;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.verifynew.pub.ConditionVO;
import nc.vo.verifynew.pub.ConditionVOSqlTool;
import nc.vo.verifynew.pub.DefaultVerifyRuleVO;
import nc.vo.verifynew.pub.FenPeiUtil;
import nc.vo.verifynew.pub.ScriptVO;
import nc.vo.verifynew.pub.VerifyCom;
import nc.vo.verifynew.pub.VerifyLogVO;
import nc.vo.verifynew.pub.VerifyVO;

/**
 * @author xuhb
 * 
 *         TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class ArapForGYL {

	/**
  *
  */
	public ArapForGYL() {
		super();
		// TODO 自动生成构造函数存根
	}

	public String getYeByZhangLing(String startdate, String enddate,
			String line, String ordercusmandoc, String pk_timecontrol,
			String pk_corp, int iArBillStat, int iAgBillStat)
			throws BusinessException {
		// TODO 自动生成方法存根
		String table = null;
		try {
			ArapForGYLDMO dmo = new ArapForGYLDMO();
			table = dmo.getBuleYSByZL(startdate, enddate, line, ordercusmandoc,
					pk_timecontrol, pk_corp, iArBillStat);
			dmo.getRedandSKByZL(startdate, enddate, line, ordercusmandoc,
					pk_timecontrol, pk_corp, table, iArBillStat, iAgBillStat);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return table;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.itf.arap.pub.IArapForGYLPublic#getYeByZhangQi(java.lang.String,
	 * java.lang.String)
	 */
	public UFDouble[] getYeByZhangQi(String where, String date, String pk_corp,
			int iArBillStat, int iAgBillStat) throws BusinessException {
		// TODO 自动生成方法存根
		UFDouble[] result = new UFDouble[3];
		try {
			ArapForGYLDMO dmo = new ArapForGYLDMO();
			result = dmo.getBuleYSByZQ(where, date, pk_corp, iArBillStat);
			UFDouble[] temp = dmo.getRedandSKByZQ(where, pk_corp, iArBillStat,
					iAgBillStat);
			for (int i = 0; i < 3; i++) {
				result[i] = result[i].add(temp[i]);
			}
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return result;
	}

	/**
	 * 暂估回冲调差.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void Adjuest(Hashtable adjuest, String clr, String clrq,
			String pk_corp, int lylx, int ly) throws BusinessException {

		Log.getInstance(this.getClass()).error(
				"@@@@@@@@adjuest size:" + adjuest.size());
		Set key = adjuest.keySet();
		Iterator it = key.iterator();
		while (it.hasNext()) {

			String clbh = (String) it.next();
			AdjuestVO[] vo = (AdjuestVO[]) adjuest.get(clbh);

			Log.getInstance(this.getClass()).error(
					"@@@@@@@@adjuest clbh:" + clbh);

			Log.getInstance(this.getClass()).error(
					"@@@@@@@@adjuest AdjuestVO volength:" + vo.length);
			this.Adjuest(vo, clbh, clr, clrq, pk_corp, lylx, ly);
		}
	}

	/**
	 * 暂估回冲调差.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void Adjuest(AdjuestVO[] vo, String clbh, String clr, String clrq,
			String pk_corp, int lylx, int ly) throws BusinessException {

		Log.getInstance(this.getClass()).error(
				"@@@@@@@@adjuest AdjuestVO volength:" + vo.length);

		SystemProfile.getInstance().log("ly" + ly);

		if (ly == 0)// 销售
		{
			adjuestVos(vo, clbh, clr, clrq, pk_corp, lylx, ly);
		} else if (ly == 2) {// 内部交易
			Map<String, AdjuestVO> lst = new HashMap<String, AdjuestVO>();
			AdjuestVO avo;
			for (int i = 0; i < vo.length; i++) {
				if (!lst.containsKey(vo[i].getDdhh())) {
					lst.put(vo[i].getDdhh(), vo[i]);
				} else {
					avo = lst.get(vo[i].getDdhh());
					avo.setShl(avo.getShl().add(vo[i].getShl()));
					// avo.setIsdone(avo.getIsdone().equals(vo[i].getIsdone())?avo.getIsdone():UFBoolean.TRUE);
				}
			}
			adjuestVos(lst.values().toArray(new AdjuestVO[] {}), clbh, clr,
					clrq, pk_corp, lylx, ly);

		} else // 采购//TODO 和
		{
			Map<String, AdjuestVO> lst = new HashMap<String, AdjuestVO>();
			AdjuestVO avo;
			for (int i = 0; i < vo.length; i++) {
				if (!lst.containsKey(vo[i].getDdhh())) {
					lst.put(vo[i].getDdhh(), vo[i]);
				} else {
					avo = lst.get(vo[i].getDdhh());
					avo.setShl(avo.getShl().add(vo[i].getShl()));
					avo
							.setIsdone(avo.getIsdone()
									.equals(vo[i].getIsdone()) ? avo
									.getIsdone() : UFBoolean.TRUE);
				}
			}
			adjuestVos(lst.values().toArray(new AdjuestVO[] {}), clbh, clr,
					clrq, pk_corp, lylx, ly);
		}
	}

	private void adjuestVos(AdjuestVO[] vo, String clbh, String clr,
			String clrq, String pk_corp, int lylx, int ly)
			throws BusinessException {
		try {
			UFBoolean RB = null;
			DJZBVO zb = new DJZBVO();
			DJZBHeaderVO header = null;

			Vector<DJZBItemVO> items = new Vector<DJZBItemVO>();
			String bz = null;

			Map<String, DJZBVO[]> cache = new HashMap<String, DJZBVO[]>();
			List<DJZBVO> lst = new ArrayList<DJZBVO>();
			UFDouble ybje = new UFDouble(0);

			SystemProfile.getInstance().log(
					"arapforgyl adjuest begin vo.length" + vo.length);

			List<String> keys = new ArrayList<String>();
			for (int i = 0; i < vo.length; i++) {
				if (vo[i].getDdhh() != null)
					keys.add(vo[i].getDdhh());
			}

			if (lylx == 0) {// 订单行id//通过订单暂沽，暂时不会执行此分支
				cache = new DJZBBO().getDJByXXID("ddhid", keys
						.toArray(new String[] {}), ly);
			} else if (lylx == 1) {// 出库单行id
				cache = new DJZBBO().getDJByXXID("ckdid", keys
						.toArray(new String[] {}), ly);
			} else if (lylx == 2) {// 发票行id////通过发票暂沽，暂时不会执行此分支
				cache = new DJZBBO().getDJByXXID("fphid", keys
						.toArray(new String[] {}), ly);
			}
			//add by ouyangzhb 为lylx为3 时 设置查询条件
		else if (lylx == 3) {// 发票行id////通过发票暂沽，暂时不会执行此分支
			cache = new DJZBBO().getDJByXXID("fb_oid", keys
					.toArray(new String[] {}), ly);
		}
			SystemProfile.getInstance().log("arapforgyl adjuest query end");

			for (int i = 0; i < vo.length; i++)// 对应的每个行id生成一个djzbvo，然后将所有的djzbvo合并成一个vo
			{
				DJZBVO[] djvo = null;
				Hashtable temp = null;

				if (cache.get(vo[i].getDdhh()) != null) {
					djvo = cache.get(vo[i].getDdhh());
				}

				if (djvo == null || djvo.length == 0)
					continue;

				SystemProfile.getInstance().log("lylx" + lylx);
				SystemProfile.getInstance().log("djvo.length" + djvo.length);

				List<DJZBVO> djzbVos = new ArrayList<DJZBVO>();

				for (int j = 0; j < djvo.length; j++) {
					if (djvo[j] != null) {
						djzbVos.add(djvo[j]);
						int zgyf = djvo[j].header.getZgyf().intValue();
						int sxbz = djvo[j].header.getSxbz().intValue();
						if ((zgyf == DJZBVOConsts.ZGYF_ZG || zgyf == DJZBVOConsts.ZGYF_HC)
								&& sxbz != DJZBVOConsts.m_intSXBZ_VALID) // 存在未生效的暂估/回冲单据
						{
							throw ExceptionHandler
									.createException(NCLangRes4VoTransl
											.getNCLangRes().getStrByID("2006",
													"UPP2006-v51-000292")/*
																		 * @res
																		 * "存在未生效暂估/回冲单据,无法进行回冲!"
																		 */);
						}
					}
				}

				djvo = (DJZBVO[]) djzbVos.toArray(new DJZBVO[] {});

				if (djvo.length == 0)
					continue;

				bz = ((DJZBItemVO) djvo[0].getChildrenVO()[0]).getBzbm();
				
				/**add by ouyangzhb 2013-10-21 */
				if(lylx == 3){
					temp = this.getNewDJForZGYF(djvo, vo[i], clbh, clrq, ly, pk_corp);
				}else{
					temp = this.getNewDJ(djvo, vo[i], clbh, clrq, ly, pk_corp);
				}
//				temp = this.getNewDJ(djvo, vo[i], clbh, clrq, ly, pk_corp);
				

				if (temp == null) {
					continue;
				}

				SystemProfile.getInstance().log("temp.length" + temp.size());

				Set key = temp.keySet();
				Iterator it = key.iterator();
				while (it.hasNext()) {
					DJZBVO zbvo = (DJZBVO) it.next();
					lst.add(zbvo);
					UFBoolean isRB = (UFBoolean) temp.get(zbvo);
					RB = isRB;
					header = (DJZBHeaderVO) zbvo.getParentVO();
					for (int j = 0; j < zbvo.getChildrenVO().length; j++) {
						items.add((DJZBItemVO) zbvo.getChildrenVO()[j]);
						ybje = ybje.add(((DJZBItemVO) zbvo.getChildrenVO()[j])
								.getYbye());
					}
				}

			}
			
			/**add by ouyangzhb 2013-10-21 暂估应付的红冲方式（不需要分单）*/
			DJZBVO[] vos  = null;
			if(lylx == 3){
				vos = seperateBillsForZGYF(lst, pk_corp, clrq); 
			}else{
				vos = seperateBills(lst, pk_corp, clrq);
			}
//			DJZBVO[] vos = seperateBills(lst, pk_corp, clrq);
			/**add by ouyangzhb 2013-10-21 暂估应付的红冲方式（不需要分单）*/

			if (vos == null || vos.length == 0) {
				return;
			}
			UFDouble fbhl = items.size() > 0 && items.get(0) != null ? items
					.get(0).getFbhl() : ArapConstant.DOUBLE_ZERO;
			UFDouble bbhl = items.size() > 0 && items.get(0) != null ? items
					.get(0).getBbhl() : ArapConstant.DOUBLE_ZERO;
			for (DJZBVO dj : vos) {
				SystemProfile.getInstance().log("saveEff" + dj);
				this.saveEff(dj);
			}

			if (RB.booleanValue())// 不需要红冲
			{
				SystemProfile.getInstance().log("realVerify");
				verify(vo, clbh, clr, clrq, pk_corp, lylx, ly, bz, ybje, fbhl,
						bbhl);
			}
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
	}
	
	
	/***
	 * add by ouyangzhb 2013-10-21 复制原来的方法，修改重算金额的方式：按数量
	 * @param vo
	 * @param adjust
	 * @param clbh
	 * @param clrq
	 * @param ly
	 * @param pk_corp
	 * @return
	 * @throws Exception
	 */
	private Hashtable getNewDJForZGYF(DJZBVO[] vo, AdjuestVO adjust, String clbh,
			String clrq, int ly, String pk_corp) throws Exception {
		Hashtable<DJZBVO, UFBoolean> result = new Hashtable<DJZBVO, UFBoolean>();
		DJZBVO zbvo = new DJZBVO();
		DJZBItemVO[] temp = null;
		UFDouble shlye = new UFDouble(0);
		UFDouble shlje = new UFDouble(0); // 合计数量金额

		DJZBVO source = null;
		Vector<DJZBItemVO> allitem = new Vector<DJZBItemVO>();
		for (int i = 0; i < vo.length; i++) {
			if (vo[i] == null)
				continue;
			DJZBHeaderVO header = (DJZBHeaderVO) vo[i].getParentVO();
			DJZBItemVO[] items = (DJZBItemVO[]) vo[i].getChildrenVO();
			if (header.getZgyf().intValue() == 1) {
				SystemProfile.getInstance().log(
						"source" + vo[i].getParentVO().getPrimaryKey());

				source = vo[i];
			}
			for (int j = 0; j < items.length; j++) {
				DJZBItemVO zbItem = items[j];
				shlye = shlye.add(zbItem.getShlye());
				shlje = shlje.add(zbItem.getDfshl().add(zbItem.getJfshl()));
				zbItem.setIsverifyfinished(new UFBoolean(false));
				zbItem.setVerifyfinisheddate(null);
				allitem.add(zbItem);
			}
		}
		if (null == source) {
			throw ExceptionHandler
					.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("2006", "UPP2006-v55-000136")/*
																	 * @res
																	 * "没有对应的暂估单据!"
																	 */);
		}
		// boolean
		// hastax=!ArapCommonTool.isZero(((DJZBItemVO)source.getChildrenVO()[0]).getJfybsj().add(((DJZBItemVO)source.getChildrenVO()[0]).getDfybsj()));
		DJZBItemVO[] temp1 = allitem.toArray(new DJZBItemVO[] {});
		Arrays.sort(temp1, new Comparator<Object>() {
			public int compare(Object a, Object b) {
				if (ArapCommonTool.isLarge(((DJZBItemVO) a).getShlye().abs(),
						((DJZBItemVO) b).getShlye().abs()))
					return 1;
				else if (((DJZBItemVO) a).getShlye().abs().equals(
						((DJZBItemVO) b).getShlye().abs()))
					return 0;
				else
					return -1;
			}
		});
		allitem = new Vector<DJZBItemVO>(Arrays.asList(temp1));
		if (adjust.getIsdone().booleanValue()
				|| ArapCommonTool.isEqual(adjust.getShl(), shlye)
				|| ArapCommonTool.isEqual(shlje, adjust.getShl()))// 最后一次调整
		{
			SystemProfile.getInstance().log(
					"adjust.getIsdone()" + adjust.getIsdone());
			SystemProfile.getInstance()
					.log("adjust.getShl()" + adjust.getShl());
			SystemProfile.getInstance().log("shlye" + shlye);
			SystemProfile.getInstance().log("shlje" + shlje);

			if (ArapCommonTool.isZero(shlye)) {
				return null;
			}
			Vector<DJZBItemVO> con = new Vector<DJZBItemVO>();
			DJZBItemVO[] item = new DJZBItemVO[allitem.size()];
			allitem.copyInto(item);
			UFDouble ybje = new UFDouble(0);
			UFDouble fbje = new UFDouble(0);
			UFDouble bbje = new UFDouble(0);
			UFDouble sumYE = ArapCommonTool.ZERO;
			for (int i = 0; i < item.length; i++) {

				if (ArapCommonTool.isZero(item[i].getShlye())
						|| ArapCommonTool.isZero(item[i].getYbye()))
					continue;

				DJZBItemVO newItem = (DJZBItemVO) item[i].clone();
				UFDouble sl = newItem.getSl();
				if (ArapCommonTool.isZero(newItem.getJfybsj().add(
						newItem.getDfybsj()))) {
					newItem.setSl(ArapCommonTool.ZERO);
				}
				con.add(newItem);
				newItem.setClbh(clbh);
				newItem.setJsfsbm(((DJZBHeaderVO) source.getParentVO())
						.getDjlxbm());
				newItem.setDdhh(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getFb_oid());
				newItem.setDdlx(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getVouchid());
				newItem.setDdhid(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getVouchid());
				DJZBHeaderVO head = (DJZBHeaderVO) source.getParentVO();
					if (i < item.length - 1) {
						newItem.setDfshl(newItem.getShlye().multiply(
								ArapConstant.INT_NEGATIVE_ONE));
						sumYE = sumYE.add(newItem.getDfshl());

					} else {
						newItem.setDfshl(adjust.getShl().multiply(
								ArapConstant.INT_NEGATIVE_ONE).sub(sumYE));
					}
					//newItem.setYbye(newItem.getDfshl().multiply(newItem.getDj()));
					newItem.setYbye(newItem.getDfshl().multiply(newItem.getHsdj()));  //wanglei 2014-04-23 根据含税单价计算余额
					newItem.setDj(null);
					newItem.setHsdj(null);
					newItem.setDfybje(newItem.getYbye());
					newItem.setDffbje(newItem.getFbye() == null ? null
							: newItem.getFbye());
					newItem.setDfbbje(newItem.getBbye());
					newItem = ArapDjCalculator.getInstance().calculateVO(
							newItem, "dfybje", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					newItem.setYbye(newItem.getDfybje());
					newItem.setFbye(newItem.getDffbje());
					newItem.setBbye(newItem.getDfbbje());
					newItem.setShlye(newItem.getDfshl());
				newItem.setSl(sl);
				ybje.add(newItem.getYbye());
				fbje.add(newItem.getFbye());
				bbje.add(newItem.getBbye());

			}
			temp = new DJZBItemVO[con.size()];
			con.copyInto(temp);
			DJZBHeaderVO header = (DJZBHeaderVO) source.getParentVO().clone();
			header.setDjbh(null);
			header.setZgyf(new Integer(2));
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
			zbvo.setParentVO(header);
			zbvo.setChildrenVO(temp);
			result.put(zbvo, new UFBoolean(true));// 生成的DJZBVO为key，是否需要红冲为value
		} else {
			UFDouble tzshl = adjust.getShl().multiply(-1);// 调整数量

			SystemProfile.getInstance().log("tzshl" + tzshl);
			SystemProfile.getInstance().log("shlye" + shlye);

			if (ArapCommonTool.isZero(tzshl)) {
				return null;
			} else if (ArapCommonTool.isLargeEqual(tzshl.multiply(shlye),
					new UFDouble(0)))// 同号
			{

				if (ArapCommonTool.isZero(shlye)) {
					if (null != adjust.getIsSO()
							&& adjust.getIsSO().booleanValue()) {
						UFDouble t = adjust.getTShl();
						if (t == null)
							t = new UFDouble(0.0d);
						if (tzshl.abs().compareTo(t.abs()) > 0) {
							tzshl = t;
						}
					}
				}
				temp = new DJZBItemVO[1];
				temp[0] = (DJZBItemVO) ((DJZBItemVO) source.getChildrenVO()[0])
						.clone();
				temp[0].setDdhh(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getFb_oid());
				temp[0].setDdlx(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getVouchid());
				temp[0].setClbh(clbh);
				temp[0].setJsfsbm(((DJZBHeaderVO) source.getParentVO())
						.getDjlxbm());
				temp[0].setShlye(tzshl);
				temp[0].setIsverifyfinished(new UFBoolean(false));
				temp[0].setVerifyfinisheddate(null);
				DJZBHeaderVO head = (DJZBHeaderVO) source.getParentVO();
				UFDouble sl = temp[0].getSl();
				if (ArapCommonTool.isZero(temp[0].getJfybsj().add(
						temp[0].getDfybsj()))) {
					temp[0].setSl(ArapCommonTool.ZERO);
				}
				if (ly == 0 || (ly == 2 && "ys".equals(head.getDjdl())))// 销售
				{
					temp[0].setJfshl(tzshl);
					temp[0] = ArapDjCalculator.getInstance().calculateVO(
							temp[0], "jfshl", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					temp[0].setYbye(temp[0].getJfybje());
					temp[0].setFbye(temp[0].getJffbje());
					temp[0].setBbye(temp[0].getJfbbje());
				} else {
					temp[0].setDfshl(tzshl);
					temp[0] = ArapDjCalculator.getInstance().calculateVO(
							temp[0], "dfshl", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					temp[0].setYbye(temp[0].getDfybje());
					temp[0].setFbye(temp[0].getDffbje());
					temp[0].setBbye(temp[0].getDfbbje());
				}
				temp[0].setSl(sl);

				DJZBHeaderVO header = (DJZBHeaderVO) source.getParentVO()
						.clone();
				header.setDjbh(null);
				header.setZgyf(new Integer(2));
				header.setYbje(temp[0].getYbye());
				header.setFbje(temp[0].getFbye());
				header.setBbje(temp[0].getBbye());
				zbvo.setParentVO(header);
				zbvo.setChildrenVO(temp);
				result.put(zbvo, new UFBoolean(false));// 生成的DJZBVO为key，是否需要红冲为value

			} else// 异号，需要红冲
			{
				verify(adjust, clbh, clrq, ly, pk_corp, result, zbvo, shlye,
						source, allitem, tzshl);

				SystemProfile.getInstance().log("verify");

			}
		}

		return result;
	}
	
	

	/***
	 * ADD BY OUYANGZHB 2013-10-21 如果是暂估应付的，不需要分单
	 * @param bills
	 * @param corpID
	 * @param clrq
	 * @return
	 * @throws Exception
	 */
	private DJZBVO[] seperateBillsForZGYF(List<DJZBVO> bills, String corpID,
			String clrq) throws Exception {
		if (null == bills || bills.size() == 0)
			return null;
		
		
		//不需要分单
		DJZBVO[] apVOs = new DJZBVO[bills.size()];
		bills.toArray(apVOs);
		// 分单
//		DJZBVO[] ret = (DJZBVO[]) getSplitVOs(DJZBVO.class.getName(),   //wanglei 2013-12-20 不分单还调用他干嘛！
//				DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName(),
//				apVOs, new String[] { }, new String[] { "ckdh" });
		
		DJZBVO[] ret = apVOs;
		//
		for (int i = 0; i < ret.length; i++) {
			DJZBVO zb = ret[i];
			DJZBHeaderVO header = (DJZBHeaderVO) zb.getParentVO();
			UFDouble ybje = new UFDouble(0);
			UFDouble fbje = new UFDouble(0);
			UFDouble bbje = new UFDouble(0);
			for (int k = 0; k < zb.getChildrenVO().length; k++)// 计算表投金额
			{
				DJZBItemVO item = (DJZBItemVO) zb.getChildrenVO()[k];
				ybje = ybje.add(item.getYbye());
				fbje = fbje.add(item.getFbye());
				bbje = bbje.add(item.getBbye());
			}
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
		}
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(new UFDate(clrq));
		nc.vo.bd.period.AccperiodVO accperiod = calendar.getYearVO();
		accperiod.setVosMonth(new AccperiodmonthVO[] { calendar.getMonthVO() });
		for (int i = 0; i < ret.length; i++) {
			DJZBVO zb = ret[i];
			DJZBHeaderVO header = (DJZBHeaderVO) zb.getParentVO();
			UFDouble ybje = new UFDouble(0);
			UFDouble fbje = new UFDouble(0);
			UFDouble bbje = new UFDouble(0);
			for (int k = 0; k < zb.getChildrenVO().length; k++)// 计算表投金额
			{
				DJZBItemVO item = (DJZBItemVO) zb.getChildrenVO()[k];
				ybje = ybje.add(item.getYbye());
				fbje = fbje.add(item.getFbye());
				bbje = bbje.add(item.getBbye());
				item.setDjbh(null);
			}
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
			header.setDjbh(null);
			header.setZgyf(new Integer(2));
			header.setDjrq(new UFDate(clrq));
			header.setDjkjnd(accperiod.getPeriodyear()); // 单据会计年度
			header.setDjkjqj(accperiod.getVosMonth()[0].getMonth()); // 单据会计期间
			header.setQcbz(UFBoolean.FALSE);
		}
		return ret;
	}
	
	
	
	private DJZBVO[] seperateBills(List<DJZBVO> bills, String corpID,
			String clrq) throws Exception {
		if (null == bills || bills.size() == 0)
			return null;
		DJZBVO[] ret = getSplittedBillVos(bills.toArray(new DJZBVO[] {}),
				corpID);
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(new UFDate(clrq));
		nc.vo.bd.period.AccperiodVO accperiod = calendar.getYearVO();
		accperiod.setVosMonth(new AccperiodmonthVO[] { calendar.getMonthVO() });
		for (int i = 0; i < ret.length; i++) {
			DJZBVO zb = ret[i];
			DJZBHeaderVO header = (DJZBHeaderVO) zb.getParentVO();
			UFDouble ybje = new UFDouble(0);
			UFDouble fbje = new UFDouble(0);
			UFDouble bbje = new UFDouble(0);
			for (int k = 0; k < zb.getChildrenVO().length; k++)// 计算表投金额
			{
				DJZBItemVO item = (DJZBItemVO) zb.getChildrenVO()[k];
				ybje = ybje.add(item.getYbye());
				fbje = fbje.add(item.getFbye());
				bbje = bbje.add(item.getBbye());
				item.setDjbh(null);
			}
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
			header.setDjbh(null);
			header.setZgyf(new Integer(2));
			header.setDjrq(new UFDate(clrq));
			header.setDjkjnd(accperiod.getPeriodyear()); // 单据会计年度
			header.setDjkjqj(accperiod.getVosMonth()[0].getMonth()); // 单据会计期间
			header.setQcbz(UFBoolean.FALSE);
		}
		return ret;
	}

	

	/**
	 * 分单处理。
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param voaSrc
	 * @return <p>
	 * @author czp
	 * @time 2007-9-17 下午12:29:52
	 */
	public DJZBVO[] getSplittedBillVos(DJZBVO[] apVOs, String strLoginCorp)
			throws Exception {

		// 分单参数
		ISysInitQry qrySrv = (ISysInitQry) NCLocator.getInstance().lookup(
				ISysInitQry.class.getName());
		String strSplitPara = qrySrv.getParaString(strLoginCorp, "AP31");

		// 单据号、业务流程、业务流程+供应商

		// 不分单，表头任意取(目前取第1张单据)，表体全部组合到一起
		if (strSplitPara == null || "不分单".equals(strSplitPara)) {/*
																 * -=notranslate=
																 * -
																 */
			ArrayList<DJZBItemVO> listBodyAll = new ArrayList<DJZBItemVO>();
			int iLenApVos = apVOs.length;
			DJZBItemVO[] items = null;
			for (int i = 0; i < iLenApVos; i++) {
				items = (DJZBItemVO[]) apVOs[i].getChildrenVO();
				for (DJZBItemVO item : items) {
					listBodyAll.add(item);
				}
			}
			//
			DJZBVO voAllBodies = new DJZBVO();
			voAllBodies.setParentVO(apVOs[0].getParentVO());
			voAllBodies.setChildrenVO(listBodyAll
					.toArray(new DJZBItemVO[listBodyAll.size()]));
			//
			return new DJZBVO[] { voAllBodies };
		}

		// 分单参数处理
		String[] saHSplit = new String[] {};
		String[] saBSplit = new String[] {};

		if ("单据号".equals(strSplitPara)) {/* -=notranslate=- */
			saBSplit = new String[] { "ckdh" };
		} else if ("业务流程+供应商".equals(strSplitPara)) {/* -=notranslate=- */
			saHSplit = new String[] { "xslxbm" };
			saBSplit = new String[] { "hbbm" };
		} else if ("业务流程".equals(strSplitPara)) {/* -=notranslate=- */
			saHSplit = new String[] { "xslxbm" };
		}
		// 分单
		DJZBVO[] voaRet = (DJZBVO[]) getSplitVOs(DJZBVO.class.getName(),
				DJZBHeaderVO.class.getName(), DJZBItemVO.class.getName(),
				apVOs, saHSplit, saBSplit);
		//
		for (int i = 0; i < voaRet.length; i++) {
			DJZBVO zb = voaRet[i];
			DJZBHeaderVO header = (DJZBHeaderVO) zb.getParentVO();
			UFDouble ybje = new UFDouble(0);
			UFDouble fbje = new UFDouble(0);
			UFDouble bbje = new UFDouble(0);
			for (int k = 0; k < zb.getChildrenVO().length; k++)// 计算表投金额
			{
				DJZBItemVO item = (DJZBItemVO) zb.getChildrenVO()[k];
				ybje = ybje.add(item.getYbye());
				fbje = fbje.add(item.getFbye());
				bbje = bbje.add(item.getBbye());
			}
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
		}
		return voaRet;
	}

	public static AggregatedValueObject[] getSplitVOs(String voName,
			String headVOName, String bodyVOName, AggregatedValueObject[] VOs,
			String[] headKeys, String[] bodyKeys) {

		try {

			String headkey = null;
			String bodykey = "";
			Vector vecHeadKey = new Vector();

			java.util.Hashtable hashtableH = new java.util.Hashtable();
			java.util.Hashtable hashtableB = new java.util.Hashtable();

			for (int i = 0; i < VOs.length; i++) {

				headkey = "";

				AggregatedValueObject oneVO = VOs[i];
				CircularlyAccessibleValueObject headVO = oneVO.getParentVO();
				for (int k = 0; k < headKeys.length; k++) {
					headkey += headVO.getAttributeValue(headKeys[k]);
				}

				CircularlyAccessibleValueObject newHeadVO = (CircularlyAccessibleValueObject) headVO
						.clone();
				for (int n = 0; n < headKeys.length; n++)
					newHeadVO.setAttributeValue(headKeys[n], headVO
							.getAttributeValue(headKeys[n]));
				hashtableH.put(headkey, newHeadVO);

				CircularlyAccessibleValueObject[] itemVO = oneVO
						.getChildrenVO();
				for (int j = 0; j < itemVO.length; j++) {
					bodykey = headkey;
					CircularlyAccessibleValueObject rowVO = itemVO[j];
					for (int l = 0; l < bodyKeys.length; l++) {
						bodykey = bodykey
								+ rowVO.getAttributeValue(bodyKeys[l]);
					}
					Vector bodyVec = (Vector) hashtableB.get(bodykey);
					if (bodyVec == null) {
						bodyVec = new Vector();
						hashtableB.put(bodykey, bodyVec);
						bodyVec.addElement(rowVO);
						vecHeadKey.addElement(headkey);
					} else
						bodyVec.addElement(rowVO);
				}
			}
			Vector vecBodyKey = new Vector();
			java.util.Enumeration enumKey = hashtableB.keys();
			while (enumKey.hasMoreElements()) {
				vecBodyKey.addElement(enumKey.nextElement());
			}
			//

			Class bodyVOClass = Class.forName(bodyVOName);
			Class VOClass = Class.forName(voName);

			AggregatedValueObject[] returnVO = (AggregatedValueObject[]) Array
					.newInstance(VOClass, vecBodyKey.size());
			for (int i = 0; i < vecBodyKey.size(); i++) {
				returnVO[i] = (AggregatedValueObject) Class.forName(voName)
						.newInstance();
				//
				CircularlyAccessibleValueObject headVO = (CircularlyAccessibleValueObject) hashtableH
						.get(vecHeadKey.elementAt(i).toString());
				returnVO[i].setParentVO(headVO);
				//
				Vector vec = (Vector) hashtableB.get(vecBodyKey.elementAt(i)
						.toString());
				CircularlyAccessibleValueObject[] bodyVO = (CircularlyAccessibleValueObject[]) Array
						.newInstance(bodyVOClass, vec.size());
				vec.copyInto(bodyVO);
				returnVO[i].setChildrenVO(bodyVO);

			}
			return returnVO;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (InstantiationException t) {
			return null;
		} catch (IllegalAccessException r) {
			return null;
		}

	}

	/**
	 * 暂估回冲核销.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	private void verify(AdjuestVO[] vo, String clbh, String clr, String clrq,
			String pk_corp, int lylx, int ly, String bz, UFDouble ybje,
			UFDouble fbhl, UFDouble bbhl) throws BusinessException {
		try {
			Hashtable<String, VerifyVO[]> vos = this.getVerifyVO(vo, lylx);
			String HxMode = null;
			Integer m_hxSeq = null;
			if (ly == 0 || ly == 2)// 销售
			{
				HxMode = SysInit.getParaString(pk_corp, "AR1");

				if (HxMode.equals("最早余额法")) {/* -=notranslate=- */
					m_hxSeq = new Integer(0);
				} else {
					m_hxSeq = new Integer(1);
				}
				this.sortVerifyVO(vos, m_hxSeq.intValue());
			} else// 采购
			{
				HxMode = SysInit.getParaString(pk_corp, "AP1");

				if (HxMode.equals("最早余额法")) {/* -=notranslate=- */
					m_hxSeq = new Integer(0);
				} else {
					m_hxSeq = new Integer(1);
				}
				this.sortVerifyVO(vos, m_hxSeq.intValue());
			}

			AccperiodVO accperiodvo = Accperiod
					.queryYearAndMonthByDate(new UFDate(clrq));
			VerifyCom com = new VerifyCom(new VerifyFilter(), new Saver(), null);
			ArrayList<VerifyLogVO> m_logs = new ArrayList<VerifyLogVO>();

			Set key = vos.keySet();
			Iterator itr = key.iterator();
			while (itr.hasNext()) {
				String ddhh = (String) itr.next();
				VerifyVO[] jd = (VerifyVO[]) vos.get(ddhh);
				DefaultVerifyRuleVO rb = null;
				rb = new DefaultVerifyRuleVO();
				rb.setM_verifyName("RED_BLUE");
				String fbpk = Currency.getFracCurrPK(pk_corp);// 从哪里取辅币pk?,红兰对冲，同币种核销，异币种核销
				String bbpk = Currency.getLocalCurrPK(pk_corp);
				rb.setM_fbpk(fbpk);
				rb.setM_bbpk(bbpk);
				rb.setM_verifySeq(m_hxSeq);
				rb.setM_shlPrecision(SysInit.getParaInt(pk_corp, "BD501"));
				rb.setM_isFracInuse(new Boolean(fbpk == null ? false : true));
				rb.setM_dwbm(pk_corp);
				rb.setM_Clrq(new UFDate(clrq));
				rb.setM_Clnd(accperiodvo.getPeriodyear());
				rb.setM_Clqj(accperiodvo.getVosMonth()[0].getMonth());
				rb.setM_clr(clr);

				rb.setM_debitCurr(bz);

				rb.setM_debttoBBExchange_rate(bbhl);

				rb.setM_debttoFBExchange_rate(fbhl);

				Integer jfbzjd = new Integer(2);

				jfbzjd = Currency.getCurrInfo(bz).getCurrdigit();

				rb.setM_creditMnyPrecision(jfbzjd);

				rb.setM_debitMnyPrecision(jfbzjd);
				// rb.setM_newDJ(vo[i].getYbdj());
				// rb.setM_tzshl(vo[i].getShl());
				rb.setM_curr(new Currency());
				rb.setSystem(ly == 0 || ly == 2 ? 0 : 1);

				DefaultVerifyRuleVO[] rules = new DefaultVerifyRuleVO[1];
				rules[0] = rb;

				com.setM_creditdata(com.getM_filter().getCreditData(jd));
				com.setM_debitdata(com.getM_filter().getDebitData(jd));
				com.setM_verifySequence(ScriptVO.getVerifySequence(4, rules));
				com.setM_rule(rules);
				com.setM_creditSelected(com.getM_filter().getCreditData(jd));
				com.setM_debitSelected(com.getM_filter().getDebitData(jd));

				FenPeiUtil.fenTanJsybjeRB(ybje, jd, new UFDouble(0), pk_corp,
						new UFDate(clrq), (ly == 0 || ly == 2) ? 0 : 1, fbhl,
						bbhl);

				com.verify(com.getM_debitSelected(), com.getM_creditSelected());

				SystemProfile.getInstance().log("verify com.verify end ");

				for (int i = 0; i < com.getM_logs().size(); i++) {
					((VerifyLogVO) com.getM_logs().get(i)).setM_clbh(clbh);
					// ((VerifyLogVO)com.getM_logs().get(i)).setM_pph(new
					// Integer(i+1));
					((VerifyLogVO) com.getM_logs().get(i))
							.setM_clbz(new Integer(10));// 调查标志

				}
				m_logs.addAll(com.getM_logs());
			}
			com.setM_logs(m_logs);

			SystemProfile.getInstance().log("realVerifylog");

			com.save();

			SystemProfile.getInstance().log("realVerifysave");
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
			// Log.getInstance(VerifyBO.class).error(e);
			// try
			// {
			// DJZBBO bo=new DJZBBO();
			// bo.returnBillCode(zb);
			// }
			// catch(Exception ex)
			// {
			//
			// }
			// throw new BusinessException(e.getMessage(),e);

			// ProxyBill.getIArapBillPublic().sendArapBillDelMsg(zb);
		}
	}

	/**
	 * 暂估回冲生成回冲单 .
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	private Hashtable getNewDJ(DJZBVO[] vo, AdjuestVO adjust, String clbh,
			String clrq, int ly, String pk_corp) throws Exception {
		Hashtable<DJZBVO, UFBoolean> result = new Hashtable<DJZBVO, UFBoolean>();
		DJZBVO zbvo = new DJZBVO();
		DJZBItemVO[] temp = null;
		UFDouble shlye = new UFDouble(0);
		UFDouble shlje = new UFDouble(0); // 合计数量金额

		DJZBVO source = null;
		Vector<DJZBItemVO> allitem = new Vector<DJZBItemVO>();
		for (int i = 0; i < vo.length; i++) {
			if (vo[i] == null)
				continue;
			DJZBHeaderVO header = (DJZBHeaderVO) vo[i].getParentVO();
			DJZBItemVO[] items = (DJZBItemVO[]) vo[i].getChildrenVO();
			if (header.getZgyf().intValue() == 1) {
				SystemProfile.getInstance().log(
						"source" + vo[i].getParentVO().getPrimaryKey());

				source = vo[i];
			}
			for (int j = 0; j < items.length; j++) {
				DJZBItemVO zbItem = items[j];
				shlye = shlye.add(zbItem.getShlye());
				shlje = shlje.add(zbItem.getDfshl().add(zbItem.getJfshl()));
				zbItem.setIsverifyfinished(new UFBoolean(false));
				zbItem.setVerifyfinisheddate(null);
				allitem.add(zbItem);
			}
		}
		if (null == source) {
			throw ExceptionHandler
					.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("2006", "UPP2006-v55-000136")/*
																	 * @res
																	 * "没有对应的暂估单据!"
																	 */);
		}
		// boolean
		// hastax=!ArapCommonTool.isZero(((DJZBItemVO)source.getChildrenVO()[0]).getJfybsj().add(((DJZBItemVO)source.getChildrenVO()[0]).getDfybsj()));
		DJZBItemVO[] temp1 = allitem.toArray(new DJZBItemVO[] {});
		Arrays.sort(temp1, new Comparator<Object>() {
			public int compare(Object a, Object b) {
				if (ArapCommonTool.isLarge(((DJZBItemVO) a).getShlye().abs(),
						((DJZBItemVO) b).getShlye().abs()))
					return 1;
				else if (((DJZBItemVO) a).getShlye().abs().equals(
						((DJZBItemVO) b).getShlye().abs()))
					return 0;
				else
					return -1;
			}
		});
		allitem = new Vector<DJZBItemVO>(Arrays.asList(temp1));
		if (adjust.getIsdone().booleanValue()
				|| ArapCommonTool.isEqual(adjust.getShl(), shlye)
				|| ArapCommonTool.isEqual(shlje, adjust.getShl()))// 最后一次调整
		{
			SystemProfile.getInstance().log(
					"adjust.getIsdone()" + adjust.getIsdone());
			SystemProfile.getInstance()
					.log("adjust.getShl()" + adjust.getShl());
			SystemProfile.getInstance().log("shlye" + shlye);
			SystemProfile.getInstance().log("shlje" + shlje);

			if (ArapCommonTool.isZero(shlye)) {
				return null;
			}
			Vector<DJZBItemVO> con = new Vector<DJZBItemVO>();
			DJZBItemVO[] item = new DJZBItemVO[allitem.size()];
			allitem.copyInto(item);
			UFDouble ybje = new UFDouble(0);
			UFDouble fbje = new UFDouble(0);
			UFDouble bbje = new UFDouble(0);
			UFDouble sumYE = ArapCommonTool.ZERO;
			for (int i = 0; i < item.length; i++) {

				if (ArapCommonTool.isZero(item[i].getShlye())
						|| ArapCommonTool.isZero(item[i].getYbye()))
					continue;

				DJZBItemVO newItem = (DJZBItemVO) item[i].clone();
				UFDouble sl = newItem.getSl();
				if (ArapCommonTool.isZero(newItem.getJfybsj().add(
						newItem.getDfybsj()))) {
					newItem.setSl(ArapCommonTool.ZERO);
				}
				con.add(newItem);
				newItem.setClbh(clbh);
				newItem.setJsfsbm(((DJZBHeaderVO) source.getParentVO())
						.getDjlxbm());
				newItem.setDdhh(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getFb_oid());
				newItem.setDdlx(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getVouchid());
				DJZBHeaderVO head = (DJZBHeaderVO) source.getParentVO();
				if (ly == 0 || (ly == 2 && "ys".equals(head.getDjdl())))// 销售
				{

					// newItem.setJfshl(newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
					if (i < item.length - 1) {
						newItem.setJfshl(newItem.getShlye().multiply(
								ArapConstant.INT_NEGATIVE_ONE));
						sumYE = sumYE.add(newItem.getJfshl());

					} else {
						newItem.setJfshl(adjust.getShl().multiply(
								ArapConstant.INT_NEGATIVE_ONE).sub(sumYE));
					}
					newItem.setDj(null);
					newItem.setHsdj(null);
					newItem.setJfybje(newItem.getYbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem.setJffbje(newItem.getFbye() == null ? null
							: newItem.getFbye().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setJfbbje(newItem.getBbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem = ArapDjCalculator.getInstance().calculateVO(
							newItem, "jfybje", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					newItem.setYbye(newItem.getJfybje());
					newItem.setFbye(newItem.getJffbje());
					newItem.setBbye(newItem.getJfbbje());
					newItem.setShlye(newItem.getJfshl());

				} else {
					// newItem.setDfshl(newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
					// newItem.setDfshl(adjust.getShl().multiply(ArapConstant.INT_NEGATIVE_ONE));
					if (i < item.length - 1) {
						newItem.setDfshl(newItem.getShlye().multiply(
								ArapConstant.INT_NEGATIVE_ONE));
						sumYE = sumYE.add(newItem.getDfshl());

					} else {
						newItem.setDfshl(adjust.getShl().multiply(
								ArapConstant.INT_NEGATIVE_ONE).sub(sumYE));
					}
					newItem.setDj(null);
					newItem.setHsdj(null);
					newItem.setDfybje(newItem.getYbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDffbje(newItem.getFbye() == null ? null
							: newItem.getFbye().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDfbbje(newItem.getBbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem = ArapDjCalculator.getInstance().calculateVO(
							newItem, "dfybje", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					newItem.setYbye(newItem.getDfybje());
					newItem.setFbye(newItem.getDffbje());
					newItem.setBbye(newItem.getDfbbje());
					newItem.setShlye(newItem.getDfshl());

				}

				newItem.setSl(sl);
				ybje.add(newItem.getYbye());
				fbje.add(newItem.getFbye());
				bbje.add(newItem.getBbye());

			}
			temp = new DJZBItemVO[con.size()];
			con.copyInto(temp);
			DJZBHeaderVO header = (DJZBHeaderVO) source.getParentVO().clone();
			header.setDjbh(null);
			header.setZgyf(new Integer(2));
			header.setYbje(ybje);
			header.setFbje(fbje);
			header.setBbje(bbje);
			zbvo.setParentVO(header);
			zbvo.setChildrenVO(temp);
			result.put(zbvo, new UFBoolean(true));// 生成的DJZBVO为key，是否需要红冲为value
		} else {
			UFDouble tzshl = adjust.getShl().multiply(-1);// 调整数量

			SystemProfile.getInstance().log("tzshl" + tzshl);
			SystemProfile.getInstance().log("shlye" + shlye);

			if (ArapCommonTool.isZero(tzshl)) {
				return null;
			} else if (ArapCommonTool.isLargeEqual(tzshl.multiply(shlye),
					new UFDouble(0)))// 同号
			{

				if (ArapCommonTool.isZero(shlye)) {
					if (null != adjust.getIsSO()
							&& adjust.getIsSO().booleanValue()) {
						UFDouble t = adjust.getTShl();
						if (t == null)
							t = new UFDouble(0.0d);
						if (tzshl.abs().compareTo(t.abs()) > 0) {
							tzshl = t;
						}
					}
				}
				temp = new DJZBItemVO[1];
				temp[0] = (DJZBItemVO) ((DJZBItemVO) source.getChildrenVO()[0])
						.clone();
				temp[0].setDdhh(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getFb_oid());
				temp[0].setDdlx(((DJZBItemVO) ((DJZBItemVO) source
						.getChildrenVO()[0])).getVouchid());
				temp[0].setClbh(clbh);
				temp[0].setJsfsbm(((DJZBHeaderVO) source.getParentVO())
						.getDjlxbm());
				temp[0].setShlye(tzshl);
				temp[0].setIsverifyfinished(new UFBoolean(false));
				temp[0].setVerifyfinisheddate(null);
				DJZBHeaderVO head = (DJZBHeaderVO) source.getParentVO();
				UFDouble sl = temp[0].getSl();
				if (ArapCommonTool.isZero(temp[0].getJfybsj().add(
						temp[0].getDfybsj()))) {
					temp[0].setSl(ArapCommonTool.ZERO);
				}
				if (ly == 0 || (ly == 2 && "ys".equals(head.getDjdl())))// 销售
				{
					temp[0].setJfshl(tzshl);
					temp[0] = ArapDjCalculator.getInstance().calculateVO(
							temp[0], "jfshl", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					temp[0].setYbye(temp[0].getJfybje());
					temp[0].setFbye(temp[0].getJffbje());
					temp[0].setBbye(temp[0].getJfbbje());
				} else {
					temp[0].setDfshl(tzshl);
					temp[0] = ArapDjCalculator.getInstance().calculateVO(
							temp[0], "dfshl", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					temp[0].setYbye(temp[0].getDfybje());
					temp[0].setFbye(temp[0].getDffbje());
					temp[0].setBbye(temp[0].getDfbbje());
				}
				temp[0].setSl(sl);

				DJZBHeaderVO header = (DJZBHeaderVO) source.getParentVO()
						.clone();
				header.setDjbh(null);
				header.setZgyf(new Integer(2));
				header.setYbje(temp[0].getYbye());
				header.setFbje(temp[0].getFbye());
				header.setBbje(temp[0].getBbye());
				zbvo.setParentVO(header);
				zbvo.setChildrenVO(temp);
				result.put(zbvo, new UFBoolean(false));// 生成的DJZBVO为key，是否需要红冲为value

			} else// 异号，需要红冲
			{
				verify(adjust, clbh, clrq, ly, pk_corp, result, zbvo, shlye,
						source, allitem, tzshl);

				SystemProfile.getInstance().log("verify");

			}
		}

		return result;
	}

	private void verify(AdjuestVO adjust, String clbh, String clrq, int ly,
			String pk_corp, Hashtable<DJZBVO, UFBoolean> result, DJZBVO zbvo,
			UFDouble shlye, DJZBVO source, Vector allitem, UFDouble tzshl)
			throws BusinessException, Exception {
		DJZBItemVO[] temp;
		Vector<DJZBItemVO> con = new Vector<DJZBItemVO>();

		if (ArapCommonTool.isLarge(tzshl.abs(), shlye.abs())) {
			tzshl = tzshl.div(adjust.getShl().abs()).multiply(shlye.abs());
		}

		DJZBItemVO[] item = new DJZBItemVO[allitem.size()];
		allitem.copyInto(item);
		UFDouble ybje = new UFDouble(0);
		UFDouble fbje = new UFDouble(0);
		UFDouble bbje = new UFDouble(0);
		DJZBHeaderVO head = (DJZBHeaderVO) source.getParentVO();
		for (int i = 0; i < item.length; i++)// 分摊调整数量
		{
			if (ArapCommonTool.isZero(item[i].getShlye())
					&& (ArapCommonTool.isNotZero(item[i].getYbye()) || ArapCommonTool
							.isNotZero(item[i].getBbye()))) {
				// DJZBItemVO newItem=(DJZBItemVO)item[i].clone();
				// con.add(newItem);
				// newItem.setDdhh(item[i].getFb_oid());
				// newItem.setDdlx(item[i].getVouchid());
				// newItem.setClbh(clbh);
				//
				// if(ly==0||(ly==2&&"ys".equals(head.getDjdl())))//销售
				// {
				// newItem.setJfshl(newItem.getJfshl()==null?null:newItem.getJfshl().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setShlye(newItem.getShlye()==null?null:newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDj(null);
				// newItem.setHsdj(null);
				// newItem.setJfybje(newItem.getJfybje()==null?null:newItem.getJfybje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setJffbje(newItem.getJffbje()==null?null:newItem.getJffbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setJfbbje(newItem.getJfbbje()==null?null:newItem.getJfbbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setYbye(newItem.getYbye()==null?null:newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setFbye(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setBbye(newItem.getBbye()==null?null:newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setJfybwsje(newItem.getJfybwsje()==null?null:newItem.getJfybwsje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setWbfybje(newItem.getWbffbje()==null?null:newItem.getWbfybje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setWbfbbje(newItem.getWbffbje()==null?null:newItem.getWbfbbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setJfybsj(newItem.getJfybsj()==null?null:newItem.getJfybsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setJffbsj(newItem.getJffbsj()==null?null:newItem.getJffbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setJfbbsj(newItem.getJfbbsj()==null?null:newItem.getJfbbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
				//
				// }
				// else
				// {
				// newItem.setDfshl(newItem.getDfshl()==null?null:newItem.getDfshl().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setShlye(newItem.getShlye()==null?null:newItem.getShlye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDj(null);
				// newItem.setHsdj(null);
				// newItem.setDfybje(newItem.getDfybje()==null?null:newItem.getDfybje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDffbje(newItem.getDffbje()==null?null:newItem.getDffbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDfbbje(newItem.getDfbbje()==null?null:newItem.getDfbbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setYbye(newItem.getYbye()==null?null:newItem.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setFbye(newItem.getFbye()==null?null:newItem.getFbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setBbye(newItem.getBbye()==null?null:newItem.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDfybwsje(newItem.getDfybwsje()==null?null:newItem.getDfybwsje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setWbffbje(newItem.getWbffbje()==null?null:newItem.getWbffbje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDfbbwsje(newItem.getDfbbwsje()==null?null:newItem.getDfbbwsje().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDfybsj(newItem.getDfybsj()==null?null:newItem.getDfybsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDffbsj(newItem.getDffbsj()==null?null:newItem.getDffbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// newItem.setDfbbsj(newItem.getDfbbsj()==null?null:newItem.getDfbbsj().multiply(ArapConstant.INT_NEGATIVE_ONE));
				// }
				// ybje=ybje.add(newItem.getYbye());
				// fbje=fbje.add(newItem.getFbye());
				// bbje=bbje.add(newItem.getBbye());

				continue;
			}
			if (ArapCommonTool.isZero(tzshl)) {
				continue;
			}
			if (ArapCommonTool.isZero(item[i].getShlye())) {
				continue;
			}
			if (ArapCommonTool.isLarge(item[i].getShlye().abs(), tzshl.abs())) {
				DJZBItemVO newItem = (DJZBItemVO) item[i].clone();
				con.add(newItem);
				newItem.setDdhh(item[i].getFb_oid());
				newItem.setDdlx(item[i].getVouchid());
				newItem.setClbh(clbh);
				newItem.setShlye(tzshl);
				newItem.setJsfsbm(((DJZBHeaderVO) source.getParentVO())
						.getDjlxbm());

				// item[i].setShlye(item[i].getShlye().add(tzshl));
				if (ly == 0 || (ly == 2 && "ys".equals(head.getDjdl())))// 销售
				{
					newItem.setJfshl(tzshl);
					newItem.setJfybje(newItem.getJfybje() == null ? null
							: newItem.getJfybje().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem = ArapDjCalculator.getInstance().calculateVO(
							newItem, "jfshl", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					newItem.setYbye(newItem.getJfybje());
					newItem.setFbye(newItem.getJffbje());
					newItem.setBbye(newItem.getJfbbje());

				} else {
					newItem.setDfshl(tzshl);
					newItem.setDfybje(newItem.getDfybje() == null ? null
							: newItem.getDfybje().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDffbje(newItem.getDffbje() == null ? null
							: newItem.getDffbje().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDfbbje(newItem.getDfbbje() == null ? null
							: newItem.getDfbbje().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setYbye(newItem.getYbye() == null ? null : newItem
							.getYbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
					newItem.setBbye(newItem.getBbye() == null ? null : newItem
							.getBbye().multiply(ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDfybwsje(newItem.getDfybwsje() == null ? null
							: newItem.getDfybwsje().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDfbbwsje(newItem.getDfbbwsje() == null ? null
							: newItem.getDfbbwsje().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDfybsj(newItem.getDfybsj() == null ? null
							: newItem.getDfybsj().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDfbbsj(newItem.getDfbbsj() == null ? null
							: newItem.getDfbbsj().multiply(
									ArapConstant.INT_NEGATIVE_ONE));

					newItem = ArapDjCalculator.getInstance().calculateVO(
							newItem, "dfshl", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					newItem.setYbye(newItem.getDfybje());
					newItem.setFbye(newItem.getDffbje());
					newItem.setBbye(newItem.getDfbbje());

					// item[i].setYbye(item[i].getYbye().add(newItem.getYbye()));
					// item[i].setFbye(item[i].getFbye().add(newItem.getFbye()));
					// item[i].setBbye(item[i].getBbye().add(newItem.getBbye()));

				}

				tzshl = new UFDouble(0);
				ybje = ybje.add(newItem.getYbye());
				fbje = fbje.add(newItem.getFbye());
				bbje = bbje.add(newItem.getBbye());

			} else {
				DJZBItemVO newItem = (DJZBItemVO) item[i].clone();
				con.add(newItem);
				newItem.setDdhh(item[i].getFb_oid());
				newItem.setDdlx(item[i].getVouchid());
				newItem.setClbh(clbh);
				newItem.setShlye(tzshl.div(tzshl.abs()).multiply(
						(item[i].getShlye().abs())));
				newItem.setJsfsbm(((DJZBHeaderVO) source.getParentVO())
						.getDjlxbm());

				// item[i].setShlye(new UFDouble(0));

				if (ly == 0 || (ly == 2 && "ys".equals(head.getDjdl())))// 销售
				{
					newItem.setJfshl(newItem.getShlye());
					newItem.setDj(null);
					newItem.setHsdj(null);
					newItem.setJfybje(newItem.getYbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem.setJffbje(newItem.getFbye() == null ? null
							: newItem.getFbye().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setJfbbje(newItem.getBbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem = ArapDjCalculator.getInstance().calculateVO(
							newItem, "jfybje", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					newItem.setYbye(newItem.getJfybje());
					newItem.setFbye(newItem.getJffbje());
					newItem.setBbye(newItem.getJfbbje());

				} else {
					newItem.setDfshl(newItem.getShlye());
					newItem.setDj(null);
					newItem.setHsdj(null);
					newItem.setDfybje(newItem.getYbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDffbje(newItem.getFbye() == null ? null
							: newItem.getFbye().multiply(
									ArapConstant.INT_NEGATIVE_ONE));
					newItem.setDfbbje(newItem.getBbye().multiply(
							ArapConstant.INT_NEGATIVE_ONE));
					newItem = ArapDjCalculator.getInstance().calculateVO(
							newItem, "dfybje", clrq,
							((DJZBHeaderVO) source.getParentVO()).getDjdl(),
							this.getParam(pk_corp, ly));
					newItem.setYbye(newItem.getDfybje());
					newItem.setFbye(newItem.getDffbje());
					newItem.setBbye(newItem.getDfbbje());
				}
				tzshl = tzshl.sub(newItem.getShlye());
				ybje = ybje.add(newItem.getYbye());
				fbje = fbje.add(newItem.getFbye());
				bbje = bbje.add(newItem.getBbye());

				// item[i].setYbye(item[i].getYbye().add(newItem.getYbye()));
				// item[i].setFbye(item[i].getFbye().add(newItem.getFbye()));
				// item[i].setBbye(item[i].getBbye().add(newItem.getBbye()));

			}
		}
		temp = new DJZBItemVO[con.size()];
		con.copyInto(temp);
		DJZBHeaderVO header = (DJZBHeaderVO) source.getParentVO().clone();
		header.setDjbh(null);
		header.setZgyf(new Integer(2));
		header.setYbje(ybje);
		header.setFbje(fbje);
		header.setBbje(bbje);
		zbvo.setParentVO(header);
		zbvo.setChildrenVO(temp);
		result.put(zbvo, new UFBoolean(true));// 生成的DJZBVO为key，是否需要红冲为value
	}

	/**
	 * 反冲销 clbhs-> <clbh(处理编号):<来源行ID(ddhh)集合:来源类型>>
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void unAdjuestForGC(Map<String, Map<String, String>> clbhs,
			String pk_corp) throws BusinessException {
		ExceptionHandler
				.debug("&^%&^%&^%开始调用public void unAdjuestForGC(Map<String,Map<String,String>> clbhs,String pk_corp)");
		ExceptionHandler.debug("pk_corp=" + pk_corp);
		DJZBDAO zbdao = new DJZBDAO();
		// 单据处理编号与其对应的CLBVOs
		Map<String, Vector<DJCLBVO>> clbvoMap = new HashMap<String, Vector<DJCLBVO>>();
		// 本次要反冲掉的上层来源类型:来源行号集合
		Map<String, List<String>> ddhhsMap = new HashMap<String, List<String>>();
		// 本次要删除的单据辅表ID
		List<String> fb_oids = new ArrayList<String>();

		try {
			DJCLBDMO clbdmo = new DJCLBDMO();
			Iterator<String> it = clbhs.keySet().iterator();
			while (it.hasNext()) {
				String clbh = it.next();
				ExceptionHandler.debug("clbh:" + clbh);
				Vector<DJCLBVO> clbvos = clbdmo.findByclbh(clbh);
				ExceptionHandler.debug("查询得到的clbvos长度" + clbvos.size());
				clbvoMap.put(clbh, clbvos);
				UFBoolean isrb = new ArapForGYLDMO().isRB(clbh);
				if (isrb.booleanValue())// 反掉红蓝对冲
				{
					new VerifyBO().unSave(new String[] { clbh, pk_corp });
				}
				// 一个处理编号对应的来源行ID(ddhh)集合
				Iterator<String> itt = clbhs.get(clbh).keySet().iterator();
				while (itt.hasNext()) {
					String ddhh = itt.next();
					ExceptionHandler.debug("ddhh=" + ddhh);
					String lx = clbhs.get(clbh).get(ddhh);
					ExceptionHandler.debug("lx=" + lx);
					if (!ddhhsMap.containsKey(lx))
						ddhhsMap.put(lx, new ArrayList<String>());
					ddhhsMap.get(lx).add(ddhh);
				}
				itt = ddhhsMap.keySet().iterator();
				while (itt.hasNext()) {
					String lx = itt.next();
					fb_oids.addAll(zbdao.getDjfbpksByAttr(lx,
							ddhhsMap.get(lx).toArray(new String[] {}), clbh)
							.keySet());
				}
				ddhhsMap.clear();
			}
			// 修改生效单据
			ExceptionHandler.debug("fb_oids长度=" + fb_oids.size());
			modifyEffectByFboids(fb_oids.toArray(new String[] {}));
			Vector<DJCLBVO> reserveClvo = new Vector<DJCLBVO>();
			it = clbvoMap.keySet().iterator();
			while (it.hasNext()) {
				reserveClvo.clear();
				String clbh = it.next();
				Vector<DJCLBVO> clbvos = clbvoMap.get(clbh);
				for (DJCLBVO clvo : clbvos) {// 删除本次要反冲掉的上层来源行号对应的处理记录
					if (!fb_oids.contains(clvo.getFb_oid())
							&& !fb_oids.contains(clvo.getM_dydjfbid())) {
						reserveClvo.add(clvo);
					}
				}
				if (reserveClvo.size() == 0) {
					return;
				}
				// 单据主表PK
				List<String> vouchids = new ArrayList<String>();
				for (DJCLBVO clvo : reserveClvo) {
					vouchids.add(clvo.getVouchid());
				}
				Map<String, String> ts = zbdao.getTsByPrimaryKeys(vouchids
						.toArray(new String[] {}), "arap_djzb");
				for (DJCLBVO clvo : reserveClvo) {
					clvo.setM_ts(ts.get(clvo.getVouchid()));
				}

				// 重新保存不在本次要反冲掉的上层来源行号中的处理记录
				new VerifyBO().save(reserveClvo);
			}
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
	}

	public void unAdjuest(String clbh, String[] ddlxs, String pk_corp)
			throws BusinessException {
		// TODO 自动生成方法存根
		try {
			UFBoolean isrb = new ArapForGYLDMO().isRB(clbh);
			if (isrb.booleanValue())// 经过红冲
			{
				new VerifyBO().unSave(new String[] { clbh, pk_corp });
				String[] vouchid = new ArapForGYLDMO().getDj(clbh);
				for (int i = 0; i < vouchid.length; i++) {
					DJZBVO zbvo = new DJZBBO().findByPrimaryKey(vouchid[i]);
					this.deleteEff(zbvo);
				}
			} else {
				String[] vouchid = new ArapForGYLDMO().getDj(clbh);
				for (int i = 0; i < vouchid.length; i++) {
					DJZBVO zbvo = new DJZBBO().findByPrimaryKey(vouchid[i]);
					this.deleteEff(zbvo);
				}
			}
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}

	}

	/**
	 * 取消暂估.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void unAdjuest(String[] clbh, String pk_corp)
			throws BusinessException {
		for (int i = 0; i < clbh.length; i++) {
			this.unAdjuest(clbh[i], pk_corp);
		}
	}

	public void unAdjuest(String clbh, String pk_corp) throws BusinessException {
		// TODO 自动生成方法存根
		try {
			UFBoolean isrb = new ArapForGYLDMO().isRB(clbh);
			if (isrb.booleanValue())// 经过红冲
			{
				new VerifyBO().unSave(new String[] { clbh, pk_corp });
				String[] vouchid = new ArapForGYLDMO().getDj(clbh);

				if (vouchid == null || vouchid.length == 0) {
					throw ExceptionHandler
							.createException(nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("2006",
											"UPP2006V52-000020")/*
																 * @res
																 * "操作失败,没有查询到回冲单据!处理编号:"
																 */
									+ clbh);
				}

				for (int i = 0; i < vouchid.length; i++) {
					DJZBVO zbvo = new DJZBBO().findByPrimaryKey(vouchid[i]);
					this.deleteEff(zbvo);
				}
			} else {
				String[] vouchid = new ArapForGYLDMO().getDj(clbh);

				if (vouchid == null || vouchid.length == 0) {
					throw ExceptionHandler
							.createException(nc.vo.ml.NCLangRes4VoTransl
									.getNCLangRes().getStrByID("2006",
											"UPP2006V52-000020")/*
																 * @res
																 * "操作失败,没有查询到回冲单据!处理编号:"
																 */
									+ clbh);
				}

				for (int i = 0; i < vouchid.length; i++) {
					DJZBVO zbvo = new DJZBBO().findByPrimaryKey(vouchid[i]);
					this.deleteEff(zbvo);
				}
			}
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}

	}

	/**
	 * 保存并生效单据.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public DJZBVO saveEff(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		String djlxbm = ((DJZBHeaderVO) vo.getParentVO()).getDjlxbm();
		String pk_corp = ((DJZBHeaderVO) vo.getParentVO()).getDwbm();
		UFDate date = ((DJZBHeaderVO) vo.getParentVO()).getDjrq();

		String prodid = "AR";
		DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
		DJZBItemVO[] items = (DJZBItemVO[])vo.getChildren();
		if (head.getPzglh().equals(ArapConstant.INT_ONE)) {
			prodid = "AP";
		}
		if (head.getQcbz() != null && head.getQcbz().booleanValue()) {
		} else {
			boolean bPass = AccountInfo.getPeriodIsAcc(pk_corp, prodid, date);
			if (!bPass) {
				throw ExceptionHandler
						.createException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("2006030201",
										"UPP2006030201-000434")/*
																 * @res
																 * "系统已经结帐，不能进行此操作。"
																 */);

			}
		}
		DjLXVO djlxvo = FiPubDataCache.getBillType(djlxbm, pk_corp);
		if (djlxvo == null) {
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006030201",
							"UPP2006030201-000435")
					+ ":" + djlxbm);
		}

		List<DJZBItemVO> lst = new ArrayList<DJZBItemVO>();
		for (DJZBItemVO item : (DJZBItemVO[]) vo.getChildrenVO()) {
			if (item.getDdhh() != null
					&& !nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"2008", "UPP2008-000070")/* @res "补差" */.equals(
							item.getZy())) {
				lst.add(item);
			}
		}
		// 暂估应付单、暂估应收单不处理由于财务与业务币种精度差异引起的补差
		DJZBItemVO[] array = lst.toArray(new DJZBItemVO[] {});
		
		resetDigit(head, array);
		
		vo.setChildrenVO(array);
		
			
		ArapDjCalculator.getInstance().sumBtoH(vo);
		boolean isqc = ((DJZBHeaderVO) vo.getParentVO()).getQcbz() != null
				&& ((DJZBHeaderVO) vo.getParentVO()).getQcbz().booleanValue();
		// 期初单据
		if (isqc) {
			befSaveChangeHVo_QC((DJZBHeaderVO) vo.getParentVO());
		}
	   //处理暂估是否含税,不含税则:无税金额=金额,税金= null,税率= 0
	   if ("yf".equals(head.getDjdl())&&DJZBVOConsts.ZGYF_ZG == head.getZgyf() && null != items && items.length > 0){
				String AP33 = new PubchangeBO().getSysinit(pk_corp, "AP33");
				if (null != AP33 && AP33.equals("N")) {
					for (DJZBItemVO item : items) {
						item.setDfybsj(UFDouble.ZERO_DBL);
						item.setDfbbsj(UFDouble.ZERO_DBL);
						item.setDfybwsje(item.getDfybje());
						item.setDfbbwsje(item.getDfbbje());
						item.setHsdj(item.getDj());
						item.setSl(UFDouble.ZERO_DBL);
					}
				}
	    }		
		DJZBVO ret = new DJZBBO().saveDj(vo);
		if (isqc) {
			return ret;
		}

		((DJZBHeaderVO) vo.getParentVO()).setShkjnd(((DJZBHeaderVO) vo
				.getParentVO()).getDjkjnd());
		((DJZBHeaderVO) vo.getParentVO()).setShkjqj(((DJZBHeaderVO) vo
				.getParentVO()).getDjkjqj());
		((DJZBHeaderVO) vo.getParentVO()).setShr(((DJZBHeaderVO) vo
				.getParentVO()).getLrr());
		((DJZBHeaderVO) vo.getParentVO()).setShrq(((DJZBHeaderVO) vo
				.getParentVO()).getDjrq());
		if (djlxvo.getIsqr() == null || !djlxvo.getIsqr().booleanValue()) // 审核态
		{
			((DJZBHeaderVO) vo.getParentVO()).setDjzt(new Integer(2));
			((DJZBHeaderVO) vo.getParentVO()).setSxbz(new Integer(10));
		} else // 签字确认
		{
			((DJZBHeaderVO) vo.getParentVO()).setDjzt(new Integer(3));
			((DJZBHeaderVO) vo.getParentVO()).setSxbz(new Integer(10));

			((DJZBHeaderVO) vo.getParentVO()).setYhqrkjnd(((DJZBHeaderVO) vo
					.getParentVO()).getDjkjnd());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrkjqj(((DJZBHeaderVO) vo
					.getParentVO()).getDjkjqj());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrr(((DJZBHeaderVO) vo
					.getParentVO()).getLrr());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrrq(((DJZBHeaderVO) vo
					.getParentVO()).getDjrq());
		}
		// ((DJZBHeaderVO)vo.getParentVO()).setZgyf(new Integer(1));

		// 保存单据

		return ProxyBill.getInstance().getIArapBillPublic()
				.auditOneArapBill(vo).djzbvo;

		// new ApplayBillBO().sendMessage(vo);
	}

	private void resetDigit(DJZBHeaderVO head, DJZBItemVO[] array)
			throws BusinessException {
		
		String[] ybfields=new String[]{"jfybje","jfybsj","jfybwsje","ybye","dfybje","dfybsj","dfybwsje"};
		String[] bbfields=new String[]{"bbye","dfbbje","dfbbsj","dfbbwsje","jfbbje","jfbbsj","wbfbbje"};
		
		String LCurr = Currency.getLocalCurrPK(head.getDwbm());
		CurrencyControlBO bzCon = new CurrencyControlBO();	
		for(DJZBItemVO item:array){
			for(String bbfield:bbfields){				
				if(item.getAttributeValue(bbfield)!=null){
					item.setAttributeValue(bbfield, bzCon.getFormat(LCurr,(UFDouble)item.getAttributeValue(bbfield)));
				}
			}
			 
			String bzbm = item.getBzbm();
			for(String ybfield:ybfields){
				if(item.getAttributeValue(ybfield)!=null){
					item.setAttributeValue(ybfield, bzCon.getFormat(bzbm,(UFDouble)item.getAttributeValue(ybfield)));
				}
			}
		}
	}

	/**
	 * 保存并生效单据-采购使用.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void saveEffForCG(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		String djlxbm = ((DJZBHeaderVO) vo.getParentVO()).getDjlxbm();
		String pk_corp = ((DJZBHeaderVO) vo.getParentVO()).getDwbm();
		String date = ((DJZBHeaderVO) vo.getParentVO()).getDjrq().toString();
		DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
		DJZBItemVO[] items = (DJZBItemVO[])vo.getChildren();
		String prodid = "AP";
		if (head.getQcbz() != null && head.getQcbz().booleanValue()) {
		} else {
			boolean bPass = AccountInfo.getPeriodIsAcc(pk_corp, prodid,
					new nc.vo.pub.lang.UFDate(date));
			if (!bPass) {
				throw ExceptionHandler
						.createException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("2006030201",
										"UPP2006030201-000434")/*
																 * @res
																 * "系统已经结帐，不能进行此操作。"
																 */);
			}
		}
		DjLXVO djlxvo = FiPubDataCache.getBillType(djlxbm, pk_corp);

		if (djlxvo == null) {
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006030201",
							"UPP2006030201-000435"));
		}
		// else if(vos.length>1)
		// {
		// throw
		// ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("2006030201","UPP2006030201-000436"));
		// }
		boolean isqc = ((DJZBHeaderVO) vo.getParentVO()).getQcbz() != null
				&& ((DJZBHeaderVO) vo.getParentVO()).getQcbz().booleanValue();
		// 期初单据
		if (isqc) {
			befSaveChangeHVo_QC((DJZBHeaderVO) vo.getParentVO());
		}

	   //处理暂估是否含税,不含税则:无税金额=金额,税金= null,税率= 0
	   if ("yf".equals(head.getDjdl())&&DJZBVOConsts.ZGYF_ZG == head.getZgyf() && null != items && items.length > 0) {
				String AP33 = new PubchangeBO().getSysinit(pk_corp, "AP33");
				if (null != AP33 && AP33.equals("N")) {
					for (DJZBItemVO item : items) {
						item.setDfybsj(UFDouble.ZERO_DBL);
						item.setDfbbsj(UFDouble.ZERO_DBL);
						item.setDfybwsje(item.getDfybje());
						item.setDfbbwsje(item.getDfbbje());
						item.setHsdj(item.getDj());
						item.setSl(UFDouble.ZERO_DBL);
					}
				}
	    }
		
		new DJZBBO().saveDj(vo);
		if (isqc) {
			return;
		}
		((DJZBHeaderVO) vo.getParentVO()).setShkjnd(((DJZBHeaderVO) vo
				.getParentVO()).getDjkjnd());
		((DJZBHeaderVO) vo.getParentVO()).setShkjqj(((DJZBHeaderVO) vo
				.getParentVO()).getDjkjqj());
		((DJZBHeaderVO) vo.getParentVO()).setShr(((DJZBHeaderVO) vo
				.getParentVO()).getLrr());
		((DJZBHeaderVO) vo.getParentVO()).setShrq(((DJZBHeaderVO) vo
				.getParentVO()).getDjrq());
		if ((djlxvo).getIsqr() == null || !(djlxvo).getIsqr().booleanValue()) // 审核态
		{
			((DJZBHeaderVO) vo.getParentVO()).setDjzt(new Integer(2));
			((DJZBHeaderVO) vo.getParentVO()).setSxbz(new Integer(10));
		} else // 签字确认
		{
			((DJZBHeaderVO) vo.getParentVO()).setDjzt(new Integer(3));
			((DJZBHeaderVO) vo.getParentVO()).setSxbz(new Integer(10));

			((DJZBHeaderVO) vo.getParentVO()).setYhqrkjnd(((DJZBHeaderVO) vo
					.getParentVO()).getDjkjnd());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrkjqj(((DJZBHeaderVO) vo
					.getParentVO()).getDjkjqj());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrr(((DJZBHeaderVO) vo
					.getParentVO()).getLrr());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrrq(((DJZBHeaderVO) vo
					.getParentVO()).getDjrq());
		}
		// ((DJZBHeaderVO)vo.getParentVO()).setZgyf(new Integer(1));

		// 保存单据

		ProxyBill.getInstance().getIArapBillPublic().auditOneArapBill(vo);

		// new ApplayBillBO().sendMessage(vo);
	}

	/**
	 * 反生效并删除单据.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void deleteEff(DJZBVO vo) throws BusinessException {

		((DJZBHeaderVO) vo.getParentVO()).m_isOtherOpration = true;
		if (((DJZBHeaderVO) vo.getParentVO()).getSxbz() != DJZBVOConsts.m_intSXBZ_VALID) {
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006", "UPT2006-v51-001000"));
		}
		boolean isqc = ((DJZBHeaderVO) vo.getParentVO()).getQcbz() != null
				&& ((DJZBHeaderVO) vo.getParentVO()).getQcbz().booleanValue();
		if (!isqc)
			ProxyBill.getInstance().getIArapBillPublic().unAuditOneArapBill(vo);

		new DJZBBO().deleteDj(vo);
	}

	/**
	 * 反生效并删除单据-采购使用.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void deleteEffForCG(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		String ddhh = ((DJZBItemVO) vo.getChildrenVO()[0]).getDdhh();
		String ddlx = ((DJZBItemVO) vo.getChildrenVO()[0]).getDdlx();
		DJZBHeaderVO[] headers = null;
		try {
			headers = new DJZBDAO()
					.getDJZBHeaderVOsUniversalArray(
							" from arap_djzb zb inner join arap_djfb fb on zb.vouchid=fb.vouchid where fb.ddhh='"
									+ ddhh
									+ "' and fb.ddlx='"
									+ ddlx
									+ "' and fb.dr=0 and zb.dr=0", false, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw ExceptionHandler.handleException(e);
		}

		vo.setParentVO(headers[0]);
		DJZBItemVO[] item = ProxyBill.getInstance().getIArapBillPrivate()
				.queryDjzbitems(headers[0].getVouchid());
		for (int i = 0; i < item.length; i++) {
			item[i].setPausetransact(new UFBoolean(false));
		}
		vo.setChildrenVO(item);
		// 删除单据
		// new ApplayBillBO().sendMessage_del(vo);
		((DJZBHeaderVO) vo.getParentVO()).m_isOtherOpration = true;
		if (((DJZBHeaderVO) vo.getParentVO()).getSxbz() != DJZBVOConsts.m_intSXBZ_VALID) {
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006", "UPT2006-v51-001000"));
		}
		boolean isqc = ((DJZBHeaderVO) vo.getParentVO()).getQcbz() != null
				&& ((DJZBHeaderVO) vo.getParentVO()).getQcbz().booleanValue();
		if (!isqc)
			ProxyBill.getInstance().getIArapBillPublic().unAuditOneArapBill(vo);
		new DJZBBO().deleteDj(vo);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.itf.arap.pub.IArapForGYLPublic#verify(null[], java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	/**
	 * 核销接口，提供给供应链使用.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void verifyForGYL(VerifyParamVO[] vo, String clbh, String clr,
			String clrq, String pk_corp) throws BusinessException {
		// TODO 自动生成方法存根

		String sql = "";
		VerifyFilter filter = new VerifyFilter();
		Saver saver = new Saver();
		VerifyCom com = new VerifyCom(filter, saver, null);
		try {
			int pph = 0;
			for (int i = 0; i < vo.length; i++) {
				if (vo[i].getDdhh() != null) {
					sql = " fb.ddhh='" + vo[i].getDdhh() + "' or fb.ddhh='"
							+ vo[i].getThdddhh() + "' ";
				} else {
					sql = " fb.cksqsh='" + vo[i].getDdlx() + "' or fb.ddhh='"
							+ vo[i].getThdddhh() + "' ";
				}
				ConditionVO condition = new ConditionVO();
				condition.setAttributeValue("other", sql);
				ConditionVOSqlTool sqltool = new ConditionVOSqlTool(condition);
				String sql1 = sqltool.getSql();
				VerifyServiceDMO dmo = null;
				dmo = new VerifyServiceDMO();
				VerifyVO[] vos = dmo.queryVerifyVO(sql1);
				com.setM_creditdata(filter.getCreditData(vos));
				com.setM_debitdata(filter.getDebitData(vos));
				com.setM_creditSelected(com.getM_creditdata());
				VerifyVO[] credit = com.getM_creditdata();
				VerifyVO[] debit = com.getM_debitdata();

				for (int i1 = 0; i1 < credit.length; i1++) {
					credit[i1].setM_jsybje(credit[i1].getM_ybye());
					credit[i1].setM_jsfbje(credit[i1].getM_fbye());
					credit[i1].setM_jsbbje(credit[i1].getM_bbye());
				}
				for (int i1 = 0; i1 < debit.length; i1++) {
					debit[i1].setM_jsybje(debit[i1].getM_ybye());
					debit[i1].setM_jsfbje(debit[i1].getM_fbye());
					debit[i1].setM_jsbbje(debit[i1].getM_bbye());
				}
				com.setM_debitSelected(com.getM_debitSelected());

				String HxMode = SysInit.getParaString(pk_corp, "AR1");
				Integer m_hxSeq = null;
				if (HxMode.equals("最早余额法")) {/* -=notranslate=- */
					m_hxSeq = new Integer(0);
				} else {
					m_hxSeq = new Integer(1);
				}
				AccperiodVO accperiodvo = Accperiod
						.queryAccyearByDate(new UFDate(clrq));
				String bz = vos[0].getM_CurrPk();

				String fbbz = Currency.getFracCurrPK(pk_corp);
				String bbbz = Currency.getLocalCurrPK(pk_corp);
				UFDouble bbhl = Currency.getRateBoth(pk_corp, bz, clrq)[1];
				UFDouble fbhl = Currency.getRateBoth(pk_corp, bz, clrq)[0];
				DefaultVerifyRuleVO rb = null;
				rb = new DefaultVerifyRuleVO();
				rb.setM_verifyName("RED_BLUE");

				rb.setM_shlPrecision(SysInit.getParaInt(pk_corp, "BD501"));
				rb.setM_fbpk(fbbz);
				rb.setM_bbpk(bbbz);
				rb.setM_verifySeq(m_hxSeq);

				rb.setM_isFracInuse(new Boolean(fbbz == null ? false : true));
				rb.setM_dwbm(pk_corp);
				rb.setM_Clrq(new UFDate(clrq));
				rb.setM_Clnd(accperiodvo.getPeriodyear());
				rb.setM_Clqj(accperiodvo.getVosMonth()[0].getMonth());
				rb.setM_clr(clr);

				rb.setM_debitCurr(bz);

				rb.setM_debttoBBExchange_rate(bbhl);

				rb.setM_debttoFBExchange_rate(fbhl);
				rb.setSystem(0);

				Integer jfbzjd = new Integer(2);
				Integer dfbzjd = new Integer(2);

				if (fbbz != null)
					jfbzjd = Currency.getCurrInfo(fbbz).getCurrdigit();
				dfbzjd = Currency.getCurrInfo(bbbz).getCurrdigit();

				rb.setM_debitMnyPrecision(jfbzjd);
				rb.setM_creditMnyPrecision(dfbzjd);
				rb.setM_curr(new Currency());

				DefaultVerifyRuleVO[] rules = new DefaultVerifyRuleVO[1];
				rules[0] = rb;

				com.setM_verifySequence(ScriptVO.getVerifySequence(7, rules));

				com.setM_rule(rules);

				com.verify(com.getM_debitSelected(), com.getM_creditSelected());

				// 处理小号
				boolean iscomplete = false;

				for (int j = 0; j < com.getM_logs().size(); j++) {

					((VerifyLogVO) com.getM_logs().get(j)).setM_clbh(clbh);

					int temp = ((VerifyLogVO) com.getM_logs().get(j))
							.getM_pph().intValue();
					((VerifyLogVO) com.getM_logs().get(j))
							.setM_pph(new Integer(pph));

					for (int k = i + 1; k < com.getM_logs().size(); k++) {

						if (temp == (((VerifyLogVO) com.getM_logs().get(k))
								.getM_pph()).intValue()) {
							((VerifyLogVO) com.getM_logs().get(k))
									.setM_pph(new Integer(pph));
							((VerifyLogVO) com.getM_logs().get(k))
									.setM_clbh(clbh);
							if (k == com.getM_logs().size() - 1) {
								iscomplete = true;
							}
						} else {
							j = k - 1;
							break;
						}

					}
					if (iscomplete)
						break;
					pph++;
				}

			}

			com.save();
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
	}

	public void verifyForGYLBattch(Hashtable VerifyParam, String clr,
			String clrq, String pk_corp) throws BusinessException {
		Set key = VerifyParam.keySet();
		Iterator it = key.iterator();
		while (it.hasNext()) {
			String clbh = (String) it.next();
			VerifyParamVO[] vo = (VerifyParamVO[]) VerifyParam.get(clbh);
			this.verifyForGYL(vo, clbh, clr, clrq, pk_corp);
		}
	}

	/**
	 * 反核销提供供应链使用.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void unVerifyForGYL(String clbh) throws BusinessException {
		// TODO 自动生成方法存根

		VerifyFilter filter = new VerifyFilter();
		Saver saver = new Saver();
		VerifyCom com = new VerifyCom(filter, saver, null);
		com.unSave(new String[] { clbh });
	}

	public void unVerifyForGYLBattch(String[] clbh) throws BusinessException {
		for (int i = 0; i < clbh.length; i++) {
			this.unVerifyForGYL(clbh[i]);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.itf.arap.pub.IArapForGYLPublic#canModify(java.lang.String)
	 */
	public UFBoolean canModify(String pk_corp) throws BusinessException {
		// TODO 自动生成方法存根
		UFBoolean result = null;
		try {
			result = new ArapForGYLDMO().canModify(pk_corp);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		return result;
	}

	// 根据ddhh查询单据,返回hashtable,key=ddhh,value=VerifyVO[]
	/**
	 * 核销回冲查询核销对象.
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	private Hashtable<String, VerifyVO[]> getVerifyVO(AdjuestVO[] vo, int lylx)
			throws BusinessException {
		try {
			Hashtable<String, List<VerifyVO>> result = new Hashtable<String, List<VerifyVO>>();
			ConditionVO condition = new ConditionVO();
			VerifyVO[] vos = null;
			String fb = "";
			if (lylx == 0)// 订单行id
			{
				fb = "fb.ddhid";
			} else if (lylx == 1)// 出库单行id
			{
				fb = "fb.ckdid";
			} else if (lylx == 2)// 发票行id
			{
				fb = "fb.fphid";
				// add by ouyangzhb 2011-05-09 增加发票的来源单据ID
			}else if (lylx == 3){
				fb = "fb.ckdid";
			}

			List<String> fbids = new ArrayList<String>();
			for (int i = 0; i < vo.length; i++) {
				fbids.add(vo[i].getDdhh());
			}
			//add by ouyangzhb 2011-05-12 费用发票红冲单据的核销处理 begin
			String inStr ;
			if(lylx == 3){
				String inStr1 = SqlUtils
				.getInStr(null, fbids.toArray(new String[] {}));
				String sql = "select ckdid  from arap_djfb where fb_oid "+inStr1+" and dr=0 ";
				inStr = " fb.ckdid in ("+sql+")" ;
			}else{
				inStr = SqlUtils
				.getInStr(fb, fbids.toArray(new String[] {}));
			}
			//add by ouyangzhb 2011-05-12 费用发票红冲单据的核销处理 end
			condition.setAttributeValue("other", inStr);
			ConditionVOSqlTool sqltool = new ConditionVOSqlTool(condition);
			String sql1 = sqltool.getSql();
			if (sql1
					.indexOf("and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0") != -1) {
				String sql2 = sql1
						.substring(sql1
								.indexOf("and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0")
								+ "and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0"
										.length());
				sql1 = sql1
						.substring(
								0,
								sql1
										.indexOf("and  (fb.pausetransact is NULL or fb.pausetransact='N' ) and zb.zgyf=0"));

				sql1 = sql1 + " and zb.sxbz=10 " + sql2;

			}
			VerifyServiceDMO dmo = null;
			dmo = new VerifyServiceDMO();
			vos = dmo.queryVerifyVO(sql1);

			for (VerifyVO vvo : vos) {
				String keyst = (String) vvo.getAttributeValue(fb.substring(3));
				if (!result.containsKey(keyst)) {
					result.put(keyst, new ArrayList<VerifyVO>());
				}
				List<VerifyVO> name = result.get(keyst);
				name.add(vvo);
				result.put(keyst, name);
			}
			Hashtable<String, VerifyVO[]> result1 = new Hashtable<String, VerifyVO[]>();

			for (String key : result.keySet()) {
				result1.put(key, result.get(key).toArray(new VerifyVO[] {}));
			}
			return result1;
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
	}

	// private void checkYE(AdjuestVO[] vos,Hashtable verifyvo) throws
	// BusinessException
	// {
	// for(int i=0;i<vos.length;i++)
	// {
	// Object verify1=verifyvo.get(vos[i].getDdhh());
	// Object[] verify=(Object[])verify1;
	// UFDouble ye=new UFDouble(0);
	// if(verify==null || verify.length==0)
	// continue;
	// for(int j=0;j<verify.length;j++)
	// {
	// ye=ye.add(((VerifyVO)verify[j]).getM_ybye());
	// }
	// UFDouble dj=null;
	// if(vos[i].getYbdj()==null || vos[i].getYbdj()==0) //调数量
	// dj=((VerifyVO)verify[0]).getM_dj();
	// else //调单价
	// dj=((VerifyVO)verify[0]).getM_dj().sub(vos[i].getYbdj());
	// UFDouble je=dj.multiply(vos[i].getShl());
	// if(ye>0 && ye.sub(je)<0) //余额不足
	// {
	// throw new BusinessException("余额不足，不能调整");
	// }
	// if(ye<0 && ye.sub(je)>0) //余额不足
	// {
	// throw new BusinessException("余额不足，不能调整");
	// }
	//
	// }
	// }

	private void sortVerifyVO(Hashtable verifyvo, Integer mode)
			throws BusinessException {
		Set keys = verifyvo.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			this.sortVector((VerifyVO[]) verifyvo.get(it.next()), mode
					.intValue());
		}
	}

	private void sortVector(VerifyVO[] vos, int flag) {
		if (vos == null || vos.length < 2) {
			return;
		}
		for (int i = 0; i < vos.length; i++) {
			// m_xydqr信用到期日
			UFDate xydqr = vos[i].getM_xydqr();
			for (int j = i + 1; j < vos.length; j++) {
				// m_xydqr信用到期日
				UFDate xydqr2 = vos[j].getM_xydqr();
				if (flag == 0) {// 顺排
					if (xydqr2.before(xydqr)) {
						swap(vos, i, j);
					}
				} else if (flag == 1) {// 倒排
					if (xydqr2.after(xydqr)) {
						swap(vos, i, j);
					}
				}
			}

		}
	}

	private static void swap(VerifyVO[] vos, int i, int j) {
		VerifyVO temp = vos[j];
		vos[j] = vos[i];
		vos[i] = temp;
	}

	public void saveAdjust(DJZBVO vo, AdjuestVO[] adjustvo, String clbh,
			String clr, String slrq, String pk_corp, int lylx, int ly)
			throws BusinessException {
		// TODO 自动生成方法存根
		new DJZBBO().save(vo);
		this.Adjuest(adjustvo, clbh, clr, slrq, pk_corp, lylx, ly);
	}

	public void unSaveAdjust(DJZBVO vo, String clbh, String pk_corp)
			throws BusinessException {
		this.unAdjuest(clbh, pk_corp);
		new DJZBBO().deleteDj(vo);
	}

	private int[] getParam(String pk_corp, int pzglh) throws Exception {
		if (pzglh == 0) {
			if (((ISysInitQry) NCLocator.getInstance().lookup(
					ISysInitQry.class.getName()))
					.getParaString(pk_corp, "AR21").equals("含税价格优先"))/*
																	 * -=notranslate
																	 * =-
																	 */
			{
				return new int[] { RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE,
						RelationsCalVO.YES_LOCAL_FRAC };
			} else {
				return new int[] { RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE,
						RelationsCalVO.YES_LOCAL_FRAC };
			}

		} else {
			if (((ISysInitQry) NCLocator.getInstance().lookup(
					ISysInitQry.class.getName()))
					.getParaString(pk_corp, "AP21").equals("含税价格优先"))/*
																	 * -=notranslate
																	 * =-
																	 */
			{
				return new int[] { RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE,
						RelationsCalVO.YES_LOCAL_FRAC };
			} else {
				return new int[] { RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE,
						RelationsCalVO.YES_LOCAL_FRAC };
			}
		}

	}

	public void doException(String clbh, String clr, String pk_corp)
			throws BusinessException {
		// DJCLBVO vo=new DJCLBVO();
		// vo.setDwbm(pk_corp);
		// vo.setClbh(clbh);
		// vo.setClr(clr);
		// new nc.bs.arap.verify.VerifyBO().onLinkPFForDisOper(vo);
		// try
		// {
		// String[] vouchid=new ArapForGYLDMO().getDj(clbh);
		// for(int i=0;i<vouchid.length;i++)
		// {
		// DJZBVO zbvo=new DJZBBO().findByPrimaryKey(vouchid[i]);
		// new DJZBBO().returnBillCode(zbvo);
		// }
		// }
		// catch(Exception e)
		// {
		// throw ExceptionHandler.handleException(this.getClass(),e);
		// }

	}

	public String getSkBalanceByBasdoc(String spk_Cubasdoc, String sWhere,
			String pk_corp, int iAgBillStat) throws BusinessException {
		try {
			return new ArapForGYLDMO().getSkBalanceByBasdoc(spk_Cubasdoc,
					sWhere, pk_corp, iAgBillStat);
		} catch (SystemException e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		} catch (NamingException e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
	}

	/**
	 * 通过来源单据行PK及当前单据所在公司删除生效单据
	 * 
	 * @param ddhh_corp
	 * @throws BusinessException
	 */
	public void deleteEff(Map<String, String> ddhh_corp)
			throws BusinessException {
		Iterator<String> it = ddhh_corp.keySet().iterator();
		Map<String, List<String>> corp_ddhhs = new HashMap<String, List<String>>();
		while (it.hasNext()) {
			String ddhh = it.next();
			String corp = ddhh_corp.get(ddhh);
			if (!corp_ddhhs.containsKey(corp))
				corp_ddhhs.put(corp, new ArrayList<String>());
			corp_ddhhs.get(corp).add(ddhh);
		}
		it = corp_ddhhs.keySet().iterator();
		while (it.hasNext()) {
			String corp = it.next();
			String[] ddhhs = corp_ddhhs.get(corp).toArray(new String[] {});
			DJZBVO[] djs = null;
			try {
				djs = new ARAPDjAlienBill().getDjvosbySourceHh(ddhhs, corp);
				this.doDelEff(djs, ddhhs);
			} catch (Exception e) {
				throw ExceptionHandler.handleException(this.getClass(), e);
			}
		}
	}

	/**
	 * 
	 * 根据来源分录PK，删除已生效单据中相应的记录.
	 * <p>
	 * <strong>调用模块：</strong>
	 * 
	 * <p>
	 * <strong>最后修改人：rocking</strong>
	 * <p>
	 * <strong>最后修改日前：2007-7-16</strong>
	 * <p>
	 * <strong>用例描述：</strong>
	 * <p>
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public void deleteEff(String[] ddhh) throws BusinessException {
		DJZBVO[] djs = null;
		try {
			djs = new ARAPDjAlienBill().getDjvosbySourceHh(ddhh, null);
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		this.doDelEff(djs, ddhh);
	}

	private void doDelEff(DJZBVO[] djs, String[] ddhh) throws BusinessException {
		if (null == djs) {
			// Log.getInstance(this.getClass())
			throw ExceptionHandler.createException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("2006", "UPT2006-v502-001008"));
		}
		List<String> ddhhlst = Arrays.asList(ddhh);
		List<DJZBVO> lst = new ArrayList<DJZBVO>();
		for (DJZBVO dj : djs) {
			boolean alldel = true;
			((DJZBHeaderVO) dj.getParentVO()).m_isOtherOpration = true;
			dj.setm_OldVO((DJZBVO) dj.clone());
			for (DJZBItemVO item : (DJZBItemVO[]) dj.getChildrenVO()) {
				if (ddhhlst.contains(item.getDdhh())) {
					item.setStatus(VOStatus.DELETED);
				} else {
					item.setStatus(VOStatus.UNCHANGED);
					alldel = false;
				}
			}
			if (alldel) {
				this.deleteEff(dj);
			} else {
				lst.add(dj);
			}

		}
		if (lst.size() > 0)
			new ModifyEffectBill().editEffectDj(lst.toArray(new DJZBVO[] {}));
	}

	protected void befSaveChangeHVo_QC(nc.vo.ep.dj.DJZBHeaderVO headvo)
			throws BusinessException {
		String djnd = null;// 单据年
		String djmondth = null;// 单据月
		nc.vo.bd.period.AccperiodVO accperiod = null;
		AccountCalendar calendar = AccountCalendar.getInstance();
		try {
			calendar.setDate(headvo.getDjrq());
			accperiod = calendar.getYearVO();
			accperiod.setVosMonth(new AccperiodmonthVO[] { calendar
					.getMonthVO() });
			djnd = accperiod.getPeriodyear(); // 单据会计年度
			djmondth = accperiod.getVosMonth()[0].getMonth(); // 单据会计期间
		} catch (Exception e) {
			Log.getInstance(this.getClass()).error(e.getMessage(), e);
		}
		if (accperiod == null) {
			calendar.setDate(new UFDate(new Date()));
			djmondth = calendar.getMonthVO().getMonth();
			djnd = calendar.getYearVO().getPeriodyear();
		}

		headvo.setDjkjnd(djnd);
		headvo.setDjkjqj(djmondth);
		headvo.setShr(headvo.getLrr()); // 期初单据审核人=录入人,审核日期=录入日期
		headvo.setShrq(headvo.getDjrq());
		headvo.setShkjnd(djnd);
		headvo.setShkjqj(djmondth);
		headvo.setSxr(headvo.getLrr());
		headvo.setSxkjnd(djnd);
		headvo.setSxkjqj(djmondth);
		headvo.setSxrq(headvo.getDjrq());
		headvo.setSpzt(DJZBVOConsts.m_strStatusVerifiedPass);

		if (DjVOTreaterAid.hasSettleInfo(headvo)) {
			headvo.setDjzt(new Integer(DJZBVOConsts.m_intDJStatus_Signature));
			headvo.setYhqrkjnd(djnd);
			headvo.setYhqrkjqj(djmondth);
			headvo.setYhqrrq(headvo.getDjrq());
			headvo.setYhqrr(headvo.getLrr());

		} else {
			headvo.setDjzt(new Integer(DJZBVOConsts.m_intDJStatus_Verified));
		}
		headvo.setSxbz(new Integer(DJZBVOConsts.m_intSXBZ_VALID));

	}

	/**
	 * 删除生效单据某写辅表行
	 * 
	 * @param
	 * @return
	 * @throws
	 * @since NCV5.011
	 * @see
	 */
	public DJZBVO[] modifyEffectByFboids(String[] fboids)
			throws BusinessException {
		List<String> fboidlst = Arrays.asList(fboids);
		DJZBDAO zbdao = new DJZBDAO();
		DJZBVO[] djs = null;
		try {
			djs = zbdao.findByPrimaryKeys(zbdao.findDjpksByFboids(fboids));
		} catch (Exception e) {
			throw ExceptionHandler.handleException(this.getClass(), e);
		}
		if (null == djs) {
			throw ExceptionHandler.createException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("2006", "UPT2006-v502-001008"));
		}
		List<DJZBVO> lst = new ArrayList<DJZBVO>();
		boolean needDel = true;
		for (DJZBVO dj : djs) {
			((DJZBHeaderVO) dj.getParentVO()).m_isOtherOpration = true;
			dj.setm_OldVO((DJZBVO) dj.clone());
			needDel = true;
			for (DJZBItemVO item : (DJZBItemVO[]) dj.getChildrenVO()) {
				if (fboidlst.contains(item.getFb_oid())) {
					item.setStatus(VOStatus.DELETED);
				} else {
					item.setStatus(VOStatus.UNCHANGED);
					needDel = false;
				}
			}
			// if(dj.getChildrenVO().length==1&&VOStatus.DELETED==dj.getChildrenVO()[0].getStatus())
			if (needDel) {
				this.deleteEff(dj);
			} else {
				lst.add(dj);
			}

		}
		return new ModifyEffectBill()
				.editEffectDj(lst.toArray(new DJZBVO[] {}));
	}

	public ResMessage saveEffForPj(DJZBVO vo) throws BusinessException {
		// TODO 自动生成方法存根

		String djlxbm = ((DJZBHeaderVO) vo.getParentVO()).getDjlxbm();
		String pk_corp = ((DJZBHeaderVO) vo.getParentVO()).getDwbm();
		UFDate date = ((DJZBHeaderVO) vo.getParentVO()).getDjrq();

		String prodid = "AR";
		DJZBHeaderVO head = (DJZBHeaderVO) vo.getParentVO();
		if (head.getPzglh() == ArapConstant.INT_ONE) {
			prodid = "AP";
		}
		if (head.getQcbz() != null && head.getQcbz().booleanValue()) {
		} else {
			boolean bPass = AccountInfo.getPeriodIsAcc(pk_corp, prodid, date);
			if (!bPass) {
				throw ExceptionHandler
						.createException(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("2006030201",
										"UPP2006030201-000434")/*
																 * @res
																 * "系统已经结帐，不能进行此操作。"
																 */);

			}
		}
		DjLXVO djlxvo = FiPubDataCache.getBillType(djlxbm, pk_corp);
		if (djlxvo == null) {
			throw ExceptionHandler.createException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("2006030201",
							"UPP2006030201-000435"));
		}

		// 暂估应付单、暂估应收单不处理由于财务与业务币种精度差异引起的补差
		ArapDjCalculator.getInstance().sumBtoH(vo);
		boolean isqc = ((DJZBHeaderVO) vo.getParentVO()).getQcbz() != null
				&& ((DJZBHeaderVO) vo.getParentVO()).getQcbz().booleanValue();
		// 期初单据
		if (isqc) {
			befSaveChangeHVo_QC((DJZBHeaderVO) vo.getParentVO());
		}
		DJZBVO ret = new DJZBBO().saveDj(vo);
		if (isqc) {
			ResMessage resMessage = new ResMessage();
			resMessage.djzbvo = ret;
			return resMessage;
		}

		((DJZBHeaderVO) vo.getParentVO()).setShkjnd(((DJZBHeaderVO) vo
				.getParentVO()).getDjkjnd());
		((DJZBHeaderVO) vo.getParentVO()).setShkjqj(((DJZBHeaderVO) vo
				.getParentVO()).getDjkjqj());
		((DJZBHeaderVO) vo.getParentVO()).setShr(((DJZBHeaderVO) vo
				.getParentVO()).getLrr());
		((DJZBHeaderVO) vo.getParentVO()).setShrq(((DJZBHeaderVO) vo
				.getParentVO()).getDjrq());
		if (djlxvo.getIsqr() == null || !djlxvo.getIsqr().booleanValue()) // 审核态
		{
			((DJZBHeaderVO) vo.getParentVO()).setDjzt(new Integer(2));
			((DJZBHeaderVO) vo.getParentVO()).setSxbz(new Integer(10));
		} else // 签字确认
		{
			((DJZBHeaderVO) vo.getParentVO()).setDjzt(new Integer(3));
			((DJZBHeaderVO) vo.getParentVO()).setSxbz(new Integer(10));

			((DJZBHeaderVO) vo.getParentVO()).setYhqrkjnd(((DJZBHeaderVO) vo
					.getParentVO()).getDjkjnd());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrkjqj(((DJZBHeaderVO) vo
					.getParentVO()).getDjkjqj());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrr(((DJZBHeaderVO) vo
					.getParentVO()).getLrr());
			((DJZBHeaderVO) vo.getParentVO()).setYhqrrq(((DJZBHeaderVO) vo
					.getParentVO()).getDjrq());
		}
		// ((DJZBHeaderVO)vo.getParentVO()).setZgyf(new Integer(1));

		// 保存单据

		ResMessage auditOneArapBill = ProxyBill.getInstance()
				.getIArapBillPublic().auditOneArapBill(vo);

		return auditOneArapBill;

		// new ApplayBillBO().sendMessage(vo);
	}

}