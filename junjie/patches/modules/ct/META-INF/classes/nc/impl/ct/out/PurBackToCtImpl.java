package nc.impl.ct.out;

/**
 * 类的功能、用途、现存BUG，以及其它别人可能感兴趣的介绍。
 * 作者：王亮
 * @version	最后修改日期(2003-6-17 11:05:37)
 * @see		需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 * 修改人 + 修改日期
 * 修改说明
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import nc.bs.ct.pub.CTCheckDMO;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.scm.inter.ArapHelper;
import nc.bs.scm.pub.bill.ScmImpl;
import nc.impl.ct.ct0101.TypeDMO;
import nc.impl.ct.pub.bill.CtBaseSqlString;
import nc.itf.ct.service.ICtToPo_BackToCt;
import nc.itf.pu.inter.IPuToCt_PoToCt;
import nc.itf.scm.cenpur.service.ChgDataUtil;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.vo.arap.inter.ContractQueryVO;
import nc.vo.dm.pub.DMDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ProductCode;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.cenpur.service.ChgPriceMnyVO;
import nc.vo.scm.constant.ct.BillState;
import nc.vo.scm.constant.ct.BillType;
import nc.vo.scm.constant.ct.OrderControlType;
import nc.vo.scm.ctpo.CtAllotImprestPoVO;
import nc.vo.scm.ctpo.CtExecImprestVO;
import nc.vo.scm.ctpo.CtStatusToPoVO;
import nc.vo.scm.ctpo.ParaPoToCtRewriteVO;
import nc.vo.scm.pub.SaveHintException;

public class PurBackToCtImpl extends ScmImpl implements ICtToPo_BackToCt {
	/**
	 * PurBackToCtBO 构造子注解。
	 * 
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 */
	public PurBackToCtImpl() {
		super();
	}

	/**
	 * 此处插入方法说明。 作者：程起伍 功能：计算合同应付余额 计算公式： 合同应付余额 = 采购应付余额 - 未核销预付款
	 * 创建日期：(2003-10-15 12:08:07)
	 * 
	 * @param vo
	 *            nc.vo.dm.pub.DMDataVO[]
	 */
	private void caculateNeedPayMny(DMDataVO[] vo) {
		if (vo != null && vo.length > 0) {
			int iLen = vo.length;
			UFDouble ufdNeedPayMny = null; // 采购应付余额
			UFDouble ufdCtNeedPayMny = null;// 合同应付余额
			UFDouble ufdNotVerify = null; // 未核销预付款
			UFDouble ufd0 = new UFDouble(0);
			for (int i = 0; i < iLen; i++) {
				ufdNeedPayMny = (UFDouble) vo[i].getAttributeValue("dapmny");
				ufdNotVerify = (UFDouble) vo[i]
						.getAttributeValue("dnotverifymny");
				// 如果发票金额为空，那么将发票金额置为0
				if (ufdNeedPayMny == null)
					ufdNeedPayMny = ufd0;
				if (ufdNotVerify == null)
					ufdNotVerify = ufd0;
				ufdCtNeedPayMny = ufdNeedPayMny.sub(ufdNotVerify);
				// 将合同应付余额置入vo
				vo[i].setAttributeValue("ctdapmny", ufdCtNeedPayMny);
			}
		}
	}

	/**
	 * 此处插入方法说明。 作者：<author> 创建日期：(2003-10-15 11:30:10)
	 * 
	 * @return java.lang.String
	 * @param sWhere
	 *            java.lang.String
	 */
	private String getSqlToArap(String pk_corp, String sWhere) {
		StringBuffer sbWhere = new StringBuffer(
				" ct_manage.activeflag=0 and ct_manage.dr=0 and ct_manage_b.dr = 0 and ct_type.nbusitype = 0 and ct_manage.ifearly = 'N' ");
		if (pk_corp != null) {
			sbWhere.append(" and ct_manage.pk_corp='");
			sbWhere.append(pk_corp);
			sbWhere.append("' ");
		}
		if (null != sWhere && sWhere.trim().length() > 0) {
			sbWhere.append(" and (");
			sbWhere.append(sWhere);
			sbWhere.append(")");
		}
		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT ct_manage.ct_code FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ");
		sql.append(" where ");
		sql.append(sbWhere.toString());

		return sql.toString();
	}/**
	 * 此处插入方法说明。 作者：<lirr> 创建日期：(2008-9-26 11:30:10)
	 * 
	 * @return java.lang.String
	 * @param sWhere
	 *            java.lang.String
	 * 增加原因：v55以前只有采购合同调用，v55改为销售合同与其他合同也调用，所以需要加参数 
	 * @param iBusitype
	 *            java.lang.Integer        
	 */
	
	private String getSqlToArapNew(String pk_corp, String sWhere, Integer iBusitype) {
		StringBuffer sbWhere = new StringBuffer(
				" ct_manage.activeflag=0 and ct_manage.dr=0 and ct_manage_b.dr = 0 and ct_manage.ifearly = 'N' and ct_type.nbusitype = ");
		sbWhere.append(iBusitype);
		if (pk_corp != null) {
			sbWhere.append(" and ct_manage.pk_corp='");
			sbWhere.append(pk_corp);
			sbWhere.append("' ");
		}
		if (null != sWhere && sWhere.trim().length() > 0) {
			sbWhere.append(" and (");
			sbWhere.append(sWhere);
			sbWhere.append(")");
		}
		//modified by lirr 2009-03-03 决定V55取消来源为采购合同的采购订单生成付款单后回写采购合同的功能。
		/*StringBuffer sql = new StringBuffer("");
		switch(iBusitype){
		case 0 : 
		{
			StringBuffer sbPoItemPk = new StringBuffer(
			"select corder_bid from po_order_b where cupsourcebillid  in ");
			sbPoItemPk.append(" (SELECT DISTINCT ct_manage.pk_ct_manage FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ) ");
			//sbPoItemPk.append(CtBaseSqlString.formInSQL("ct.pk_ct_manage", pk_ct_manage))
			sql.append(
			"( SELECT DISTINCT ct_manage.pk_ct_manage_b FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ");
			sql.append(" where ");
			sql.append(sbWhere.toString());
			sql.append(" ) union ");
			sql.append(sbPoItemPk.toString());
			sql.append(" )  ");
			
			break;
		}
		
		default: {
			sql.append(
			"SELECT DISTINCT ct_manage.pk_ct_manage FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ");
			sql.append(" where ");
			sql.append(sbWhere.toString());
			break;
		}
		}*/
		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT ct_manage.pk_ct_manage FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ");
		sql.append(" where ");
		sql.append(sbWhere.toString());

		return sql.toString();
	}

	/**
	 * 此处插入方法说明。 作者：<author> 创建日期：(2003-9-25 14:53:46)
	 * 
	 * @return java.lang.String
	 * @param sWhere
	 *            java.lang.String
	 */
	private String getSqlToPu(String pk_corp, String sWhere) {
		StringBuffer sbWhere = new StringBuffer(
				" ct_manage.activeflag=0 and ct_manage.dr=0 and ct_manage_b.dr = 0 and ct_type.nbusitype = 0 and ct_manage.ifearly = 'N' ");
		if (pk_corp != null) {
			sbWhere.append(" and ct_manage.pk_corp='");
			sbWhere.append(pk_corp);
			sbWhere.append("'");
		}
		if (null != sWhere && sWhere.trim().length() > 0) {
			sbWhere.append(" and (");
			sbWhere.append(sWhere);
			sbWhere.append(")");
		}
		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT ct_manage.pk_ct_manage FROM ct_manage LEFT OUTER JOIN ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type LEFT OUTER JOIN ct_manage_b ON ct_manage_b.pk_ct_manage = ct_manage.pk_ct_manage ");
		sql.append(" where ");
		sql.append(sbWhere.toString());

		return sql.toString();
	}

	/**
	 * 此处插入方法说明。 作者：程起伍 功能：查询合同是否是有效的合同 创建日期：(2003-10-8 11:03:12)
	 * 
	 * @return nc.vo.scm.ctpo.CtStatusToPoVO
	 * @param vo
	 *            nc.vo.scm.ctpo.CtStatusToPoVO
	 * @exception BusinessException
	 *                异常说明。
	 */
	public CtStatusToPoVO isCtActive(CtStatusToPoVO vo)
			throws BusinessException {
		CtStatusToPoVO retVO = null;
		try {
			PurForCtDMO dmo = new PurForCtDMO();
			retVO = dmo.isCtActive(vo);

		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return retVO;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-9-16 21:15:03)
	 * 
	 * @return nc.vo.dm.pub.DMDataVO[]
	 * @param alConds
	 *            java.util.ArrayList 0: String sWhere 1: String pk_corp
	 * 
	 * @exception BusinessException
	 *                异常说明。
	 * modified by lirr 2008-08-21 v55需求 销售合同、其他合同 都要增加执行情况查询，因此继续沿用此方法，加单据类型sBillType区分              
	 */
	/*public DMDataVO[] qryExecImprest(ArrayList alConds ,String sBillType)
			throws BusinessException {
		DMDataVO[] voRet = null; // 返回值
		if (alConds == null || alConds.size() <= 0)
			return null;

		try {
			String sWhere = (String) alConds.get(0);
			String pk_corp = (String) alConds.get(1);
			int inbusitype = 0 ;
			if ( BillType.PURDAILY.equals(sBillType)) {
				inbusitype = 0 ;
			}
			else if ( BillType.SALEDAILY.equals(sBillType)) {
				inbusitype = 1 ;
			}
			else if ( BillType.OTHER.equals(sBillType)) {
				inbusitype = 2 ;
			}
			
			ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
					.getInstance().lookup(
							ICreateCorpQueryService.class.getName());
			//采购接口的返回值
			Hashtable<String,CtExecImprestVO> htPo = null;

			// 销售接口返回值 2004-11-23 HashMap<String:合同PK，UFDouble：应收余额合计> 
			HashMap<String, UFDouble> htSo = null;
			
			//应收应付接口返回值关于应付、应收值 2004-11-23 Hashtable<String:合同PK，UFDouble：应收/付余额合计> 
			Hashtable<String ,UFDouble> htArapYf = null;
			Hashtable<String ,UFDouble> htArapYs = null;
			Hashtable<String ,UFDouble> htArapFk = null;
			Hashtable<String ,UFDouble> htArapSk = null;
			
		
			
			// 调用应收应付的接口：
			ContractQueryVO voCond = new ContractQueryVO();
			voCond.setPk_corp(pk_corp);
			voCond.setSubcolumn("ct_code");
			voCond.setSubsql(getSqlToArapNew(pk_corp, sWhere, inbusitype));
			//对于Z5多条记录的收付方向不一致所以要增加一个
			ContractQueryVO voCondZ5fk = new ContractQueryVO();
			ContractQueryVO voCondZ5sk = new ContractQueryVO();
			ContractQueryVO voCondForYf = new ContractQueryVO();
			ContractQueryVO voCondForYs = new ContractQueryVO();
			
			if(BillType.OTHER.equals(sBillType)){
				//ContractQueryVO voCondZ5fk = new ContractQueryVO();
				voCondZ5fk.setPk_corp(pk_corp);
				voCondZ5fk.setSubcolumn("ct_code");
				voCondZ5fk.setSubsql(getSqlToArapNew(pk_corp, sWhere, inbusitype));
				voCondZ5fk.setDjdl("fk");
				
				//ContractQueryVO voCondZ5sk = new ContractQueryVO();
				voCondZ5sk.setPk_corp(pk_corp);
				voCondZ5sk.setSubcolumn("ct_code");
				voCondZ5sk.setSubsql(getSqlToArapNew(pk_corp, sWhere, inbusitype));
				voCondZ5sk.setDjdl("sk");
				
				//ContractQueryVO voCondForYf = new ContractQueryVO();
				voCondForYf.setPk_corp(pk_corp);
				voCondForYf.setSubcolumn("ct_code");
				voCondForYf.setSubsql(getSqlToArapNew(pk_corp, sWhere, inbusitype)); 
				voCondForYf.setDjdl("yf");
				
				//ContractQueryVO voCondForYs = new ContractQueryVO();
				voCondForYs.setPk_corp(pk_corp);
				voCondForYs.setSubcolumn("ct_code");
				voCondForYs.setSubsql(getSqlToArapNew(pk_corp, sWhere, inbusitype)); 
				voCondForYs.setDjdl("ys");
			}
		
			
			PurBackToCtDMO dmo1 = new PurBackToCtDMO();
			voRet = dmo1.qryExecImprest(alConds,inbusitype);
			
			// 判断应收应付是否启用：
			boolean isArap = srv.isEnabled(pk_corp, ProductCode.PROD_AR);
			if (!isArap) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000157")
																	 * @res
																	 * "没有启用应收应付产品,不能查询"
																	 );
			}
			
			if ( BillType.PURDAILY.equals(sBillType)) {
				//判断采购系统是否启用:
				boolean isPurUsed = srv.isEnabled(pk_corp, ProductCode.PROD_PO);
				if (!isPurUsed) {
					throw new nc.vo.pub.BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
									"SCMCOMMON", "UPPSCMCommon-000156")
																		 * @res
																		 * "没有启用采购产品,不能查询"
																		 );
				}
				IPuToCt_PoToCt po = (IPuToCt_PoToCt) NCLocator.getInstance()
				.lookup(IPuToCt_PoToCt.class.getName());
				if (po != null)
					htPo = po.queryCtPayExecInfo(pk_corp, getSqlToPu(pk_corp,
					sWhere));
				voCond.setDjdl("fk");
				inbusitype = 0 ;
			}
			else if ( BillType.SALEDAILY.equals(sBillType)) {
				// 判断销售系统是否启用:
				boolean isSoUsed = srv.isEnabled(pk_corp, ProductCode.PROD_SO);
				if (!isSoUsed) {
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000266")没有启用销售产品,不能查询);
				}
				String[] sCtPksToSo = null;
				if (voRet != null && voRet.length > 0) {
					int iCtPkLen = voRet.length;
					sCtPksToSo = new String[iCtPkLen]; 
					for (int i = 0; i < iCtPkLen; i++) {
						sCtPksToSo[i] = (String) voRet[i]
						   							.getAttributeValue("pk_ct_manage");
					}
					nc.itf.so.service.ISoToCt so = (nc.itf.so.service.ISoToCt) NCLocator.getInstance()
					.lookup(nc.itf.so.service.ISoToCt.class.getName());
					if (so != null)
						htSo = so.querySOPayRemainMny(sCtPksToSo);
					voCond.setDjdl("sk");
					inbusitype = 1 ;
				}
			}
			else if ( BillType.OTHER.equals(sBillType)) {			
				if (voRet != null && voRet.length > 0) {
					
						htArapYf = ArapHelper.getContractPr(voCondForYf);
						htArapYs = ArapHelper.getContractPr(voCondForYs);
						htArapFk = ArapHelper.getContractPr(voCondZ5fk);
						htArapSk = ArapHelper.getContractPr(voCondZ5sk);
						inbusitype = 2 ;
					}
			}
			
			
		
			// 解耦后新的接口调用 2004-11-23
			Hashtable htArap = ArapHelper.getContractPr(voCond);
			
			if (voRet != null && voRet.length > 0) {
				int iLen = voRet.length;
				// 采购接口返回值哈希表的主键
				String pk_ct_manage = null;
				// 采购接口返回值哈希表的键值[包含发票金额和付款额和应付余额]
				CtExecImprestVO voPo = null;
				// 应收应付接口返回值哈西表的主键
				String ct_code = null;
				// 应收应付接口返回值哈西表的键值[未核销预付款]
				UFDouble ufdNotVerifyMny = null;
				//added by lirr 对于Z5多条记录的收付方向不一致所以要增加一个
				UFDouble ufdNotVerifyMnyFk = null;
				UFDouble ufdNotVerifyMnySk = null;
				
				// 销售接口返回值哈希表的键值[String:合同PK，UFDouble：应收余额合计]
				UFDouble soDRecmny = null;
				//财务接口返回值哈希表的键值[String:合同PK，UFDouble：应收余额合计]

				UFDouble arapDRecmnyYs = null;
				UFDouble arapDRecmnyYf = null;
				
				for (int i = 0; i < iLen; i++) {
					pk_ct_manage = (String) voRet[i]
							.getAttributeValue("pk_ct_manage");
					ct_code = (String) voRet[i].getAttributeValue("ct_code");
					if ( BillType.PURDAILY.equals(sBillType)) {
						if (pk_ct_manage != null && htPo != null && htPo.size() > 0
								&& htPo.containsKey(pk_ct_manage)) {
							voPo = (CtExecImprestVO) htPo.get(pk_ct_manage);
							if (voPo != null) {
								// 将接口的VO设置到DMDataVO中
								voRet[i].setAttributeValue("dapmny", voPo
										.getDapmny());
								voRet[i].setAttributeValue("dinvoicemny", voPo
										.getDinvoicemny());
								voRet[i].setAttributeValue("dpaymny", voPo
										.getDpaymny());
							}
							// 为减少哈希表的检索，将已经处理的VO从中删掉。
							htPo.remove(pk_ct_manage);
						}
						if (ct_code != null && htArap != null && htArap.size() > 0
								&& htArap.containsKey(ct_code)) {
							ufdNotVerifyMny = (UFDouble) htArap.get(ct_code);
							if (ufdNotVerifyMny != null) {
								// 将接口的VO设置到DMDataVO中
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// 为减少哈希表的检索，将已经处理的VO从中删掉。
							htArap.remove(ct_code);
						}
					}
					else if ( BillType.SALEDAILY.equals(sBillType)) {
						if (pk_ct_manage != null && htSo != null && htSo.size() > 0
								&& htSo.containsKey(pk_ct_manage)) {
							soDRecmny = htSo.get(pk_ct_manage);
							if (soDRecmny != null) {
								// 将接口的VO设置到DMDataVO中
								voRet[i].setAttributeValue("dapmny", soDRecmny);
							}
							// 为减少哈希表的检索，将已经处理的VO从中删掉。
							htSo.remove(pk_ct_manage);
						}
						if (ct_code != null && htArap != null && htArap.size() > 0
								&& htArap.containsKey(ct_code)) {
							ufdNotVerifyMny = (UFDouble) htArap.get(ct_code);
							if (ufdNotVerifyMny != null) {
								// 将接口的VO设置到DMDataVO中
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// 为减少哈希表的检索，将已经处理的VO从中删掉。
							htArap.remove(ct_code);
						}
					}
					else if ( BillType.OTHER.equals(sBillType)) {
						
								if (ct_code != null && htArapYf != null && htArapYf.size() > 0
										&& htArapYf.containsKey(ct_code)) {
									arapDRecmnyYf = (UFDouble)htArapYf.get(ct_code);
									if (arapDRecmnyYf != null) {
										// 将接口的VO设置到DMDataVO中
										voRet[i].setAttributeValue("dapmny", arapDRecmnyYf);
									}
									
									// 为减少哈希表的检索，将已经处理的VO从中删掉。
									htArapYf.remove(pk_ct_manage);
									
								}
								if (ct_code != null && htArapYs != null && htArapYs.size() > 0
										&& htArapYs.containsKey(ct_code)) {
									arapDRecmnyYs = (UFDouble)htArapYs.get(ct_code);
									
									if (arapDRecmnyYs != null) {
										// 将接口的VO设置到DMDataVO中
										voRet[i].setAttributeValue("dapmny", arapDRecmnyYs);
									}
									htArapYs.remove(pk_ct_manage);
								}
						
							if (ct_code != null && htArapFk != null && htArapFk.size() > 0
									&& htArapFk.containsKey(ct_code)) {
								ufdNotVerifyMnyFk = (UFDouble) htArapFk.get(ct_code);
								if (ufdNotVerifyMnyFk != null) {
									// 将接口的VO设置到DMDataVO中
									voRet[i].setAttributeValue("dnotverifymny",
											ufdNotVerifyMnyFk);
								}
								// 为减少哈希表的检索，将已经处理的VO从中删掉。
								htArapFk.remove(ct_code);
							}
							if (ct_code != null && htArapSk != null && htArapSk.size() > 0
									&& htArapSk.containsKey(ct_code)) {
								ufdNotVerifyMnySk = (UFDouble) htArapSk.get(ct_code);
								if (ufdNotVerifyMnySk != null) {
									// 将接口的VO设置到DMDataVO中
									voRet[i].setAttributeValue("dnotverifymny",
											ufdNotVerifyMnySk);
								}
								// 为减少哈希表的检索，将已经处理的VO从中删掉。
								htArapSk.remove(ct_code);
							}
						}

				}
			}

			// 计算应付余额
			caculateNeedPayMny(voRet);

		} catch (BusinessException e) {// 防止重复包装的关键处理
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}*/

	/**
	 * 此处插入方法说明。 根据查询条件查出需要分配合同预付款的采购订单信息。 需要调用采购提供的接口。 作者：程起伍 创建日期：(2003-9-16
	 * 10:12:03)
	 * 
	 * @return nc.vo.scm.ctpo.CtAllotImprestPoVO[]：采购订单信息VO数组。
	 * @param alConds
	 *            java.util.ArrayList：提供的查询条件。目前只有一个参数。 0:合同ID
	 * @throws Exception
	 */
	public CtAllotImprestPoVO[] qryNeedAllotImprestBills(
			java.util.ArrayList alConds) throws BusinessException {
		CtAllotImprestPoVO[] voaRet = null;
		if (alConds == null || alConds.size() <= 0)
			return null;
		// 合同主键
		String sPK = (String) alConds.get(0);
		try {
			if (sPK != null) {
				// 调用采购提供的查询接口
				// 解耦后新的接口调用 2004-11-23
				IPuToCt_PoToCt po = (IPuToCt_PoToCt) NCLocator.getInstance()
						.lookup(IPuToCt_PoToCt.class.getName());
				if (po != null)
					voaRet = po.queryOrderInfoForPrePay(sPK);
			}
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:", e);
		}
		return voaRet;
	}

	/**
	 * 转换金额为相应合同所属公司的本币金额
	 * 
	 * @param pptcrvos
	 * @throws Exception
	 * @since 5.0
	 */
	private ArrayList<String> convertMnyToCtCorp(PurBackToCtDMO pbtcdmo,
			ParaPoToCtRewriteVO[] pptcrvos) throws Exception {
		ArrayList<String> alCtRowID = new ArrayList<String>();
		// 根据合同行id查询合同对应的公司
		for (int i = 0; i < pptcrvos.length; i++)
			alCtRowID.add(pptcrvos[i].getCContractRowId());
		// [合同行id，(合同公司, 表头ID)]
		HashMap hmCtCorpAndHeadID = null;
		hmCtCorpAndHeadID = pbtcdmo.queryCtCorpAndHeadIDByCtRowID(alCtRowID);

		// 表头ID
		ArrayList<String> alHeadIDs = new ArrayList<String>();

		String sCtCorp = null;
		String sOrderCorp = null;
		String sHeadID = null;
		ChgPriceMnyVO[] chgMnyVOs = null;

		for (int i = 0; i < pptcrvos.length; i++) {
			// 订单公司
			sOrderCorp = pptcrvos[i].getCorpId();
			// 合同公司
			String[] sValue = (String[]) hmCtCorpAndHeadID.get(pptcrvos[i]
					.getCContractRowId());
			sCtCorp = sValue[0];
			sHeadID = sValue[1];

			if (!alHeadIDs.contains(sHeadID))
				alHeadIDs.add(sHeadID);

			if (sOrderCorp == null || sCtCorp == null
					|| sOrderCorp.trim().equals(sCtCorp))
				continue;

			ChgPriceMnyVO mnyVO = new ChgPriceMnyVO();
			// 转换前数值
			mnyVO.setSrcVal(pptcrvos[i].getDSummny());
			// 转换前公司
			mnyVO.setSrcCorpId(sOrderCorp);
			// 转换目标公司
			mnyVO.setDstCorpId(sCtCorp);
			// 制单时间
			mnyVO.setDRateDate(pptcrvos[i].getBillDate());

			// 金额转换
			chgMnyVOs = ChgDataUtil
					.chgPriceByCorp(new ChgPriceMnyVO[] { mnyVO });
			// 将回写金额置为转换后金额
			pptcrvos[i].setDSummny(chgMnyVOs[0].getDstVal());
		}

		return alHeadIDs;
	}

	/**
	 * 此处插入方法说明。 功能描述:订单执行数量回写 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @param pptcrvos
	 *            nc.vo.scm.ctpo.ParaPoToCtRewriteVO[]
	 * @exception BusinessException
	 *                异常说明。
	 */
	public void writeBackAccuOrdData(ParaPoToCtRewriteVO[] pptcrvos)
			throws BusinessException {

		try {
			if (pptcrvos == null || pptcrvos.length == 0)
				return;

			nc.impl.ct.out.PurBackToCtDMO pbtcdmo = new nc.impl.ct.out.PurBackToCtDMO();
			// 金额转换: 转换金额为相应合同所属公司的本币金额
			ArrayList<String> alHeadIDs = convertMnyToCtCorp(pbtcdmo, pptcrvos);

			// 检查合同的状态是否改变
			// (new CTCheckDMO()).checkStatusBatchWhenRef(alHeadIDs,
			// BillState.VALIDATE);
			
			//modified by liuzy 检查合同控制范围，并发问题
			//发现订单会校验合同的时间戳
//			String[] sPk_ct_manage = new String[alHeadIDs.size()];
//			alHeadIDs.toArray(sPk_ct_manage);
//			String sOrderPk_corp = pptcrvos[0].getCorpId();
//
//			if (!(new CTCheckDMO()).checkCTScope(sPk_ct_manage, sOrderPk_corp)
//					.booleanValue()) {
//				throw new BusinessException("合同控制范围发生变化，当前公司不在所选合同的控制范围内");
//			}

			// 先保存
			pbtcdmo.writeBackAccuOrdData(pptcrvos);

			// 再校验
			boolean bUserChoose = pptcrvos[0].isFirstTime();
			if (!bUserChoose) // 二次确认
				return;

			TypeDMO typedmo = new TypeDMO();
			String[] sContractBIDs = new String[pptcrvos.length];
			for (int i = 0; i < pptcrvos.length; i++) {
				sContractBIDs[i] = pptcrvos[i].getCContractRowId();
			}
			// 查得有可能报错的VO
			Hashtable ht = typedmo.queryByContractIDs(sContractBIDs);

			boolean bMustException = false;
			boolean bNeedException = false;
			Hashtable htMust = new Hashtable(); // 直接抛错的合同号
			Hashtable htNeedReply = new Hashtable(); // 需要用户确认的合同号
			Hashtable htFullCheck = new Hashtable(); // 需要全单检查的单据ID

			//added by lirr 2008-12-23合同生效需获得报错的行id
			ArrayList<String> alNeedReplyPk = new ArrayList<String>();
			// 组织错误提示
			DMDataVO dmdvo = null;
			int icontroltype = 0;
			UFBoolean bismustcontrol = null;
			String sCtCode = null, sRowNo = null; // 合同号，合同行号
			UFDouble ufd0 = new UFDouble(0);
			UFDouble ufdNumMinus = null; // 容差数量
			UFDouble ufdMnyMinus = null; // 容差金额

			String sMessage = null;

			for (int i = 0; i < pptcrvos.length; i++) {
				if (ht.containsKey(pptcrvos[i].getCContractRowId())) {
					dmdvo = (DMDataVO) ht.get(pptcrvos[i].getCContractRowId());
					if (null == dmdvo.getAttributeValue("controltype")) {
						icontroltype = OrderControlType.NUN;
					} else {
						icontroltype = Integer.parseInt(dmdvo
								.getAttributeValue("controltype").toString());
					}
					bismustcontrol = new UFBoolean(dmdvo
							.getAttributeValue("ismustcontrol") == null
							|| dmdvo.getAttributeValue("ismustcontrol")
									.toString().trim().length() == 0 ? "N"
							: dmdvo.getAttributeValue("ismustcontrol")
									.toString());

					sCtCode = (String) dmdvo.getAttributeValue("ct_code");
					// modified by lirr 2009-9-25下午01:16:04 提示改为订单号
					sRowNo = (String) dmdvo.getAttributeValue("crowno");
					//sRowNo = (String) pptcrvos[i].getM_cOrderCode();  //waglei 2013-12-19 恢复回来
					//added by lirr 2008-12-23 合同行id
					String sCtLinePk = (String) pptcrvos[i].getCContractRowId();
					ufdNumMinus = (UFDouble) dmdvo
							.getAttributeValue("numminus");
					ufdMnyMinus = (UFDouble) dmdvo
							.getAttributeValue("moneyminus");

					switch (icontroltype) {
					case OrderControlType.NUM: // Control quantity
					{
						if (ufdNumMinus.compareTo(ufd0) > 0) {

							// modify by liuzy 2007-11-13
							// 给采购、销售订单报错时提示超出的数量或金额是多少

							/* @res "合同{0}，行号{1}，数量超出!" */
							sMessage = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID(
											"4020pub",
											"UPP4020pub-000210",
											null,
											new String[] { sCtCode, sRowNo,
													ufdNumMinus.toString() });

							if (bismustcontrol.booleanValue()) {
								htMust.put(sCtCode, sMessage + "\r\n");
								bMustException = true;
							} else {
								htNeedReply.put(sCtCode, sMessage + "\r\n");
								alNeedReplyPk.add(sCtLinePk);
								bNeedException = true;
							}
						}
						break;
					}
					case OrderControlType.MON: // Control sum
					{
						if (ufdMnyMinus.compareTo(ufd0) > 0) {

							// modify by liuzy 2007-11-13
							// 给采购、销售订单报错时提示超出的数量或金额是多少

							/* @res "合同{0}，行号{1}，金额超出!" */
							sMessage = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID(
											"4020pub",
											"UPP4020pub-000211",
											null,
											new String[] { sCtCode, sRowNo,
													ufdMnyMinus.toString() });

							if (bismustcontrol.booleanValue()) {
								htMust.put(sCtCode, sMessage + "\r\n");
								bMustException = true;
							} else {
								htNeedReply.put(sCtCode, sMessage + "\r\n");
								bNeedException = true;
								alNeedReplyPk.add(sCtLinePk);
							}
						}
						break;
					}
					case OrderControlType.NUM_MON: {
						if (ufdNumMinus.compareTo(ufd0) > 0) {

							// modify by liuzy 2007-11-13
							// 给采购、销售订单报错时提示超出的数量或金额是多少

							/* @res "合同{0}，行号{1}，数量超出!" */
							sMessage = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID(
											"4020pub",
											"UPP4020pub-000210",
											null,
											new String[] { sCtCode, sRowNo,
													ufdNumMinus.toString() });

							if (bismustcontrol.booleanValue()) {
								htMust.put(sCtCode, sMessage + "\r\n");
								bMustException = true;
							} else {
								htNeedReply.put(sCtCode, sMessage + "\r\n");
								bNeedException = true;
								alNeedReplyPk.add(sCtLinePk);
							}
						} else if (ufdMnyMinus.compareTo(ufd0) > 0) {

							// modify by liuzy 2007-11-13
							// 给采购、销售订单报错时提示超出的数量或金额是多少

							/* @res "合同{0}，行号{1}，金额超出!" */
							sMessage = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID(
											"4020pub",
											"UPP4020pub-000211",
											null,
											new String[] { sCtCode, sRowNo,
													ufdMnyMinus.toString() });

							if (bismustcontrol.booleanValue()) {
								htMust.put(sCtCode, sMessage + "\r\n");
								bMustException = true;
							} else {
								htNeedReply.put(sCtCode, sMessage + "\r\n");
								bNeedException = true;
								alNeedReplyPk.add(sCtLinePk);
							}
						}
						break;
					}
					case OrderControlType.NUMALL: {
						htFullCheck.put(
								dmdvo.getAttributeValue("pk_ct_manage"), dmdvo);
						break;
					}
					case OrderControlType.MONALL: {
						htFullCheck.put(
								dmdvo.getAttributeValue("pk_ct_manage"), dmdvo);
						break;
					}
					}
				}
			}

			// 总数量和总金额超出校验
			if (!htFullCheck.isEmpty()) {
				String[] sFullCheckID = new DMDataVO()
						.getAllStrKeysFromHashtable(htFullCheck);
				DMDataVO[] dmdvos = pbtcdmo
						.queryFullNumByContractHIDs(sFullCheckID);
				String sContractHID = null;
				for (int i = 0; i < dmdvos.length; i++) {
					sContractHID = dmdvos[i].getAttributeValue("pk_ct_manage")
							.toString();
					dmdvo = (DMDataVO) htFullCheck.get(sContractHID);
					icontroltype = Integer.parseInt(dmdvo.getAttributeValue(
							"controltype").toString());
					bismustcontrol = new UFBoolean(dmdvo.getAttributeValue(
							"ismustcontrol").toString());
					bismustcontrol = (bismustcontrol == null ? new UFBoolean(
							false) : bismustcontrol);
					sCtCode = (String) dmdvo.getAttributeValue("ct_code");
					switch (icontroltype) {
					case OrderControlType.NUMALL: {
						if ((new UFDouble(dmdvos[i].getAttributeValue(
								"numminus").toString())).doubleValue() > 0) {

							/* @res "合同{0}总数量超出!" */
							sMessage = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4020pub", "UPP4020pub-000123",
											null, new String[] { sCtCode });

							if (bismustcontrol.booleanValue()) {
								htMust.put(sCtCode, sMessage + "\r\n");
								bMustException = true;
							} else {
								htNeedReply.put(sCtCode, sMessage + "\r\n");
								bNeedException = true;
							}
						}
						break;
					}
					case OrderControlType.MONALL: {
						if ((new UFDouble(dmdvos[i].getAttributeValue(
								"moneyminus").toString())).doubleValue() > 0) {

							/* @res "合同{0}总金额超出!" */
							sMessage = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4020pub", "UPP4020pub-000124",
											null, new String[] { sCtCode });

							if (bismustcontrol.booleanValue()) {
								htMust.put(sCtCode, sMessage + "\r\n");
								bMustException = true;
							} else {
								htNeedReply.put(sCtCode, sMessage + "\r\n");
								bNeedException = true;
							}
						}
						break;
					}
					}
				}
			}

			// 报出错误
			if (bMustException) {
				StringBuffer sbMust = new StringBuffer(); // 直接抛错的
				java.util.Enumeration enumError = htMust.elements();
				while (enumError.hasMoreElements()) {
					sbMust.append(enumError.nextElement().toString());
				}
				throw new BusinessException(sbMust.toString());
			} else if (bUserChoose && bNeedException) {
				StringBuffer sbNeedReply = new StringBuffer(); // 需要用户确认的
				java.util.Enumeration enumAffirm = htNeedReply.elements();
				while (enumAffirm.hasMoreElements()) {
					sbNeedReply.append(enumAffirm.nextElement().toString());
				}
				sbNeedReply.append(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000125")/*
																	 * @res
																	 * "请确定是否继续?"
				//added by lirr 2008-12-23													 */);
				if(alNeedReplyPk.size() > 0){
					String[] arrCtLinePks =  new String[alNeedReplyPk.size()];
					arrCtLinePks = alNeedReplyPk.toArray(arrCtLinePks);
					SaveHintException saveHintEx = new SaveHintException(sbNeedReply.toString(),arrCtLinePks);
					throw saveHintEx;
				}
				else
					throw new SaveHintException(sbNeedReply.toString());
			}

		} catch (Exception e) {
			reportException(e);
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException("Caused by:" + e.getMessage(), e);
		}
	}

	/**
	 * 此处插入方法说明。 分配完预付款后回写采购订单 作者：程起伍 创建日期：(2003-9-16 10:34:56)
	 * 
	 * @param voaBack
	 *            nc.vo.scm.ctpo.CtAllotImprestPoVO[]
	 * @throws Exception
	 */
	public void writeBackImprest(CtAllotImprestPoVO[] voaBack)
			throws BusinessException {
		try {
			if (voaBack != null && voaBack.length > 0) {
				// 调用采购接口。
				// 解耦后新的接口调用 2004-11-23
				IPuToCt_PoToCt po = (IPuToCt_PoToCt) NCLocator.getInstance()
						.lookup(IPuToCt_PoToCt.class.getName());
				if (po != null)
					po.writeBackPrePayMny(voaBack);
			}
		} catch (Exception e) {
			reportException(e);
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000267")/*合同回写订单预付款时出错! */);
		}
	}

	/**
	 * 将异常信息打印到控制台。
	 * 
	 * @param e
	 *            java.lang.Exception
	 */
	protected void reportException(Exception e) {
		nc.vo.scm.pub.SCMEnv.out(e);
	}
	/**
	 * 整理方法 获得应收应付未核销金额
	 * 作者：lirr
	 * 日期：2008-12-3下午01:02:34
	 * 修改日期 2008-12-3下午01:02:34 修改人，lirr 修改原因，注释标志：
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	 */
	private ContractQueryVO getCondVoForArap(String sBillType,String sWhere ,String pk_corp )
			throws BusinessException {
		ContractQueryVO voCond = null;
		
			try{
				voCond = new ContractQueryVO();
				voCond.setPk_corp(pk_corp);
				int inbusitype = 0 ;
				if ( BillType.PURDAILY.equals(sBillType)) {
					inbusitype = 0 ;
					//voCond.setSubcolumn("pk_ct_manage_b");
				}
				else if ( BillType.SALEDAILY.equals(sBillType)) {
					inbusitype = 1 ;
					//voCond.setSubcolumn("pk_ct_manage");
				}
				else if ( BillType.OTHER.equals(sBillType)) {
					inbusitype = 2 ;
					//voCond.setSubcolumn("pk_ct_manage");
				}
				voCond = new ContractQueryVO();
				voCond.setPk_corp(pk_corp);
				//modified by lirr 2009-03-03 v55改为按照合同id查询
				//voCond.setSubcolumn("ct_code");
				voCond.setSubcolumn("ddlx");
				voCond.setSubsql(getSqlToArapNew(pk_corp, sWhere, inbusitype));
			

		}catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException(e);
		}
		return voCond;
	}
	/**
	 * 整理方法 获得应收应付未核销金额
	 * 作者：lirr
	 * 日期：2008-12-3下午01:02:34
	 * 修改日期 2008-12-3下午01:02:34 修改人，lirr 修改原因，注释标志：
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	 */
	private Hashtable getHtArap(String pk_corp,ContractQueryVO voCond)
			throws BusinessException {
			Hashtable htArap = null;
			try{
			ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
					.getInstance().lookup(
							ICreateCorpQueryService.class.getName());
			
			// 判断应收应付是否启用：
			boolean isArap = srv.isEnabled(pk_corp, ProductCode.PROD_AR);
			if (!isArap) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000157")/*
																	 * @res
																	 * "没有启用应收应付产品,不能查询"
																	 */);
			}
			htArap = ArapHelper.getContractPr(voCond);
			

		} catch (BusinessException e) {// 防止重复包装的关键处理
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return htArap;
	}
	/**
	 * 整理方法 获得采购合同执行情况
	 * 作者：lirr
	 * 日期：2008-12-3下午01:02:34
	 * 修改日期 2008-12-3下午01:02:34 修改人，lirr 修改原因，注释标志：
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	* 修改日期 2009-03-03 修改人，lirr  v55改为按照合同id查询	
	 */
	private DMDataVO[]  qryPuExecImprest(DMDataVO[] voRet,String pk_corp,ContractQueryVO voCond,String sWhere )
			throws BusinessException {
		//采购接口的返回值
		Hashtable<String,CtExecImprestVO> htPo = null;
		try{
			ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
			.getInstance().lookup(
			ICreateCorpQueryService.class.getName());
			//判断采购系统是否启用:
			boolean isPurUsed = srv.isEnabled(pk_corp, ProductCode.PROD_PO);
			/*if (!isPurUsed) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000156")
																	 * @res
																	 * "没有启用采购产品,不能查询"
																	 );
			}
			IPuToCt_PoToCt po = (IPuToCt_PoToCt) NCLocator.getInstance()
			.lookup(IPuToCt_PoToCt.class.getName());
			if (po != null)
				htPo = po.queryCtPayExecInfo(pk_corp, getSqlToPu(pk_corp,
				sWhere));*/
			//modified by lirr 2008-12-05 若采购模块没有启动也应该允许查询
			if (isPurUsed) {
				IPuToCt_PoToCt po = (IPuToCt_PoToCt) NCLocator.getInstance()
				.lookup(IPuToCt_PoToCt.class.getName());
				if (po != null)
					htPo = po.queryCtPayExecInfo(pk_corp, getSqlToPu(pk_corp,
					sWhere));
			}
			
			voCond.setDjdl("fk");
			Hashtable htArap = getHtArap(pk_corp,voCond);
			if (voRet != null && voRet.length > 0) {
				int iLen = voRet.length;
				// 采购接口返回值哈希表的主键
				String pk_ct_manage = null;
				// 采购接口返回值哈希表的键值[包含发票金额和付款额和应付余额]
				CtExecImprestVO voPo = null;
				// 应收应付接口返回值哈西表的主键
				//String ct_code = null;
				// 应收应付接口返回值哈西表的键值[未核销预付款]
				UFDouble ufdNotVerifyMny = null;
				UFDouble ufd0 = new UFDouble(0.0);
				
				for (int i = 0; i < iLen; i++) {
					pk_ct_manage = (String) voRet[i]
							.getAttributeValue("pk_ct_manage");
					//ct_code = (String) voRet[i].getAttributeValue("ct_code");

						if (pk_ct_manage != null && htPo != null && htPo.size() > 0
								&& htPo.containsKey(pk_ct_manage)) {
							voPo = (CtExecImprestVO) htPo.get(pk_ct_manage);
							if (voPo != null) {
								// 将接口的VO设置到DMDataVO中
								voRet[i].setAttributeValue("dapmny", voPo
										.getDapmny());
								voRet[i].setAttributeValue("dinvoicemny", voPo
										.getDinvoicemny());
								voRet[i].setAttributeValue("dpaymny", voPo
										.getDpaymny());
							}
							// 为减少哈希表的检索，将已经处理的VO从中删掉。
							htPo.remove(pk_ct_manage);
						} 
						//added by lirr 2008-12-05 若采购模块没有启动也应该允许查询
						else
						{
							voRet[i].setAttributeValue("dapmny", ufd0);
							voRet[i].setAttributeValue("dinvoicemny",ufd0);
							voRet[i].setAttributeValue("dpaymny", ufd0);
						}
						
						/*if (ct_code != null && htArap != null && htArap.size() > 0
								&& htArap.containsKey(ct_code)) {
							ufdNotVerifyMny = (UFDouble) htArap.get(ct_code);
							if (ufdNotVerifyMny != null) {
								// 将接口的VO设置到DMDataVO中
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// 为减少哈希表的检索，将已经处理的VO从中删掉。
							htArap.remove(ct_code);
						}*/
						if (pk_ct_manage != null && htArap != null && htArap.size() > 0
								&& htArap.containsKey(pk_ct_manage)) {
							ufdNotVerifyMny = (UFDouble) htArap.get(pk_ct_manage);
							if (ufdNotVerifyMny != null) {
								// 将接口的VO设置到DMDataVO中
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// 为减少哈希表的检索，将已经处理的VO从中删掉。
							htArap.remove(pk_ct_manage);
						}
					}
				//计算应付余额
				caculateNeedPayMny(voRet);
			}
			
		} catch (BusinessException e) {// 防止重复包装的关键处理
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}
	/**
	 * 整理方法 获得销售合同执行情况
	 * 作者：lirr
	 * 日期：2008-12-3下午01:02:34
	 * 修改日期 2008-12-3下午01:02:34 修改人，lirr 修改原因，注释标志：
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	 * 修改日期 2009-03-03 修改人，lirr  v55改为按照合同id查询		
	 */
	private DMDataVO[]  qrySoExecImprest(DMDataVO[] voRet,String pk_corp,ContractQueryVO voCond,String sWhere )
			throws BusinessException {
		//销售接口返回值 2004-11-23 HashMap<String:合同PK，UFDouble：应收余额合计> 
		HashMap<String, UFDouble> htSo = null;
		try{
			ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
			.getInstance().lookup(
			ICreateCorpQueryService.class.getName());
			//判断销售系统是否启用:
			boolean isSoUsed = srv.isEnabled(pk_corp, ProductCode.PROD_SO);
			//modified  by lirr 2008-12-05 若销售模块没有启动也应该允许查询
			/*if (!isSoUsed) {
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000266")没有启用销售产品,不能查询);
			}*/
			String[] sCtPksToSo = null;
			if (voRet != null && voRet.length > 0) {
				int iCtPkLen = voRet.length;
				sCtPksToSo = new String[iCtPkLen]; 
				for (int i = 0; i < iCtPkLen; i++) {
					sCtPksToSo[i] = (String) voRet[i]
					   							.getAttributeValue("pk_ct_manage");
				}
				//modified  by lirr 2008-12-05 若销售模块没有启动也应该允许查询
				if(isSoUsed){
					nc.itf.so.service.ISoToCt so = (nc.itf.so.service.ISoToCt) NCLocator.getInstance()
					.lookup(nc.itf.so.service.ISoToCt.class.getName());
					if (so != null)
						htSo = so.querySOPayRemainMny(sCtPksToSo);
				}
				/*nc.itf.so.service.ISoToCt so = (nc.itf.so.service.ISoToCt) NCLocator.getInstance()
				.lookup(nc.itf.so.service.ISoToCt.class.getName());
				if (so != null)
					htSo = so.querySOPayRemainMny(sCtPksToSo);*/
				voCond.setDjdl("sk");
			}
			Hashtable htArap = getHtArap(pk_corp,voCond);
			if (voRet != null && voRet.length > 0) {
				int iLen = voRet.length;
				// 采购接口返回值哈希表的主键
				String pk_ct_manage = null;
				//销售接口返回值哈希表的键值[String:合同PK，UFDouble：应收余额合计]
				UFDouble soDRecmny = null;
				//财务接口返回值哈希表的键值[String:合同PK，UFDouble：应收余额合计]

				// 应收应付接口返回值哈西表的主键
				//String ct_code = null;
				// 应收应付接口返回值哈西表的键值[未核销预付款]
				UFDouble ufdNotVerifyMny = null;

				for (int i = 0; i < iLen; i++) {
					pk_ct_manage = (String) voRet[i]
							.getAttributeValue("pk_ct_manage");
					//ct_code = (String) voRet[i].getAttributeValue("ct_code");

					if (pk_ct_manage != null && htSo != null && htSo.size() > 0
							&& htSo.containsKey(pk_ct_manage)) {
						soDRecmny = htSo.get(pk_ct_manage);
						if (soDRecmny != null) {
							// 将接口的VO设置到DMDataVO中
							voRet[i].setAttributeValue("dapmny", soDRecmny);
						}
						// 为减少哈希表的检索，将已经处理的VO从中删掉。
						htSo.remove(pk_ct_manage);
					}
					//added by lirr 2008-12-05 若销售模块没有启动也应该允许查询
					else {
						voRet[i].setAttributeValue("dapmny", new UFDouble(0.0));
						
					}
					/*if (ct_code != null && htArap != null && htArap.size() > 0
							&& htArap.containsKey(ct_code)) {
						ufdNotVerifyMny = (UFDouble) htArap.get(ct_code);
						if (ufdNotVerifyMny != null) {
							// 将接口的VO设置到DMDataVO中
							voRet[i].setAttributeValue("dnotverifymny",
									ufdNotVerifyMny);
						}
						// 为减少哈希表的检索，将已经处理的VO从中删掉。
						htArap.remove(ct_code);
					}*/
					
					if (pk_ct_manage != null && htArap != null && htArap.size() > 0
							&& htArap.containsKey(pk_ct_manage)) {
						ufdNotVerifyMny = (UFDouble) htArap.get(pk_ct_manage);
						if (ufdNotVerifyMny != null) {
							// 将接口的VO设置到DMDataVO中
							voRet[i].setAttributeValue("dnotverifymny",
									ufdNotVerifyMny);
						}
						// 为减少哈希表的检索，将已经处理的VO从中删掉。
						htArap.remove(pk_ct_manage);
					}
						
					}
				//计算应付余额
				caculateNeedPayMny(voRet);
			}

		} catch (BusinessException e) {// 防止重复包装的关键处理
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}
	/**
	 * 整理方法 获得其它合同执行情况
	 * 作者：lirr
	 * 日期：2008-12-3下午01:02:34
	 * 修改日期 2008-12-3下午01:02:34 修改人，lirr 修改原因，注释标志：
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	 * 修改日期 2009-03-03 修改人，lirr  v55改为按照合同id查询
	 */
	private DMDataVO[]  qryOtherExecImprest(DMDataVO[] voRet,String pk_corp,ContractQueryVO voCond,String sWhere )
			throws BusinessException {
//		应收应付接口返回值关于应付、应收值 2004-11-23 Hashtable<String:合同PK，UFDouble：应收/付余额合计> 
		Hashtable<String ,UFDouble> htArapYf = null;
		Hashtable<String ,UFDouble> htArapYs = null;
		Hashtable<String ,UFDouble> htArapFk = null;
		Hashtable<String ,UFDouble> htArapSk = null;
		
//		对于Z5多条记录的收付方向不一致所以要增加一个
		ContractQueryVO voCondZ5fk = new ContractQueryVO();
		ContractQueryVO voCondZ5sk = new ContractQueryVO();
		ContractQueryVO voCondForYf = new ContractQueryVO();
		ContractQueryVO voCondForYs = new ContractQueryVO();

		//ContractQueryVO voCondZ5fk = new ContractQueryVO();
		voCondZ5fk.setPk_corp(pk_corp);
		//voCondZ5fk.setSubcolumn("ct_code");
		voCondZ5fk.setSubcolumn("ddlx");
		voCondZ5fk.setSubsql(getSqlToArapNew(pk_corp, sWhere, 2));
		voCondZ5fk.setDjdl("fk");
		
		//ContractQueryVO voCondZ5sk = new ContractQueryVO();
		voCondZ5sk.setPk_corp(pk_corp);
		//voCondZ5sk.setSubcolumn("ct_code");
		voCondZ5sk.setSubcolumn("ddlx");
		voCondZ5sk.setSubsql(getSqlToArapNew(pk_corp, sWhere, 2));
		voCondZ5sk.setDjdl("sk");
		
		//ContractQueryVO voCondForYf = new ContractQueryVO();
		voCondForYf.setPk_corp(pk_corp);
		//voCondForYf.setSubcolumn("ct_code");
		voCondForYf.setSubcolumn("ddlx");
		voCondForYf.setSubsql(getSqlToArapNew(pk_corp, sWhere, 2)); 
		voCondForYf.setDjdl("yf");
		
		//ContractQueryVO voCondForYs = new ContractQueryVO();
		voCondForYs.setPk_corp(pk_corp);
		//voCondForYs.setSubcolumn("ct_code");
		voCondForYs.setSubcolumn("ddlx");
		voCondForYs.setSubsql(getSqlToArapNew(pk_corp, sWhere, 2)); 
		voCondForYs.setDjdl("ys");
		htArapYf = getHtArap(pk_corp, voCondForYf);
		htArapYs = getHtArap(pk_corp,voCondForYs);
		htArapFk = getHtArap(pk_corp,voCondZ5fk);
		htArapSk = getHtArap(pk_corp,voCondZ5sk);
		try{			
			if (voRet != null && voRet.length > 0) {
				int iLen = voRet.length;
				// 采购接口返回值哈希表的主键
				String pk_ct_manage = null;
				// 应收应付接口返回值哈西表的主键
				//String ct_code = null;
				//added by lirr 对于Z5多条记录的收付方向不一致所以要增加一个
				UFDouble ufdNotVerifyMnyFk = null;
				UFDouble ufdNotVerifyMnySk = null;
				
				UFDouble arapDRecmnyYs = null;
				UFDouble arapDRecmnyYf = null;
				

				for (int i = 0; i < iLen; i++) {
					pk_ct_manage = (String) voRet[i]
							.getAttributeValue("pk_ct_manage");
					//ct_code = (String) voRet[i].getAttributeValue("ct_code");


					
					/*if (ct_code != null && htArapYf != null && htArapYf.size() > 0
							&& htArapYf.containsKey(ct_code)) {*/
					if (pk_ct_manage != null && htArapYf != null && htArapYf.size() > 0
							&& htArapYf.containsKey(pk_ct_manage)) {	
						arapDRecmnyYf = (UFDouble)htArapYf.get(pk_ct_manage);
						if (arapDRecmnyYf != null) {
							// 将接口的VO设置到DMDataVO中
							voRet[i].setAttributeValue("dapmny", arapDRecmnyYf);
						}
						
						// 为减少哈希表的检索，将已经处理的VO从中删掉。
						htArapYf.remove(pk_ct_manage);
						
					}
					/*if (ct_code != null && htArapYs != null && htArapYs.size() > 0
							&& htArapYs.containsKey(ct_code)) {*/
					if (pk_ct_manage != null && htArapYs != null && htArapYs.size() > 0
							&& htArapYs.containsKey(pk_ct_manage)) {	
						arapDRecmnyYs = (UFDouble)htArapYs.get(pk_ct_manage);
						
						if (arapDRecmnyYs != null) {
							// 将接口的VO设置到DMDataVO中
							voRet[i].setAttributeValue("dapmny", arapDRecmnyYs);
						}
						htArapYs.remove(pk_ct_manage);
					}
			
			/*	if (ct_code != null && htArapFk != null && htArapFk.size() > 0
						&& htArapFk.containsKey(ct_code)) {*/
				if (pk_ct_manage != null && htArapFk != null && htArapFk.size() > 0
							&& htArapFk.containsKey(pk_ct_manage)) {	
					ufdNotVerifyMnyFk = (UFDouble) htArapFk.get(pk_ct_manage);
					if (ufdNotVerifyMnyFk != null) {
						// 将接口的VO设置到DMDataVO中
						voRet[i].setAttributeValue("dnotverifymny",
								ufdNotVerifyMnyFk);
					}
					// 为减少哈希表的检索，将已经处理的VO从中删掉。
					htArapFk.remove(pk_ct_manage);
				}
				/*if (ct_code != null && htArapSk != null && htArapSk.size() > 0
						&& htArapSk.containsKey(ct_code)) {*/
				if (pk_ct_manage != null && htArapSk != null && htArapSk.size() > 0
						&& htArapSk.containsKey(pk_ct_manage)) {
					ufdNotVerifyMnySk = (UFDouble) htArapSk.get(pk_ct_manage);
					if (ufdNotVerifyMnySk != null) {
						// 将接口的VO设置到DMDataVO中
						voRet[i].setAttributeValue("dnotverifymny",
								ufdNotVerifyMnySk);
					}
					// 为减少哈希表的检索，将已经处理的VO从中删掉。
					htArapSk.remove(pk_ct_manage);
				}
			
			}
				//计算应付余额
				caculateNeedPayMny(voRet);
		}
	} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e);
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException(e);
		}
		return voRet;
	}
	/**
	 * 此处插入方法说明。 创建日期：(2003-9-16 21:15:03)
	 * 
	 * @return nc.vo.dm.pub.DMDataVO[]
	 * @param alConds
	 *            java.util.ArrayList 0: String sWhere 1: String pk_corp
	 * 
	 * @exception BusinessException
	 *                异常说明。
	 * modified by lirr 2008-08-21 v55需求 销售合同、其他合同 都要增加执行情况查询，因此继续沿用此方法，加单据类型sBillType区分              
	 */
	public DMDataVO[] qryExecImprest(ArrayList alConds ,String sBillType)
			throws BusinessException {
		DMDataVO[] voRet = null; // 返回值
		ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
		.getInstance().lookup(
				ICreateCorpQueryService.class.getName());
		if (alConds == null || alConds.size() <= 0)
			return null;

		try {
			
			String sWhere = (String) alConds.get(0);
			String pk_corp = (String) alConds.get(1);
			int inbusitype = 0 ;
			if ( BillType.PURDAILY.equals(sBillType)) {
				inbusitype = 0 ;
			}
			else if ( BillType.SALEDAILY.equals(sBillType)) {
				inbusitype = 1 ;
			}
			else if ( BillType.OTHER.equals(sBillType)) {
				inbusitype = 2 ;
			}

			PurBackToCtDMO dmo1 = new PurBackToCtDMO();
			voRet = dmo1.qryExecImprest(alConds,inbusitype);
			//判断应收应付是否启用：
			boolean isArap = srv.isEnabled(pk_corp, ProductCode.PROD_AR);
			if (!isArap) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000157")/*
																	 * @res
																	 * "没有启用应收应付产品,不能查询"
																	 */);
			}
			ContractQueryVO voCond = getCondVoForArap(sBillType,sWhere,pk_corp);
			if(BillType.PURDAILY.equals(sBillType)){
				voRet = qryPuExecImprest(voRet,pk_corp,voCond,sWhere);
			}
			else if ( BillType.SALEDAILY.equals(sBillType)) {
				voRet = qrySoExecImprest(voRet,pk_corp,voCond,sWhere);
			}
			else if ( BillType.OTHER.equals(sBillType)) {
				voRet = qryOtherExecImprest(voRet,pk_corp,voCond,sWhere);
			}
			

		} catch (BusinessException e) {// 防止重复包装的关键处理
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}

}