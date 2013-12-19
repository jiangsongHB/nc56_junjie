package nc.impl.ct.out;

/**
 * ��Ĺ��ܡ���;���ִ�BUG���Լ��������˿��ܸ���Ȥ�Ľ��ܡ�
 * ���ߣ�����
 * @version	����޸�����(2003-6-17 11:05:37)
 * @see		��Ҫ�μ���������
 * @since		�Ӳ�Ʒ����һ���汾�����౻��ӽ���������ѡ��
 * �޸��� + �޸�����
 * �޸�˵��
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
	 * PurBackToCtBO ������ע�⡣
	 * 
	 * @exception javax.naming.NamingException
	 *                �쳣˵����
	 * @exception nc.bs.pub.SystemException
	 *                �쳣˵����
	 */
	public PurBackToCtImpl() {
		super();
	}

	/**
	 * �˴����뷽��˵���� ���ߣ������� ���ܣ������ͬӦ����� ���㹫ʽ�� ��ͬӦ����� = �ɹ�Ӧ����� - δ����Ԥ����
	 * �������ڣ�(2003-10-15 12:08:07)
	 * 
	 * @param vo
	 *            nc.vo.dm.pub.DMDataVO[]
	 */
	private void caculateNeedPayMny(DMDataVO[] vo) {
		if (vo != null && vo.length > 0) {
			int iLen = vo.length;
			UFDouble ufdNeedPayMny = null; // �ɹ�Ӧ�����
			UFDouble ufdCtNeedPayMny = null;// ��ͬӦ�����
			UFDouble ufdNotVerify = null; // δ����Ԥ����
			UFDouble ufd0 = new UFDouble(0);
			for (int i = 0; i < iLen; i++) {
				ufdNeedPayMny = (UFDouble) vo[i].getAttributeValue("dapmny");
				ufdNotVerify = (UFDouble) vo[i]
						.getAttributeValue("dnotverifymny");
				// �����Ʊ���Ϊ�գ���ô����Ʊ�����Ϊ0
				if (ufdNeedPayMny == null)
					ufdNeedPayMny = ufd0;
				if (ufdNotVerify == null)
					ufdNotVerify = ufd0;
				ufdCtNeedPayMny = ufdNeedPayMny.sub(ufdNotVerify);
				// ����ͬӦ���������vo
				vo[i].setAttributeValue("ctdapmny", ufdCtNeedPayMny);
			}
		}
	}

	/**
	 * �˴����뷽��˵���� ���ߣ�<author> �������ڣ�(2003-10-15 11:30:10)
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
	 * �˴����뷽��˵���� ���ߣ�<lirr> �������ڣ�(2008-9-26 11:30:10)
	 * 
	 * @return java.lang.String
	 * @param sWhere
	 *            java.lang.String
	 * ����ԭ��v55��ǰֻ�вɹ���ͬ���ã�v55��Ϊ���ۺ�ͬ��������ͬҲ���ã�������Ҫ�Ӳ��� 
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
		//modified by lirr 2009-03-03 ����V55ȡ����ԴΪ�ɹ���ͬ�Ĳɹ��������ɸ�����д�ɹ���ͬ�Ĺ��ܡ�
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
	 * �˴����뷽��˵���� ���ߣ�<author> �������ڣ�(2003-9-25 14:53:46)
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
	 * �˴����뷽��˵���� ���ߣ������� ���ܣ���ѯ��ͬ�Ƿ�����Ч�ĺ�ͬ �������ڣ�(2003-10-8 11:03:12)
	 * 
	 * @return nc.vo.scm.ctpo.CtStatusToPoVO
	 * @param vo
	 *            nc.vo.scm.ctpo.CtStatusToPoVO
	 * @exception BusinessException
	 *                �쳣˵����
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
	 * �˴����뷽��˵���� �������ڣ�(2003-9-16 21:15:03)
	 * 
	 * @return nc.vo.dm.pub.DMDataVO[]
	 * @param alConds
	 *            java.util.ArrayList 0: String sWhere 1: String pk_corp
	 * 
	 * @exception BusinessException
	 *                �쳣˵����
	 * modified by lirr 2008-08-21 v55���� ���ۺ�ͬ��������ͬ ��Ҫ����ִ�������ѯ����˼������ô˷������ӵ�������sBillType����              
	 */
	/*public DMDataVO[] qryExecImprest(ArrayList alConds ,String sBillType)
			throws BusinessException {
		DMDataVO[] voRet = null; // ����ֵ
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
			//�ɹ��ӿڵķ���ֵ
			Hashtable<String,CtExecImprestVO> htPo = null;

			// ���۽ӿڷ���ֵ 2004-11-23 HashMap<String:��ͬPK��UFDouble��Ӧ�����ϼ�> 
			HashMap<String, UFDouble> htSo = null;
			
			//Ӧ��Ӧ���ӿڷ���ֵ����Ӧ����Ӧ��ֵ 2004-11-23 Hashtable<String:��ͬPK��UFDouble��Ӧ��/�����ϼ�> 
			Hashtable<String ,UFDouble> htArapYf = null;
			Hashtable<String ,UFDouble> htArapYs = null;
			Hashtable<String ,UFDouble> htArapFk = null;
			Hashtable<String ,UFDouble> htArapSk = null;
			
		
			
			// ����Ӧ��Ӧ���Ľӿڣ�
			ContractQueryVO voCond = new ContractQueryVO();
			voCond.setPk_corp(pk_corp);
			voCond.setSubcolumn("ct_code");
			voCond.setSubsql(getSqlToArapNew(pk_corp, sWhere, inbusitype));
			//����Z5������¼���ո�����һ������Ҫ����һ��
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
			
			// �ж�Ӧ��Ӧ���Ƿ����ã�
			boolean isArap = srv.isEnabled(pk_corp, ProductCode.PROD_AR);
			if (!isArap) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000157")
																	 * @res
																	 * "û������Ӧ��Ӧ����Ʒ,���ܲ�ѯ"
																	 );
			}
			
			if ( BillType.PURDAILY.equals(sBillType)) {
				//�жϲɹ�ϵͳ�Ƿ�����:
				boolean isPurUsed = srv.isEnabled(pk_corp, ProductCode.PROD_PO);
				if (!isPurUsed) {
					throw new nc.vo.pub.BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
									"SCMCOMMON", "UPPSCMCommon-000156")
																		 * @res
																		 * "û�����òɹ���Ʒ,���ܲ�ѯ"
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
				// �ж�����ϵͳ�Ƿ�����:
				boolean isSoUsed = srv.isEnabled(pk_corp, ProductCode.PROD_SO);
				if (!isSoUsed) {
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000266")û���������۲�Ʒ,���ܲ�ѯ);
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
			
			
		
			// ������µĽӿڵ��� 2004-11-23
			Hashtable htArap = ArapHelper.getContractPr(voCond);
			
			if (voRet != null && voRet.length > 0) {
				int iLen = voRet.length;
				// �ɹ��ӿڷ���ֵ��ϣ�������
				String pk_ct_manage = null;
				// �ɹ��ӿڷ���ֵ��ϣ��ļ�ֵ[������Ʊ���͸�����Ӧ�����]
				CtExecImprestVO voPo = null;
				// Ӧ��Ӧ���ӿڷ���ֵ�����������
				String ct_code = null;
				// Ӧ��Ӧ���ӿڷ���ֵ������ļ�ֵ[δ����Ԥ����]
				UFDouble ufdNotVerifyMny = null;
				//added by lirr ����Z5������¼���ո�����һ������Ҫ����һ��
				UFDouble ufdNotVerifyMnyFk = null;
				UFDouble ufdNotVerifyMnySk = null;
				
				// ���۽ӿڷ���ֵ��ϣ��ļ�ֵ[String:��ͬPK��UFDouble��Ӧ�����ϼ�]
				UFDouble soDRecmny = null;
				//����ӿڷ���ֵ��ϣ��ļ�ֵ[String:��ͬPK��UFDouble��Ӧ�����ϼ�]

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
								// ���ӿڵ�VO���õ�DMDataVO��
								voRet[i].setAttributeValue("dapmny", voPo
										.getDapmny());
								voRet[i].setAttributeValue("dinvoicemny", voPo
										.getDinvoicemny());
								voRet[i].setAttributeValue("dpaymny", voPo
										.getDpaymny());
							}
							// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
							htPo.remove(pk_ct_manage);
						}
						if (ct_code != null && htArap != null && htArap.size() > 0
								&& htArap.containsKey(ct_code)) {
							ufdNotVerifyMny = (UFDouble) htArap.get(ct_code);
							if (ufdNotVerifyMny != null) {
								// ���ӿڵ�VO���õ�DMDataVO��
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
							htArap.remove(ct_code);
						}
					}
					else if ( BillType.SALEDAILY.equals(sBillType)) {
						if (pk_ct_manage != null && htSo != null && htSo.size() > 0
								&& htSo.containsKey(pk_ct_manage)) {
							soDRecmny = htSo.get(pk_ct_manage);
							if (soDRecmny != null) {
								// ���ӿڵ�VO���õ�DMDataVO��
								voRet[i].setAttributeValue("dapmny", soDRecmny);
							}
							// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
							htSo.remove(pk_ct_manage);
						}
						if (ct_code != null && htArap != null && htArap.size() > 0
								&& htArap.containsKey(ct_code)) {
							ufdNotVerifyMny = (UFDouble) htArap.get(ct_code);
							if (ufdNotVerifyMny != null) {
								// ���ӿڵ�VO���õ�DMDataVO��
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
							htArap.remove(ct_code);
						}
					}
					else if ( BillType.OTHER.equals(sBillType)) {
						
								if (ct_code != null && htArapYf != null && htArapYf.size() > 0
										&& htArapYf.containsKey(ct_code)) {
									arapDRecmnyYf = (UFDouble)htArapYf.get(ct_code);
									if (arapDRecmnyYf != null) {
										// ���ӿڵ�VO���õ�DMDataVO��
										voRet[i].setAttributeValue("dapmny", arapDRecmnyYf);
									}
									
									// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
									htArapYf.remove(pk_ct_manage);
									
								}
								if (ct_code != null && htArapYs != null && htArapYs.size() > 0
										&& htArapYs.containsKey(ct_code)) {
									arapDRecmnyYs = (UFDouble)htArapYs.get(ct_code);
									
									if (arapDRecmnyYs != null) {
										// ���ӿڵ�VO���õ�DMDataVO��
										voRet[i].setAttributeValue("dapmny", arapDRecmnyYs);
									}
									htArapYs.remove(pk_ct_manage);
								}
						
							if (ct_code != null && htArapFk != null && htArapFk.size() > 0
									&& htArapFk.containsKey(ct_code)) {
								ufdNotVerifyMnyFk = (UFDouble) htArapFk.get(ct_code);
								if (ufdNotVerifyMnyFk != null) {
									// ���ӿڵ�VO���õ�DMDataVO��
									voRet[i].setAttributeValue("dnotverifymny",
											ufdNotVerifyMnyFk);
								}
								// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
								htArapFk.remove(ct_code);
							}
							if (ct_code != null && htArapSk != null && htArapSk.size() > 0
									&& htArapSk.containsKey(ct_code)) {
								ufdNotVerifyMnySk = (UFDouble) htArapSk.get(ct_code);
								if (ufdNotVerifyMnySk != null) {
									// ���ӿڵ�VO���õ�DMDataVO��
									voRet[i].setAttributeValue("dnotverifymny",
											ufdNotVerifyMnySk);
								}
								// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
								htArapSk.remove(ct_code);
							}
						}

				}
			}

			// ����Ӧ�����
			caculateNeedPayMny(voRet);

		} catch (BusinessException e) {// ��ֹ�ظ���װ�Ĺؼ�����
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}*/

	/**
	 * �˴����뷽��˵���� ���ݲ�ѯ���������Ҫ�����ͬԤ����Ĳɹ�������Ϣ�� ��Ҫ���òɹ��ṩ�Ľӿڡ� ���ߣ������� �������ڣ�(2003-9-16
	 * 10:12:03)
	 * 
	 * @return nc.vo.scm.ctpo.CtAllotImprestPoVO[]���ɹ�������ϢVO���顣
	 * @param alConds
	 *            java.util.ArrayList���ṩ�Ĳ�ѯ������Ŀǰֻ��һ�������� 0:��ͬID
	 * @throws Exception
	 */
	public CtAllotImprestPoVO[] qryNeedAllotImprestBills(
			java.util.ArrayList alConds) throws BusinessException {
		CtAllotImprestPoVO[] voaRet = null;
		if (alConds == null || alConds.size() <= 0)
			return null;
		// ��ͬ����
		String sPK = (String) alConds.get(0);
		try {
			if (sPK != null) {
				// ���òɹ��ṩ�Ĳ�ѯ�ӿ�
				// ������µĽӿڵ��� 2004-11-23
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
	 * ת�����Ϊ��Ӧ��ͬ������˾�ı��ҽ��
	 * 
	 * @param pptcrvos
	 * @throws Exception
	 * @since 5.0
	 */
	private ArrayList<String> convertMnyToCtCorp(PurBackToCtDMO pbtcdmo,
			ParaPoToCtRewriteVO[] pptcrvos) throws Exception {
		ArrayList<String> alCtRowID = new ArrayList<String>();
		// ���ݺ�ͬ��id��ѯ��ͬ��Ӧ�Ĺ�˾
		for (int i = 0; i < pptcrvos.length; i++)
			alCtRowID.add(pptcrvos[i].getCContractRowId());
		// [��ͬ��id��(��ͬ��˾, ��ͷID)]
		HashMap hmCtCorpAndHeadID = null;
		hmCtCorpAndHeadID = pbtcdmo.queryCtCorpAndHeadIDByCtRowID(alCtRowID);

		// ��ͷID
		ArrayList<String> alHeadIDs = new ArrayList<String>();

		String sCtCorp = null;
		String sOrderCorp = null;
		String sHeadID = null;
		ChgPriceMnyVO[] chgMnyVOs = null;

		for (int i = 0; i < pptcrvos.length; i++) {
			// ������˾
			sOrderCorp = pptcrvos[i].getCorpId();
			// ��ͬ��˾
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
			// ת��ǰ��ֵ
			mnyVO.setSrcVal(pptcrvos[i].getDSummny());
			// ת��ǰ��˾
			mnyVO.setSrcCorpId(sOrderCorp);
			// ת��Ŀ�깫˾
			mnyVO.setDstCorpId(sCtCorp);
			// �Ƶ�ʱ��
			mnyVO.setDRateDate(pptcrvos[i].getBillDate());

			// ���ת��
			chgMnyVOs = ChgDataUtil
					.chgPriceByCorp(new ChgPriceMnyVO[] { mnyVO });
			// ����д�����Ϊת������
			pptcrvos[i].setDSummny(chgMnyVOs[0].getDstVal());
		}

		return alHeadIDs;
	}

	/**
	 * �˴����뷽��˵���� ��������:����ִ��������д �������: ����ֵ: �쳣����: ����:
	 * 
	 * @param pptcrvos
	 *            nc.vo.scm.ctpo.ParaPoToCtRewriteVO[]
	 * @exception BusinessException
	 *                �쳣˵����
	 */
	public void writeBackAccuOrdData(ParaPoToCtRewriteVO[] pptcrvos)
			throws BusinessException {

		try {
			if (pptcrvos == null || pptcrvos.length == 0)
				return;

			nc.impl.ct.out.PurBackToCtDMO pbtcdmo = new nc.impl.ct.out.PurBackToCtDMO();
			// ���ת��: ת�����Ϊ��Ӧ��ͬ������˾�ı��ҽ��
			ArrayList<String> alHeadIDs = convertMnyToCtCorp(pbtcdmo, pptcrvos);

			// ����ͬ��״̬�Ƿ�ı�
			// (new CTCheckDMO()).checkStatusBatchWhenRef(alHeadIDs,
			// BillState.VALIDATE);
			
			//modified by liuzy ����ͬ���Ʒ�Χ����������
			//���ֶ�����У���ͬ��ʱ���
//			String[] sPk_ct_manage = new String[alHeadIDs.size()];
//			alHeadIDs.toArray(sPk_ct_manage);
//			String sOrderPk_corp = pptcrvos[0].getCorpId();
//
//			if (!(new CTCheckDMO()).checkCTScope(sPk_ct_manage, sOrderPk_corp)
//					.booleanValue()) {
//				throw new BusinessException("��ͬ���Ʒ�Χ�����仯����ǰ��˾������ѡ��ͬ�Ŀ��Ʒ�Χ��");
//			}

			// �ȱ���
			pbtcdmo.writeBackAccuOrdData(pptcrvos);

			// ��У��
			boolean bUserChoose = pptcrvos[0].isFirstTime();
			if (!bUserChoose) // ����ȷ��
				return;

			TypeDMO typedmo = new TypeDMO();
			String[] sContractBIDs = new String[pptcrvos.length];
			for (int i = 0; i < pptcrvos.length; i++) {
				sContractBIDs[i] = pptcrvos[i].getCContractRowId();
			}
			// ����п��ܱ����VO
			Hashtable ht = typedmo.queryByContractIDs(sContractBIDs);

			boolean bMustException = false;
			boolean bNeedException = false;
			Hashtable htMust = new Hashtable(); // ֱ���״�ĺ�ͬ��
			Hashtable htNeedReply = new Hashtable(); // ��Ҫ�û�ȷ�ϵĺ�ͬ��
			Hashtable htFullCheck = new Hashtable(); // ��Ҫȫ�����ĵ���ID

			//added by lirr 2008-12-23��ͬ��Ч���ñ������id
			ArrayList<String> alNeedReplyPk = new ArrayList<String>();
			// ��֯������ʾ
			DMDataVO dmdvo = null;
			int icontroltype = 0;
			UFBoolean bismustcontrol = null;
			String sCtCode = null, sRowNo = null; // ��ͬ�ţ���ͬ�к�
			UFDouble ufd0 = new UFDouble(0);
			UFDouble ufdNumMinus = null; // �ݲ�����
			UFDouble ufdMnyMinus = null; // �ݲ���

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
					// modified by lirr 2009-9-25����01:16:04 ��ʾ��Ϊ������
					sRowNo = (String) dmdvo.getAttributeValue("crowno");
					//sRowNo = (String) pptcrvos[i].getM_cOrderCode();  //waglei 2013-12-19 �ָ�����
					//added by lirr 2008-12-23 ��ͬ��id
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
							// ���ɹ������۶�������ʱ��ʾ���������������Ƕ���

							/* @res "��ͬ{0}���к�{1}����������!" */
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
							// ���ɹ������۶�������ʱ��ʾ���������������Ƕ���

							/* @res "��ͬ{0}���к�{1}������!" */
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
							// ���ɹ������۶�������ʱ��ʾ���������������Ƕ���

							/* @res "��ͬ{0}���к�{1}����������!" */
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
							// ���ɹ������۶�������ʱ��ʾ���������������Ƕ���

							/* @res "��ͬ{0}���к�{1}������!" */
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

			// ���������ܽ���У��
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

							/* @res "��ͬ{0}����������!" */
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

							/* @res "��ͬ{0}�ܽ���!" */
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

			// ��������
			if (bMustException) {
				StringBuffer sbMust = new StringBuffer(); // ֱ���״��
				java.util.Enumeration enumError = htMust.elements();
				while (enumError.hasMoreElements()) {
					sbMust.append(enumError.nextElement().toString());
				}
				throw new BusinessException(sbMust.toString());
			} else if (bUserChoose && bNeedException) {
				StringBuffer sbNeedReply = new StringBuffer(); // ��Ҫ�û�ȷ�ϵ�
				java.util.Enumeration enumAffirm = htNeedReply.elements();
				while (enumAffirm.hasMoreElements()) {
					sbNeedReply.append(enumAffirm.nextElement().toString());
				}
				sbNeedReply.append(nc.bs.ml.NCLangResOnserver.getInstance()
						.getStrByID("4020pub", "UPP4020pub-000125")/*
																	 * @res
																	 * "��ȷ���Ƿ����?"
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
	 * �˴����뷽��˵���� ������Ԥ������д�ɹ����� ���ߣ������� �������ڣ�(2003-9-16 10:34:56)
	 * 
	 * @param voaBack
	 *            nc.vo.scm.ctpo.CtAllotImprestPoVO[]
	 * @throws Exception
	 */
	public void writeBackImprest(CtAllotImprestPoVO[] voaBack)
			throws BusinessException {
		try {
			if (voaBack != null && voaBack.length > 0) {
				// ���òɹ��ӿڡ�
				// ������µĽӿڵ��� 2004-11-23
				IPuToCt_PoToCt po = (IPuToCt_PoToCt) NCLocator.getInstance()
						.lookup(IPuToCt_PoToCt.class.getName());
				if (po != null)
					po.writeBackPrePayMny(voaBack);
			}
		} catch (Exception e) {
			reportException(e);
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000267")/*��ͬ��д����Ԥ����ʱ����! */);
		}
	}

	/**
	 * ���쳣��Ϣ��ӡ������̨��
	 * 
	 * @param e
	 *            java.lang.Exception
	 */
	protected void reportException(Exception e) {
		nc.vo.scm.pub.SCMEnv.out(e);
	}
	/**
	 * ������ ���Ӧ��Ӧ��δ�������
	 * ���ߣ�lirr
	 * ���ڣ�2008-12-3����01:02:34
	 * �޸����� 2008-12-3����01:02:34 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
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
				//modified by lirr 2009-03-03 v55��Ϊ���պ�ͬid��ѯ
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
	 * ������ ���Ӧ��Ӧ��δ�������
	 * ���ߣ�lirr
	 * ���ڣ�2008-12-3����01:02:34
	 * �޸����� 2008-12-3����01:02:34 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
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
			
			// �ж�Ӧ��Ӧ���Ƿ����ã�
			boolean isArap = srv.isEnabled(pk_corp, ProductCode.PROD_AR);
			if (!isArap) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000157")/*
																	 * @res
																	 * "û������Ӧ��Ӧ����Ʒ,���ܲ�ѯ"
																	 */);
			}
			htArap = ArapHelper.getContractPr(voCond);
			

		} catch (BusinessException e) {// ��ֹ�ظ���װ�Ĺؼ�����
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return htArap;
	}
	/**
	 * ������ ��òɹ���ִͬ�����
	 * ���ߣ�lirr
	 * ���ڣ�2008-12-3����01:02:34
	 * �޸����� 2008-12-3����01:02:34 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	* �޸����� 2009-03-03 �޸��ˣ�lirr  v55��Ϊ���պ�ͬid��ѯ	
	 */
	private DMDataVO[]  qryPuExecImprest(DMDataVO[] voRet,String pk_corp,ContractQueryVO voCond,String sWhere )
			throws BusinessException {
		//�ɹ��ӿڵķ���ֵ
		Hashtable<String,CtExecImprestVO> htPo = null;
		try{
			ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
			.getInstance().lookup(
			ICreateCorpQueryService.class.getName());
			//�жϲɹ�ϵͳ�Ƿ�����:
			boolean isPurUsed = srv.isEnabled(pk_corp, ProductCode.PROD_PO);
			/*if (!isPurUsed) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000156")
																	 * @res
																	 * "û�����òɹ���Ʒ,���ܲ�ѯ"
																	 );
			}
			IPuToCt_PoToCt po = (IPuToCt_PoToCt) NCLocator.getInstance()
			.lookup(IPuToCt_PoToCt.class.getName());
			if (po != null)
				htPo = po.queryCtPayExecInfo(pk_corp, getSqlToPu(pk_corp,
				sWhere));*/
			//modified by lirr 2008-12-05 ���ɹ�ģ��û������ҲӦ�������ѯ
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
				// �ɹ��ӿڷ���ֵ��ϣ�������
				String pk_ct_manage = null;
				// �ɹ��ӿڷ���ֵ��ϣ��ļ�ֵ[������Ʊ���͸�����Ӧ�����]
				CtExecImprestVO voPo = null;
				// Ӧ��Ӧ���ӿڷ���ֵ�����������
				//String ct_code = null;
				// Ӧ��Ӧ���ӿڷ���ֵ������ļ�ֵ[δ����Ԥ����]
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
								// ���ӿڵ�VO���õ�DMDataVO��
								voRet[i].setAttributeValue("dapmny", voPo
										.getDapmny());
								voRet[i].setAttributeValue("dinvoicemny", voPo
										.getDinvoicemny());
								voRet[i].setAttributeValue("dpaymny", voPo
										.getDpaymny());
							}
							// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
							htPo.remove(pk_ct_manage);
						} 
						//added by lirr 2008-12-05 ���ɹ�ģ��û������ҲӦ�������ѯ
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
								// ���ӿڵ�VO���õ�DMDataVO��
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
							htArap.remove(ct_code);
						}*/
						if (pk_ct_manage != null && htArap != null && htArap.size() > 0
								&& htArap.containsKey(pk_ct_manage)) {
							ufdNotVerifyMny = (UFDouble) htArap.get(pk_ct_manage);
							if (ufdNotVerifyMny != null) {
								// ���ӿڵ�VO���õ�DMDataVO��
								voRet[i].setAttributeValue("dnotverifymny",
										ufdNotVerifyMny);
							}
							// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
							htArap.remove(pk_ct_manage);
						}
					}
				//����Ӧ�����
				caculateNeedPayMny(voRet);
			}
			
		} catch (BusinessException e) {// ��ֹ�ظ���װ�Ĺؼ�����
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}
	/**
	 * ������ ������ۺ�ִͬ�����
	 * ���ߣ�lirr
	 * ���ڣ�2008-12-3����01:02:34
	 * �޸����� 2008-12-3����01:02:34 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	 * �޸����� 2009-03-03 �޸��ˣ�lirr  v55��Ϊ���պ�ͬid��ѯ		
	 */
	private DMDataVO[]  qrySoExecImprest(DMDataVO[] voRet,String pk_corp,ContractQueryVO voCond,String sWhere )
			throws BusinessException {
		//���۽ӿڷ���ֵ 2004-11-23 HashMap<String:��ͬPK��UFDouble��Ӧ�����ϼ�> 
		HashMap<String, UFDouble> htSo = null;
		try{
			ICreateCorpQueryService srv = (ICreateCorpQueryService) NCLocator
			.getInstance().lookup(
			ICreateCorpQueryService.class.getName());
			//�ж�����ϵͳ�Ƿ�����:
			boolean isSoUsed = srv.isEnabled(pk_corp, ProductCode.PROD_SO);
			//modified  by lirr 2008-12-05 ������ģ��û������ҲӦ�������ѯ
			/*if (!isSoUsed) {
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("4020pub", "UPP4020pub-000266")û���������۲�Ʒ,���ܲ�ѯ);
			}*/
			String[] sCtPksToSo = null;
			if (voRet != null && voRet.length > 0) {
				int iCtPkLen = voRet.length;
				sCtPksToSo = new String[iCtPkLen]; 
				for (int i = 0; i < iCtPkLen; i++) {
					sCtPksToSo[i] = (String) voRet[i]
					   							.getAttributeValue("pk_ct_manage");
				}
				//modified  by lirr 2008-12-05 ������ģ��û������ҲӦ�������ѯ
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
				// �ɹ��ӿڷ���ֵ��ϣ�������
				String pk_ct_manage = null;
				//���۽ӿڷ���ֵ��ϣ��ļ�ֵ[String:��ͬPK��UFDouble��Ӧ�����ϼ�]
				UFDouble soDRecmny = null;
				//����ӿڷ���ֵ��ϣ��ļ�ֵ[String:��ͬPK��UFDouble��Ӧ�����ϼ�]

				// Ӧ��Ӧ���ӿڷ���ֵ�����������
				//String ct_code = null;
				// Ӧ��Ӧ���ӿڷ���ֵ������ļ�ֵ[δ����Ԥ����]
				UFDouble ufdNotVerifyMny = null;

				for (int i = 0; i < iLen; i++) {
					pk_ct_manage = (String) voRet[i]
							.getAttributeValue("pk_ct_manage");
					//ct_code = (String) voRet[i].getAttributeValue("ct_code");

					if (pk_ct_manage != null && htSo != null && htSo.size() > 0
							&& htSo.containsKey(pk_ct_manage)) {
						soDRecmny = htSo.get(pk_ct_manage);
						if (soDRecmny != null) {
							// ���ӿڵ�VO���õ�DMDataVO��
							voRet[i].setAttributeValue("dapmny", soDRecmny);
						}
						// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
						htSo.remove(pk_ct_manage);
					}
					//added by lirr 2008-12-05 ������ģ��û������ҲӦ�������ѯ
					else {
						voRet[i].setAttributeValue("dapmny", new UFDouble(0.0));
						
					}
					/*if (ct_code != null && htArap != null && htArap.size() > 0
							&& htArap.containsKey(ct_code)) {
						ufdNotVerifyMny = (UFDouble) htArap.get(ct_code);
						if (ufdNotVerifyMny != null) {
							// ���ӿڵ�VO���õ�DMDataVO��
							voRet[i].setAttributeValue("dnotverifymny",
									ufdNotVerifyMny);
						}
						// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
						htArap.remove(ct_code);
					}*/
					
					if (pk_ct_manage != null && htArap != null && htArap.size() > 0
							&& htArap.containsKey(pk_ct_manage)) {
						ufdNotVerifyMny = (UFDouble) htArap.get(pk_ct_manage);
						if (ufdNotVerifyMny != null) {
							// ���ӿڵ�VO���õ�DMDataVO��
							voRet[i].setAttributeValue("dnotverifymny",
									ufdNotVerifyMny);
						}
						// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
						htArap.remove(pk_ct_manage);
					}
						
					}
				//����Ӧ�����
				caculateNeedPayMny(voRet);
			}

		} catch (BusinessException e) {// ��ֹ�ظ���װ�Ĺؼ�����
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}
	/**
	 * ������ ���������ִͬ�����
	 * ���ߣ�lirr
	 * ���ڣ�2008-12-3����01:02:34
	 * �޸����� 2008-12-3����01:02:34 �޸��ˣ�lirr �޸�ԭ��ע�ͱ�־��
		@param pk_corp
		@param voCond
		@return
		@throws BusinessException
	 * �޸����� 2009-03-03 �޸��ˣ�lirr  v55��Ϊ���պ�ͬid��ѯ
	 */
	private DMDataVO[]  qryOtherExecImprest(DMDataVO[] voRet,String pk_corp,ContractQueryVO voCond,String sWhere )
			throws BusinessException {
//		Ӧ��Ӧ���ӿڷ���ֵ����Ӧ����Ӧ��ֵ 2004-11-23 Hashtable<String:��ͬPK��UFDouble��Ӧ��/�����ϼ�> 
		Hashtable<String ,UFDouble> htArapYf = null;
		Hashtable<String ,UFDouble> htArapYs = null;
		Hashtable<String ,UFDouble> htArapFk = null;
		Hashtable<String ,UFDouble> htArapSk = null;
		
//		����Z5������¼���ո�����һ������Ҫ����һ��
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
				// �ɹ��ӿڷ���ֵ��ϣ�������
				String pk_ct_manage = null;
				// Ӧ��Ӧ���ӿڷ���ֵ�����������
				//String ct_code = null;
				//added by lirr ����Z5������¼���ո�����һ������Ҫ����һ��
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
							// ���ӿڵ�VO���õ�DMDataVO��
							voRet[i].setAttributeValue("dapmny", arapDRecmnyYf);
						}
						
						// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
						htArapYf.remove(pk_ct_manage);
						
					}
					/*if (ct_code != null && htArapYs != null && htArapYs.size() > 0
							&& htArapYs.containsKey(ct_code)) {*/
					if (pk_ct_manage != null && htArapYs != null && htArapYs.size() > 0
							&& htArapYs.containsKey(pk_ct_manage)) {	
						arapDRecmnyYs = (UFDouble)htArapYs.get(pk_ct_manage);
						
						if (arapDRecmnyYs != null) {
							// ���ӿڵ�VO���õ�DMDataVO��
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
						// ���ӿڵ�VO���õ�DMDataVO��
						voRet[i].setAttributeValue("dnotverifymny",
								ufdNotVerifyMnyFk);
					}
					// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
					htArapFk.remove(pk_ct_manage);
				}
				/*if (ct_code != null && htArapSk != null && htArapSk.size() > 0
						&& htArapSk.containsKey(ct_code)) {*/
				if (pk_ct_manage != null && htArapSk != null && htArapSk.size() > 0
						&& htArapSk.containsKey(pk_ct_manage)) {
					ufdNotVerifyMnySk = (UFDouble) htArapSk.get(pk_ct_manage);
					if (ufdNotVerifyMnySk != null) {
						// ���ӿڵ�VO���õ�DMDataVO��
						voRet[i].setAttributeValue("dnotverifymny",
								ufdNotVerifyMnySk);
					}
					// Ϊ���ٹ�ϣ��ļ��������Ѿ������VO����ɾ����
					htArapSk.remove(pk_ct_manage);
				}
			
			}
				//����Ӧ�����
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
	 * �˴����뷽��˵���� �������ڣ�(2003-9-16 21:15:03)
	 * 
	 * @return nc.vo.dm.pub.DMDataVO[]
	 * @param alConds
	 *            java.util.ArrayList 0: String sWhere 1: String pk_corp
	 * 
	 * @exception BusinessException
	 *                �쳣˵����
	 * modified by lirr 2008-08-21 v55���� ���ۺ�ͬ��������ͬ ��Ҫ����ִ�������ѯ����˼������ô˷������ӵ�������sBillType����              
	 */
	public DMDataVO[] qryExecImprest(ArrayList alConds ,String sBillType)
			throws BusinessException {
		DMDataVO[] voRet = null; // ����ֵ
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
			//�ж�Ӧ��Ӧ���Ƿ����ã�
			boolean isArap = srv.isEnabled(pk_corp, ProductCode.PROD_AR);
			if (!isArap) {
				throw new nc.vo.pub.BusinessException(
						nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
								"SCMCOMMON", "UPPSCMCommon-000157")/*
																	 * @res
																	 * "û������Ӧ��Ӧ����Ʒ,���ܲ�ѯ"
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
			

		} catch (BusinessException e) {// ��ֹ�ظ���װ�Ĺؼ�����
			throw e;
		} catch (Exception e) {
			reportException(e);
			throw new BusinessException("Caused by:", e);
		}
		return voRet;
	}

}