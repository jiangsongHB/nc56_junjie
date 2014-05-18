package nc.impl.scm.so.so001;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingException;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.pf.IBackCheckState;
import nc.bs.pub.pf.ICheckState;
import nc.bs.pub.pf.IPfPersonFilter2;
import nc.bs.pub.pf.IPrintDataGetter;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.pub.pf.IQueryData3;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.scm.ic.freeitem.DefdefDMO;
import nc.bs.scm.plugin.InvokeEventProxy;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.bs.scm.pub.CommonDataDMO;
import nc.bs.scm.pub.redun.IRedunSource;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.impl.scm.so.pub.BusinessControlDMO;
import nc.impl.scm.so.so012.SquareAdjust;
import nc.impl.scm.so.so016.BalanceDMO;
import nc.impl.scm.so.so016.BillConvertDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.impl.so.sointerface.SOATP;
import nc.impl.so.sointerface.SaleToCRM;
import nc.itf.ct.service.ICtToPo_BackToCt;
import nc.itf.ic.service.IICPub_GeneralBillDMO;
import nc.itf.ic.service.IICPub_InvATPDMO;
import nc.itf.ic.service.IICToPU_StoreadminDMO;
import nc.itf.ic.service.IICToSO;
import nc.itf.scm.crm.ICRMService;
import nc.itf.scm.pub.bill.IScm;
import nc.itf.scm.pub.lock.LockTool;
import nc.itf.scm.so.receive.IReceiveService;
import nc.itf.scm.so.so001.ISaleOrdBalEndSrv;
import nc.itf.scm.sp.sp015.IPreorder;
import nc.itf.scm.to.service.IOuter4SO;
import nc.itf.so.so120.IBillInvokeCreditManager;
import nc.itf.uap.pf.IPFMetaModel;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.rbac.IRoleManageQuery;
import nc.itf.uap.sf.ICreateCorpQueryService;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.print.IDataSource;
import nc.ui.scm.so.RedunVOTool;
import nc.ui.scm.so.SaleBillType;
import nc.vo.ic.ic004.StoreadminBodyVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pf.change.IChangeVOCheck;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.pf.IPFSourceBillFinder;
import nc.vo.pub.pf.SourceBillInfo;
import nc.vo.pub.pfflow04.MessagedriveVO;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.ct.TypeVO;
import nc.vo.scm.ctpo.ParaPoToCtRewriteVO;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.bill.CreditConst;
import nc.vo.scm.pub.isnull.NullFieldUtils;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.sm.UserVO;
import nc.vo.so.credit.SOCreditPara;
import nc.vo.so.pub.BusiUtil;
import nc.vo.so.pub.GeneralSqlString;
import nc.vo.so.pub.SOGenMethod;
import nc.vo.so.pub.SaleOrdBalConst;
import nc.vo.so.pub.SaleOrderPriceProcess;
import nc.vo.so.saleordbal.SaleordBalEndVO;
import nc.vo.so.so001.ISaleOrderAction;
import nc.vo.so.so001.SODataDesc;
import nc.vo.so.so001.SOField;
import nc.vo.so.so001.SORowData;
import nc.vo.so.so001.SOToolVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.so.so001.SoVoConst;
import nc.vo.so.so016.OrdVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.uap.rbac.RoleVO;

/**
 * Sale的DMO类。
 * 
 * 创建日期：(2001-4-20)
 * 
 * @author：宋杰
 * 
 * 修改：queryAllHeadData(String) 增加关联客商管理档案，客商基本档案 增加查询条件 地区分类 顾焱
 * 
 * 修改：增加对“整单结单”更新 2003-10-15.02 毕佥骞 updateOrderItemEnd(SaleorderBVO)
 * 
 * 修改：增加对“整单结单”查询 2003-10-15.02 毕佥骞 queryOrderEnd(String)
 * 
 * 修改日期：2003-10-16 修改人：杨涛 修改内容：增加表头中的“预收款比例”和“预收款金额”的处理
 * 
 * 修改日期：2008-06-25 修改人：周长胜  修改内容：v5.5销售订单修订表头增加修订版本号和修订日期、修订人的存储
 */
public class SaleOrderDMO extends DataManageObject implements IQueryData, IQueryData2, ICheckState, IBackCheckState,
		IRedunSource, IPfPersonFilter2, IChangeVOCheck, IPFSourceBillFinder, IPrintDataGetter, IQueryData3,ICRMService {

	UFBoolean SO39 = null;

	// 二次开发插件
	private InvokeEventProxy eventproxy = new InvokeEventProxy("SO","30");
	
	public SaleOrderDMO() throws javax.naming.NamingException {
		super();
	}

	public SaleOrderDMO(String dbName) throws javax.naming.NamingException, SystemException {
		super(dbName);
	}

	/**
	 * 根据指定pk批量执行updade语句（@param sql）
	 * @param sql
	 * @param pks
	 * @throws BusinessException
	 */
	public void executeUpdateBatchByPK(String sql,String[] pks) throws BusinessException{
		try {
			ArrayList<ArrayList> alValue = new ArrayList<ArrayList>();
			ArrayList<String> alitem = null;
			for (int i = 0; i < pks.length; i++) {
				alitem = new ArrayList<String>();
				alitem.add(pks[i]);
				alValue.add(alitem);
			}

			ArrayList<Integer> alType = new ArrayList<Integer>();
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_STRING));

			new SmartDMO().executeUpdateBatch(sql, alValue, alType);

		} catch (Exception e) {
			handleException(e);
		}
	}
	
   /**
	* 流程运行中,单据审批进行中
	* 包括流程中的审批通过、审批不通过
	*/
	public boolean checkGoing(String strBillID, String strApproveId, String strApproveDate, String checkNote)
			throws BusinessException {

		try {
			long s = System.currentTimeMillis();
			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态开始...[正在审批]：");

			BusinessControlDMO dmo = new BusinessControlDMO();
			dmo.setBillAudit(SaleBillType.SaleOrder, strBillID, strApproveId, strApproveDate, BillStatus.AUDITING);

			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态用时[正在审批]：[" + (System.currentTimeMillis() - s) + "]" + "ms");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		return true;
	}

   /**
	* 流程结束,单据审批不通过
	*/
	public boolean checkNoPass(String strBillID, String strApproveID, String strApproveDate, String checkNote)
			throws BusinessException {

		try {
			long s = System.currentTimeMillis();
			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态开始...[审批未通过]：");

			BusinessControlDMO dmo = new BusinessControlDMO();
			dmo.setBillAudit(SaleBillType.SaleOrder, strBillID, strApproveID, strApproveDate, BillStatus.NOPASS);

			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态用时[审批未通过]：[" + (System.currentTimeMillis() - s) + "]" + "ms");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		return true;
	}

   /**
	* 流程结束,单据审批通过
    */
	public boolean checkPass(String strBillID, String strApproveId, String strApproveDate, String strCheckNote)
			throws BusinessException {

		try {
			long s = System.currentTimeMillis();

			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态开始...[审批通过]：");
			// 可用量
			// nc.bs.so.sointerface.SOATP atp= new nc.bs.so.sointerface.SOATP();
			// SaleOrderVO saleorder= queryData(strBillID);
			// SaleOrderVO[] vos= saleorder.getOrdVOsOfOtherCorp();
			// if (vos != null && vos.length > 0) {
			// for (int i= 0, loop= vos.length; i < loop; i++)
			// atp.modifyATP(vos[i]);
			// }

			BusinessControlDMO dmo = new BusinessControlDMO();
			dmo.setBillAudit(SaleBillType.SaleOrder, strBillID, strApproveId, strApproveDate, BillStatus.AUDIT);

			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态用时[审批通过]：[" + (System.currentTimeMillis() - s) + "]" + "ms");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		return true;
	}

	/**
	 * 审批流接口方法：实现逐级弃审时匹配的接口方法，
	 * 弃审为单据审批进行中。
	 */
	public void backGoing(String billId, String approveId, String approveDate, String backNote)
			throws java.lang.Exception {
		
		try {
			long s = System.currentTimeMillis();
			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态开始...[正在审批]：");

			BusinessControlDMO dmo = new BusinessControlDMO();
			dmo.setBillAudit(SaleBillType.SaleOrder, billId, approveId, approveDate, BillStatus.AUDITING);

			nc.vo.scm.pub.SCMEnv.out("审批订单后台更改审批状态用时[正在审批]：[" + (System.currentTimeMillis() - s) + "]" + "ms");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		
	}

	/**
	 * 审批流接口方法：1.实现驳回的接口方法;2.弃审为自由态 
	 * 本接口方法中主要完成数据库更新，即对以下三个字段置值: 
	 * A)、单据状态设置为“自由” B)、清空审批人 C)、清空审批日期
	 */
	public void backNoState(String billId, String approveId, String approveDate, String backNote)
			throws java.lang.Exception {
		try {
			long s = System.currentTimeMillis();

			nc.vo.scm.pub.SCMEnv.out("驳回订单后台更改审批状态开始...[驳回]");

			BusinessControlDMO dmo = new BusinessControlDMO();
			dmo.setBillAudit(SaleBillType.SaleOrder, billId, null, null, BillStatus.FREE);

			nc.vo.scm.pub.SCMEnv.out("驳回订单后台更改审批状态用时[驳回]：[" + (System.currentTimeMillis() - s) + "]" + "ms");

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		}

	}

	/**
	 * 检测发货库存组织与存货是否匹配。 创建日期：(2001-11-28 9:10:38)
	 * 
	 * @param saleVO
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	private void checkStoreStructure(SaleOrderVO saleVO, String newHeadID) throws BusinessRuntimeException,
			nc.vo.pub.BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT so_saleorder_b.cinventoryid1, bd_produce.pk_calbody ");
		sql.append("FROM so_saleorder_b LEFT OUTER JOIN ");
		sql.append("bd_produce ON bd_produce.pk_invmandoc = so_saleorder_b.cinventoryid1 AND ");
		sql.append("bd_produce.pk_calbody = so_saleorder_b.cadvisecalbodyid ");
		sql.append("where so_saleorder_b.csaleid=?");
		// SaleorderHVO saleHVOs = (SaleorderHVO) saleVO.getParentVO();
		SaleorderBVO[] saleBVOs = (SaleorderBVO[]) saleVO.getChildrenVO();
		Connection con = null;
		PreparedStatement stmt = null;
		if (saleBVOs != null) {
			try {
				StringBuffer sbfErr = new StringBuffer();
				HashMap htTemp = new HashMap();
				String invid = null;
				String calbodyid = null;
				con = getConnection();
				stmt = con.prepareStatement(sql.toString());
				stmt.setString(1, newHeadID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					invid = rs.getString(1);
					invid = (invid == null ? null : invid.trim());
					calbodyid = rs.getString(2);
					calbodyid = (calbodyid == null ? null : calbodyid.trim());
					if (invid != null && invid.length() > 0 && calbodyid != null && calbodyid.length() > 0) {
						htTemp.put(invid, calbodyid);
					}
				}
				rs.close();
				Object oTemp = null;
				for (int i = 0; i < saleBVOs.length; i++) {
					// yb 修改'保存订单后，删行后保存出错的问题' 03-09-05 排除对删除行的检查
					if (saleBVOs[i].getStatus() == VOStatus.DELETED)
						continue;

					if (saleBVOs[i].getLaborflag() != null && saleBVOs[i].getLaborflag().booleanValue())
						continue;

					if (saleBVOs[i].getDiscountflag() != null && saleBVOs[i].getDiscountflag().booleanValue())
						continue;

					if (saleBVOs[i].getBoosflag() == null || saleBVOs[i].getBoosflag().booleanValue() == false) {

						if (saleBVOs[i].getCadvisecalbodyid() == null
								|| saleBVOs[i].getCadvisecalbodyid().trim().length() <= 0)
							continue;
						// 排除缺货登记的存货
						oTemp = htTemp.get(saleBVOs[i].getCinventoryid1());
						if (oTemp == null) {
							sbfErr.append(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000002",
									null,
									new String[] { saleBVOs[i].getCrowno() == null ? "" : saleBVOs[i].getCrowno() }));
							sbfErr.append("\n");
							// sbfErr.append("行号为");
							// sbfErr.append(saleBVOs[i].getCrowno() == null ?
							// ""
							// : saleBVOs[i].getCrowno());
							// sbfErr.append("的行存货不属于发货库存组织！\n");

						}
					}
				}
				if (sbfErr.toString().trim().length() > 0) {
					throw new nc.vo.pub.BusinessException(sbfErr.toString());
				}
			} catch (SQLException e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
				throw new BusinessRuntimeException(e.getMessage());
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (Exception e) {
				}
				try {
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void delete(String key) {
		try {
			deleteBodys(key);
			deleteHead(key);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		}
	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void deleteBodyAndExe(PreparedStatement stmtdeleteBody, PreparedStatement stmtdeleteExe, String key) {

		// String sql = "delete from so_saleorder_b where corder_bid = ?";
		// "delete from so_saleexecute where csale_bid = ? and creceipttype =
		// '30' ";

		try {

			stmtdeleteBody.setString(1, key);
			executeUpdate(stmtdeleteBody);

			stmtdeleteExe.setString(1, key);
			executeUpdate(stmtdeleteExe);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		}

	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void deleteBody(String key) {

		String sql = "delete from so_saleorder_b where corder_bid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setString(1, key);
			stmt.executeUpdate();

			deleteFollowBody(key);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void deleteBodys(String key) {

		String sql = "delete from so_saleorder_b where csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setString(1, key);
			stmt.executeUpdate();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void deleteFollowBody(String key) {

		String sql = "delete from so_saleexecute where csale_bid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setString(1, key);
			stmt.executeUpdate();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}
	
	/**
	 * 
	 * 描述：删除某个销售订单的所有修订记录信息，在销售订单删除时调用
	 * <p>
	 * <b>参数说明</b>
	 * @param key 销售订单的PK
	 * <p>
	 * @author duy
	 * @time 2008-12-12 下午03:21:42
	 */
	public void deleteHistory(String key) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			String sql = "delete from so_saleorder_history_b where csaleid = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, key);
			stmt.executeUpdate();
			stmt.close();
			sql = "delete from so_sale_history where csaleid = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, key);
			stmt.executeUpdate();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 向数据库插入一个VO对象。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param node
	 *            nc.vo.so.so001.SaleVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void deleteHead(String key) {

		String sql = "delete from so_sale where csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setString(1, key);
			stmt.executeUpdate();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public TypeVO[] getAllContractType(String sWhere) {
		if (sWhere == null || sWhere.length() <= 0)
			return null;
		String sql = " SELECT ct_manage.pk_ct_manage,ct_type.nbusitype, ct_type.ninvctlstyle, ct_type.ndatactlstyle "
				+ " FROM ct_manage INNER JOIN " + " ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type ";
		// + "WHERE ct_manage.pk_ct_manage = ?";
		sql = sql + " WHERE " + sWhere;
		TypeVO type = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ArrayList alist = new ArrayList();
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				type = new TypeVO(rs.getString(1));
				// nbusitype :
				Integer nbusitype = (Integer) rs.getObject(2);
				type.setNbusitype(nbusitype == null ? null : nbusitype);
				// ninvctlstyle :
				Integer ninvctlstyle = (Integer) rs.getObject(3);
				type.setNinvctlstyle(ninvctlstyle == null ? null : ninvctlstyle);
				// ndatactlstyle :
				Integer ndatactlstyle = (Integer) rs.getObject(4);
				type.setNdatactlstyle(ndatactlstyle == null ? null : ndatactlstyle);
				alist.add(type);
			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		if (alist.size() <= 0)
			return null;
		return (TypeVO[]) alist.toArray(new TypeVO[0]);
	}

	/**
	 * 作者：李金巧 功能：取得某个指定表中某个字段的一组值 参数：tableName 表名 参数：fieldName 要查询的字段名 参数：key
	 * 键值字段 参数：values 键值数组 返回： 例外： 日期：(2002-4-2 11:53:15) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public java.util.Hashtable getAnyValue(String tableName, String fieldName, String key, String[] values)
			throws SQLException {
		if (tableName == null || fieldName == null || key == null || values == null || values.length <= 0)
			return null;

		Hashtable table = new Hashtable();

		ArrayList ids = new ArrayList();
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null && !values[i].equals("") && !ids.contains(values[i])) {
				ids.add(values[i]);
			}
		}
		StringBuffer sWhere = new StringBuffer();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int onenum = 200;

		try {

			con = getConnection();

			for (int i = 0, count = (ids.size() % onenum == 0 ? ids.size() / onenum : ids.size() / onenum + 1); i < count; i++) {

				String sql = " select " + key + ", " + fieldName + " from " + tableName + " where " + key + " in ( ";
				sWhere.setLength(0);
				for (int j = 0, count1 = ids.size() - i * onenum > onenum ? onenum : ids.size() - i * onenum; j < count1; j++) {
					if (j > 0) {
						sWhere.append(" , ");
					}
					sWhere.append("'" + ids.get(j + i * onenum).toString() + "'");
				}
				sWhere.append(" ) ");

				sql += sWhere.toString();
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();

				//
				String keyValue = null;
				String fieldValue = null;
				while (rs.next()) {
					keyValue = rs.getString(1);
					if (keyValue == null || keyValue.trim().equals(""))
						continue;
					fieldValue = rs.getString(2);
					if (fieldValue == null || fieldValue.trim().equals(""))
						continue;
					if (!table.containsKey(keyValue))
						table.put(keyValue, fieldValue);
				}
				rs.close();
				stmt.close();

			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return table;

	}

	public String[][] getBomChildInfo(String pk_corp, String invID) throws SQLException {
		String strSql = "SELECT distinct mm_pzbom_b.wlbmid, mm_pzbom_b.sfslkx, mm_pzbom_b.slsx,mm_pzbom_b.slxx, mm_pzbom_b.sl, mm_pzbom_b.jldwid, mm_pzbom_b.sfkx, mm_pzbom_b.bz,mm_pzbom_b.wlglid, mm_pzbom_b.sfqs "
				+ "FROM mm_pzbom INNER JOIN mm_pzbom_b ON mm_pzbom_b.pk_pzbomid = mm_pzbom.pk_pzbomid "
				+ "WHERE mm_pzbom.pk_corp='" + pk_corp + "' and mm_pzbom.wlbmid= '" + invID + "' ";
		// "ORDER BY mm_pzbom_b.hh";

		Connection con = null;
		PreparedStatement stmt = null;
		Vector v = new Vector();
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Vector vLine = new Vector();
				String obj = rs.getString(1);
				vLine.addElement((obj == null ? null : obj.toString().trim()));
				Object o = rs.getObject(2);
				vLine.addElement((o == null ? null : o.toString().trim()));
				o = rs.getObject(3);
				vLine.addElement((o == null ? null : o.toString().trim()));
				o = rs.getObject(4);
				vLine.addElement((o == null ? null : o.toString().trim()));
				o = rs.getObject(5);
				vLine.addElement((o == null ? null : o.toString().trim()));
				obj = rs.getString(6);
				vLine.addElement((obj == null ? null : obj.toString().trim()));
				// sfkx
				o = rs.getObject(7);
				vLine.addElement((o == null ? null : o.toString().trim()));
				obj = rs.getString(8);
				vLine.addElement((obj == null ? null : obj.toString().trim()));
				obj = rs.getString(9);
				vLine.addElement((obj == null ? null : obj.toString().trim()));
				// sfqs
				obj = rs.getString(10);
				vLine.addElement((obj == null ? null : obj.toString().trim()));
				v.addElement(vLine);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		String[][] results = null;
		if (v.size() > 0) {
			results = new String[v.size()][((Vector) v.elementAt(0)).size()];
			for (int i = 0; i < v.size(); i++) {
				for (int j = 0; j < ((Vector) v.elementAt(i)).size(); j++) {
					Object o = ((Vector) v.elementAt(i)).elementAt(j);
					results[i][j] = (o == null ? null : o.toString());
				}
			}
		}
		return results;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-24 9:42:35)
	 * 
	 * @return java.lang.String[]
	 * @param customerid
	 *            java.lang.String
	 */
	public String[][] getBomInfo(String pk_corp, String invID, String curdate) throws SQLException {
		String strSql = "SELECT  mm_pzbom_b.wlbmid,bd_invbasdoc.invname,mm_pzbom_b.wlglid,bd_invmandoc.isspecialty "
				+ "FROM mm_pzbom_b INNER JOIN "
				+ "mm_pzbom ON mm_pzbom_b.pk_pzbomid = mm_pzbom.pk_pzbomid LEFT OUTER JOIN "
				+ "bd_invmandoc INNER JOIN "
				+ "bd_invbasdoc ON bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc ON "
				+ "mm_pzbom_b.wlglid = bd_invmandoc.pk_invmandoc AND "
				+ "mm_pzbom_b.wlbmid = bd_invbasdoc.pk_invbasdoc " + "WHERE mm_pzbom.pk_corp='" + pk_corp
				+ "' and mm_pzbom.wlbmid = '" + invID + "' ";
		// "and mm_pzbom_b.sdate<='"+curdate+"' and
		// mm_pzbom_b.sdate<='"+curdate+"'"; ;
		Connection con = null;
		PreparedStatement stmt = null;
		Vector v = new Vector();
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Vector vLine = new Vector();
				// wlbmid
				String obj = rs.getString(1);
				vLine.addElement((obj == null ? "" : obj.trim()));
				// invname
				obj = rs.getString(2);
				vLine.addElement((obj == null ? "" : obj.trim()));
				// wlglid
				obj = rs.getString(3);
				vLine.addElement((obj == null ? "" : obj.trim()));
				// isspecialty
				obj = rs.getString(4);
				vLine.addElement((obj == null ? "" : obj.trim()));
				v.addElement(vLine);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		String[][] results = null;
		if (v.size() > 0) {
			Vector vtemp = new Vector();
			for (int i = 0; i < v.size(); i++) {
				Vector vline = (Vector) v.elementAt(i);

				// v5.02修改:BOOM树展现
				// if (isHaveSub(pk_corp, vline.elementAt(0).toString(),
				// curdate))
				vtemp.add(vline);

			}
			v = vtemp;
			if (v.size() > 0) {
				results = new String[v.size()][((Vector) v.elementAt(0)).size()];
				for (int i = 0; i < v.size(); i++) {
					for (int j = 0; j < ((Vector) v.elementAt(i)).size(); j++)
						results[i][j] = ((Vector) v.elementAt(i)).elementAt(j).toString();
				}
			}
		}
		return results;
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void freshVOStatus(SaleOrderVO saleorder) throws nc.vo.pub.BusinessException, java.sql.SQLException {
		if (saleorder == null || saleorder.getChildrenVO() == null)
			return;

		HashMap hs30 = SOToolsDMO
				.getAnyValueSORow(
						"so_saleexecute,so_saleorder_b",
						new String[] {
						// 累计发运数量
								"so_saleexecute.ntotalreceivenumber",
								// 累计出库数量
								"so_saleexecute.ntotalinventorynumber",
								// 行状态
								"so_saleorder_b.frowstatus",
								// 发运完毕
								"so_saleexecute.bifreceivefinish",
								// //发运出库
								"so_saleexecute.bifinventoryfinish" },
						"csale_bid",
						SoVoTools.getVOsOnlyValues(saleorder.getChildrenVO(), "corder_bid"),
						" so_saleexecute.csale_bid=so_saleorder_b.corder_bid and so_saleexecute.csaleid=so_saleorder_b.csaleid and so_saleexecute.creceipttype='30' ");

		if (hs30 == null || hs30.size() <= 0)
			return;

		SaleorderBVO[] bvos = (SaleorderBVO[]) saleorder.getChildrenVO();
		SORowData rowdata = null;
		for (int i = 0; i < bvos.length; i++) {
			if (bvos[i] == null || SoVoTools.isEmptyString(bvos[i].getCorder_bid()))
				continue;
			rowdata = (SORowData) hs30.get(bvos[i].getCorder_bid());
			if (rowdata == null)
				continue;
			bvos[i].setNtotalreceivenumber(rowdata.getUFDouble(0));
			// bvos[i].setNtotalinventorynumber(rowdata.getUFDouble(1));
			bvos[i].setFrowstatus(rowdata.getInteger(2));
			bvos[i].setBifreceivefinish(rowdata.getUFBoolean(3));
			bvos[i].setBifinventoryfinish(rowdata.getUFBoolean(4));
		}

	}

	/**
	 * 
	 * @param saleorder
	 * @return
	 * @throws nc.vo.pub.BusinessException
	 * @throws java.sql.SQLException
	 * 
	 * @comment 当前回写方式为数量差值，需要判断多种情况；以后可参考修改为分别回写新旧bvo的方式
	 * 
	 */
	public ParaPoToCtRewriteVO[] getChangeFromSaleVO(SaleOrderVO saleorder) throws nc.vo.pub.BusinessException,
			java.sql.SQLException {

		if (saleorder == null)
			return null;

		SaleorderBVO[] bvo = (SaleorderBVO[]) saleorder.getChildrenVO();
		Vector v = new Vector();
		ParaPoToCtRewriteVO added;
		for (int i = 0; i < bvo.length; i++) {
			added = null;

			// 20051020 增加过滤
			/** 对于原来有关联而现在没有关联的情况，需要回写* */
			if ((bvo[i].getAttributeValue("ct_manageid") == null)
					&& (bvo[i].getOldbvo() == null || bvo[i].getOldbvo().getCt_manageid() == null)) {
				continue;
			}

			
			/** 如果有来源，则必须为合同 --废弃(V5.6支持来源预订单同时支持合同) **/
			/*if (bvo[i].getCreceipttype() != null) {
				if (bvo[i].getAttributeValue("creceipttype").equals(nc.ui.scm.so.SaleBillType.SoContract)
						|| bvo[i].getAttributeValue("creceipttype").equals(nc.ui.scm.so.SaleBillType.SoInitContract)) {
				} else {
					continue;
				}
			}*/

			ParaPoToCtRewriteVO vo = new ParaPoToCtRewriteVO();

			vo.setCContractRowID(bvo[i].getCt_managebid());
			//订单行号已供合同提示
			vo.setM_cOrderCode(bvo[i].getCrowno());

			/** 如已经取消关联，取之前的源头* */
			if (vo.getCContractRowId() == null && bvo[i].getOldbvo()!=null) {
				vo.setCContractRowID(bvo[i].getOldbvo().getCt_managebid());
			}
			
			if (vo.getCContractRowId() == null)
				return null;

			vo.setFirstTime(saleorder.getFirstTime());

			UFDouble number = bvo[i].getNnumber();
			if (number == null)
				number = SoVoConst.duf0;
			UFDouble nmny = bvo[i].getNsummny();
			if (nmny == null)
				nmny = SoVoConst.duf0;

			if (bvo[i].getBlargessflag() != null && bvo[i].getBlargessflag().booleanValue()) {
				number = SoVoConst.duf0;
				nmny = SoVoConst.duf0;
			}

			switch (bvo[i].getStatus()) {
			case VOStatus.NEW: {
				break;
			}
			case VOStatus.DELETED: {
				UFDouble oldnumber = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNnumber();// (UFDouble)
				// hOldNumber.get(bid);
				UFDouble oldnmny = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNsummny();// (UFDouble)
				// hOldNmny.get(bid);

				if (bvo[i].getOldbvo() != null && bvo[i].getOldbvo().getBlargessflag() != null
						&& bvo[i].getOldbvo().getBlargessflag().booleanValue()) {
					oldnumber = SoVoConst.duf0;
					oldnmny = SoVoConst.duf0;
				}

				if (bvo[i].getOldbvo() != null) {
					number = SoVoConst.duf0.sub(oldnumber == null ? SoVoConst.duf0 : oldnumber);
					nmny = SoVoConst.duf0.sub(oldnmny == null ? SoVoConst.duf0 : oldnmny);
				} else {
					number = SoVoConst.duf0.sub(number == null ? SoVoConst.duf0 : number);
					nmny = SoVoConst.duf0.sub(nmny == null ? SoVoConst.duf0 : nmny);
				}
				break;
			}
			case VOStatus.UNCHANGED: {
				break;
			}
			case VOStatus.UPDATED:
			case nc.vo.pub.bill.BillVOStatus.MODIFICATION: {
				UFDouble oldnumber = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNnumber();// (UFDouble)

				if (bvo[i].getOldbvo() != null && bvo[i].getOldbvo().getBlargessflag() != null
						&& bvo[i].getOldbvo().getBlargessflag().booleanValue()) {
					oldnumber = SoVoConst.duf0;
				}

				// 以前此行非合同来源
				if ((bvo[i].getOldbvo() != null)
						&& (bvo[i].getOldbvo().getCt_manageid() == null)) {
					oldnumber = SoVoConst.duf0;
				}

				number = number.sub(oldnumber == null ? SoVoConst.duf0 : oldnumber);

				UFDouble oldnmny = bvo[i].getOldbvo() == null ? SoVoConst.duf0 : bvo[i].getOldbvo().getNsummny();// (UFDouble)
				// hOldNmny.get(bid);

				if (bvo[i].getOldbvo() != null && bvo[i].getOldbvo().getBlargessflag() != null
						&& bvo[i].getOldbvo().getBlargessflag().booleanValue()) {
					oldnmny = SoVoConst.duf0;
				}

				// 以前此行非合同来源
				if ((bvo[i].getOldbvo() != null)
						&& (bvo[i].getOldbvo().getCt_manageid() == null)){
					oldnmny = SoVoConst.duf0;
				}

				nmny = nmny.sub(oldnmny == null ? SoVoConst.duf0 : oldnmny);

				/** 如已经取消关联，释放之前的占用值* */
				if (bvo[i].getCt_manageid() == null) {
					number = SoVoConst.duf0.sub(oldnumber);
					nmny = SoVoConst.duf0.sub(oldnmny);
				}

				/** 如前后两次关联的合同不同，则分别回写* */
				if ((bvo[i].getCt_managebid() != null) && (bvo[i].getOldbvo() != null)
						&& (bvo[i].getOldbvo().getCt_managebid() != null)
						&& (!bvo[i].getCt_managebid().equals(bvo[i].getOldbvo().getCt_managebid()))) {
					number = bvo[i].getNnumber();
					if (number == null)
						number = SoVoConst.duf0;
					nmny = bvo[i].getNsummny();
					if (nmny == null)
						nmny = SoVoConst.duf0;

					if (bvo[i].getBlargessflag() != null && bvo[i].getBlargessflag().booleanValue()) {
						number = SoVoConst.duf0;
						nmny = SoVoConst.duf0;
					}

					added = new ParaPoToCtRewriteVO();

					added.setCContractRowID(bvo[i].getOldbvo().getCt_managebid());
					added.setFirstTime(saleorder.getFirstTime());
					// //相当于删除
					added.setDNum(bvo[i].getOldbvo().getNnumber() == null ? SoVoConst.duf0 : SoVoConst.duf0.sub(bvo[i]
							.getOldbvo().getNnumber()));
					added.setDSummny(bvo[i].getOldbvo().getNsummny() == null ? SoVoConst.duf0 : SoVoConst.duf0
							.sub(bvo[i].getOldbvo().getNsummny()));

				}// end if different source CT

				break;
			}

			}// end switch

			vo.setDNum(number);
			vo.setDSummny(nmny);

			v.add(vo);

			if (added != null) {
				v.add(added);
			}

		}// end for

		ParaPoToCtRewriteVO[] rvo = null;
		if (v.size() > 0) {
			rvo = new ParaPoToCtRewriteVO[v.size()];
			v.copyInto(rvo);
		}		
		
		return rvo;
	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public TypeVO getContractType(String key) throws SQLException {
		String sql = "SELECT ct_type.nbusitype, ct_type.ninvctlstyle, ct_type.ndatactlstyle "
				+ "FROM ct_manage INNER JOIN " + "ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type "
				+ "WHERE ct_manage.pk_ct_manage = ?";
		TypeVO type = null;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, key);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				type = new TypeVO(key);
				// nbusitype :
				Integer nbusitype = (Integer) rs.getObject(1);
				type.setNbusitype(nbusitype == null ? null : nbusitype);
				// ninvctlstyle :
				Integer ninvctlstyle = (Integer) rs.getObject(2);
				type.setNinvctlstyle(ninvctlstyle == null ? null : ninvctlstyle);
				// ndatactlstyle :
				Integer ndatactlstyle = (Integer) rs.getObject(3);
				type.setNdatactlstyle(ndatactlstyle == null ? null : ndatactlstyle);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		return type;
	}

	/**
	 * 功能：按客商管理档案id和公司id取得客户信用 参数： 返回： 例外： 作者：顾焱 日期：(2002-9-26 21:07:31)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param sCusMID
	 *            java.lang.String
	 * @param sPKCorp
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble getCreditMny(String sCusMID) throws SQLException {
		StringBuffer sbfSQL = new StringBuffer();
		sbfSQL.append("SELECT creditmny FROM bd_cumandoc WHERE pk_cumandoc = ? ");

		Connection con = null;
		PreparedStatement stmt = null;
		UFDouble result = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sbfSQL.toString());
			stmt.setString(1, sCusMID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// pk_ct_manage
				Object sTemp = rs.getObject(1);
				result = (sTemp == null ? null : new UFDouble(sTemp.toString()));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return result;
	}

	/**
	 * 功能：按客商管理档案id和公司id取得信用保证金 参数： 返回： 例外： 作者：顾焱 日期：(2002-9-26 21:07:31)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param sCusMID
	 *            java.lang.String
	 * @param sPKCorp
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble getCreditMoney(String sCusMID, String sPKCorp) throws SQLException {
		StringBuffer sbfSQL = new StringBuffer();
		sbfSQL.append("SELECT creditmoney FROM bd_cumandoc ");
		sbfSQL.append("WHERE pk_cumandoc =? AND pk_corp =? and dr=0 ");
		Connection con = null;
		PreparedStatement stmt = null;
		UFDouble result = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sbfSQL.toString());
			stmt.setString(1, sCusMID);
			stmt.setString(2, sPKCorp);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// pk_ct_manage
				Object sTemp = rs.getObject(1);
				result = (sTemp == null ? null : new UFDouble(sTemp.toString()));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return result;
	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public String[][] getCtCode(String custID, String invID, String ctDate) throws SQLException {
		String sql = "SELECT ct_manage.pk_ct_manage,ct_manage.ct_code,ct_manage_b.oriprice,ct_manage_b.pk_ct_manage_b, ct_manage_b.taxration, "
				+ "CASE WHEN ct_manage_b.oriprice IS NULL THEN NULL "
				+ "ELSE ct_manage_b.oriprice * (1 + ct_manage_b.taxration / 100) "
				+ "END AS taxprice , ct_manage.ct_name "
				+ "FROM ct_manage INNER JOIN ct_manage_b ON ct_manage.pk_ct_manage = ct_manage_b.pk_ct_manage "
				+ "WHERE ct_manage.custid='"
				+ custID
				+ "' and ct_manage_b.invid='"
				+ invID
				+ "' "
				+ "and ct_manage.valdate<='"
				+ ctDate
				+ "' and ct_manage.invallidate>='"
				+ ctDate
				+ "' and ct_manage.ctflag=2";
		Connection con = null;
		PreparedStatement stmt = null;
		String[][] results = null;
		Vector v = new Vector();
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Vector vLine = new Vector();
				// pk_ct_manage
				String sTemp = rs.getString(1);
				vLine.addElement((sTemp == null ? "" : sTemp.trim()));
				// ct_code
				sTemp = rs.getString(2);
				vLine.addElement((sTemp == null ? "" : sTemp.trim()));
				// ordprice
				Object oTemp = rs.getObject(3);
				vLine.addElement(oTemp == null ? "0.0" : oTemp.toString());
				// pk_ct_manage_b
				sTemp = rs.getString(4);
				vLine.addElement(sTemp == null ? "" : sTemp.trim());
				// taxration
				Object taxration = rs.getObject(5);
				vLine.addElement(taxration == null ? "0.0" : taxration.toString());
				// taxprice
				Object taxprice = rs.getObject(6);
				vLine.addElement(taxprice == null ? "0.0" : taxprice.toString());
				sTemp = rs.getString(7);
				vLine.addElement(sTemp == null ? "" : sTemp.trim());

				v.addElement(vLine);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		// 获得合同类型
		v = getCtType(v);
		if (v.size() > 0) {
			results = new String[v.size()][((Vector) v.elementAt(0)).size()];
			for (int i = 0; i < v.size(); i++) {
				for (int j = 0; j < ((Vector) v.elementAt(i)).size(); j++)
					results[i][j] = ((Vector) v.elementAt(i)).elementAt(j).toString();
			}
		}
		return results;
	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public Vector getCtType(Vector vecSource) throws SQLException {
		if (vecSource == null || vecSource.size() == 0)
			return vecSource;
		String sql = "SELECT ct_type.nbusitype, ct_type.ninvctlstyle, ct_type.ndatactlstyle "
				+ "FROM ct_manage INNER JOIN " + "ct_type ON ct_manage.pk_ct_type = ct_type.pk_ct_type "
				+ "WHERE ct_manage.pk_ct_manage = ?";
		// TypeVO type = null;
		Connection con = null;
		PreparedStatement stmt = null;
		Vector vecContract = (Vector) vecSource.elementAt(0);
		String pk_ct_manage = (String) (vecContract.elementAt(0));

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, pk_ct_manage);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				// nbusitype :
				Object nbusitype = rs.getObject(1);
				vecContract.addElement(nbusitype == null ? "-1" : nbusitype.toString());
				// ninvctlstyle :
				Object ninvctlstyle = (Integer) rs.getObject(2);
				vecContract.addElement(ninvctlstyle == null ? "-1" : ninvctlstyle.toString());
				// ndatactlstyle :
				Object ndatactlstyle = (Integer) rs.getObject(3);
				vecContract.addElement(ndatactlstyle == null ? "-1" : ndatactlstyle.toString());

			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		return vecSource;
	}

	/**
	 * 方法名称： 方法说明：
	 */
	public String[] getCustAddress(String sCustManID, String sPKCorp) throws SQLException {
		StringBuffer sbfSQL = new StringBuffer();
		sbfSQL
				.append("SELECT bd_cubasdoc.pk_cubasdoc, bd_custaddr.addrname, bd_custaddr.pk_areacl, bd_areacl.areaclname ");
		sbfSQL.append("FROM bd_custaddr LEFT OUTER JOIN ");
		sbfSQL.append("bd_areacl ON bd_custaddr.pk_areacl = bd_areacl.pk_areacl LEFT OUTER JOIN ");
		sbfSQL.append("bd_cubasdoc ON ");
		sbfSQL.append("bd_custaddr.pk_cubasdoc = bd_cubasdoc.pk_cubasdoc LEFT OUTER JOIN ");
		sbfSQL.append("bd_cumandoc ON bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc ");
		sbfSQL.append("WHERE bd_custaddr.defaddrflag = 'Y' AND ");
		sbfSQL.append("bd_cumandoc.pk_cumandoc = '" + sCustManID + "' ");

		Connection con = null;
		PreparedStatement stmt = null;
		String[] result = new String[4];
		try {
			con = getConnection();
			stmt = con.prepareStatement(sbfSQL.toString());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// pk_cubasdoc
				String pk_cubasdoc = rs.getString(1);
				result[0] = pk_cubasdoc == null ? null : pk_cubasdoc.trim();
				// addrname
				String addrname = rs.getString(2);
				result[1] = addrname == null ? null : addrname.trim();
				// pk_areacl
				String pk_areacl = rs.getString(3);
				result[2] = pk_areacl == null ? null : pk_areacl.trim();
				// areaclname
				String areaclname = rs.getString(4);
				result[3] = areaclname == null ? null : areaclname.trim();
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-24 9:42:35)
	 * 
	 * @return java.lang.String[]
	 * @param customerid
	 *            java.lang.String
	 */
	public double getInvInNumByBatch(String pk_corp, String invID, String vbatchcode) throws SQLException {
		String strSql = "SELECT sum(ic_general_b.ninnum) FROM ic_general_b "
				+ "INNER JOIN ic_general_h ON ic_general_b.cgeneralhid=ic_general_h.cgeneralhid "
				+ "where ic_general_h.pk_corp= '" + pk_corp + "' and ic_general_b.cinventoryid='" + invID
				+ "' and ic_general_b.vbatchcode='" + vbatchcode + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		double innum = 0.0;
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSql);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				UFDouble invBatchNum = (UFDouble) rs.getObject(1);
				if (invBatchNum != null)
					innum = invBatchNum.doubleValue();
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return innum;
	}

	/**
	 * 订单结束管理。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public SaleOrderVO[] getOrderVOOfNeedUpdateARATP(SaleOrderVO[] vos) throws nc.vo.pub.BusinessException,
			nc.bs.pub.SystemException, javax.naming.NamingException, java.sql.SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "getOrderVOOfNeedUpdateARATP", new Object[] { vos });
		/** ********************************************************** */

		if (vos == null)
			return null;

		String[] ids = new String[vos.length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = ((SaleorderHVO) vos[i].getParentVO()).getCsaleid();

		nc.vo.pub.CircularlyAccessibleValueObject[] newbvos;
		try {
			newbvos = queryAllBodyDataByHIDs(ids);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e);
		}

		if (newbvos == null || newbvos.length <= 0)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000004")/*
			 * @res "查询订单体失败"
			 */);
		Hashtable hsold = new Hashtable();
		for (int i = 0; i < vos.length; i++) {
			for (int j = 0; j < vos[i].getChildrenVO().length; j++)
				hsold.put(((SaleorderBVO) vos[i].getChildrenVO()[j]).getCorder_bid(), vos[i].getChildrenVO()[j]);
		}

		Hashtable hs = new Hashtable();
		Vector vlist = null;
		for (int i = 0; i < newbvos.length; i++) {
			if (((SaleorderBVO) newbvos[i]).getFrowstatus().intValue() != BillStatus.FINISH
					&& ((SaleorderBVO) newbvos[i]).getFrowstatus().intValue() != BillStatus.AUDIT)
				continue;
			SaleorderBVO oldbvo = (SaleorderBVO) hsold.get(((SaleorderBVO) newbvos[i]).getCorder_bid());
			if (oldbvo == null)
				continue;
			if (oldbvo.getFrowstatus() != null
					&& oldbvo.getFrowstatus().intValue() == ((SaleorderBVO) newbvos[i]).getFrowstatus().intValue())
				continue;
			vlist = (Vector) hs.get(((SaleorderBVO) newbvos[i]).getCsaleid());
			if (vlist != null) {
				vlist.add(newbvos[i]);
			} else {
				vlist = new Vector();
				hs.put(((SaleorderBVO) newbvos[i]).getCsaleid(), vlist);
				vlist.add(newbvos[i]);
			}
		}

		// SaleOrderVO[] saleorders =new SaleOrderVO[hvos.length];
		// vlist=new Vector();
		Vector vreobj = new Vector();
		for (int i = 0; i < vos.length; i++) {
			vlist = (Vector) hs.get(((SaleorderHVO) vos[i].getParentVO()).getCsaleid());
			if (vlist == null)
				continue;
			vos[i].setChildrenVO((SaleorderBVO[]) vlist.toArray(new SaleorderBVO[0]));
			vreobj.add(vos[i]);
		}

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "getOrderVOOfNeedUpdateARATP", new Object[] { vos });
		/** ********************************************************** */

		if (vreobj.size() <= 0)
			return null;

		return (SaleOrderVO[]) vreobj.toArray(new SaleOrderVO[0]);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-4-24 9:42:35)
	 * 
	 * @return java.lang.String[]
	 * @param customerid
	 *            java.lang.String
	 */
	public String[][] getSoInfoByDate(String pk_corp, String curDate) throws SQLException {
		String strSql = "SELECT so_sale.csaleid,so_saleorder_b.corder_bid,so_saleorder_b.cbomorderid FROM so_sale "
				+ "INNER JOIN so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid " + "WHERE so_sale.pk_corp='"
				+ pk_corp + "' and so_sale.dbilldate >= '" + curDate + "' and so_saleorder_b.cbomorderid IS NOT NULL";

		Connection con = null;
		PreparedStatement stmt = null;
		Vector v = new Vector();
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Vector vLine = new Vector();
				String obj = rs.getString(1);
				vLine.addElement((obj == null ? "" : obj.trim()));
				obj = rs.getString(2);
				vLine.addElement((obj == null ? "" : obj.trim()));
				obj = rs.getString(3);
				vLine.addElement((obj == null ? "" : obj.trim()));
				v.addElement(vLine);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		String[][] results = null;
		if (v.size() > 0) {
			results = new String[v.size()][((Vector) v.elementAt(0)).size()];
			for (int i = 0; i < v.size(); i++) {
				for (int j = 0; j < ((Vector) v.elementAt(i)).size(); j++)
					results[i][j] = ((Vector) v.elementAt(i)).elementAt(j).toString();
			}
		}
		return results;
	}

	/**
	 * 从结果集获取值，同时设置相应vo字段值。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param rs
	 *            ResultSet
	 * @param pos
	 *            int 如果位置参数小于0，则按名字从结果集获取值
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private void getValueFromResultSet(ResultSet rs, int pos, SOField field,
			nc.vo.pub.CircularlyAccessibleValueObject vo) throws java.sql.SQLException {
		if (rs == null || field == null || vo == null)
			return;
		Object value = null;
		if (pos < 0) {
			switch (field.getUftype()) {
			case SODataDesc.INTEGER: {
				value = rs.getObject(field.getDatabasename());
				break;
			}
			case SODataDesc.STRING: {
				String stemp = rs.getString(field.getDatabasename());
				value = stemp == null ? null : stemp.trim();
				break;
			}
			case SODataDesc.UFDATE: {
				String stemp = rs.getString(field.getDatabasename());
				value = stemp == null ? null : new UFDate(stemp);
				break;
			}
			case SODataDesc.BOOLEAN:
			case SODataDesc.UFBOOLEAN: {
				String stemp = rs.getString(field.getDatabasename());
				value = stemp == null ? null : new UFBoolean(stemp);
				break;
			}
			case SODataDesc.UFDOUBLE: {
				BigDecimal bgtemp = (BigDecimal) rs.getObject(field.getDatabasename());
				value = bgtemp == null ? null : new UFDouble(bgtemp);
				break;
			}
			case SODataDesc.UFTIME: {
				String stemp = rs.getString(field.getDatabasename());
				value = stemp == null ? null : new UFTime(stemp);
				break;
			}
			default: {
				value = rs.getObject(field.getDatabasename());
			}
			}
		} else {
			switch (field.getUftype()) {
			case SODataDesc.INTEGER: {
				value = rs.getObject(pos);
				break;
			}
			case SODataDesc.STRING: {
				String stemp = rs.getString(pos);
				value = stemp == null ? null : stemp.trim();
				break;
			}
			case SODataDesc.UFDATE: {
				String stemp = rs.getString(pos);
				value = stemp == null ? null : new UFDate(stemp);
				break;
			}
			case SODataDesc.BOOLEAN:
			case SODataDesc.UFBOOLEAN: {
				String stemp = rs.getString(pos);
				value = stemp == null ? null : new UFBoolean(stemp);
				break;
			}
			case SODataDesc.UFDOUBLE: {
				BigDecimal bgtemp = (BigDecimal) rs.getObject(pos);
				value = bgtemp == null ? null : new UFDouble(bgtemp);
				break;
			}
			case SODataDesc.UFTIME: {
				String stemp = rs.getString(pos);
				value = stemp == null ? null : new UFTime(stemp);
				break;
			}
			default: {
				value = rs.getObject(pos);
			}
			}
		}

		vo.setAttributeValue(field.getVoname(), value);
	}

	/**
	 * 处理自由项 创建日期：(2002-7-10 13:13:17)
	 * 
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	private void initFreeItem(nc.vo.pub.CircularlyAccessibleValueObject[] vos) throws Exception {

		if (vos == null || vos.length <= 0)
			return;

		ArrayList invs = null;

		for (int i = 0; i < vos.length; i++) {
			// 存货ID
			String inv = (String) vos[i].getAttributeValue("cinventoryid");
			if (invs == null)
				invs = new ArrayList();
			invs.add(inv);
		}

		try {
			if (invs != null) {
				DefdefDMO dmo = new DefdefDMO();

				ArrayList freeitem = dmo.queryFreeVOByInvIDsOnceAll(invs);

				for (int i = 0; i < vos.length; i++) {
					
					if (freeitem.get(i)==null)
						continue;
					
					nc.vo.scm.ic.bill.FreeVO freeVO = (nc.vo.scm.ic.bill.FreeVO) freeitem.get(i);
					if(freeVO==null)
						continue;
					
					freeVO.setAttributeValue("vfree1", (String) vos[i].getAttributeValue("vfree1"));
					freeVO.setAttributeValue("vfree2", (String) vos[i].getAttributeValue("vfree2"));
					freeVO.setAttributeValue("vfree3", (String) vos[i].getAttributeValue("vfree3"));
					freeVO.setAttributeValue("vfree4", (String) vos[i].getAttributeValue("vfree4"));
					freeVO.setAttributeValue("vfree5", (String) vos[i].getAttributeValue("vfree5"));
					vos[i].setAttributeValue("vfree0", freeVO.getWholeFreeItem());
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e);
			throw e;
		}

	}
	
	/**
	 * 1:1尾差处理--e.g 数量由3-->1-->3，价税合计出现尾差
	 * @param saleorder
	 * @throws BusinessException 
	 */
	private boolean processPriceBeforeSave(SaleOrderVO saleorder) throws BusinessException{
		return new SaleOrderPriceProcess(saleorder.getPk_corp()).priceProcess(saleorder);
	}

	/**
	 * 后台保存校验
	 * @param saleorder
	 * @throws BusinessException
	 */
	private void checkVo(SaleOrderVO saleorder)throws BusinessException {
		
		//校验末级销售组织
		checkSaleCorp(saleorder);
		
		// 检查自定义项
		IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());
		srv.checkDefDataType(saleorder);
	}
	
	/**
	 * 校验末级销售组织
	 * 
	 */
	private void checkSaleCorp(SaleOrderVO saleorder) throws BusinessException {
		Object csalestruid = saleorder.getHeadVO().getCsalecorpid();
		
		if (csalestruid != null) {
			
			Object[] o;
			try {
				o = new SmartDMO().selectBy2(
						"select csalestruid from bd_salestru where pk_fathsalestru = '"	+ csalestruid + "'");
			
			if (o != null && o.length > 0) {
				throw new BusinessException(NCLangResOnserver.getInstance().getStrByID(
						"40060301", "UPP40060301-000543")/*
															 * @res "只有末级销售组织可选"
															 */);
			}
			
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
				throw new BusinessException(e);
			} 
		}
	}
	
	/**
	 * 插入单据体。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param node
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public ArrayList insert(SaleOrderVO saleorder) throws Exception {
		
	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		//wanglei 2014-05-14 
		reCalcateInvFee(saleorder);
		//后台VO校验
		checkVo(saleorder);
		timer.addExecutePhase("insert操作-后台VO校验：");
		
		//1:1尾差处理--e.g 数量由3-->1-->3，价税合计出现尾差
		boolean ifReCal = false;
		if(!saleorder.isCoopped())
			ifReCal = processPriceBeforeSave(saleorder);
		timer.addExecutePhase("insert操作-尾差处理：");
		
		eventproxy.beforeAction(Action.SAVE, new AggregatedValueObject[]{saleorder}, null);
		timer.addExecutePhase("insert操作-保存前插件处理：");

		// 获得单据号
		nc.impl.scm.so.pub.CheckValueValidityImpl dmocode = new nc.impl.scm.so.pub.CheckValueValidityImpl();
		String vreceiptcode = null;
		SaleorderHVO hvo = (SaleorderHVO) saleorder.getParentVO();

		try {
			vreceiptcode = dmocode.getSysBillNO(saleorder);
			hvo.setVreceiptcode(vreceiptcode);
			// 检查VO
			onCheck(saleorder);
		} catch (Exception e) {
			// v5.02修改，by zhangcheng//////////
			/** 单据号获取成功，vo检查出现异常 */
			if (vreceiptcode != null)
				throw e;
			// /////////////////////////////////

			/** 如果获取单据号失败，再尝试一次 */
			try {
				vreceiptcode = dmocode.getSysBillNO(saleorder);
				hvo.setVreceiptcode(vreceiptcode);
				// 检查VO
				onCheck(saleorder);
			} catch (Exception ex1) {
				throw ex1;
			}
		}
		timer.addExecutePhase("insert操作-单据号处理：");

		SaleorderHVO headVO = (SaleorderHVO) saleorder.getParentVO();
		SaleorderBVO[] bodyVO = (SaleorderBVO[]) saleorder.getChildrenVO();
		String pk_corp = headVO.getPk_corp();
		String creceipttype = headVO.getCreceipttype();

		ArrayList listBillID = null;
		CrossDBConnection con = null;

		String newHeadID = null;
		// 处理超账期标志
		if (saleorder.getErrMsg() != null
				&& saleorder.getErrMsg().indexOf(
						NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000005")) >= 0/*
				 * @res
				 * "账期"
				 */) {
			// "账期")>0){
			headVO.setBoverdate(new UFBoolean(true));
		}
		try {
			con = (CrossDBConnection) getConnection();
			con.setAddTimeStamp(false);

			// 插入主表数据
			ArrayList retlistID = fillPkAndTs(saleorder);
			ArrayList listID = new ArrayList();
			newHeadID = insertHead(headVO, pk_corp, con);
			// listID.add(newHeadID);
			// listID.add(vreceiptcode);
			// 插入附表数据

			listBillID = insertBodys(bodyVO, pk_corp, newHeadID, creceipttype, listID, con);

			listBillID = retlistID;
			
		} catch (Exception e) {
			throw nc.vo.so.pub.ExceptionUtils.wrappExceptionForSO(e);
		} finally {

			try {
				if (con != null) {
					con.setAddTimeStamp(true);
					con.enableSQLTranslator(true);
					con.close();
				}
			} catch (Exception e) {
			}
		}
		timer.addExecutePhase("insert操作-销售订单持久化：");

		// 存货是否匹配存货组织
		checkStoreStructure(saleorder, newHeadID);
		// 存货是否匹配收货存货组织
		checkRecStoreStructure(saleorder, newHeadID);
		//发货库存组织是否匹配发货公司
		checkCalbody(saleorder);
		timer.addExecutePhase("insert操作-保存后校验存货是否匹配存货组织：");
		
		// 如果是退货单，回写退货数量
		setRetNum(saleorder);
		timer.addExecutePhase("insert操作-保存后回写退货数量：");

		// 更新so_bomorder表
		updateBomID(saleorder);
		timer.addExecutePhase("insert操作-保存后更新so_bomorder表：");

		eventproxy.afterAction(Action.SAVE, new AggregatedValueObject[]{saleorder}, null);
		timer.addExecutePhase("insert操作-保存后插件处理：");
		
		//将尾差处理完的金额传回前台 v5.5
		if (ifReCal)
			listBillID.add(getReturnNewVO(saleorder));
		
		//输出各个业务操作占用时间分布
		timer.showAllExecutePhase("insert操作时间：");
		
		return listBillID;
	}

	  /**方法功能描述：从传入的VO中获得传递到前台的新VO。
	   * V55后期顾焱、方禅提出在转单结束后用户编辑数量单价金额字段后在发票保存时也要进行尾差处理，
	   * 导致金额字段发生变化
	   * 需要保存结束后向前台传递表头的发票总金额、冲减金额、净额字段，表体的金额、税额、价税合计字段。
	   */
	private SaleOrderVO getReturnNewVO(SaleOrderVO saleorder){
		SaleOrderVO newVo = new SaleOrderVO();
		SaleorderHVO newheadVO = new SaleorderHVO();
		SaleorderBVO newbvo = null;
		
		newheadVO.setNheadsummny(saleorder.getHeadVO().getNheadsummny());
		
	    /**返回附表VO数据设置 begin**/
	    ArrayList<SaleorderBVO> aryreturnbody = new ArrayList<SaleorderBVO>();
		for (SaleorderBVO bvo : saleorder.getBodyVOs() ){
			newbvo = new SaleorderBVO();
		      //主键
			newbvo.setCorder_bid(bvo.getCorder_bid());
		      //行号
			newbvo.setCrowno(bvo.getCrowno());
		      //时间戳
			newbvo.setTs(bvo.getTs());
		      //原币税额、无税金额、价税合计
			newbvo.setNoriginalcurtaxmny(bvo.getNoriginalcurtaxmny());
			newbvo.setNoriginalcurmny(bvo.getNoriginalcurmny());
			newbvo.setNoriginalcursummny(bvo.getNoriginalcursummny());
		      //本币税额、无税金额、价税合计
			newbvo.setNtaxmny(bvo.getNtaxmny());
			newbvo.setNmny(bvo.getNmny());
			newbvo.setNsummny(bvo.getNsummny());
			
			aryreturnbody.add(newbvo);
		}
		
		SaleorderBVO[] newbvos = aryreturnbody.toArray(new SaleorderBVO[aryreturnbody.size()]);
		newVo.setParentVO(newheadVO);
		newVo.setChildrenVO(newbvos);
		return newVo;
	}
	
	/**
	 * 插入单据体。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param node
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public String insertBody(SaleorderBVO saleItem, String strMainID, String creceipttype, PreparedStatement stmt,
			PreparedStatement stmtexe) throws java.sql.SQLException, nc.bs.pub.SystemException {

		String key = null;
		// String pk_corp = saleItem.getPkcorp();

		try {
			// set PK fields:
			// 海尔数据导入主楗处理
			// if(saleItem.getCorder_bid()==null ||
			// saleItem.getCorder_bid().trim().length()<=0){
			// key = getOID(pk_corp);
			// }else{
			// key=saleItem.getCorder_bid();
			// }

			// saleItem.setCorder_bid(key);
			key = saleItem.getCorder_bid();

			saleItem.setCsaleid(strMainID);

			stmt.setString(1, key);
			// set FK fields:
			stmt.setString(2, strMainID);
			// set non PK fields:
			if (saleItem.getPkcorp() == null) {
				stmt.setNull(3, Types.CHAR);
			} else {
				stmt.setString(3, saleItem.getPkcorp());
			}
			if (saleItem.getCreceipttype() == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, saleItem.getCreceipttype());
			}
			if (saleItem.getCsourcebillid() == null) {
				stmt.setNull(5, Types.CHAR);
			} else {
				stmt.setString(5, saleItem.getCsourcebillid());
			}
			if (saleItem.getCsourcebillbodyid() == null) {
				stmt.setNull(6, Types.CHAR);
			} else {
				stmt.setString(6, saleItem.getCsourcebillbodyid());
			}
			if (saleItem.getCinventoryid() == null) {
				stmt.setNull(7, Types.CHAR);
			} else {
				stmt.setString(7, saleItem.getCinventoryid());
			}
			if (saleItem.getCunitid() == null) {
				stmt.setNull(8, Types.CHAR);
			} else {
				stmt.setString(8, saleItem.getCunitid());
			}
			if (saleItem.getCpackunitid() == null) {
				stmt.setNull(9, Types.CHAR);
			} else {
				stmt.setString(9, saleItem.getCpackunitid());
			}
			if (saleItem.getNnumber() == null) {
				stmt.setNull(10, Types.INTEGER);
			} else {
				stmt.setBigDecimal(10, saleItem.getNnumber().toBigDecimal());
			}
			if (saleItem.getNpacknumber() == null) {
				stmt.setNull(11, Types.INTEGER);
			} else {
				stmt.setBigDecimal(11, saleItem.getNpacknumber().toBigDecimal());
			}
			if (saleItem.getCbodywarehouseid() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleItem.getCbodywarehouseid());
			}
			if (saleItem.getDconsigndate() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleItem.getDconsigndate().toString());
			}
			if (saleItem.getDdeliverdate() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, saleItem.getDdeliverdate().toString());
			}
			if (saleItem.getBlargessflag() == null) {
				stmt.setString(15, "N");
			} else {
				stmt.setString(15, saleItem.getBlargessflag().toString());
			}
			// crowno
			if (saleItem.getCrowno() == null) {
				stmt.setNull(16, Types.CHAR);
			} else {
				stmt.setString(16, saleItem.getCrowno());
			}

			// ts
			if (saleItem.getTs() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, saleItem.getTs().toString());
			}

			if (saleItem.getVeditreason() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, saleItem.getVeditreason());
			}
			if (saleItem.getCcurrencytypeid() == null) {
				stmt.setNull(19, Types.CHAR);
			} else {
				stmt.setString(19, saleItem.getCcurrencytypeid());
			}
			if (saleItem.getNitemdiscountrate() == null) {
				stmt.setNull(20, Types.INTEGER);
			} else {
				stmt.setBigDecimal(20, saleItem.getNitemdiscountrate().toBigDecimal());
			}
			if (saleItem.getNdiscountrate() == null) {
				stmt.setNull(21, Types.INTEGER);
			} else {
				stmt.setBigDecimal(21, saleItem.getNdiscountrate().toBigDecimal());
			}
			if (saleItem.getNexchangeotobrate() == null) {
				stmt.setNull(22, Types.INTEGER);
			} else {
				stmt.setBigDecimal(22, saleItem.getNexchangeotobrate().toBigDecimal());
			}
			if (saleItem.getNtaxrate() == null) {
				stmt.setNull(23, Types.INTEGER);
			} else {
				stmt.setBigDecimal(23, saleItem.getNtaxrate().toBigDecimal());
			}
			if (saleItem.getNoriginalcurprice() == null) {
				stmt.setNull(24, Types.INTEGER);
			} else {
				stmt.setBigDecimal(24, saleItem.getNoriginalcurprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxprice() == null) {
				stmt.setNull(25, Types.INTEGER);
			} else {
				stmt.setBigDecimal(25, saleItem.getNoriginalcurtaxprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurnetprice() == null) {
				stmt.setNull(26, Types.INTEGER);
			} else {
				stmt.setBigDecimal(26, saleItem.getNoriginalcurnetprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxnetprice() == null) {
				stmt.setNull(27, Types.INTEGER);
			} else {
				stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxmny() == null) {
				stmt.setNull(28, Types.INTEGER);
			} else {
				stmt.setBigDecimal(28, saleItem.getNoriginalcurtaxmny().toBigDecimal());
			}
			if (saleItem.getNoriginalcurmny() == null) {
				stmt.setNull(29, Types.INTEGER);
			} else {
				stmt.setBigDecimal(29, saleItem.getNoriginalcurmny().toBigDecimal());
			}
			if (saleItem.getNoriginalcursummny() == null) {
				stmt.setNull(30, Types.INTEGER);
			} else {
				stmt.setBigDecimal(30, saleItem.getNoriginalcursummny().toBigDecimal());
			}
			if (saleItem.getNoriginalcurdiscountmny() == null) {
				stmt.setNull(31, Types.INTEGER);
			} else {
				stmt.setBigDecimal(31, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
			}
			if (saleItem.getNprice() == null) {
				stmt.setNull(32, Types.INTEGER);
			} else {
				stmt.setBigDecimal(32, saleItem.getNprice().toBigDecimal());
			}
			if (saleItem.getNtaxprice() == null) {
				stmt.setNull(33, Types.INTEGER);
			} else {
				stmt.setBigDecimal(33, saleItem.getNtaxprice().toBigDecimal());
			}
			if (saleItem.getNnetprice() == null) {
				stmt.setNull(34, Types.INTEGER);
			} else {
				stmt.setBigDecimal(34, saleItem.getNnetprice().toBigDecimal());
			}
			if (saleItem.getNtaxnetprice() == null) {
				stmt.setNull(35, Types.INTEGER);
			} else {
				stmt.setBigDecimal(35, saleItem.getNtaxnetprice().toBigDecimal());
			}
			if (saleItem.getNtaxmny() == null) {
				stmt.setNull(36, Types.INTEGER);
			} else {
				stmt.setBigDecimal(36, saleItem.getNtaxmny().toBigDecimal());
			}
			if (saleItem.getNmny() == null) {
				stmt.setNull(37, Types.INTEGER);
			} else {
				stmt.setBigDecimal(37, saleItem.getNmny().toBigDecimal());
			}
			if (saleItem.getNsummny() == null) {
				stmt.setNull(38, Types.INTEGER);
			} else {
				stmt.setBigDecimal(38, saleItem.getNsummny().toBigDecimal());
			}
			if (saleItem.getNdiscountmny() == null) {
				stmt.setNull(39, Types.INTEGER);
			} else {
				stmt.setBigDecimal(39, saleItem.getNdiscountmny().toBigDecimal());
			}
			if (saleItem.getCoperatorid() == null) {
				stmt.setNull(40, Types.CHAR);
			} else {
				stmt.setString(40, saleItem.getCoperatorid());
			}
			if (saleItem.getFrowstatus() == null) {
				stmt.setNull(41, Types.INTEGER);
			} else {
				stmt.setInt(41, saleItem.getFrowstatus().intValue());
			}
			if (saleItem.getFrownote() == null) {
				stmt.setNull(42, Types.CHAR);
			} else {
				stmt.setString(42, saleItem.getFrownote());
			}
			//
			if (saleItem.getCinvbasdocid() == null) {
				stmt.setNull(43, Types.CHAR);
			} else {
				stmt.setString(43, saleItem.getCinvbasdocid());
			}
			//
			if (saleItem.getCbatchid() == null) {
				stmt.setNull(44, Types.CHAR);
			} else {
				stmt.setString(44, saleItem.getCbatchid());
			}
			//
			if (saleItem.getFbatchstatus() == null) {
				stmt.setNull(45, Types.INTEGER);
			} else {
				stmt.setInt(45, saleItem.getFbatchstatus().intValue());
			}
			//
			if (saleItem.getCbomorderid() == null) {
				stmt.setNull(46, Types.CHAR);
			} else {
				stmt.setString(46, saleItem.getCbomorderid());
			}
			//
			if (saleItem.getCt_manageid() == null) {
				stmt.setNull(47, Types.CHAR);
			} else {
				stmt.setString(47, saleItem.getCt_manageid());
			}
			//
			if (saleItem.getCfreezeid() == null) {
				stmt.setNull(48, Types.CHAR);
			} else {
				stmt.setString(48, saleItem.getCfreezeid());
			}
			// cadvisecalbodyid
			if (saleItem.getCadvisecalbodyid() == null) {
				stmt.setNull(49, Types.CHAR);
			} else {
				stmt.setString(49, saleItem.getCadvisecalbodyid());
			}
			// boosflag
			if (saleItem.getBoosflag() == null) {
				stmt.setString(50, "N");
			} else {
				stmt.setString(50, saleItem.getBoosflag().toString());
			}
			// bsupplyflag
			if (saleItem.getBsupplyflag() == null) {
				stmt.setString(51, "N");
			} else {
				stmt.setString(51, saleItem.getBsupplyflag().toString());
			}
			// creceiptareaid
			if (saleItem.getCreceiptareaid() == null) {
				stmt.setNull(52, Types.CHAR);
			} else {
				stmt.setString(52, saleItem.getCreceiptareaid());
			}
			// vreceiveaddress
			if (saleItem.getVreceiveaddress() == null) {
				stmt.setNull(53, Types.CHAR);
			} else {
				stmt.setString(53, saleItem.getVreceiveaddress());
			}
			// creceiptcorpid
			if (saleItem.getCreceiptcorpid() == null) {
				stmt.setNull(54, Types.CHAR);
			} else {
				stmt.setString(54, saleItem.getCreceiptcorpid());
			}

			// 设置v30后加入的其他字段值

			setPreStatementOrdB(stmt, 55, saleItem);

			executeUpdate(stmt);

		} finally {

		}
		// 插入子子表
		saleItem.setPrimaryKey(key);
		saleItem.setCsaleid(strMainID);
		insertFollowBody(saleItem, creceipttype, stmtexe);
		return key;
	}

	/**
	 * 向数据库插入一个VO对象。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param node
	 *            nc.vo.so.so001.SaleVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public ArrayList insertBodys(SaleorderBVO[] saleorderB, String pk_corp, String strMainID, String creceipttype,
			ArrayList listID, CrossDBConnection con) throws java.sql.SQLException, nc.bs.pub.SystemException {

		// 补充以后版本增加的数据库字段
		SOField[] addfields = SaleorderBVO.getSoSaleOrderBAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		StringBuffer addfieldvaluesql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldvaluesql.append(",?");
				}
			}
		}

		// 订单表体sql
		String sql = "insert into so_saleorder_b("
				+ "corder_bid, csaleid, pk_corp, creceipttype, csourcebillid, "
				+ "csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, "
				+ "npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, "
				+ "crowno, ts, veditreason, ccurrencytypeid, nitemdiscountrate, "
				+ "ndiscountrate, nexchangeotobrate,ntaxrate, noriginalcurprice, "
				+ "noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, "
				+ "noriginalcursummny, noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, "
				+ "ntaxnetprice, ntaxmny, nmny, nsummny, ndiscountmny, "
				+ "coperatorid, frowstatus, frownote, cinvbasdocid, cbatchid, "
				+ "fbatchstatus, cbomorderid, ct_manageid, cfreezeid, cadvisecalbodyid, "
				+ "boosflag, bsupplyflag, creceiptareaid, vreceiveaddress, creceiptcorpid " + addfieldsql.toString()
				+ ") " + "values(" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ? ,? " + addfieldvaluesql.toString() + ")";

		PreparedStatement stmt = null;

		// 补充以后版本增加的数据库字段
		addfields = SaleorderBVO.getSoSaleExecuteAddFields();
		addfieldsql.setLength(0);
		addfieldsql.append("");
		addfieldvaluesql.setLength(0);
		addfieldvaluesql.append("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldvaluesql.append(",?");
				}
			}
		}
		// 订单执行表sql
		String sqlexe = "insert into so_saleexecute(csale_bid, csaleid, creceipttype, ntotalpaymny, ntotalreceivenumber, "
				+ "ntotalinvoicenumber, ntotalinventorynumber, bifinvoicefinish, bifreceivefinish, bifinventoryfinish, "
				+ "bifpayfinish, cprojectid3, cprojectphaseid, cprojectid, vfree5, vfree4, vfree3, vfree2, vfree1, vdef6, "
				+ "vdef5, vdef4, vdef3, vdef2, vdef1,ts"
				+ addfieldsql.toString()
				+ ")"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
				+ addfieldvaluesql.toString() + ")";
		PreparedStatement stmtexe = null;

		try {
			// 关闭sql翻译器
			// con.enableSQLTranslator(false);
			// //判断是否为oracle
			// if (con.getDatabaseType() == con.ORACLE) {
			// beSuportBatchUpdate = true;
			// } else {
			// beSuportBatchUpdate = false;
			// }
			// //预编译sql
			// stmt = con.prepareStatement(sql);
			// stmtexe = con.prepareStatement(sqlexe);
			// 打开sql翻译器
			// con.enableSQLTranslator(true);
			stmt = prepareStatement(con, sql);
			stmtexe = prepareStatement(con, sqlexe);

			for (int i = 0; i < saleorderB.length; i++) {
				// 缺货存货不存入订单
				if (saleorderB[i].getBoosflag() == null || !saleorderB[i].getBoosflag().booleanValue()) {
					saleorderB[i].setPkcorp(pk_corp);

					String strItemID = insertBody(saleorderB[i], strMainID, creceipttype, stmt, stmtexe);
					// 表体ID
					if (listID != null)
						listID.add(strItemID);
					// BOM配置
					String bomID = (String) saleorderB[i].getCbomorderid();
					if (bomID != null && bomID.trim().length() != 0) {
						updateBom(bomID, strMainID, strItemID);
						// 需求数量
						updateBomVO(saleorderB[i]);
					}
				}
			}
			// if (beSuportBatchUpdate) {
			// //如果数据库为oracle批执行sql
			// stmt.executeBatch();
			// stmtexe.executeBatch();
			// }
			executeBatch(stmt);
			executeBatch(stmtexe);
		} finally {

		}

		return listID;
	}

	/**
	 * 插入子子表。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param node
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void insertFollowBody(SaleorderBVO saleexecute, String creceipttype, PreparedStatement stmt)
			throws java.sql.SQLException, nc.bs.pub.SystemException {

		try {
			// set PK fields:
			if (saleexecute.getPrimaryKey() == null) {
				stmt.setNull(1, Types.CHAR);
			} else {
				stmt.setString(1, saleexecute.getPrimaryKey());
			}
			// set non PK fields:
			if (saleexecute.getCsaleid() == null) {
				stmt.setNull(2, Types.CHAR);
			} else {
				stmt.setString(2, saleexecute.getCsaleid());
			}
			stmt.setString(3, creceipttype);
			if (saleexecute.getNtotalpaymny() == null) {
				stmt.setNull(4, Types.INTEGER);
			} else {
				stmt.setBigDecimal(4, saleexecute.getNtotalpaymny().toBigDecimal());
			}
			if (saleexecute.getNtotalreceivenumber() == null) {
				stmt.setNull(5, Types.INTEGER);
			} else {
				stmt.setBigDecimal(5, saleexecute.getNtotalreceivenumber().toBigDecimal());
			}
			if (saleexecute.getNtotalinvoicenumber() == null) {
				stmt.setNull(6, Types.INTEGER);
			} else {
				stmt.setBigDecimal(6, saleexecute.getNtotalinvoicenumber().toBigDecimal());
			}
			if (saleexecute.getNtotalinventorynumber() == null) {
				stmt.setNull(7, Types.INTEGER);
			} else {
				stmt.setBigDecimal(7, saleexecute.getNtotalinventorynumber().toBigDecimal());
			}
			if (saleexecute.getBifinvoicefinish() == null) {
				// stmt.setNull(8, Types.CHAR);
				stmt.setString(8, "N");
			} else {
				stmt.setString(8, saleexecute.getBifinvoicefinish().toString());
			}
			if (saleexecute.getBifreceivefinish() == null) {
				// stmt.setNull(9, Types.CHAR);
				stmt.setString(9, "N");
			} else {
				stmt.setString(9, saleexecute.getBifreceivefinish().toString());
			}
			if (saleexecute.getBifinventoryfinish() == null) {
				// stmt.setNull(10, Types.CHAR);
				stmt.setString(10, "N");
			} else {
				stmt.setString(10, saleexecute.getBifinventoryfinish().toString());
			}
			if (saleexecute.getBifpayfinish() == null) {
				// stmt.setNull(11, Types.CHAR);
				stmt.setString(11, "N");
			} else {
				stmt.setString(11, saleexecute.getBifpayfinish().toString());
			}

			if (saleexecute.getCprojectid3() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleexecute.getCprojectid3());
			}
			if (saleexecute.getCprojectphaseid() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleexecute.getCprojectphaseid());
			}
			if (saleexecute.getCprojectid() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, saleexecute.getCprojectid());
			}
			if (saleexecute.getVfree5() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, saleexecute.getVfree5());
			}
			if (saleexecute.getVfree4() == null) {
				stmt.setNull(16, Types.CHAR);
			} else {
				stmt.setString(16, saleexecute.getVfree4());
			}
			if (saleexecute.getVfree3() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, saleexecute.getVfree3());
			}
			if (saleexecute.getVfree2() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, saleexecute.getVfree2());
			}
			if (saleexecute.getVfree1() == null) {
				stmt.setNull(19, Types.CHAR);
			} else {
				stmt.setString(19, saleexecute.getVfree1());
			}
			if (saleexecute.getVdef6() == null) {
				stmt.setNull(20, Types.CHAR);
			} else {
				stmt.setString(20, saleexecute.getVdef6());
			}
			if (saleexecute.getVdef5() == null) {
				stmt.setNull(21, Types.CHAR);
			} else {
				stmt.setString(21, saleexecute.getVdef5());
			}
			if (saleexecute.getVdef4() == null) {
				stmt.setNull(22, Types.CHAR);
			} else {
				stmt.setString(22, saleexecute.getVdef4());
			}
			if (saleexecute.getVdef3() == null) {
				stmt.setNull(23, Types.CHAR);
			} else {
				stmt.setString(23, saleexecute.getVdef3());
			}
			if (saleexecute.getVdef2() == null) {
				stmt.setNull(24, Types.CHAR);
			} else {
				stmt.setString(24, saleexecute.getVdef2());
			}
			if (saleexecute.getVdef1() == null) {
				stmt.setNull(25, Types.CHAR);
			} else {
				stmt.setString(25, saleexecute.getVdef1());
			}
			if (saleexecute.getTs() == null) {
				stmt.setNull(26, Types.CHAR);
			} else {
				stmt.setString(26, saleexecute.getTs().toString());
			}

			// 设置v30后加入的其他字段值
			setPreStatementOrdE(stmt, 27, saleexecute);

			executeUpdate(stmt);
		} finally {
		}

	}

	/**
	 * 插入表头。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param node
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。 修改日期：2003-10-16 修改人：杨涛 修改内容：增加表头“预收款比例”和“预收款金额”
	 */
	public String insertHead(SaleorderHVO saleHeader, String pk_corp, Connection con) throws Exception {

		// 设置其他增加的字段
		SOField[] addfields = SaleorderHVO.getAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		StringBuffer addfieldvaluesql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldvaluesql.append(",?");
				}
			}
		}

		String sql = "insert into so_sale("
				+ "csaleid, pk_corp, vreceiptcode, creceipttype, cbiztype, finvoiceclass, finvoicetype, vaccountyear, binitflag, "
				+ "dbilldate, ccustomerid, cdeptid, cemployeeid, coperatorid, ctermprotocolid, csalecorpid, creceiptcustomerid, "
				+ "vreceiveaddress, creceiptcorpid, ctransmodeid, ndiscountrate, cwarehouseid, veditreason, bfreecustflag, "
				+ "cfreecustid, ibalanceflag, nsubscription, ccreditnum, nevaluatecarriage, dmakedate, capproveid, dapprovedate, "
				+ "fstatus, vnote, vdef1, vdef2, vdef3, vdef4, vdef5, vdef6, vdef7, vdef8, vdef9, vdef10, ccalbodyid, bretinvflag,"
				+ "boutendflag,binvoicendflag,breceiptendflag,npreceiverate,npreceivemny,ts,nheadsummny,editionnum"
				+ addfieldsql.toString()
				+ ")"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? " + addfieldvaluesql.toString() + ")";

		String key = null;
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			// set PK fields:
			// 海尔数据导入主楗处理
			if (saleHeader.getCsaleid() == null || saleHeader.getCsaleid().trim().length() <= 0) {
				key = getOID(pk_corp);
			} else {
				key = saleHeader.getCsaleid();
			}

			saleHeader.setCsaleid(key);

			stmt.setString(1, key);
			// set non PK fields:
			if (pk_corp == null) {
				stmt.setNull(2, Types.CHAR);
			} else {
				stmt.setString(2, pk_corp);
			}
			if (saleHeader.getVreceiptcode() == null) {
				stmt.setNull(3, Types.CHAR);
			} else {
				stmt.setString(3, saleHeader.getVreceiptcode());
			}
			if (saleHeader.getCreceipttype() == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, saleHeader.getCreceipttype());
			}
			if (saleHeader.getCbiztype() == null) {
				stmt.setNull(5, Types.CHAR);
			} else {
				stmt.setString(5, saleHeader.getCbiztype());
			}
			if (saleHeader.getFinvoiceclass() == null) {
				stmt.setNull(6, Types.INTEGER);
			} else {
				stmt.setInt(6, saleHeader.getFinvoiceclass().intValue());
			}
			if (saleHeader.getFinvoicetype() == null) {
				stmt.setNull(7, Types.INTEGER);
			} else {
				stmt.setInt(7, saleHeader.getFinvoicetype().intValue());
			}
			if (saleHeader.getVaccountyear() == null) {
				stmt.setNull(8, Types.CHAR);
			} else {
				stmt.setString(8, saleHeader.getVaccountyear());
			}
			if (saleHeader.getBinitflag() == null) {
				stmt.setString(9, "N");
			} else {
				stmt.setString(9, saleHeader.getBinitflag().toString());
			}
			if (saleHeader.getDbilldate() == null) {
				stmt.setNull(10, Types.CHAR);
			} else {
				stmt.setString(10, saleHeader.getDbilldate().toString());
			}
			if (saleHeader.getCcustomerid() == null) {
				stmt.setNull(11, Types.CHAR);
			} else {
				stmt.setString(11, saleHeader.getCcustomerid());
			}
			if (saleHeader.getCdeptid() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleHeader.getCdeptid());
			}
			if (saleHeader.getCemployeeid() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleHeader.getCemployeeid());
			}
			if (saleHeader.getCoperatorid() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, saleHeader.getCoperatorid());
			}
			if (saleHeader.getCtermprotocolid() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, saleHeader.getCtermprotocolid());
			}
			if (saleHeader.getCsalecorpid() == null) {
				stmt.setNull(16, Types.CHAR);
			} else {
				stmt.setString(16, saleHeader.getCsalecorpid());
			}
			if (saleHeader.getCreceiptcustomerid() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, saleHeader.getCreceiptcustomerid());
			}
			if (saleHeader.getVreceiveaddress() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, saleHeader.getVreceiveaddress());
			}
			if (saleHeader.getCreceiptcorpid() == null) {
				stmt.setNull(19, Types.CHAR);
			} else {
				stmt.setString(19, saleHeader.getCreceiptcorpid());
			}
			if (saleHeader.getCtransmodeid() == null) {
				stmt.setNull(20, Types.CHAR);
			} else {
				stmt.setString(20, saleHeader.getCtransmodeid());
			}
			if (saleHeader.getNdiscountrate() == null) {
				stmt.setNull(21, Types.INTEGER);
			} else {
				stmt.setBigDecimal(21, saleHeader.getNdiscountrate().toBigDecimal());
			}
			if (saleHeader.getCwarehouseid() == null) {
				stmt.setNull(22, Types.CHAR);
			} else {
				stmt.setString(22, saleHeader.getCwarehouseid());
			}
			if (saleHeader.getVeditreason() == null) {
				stmt.setNull(23, Types.CHAR);
			} else {
				stmt.setString(23, saleHeader.getVeditreason());
			}
			if (saleHeader.getBfreecustflag() == null) {
				stmt.setString(24, "N");
			} else {
				stmt.setString(24, saleHeader.getBfreecustflag().toString());
			}
			if (saleHeader.getCfreecustid() == null) {
				stmt.setNull(25, Types.CHAR);
			} else {
				stmt.setString(25, saleHeader.getCfreecustid());
			}
			if (saleHeader.getIbalanceflag() == null) {
				stmt.setNull(26, Types.INTEGER);
			} else {
				stmt.setInt(26, saleHeader.getIbalanceflag().intValue());
			}
			if (saleHeader.getNsubscription() == null) {
				stmt.setNull(27, Types.INTEGER);
			} else {
				stmt.setBigDecimal(27, saleHeader.getNsubscription().toBigDecimal());
			}
			if (saleHeader.getCcreditnum() == null) {
				stmt.setNull(28, Types.CHAR);
			} else {
				stmt.setString(28, saleHeader.getCcreditnum());
			}
			if (saleHeader.getNevaluatecarriage() == null) {
				stmt.setNull(29, Types.INTEGER);
			} else {
				stmt.setBigDecimal(29, saleHeader.getNevaluatecarriage().toBigDecimal());
			}
			if (saleHeader.getDmakedate() == null) {
				stmt.setNull(30, Types.CHAR);
			} else {
				stmt.setString(30, saleHeader.getDmakedate().toString());
			}
			if (saleHeader.getCapproveid() == null) {
				stmt.setNull(31, Types.CHAR);
			} else {
				stmt.setString(31, saleHeader.getCapproveid());
			}
			if (saleHeader.getDapprovedate() == null) {
				stmt.setNull(32, Types.CHAR);
			} else {
				stmt.setString(32, saleHeader.getDapprovedate().toString());
			}
			if (saleHeader.getFstatus() == null) {
				stmt.setNull(33, Types.INTEGER);
			} else {
				stmt.setInt(33, saleHeader.getFstatus().intValue());
			}
			if (saleHeader.getVnote() == null) {
				stmt.setNull(34, Types.CHAR);
			} else {
				stmt.setString(34, saleHeader.getVnote());
			}
			if (saleHeader.getVdef1() == null) {
				stmt.setNull(35, Types.CHAR);
			} else {
				stmt.setString(35, saleHeader.getVdef1());
			}
			if (saleHeader.getVdef2() == null) {
				stmt.setNull(36, Types.CHAR);
			} else {
				stmt.setString(36, saleHeader.getVdef2());
			}
			if (saleHeader.getVdef3() == null) {
				stmt.setNull(37, Types.CHAR);
			} else {
				stmt.setString(37, saleHeader.getVdef3());
			}
			if (saleHeader.getVdef4() == null) {
				stmt.setNull(38, Types.CHAR);
			} else {
				stmt.setString(38, saleHeader.getVdef4());
			}
			if (saleHeader.getVdef5() == null) {
				stmt.setNull(39, Types.CHAR);
			} else {
				stmt.setString(39, saleHeader.getVdef5());
			}
			if (saleHeader.getVdef6() == null) {
				stmt.setNull(40, Types.CHAR);
			} else {
				stmt.setString(40, saleHeader.getVdef6());
			}
			if (saleHeader.getVdef7() == null) {
				stmt.setNull(41, Types.CHAR);
			} else {
				stmt.setString(41, saleHeader.getVdef7());
			}
			if (saleHeader.getVdef8() == null) {
				stmt.setNull(42, Types.CHAR);
			} else {
				stmt.setString(42, saleHeader.getVdef8());
			}
			if (saleHeader.getVdef9() == null) {
				stmt.setNull(43, Types.CHAR);
			} else {
				stmt.setString(43, saleHeader.getVdef9());
			}
			if (saleHeader.getVdef10() == null) {
				stmt.setNull(44, Types.CHAR);
			} else {
				stmt.setString(44, saleHeader.getVdef10());
			}
			//
			if (saleHeader.getCcalbodyid() == null) {
				stmt.setNull(45, Types.CHAR);
			} else {
				stmt.setString(45, saleHeader.getCcalbodyid());
			}
			if (saleHeader.getBretinvflag() == null) {
				stmt.setString(46, "N");
			} else {
				stmt.setString(46, saleHeader.getBretinvflag().toString());
			}
			if (saleHeader.getBoutendflag() == null) {
				stmt.setString(47, "N");
			} else {
				stmt.setString(47, saleHeader.getBoutendflag().toString());
			}
			if (saleHeader.getBinvoicendflag() == null) {
				stmt.setString(48, "N");
			} else {
				stmt.setString(48, saleHeader.getBinvoicendflag().toString());
			}
			if (saleHeader.getBreceiptendflag() == null) {
				stmt.setString(49, "N");
			} else {
				stmt.setString(49, saleHeader.getBreceiptendflag().toString());
			}
			// yt add 2003-10-16
			// 预收款比例
			if (saleHeader.getNpreceiverate() == null) {
				stmt.setNull(50, Types.INTEGER);
			} else {
				stmt.setBigDecimal(50, saleHeader.getNpreceiverate().toBigDecimal());
			}
			// 预收款金额
			if (saleHeader.getNpreceivemny() == null) {
				stmt.setNull(51, Types.INTEGER);
			} else {
				stmt.setBigDecimal(51, saleHeader.getNpreceivemny().toBigDecimal());
			}
			// ts
			if (saleHeader.getTs() == null) {
				stmt.setNull(52, Types.CHAR);
			} else {
				stmt.setString(52, saleHeader.getTs().toString());
			}
			if (saleHeader.getAttributeValue("nheadsummny") == null) {
				stmt.setNull(53, Types.INTEGER);
			} else {
				stmt.setBigDecimal(53, ((UFDouble) saleHeader.getAttributeValue("nheadsummny")).toBigDecimal());
			}

			if (saleHeader.getEditionNum() == null) {
				stmt.setNull(54, Types.CHAR);
			} else {
				stmt.setString(54, saleHeader.getEditionNum());
			}
			
			// 设置v30后加入的其他字段值
			setPreStatementOrdH(stmt, 55, saleHeader);

			stmt.executeUpdate();
			
			if (saleHeader.isCoopped()) {
				String sourceId = (String) saleHeader.getCoopPoId();
				reWritePO(new String[] { sourceId }, true, saleHeader.getVreceiptcode(), new String[] { saleHeader
						.getCoopPoTs() }, getOID());
			}

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
		}

		saleHeader.setCsaleid(key);
		return key;
	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public boolean isBomApproved(String orderID) throws SQLException {

		String sql = "SELECT fstatus FROM so_bomorder WHERE csaleid ='" + orderID + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		Object o = null;
		boolean isApproved = true;
		int status = -1;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				o = rs.getObject(1);
				status = (o == null ? 0 : ((Integer) o).intValue());
				if (status != 2)
					isApproved = false;
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return isApproved;

	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public boolean isCtExist(String custID, String invID) throws SQLException {

		String sql = "SELECT ct_manage.ct_code FROM ct_manage "
				+ "INNER JOIN ct_manage_b ON ct_manage.pk_ct_manage = ct_manage_b.pk_ct_manage "
				+ "WHERE ct_manage.custid='" + custID + "' and ct_manage_b.invid='" + invID
				+ "' and ct_manage.ctflag=2";

		Connection con = null;
		PreparedStatement stmt = null;
		Object o = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				o = rs.getObject(1);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return (o == null ? false : true);

	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return boolean
	 */
	public boolean isFinished(SaleorderHVO saleorderHVO) throws BusinessException {
		SaleorderHVO[] saleorderHVOs = (SaleorderHVO[]) queryAllHeadData(" so_sale.csaleid ='"
				+ saleorderHVO.getPrimaryKey() + "'");
		if (saleorderHVOs != null && saleorderHVOs.length != 0)
			return saleorderHVOs[0].getFstatus().intValue() == BillStatus.FINISH;
		return true;
	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public boolean isInvBatched(String invID) throws SQLException {

		String sql = "SELECT wholemanaflag FROM bd_invmandoc WHERE pk_invmandoc='" + invID + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		Object o = null;
		String isBom = "N";

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				o = rs.getObject(1);
				isBom = (o == null ? "N" : o.toString());
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return (isBom.equals("N") ? false : true);

	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public boolean isInvBom(String invID) throws SQLException {

		String sql = "SELECT isconfigable FROM bd_invmandoc WHERE pk_invmandoc='" + invID + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		Object o = null;
		String isBom = "N";

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				o = rs.getObject(1);
				isBom = (o == null ? "N" : o.toString());
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return (isBom.equals("N") ? false : true);

	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public boolean isInvExist(String pk_corp, String invID) throws SQLException {

		String sql = "SELECT wlbmid FROM mm_pzbom " + "WHERE pk_corp='" + pk_corp + "' and wlbmid = '" + invID + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		Object o = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				o = rs.getObject(1);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return (o == null ? false : true);

	}

	/**
	 * 通过主键查找一个VO对象。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public boolean isInvLocked(String sale_bid) throws SQLException {

		String sql = "SELECT ntotalreceivenumber, ntotalinvoicenumber, ntotalinventorynumber FROM so_saleexecute "
				+ "WHERE csale_bid = '" + sale_bid + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		Object o1 = null;
		Object o2 = null;
		Object o3 = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				o1 = rs.getObject(1);
				o2 = rs.getObject(2);
				o3 = rs.getObject(3);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		if (o1 != null && o1.toString().length() != 0)
			return true;
		else if (o2 != null && o2.toString().length() != 0)
			return true;
		else if (o3 != null && o3.toString().length() != 0)
			return true;
		else
			return false;

	}

	/**
	 * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
	 * 
	 * @return java.lang.Boolean
	 */
	public void isModifyNOriginalCurnetPrice(SaleOrderVO saleorder) throws nc.vo.pub.BusinessException {
		// 判断订单净价修改强制通过标志是否设置
		if (saleorder.getModPriceTag() == null || saleorder.getModPriceTag().booleanValue())
			return;
		String[] paracodes = { "SA15", "SA07", "SA02", "SO17" };
		// 价格参数
		UFBoolean SA_02 = null; // 基价含税
		UFBoolean SA_07 = null; // 价格是否可调整
		UFBoolean SA_15 = null; // 是否询价
		UFBoolean SO_17 = null; // 是否执行合同价格
		ArrayList modrowlist = null;
		try {
			// nc.bs.pub.para.SysInitHome
			// home=(nc.bs.pub.para.SysInitHome)getBeanHome(nc.bs.pub.para.SysInitHome.class,"nc.bs.pub.para.SysInitHome");
			// nc.bs.pub.para.SysInitBO
			// sysbo=(nc.bs.pub.para.SysInitBO)home.create();
			nc.bs.pub.para.SysInitBO sysbo = new nc.bs.pub.para.SysInitBO();
			java.util.Hashtable h = sysbo.queryBatchParaValues(((nc.vo.so.so001.SaleorderHVO) saleorder.getParentVO())
					.getPk_corp(), paracodes);

			String str = (String) h.get(paracodes[0]);
			if (str == null)
				SA_15 = new UFBoolean(false);
			else
				SA_15 = new UFBoolean(str);

			//
			str = (String) h.get(paracodes[1]);
			if (str == null)
				SA_07 = new UFBoolean(false);
			else
				SA_07 = new UFBoolean(str);

			str = (String) h.get(paracodes[2]);
			if (str == null)
				SA_02 = new UFBoolean(false);
			else
				SA_02 = new UFBoolean(str);

			str = (String) h.get(paracodes[3]);
			if (str == null)
				SO_17 = new UFBoolean(false);
			else
				SO_17 = new UFBoolean(str);

			// 如果是否询价SA_15=false，则无需判断
			if (!SA_15.booleanValue())
				return;
			// 如果价格是否可调整SA_07=false，则无需判断
			if (!SA_07.booleanValue())
				return;

			UFDouble discount = null;

			// 取该客户在客户管理档案中的默认折扣，如未设置，设为100（与ui端一致）
			nc.impl.scm.so.pub.FetchValueDMO fvdmo = new nc.impl.scm.so.pub.FetchValueDMO();
			String sdis = fvdmo.getColValue("bd_cumandoc", "discountrate", " pk_cumandoc='"
					+ ((nc.vo.so.so001.SaleorderHVO) saleorder.getParentVO()).getCcustomerid() + "' ");
			if (sdis == null || sdis.trim().length() <= 0)
				discount = new UFDouble(100);
			else
				discount = new UFDouble(sdis);
			discount = discount.div(100.0);
			for (int i = 0, count = saleorder.getChildrenVO().length; i < count; i++) {
				nc.vo.so.so001.SaleorderBVO border = (nc.vo.so.so001.SaleorderBVO) saleorder.getChildrenVO()[i];

				// 如果是退货，无需判断
				if (border.getNnumber() == null || border.getNnumber().doubleValue() <= 0.0)
					continue;

				// 如果是赠品，无需判断
				if (border.getBlargessflag().booleanValue())
					continue;

				// 来源为合同，且SO_17执行合同价格
				if (border.getCt_manageid() != null && border.getCt_manageid().trim().length() > 0) {
					// 如果SO_17执行合同价格,无需判断
					if (SO_17.booleanValue())
						continue;
					else {
						// ?未处理
					}
				}

				UFDouble price0 = null, price1 = null;
				// 根据基价是否含税SA_02，分别取含税与不含税价格与净价进行判断
				if (SA_02.booleanValue()) {
					price0 = border.getNoriginalcurtaxprice();
					price1 = border.getNoriginalcurtaxnetprice();
				} else {
					price0 = border.getNoriginalcurprice();
					price1 = border.getNoriginalcurnetprice();
				}
				// 根据询价价格及默认折扣计算原始净价，再与订单上的净价进行比较判断
				UFDouble dtmp = price0.multiply(discount).sub(price1).abs();
				if (dtmp.doubleValue() > 0.00000000001) {
					if (modrowlist == null)
						modrowlist = new ArrayList();
					modrowlist.add("" + i);
				}
			}

		} catch (Exception e) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000006")
			/* @res "检查单据净价出错" */);
		}
		if (modrowlist != null && modrowlist.size() > 0) {
			StringBuffer stemp = new StringBuffer();
			for (int i = 0, count = modrowlist.size(); i < count; i++) {
				if (i != 0)
					stemp.append(",");
				stemp.append(modrowlist.get(i).toString());
			}
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000007"/* "单据净价已改变，净价改变的相应行在：" */, null, new String[] { stemp.toString() }));
		}
	}

	/**
	 * 订单结束管理。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public SaleOrderVO[] mergeToSaleOrderVO(SaleorderBVO[] vobodys) throws javax.naming.NamingException,
			nc.bs.pub.SystemException, nc.vo.pub.BusinessException, java.sql.SQLException {
		if (vobodys == null || vobodys.length <= 0)
			return null;
		Hashtable hs = new Hashtable();
		Vector vlist = null;
		for (int i = 0; i < vobodys.length; i++) {
			vlist = (Vector) hs.get(vobodys[i].getCsaleid());
			if (vlist != null) {
				vlist.add(vobodys[i]);
			} else {
				vlist = new Vector();
				hs.put(vobodys[i].getCsaleid(), vlist);
				vlist.add(vobodys[i]);
			}
		}
		String[] ids = (String[]) hs.keySet().toArray(new String[0]);
		nc.vo.pub.CircularlyAccessibleValueObject[] hvos = queryAllHeadData(ids);

		if (ids == null || ids.length <= 0)
			return null;

		SaleOrderVO[] saleorders = new SaleOrderVO[hvos.length];
		for (int i = 0; i < hvos.length; i++) {
			saleorders[i] = new nc.vo.so.so001.SaleOrderVO();
			saleorders[i].setParentVO(hvos[i]);
			vlist = (Vector) hs.get(((SaleorderHVO) hvos[i]).getCsaleid());
			if (vlist != null)
				saleorders[i].setChildrenVO((SaleorderBVO[]) vlist.toArray(new SaleorderBVO[0]));
			else
				saleorders[i].setChildrenVO(null);
		}
		return saleorders;

	}

	/**
	 * 修订表体
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	/*
	 * public void modificationBody(SaleorderBVO saleItem, String strMainID,
	 * String creceipttype, PreparedStatement stmt, PreparedStatement stmtexe,
	 * PreparedStatement stmtupdate, PreparedStatement stmtfollow) throws
	 * java.sql.SQLException, SystemException { // 新增 // 查询 SaleorderBVO
	 * oldsaleItem = queryBodyData(saleItem.getPrimaryKey()); // 设置修订主键
	 * oldsaleItem.setCeditsaleid(oldsaleItem.getPrimaryKey()); // 设置修订理由
	 * oldsaleItem.setVeditreason(null); String key =
	 * getOID(oldsaleItem.getPkcorp()); oldsaleItem.setCorder_bid(key); // 新增记录
	 * insertBody(oldsaleItem, strMainID, creceipttype, stmt, stmtexe); // 修改
	 * updateBody(saleItem, stmtupdate, stmtfollow); }
	 */

	/**
	 * 检查合法性。
	 * 
	 * 创建日期：(2001-11-8 14:10:15)
	 * 
	 * @param saleorder
	 *            nc.vo.so.so001.SaleOrderVO
	 * 
	 */
	private void onCheck(SaleOrderVO saleorder) throws nc.vo.pub.BusinessException, javax.naming.NamingException,
			java.sql.SQLException, SystemException {

		// 检测单据号
		nc.impl.scm.so.pub.CheckValueValidityImpl checkDmo = new nc.impl.scm.so.pub.CheckValueValidityImpl();
		checkDmo.isValueValidity(saleorder);

		try {
			saleorder.validateForBS();

			// 检测买赠--v5.5去掉此检测。
			// 放在存货买赠保存时检查--不允许修改已被销售订单引用的买赠关系
			/** 只对非退货订单检查买赠关系 V502 jindongmei zhongwei* */
			/*
			 * if (saleorder.getHeadVO().getBretinvflag() != null &&
			 * !saleorder.getHeadVO().getBretinvflag().booleanValue())
			 * checkBuyLargess(saleorder);
			 */

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			throw new BusinessException(e.getMessage(),e);
		}
		// 存货是否匹配存货组织
		// checkStoreStructure(saleorder);

		// 判断行号
		try {
			SaleorderBVO[] bvos = saleorder.getBodyVOs();
			if (bvos != null && bvos.length > 0) {
				if (bvos[0].getCrowno() == null || bvos[0].getCrowno().trim().length() == 0) {
					BillRowNoDMO.setVORowNoByRule(saleorder, "30", "crowno");
				}
			}
		} catch (Exception e) {
			// 不处理异常.
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
		if (saleorder.getHeadVO() != null && saleorder.getHeadVO().getCsaleid() != null
				&& saleorder.getHeadVO().getFstatus() != null && saleorder.getHeadVO().getFstatus().intValue() == 2) {// 审批
			SaleorderBVO[] servos = queryExecDate(saleorder.getHeadVO().getCsaleid());

			if (servos != null && servos.length > 0) {

				String[] snames = null;

				snames = new String[] { "ntotalreceivenumber", "ntotalinvoicenumber", "ntotalshouldoutnum",
						"ntotalinvoicemny", "ntotalinventorynumber", "ntotalbalancenumber", "ntotalsignnumber",
						"ntotalcostmny" };

				Hashtable ht = new Hashtable();
				for (int i = 0, iLen = servos.length; i < iLen; i++) {
					for (int k = 0; k < snames.length; k++) {
						if (servos[i].getAttributeValue(snames[k]) != null
								&& (new UFDouble(servos[i].getAttributeValue(snames[k]).toString())).doubleValue() != 0) {
							ht.put(servos[i].getCorder_bid(), servos[i]);
							break;
						}
					}
				}
				if (ht.size() > 0) {
					SaleorderBVO bvo = null;
					for (int i = 0; i < saleorder.getBodyVOs().length; i++) {
						if (saleorder.getBodyVOs()[i].getCorder_bid() != null) {
							if (ht.containsKey(saleorder.getBodyVOs()[i].getCorder_bid())) {
								bvo = (SaleorderBVO) ht.get(saleorder.getBodyVOs()[i].getCorder_bid());
								if (bvo != null
										&& !bvo.getCinventoryid().equals(saleorder.getBodyVOs()[i].getCinventoryid())) {
									throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
											"UPP40060301-000506", null, new String[] { String.valueOf(i + 1) })
									/* @res "检查客户信用占用出错" */);
								}
							}
						}
					}
				}

			}
		}
	}

	/** 
	 * 销售订单列表切换表头查询表体数据
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllBodyData(String key) throws BusinessException {
		if (key == null || key.trim().length() <= 0)
			return null;

		String swhere = " so_saleorder_b.csaleid='" + key + "' ";
		try {
			SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
			if (saleItems == null || saleItems.length <= 0)
				return null;

			initBodyDataAfterQuery(saleItems);

			return saleItems;
		} catch (Exception e) {
			handleException(e);
		}
		
		return null;
	}
	
	/**
	 * 初始化合同、价格信息
	 * @param vos
	 */
	private void initCTPrice(nc.vo.pub.CircularlyAccessibleValueObject[] vos){
        if (vos!=null){
        	String[] formulas = {
					"cpriceitem->getColValue(prm_pricetype,CPRICETYPENAME,cpricetypeid,cpriceitemid)",
					"cpriceitemtablename->getColValue(prm_tariff,cpricetariffname,cpricetariffid,cpriceitemtable)",
					"cpricepolicy->getColValue(prm_pricepolicy,pricepolicyname,pricepolicyid,cpricepolicyid)", 
					"ctinvclassid->getColValue(ct_manage_b,invclid,pk_ct_manage_b,ct_managebid)",
					"ct_name->getColValue(ct_manage,ct_name,pk_ct_manage,ct_manageid)",
					"ct_code->getColValue(ct_manage,ct_code,pk_ct_manage,ct_manageid)",
					"norgviapricetax->iif(cpackunitid=null,null,(noriginalcursummny+noriginalcurdiscountmny)/npacknumber)",
					"norgviaprice->iif(cpackunitid=null,null,(noriginalcursummny+noriginalcurdiscountmny)/((1+ntaxrate/100.0)*npacknumber))"
					};
        	SoVoTools.execFormulasAtBs(formulas, vos);
        }
	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllBodyData(String key, String where)
			throws BusinessException {
		String swhere = new String();

		if (key == null || key.trim().length() <= 0) {
			swhere = " so_saleorder_b.csaleid in (select so_sale.csaleid from so_sale where " + where + ") ";

		} else {

			swhere = " so_saleorder_b.csaleid='" + key + "' " + (where == null ? "" : where);
			// +" order by so_saleorder_b.corder_bid ";
		}
		try {
			SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
			if (saleItems == null || saleItems.length <= 0)
				return null;

			initBodyDataAfterQuery(saleItems);
			
			/** ********************************************************** */
			// 保留的系统管理接口：
			afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { key });
			/** ********************************************************** */
			
			return saleItems;
			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

	}
	
	/**
	 * 查询完表体数据后补充vo信息
	 * @param vos
	 * @throws Exception 
	 */
	private void initBodyDataAfterQuery(SaleorderBVO[] saleItems) throws Exception{
		//初始化自由项
		initFreeItem(saleItems);
		
		//初始化合同、价格信息
		initCTPrice(saleItems);
	}
	
	public SaleorderBVO[] queryAllBodyDataByBIDs(String[] bodykeys) throws BusinessException {
		if (bodykeys == null || bodykeys.length <= 0)
			return null;
		String swhere = GeneralSqlString.formInSQL("so_saleorder_b.corder_bid",
				bodykeys);
		try {
			return queryAllBodyDataByWhere(swhere, null);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	public SaleorderBVO[] queryAllBodyDataByHIDs(String[] hkeys) throws Exception {
		if (hkeys == null || hkeys.length <= 0)
			return null;
		
		String sWhere = GeneralSqlString.formInSQL("so_saleorder_b.csaleid", hkeys);
		return queryAllBodyDataByWhere(sWhere, null);
	}

	public Hashtable<String, ArrayList<SaleorderBVO>> queryAllBodyDataBySourceIDs(String[] hkeys) throws SQLException {

		if (hkeys == null || hkeys.length <= 0)
			return null;
		String sWhere = GeneralSqlString.formInSQL("so_saleorder_b.csourcebillbodyid", hkeys);
		return queryAllBodyDataHashByWhere(sWhere, null);
	}

	/*
	 * 修改日期：2003-10-16 修改人：杨涛 修改内容：表头增加“预收款比例”和“预收款金额”
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllHeadData(String[] ids) throws BusinessException {
		if (ids == null || ids.length <= 0)
			return null;
		// int onecount = 200;
		StringBuffer wheresql = null;
		ArrayList relist = new ArrayList();
		int hvocount = 0;
		for (int i = 0, count = (ids.length % 200 == 0 ? ids.length / 200 : ids.length / 200 + 1); i < count; i++) {
			wheresql = new StringBuffer();
			for (int j = 0, count1 = ids.length - i * 200 > 200 ? 200 : ids.length - i * 200; j < count1; j++) {
				if (j > 0)
					wheresql.append(" , ");
				wheresql.append("'" + ids[i * 200 + j] + "'");
			}
			SaleorderHVO[] hvos = (SaleorderHVO[]) queryAllHeadData(" so_sale.csaleid in ( " + wheresql.toString()
					+ " ) ");
			relist.add(hvos);
			hvocount += hvos.length;
		}
		SaleorderHVO[] reobjs = new SaleorderHVO[hvocount];
		int pos = 0;
		for (int i = 0, count = relist.size(); i < count; i++) {
			SaleorderHVO[] hvos = (SaleorderHVO[]) relist.get(i);
			System.arraycopy(hvos, 0, reobjs, pos, hvos.length);
			pos += hvos.length;
		}
		return reobjs;

	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllHeadData(String where) throws BusinessException {
		return queryAllHeadData(where, null);
	}

	/**
	 * 待审批条件：objIsWaitAudit[0] -- "Y" 有待审批条件 "N" 无待审批条件 objIsWaitAudit[1] --
	 * 当前操作员ID
	 * 
	 * 修改日期：2003-10-16 修改人：杨涛 修改内容：表头增加“预收款比例”和“预收款金额”
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllHeadData(String where, Object objIsWaitAudit)
			throws BusinessException {
		//----v56 补货直运连接数问题：统一查询‘核算规则’置入headVO
		HashMap<String,String> hm_verifyrule= new HashMap<String,String>();
		if(where != null) {
			int begin = where.indexOf("pk_corp");
			int beginIndex = begin + 11;
			int endIndex = begin + 15;
			String pk_corp = where.substring(beginIndex, endIndex);
			if (pk_corp != null) {
				IPFMetaModel bo = (IPFMetaModel) NCLocator.getInstance().lookup(IPFMetaModel.class.getName());
				BusitypeVO[] busvos;
				try {
					busvos = bo.queryAllBusitypes(pk_corp);
					if(busvos != null ) {
						for(BusitypeVO busvo : busvos) {
							hm_verifyrule.put(busvo.getPk_busitype(), busvo.getVerifyrule());
						}
					}
				} catch (BusinessException e) {
					//e.printStackTrace();
					return null;
				}
			}
		}
		//---------------end
		// 设置其他增加的字段
		SOField[] addfields = SaleorderHVO.getAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getTablename());
					addfieldsql.append(".");
					addfieldsql.append(addfields[i].getDatabasename());
				}
			}
		}
		// boolean isso_saleexecute = false;
		// boolean isbd_cumandoc = false;
		// boolean isbd_cubasdoc = false;
		if (where != null) {
			if (where.indexOf("so_saleexecute") >= 0)
				// isso_saleexecute = true
				;
			// if(where.indexOf("bd_cumandoc")>=0)
			// isbd_cumandoc = true;

		}
		String strSql = "SELECT DISTINCT so_sale.pk_corp, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,"
				+ "so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, "
				+ "so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, "
				+ "so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, "
				+ "so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, "
				+ "so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,"
				+ "so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.csaleid,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,"
				+ "so_sale.breceiptendflag,so_sale.ts,so_sale.npreceiverate,so_sale.npreceivemny,so_sale.bpayendflag,so_saleorder_b.ccurrencytypeid, so_sale.nheadsummny,"
				+ "so_sale.editionnum,so_sale.editdate,so_sale.editauthor"// v5.5
				// 周长胜添加
				// 2008-09-01
				+ addfieldsql.toString() + " FROM so_sale, so_saleorder_b,so_saleexecute ";

		strSql += " where so_saleexecute.creceipttype = '30' ";
		strSql += " and so_sale.csaleid = so_saleorder_b.csaleid  and so_sale.csaleid = so_saleexecute.csaleid ";
		strSql += " and so_saleorder_b.corder_bid = so_saleexecute.csale_bid  "
				+ " and so_saleorder_b.csaleid = so_saleexecute.csaleid ";
		//strSql += " and bd_invbasdoc.pk_invbasdoc = so_saleorder_b.cinvbasdocid ";
		strSql += " and so_sale.dr = 0 and so_saleorder_b.dr = 0 and so_saleexecute.dr = 0 ";

		if (where != null && where.trim().length() > 0)
			strSql = strSql + " AND " + where;

		strSql = strSql + " order by so_sale.vreceiptcode ";
		SaleorderHVO[] saleorderHs = null;
		ArrayList csaleids = new ArrayList();
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		con = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				SaleorderHVO saleHeader = new SaleorderHVO();
				// pk_corp :
				String pk_corp = rs.getString(1);
				saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());
				// vreceiptcode :
				String vreceiptcode = rs.getString(2);
				saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());
				// creceipttype :
				String creceipttype = rs.getString(3);
				saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
				// cbiztype :
				String cbiztype = rs.getString(4);
				saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());
				// finvoiceclass :
				Integer finvoiceclass = (Integer) rs.getObject(5);
				saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);
				// finvoicetype :
				Integer finvoicetype = (Integer) rs.getObject(6);
				saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);
				// vaccountyear :
				String vaccountyear = rs.getString(7);
				saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());
				// binitflag :
				String binitflag = rs.getString(8);
				saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));
				// dbilldate :
				String dbilldate = rs.getString(9);
				saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));
				// ccustomerid :
				String ccustomerid = rs.getString(10);
				saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());
				// cdeptid :
				String cdeptid = rs.getString(11);
				saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());
				// cemployeeid :
				String cemployeeid = rs.getString(12);
				saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());
				// coperatorid :
				String coperatorid = rs.getString(13);
				saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());
				// ctermprotocolid :
				String ctermprotocolid = rs.getString(14);
				saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());
				// csalecorpid :
				String csalecorpid = rs.getString(15);
				saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());
				// creceiptcustomerid :
				String creceiptcustomerid = rs.getString(16);
				saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());
				// vreceiveaddress :
				String vreceiveaddress = rs.getString(17);
				saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());
				// creceiptcorpid :
				String creceiptcorpid = rs.getString(18);
				saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());
				// ctransmodeid :
				String ctransmodeid = rs.getString(19);
				saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());
				// ndiscountrate :
				BigDecimal ndiscountrate = rs.getBigDecimal(20);
				saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));
				// cwarehouseid :
				String cwarehouseid = rs.getString(21);
				saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());
				// veditreason :
				String veditreason = rs.getString(22);
				saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());
				// bfreecustflag :
				String bfreecustflag = rs.getString(23);
				saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));
				// cfreecustid :
				String cfreecustid = rs.getString(24);
				saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());
				// ibalanceflag :
				Integer ibalanceflag = (Integer) rs.getObject(25);
				saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);
				// nsubscription :
				BigDecimal nsubscription = rs.getBigDecimal(26);
				saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));
				// ccreditnum :
				String ccreditnum = rs.getString(27);
				saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());
				// nevaluatecarriage :
				BigDecimal nevaluatecarriage = rs.getBigDecimal(28);
				saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));
				// dmakedate :
				String dmakedate = rs.getString(29);
				saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));
				// capproveid :
				String capproveid = rs.getString(30);
				saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());
				// dapprovedate :
				String dapprovedate = rs.getString(31);
				saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));
				// fstatus :
				Integer fstatus = (Integer) rs.getObject(32);
				saleHeader.setFstatus(fstatus == null ? null : fstatus);
				// vnote :
				String vnote = rs.getString(33);
				saleHeader.setVnote(vnote == null ? null : vnote.trim());
				// vdef1 :
				String vdef1 = rs.getString(34);
				saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());
				// vdef2 :
				String vdef2 = rs.getString(35);
				saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());
				// vdef3 :
				String vdef3 = rs.getString(36);
				saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());
				// vdef4 :
				String vdef4 = rs.getString(37);
				saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());
				// vdef5 :
				String vdef5 = rs.getString(38);
				saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());
				// vdef6 :
				String vdef6 = rs.getString(39);
				saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());
				// vdef7 :
				String vdef7 = rs.getString(40);
				saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());
				// vdef8 :
				String vdef8 = rs.getString(41);
				saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());
				// vdef9 :
				String vdef9 = rs.getString(42);
				saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());
				// vdef10 :
				String vdef10 = rs.getString(43);
				saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());
				// ccalbodyid :
				String ccalbodyid = rs.getString(44);
				saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());
				// csaleid :
				String csaleid = rs.getString(45);
				saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());

				csaleids.add(csaleid);

				// bretinvflag :
				String bretinvflag = rs.getString(46);
				saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));
				// boutendflag :
				String boutendflag = rs.getString(47);
				saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));
				// binvoicendflag :
				String binvoicendflag = rs.getString(48);
				saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));
				// breceiptendflag :
				String breceiptendflag = rs.getString(49);
				saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));
				// ts
				String ts = rs.getString(50);
				saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

				// yt add 2003-10-16
				// npreceiverate
				BigDecimal npreceiverate = rs.getBigDecimal(51);
				saleHeader.setNpreceiverate(npreceiverate == null ? null : new UFDouble(npreceiverate));
				// npreceivemny
				BigDecimal npreceivemny = rs.getBigDecimal(52);
				saleHeader.setNpreceivemny(npreceivemny == null ? null : new UFDouble(npreceivemny));

				String bpayendflag = rs.getString(53);
				saleHeader.setBpayendflag(bpayendflag == null ? null : new UFBoolean(bpayendflag.trim()));

				// ccurrencytypeid :
				String ccurrencytypeid = rs.getString(54);
				saleHeader
						.setAttributeValue("ccurrencytypeid", ccurrencytypeid == null ? null : ccurrencytypeid.trim());
				// npreceivemny
				BigDecimal nheadsummny = rs.getBigDecimal(55);
				saleHeader.setAttributeValue("nheadsummny", nheadsummny == null ? null : new UFDouble(nheadsummny));
				// v5.5 周长胜添加 2008-09-01
				String editionnum = rs.getString(56);
				saleHeader.setAttributeValue("editionnum", editionnum == null ? null : editionnum.trim());

				String editdate = rs.getString(57);
				saleHeader.setAttributeValue("editdate", editdate == null ? null : editdate.trim());

				String editauthor = rs.getString(58);
				saleHeader.setAttributeValue("editauthor", editauthor == null ? null : editauthor.trim());

				// v5.6 liuzwc 
				String verifyrule = hm_verifyrule.get(cbiztype);
				saleHeader.setAttributeValue("verifyrule", verifyrule == null ? null : verifyrule.trim());
				
				// 获取v30后加入的其他字段值

				getOrdHValueFromResultSet(rs, 59, saleHeader);

				// if(addfields!=null){
				// for (int i = 0, loop = addfields.length; i < loop; i++) {
				// getValueFromResultSet(rs,55+i,addfields[i],saleHeader);
				// }
				// }

				v.addElement(saleHeader);
			}

			if (v.size() > 0) {
				saleorderHs = new SaleorderHVO[v.size()];
				v.copyInto(saleorderHs);
				// 查询订单现收数据
				HashMap hscachpay = queryCachPayByOrdIds((String[]) csaleids.toArray(new String[csaleids.size()]));
				if (hscachpay != null) {
					for (int i = 0, loop = saleorderHs.length; i < loop; i++) {
						saleorderHs[i].setNreceiptcathmny((UFDouble) hscachpay.get(saleorderHs[i].getCsaleid()));
					}
				}

				// 待审批条件
				if (objIsWaitAudit != null && ((Object[]) objIsWaitAudit)[0].toString().equals("Y")) {
					String userid = ((Object[]) objIsWaitAudit)[1].toString();

					// 查询满足待审批条件的主表id
					String[] billIdAry = new String[saleorderHs.length];
					for (int i = 0, loop = saleorderHs.length; i < loop; i++)
						billIdAry[i] = saleorderHs[i].getPrimaryKey();
					IPFWorkflowQry ipfQry = (IPFWorkflowQry) NCLocator.getInstance().lookup(
							IPFWorkflowQry.class.getName());
					String[] hids = ipfQry.isCheckmanAry(billIdAry, SaleBillType.SaleOrder, userid);

					// 过滤
					if (hids != null && hids.length > 0) {
						// 满足条件的hid--HashMap
						HashMap hid_map = new HashMap();
						for (String hid : hids)
							hid_map.put(hid, hid);

						// 满足条件的hvo
						ArrayList<SaleorderHVO> hvo_list = new ArrayList<SaleorderHVO>();
						for (SaleorderHVO hvo : saleorderHs)
							if (hid_map.containsKey(hvo.getPrimaryKey()))
								hvo_list.add(hvo);

						// 返回结果
						SaleorderHVO[] rethvo = hvo_list.toArray(new SaleorderHVO[hids.length]);
						return rethvo;
					}
				}
				// 待审批条件

			}

		} catch (SQLException e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		
		//将"   "置null
		NullFieldUtils.setNull(saleorderHs);
		
		return saleorderHs;
	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(String where) throws SQLException, Exception {

		if (where == null || where.trim().length() <= 0)
			return null;

		String swhere = " so_saleorder_b.csaleid='" + where + "' ";

		SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
		if (saleItems == null || saleItems.length <= 0)
			return null;
		initFreeItem(saleItems);
		return saleItems;
	}

	public SaleorderBVO queryBodyData(String key) throws Exception {

		if (key == null || key.trim().length() <= 0)
			return null;

		// 设置其他增加的字段
		String swhere = " so_saleorder_b.corder_bid='" + key + "' ";

		SaleorderBVO[] saleItems = queryAllBodyDataByWhere(swhere, null);
		if (saleItems == null || saleItems.length <= 0)
			return null;

		initFreeItem(saleItems);

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", new Object[] { key });
		/** ********************************************************** */
		return saleItems[0];
	}

	/**
	 * @param csaleid
	 */
	public SaleorderBVO[] queryExecDate(String csaleid) throws SQLException, NamingException, SystemException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT so_saleexecute.csaleid,so_saleexecute.csale_bid,");
		sql
				.append("so_saleexecute.ntotalreceivenumber, so_saleexecute.ntotalinvoicenumber,ntotalshouldoutnum, ntotalinvoicemny, ntotalinventorynumber,");
		sql.append("ntotalbalancenumber, ntotalsignnumber, ntotalcostmny ,so_saleorder_b.cinventoryid ");
		sql.append("FROM  ");
		sql.append(" so_saleexecute,so_saleorder_b ");
		sql.append("WHERE so_saleexecute.csaleid = '" + csaleid + "' and so_saleexecute.creceipttype = '30' ").append(
				" and so_saleexecute.csale_bid = so_saleorder_b.corder_bid");
		SmartDMO sdmo = new SmartDMO();

		Object[] o = sdmo.selectBy2(sql.toString());
		if (o == null || o.length == 0)
			return null;
		Object[] o1 = null;
		SaleorderBVO bvo = null;
		Vector vt = new Vector();
		for (int i = 0; i < o.length; i++) {
			o1 = (Object[]) o[i];
			if (o1 == null || o1.length == 0)
				continue;
			bvo = new SaleorderBVO();
			bvo.setCsaleid(o1[0].toString());
			bvo.setCorder_bid(o1[1].toString());
			bvo.setAttributeValue("ntotalreceivenumber", o1[2] == null ? new UFDouble(0) : new UFDouble(o1[2]
					.toString()));
			bvo.setAttributeValue("ntotalinvoicenumber", o1[3] == null ? new UFDouble(0) : new UFDouble(o1[3]
					.toString()));
			bvo.setAttributeValue("ntotalshouldoutnum", o1[4] == null ? new UFDouble(0)
					: new UFDouble(o1[4].toString()));
			bvo.setAttributeValue("ntotalinvoicemny", o1[5] == null ? new UFDouble(0) : new UFDouble(o1[5].toString()));
			bvo.setAttributeValue("ntotalinventorynumber", o1[6] == null ? new UFDouble(0) : new UFDouble(o1[6]
					.toString()));
			bvo.setAttributeValue("ntotalbalancenumber", o1[7] == null ? new UFDouble(0) : new UFDouble(o1[7]
					.toString()));
			bvo.setAttributeValue("ntotalsignnumber", o1[8] == null ? new UFDouble(0) : new UFDouble(o1[8].toString()));
			bvo.setAttributeValue("ntotalcostmny", o1[9] == null ? new UFDouble(0) : new UFDouble(o1[9].toString()));
			bvo.setAttributeValue("cinventoryid", o1[10] == null ? null : o1[10].toString());
			vt.add(bvo);
		}
		SaleorderBVO[] bvos = new SaleorderBVO[vt.size()];
		vt.copyInto(bvos);
		return bvos;

	}

	public SaleOrderVO queryData(String strID) throws SQLException, Exception {
		Connection con = null;
		PreparedStatement stmt = null;

		SaleOrderVO saleorder = new SaleOrderVO();
		con = getConnection();
		try {
			String strWhere = "so_sale.csaleid = '" + strID + "'";
			nc.vo.pub.CircularlyAccessibleValueObject[] Headvo = queryAllHeadData(strWhere);
			if (Headvo != null && Headvo.length > 0)
				saleorder.setParentVO(Headvo[0]);
			saleorder.setChildrenVO(queryAllBodyData(strID));
			//查询现收数据
			saleorder.getHeadVO().setNreceiptcathmny(queryCachPayByOrdId(strID));
			
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return saleorder;
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public SaleOrderVO[] queryDataByContract(String strContractID) throws SQLException, Exception {

		String[] strIDs = queryOrderIDByContractID(strContractID);

		SaleOrderVO[] saleorders = null;

		if (strIDs != null) {
			saleorders = new SaleOrderVO[strIDs.length];
			for (int i = 0; i < strIDs.length; i++) {
				saleorders[i] = queryData(strIDs[i]);
			}
		}

		return saleorders;
	}

	public AggregatedValueObject[] queryDataByWhere(String swhere) throws BusinessException {

		if (swhere == null)
			return null;

		// 设置子表其他增加的字段
		SOField[] addfields = SaleorderBVO.getAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getTablename());
					addfieldsql.append(".");
					addfieldsql.append(addfields[i].getDatabasename());
				}
			}
		}

		String sql = "SELECT DISTINCT so_sale.pk_corp, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,"
				+ "so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, "
				+ "so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, "
				+ "so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, "
				+ "so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, "
				+ "so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,"
				+ "so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.csaleid,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,"
				+ "so_sale.breceiptendflag, so_sale.ts,so_sale.npreceiverate,so_sale.npreceivemny,"

				+ "so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, "
				+ "so_saleorder_b.creceipttype, csourcebillid, csourcebillbodyid, "
				+ "cinventoryid, cunitid, cpackunitid, nnumber, npacknumber, "
				+ "cbodywarehouseid, so_saleorder_b.dconsigndate, ddeliverdate, blargessflag, "
				+ "so_sale.pk_defdoc12, so_sale.pk_defdoc13, so_saleorder_b.veditreason, so_saleorder_b.ccurrencytypeid, so_saleorder_b.nitemdiscountrate, "
				+ "so_saleorder_b.ndiscountrate, nexchangeotobrate, so_sale.pk_defdoc11, ntaxrate, noriginalcurprice, "
				+ "noriginalcurtaxprice, noriginalcurnetprice, noriginalcurtaxnetprice, "
				+ "noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, "
				+ "noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, "
				+ "ntaxmny, nmny, nsummny, so_saleorder_b.ndiscountmny, so_saleorder_b.coperatorid, frowstatus, "
				+ "frownote,cinvbasdocid,cbatchid,fbatchstatus,cbomorderid,cfreezeid,ct_manageid, so_saleorder_b.ts, "
				+ "cadvisecalbodyid, boosflag, bsupplyflag, so_saleorder_b.creceiptareaid, so_saleorder_b.vreceiveaddress, "
				+ "so_saleorder_b.creceiptcorpid, so_saleorder_b.crowno,"

				// exe
				+ " so_saleexecute.creceipttype, ntotalpaymny, ntotalreceivenumber, ntotalinvoicenumber, "
				+ "ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, "
				+ "bifinvoicefinish, bifreceivefinish, bifinventoryfinish, bifpayfinish, so_sale.pk_defdoc15, so_sale.pk_defdoc10, "
				+ "so_sale.pk_defdoc16, so_sale.pk_defdoc17, so_sale.pk_defdoc18, so_sale.pk_defdoc19, so_sale.pk_defdoc20, so_saleexecute.ts, so_sale.dbilltime, so_sale.daudittime, "
				+ "cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, "
				+ "vfree4, vfree5, so_saleexecute.vdef1, so_saleexecute.vdef2, so_saleexecute.vdef3, "
				+ "so_saleexecute.vdef4, so_saleexecute.vdef5, so_saleexecute.vdef6, "
				+ "so_sale.iprintcount, so_sale.pk_defdoc14, "
				+ "so_sale.vdef11,so_sale.vdef12,so_sale.vdef13,so_sale.vdef14,so_sale.vdef15,so_sale.vdef16, "
				+ "so_sale.vdef17,so_sale.vdef18,so_sale.vdef19,so_sale.vdef20,  "
				+ "so_sale.pk_defdoc1,so_sale.pk_defdoc2,so_sale.pk_defdoc3,so_sale.pk_defdoc4, "
				+ "so_sale.pk_defdoc5,so_sale.pk_defdoc6,so_sale.pk_defdoc7,so_sale.pk_defdoc8, "
				+ "so_sale.pk_defdoc9 "

				// v30add
				+ addfieldsql.toString()

				+ " FROM so_sale, so_saleorder_b,so_saleexecute "

				+ " where so_sale.csaleid = so_saleorder_b.csaleid AND so_sale.csaleid = so_saleexecute.csaleid  "
				+ " AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid  "
				+ " AND so_saleexecute.creceipttype='30' AND so_sale.dr=0  AND " + swhere;
		nc.vo.scm.pub.SCMEnv.out(sql);

		SaleorderBVO saleItem = null;
		SaleorderHVO saleHeader = null;
		// HashMap hordvo=new HashMap();
		HashMap hordhvo = new HashMap();
		HashMap hordbvo = new HashMap();
		ArrayList lordbvolist = null;

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {

				saleHeader = new SaleorderHVO();

				// pk_corp :
				String pk_corp = rs.getString(1);
				saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());
				// vreceiptcode :
				String vreceiptcode = rs.getString(2);
				saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());
				// creceipttype :
				String creceipttype = rs.getString(3);
				saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
				// cbiztype :
				String cbiztype = rs.getString(4);
				saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());
				// finvoiceclass :
				Integer finvoiceclass = (Integer) rs.getObject(5);
				saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);
				// finvoicetype :
				Integer finvoicetype = (Integer) rs.getObject(6);
				saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);
				// vaccountyear :
				String vaccountyear = rs.getString(7);
				saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());
				// binitflag :
				String binitflag = rs.getString(8);
				saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));
				// dbilldate :
				String dbilldate = rs.getString(9);
				saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));
				// ccustomerid :
				String ccustomerid = rs.getString(10);
				saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());
				// cdeptid :
				String cdeptid = rs.getString(11);
				saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());
				// cemployeeid :
				String cemployeeid = rs.getString(12);
				saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());
				// coperatorid :
				String coperatorid = rs.getString(13);
				saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());
				// ctermprotocolid :
				String ctermprotocolid = rs.getString(14);
				saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());
				// csalecorpid :
				String csalecorpid = rs.getString(15);
				saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());
				// creceiptcustomerid :
				String creceiptcustomerid = rs.getString(16);
				saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());
				// vreceiveaddress :
				String vreceiveaddress = rs.getString(17);
				saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());
				// creceiptcorpid :
				String creceiptcorpid = rs.getString(18);
				saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());
				// ctransmodeid :
				String ctransmodeid = rs.getString(19);
				saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());
				// ndiscountrate :
				BigDecimal ndiscountrate = (BigDecimal) rs.getObject(20);
				saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate.toString()));
				// cwarehouseid :
				String cwarehouseid = rs.getString(21);
				saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());
				// veditreason :
				String veditreason = rs.getString(22);
				saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());
				// bfreecustflag :
				String bfreecustflag = rs.getString(23);
				saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));
				// cfreecustid :
				String cfreecustid = rs.getString(24);
				saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());
				// ibalanceflag :
				Integer ibalanceflag = (Integer) rs.getObject(25);
				saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);
				// nsubscription :
				Object nsubscription = (BigDecimal) rs.getObject(26);
				saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription.toString()));
				// ccreditnum :
				String ccreditnum = rs.getString(27);
				saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());
				// nevaluatecarriage :
				BigDecimal nevaluatecarriage = (BigDecimal) rs.getObject(28);
				saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));
				// dmakedate :
				String dmakedate = rs.getString(29);
				saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));
				// capproveid :
				String capproveid = rs.getString(30);
				saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());
				// dapprovedate :
				String dapprovedate = rs.getString(31);
				saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));
				// fstatus :
				Integer fstatus = (Integer) rs.getObject(32);
				saleHeader.setFstatus(fstatus == null ? null : fstatus);
				// vnote :
				String vnote = rs.getString(33);
				saleHeader.setVnote(vnote == null ? null : vnote.trim());
				// vdef1 :
				String vdef1 = rs.getString(34);
				saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());
				// vdef2 :
				String vdef2 = rs.getString(35);
				saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());
				// vdef3 :
				String vdef3 = rs.getString(36);
				saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());
				// vdef4 :
				String vdef4 = rs.getString(37);
				saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());
				// vdef5 :
				String vdef5 = rs.getString(38);
				saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());
				// vdef6 :
				String vdef6 = rs.getString(39);
				saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());
				// vdef7 :
				String vdef7 = rs.getString(40);
				saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());
				// vdef8 :
				String vdef8 = rs.getString(41);
				saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());
				// vdef9 :
				String vdef9 = rs.getString(42);
				saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());
				// vdef10 :
				String vdef10 = rs.getString(43);
				saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());
				// ccalbodyid :
				String ccalbodyid = rs.getString(44);
				saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());
				// csaleid :
				String csaleid = rs.getString(45);
				saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());
				// bretinvflag :
				String bretinvflag = rs.getString(46);
				saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));
				// boutendflag :
				String boutendflag = rs.getString(47);
				saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));
				// binvoicendflag :
				String binvoicendflag = rs.getString(48);
				saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));
				// breceiptendflag :
				String breceiptendflag = rs.getString(49);
				saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));
				// ts
				String ts = rs.getString(50);
				saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

				// yt add 2003-10-16
				// npreceiverate
				BigDecimal npreceiverate = (BigDecimal) rs.getObject(51);
				saleHeader.setNpreceiverate(npreceiverate == null ? null : new UFDouble(npreceiverate));
				// npreceivemny
				BigDecimal npreceivemny = (BigDecimal) rs.getObject(52);
				saleHeader.setNpreceivemny(npreceivemny == null ? null : new UFDouble(npreceivemny));

				if (hordhvo.get(saleHeader.getCsaleid()) == null) {
					hordhvo.put(saleHeader.getCsaleid(), saleHeader);
				}

				// /body
				saleItem = new SaleorderBVO();
				//
				String corder_bid = rs.getString(53);
				saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());
				//
				csaleid = rs.getString(54);
				saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());
				//
				String ccorpid = rs.getString(55);
				saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());
				//
				creceipttype = rs.getString(56);
				saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
				//
				String csourcebillid = rs.getString(57);
				saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());
				//
				String csourcebillbodyid = rs.getString(58);
				saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());
				//
				String cinventoryid = rs.getString(59);
				saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());
				//
				String cunitid = rs.getString(60);
				saleItem.setCunitid(cunitid == null ? null : cunitid.trim());
				//
				String cpackunitid = rs.getString(61);
				saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());
				//
				BigDecimal nnumber = (BigDecimal) rs.getObject(62);
				saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));
				//
				BigDecimal npacknumber = (BigDecimal) rs.getObject(63);
				saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));
				//
				String cbodywarehouseid = rs.getString(64);
				saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());
				//
				String dconsigndate = rs.getString(65);
				saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));
				//
				String ddeliverdate = rs.getString(66);
				saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));
				//
				String blargessflag = rs.getString(67);
				saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

				// pk_defdoc12 :
				String pk_defdoc12 = rs.getString(68);
				saleHeader.setPk_defdoc12(pk_defdoc12 == null ? null : pk_defdoc12.trim());

				// pk_defdoc13 :
				String pk_defdoc13 = rs.getString(69);
				saleHeader.setPk_defdoc13(pk_defdoc13 == null ? null : pk_defdoc13.trim());
				//
				veditreason = rs.getString(70);
				saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());
				//
				String ccurrencytypeid = rs.getString(71);
				saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());
				//
				BigDecimal nitemdiscountrate = (BigDecimal) rs.getObject(72);
				saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));
				//
				ndiscountrate = (BigDecimal) rs.getObject(73);
				saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));
				//
				BigDecimal nexchangeotobrate = (BigDecimal) rs.getObject(74);
				saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

				// pk_defdoc11 :
				String pk_defdoc11 = rs.getString(75);
				saleHeader.setPk_defdoc11(pk_defdoc11 == null ? null : pk_defdoc11.trim());

				BigDecimal ntaxrate = (BigDecimal) rs.getObject(76);
				saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));
				//
				BigDecimal noriginalcurprice = (BigDecimal) rs.getObject(77);
				saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));
				//
				BigDecimal noriginalcurtaxprice = (BigDecimal) rs.getObject(78);
				saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(
						noriginalcurtaxprice));
				//
				BigDecimal noriginalcurnetprice = (BigDecimal) rs.getObject(79);
				saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(
						noriginalcurnetprice));
				//
				BigDecimal noriginalcurtaxnetprice = (BigDecimal) rs.getObject(80);
				saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(
						noriginalcurtaxnetprice));
				//
				BigDecimal noriginalcurtaxmny = (BigDecimal) rs.getObject(81);
				saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));
				//
				BigDecimal noriginalcurmny = (BigDecimal) rs.getObject(82);
				saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));
				//
				BigDecimal noriginalcursummny = (BigDecimal) rs.getObject(83);
				saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));
				//
				BigDecimal noriginalcurdiscountmny = (BigDecimal) rs.getObject(84);
				saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(
						noriginalcurdiscountmny));
				//
				BigDecimal nprice = (BigDecimal) rs.getObject(85);
				saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));
				//
				BigDecimal ntaxprice = (BigDecimal) rs.getObject(86);
				saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));
				//
				BigDecimal nnetprice = (BigDecimal) rs.getObject(87);
				saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));
				//
				BigDecimal ntaxnetprice = (BigDecimal) rs.getObject(88);
				saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));
				//
				BigDecimal ntaxmny = (BigDecimal) rs.getObject(89);
				saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));
				//
				BigDecimal nmny = (BigDecimal) rs.getObject(90);
				saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));
				//
				BigDecimal nsummny = (BigDecimal) rs.getObject(91);
				saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));
				//
				BigDecimal ndiscountmny = (BigDecimal) rs.getObject(92);
				saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));
				//
				coperatorid = rs.getString(93);
				saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());
				//
				Integer frowstatus = (Integer) rs.getObject(94);
				saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);
				//
				String frownote = rs.getString(95);
				saleItem.setFrownote(frownote == null ? null : frownote.trim());
				// //
				String cinvbasdocid = rs.getString(96);
				saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());
				//
				String cbatchid = rs.getString(97);
				saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());
				// fbatchstatus :
				Integer fbatchstatus = (Integer) rs.getObject(98);
				saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);
				//
				String cbomorderid = rs.getString(99);
				saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());
				//
				String cfreezeid = rs.getString(100);
				saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());
				//
				String ct_manageid = rs.getString(101);
				saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());
				// ts
				ts = rs.getString(102);
				saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));
				// cadvisecalbodyid
				String cadvisecalbodyid = rs.getString(103);
				saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());
				// boosflag
				String boosflag = rs.getString(104);
				saleItem.setBoosflag(boosflag == null ? null : new UFBoolean(boosflag.trim()));
				// bsupplyflag
				String bsupplyflag = rs.getString(105);
				saleItem.setBsupplyflag(bsupplyflag == null ? null : new UFBoolean(bsupplyflag.trim()));
				// creceiptareaid
				String creceiptareaid = rs.getString(106);
				saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());
				// vreceiveaddress
				vreceiveaddress = rs.getString(107);
				saleItem.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());
				// creceiptcorpid
				creceiptcorpid = rs.getString(108);
				saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());
				// crowno
				String crowno = rs.getString(109);
				saleItem.setCrowno(crowno == null ? null : crowno.trim());

				// exe

				// creceipttype :
				creceipttype = rs.getString(110);
				// saleItem.setCreceipttype(creceipttype == null ? null :
				// creceipttype.trim());
				// ntotalpaymny :
				BigDecimal ntotalpaymny = (BigDecimal) rs.getObject(111);
				saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));
				// ntotalreceivenumber :
				BigDecimal ntotalreceivenumber = (BigDecimal) rs.getObject(112);
				saleItem.setNtotalreceivenumber(ntotalreceivenumber == null ? null : new UFDouble(ntotalreceivenumber));
				// ntotalinvoicenumber :
				BigDecimal ntotalinvoicenumber = (BigDecimal) rs.getObject(113);
				saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));
				// ntotalinvoicemny :
				BigDecimal ntotalinvoicemny = (BigDecimal) rs.getObject(114);
				saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));
				// ntotalinventorynumber :
				BigDecimal ntotalinventorynumber = (BigDecimal) rs.getObject(115);
				saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(
						ntotalinventorynumber));
				// ntotalbalancenumber :
				BigDecimal ntotalbalancenumber = (BigDecimal) rs.getObject(116);
				saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));
				// ntotalsignnumber :
				BigDecimal ntotalsignnumber = (BigDecimal) rs.getObject(117);
				saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));
				// ntotalcostmny :
				BigDecimal ntotalcostmny = (BigDecimal) rs.getObject(118);
				saleItem.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));
				// bifinvoicefinish :
				String bifinvoicefinish = rs.getString(119);
				saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));
				// bifreceivefinish :
				String bifreceivefinish = rs.getString(120);
				saleItem.setBifreceivefinish(bifreceivefinish == null ? null : new UFBoolean(bifreceivefinish.trim()));

				// bifinventoryfinish :
				String bifinventoryfinish = rs.getString(121);
				saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish
						.trim()));
				// bifpayfinish :
				String bifpayfinish = rs.getString(122);
				saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

				// pk_defdoc15 :
				String pk_defdoc15 = rs.getString(123);
				saleHeader.setPk_defdoc15(pk_defdoc15 == null ? null : pk_defdoc15.trim());

				// pk_defdoc10 :
				String pk_defdoc10 = rs.getString(124);
				saleHeader.setPk_defdoc10(pk_defdoc10 == null ? null : pk_defdoc10.trim());

				// pk_defdoc16 :
				String pk_defdoc16 = rs.getString(125);
				saleHeader.setPk_defdoc16(pk_defdoc16 == null ? null : pk_defdoc16.trim());

				// pk_defdoc17 :
				String pk_defdoc17 = rs.getString(126);
				saleHeader.setPk_defdoc17(pk_defdoc17 == null ? null : pk_defdoc17.trim());

				// pk_defdoc18 :
				String pk_defdoc18 = rs.getString(127);
				saleHeader.setPk_defdoc18(pk_defdoc18 == null ? null : pk_defdoc18.trim());

				// pk_defdoc19 :
				String pk_defdoc19 = rs.getString(128);
				saleHeader.setPk_defdoc19(pk_defdoc19 == null ? null : pk_defdoc19.trim());

				// pk_defdoc20 :
				String pk_defdoc20 = rs.getString(129);
				saleHeader.setPk_defdoc20(pk_defdoc20 == null ? null : pk_defdoc20.trim());

				// exets
				String exets = rs.getString(130);
				saleItem.setExets(exets == null ? null : new UFDateTime(exets.trim()));

				String timenew = rs.getString(131);
				saleHeader.setTimenew(timenew == null ? null : new UFDateTime(timenew.trim()));
				saleHeader.setDbilltime(timenew == null ? null : new UFDateTime(timenew.trim()));

				String timeaudit = rs.getString(132);
				saleHeader.setTimeaudit(timeaudit == null ? null : new UFDateTime(timeaudit.trim()));
				saleHeader.setDaudittime(timeaudit == null ? null : new UFDateTime(timeaudit.trim()));

				// cprojectid :
				String cprojectid = rs.getString(133);
				saleItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());
				// cprojectphaseid :
				String cprojectphaseid = rs.getString(134);
				saleItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());
				// cprojectid3 :
				String cprojectid3 = rs.getString(135);
				saleItem.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());
				// vfree1 :
				String vfree1 = rs.getString(136);
				saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());
				// vfree2 :
				String vfree2 = rs.getString(137);
				saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());
				// vfree3 :
				String vfree3 = rs.getString(138);
				saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());
				// vfree4 :
				String vfree4 = rs.getString(139);
				saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());
				// vfree5 :
				String vfree5 = rs.getString(140);
				saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());
				// vdef1 :
				vdef1 = rs.getString(141);
				saleItem.setVdef1(vdef1 == null ? null : vdef1.trim());
				// vdef2 :
				vdef2 = rs.getString(142);
				saleItem.setVdef2(vdef2 == null ? null : vdef2.trim());
				// vdef3 :
				vdef3 = rs.getString(143);
				saleItem.setVdef3(vdef3 == null ? null : vdef3.trim());
				// vdef4 :
				vdef4 = rs.getString(144);
				saleItem.setVdef4(vdef4 == null ? null : vdef4.trim());
				// vdef5 :
				vdef5 = rs.getString(145);
				saleItem.setVdef5(vdef5 == null ? null : vdef5.trim());
				// vdef6 :
				vdef6 = rs.getString(146);
				saleItem.setVdef6(vdef6 == null ? null : vdef6.trim());

				// iprintcount
				Integer iprintcount = (Integer) rs.getObject(147);
				saleHeader.setIprintcount(iprintcount == null ? null : iprintcount);

				// pk_defdoc14 :
				String pk_defdoc14 = rs.getString(148);
				saleHeader.setPk_defdoc14(pk_defdoc14 == null ? null : pk_defdoc14.trim());

				// vdef11 :
				String vdef11 = rs.getString(149);
				saleHeader.setVdef11(vdef11 == null ? null : vdef11.trim());

				// vdef12 :
				String vdef12 = rs.getString(150);
				saleHeader.setVdef12(vdef12 == null ? null : vdef12.trim());

				// vdef13 :
				String vdef13 = rs.getString(151);
				saleHeader.setVdef13(vdef13 == null ? null : vdef13.trim());

				// vdef14 :
				String vdef14 = rs.getString(152);
				saleHeader.setVdef14(vdef14 == null ? null : vdef14.trim());

				// vdef15 :
				String vdef15 = rs.getString(153);
				saleHeader.setVdef15(vdef15 == null ? null : vdef15.trim());

				// vdef16 :
				String vdef16 = rs.getString(154);
				saleHeader.setVdef16(vdef16 == null ? null : vdef16.trim());

				// vdef17 :
				String vdef17 = rs.getString(155);
				saleHeader.setVdef17(vdef17 == null ? null : vdef17.trim());

				// vdef18 :
				String vdef18 = rs.getString(156);
				saleHeader.setVdef18(vdef18 == null ? null : vdef18.trim());

				// vdef19 :
				String vdef19 = rs.getString(157);
				saleHeader.setVdef19(vdef19 == null ? null : vdef19.trim());

				// vdef20 :
				String vdef20 = rs.getString(158);
				saleHeader.setVdef20(vdef20 == null ? null : vdef20.trim());

				// pk_defdoc1 :
				String pk_defdoc1 = rs.getString(159);
				saleHeader.setPk_defdoc1(pk_defdoc1 == null ? null : pk_defdoc1.trim());

				// pk_defdoc2 :
				String pk_defdoc2 = rs.getString(160);
				saleHeader.setPk_defdoc2(pk_defdoc2 == null ? null : pk_defdoc2.trim());

				// pk_defdoc3 :
				String pk_defdoc3 = rs.getString(161);
				saleHeader.setPk_defdoc3(pk_defdoc3 == null ? null : pk_defdoc3.trim());

				// pk_defdoc4 :
				String pk_defdoc4 = rs.getString(162);
				saleHeader.setPk_defdoc4(pk_defdoc4 == null ? null : pk_defdoc4.trim());

				// pk_defdoc5 :
				String pk_defdoc5 = rs.getString(163);
				saleHeader.setPk_defdoc5(pk_defdoc5 == null ? null : pk_defdoc5.trim());

				// pk_defdoc6 :
				String pk_defdoc6 = rs.getString(164);
				saleHeader.setPk_defdoc6(pk_defdoc6 == null ? null : pk_defdoc6.trim());

				// pk_defdoc7 :
				String pk_defdoc7 = rs.getString(165);
				saleHeader.setPk_defdoc7(pk_defdoc7 == null ? null : pk_defdoc7.trim());

				// pk_defdoc8 :
				String pk_defdoc8 = rs.getString(166);
				saleHeader.setPk_defdoc8(pk_defdoc8 == null ? null : pk_defdoc8.trim());

				// pk_defdoc9 :
				String pk_defdoc9 = rs.getString(167);
				saleHeader.setPk_defdoc9(pk_defdoc9 == null ? null : pk_defdoc9.trim());

				// 获取v30后加入的其他字段值
				if (addfields != null) {
					for (int i = 0, loop = addfields.length; i < loop; i++) {
						getValueFromResultSet(rs, 168 + i, addfields[i], saleItem);
					}
				}

				lordbvolist = (ArrayList) hordbvo.get(saleItem.getCsaleid());
				if (lordbvolist == null) {
					lordbvolist = new ArrayList();
					hordbvo.put(saleItem.getCsaleid(), lordbvolist);
				}
				lordbvolist.add(saleItem);

			}
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		} finally {

			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		/*
		 * saleItems = new SaleorderBVO[v.size()]; if (v.size() > 0) {
		 * v.copyInto(saleItems); } queryFollowBody(saleItems);
		 * initFreeItem(saleItems);
		 */

		if (hordhvo.size() <= 0)
			return null;

		SaleOrderVO[] ordvos = new SaleOrderVO[hordhvo.size()];
		Map.Entry entry = null;
		Iterator iter = hordhvo.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			ordvos[i] = new SaleOrderVO();
			entry = (Map.Entry) iter.next();
			ordvos[i].setParentVO((SaleorderHVO) entry.getValue());
			lordbvolist = (ArrayList) hordbvo.get(entry.getKey());
			if (lordbvolist != null && lordbvolist.size() > 0)
				ordvos[i].setChildrenVO((SaleorderBVO[]) lordbvolist.toArray(new SaleorderBVO[lordbvolist.size()]));
			else
				ordvos[i].setChildrenVO(null);
			i++;
		}

		return (nc.vo.pub.AggregatedValueObject[]) ordvos;
	}

	/**
	 * 查询子子表数据。 创建日期：(2001-4-23 9:38:18)
	 * 
	 * @param saleexecute
	 *            SaleorderBVO
	 */
	public void queryFollowBody(SaleorderBVO[] bodys) throws Exception {
		if (bodys == null || bodys.length <= 0)
			return;

		// 设置其他增加的字段
		SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
				}
			}
		}
		String sql = "select csale_bid, creceipttype, ntotalpaymny, ntotalreceivenumber, ntotalinvoicenumber, "
				+ "ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, "
				+ "bifinvoicefinish, bifreceivefinish, bifinventoryfinish, bifpayfinish, "
				+ "ntotalreturnnumber, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, "
				+ "vfree4, vfree5, vdef1, vdef2, vdef3, " + "vdef4, vdef5, vdef6  " + addfieldsql.toString()
				+ " from so_saleexecute " + " where creceipttype = '30' AND csaleid = ? order by csale_bid";
		Connection con = null;
		PreparedStatement stmt = null;
		con = getConnection();
		stmt = con.prepareStatement(sql);
		try {
			SaleorderBVO saleexecute = bodys[0];
			// String key = saleexecute.getPrimaryKey();
			String key = saleexecute.getCsaleid();
			stmt.setString(1, key);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				if (i < bodys.length) {
					saleexecute = bodys[i];
					// csale_bid :
					String csale_bid = rs.getString(1);
					if (csale_bid.equals(saleexecute.getPrimaryKey())) {

						// ntotalpaymny :
						BigDecimal ntotalpaymny = (BigDecimal) rs.getObject(3);
						saleexecute.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));
						// ntotalreceivenumber :
						BigDecimal ntotalreceivenumber = (BigDecimal) rs.getObject(4);
						saleexecute.setNtotalreceivenumber(ntotalreceivenumber == null ? null : new UFDouble(
								ntotalreceivenumber));
						// ntotalinvoicenumber :
						BigDecimal ntotalinvoicenumber = (BigDecimal) rs.getObject(5);
						saleexecute.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(
								ntotalinvoicenumber));
						// ntotalinvoicemny :
						BigDecimal ntotalinvoicemny = (BigDecimal) rs.getObject(6);
						saleexecute.setNtotalinvoicemny(ntotalinvoicemny == null ? null
								: new UFDouble(ntotalinvoicemny));
						// ntotalinventorynumber :
						BigDecimal ntotalinventorynumber = (BigDecimal) rs.getObject(7);
						saleexecute.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(
								ntotalinventorynumber));
						// ntotalbalancenumber :
						BigDecimal ntotalbalancenumber = (BigDecimal) rs.getObject(8);
						saleexecute.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(
								ntotalbalancenumber));
						// ntotalsignnumber :
						BigDecimal ntotalsignnumber = (BigDecimal) rs.getObject(9);
						saleexecute.setNtotalsignnumber(ntotalsignnumber == null ? null
								: new UFDouble(ntotalsignnumber));
						// ntotalcostmny :
						BigDecimal ntotalcostmny = (BigDecimal) rs.getObject(10);
						saleexecute.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));
						// bifinvoicefinish :
						String bifinvoicefinish = rs.getString(11);
						saleexecute.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(
								bifinvoicefinish.trim()));
						// bifreceivefinish :
						String bifreceivefinish = rs.getString(12);
						saleexecute.setBifreceivefinish(bifreceivefinish == null ? null : new UFBoolean(
								bifreceivefinish.trim()));

						// bifinventoryfinish :
						String bifinventoryfinish = rs.getString(13);
						saleexecute.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(
								bifinventoryfinish.trim()));
						// bifpayfinish :
						String bifpayfinish = rs.getString(14);
						saleexecute.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

						// ntotalreturnnumber
						BigDecimal ntotalreturnnumber = (BigDecimal) rs.getObject(15);
						saleexecute.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(
								ntotalreturnnumber));

						// cprojectid :
						String cprojectid = rs.getString(16);
						saleexecute.setCprojectid(cprojectid == null ? null : cprojectid.trim());
						// cprojectphaseid :
						String cprojectphaseid = rs.getString(17);
						saleexecute.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());
						// cprojectid3 :
						String cprojectid3 = rs.getString(18);
						saleexecute.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());
						// vfree1 :
						String vfree1 = rs.getString(19);
						saleexecute.setVfree1(vfree1 == null ? null : vfree1.trim());
						// vfree2 :
						String vfree2 = rs.getString(20);
						saleexecute.setVfree2(vfree2 == null ? null : vfree2.trim());
						// vfree3 :
						String vfree3 = rs.getString(21);
						saleexecute.setVfree3(vfree3 == null ? null : vfree3.trim());
						// vfree4 :
						String vfree4 = rs.getString(22);
						saleexecute.setVfree4(vfree4 == null ? null : vfree4.trim());
						// vfree5 :
						String vfree5 = rs.getString(23);
						saleexecute.setVfree5(vfree5 == null ? null : vfree5.trim());
						// vdef1 :
						String vdef1 = rs.getString(24);
						saleexecute.setVdef1(vdef1 == null ? null : vdef1.trim());
						// vdef2 :
						String vdef2 = rs.getString(25);
						saleexecute.setVdef2(vdef2 == null ? null : vdef2.trim());
						// vdef3 :
						String vdef3 = rs.getString(26);
						saleexecute.setVdef3(vdef3 == null ? null : vdef3.trim());
						// vdef4 :
						String vdef4 = rs.getString(27);
						saleexecute.setVdef4(vdef4 == null ? null : vdef4.trim());
						// vdef5 :
						String vdef5 = rs.getString(28);
						saleexecute.setVdef5(vdef5 == null ? null : vdef5.trim());
						// vdef6 :
						String vdef6 = rs.getString(29);
						saleexecute.setVdef6(vdef6 == null ? null : vdef6.trim());

						// 获取v30后加入的其他字段值
						getOrdExeValueFromResultSet(rs, 30, saleexecute);

						i = i + 1;
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 查询子子表数据。 创建日期：(2001-4-23 9:38:18)
	 * 
	 * @param saleexecute
	 *            SaleorderBVO
	 */
	public void queryFollowBody(SaleorderBVO saleexecute) throws Exception {

		if (saleexecute == null)
			return;

		// 设置其他增加的字段
		SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
				}
			}
		}
		String sql = "select csaleid, creceipttype, ntotalpaymny, ntotalreceivenumber, ntotalinvoicenumber, "
				+ "ntotalinvoicemny, ntotalinventorynumber, ntotalbalancenumber, ntotalsignnumber, ntotalcostmny, "
				+ "bifinvoicefinish, bifreceivefinish, bifinventoryfinish, bifpayfinish, "
				+ "ntotalreturnnumber, cprojectid, cprojectphaseid, cprojectid3, vfree1, vfree2, vfree3, "
				+ "vfree4, vfree5, vdef1, vdef2, vdef3, " + "vdef4, vdef5, vdef6  " + addfieldsql.toString()
				+ " from so_saleexecute " + " where creceipttype = '30' AND csale_bid = ?";

		String key = saleexecute.getPrimaryKey();

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.setString(1, key);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				// csaleid :
				String csaleid = rs.getString(1);
				saleexecute.setCsaleid(csaleid == null ? null : csaleid.trim());

				// ntotalpaymny :
				BigDecimal ntotalpaymny = (BigDecimal) rs.getObject(3);
				saleexecute.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));
				// ntotalreceivenumber :
				BigDecimal ntotalreceivenumber = (BigDecimal) rs.getObject(4);
				saleexecute.setNtotalreceivenumber(ntotalreceivenumber == null ? null : new UFDouble(
						ntotalreceivenumber));
				// ntotalinvoicenumber :
				BigDecimal ntotalinvoicenumber = (BigDecimal) rs.getObject(5);
				saleexecute.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(
						ntotalinvoicenumber));
				// ntotalinvoicemny :
				BigDecimal ntotalinvoicemny = (BigDecimal) rs.getObject(6);
				saleexecute.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));
				// ntotalinventorynumber :
				BigDecimal ntotalinventorynumber = (BigDecimal) rs.getObject(7);
				saleexecute.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(
						ntotalinventorynumber));
				// ntotalbalancenumber :
				BigDecimal ntotalbalancenumber = (BigDecimal) rs.getObject(8);
				saleexecute.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(
						ntotalbalancenumber));
				// ntotalsignnumber :
				BigDecimal ntotalsignnumber = (BigDecimal) rs.getObject(9);
				saleexecute.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));
				// ntotalcostmny :
				BigDecimal ntotalcostmny = (BigDecimal) rs.getObject(10);
				saleexecute.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));
				// bifinvoicefinish :
				String bifinvoicefinish = rs.getString(11);
				saleexecute.setBifinvoicefinish(bifinvoicefinish == null ? null
						: new UFBoolean(bifinvoicefinish.trim()));
				// bifreceivefinish :
				String bifreceivefinish = rs.getString(12);
				saleexecute.setBifreceivefinish(bifreceivefinish == null ? null
						: new UFBoolean(bifreceivefinish.trim()));

				// bifinventoryfinish :
				String bifinventoryfinish = rs.getString(13);
				saleexecute.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish
						.trim()));
				// bifpayfinish :
				String bifpayfinish = rs.getString(14);
				saleexecute.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

				// ntotalreturnnumber
				BigDecimal ntotalreturnnumber = (BigDecimal) rs.getObject(15);
				saleexecute.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

				// cprojectid :
				String cprojectid = rs.getString(16);
				saleexecute.setCprojectid(cprojectid == null ? null : cprojectid.trim());
				// cprojectphaseid :
				String cprojectphaseid = rs.getString(17);
				saleexecute.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());
				// cprojectid3 :
				String cprojectid3 = rs.getString(18);
				saleexecute.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());
				// vfree1 :
				String vfree1 = rs.getString(19);
				saleexecute.setVfree1(vfree1 == null ? null : vfree1.trim());
				// vfree2 :
				String vfree2 = rs.getString(20);
				saleexecute.setVfree2(vfree2 == null ? null : vfree2.trim());
				// vfree3 :
				String vfree3 = rs.getString(21);
				saleexecute.setVfree3(vfree3 == null ? null : vfree3.trim());
				// vfree4 :
				String vfree4 = rs.getString(22);
				saleexecute.setVfree4(vfree4 == null ? null : vfree4.trim());
				// vfree5 :
				String vfree5 = rs.getString(23);
				saleexecute.setVfree5(vfree5 == null ? null : vfree5.trim());
				// vdef1 :
				String vdef1 = rs.getString(24);
				saleexecute.setVdef1(vdef1 == null ? null : vdef1.trim());
				// vdef2 :
				String vdef2 = rs.getString(25);
				saleexecute.setVdef2(vdef2 == null ? null : vdef2.trim());
				// vdef3 :
				String vdef3 = rs.getString(26);
				saleexecute.setVdef3(vdef3 == null ? null : vdef3.trim());
				// vdef4 :
				String vdef4 = rs.getString(27);
				saleexecute.setVdef4(vdef4 == null ? null : vdef4.trim());
				// vdef5 :
				String vdef5 = rs.getString(28);
				saleexecute.setVdef5(vdef5 == null ? null : vdef5.trim());
				// vdef6 :
				String vdef6 = rs.getString(29);
				saleexecute.setVdef6(vdef6 == null ? null : vdef6.trim());

				// 获取v30后加入的其他字段值
				getOrdExeValueFromResultSet(rs, 30, saleexecute);

			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 查询。 创建日期：(2001-4-23 9:38:18)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * @param strCondition
	 *            java.lang.String
	 */
	public SaleorderBVO[] queryOrderEnd(String strWhere) throws SQLException {

		/** 开票数量的未完量 = 订单数量 – 累计开票数量 – 累计出库对冲数量。(V502)* */

		String strSql = "select distinct so_saleorder_b.corder_bid, fstatus, so_saleorder_b.csaleid, so_saleorder_b.cinvbasdocid"
				+ ", nnumber, so_saleorder_b.ntaxnetprice, so_saleorder_b.nsummny"
				// 发货未完量 = 订单数量 - 累计发货数量
				+ ", isnull(nnumber,0)-isnull(ntotalreceivenumber,0) as ntotalreceivenumber,bifreceivefinish"
				// 出库未完量 = 订单数量 - 累计出库数量 + 累计途损数量
				+ ", isnull(nnumber,0)-isnull(ntotalinventorynumber,0)+isnull(ntranslossnum,0) as ntotalinventorynumber,bifinventoryfinish"
				// 开票未完量 = 订单数量 - 累计开票数量
				+ ", isnull(nnumber,0)-isnull(ntotalinvoicenumber,0)-isnull(nrushnum,0) as ntotalinvoicenumber,bifinvoicefinish"
				// 收款金额
				+ ", (isnull(nnumber,0)-isnull(ntotalinvoicenumber,0)-isnull(nrushnum,0))*isnull(so_saleorder_b.ntaxnetprice,0) + isnull(ntotalinvoicemny,0)-isnull(ntotalpaymny,0) as ntotalpaymny"
				+ ", bifpayfinish, frowstatus, so_sale.ts, so_saleorder_b.ts, so_saleexecute.ts, so_saleorder_b.ccurrencytypeid"
				// add by fengjb V55 新增结算关闭
				// 需要查询出 业务类型、累计出库数量、累计传确认应收数量、累计出库对冲数量、累计开票数量、累计传存货核算数量、是否结算结束
				+ ", so_sale.cfreecustid, so_sale.ccustomerid,so_sale.vreceiptcode,"

				+ "so_sale.cbiztype, ntotalinventorynumber as ntotaloutnumber,isnull(ntotalbalancenumber,0) as ntotalbalancenumber,isnull(nrushnum,0) as nrushnum,ntotalinvoicenumber as ntotalinvoicenum, isnull(ntotlbalcostnum,0) as ntotlbalcostnum,so_saleexecute.bsquareendflag  "
				+ "FROM so_sale INNER JOIN so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid "
				+ "INNER JOIN so_saleexecute ON (so_saleorder_b.corder_bid = so_saleexecute.csale_bid and so_saleorder_b.csaleid = so_saleexecute.csaleid )"
				// // 寻找对冲记录
				// // 只关心出库数量>=0的行，对冲行在被对冲的行上都有记录
				// + "inner join so_square_b on (((so_square_b.nrushtimes > 0)
				// and (so_square_b.noutnum >= 0) or (so_square_b.nrushtimes =
				// 0)) "
				// + "and ((so_square_b.creceipttype = '30' and
				// so_square_b.csourcebillbodyid =
				// so_saleorder_b.corder_bid)or(so_square_b.cupreceipttype =
				// '30' and so_square_b.cupbillbodyid =
				// so_saleorder_b.corder_bid)))"
				+ " WHERE so_sale.dr=0 and so_sale.fstatus in (2,4,6) ";

		if (strWhere != null && !strWhere.equals(""))
			strSql = strSql + " and " + strWhere;

		strSql = strSql + "order by so_saleorder_b.csaleid,so_saleorder_b.corder_bid";

		SaleorderBVO[] saleItems = null;
		Vector v = new Vector();

		Connection con = null;
		PreparedStatement stmt = null;

		con = getConnection();
		try {
			stmt = con.prepareStatement(strSql);
			ResultSet rs = stmt.executeQuery();

			//
			while (rs.next()) {
				SaleorderBVO saleItem = new SaleorderBVO();
				//
				String corder_bid = rs.getString("corder_bid");
				saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());
				//
				Object fstatus = rs.getObject("fstatus");
				saleItem
						.setFinished(new UFBoolean(Integer.valueOf(fstatus.toString()).intValue() == BillStatus.FINISH));
				//

				String csaleid = rs.getString("csaleid");
				saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());
				//
				String cinvbasdocid = rs.getString("cinvbasdocid");
				saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());
				//
				Object o = rs.getObject("nnumber");
				if (o != null) {
					BigDecimal nnumber = new BigDecimal(o.toString());
					saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));
				}
				//
				o = rs.getObject("ntaxnetprice");
				if (o != null) {
					BigDecimal ntaxnetprice = new BigDecimal(o.toString());
					saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));
				}
				//
				o = rs.getObject("nsummny");
				if (o != null) {
					BigDecimal nsummny = new BigDecimal(o.toString());
					saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));
				}
				//
				o = rs.getObject("ntotalreceivenumber");
				if (o != null) {
					BigDecimal ntotalreceivenumber = new BigDecimal(o.toString());
					saleItem.setNtotalreceivenumber(ntotalreceivenumber == null ? null : new UFDouble(
							ntotalreceivenumber));
				}
				//
				String bifreceivefinish = rs.getString("bifreceivefinish");
				saleItem.setBifreceivefinish(bifreceivefinish == null ? null : new UFBoolean(bifreceivefinish.trim()));
				//
				o = rs.getObject("ntotalinventorynumber");
				if (o != null) {
					BigDecimal ntotalinventorynumber = new BigDecimal(o.toString());
					saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(
							ntotalinventorynumber));
				}
				//
				String bifinventoryfinish = rs.getString("bifinventoryfinish");
				saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish
						.trim()));
				//
				o = rs.getObject("ntotalinvoicenumber");
				if (o != null) {
					BigDecimal ntotalinvoicenumber = new BigDecimal(o.toString());
					saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(
							ntotalinvoicenumber));
				}
				//
				String bifinvoicefinish = rs.getString("bifinvoicefinish");
				saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));
				//
				o = rs.getObject("ntotalpaymny");
				if (o != null) {
					BigDecimal ntotalpaymny = new BigDecimal(o.toString());
					saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));
				}
				//
				String bifpayfinish = rs.getString("bifpayfinish");
				saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

				// String bifpayfinish = rs.getInt("frowstatus");
				saleItem.setFrowstatus(new Integer(rs.getInt("frowstatus")));

				// hts
				String ts = rs.getString(17);
				saleItem.m_headts = ts == null ? null : new UFDateTime(ts.trim());

				// ts
				ts = rs.getString(18);
				saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));

				// exets
				ts = rs.getString(19);
				saleItem.setExets(ts == null ? null : new UFDateTime(ts.trim()));

				// ccurrencytypeid
				String ccurrencytypeid = rs.getString(20);
				saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());

				// cfreecustid
				String cfreecustid = rs.getString(21);
				saleItem.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());

				// ccustomerid
				String ccustomerid = rs.getString(22);
				saleItem.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());

				// saleHeader.setTs(ts == null ? null : new
				// UFDateTime(ts.trim()));
				// //公司
				// String pk_corp = rs.getString(23);
				// saleItem.setPk_corp(pk_corp);
				//订单号
				String vreceiptcode = rs.getString(23);
				saleItem.setAttributeValue("OrderName", vreceiptcode==null?null:vreceiptcode.trim());
				// 业务类型
				String cbiztype = rs.getString(24);
				saleItem.setCbiztype(cbiztype == null ? null : cbiztype.trim());
				// 累计出库数量
				o = rs.getObject("ntotaloutnumber");
				if (o != null) {
					BigDecimal ntotaloutnumber = new BigDecimal(o.toString());
					saleItem.setNtotaloutnumber(ntotaloutnumber == null ? null : new UFDouble(ntotaloutnumber));
				}
				// 累计传确认应收数量
				o = rs.getObject("ntotalbalancenumber");
				if (o != null) {
					BigDecimal ntotalbalancenumber = new BigDecimal(o.toString());
					saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(
							ntotalbalancenumber));
				}
				// 累计出库对冲数量
				o = rs.getObject("nrushnum");
				if (o != null) {
					BigDecimal nrushnum = new BigDecimal(o.toString());
					saleItem.setNrushnum(nrushnum == null ? null : new UFDouble(nrushnum));
				}
				// 累计开票数量
				o = rs.getObject("ntotalinvoicenum");
				if (o != null) {
					BigDecimal ntotalinvoicenum = new BigDecimal(o.toString());
					saleItem.setNtotalinvoicenum(ntotalinvoicenum == null ? null : new UFDouble(ntotalinvoicenum));
				}
				// 累计传存货核算数量
				o = rs.getObject("ntotlbalcostnum");
				if (o != null) {
					BigDecimal ntotlbalcostnum = new BigDecimal(o.toString());
					saleItem.setNtotlbalcostnum(ntotlbalcostnum == null ? null : new UFDouble(ntotlbalcostnum));
				}
				// 是否结算关闭
				String bsquareendflag = rs.getString("bsquareendflag");
				saleItem.setBsquareendflag(bsquareendflag == null ? UFBoolean.FALSE : new UFBoolean(bsquareendflag
						.trim()));

				v.addElement(saleItem);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		saleItems = new SaleorderBVO[v.size()];
		if (v.size() > 0) {
			v.copyInto(saleItems);
		}

		return saleItems;
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public String[] queryOrderIDByContractID(String strContractID) throws SQLException {
		String[] strIDs = null;
		String strSql = "select distinct csaleid from so_saleorder_b where "
				+ "(creceipttype = ?  or creceipttype = ?) and csourcebillid = ?  " + " and frowstatus <> "
				+ nc.ui.pub.bill.BillStatus.BLANKOUT + " order by csaleid ";

		Vector v = new Vector();

		Connection con = null;
		PreparedStatement stmt = null;

		con = getConnection();
		stmt = con.prepareStatement(strSql);

		stmt.setString(1, SaleBillType.SoContract);
		stmt.setString(2, SaleBillType.SoInitContract);
		stmt.setString(3, strContractID);

		ResultSet rs = stmt.executeQuery();

		try {
			while (rs.next()) {
				// csaleid :
				String csaleid = rs.getString(1);
				v.addElement(csaleid);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		if (v.size() > 0) {
			strIDs = new String[v.size()];
			v.copyInto(strIDs);
		}
		return strIDs;
	}

	/**
	 * 方法说明：审批未通过单据VO修改后更改ATP时，移去单据行VO状态为删除的行
	 * 
	 */
	public SaleOrderVO removeDelLine(SaleOrderVO voSource) throws SQLException, Exception {
		Vector vecLines = new Vector();
		// 子表VO状态
		SaleorderBVO[] voBodys = (SaleorderBVO[]) voSource.getChildrenVO();
		nc.vo.scm.pub.SCMEnv.out("//////////////////////////////	voBodys.length:" + voBodys.length);
		nc.vo.scm.pub.SCMEnv.out("\n");
		for (int i = 0; i < voBodys.length; i++) {
			if (voBodys[i].getStatus() != VOStatus.DELETED)
				vecLines.addElement(voBodys[i]);
		}
		nc.vo.scm.pub.SCMEnv.out("//////////////////////////////	vecLines.size:" + vecLines.size());
		nc.vo.scm.pub.SCMEnv.out("\n");
		SaleorderBVO[] aryBodys = new SaleorderBVO[vecLines.size()];
		vecLines.copyInto(aryBodys);
		nc.vo.scm.pub.SCMEnv.out("//////////////////////////////	aryBodys.length:" + aryBodys.length);
		nc.vo.scm.pub.SCMEnv.out("\n");
		voSource.setChildrenVO(aryBodys);
		return voSource;
	}

	/**
	 * 订单结束管理。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public Vector splitSaleOrderVOs(SaleOrderVO[] aratpvos) {
		if (aratpvos == null || aratpvos.length <= 0)
			return null;
		Vector vlist1 = new Vector();
		Vector vlist2 = new Vector();
		Vector vlistaudit = new Vector();
		Vector vlistfinish = new Vector();
		Vector vlistre = new Vector();
		for (int i = 0; i < aratpvos.length; i++) {
			SaleorderBVO[] bvo = (SaleorderBVO[]) aratpvos[i].getChildrenVO();
			for (int j = 0; j < bvo.length; j++) {
				if (bvo[j].getFrowstatus().intValue() == BillStatus.AUDIT) {
					vlist1.add(bvo[j]);
				} else if (bvo[j].getFrowstatus().intValue() == BillStatus.FINISH) {
					vlist2.add(bvo[j]);
				}
			}
			if (vlist1.size() > 0) {
				SaleOrderVO vo = new SaleOrderVO();
				vo.setParentVO(aratpvos[i].getParentVO());
				vo.setChildrenVO((SaleorderBVO[]) vlist1.toArray(new SaleorderBVO[0]));
				vlistaudit.add(vo);
			}
			if (vlist2.size() > 0) {
				SaleOrderVO vo = new SaleOrderVO();
				vo.setParentVO(aratpvos[i].getParentVO());
				vo.setChildrenVO((SaleorderBVO[]) vlist2.toArray(new SaleorderBVO[0]));
				vlistfinish.add(vo);
			}
			vlist1.clear();
			vlist2.clear();
		}
		vlistre.add(vlistaudit);
		vlistre.add(vlistfinish);
		return vlistre;
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public ArrayList update(SaleOrderVO saleorder) throws BusinessException{

		eventproxy.beforeAction(Action.SAVE, new AggregatedValueObject[]{saleorder}, null);
		
		// 检查多公司定单行
		// checkOtherCorpOrdRow(saleorder);

		// 检查自定义项
		IScm srv = (IScm) NCLocator.getInstance().lookup(IScm.class.getName());
		srv.checkDefDataType(saleorder);
		
		SaleorderHVO hvo = (SaleorderHVO) saleorder.getParentVO();
		ArrayList listID = null;

		// 如果是修改保存，检查客户是否修改
		if (saleorder.getIAction() == ISaleOrderAction.A_EDIT
				|| saleorder.getIAction() == ISaleOrderAction.A_SPECIALADD) {
			if (saleorder.getOldSaleOrderVO() != null
					&& !hvo.getCcustomerid().equals(saleorder.getOldSaleOrderVO().getHeadVO().getCcustomerid())) {
				UFDouble dcathpay = queryCachPayByOrdId(saleorder.getHeadVO().getCsaleid());
				if (dcathpay != null && dcathpay.doubleValue() != 0) {
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
							"UPP40060301-000011")/*
					 * @res
					 * "订单已收款不能修改客户!"
					 */
					);
				}
			}
		}

		try {
			// try{
			if (hvo.getVreceiptcode() == null || hvo.getVreceiptcode().trim().equals("")
					|| (hvo.getVoldreceiptcode() != null && !hvo.getVreceiptcode().equals(hvo.getVoldreceiptcode()))) {

				nc.impl.scm.so.pub.CheckValueValidityImpl dmocode = new nc.impl.scm.so.pub.CheckValueValidityImpl();
				// 获得单据号
				String vreceiptcode = dmocode.getSysBillNO(saleorder);
				saleorder.getParentVO().setAttributeValue("vreceiptcode", vreceiptcode);
				dmocode.isValueValidity(saleorder);
			}
			// }catch(Exception ex){
			// saleorder.getParentVO().setAttributeValue("vreceiptcode", null);
			// if(ex.getClass()==nc.vo.pub.BusinessException.class)
			// throw (nc.vo.pub.BusinessException)ex;
			// else
			// throw new java.rmi.RemoteException("Remote Call",ex);
			// }

			try {
				// 检查VO
				onCheck(saleorder);
				//wanglei 2014-05-14 
				reCalcateInvFee(saleorder);
				
			} catch (Exception ex) {
				saleorder.getParentVO().setAttributeValue("vreceiptcode", null);
				if (ex.getClass() == nc.vo.pub.BusinessException.class)
					throw (nc.vo.pub.BusinessException) ex;
				else
					throw new BusinessException(ex.getMessage());
			}

			SaleorderHVO headVO = (SaleorderHVO) saleorder.getParentVO();
			SaleorderBVO[] bodyVO = (SaleorderBVO[]) saleorder.getChildrenVO();
			// 处理超账期标志
			if (saleorder.getErrMsg() != null
					&& saleorder.getErrMsg().indexOf(
							NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000005")/*
					 * @res
					 * "该物料未被上级物料选择!"
					 */) >= 0) {
				// "账期")>0){
				headVO.setBoverdate(new UFBoolean(true));
			}
			
			//1:1尾差处理--e.g 数量由3-->1-->3，价税合计出现尾差
			boolean ifReCal = false;
			if(!saleorder.isCoopped())
				ifReCal = processPriceBeforeSave(saleorder);
			
			try {
				// con = getConnection();
				// ((CrossDBConnection)con).setAddTimeStamp(false);
				ArrayList retlist = fillPkAndTs(saleorder);
				// v5.5销售定单修订备份历史记录 周长胜 2008-6-15
				if (saleorder.getIAction() == ISaleOrderAction.A_MODIFY) {
					String mainID = insertHistoryHead(headVO);
					insertHistoryBodys(headVO, mainID);
				}
				
				//更新表头
				updateHead(headVO);
				
				//更新表体
				listID = updateBodys(bodyVO, headVO.getPrimaryKey(), headVO.getCreceipttype());
				listID = retlist;
				
				
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e);
				throw new BusinessException(e.getMessage());
			}

			//存货是否匹配存货组织
			checkStoreStructure(saleorder, headVO.getCsaleid());

			// 存货是否匹配收货存货组织
			checkRecStoreStructure(saleorder, headVO.getCsaleid());

			//发货库存组织是否匹配发货公司
			checkCalbody(saleorder);
			
			// 如果是退货单，回写退货数量
			setRetNum(saleorder);

			// 更新so_bomorder表
			updateBomID(saleorder);
			
			//将尾差处理完的金额传回前台 v5.5
			if (ifReCal)
				listID.add(getReturnNewVO(saleorder));

			// 如果修订订单，修改出库单的价格及金额
			if (saleorder.getIAction() == ISaleOrderAction.A_MODIFY) {

				SaleorderBVO[] ordbvos = saleorder.getBodyVOs();

				if (ordbvos != null && ordbvos.length > 0) {

					Hashtable params = new Hashtable();
					// UFDouble[] dvalues = null;
					Object[] ovalues = null;

					Object[] ovaluesfor33 = null;
					ArrayList paramfor33 = new ArrayList();

					String corder_bid = null;
					String scurdate = saleorder.getDcurdate() == null ? saleorder.getHeadVO().getDmakedate().toString()
							: saleorder.getDcurdate().toString();

					ArrayList list = new ArrayList();
					for (int i = 0, loop = ordbvos.length; i < loop; i++) {

						corder_bid = ordbvos[i].getCorder_bid();

						if (corder_bid == null || corder_bid.trim().length() <= 0)
							continue;

						if (ordbvos[i].getStatus() == nc.vo.pub.bill.BillVOStatus.MODIFICATION
								|| ordbvos[i].getStatus() == VOStatus.UPDATED) {

							// 报价含税净价 nquoteprice
							// 报价单位无税净价 nquotentprice
							// 本币含税净单价 ntaxprice
							// 本币无税净单价 nsaleprice
							// 报价计量单位ID cquoteunitid
							// 报价计量单位换算率 nquoteunitrate
							//				            
							// 是否价保 bsafeprice
							// 是否返利 breturnprofit
							//				            

							// /** 雨润项目IC接口变更 2008-03-19 */
							GeneralBillItemVO itemvo = new GeneralBillItemVO();
							itemvo.setCsourcebillbid(corder_bid);// 订单或退货申请单表体id
							itemvo.setAttributeValue("cquotecurrency", ordbvos[i].getCcurrencytypeid());// 币种

							itemvo.setAttributeValue("nquoteprice", ordbvos[i].getNorgqttaxnetprc());// 报价含税净价
							itemvo.setNquotentprice(ordbvos[i].getNorgqtnetprc());// 报价无税净价
							itemvo.setNtaxprice(ordbvos[i].getNtaxnetprice());// 本币含税净价
							itemvo.setNsaleprice(ordbvos[i].getNnetprice());// 本币无税净价

							itemvo.setAttributeValue("cquoteunitid", ordbvos[i].getCquoteunitid());// 报价计量单位ID
							itemvo.setAttributeValue("nquoteunitrate", ordbvos[i].getNqtscalefactor());// 报价计量单位换算率
							itemvo.setBsafeprice(ordbvos[i].getBsafeprice());// 是否价保
							itemvo.setBreturnprofit(ordbvos[i].getBreturnprofit());// 是否返利
							list.add(itemvo);
							// /** 雨润项目IC接口变更 2008-03-19 */

							ovalues = new Object[8];

							// 0位置存放含税单价
							ovalues[0] = ordbvos[i].getNtaxnetprice();
							// 存放非含税单价nsaleprice
							ovalues[1] = ordbvos[i].getNnetprice();

							// 是否价保
							ovalues[2] = ordbvos[i].getBsafeprice();

							// 是否返利
							ovalues[3] = ordbvos[i].getBreturnprofit();
							// 4报价单位
							// 5报价换算率
							// 6币种
							// 7报价单位含税单价

							ovalues[4] = ordbvos[i].getCquoteunitid();
							ovalues[5] = ordbvos[i].getNqtscalefactor();
							ovalues[6] = ordbvos[i].getCcurrencytypeid();

							ovalues[7] = ordbvos[i].getNorgqttaxnetprc();

							// 调用方法，通过接口调用：
							// IICPub_GeneralBillDMO，编号：IC0011
							params.put(corder_bid, ovalues);
							// if((ordbvos[i].getNtotalbalancenumber()==null||ordbvos[i].getNtotalbalancenumber().doubleValue()==0)
							// &&(ordbvos[i].getNtranslossnum()==null||ordbvos[i].getNtranslossnum().doubleValue()==0)){
							ovaluesfor33 = new Object[20];

							// 数组依次存放以下内容:订单表体id,原币无税净价，原币含税净价，本币无税净价，本币含税净价，税率，折本汇率,折辅汇率，原币币种,订单修订日期

							// v31报价计量单位
							// 报价计量单位ID
							// String cquoteunitid;10

							// 报价计量单位换算率
							// UFDouble nquoteunitrate ;
							// 报价计量单位本币含税净价
							// UFDouble nqttaxnetprc;
							// 报价计量单位本币无税净价
							// UFDouble nqtnetprc;
							// 报价计量单位本币含税单价
							// UFDouble nqttaxprc;
							// 报价计量单位本币无税单价
							// UFDouble nqtprc;
							// 报价计量单位原币含税净价
							// private UFDouble norgqttaxnetprc;
							// 报价计量单位原币无税净价
							// private UFDouble norgqtnetprc;
							// 报价计量单位原币含税单价
							// private UFDouble norgqttaxprc;
							// 报价计量单位原币无税单价
							// private UFDouble norgqtprc;

							ovaluesfor33[0] = ordbvos[i].getCorder_bid();
							ovaluesfor33[1] = ordbvos[i].getNoriginalcurnetprice();
							ovaluesfor33[2] = ordbvos[i].getNoriginalcurtaxnetprice();
							ovaluesfor33[3] = ordbvos[i].getNnetprice();
							ovaluesfor33[4] = ordbvos[i].getNtaxnetprice();
							ovaluesfor33[5] = ordbvos[i].getNtaxrate();
							ovaluesfor33[6] = ordbvos[i].getNexchangeotobrate();
							/** V5.5 删除辅币 */
							// ovaluesfor33[7] =
							// ordbvos[i].getNexchangeotoarate();
							ovaluesfor33[8] = ordbvos[i].getCcurrencytypeid();
							ovaluesfor33[9] = scurdate;
							ovaluesfor33[10] = ordbvos[i].getCquoteunitid();
							ovaluesfor33[11] = SoVoTools.div(ordbvos[i].getNquoteunitnum(), ordbvos[i].getNnumber());
							ovaluesfor33[12] = ordbvos[i].getNqttaxnetprc();
							ovaluesfor33[13] = ordbvos[i].getNqtnetprc();
							ovaluesfor33[14] = ordbvos[i].getNqttaxprc();
							ovaluesfor33[15] = ordbvos[i].getNqtprc();
							ovaluesfor33[16] = ordbvos[i].getNorgqttaxnetprc();
							ovaluesfor33[17] = ordbvos[i].getNorgqtnetprc();
							ovaluesfor33[18] = ordbvos[i].getNorgqttaxprc();
							ovaluesfor33[19] = ordbvos[i].getNorgqtprc();

							paramfor33.add(ovaluesfor33);
							// }

						}

					}

					if (params.size() > 0) {

						// 修订出库单
						try {
							IICPub_GeneralBillDMO gdmo = (IICPub_GeneralBillDMO) NCLocator.getInstance().lookup(
									IICPub_GeneralBillDMO.class.getName());
							// (IICPub_GeneralBillDMO)serbo.getInterInstance(ProductCode.PROD_IC,
							// InterRegister.IC0011);
							// /** 等到v55的时候，需要将此逻辑去掉 */
							// String retic = gdmo.changeSalePrices(params,
							// saleorder.getCuruserid(), scurdate,
							// saleorder.getHeadVO().getPk_corp(), "30");

							/** 雨润项目IC接口变更 2008-03-19 */
							/*
							 * String retic = gdmo.changeSalePrices_new(itemvos,
							 * saleorder.getCuruserid(), scurdate,
							 * saleorder.getHeadVO().getPk_corp(), "30");
							 */

							GeneralBillItemVO[] itemvos = new GeneralBillItemVO[list.size()];
							list.toArray(itemvos);
							String retic = gdmo.changeSalePrices_new(itemvos, saleorder.getCuruserid(), scurdate,
									saleorder.getHeadVO().getPk_corp(), "30");
							/** 雨润项目IC接口变更 2008-03-19 */

							if (retic != null) {
								/** 信用检查提示 */
								if (saleorder.getErrMsg() != null && saleorder.getErrMsg().trim().length() > 0) {
									saleorder.setErrMsg(saleorder.getErrMsg() + "\n" + retic);
								} else {
									saleorder.setErrMsg(retic);
								}
							}

						} catch (Exception e) {
							nc.vo.scm.pub.SCMEnv.out(e.getMessage());
							throw new BusinessRuntimeException(e.getMessage());

						}

						// 修订结算单
						try {
							nc.impl.scm.so.so012.SquareDMO sqdmo = new nc.impl.scm.so.so012.SquareDMO();
							sqdmo.orderChangeToSquare(paramfor33, saleorder.getHeadVO().getPk_corp(), saleorder
									.getCuruserid());

						} catch (BusinessException e) {
							throw e;
						} catch (Exception e) {
							nc.vo.scm.pub.SCMEnv.out(e.getMessage());
							throw new BusinessRuntimeException(e.getMessage());
						}
					}

				}

				// 回写调拨订单
				try {
					// DMVO othervo =
					// saleorder.getChgVOValueForUpdateOtherBill();
					BillConvertDMO bcdmo = new BillConvertDMO();
					bcdmo.writeBackArrangeInfoFor30(new SaleOrderVO[] { saleorder });
					// }
				} catch (BusinessException e) {
					throw e;
				} catch (Exception e) {
					nc.vo.scm.pub.SCMEnv.out(e.getMessage());
					throw new BusinessRuntimeException(e.getMessage());
				}

			}
			// 由于单据号处理是独立事务，所以只有在所有操作完成后，去回退原单据号。 20060307
			if (hvo.getVreceiptcode() != null && hvo.getVoldreceiptcode() != null
					&& !hvo.getVreceiptcode().equals(hvo.getVoldreceiptcode())) {
				nc.impl.scm.so.pub.CheckValueValidityImpl dmocode = new nc.impl.scm.so.pub.CheckValueValidityImpl();

				// 退单据号
				try {
					dmocode.returnBillNo(saleorder, "voldreceiptcode");
				} catch (Exception e) {
					nc.vo.scm.pub.SCMEnv.out(e.getMessage());
					// 回退不成功，抛异常，此时保存回退，新申请单据号将在异常处理中回退,20060307
					throw new BusinessException("回退单据号错误!");
				}
			}
			
			eventproxy.afterAction(Action.SAVE, new AggregatedValueObject[]{saleorder}, null);
			
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			try {
				// 如果是单据号已存在的错误，则不再回退，以便第二次可以继续申请成功 20060307
				if (!e.getMessage().equals(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000068")))// 单据号已存在
				{
					if (hvo.getVreceiptcode() != null && hvo.getVoldreceiptcode() != null
							&& !hvo.getVreceiptcode().equals(hvo.getVoldreceiptcode())) {
						nc.impl.scm.so.pub.CheckValueValidityImpl dmocode = new nc.impl.scm.so.pub.CheckValueValidityImpl();
						// 退新申请单据号
						dmocode.returnBillNo(saleorder, "vreceiptcode");
					}
				}

			} catch (Exception ex) {
				throw new BusinessException(ex.getMessage());
			}
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			throw new BusinessException(e.getMessage());

		}
		
		return listID;
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateBody(SaleorderBVO saleItem, String creceipttype, PreparedStatement stmt,
			PreparedStatement stmtfollow2) throws java.sql.SQLException, nc.bs.pub.SystemException {
		try {
			// update non PK fields:
			if (saleItem.getCsaleid() == null) {
				stmt.setNull(1, Types.CHAR);
			} else {
				stmt.setString(1, saleItem.getCsaleid());
			}
			if (saleItem.getPkcorp() == null) {
				stmt.setNull(2, Types.CHAR);
			} else {
				stmt.setString(2, saleItem.getPkcorp());
			}
			if (saleItem.getCreceipttype() == null) {
				stmt.setNull(3, Types.CHAR);
			} else {
				stmt.setString(3, saleItem.getCreceipttype());
			}
			if (saleItem.getCsourcebillid() == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, saleItem.getCsourcebillid());
			}
			if (saleItem.getCsourcebillbodyid() == null) {
				stmt.setNull(5, Types.CHAR);
			} else {
				stmt.setString(5, saleItem.getCsourcebillbodyid());
			}
			if (saleItem.getCinventoryid() == null) {
				stmt.setNull(6, Types.CHAR);
			} else {
				stmt.setString(6, saleItem.getCinventoryid());
			}
			if (saleItem.getCunitid() == null) {
				stmt.setNull(7, Types.CHAR);
			} else {
				stmt.setString(7, saleItem.getCunitid());
			}
			if (saleItem.getCpackunitid() == null) {
				stmt.setNull(8, Types.CHAR);
			} else {
				stmt.setString(8, saleItem.getCpackunitid());
			}
			if (saleItem.getNnumber() == null) {
				stmt.setNull(9, Types.INTEGER);
			} else {
				stmt.setBigDecimal(9, saleItem.getNnumber().toBigDecimal());
			}
			if (saleItem.getNpacknumber() == null) {
				stmt.setNull(10, Types.INTEGER);
			} else {
				stmt.setBigDecimal(10, saleItem.getNpacknumber().toBigDecimal());
			}
			if (saleItem.getCbodywarehouseid() == null) {
				stmt.setNull(11, Types.CHAR);
			} else {
				stmt.setString(11, saleItem.getCbodywarehouseid());
			}
			if (saleItem.getDconsigndate() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleItem.getDconsigndate().toString());
			}
			if (saleItem.getDdeliverdate() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleItem.getDdeliverdate().toString());
			}
			if (saleItem.getBlargessflag() == null) {
				// stmt.setNull(14, Types.CHAR);
				stmt.setString(14, "N");
			} else {
				stmt.setString(14, saleItem.getBlargessflag().toString());
			}

			// crowno
			if (saleItem.getCrowno() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, saleItem.getCrowno());
			}

			// ts
			if (saleItem.getTs() == null) {
				stmt.setNull(16, Types.CHAR);
			} else {
				stmt.setString(16, saleItem.getTs().toString());
			}

			if (saleItem.getVeditreason() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, saleItem.getVeditreason());
			}
			if (saleItem.getCcurrencytypeid() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, saleItem.getCcurrencytypeid());
			}
			if (saleItem.getNitemdiscountrate() == null) {
				stmt.setNull(19, Types.INTEGER);
			} else {
				stmt.setBigDecimal(19, saleItem.getNitemdiscountrate().toBigDecimal());
			}
			if (saleItem.getNdiscountrate() == null) {
				stmt.setNull(20, Types.INTEGER);
			} else {
				stmt.setBigDecimal(20, saleItem.getNdiscountrate().toBigDecimal());
			}
			if (saleItem.getNexchangeotobrate() == null) {
				stmt.setNull(21, Types.INTEGER);
			} else {
				stmt.setBigDecimal(21, saleItem.getNexchangeotobrate().toBigDecimal());
			}
			if (saleItem.getNtaxrate() == null) {
				stmt.setNull(22, Types.INTEGER);
			} else {
				stmt.setBigDecimal(22, saleItem.getNtaxrate().toBigDecimal());
			}
			if (saleItem.getNoriginalcurprice() == null) {
				stmt.setNull(23, Types.INTEGER);
			} else {
				stmt.setBigDecimal(23, saleItem.getNoriginalcurprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxprice() == null) {
				stmt.setNull(24, Types.INTEGER);
			} else {
				stmt.setBigDecimal(24, saleItem.getNoriginalcurtaxprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurnetprice() == null) {
				stmt.setNull(25, Types.INTEGER);
			} else {
				stmt.setBigDecimal(25, saleItem.getNoriginalcurnetprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxnetprice() == null) {
				stmt.setNull(26, Types.INTEGER);
			} else {
				stmt.setBigDecimal(26, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxmny() == null) {
				stmt.setNull(27, Types.INTEGER);
			} else {
				stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxmny().toBigDecimal());
			}
			if (saleItem.getNoriginalcurmny() == null) {
				stmt.setNull(28, Types.INTEGER);
			} else {
				stmt.setBigDecimal(28, saleItem.getNoriginalcurmny().toBigDecimal());
			}
			if (saleItem.getNoriginalcursummny() == null) {
				stmt.setNull(29, Types.INTEGER);
			} else {
				stmt.setBigDecimal(29, saleItem.getNoriginalcursummny().toBigDecimal());
			}
			if (saleItem.getNoriginalcurdiscountmny() == null) {
				stmt.setNull(30, Types.INTEGER);
			} else {
				stmt.setBigDecimal(30, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
			}
			if (saleItem.getNprice() == null) {
				stmt.setNull(31, Types.INTEGER);
			} else {
				stmt.setBigDecimal(31, saleItem.getNprice().toBigDecimal());
			}
			if (saleItem.getNtaxprice() == null) {
				stmt.setNull(32, Types.INTEGER);
			} else {
				stmt.setBigDecimal(32, saleItem.getNtaxprice().toBigDecimal());
			}
			if (saleItem.getNnetprice() == null) {
				stmt.setNull(33, Types.INTEGER);
			} else {
				stmt.setBigDecimal(33, saleItem.getNnetprice().toBigDecimal());
			}
			if (saleItem.getNtaxnetprice() == null) {
				stmt.setNull(34, Types.INTEGER);
			} else {
				stmt.setBigDecimal(34, saleItem.getNtaxnetprice().toBigDecimal());
			}
			if (saleItem.getNtaxmny() == null) {
				stmt.setNull(35, Types.INTEGER);
			} else {
				stmt.setBigDecimal(35, saleItem.getNtaxmny().toBigDecimal());
			}
			if (saleItem.getNmny() == null) {
				stmt.setNull(36, Types.INTEGER);
			} else {
				stmt.setBigDecimal(36, saleItem.getNmny().toBigDecimal());
			}
			if (saleItem.getNsummny() == null) {
				stmt.setNull(37, Types.INTEGER);
			} else {
				stmt.setBigDecimal(37, saleItem.getNsummny().toBigDecimal());
			}
			if (saleItem.getNdiscountmny() == null) {
				stmt.setNull(38, Types.INTEGER);
			} else {
				stmt.setBigDecimal(38, saleItem.getNdiscountmny().toBigDecimal());
			}
			if (saleItem.getCoperatorid() == null) {
				stmt.setNull(39, Types.CHAR);
			} else {
				stmt.setString(39, saleItem.getCoperatorid());
			}
			if (saleItem.getFrowstatus() == null) {
				stmt.setNull(40, Types.INTEGER);
			} else {
				stmt.setInt(40, saleItem.getFrowstatus().intValue());
			}
			if (saleItem.getFrownote() == null) {
				stmt.setNull(41, Types.CHAR);
			} else {
				stmt.setString(41, saleItem.getFrownote());
			}
			//
			if (saleItem.getCinvbasdocid() == null) {
				stmt.setNull(42, Types.CHAR);
			} else {
				stmt.setString(42, saleItem.getCinvbasdocid());
			}
			//
			if (saleItem.getCbatchid() == null) {
				stmt.setNull(43, Types.CHAR);
			} else {
				stmt.setString(43, saleItem.getCbatchid());
			}
			//
			if (saleItem.getFbatchstatus() == null) {
				stmt.setNull(44, Types.INTEGER);
			} else {
				stmt.setInt(44, saleItem.getFbatchstatus().intValue());
			}
			//
			if (saleItem.getCbomorderid() == null) {
				stmt.setNull(45, Types.CHAR);
			} else {
				stmt.setString(45, saleItem.getCbomorderid());
			}
			//
			if (saleItem.getCfreezeid() == null) {
				stmt.setNull(46, Types.CHAR);
			} else {
				stmt.setString(46, saleItem.getCfreezeid());
			}
			//
			if (saleItem.getCt_manageid() == null) {
				stmt.setNull(47, Types.CHAR);
			} else {
				stmt.setString(47, saleItem.getCt_manageid());
			}
			// cadvisecalbodyid
			if (saleItem.getCadvisecalbodyid() == null) {
				stmt.setNull(48, Types.CHAR);
			} else {
				stmt.setString(48, saleItem.getCadvisecalbodyid());
			}
			// boosflag
			if (saleItem.getBoosflag() == null) {
				stmt.setString(49, "N");
			} else {
				stmt.setString(49, saleItem.getBoosflag().toString());
			}
			// bsupplyflag
			if (saleItem.getBsupplyflag() == null) {
				stmt.setString(50, "N");
			} else {
				stmt.setString(50, saleItem.getBsupplyflag().toString());
			}
			// creceiptareaid
			if (saleItem.getCreceiptareaid() == null) {
				stmt.setNull(51, Types.CHAR);
			} else {
				stmt.setString(51, saleItem.getCreceiptareaid());
			}
			// vreceiveaddress
			if (saleItem.getVreceiveaddress() == null) {
				stmt.setNull(52, Types.CHAR);
			} else {
				stmt.setString(52, saleItem.getVreceiveaddress());
			}
			// creceiptcorpid
			if (saleItem.getCreceiptcorpid() == null) {
				stmt.setNull(53, Types.CHAR);
			} else {
				stmt.setString(53, saleItem.getCreceiptcorpid());
			}
			
			// 设置v30后加入的其他字段值
			int curpos = setPreStatementOrdB(stmt, 54, saleItem);

			// find record by PK fields:
			stmt.setString(curpos, saleItem.getPrimaryKey());
			//
			executeUpdate(stmt);
		} finally {

		}
		updateFollowBody(saleItem, creceipttype, stmtfollow2);
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateBody(SaleorderBVO saleItem, PreparedStatement stmt, PreparedStatement stmtfollow)
			throws java.sql.SQLException, nc.bs.pub.SystemException {
		try {
			// update non PK fields:
			if (saleItem.getCsaleid() == null) {
				stmt.setNull(1, Types.CHAR);
			} else {
				stmt.setString(1, saleItem.getCsaleid());
			}
			if (saleItem.getPkcorp() == null) {
				stmt.setNull(2, Types.CHAR);
			} else {
				stmt.setString(2, saleItem.getPkcorp());
			}
			if (saleItem.getCreceipttype() == null) {
				stmt.setNull(3, Types.CHAR);
			} else {
				stmt.setString(3, saleItem.getCreceipttype());
			}
			if (saleItem.getCsourcebillid() == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, saleItem.getCsourcebillid());
			}
			if (saleItem.getCsourcebillbodyid() == null) {
				stmt.setNull(5, Types.CHAR);
			} else {
				stmt.setString(5, saleItem.getCsourcebillbodyid());
			}
			if (saleItem.getCinventoryid() == null) {
				stmt.setNull(6, Types.CHAR);
			} else {
				stmt.setString(6, saleItem.getCinventoryid());
			}
			if (saleItem.getCunitid() == null) {
				stmt.setNull(7, Types.CHAR);
			} else {
				stmt.setString(7, saleItem.getCunitid());
			}
			if (saleItem.getCpackunitid() == null) {
				stmt.setNull(8, Types.CHAR);
			} else {
				stmt.setString(8, saleItem.getCpackunitid());
			}
			if (saleItem.getNnumber() == null) {
				stmt.setNull(9, Types.INTEGER);
			} else {
				stmt.setBigDecimal(9, saleItem.getNnumber().toBigDecimal());
			}
			if (saleItem.getNpacknumber() == null) {
				stmt.setNull(10, Types.INTEGER);
			} else {
				stmt.setBigDecimal(10, saleItem.getNpacknumber().toBigDecimal());
			}
			if (saleItem.getCbodywarehouseid() == null) {
				stmt.setNull(11, Types.CHAR);
			} else {
				stmt.setString(11, saleItem.getCbodywarehouseid());
			}
			if (saleItem.getDconsigndate() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleItem.getDconsigndate().toString());
			}
			if (saleItem.getDdeliverdate() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleItem.getDdeliverdate().toString());
			}
			if (saleItem.getBlargessflag() == null) {
				// stmt.setNull(14, Types.CHAR);
				stmt.setString(14, "N");
			} else {
				stmt.setString(14, saleItem.getBlargessflag().toString());
			}

			// crowno
			if (saleItem.getCrowno() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, saleItem.getCrowno());
			}

			// ts
			if (saleItem.getTs() == null) {
				stmt.setNull(16, Types.CHAR);
			} else {
				stmt.setString(16, saleItem.getTs().toString());
			}

			if (saleItem.getVeditreason() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, saleItem.getVeditreason());
			}
			if (saleItem.getCcurrencytypeid() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, saleItem.getCcurrencytypeid());
			}
			if (saleItem.getNitemdiscountrate() == null) {
				stmt.setNull(19, Types.INTEGER);
			} else {
				stmt.setBigDecimal(19, saleItem.getNitemdiscountrate().toBigDecimal());
			}
			if (saleItem.getNdiscountrate() == null) {
				stmt.setNull(20, Types.INTEGER);
			} else {
				stmt.setBigDecimal(20, saleItem.getNdiscountrate().toBigDecimal());
			}
			if (saleItem.getNexchangeotobrate() == null) {
				stmt.setNull(21, Types.INTEGER);
			} else {
				stmt.setBigDecimal(21, saleItem.getNexchangeotobrate().toBigDecimal());
			}
			if (saleItem.getNtaxrate() == null) {
				stmt.setNull(22, Types.INTEGER);
			} else {
				stmt.setBigDecimal(22, saleItem.getNtaxrate().toBigDecimal());
			}
			if (saleItem.getNoriginalcurprice() == null) {
				stmt.setNull(23, Types.INTEGER);
			} else {
				stmt.setBigDecimal(23, saleItem.getNoriginalcurprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxprice() == null) {
				stmt.setNull(24, Types.INTEGER);
			} else {
				stmt.setBigDecimal(24, saleItem.getNoriginalcurtaxprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurnetprice() == null) {
				stmt.setNull(25, Types.INTEGER);
			} else {
				stmt.setBigDecimal(25, saleItem.getNoriginalcurnetprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxnetprice() == null) {
				stmt.setNull(26, Types.INTEGER);
			} else {
				stmt.setBigDecimal(26, saleItem.getNoriginalcurtaxnetprice().toBigDecimal());
			}
			if (saleItem.getNoriginalcurtaxmny() == null) {
				stmt.setNull(27, Types.INTEGER);
			} else {
				stmt.setBigDecimal(27, saleItem.getNoriginalcurtaxmny().toBigDecimal());
			}
			if (saleItem.getNoriginalcurmny() == null) {
				stmt.setNull(28, Types.INTEGER);
			} else {
				stmt.setBigDecimal(28, saleItem.getNoriginalcurmny().toBigDecimal());
			}
			if (saleItem.getNoriginalcursummny() == null) {
				stmt.setNull(29, Types.INTEGER);
			} else {
				stmt.setBigDecimal(29, saleItem.getNoriginalcursummny().toBigDecimal());
			}
			if (saleItem.getNoriginalcurdiscountmny() == null) {
				stmt.setNull(30, Types.INTEGER);
			} else {
				stmt.setBigDecimal(30, saleItem.getNoriginalcurdiscountmny().toBigDecimal());
			}
			if (saleItem.getNprice() == null) {
				stmt.setNull(31, Types.INTEGER);
			} else {
				stmt.setBigDecimal(31, saleItem.getNprice().toBigDecimal());
			}
			if (saleItem.getNtaxprice() == null) {
				stmt.setNull(32, Types.INTEGER);
			} else {
				stmt.setBigDecimal(32, saleItem.getNtaxprice().toBigDecimal());
			}
			if (saleItem.getNnetprice() == null) {
				stmt.setNull(33, Types.INTEGER);
			} else {
				stmt.setBigDecimal(33, saleItem.getNnetprice().toBigDecimal());
			}
			if (saleItem.getNtaxnetprice() == null) {
				stmt.setNull(34, Types.INTEGER);
			} else {
				stmt.setBigDecimal(34, saleItem.getNtaxnetprice().toBigDecimal());
			}
			if (saleItem.getNtaxmny() == null) {
				stmt.setNull(35, Types.INTEGER);
			} else {
				stmt.setBigDecimal(35, saleItem.getNtaxmny().toBigDecimal());
			}
			if (saleItem.getNmny() == null) {
				stmt.setNull(36, Types.INTEGER);
			} else {
				stmt.setBigDecimal(36, saleItem.getNmny().toBigDecimal());
			}
			if (saleItem.getNsummny() == null) {
				stmt.setNull(37, Types.INTEGER);
			} else {
				stmt.setBigDecimal(37, saleItem.getNsummny().toBigDecimal());
			}
			if (saleItem.getNdiscountmny() == null) {
				stmt.setNull(38, Types.INTEGER);
			} else {
				stmt.setBigDecimal(38, saleItem.getNdiscountmny().toBigDecimal());
			}
			if (saleItem.getCoperatorid() == null) {
				stmt.setNull(39, Types.CHAR);
			} else {
				stmt.setString(39, saleItem.getCoperatorid());
			}
			if (saleItem.getFrowstatus() == null) {
				stmt.setNull(40, Types.INTEGER);
			} else {
				stmt.setInt(40, saleItem.getFrowstatus().intValue());
			}
			if (saleItem.getFrownote() == null) {
				stmt.setNull(41, Types.CHAR);
			} else {
				stmt.setString(41, saleItem.getFrownote());
			}
			//
			if (saleItem.getCinvbasdocid() == null) {
				stmt.setNull(42, Types.CHAR);
			} else {
				stmt.setString(42, saleItem.getCinvbasdocid());
			}
			//
			if (saleItem.getCbatchid() == null) {
				stmt.setNull(43, Types.CHAR);
			} else {
				stmt.setString(43, saleItem.getCbatchid());
			}
			//
			if (saleItem.getFbatchstatus() == null) {
				stmt.setNull(44, Types.INTEGER);
			} else {
				stmt.setInt(44, saleItem.getFbatchstatus().intValue());
			}
			//
			if (saleItem.getCbomorderid() == null) {
				stmt.setNull(45, Types.CHAR);
			} else {
				stmt.setString(45, saleItem.getCbomorderid());
			}
			//
			if (saleItem.getCfreezeid() == null) {
				stmt.setNull(46, Types.CHAR);
			} else {
				stmt.setString(46, saleItem.getCfreezeid());
			}
			//
			if (saleItem.getCt_manageid() == null) {
				stmt.setNull(47, Types.CHAR);
			} else {
				stmt.setString(47, saleItem.getCt_manageid());
			}
			// cadvisecalbodyid
			if (saleItem.getCadvisecalbodyid() == null) {
				stmt.setNull(48, Types.CHAR);
			} else {
				stmt.setString(48, saleItem.getCadvisecalbodyid());
			}
			// boosflag
			if (saleItem.getBoosflag() == null) {
				stmt.setString(49, "N");
			} else {
				stmt.setString(49, saleItem.getBoosflag().toString());
			}
			// bsupplyflag
			if (saleItem.getBsupplyflag() == null) {
				stmt.setString(50, "N");
			} else {
				stmt.setString(50, saleItem.getBsupplyflag().toString());
			}
			// creceiptareaid
			if (saleItem.getCreceiptareaid() == null) {
				stmt.setNull(51, Types.CHAR);
			} else {
				stmt.setString(51, saleItem.getCreceiptareaid());
			}
			// vreceiveaddress
			if (saleItem.getVreceiveaddress() == null) {
				stmt.setNull(52, Types.CHAR);
			} else {
				stmt.setString(52, saleItem.getVreceiveaddress());
			}
			// creceiptcorpid
			if (saleItem.getCreceiptcorpid() == null) {
				stmt.setNull(53, Types.CHAR);
			} else {
				stmt.setString(53, saleItem.getCreceiptcorpid());
			}

			SOField[] addfields = SaleorderBVO.getSoSaleOrderBAddFields();
			// 设置v30后加入的其他字段值
			setPreStatementOrdB(stmt, 54, saleItem);

			// find record by PK fields:
			stmt.setString(54 + (addfields == null ? 0 : addfields.length), saleItem.getPrimaryKey());

			executeUpdate(stmt);
		} finally {
		}
		updateFollowBody(saleItem, stmtfollow);
	}

	/**
	 * 功能:定单修订备份表体数据
	 * 
	 * 创建日期：2008-06-26
	 * 
	 * 作者:周长胜
	 */
	public void insertHistoryBodys(SaleorderHVO headVO, String mainID) throws Exception {
		Connection con = null;
		PreparedStatement selectStmt = null;
		PreparedStatement insertStmt = null;
		String selectSql = "select corder_bid,csaleid,pk_corp,creceipttype,csourcebillid,cinvbasdocid,csourcebillbodyid,cinventoryid,"
				+ "cunitid,cpackunitid,cbatchid,nnumber,npacknumber,cbodywarehouseid,dconsigndate,ddeliverdate,blargessflag,bbindflag,"
				+ "clargessrowno,veditreason,ccurrencytypeid,nitemdiscountrate,ndiscountrate,nexchangeotobrate,"
				+ "ntaxrate,noriginalcurprice,noriginalcurtaxprice,noriginalcurnetprice,noriginalcurtaxnetprice,noriginalcurtaxmny,"
				+ "noriginalcurmny,noriginalcursummny,noriginalcurdiscountmny,nprice,ntaxprice,nnetprice,ntaxnetprice,ntaxmny,nmny,"
				+ "nsummny,ndiscountmny,coperatorid,frowstatus,frownote,fbatchstatus,ct_manageid,cfactoryid,cfreezeid,cbomorderid,"
				+ "cadvisecalbodyid,boosflag,bsupplyflag,creceiptareaid,vreceiveaddress,creceiptcorpid,cconsigncorpid,nreturntaxrate,"
				+ "creccalbodyid,crecwareid,bdericttrans,tconsigntime,tdelivertime,bsafeprice,ntaldcnum,nasttaldcnum,ntaldcmny,"
				+ "breturnprofit,nretprofnum,nastretprofnum,nretprofmny,cpricepolicyid,cpriceitemid,cpriceitemtable,cpricecalproc,"
				+ "cquoteunitid,nquoteunitnum,norgqttaxprc,norgqtprc,norgqttaxnetprc,norgqtnetprc,nqttaxnetprc,nqtnetprc,nqttaxprc,"
				+ "nqtprc,crecaddrnode,cprolineid,crowno,cinventoryid1,cchantypeid,nqtorgtaxprc,nqtorgprc,nquoteunitrate,"
				+ "cproviderid,dr,ts,nqtorgtaxnetprc,nqtorgnetprc from so_saleorder_b where dr=0 and csaleid=?";
		String insertSql = "insert into so_saleorder_history_b (csalehistory_bid,corder_bid,csaleid,pk_corp,creceipttype,csourcebillid,"
				+ "cinvbasdocid,csourcebillbodyid,cinventoryid,cunitid,cpackunitid,cbatchid,nnumber,npacknumber,cbodywarehouseid,"
				+ "dconsigndate,ddeliverdate,blargessflag,bbindflag,clargessrowno,veditreason,ccurrencytypeid,nitemdiscountrate,ndiscountrate,"
				+ "nexchangeotobrate,ntaxrate,noriginalcurprice,noriginalcurtaxprice,noriginalcurnetprice,noriginalcurtaxnetprice,"
				+ "noriginalcurtaxmny,noriginalcurmny,noriginalcursummny,noriginalcurdiscountmny,nprice,ntaxprice,nnetprice,ntaxnetprice,"
				+ "ntaxmny,nmny,nsummny,ndiscountmny,coperatorid,frowstatus,frownote,fbatchstatus,ct_manageid,cfactoryid,cfreezeid,"
				+ "cbomorderid,cadvisecalbodyid,boosflag,bsupplyflag,creceiptareaid,vreceiveaddress,creceiptcorpid,cconsigncorpid,"
				+ "nreturntaxrate,creccalbodyid,crecwareid,bdericttrans,tconsigntime,tdelivertime,bsafeprice,ntaldcnum,nasttaldcnum,"
				+ "ntaldcmny,breturnprofit,nretprofnum,nastretprofnum,nretprofmny,cpricepolicyid,cpriceitemid,cpriceitemtable,cpricecalproc,"
				+ "cquoteunitid,nquoteunitnum,norgqttaxprc,norgqtprc,norgqttaxnetprc,norgqtnetprc,nqttaxnetprc,nqtnetprc,nqttaxprc,"
				+ "nqtprc,crecaddrnode,cprolineid,crowno,cinventoryid1,cchantypeid,nqtorgtaxprc,nqtorgprc,nquoteunitrate,"
				+ "cproviderid,dr,ts,nqtorgtaxnetprc,nqtorgnetprc,csalehistoryid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ResultSet rs = null;
		try {
			con = getConnection();
			selectStmt = con.prepareStatement(selectSql);
			selectStmt.setString(1, headVO.getCsaleid().toString());
			rs = selectStmt.executeQuery();
			while (rs.next()) {
				insertStmt = con.prepareStatement(insertSql);
				String history_records_b_ID = getOID(headVO.getPk_corp());
				insertStmt.setString(1, history_records_b_ID);
				insertStmt.setString(2, rs.getString(1));
				insertStmt.setString(3, rs.getString(2));
				insertStmt.setString(4, rs.getString(3));
				insertStmt.setString(5, rs.getString(4));
				insertStmt.setString(6, rs.getString(5));
				insertStmt.setString(7, rs.getString(6));
				insertStmt.setString(8, rs.getString(7));
				insertStmt.setString(9, rs.getString(8));
				insertStmt.setString(10, rs.getString(9));
				insertStmt.setString(11, rs.getString(10));
				insertStmt.setString(12, rs.getString(11));
				insertStmt.setBigDecimal(13, rs.getBigDecimal(12));
				insertStmt.setBigDecimal(14, rs.getBigDecimal(13));
				insertStmt.setString(15, rs.getString(14));
				insertStmt.setString(16, rs.getString(15));
				insertStmt.setString(17, rs.getString(16));
				insertStmt.setString(18, rs.getString(17));
				insertStmt.setString(19, rs.getString(18));
				insertStmt.setString(20, rs.getString(19));
				insertStmt.setString(21, rs.getString(20));// veditreason
				insertStmt.setString(22, rs.getString(21));// ccurrencytypeid
				insertStmt.setBigDecimal(23, rs.getBigDecimal(22));// nitemdiscountrate
				insertStmt.setBigDecimal(24, rs.getBigDecimal(23));
				insertStmt.setBigDecimal(25, rs.getBigDecimal(24));
				insertStmt.setBigDecimal(26, rs.getBigDecimal(25));
				insertStmt.setBigDecimal(27, rs.getBigDecimal(26));
				insertStmt.setBigDecimal(28, rs.getBigDecimal(27));
				insertStmt.setBigDecimal(29, rs.getBigDecimal(28));
				insertStmt.setBigDecimal(30, rs.getBigDecimal(29));
				insertStmt.setBigDecimal(31, rs.getBigDecimal(30));
				insertStmt.setBigDecimal(32, rs.getBigDecimal(31));
				insertStmt.setBigDecimal(33, rs.getBigDecimal(32));
				insertStmt.setBigDecimal(34, rs.getBigDecimal(33));
				insertStmt.setBigDecimal(35, rs.getBigDecimal(34));
				insertStmt.setBigDecimal(36, rs.getBigDecimal(35));
				insertStmt.setBigDecimal(37, rs.getBigDecimal(36));
				insertStmt.setBigDecimal(38, rs.getBigDecimal(37));
				insertStmt.setBigDecimal(39, rs.getBigDecimal(38));
				insertStmt.setBigDecimal(40, rs.getBigDecimal(39));
				insertStmt.setBigDecimal(41, rs.getBigDecimal(40));
				insertStmt.setBigDecimal(42, rs.getBigDecimal(41));
				insertStmt.setString(43, rs.getString(42));
				insertStmt.setInt(44, rs.getInt(43));
				insertStmt.setString(45, rs.getString(44));
				insertStmt.setInt(46, rs.getInt(45));
				insertStmt.setString(47, rs.getString(46));
				insertStmt.setString(48, rs.getString(47));
				insertStmt.setString(49, rs.getString(48));
				insertStmt.setString(50, rs.getString(49));
				insertStmt.setString(51, rs.getString(50));
				insertStmt.setString(52, rs.getString(51));
				insertStmt.setString(53, rs.getString(52));
				insertStmt.setString(54, rs.getString(53));
				insertStmt.setString(55, rs.getString(54));
				insertStmt.setString(56, rs.getString(55));
				insertStmt.setString(57, rs.getString(56));
				insertStmt.setBigDecimal(58, rs.getBigDecimal(57));
				insertStmt.setString(59, rs.getString(58));
				insertStmt.setString(60, rs.getString(59));
				insertStmt.setString(61, rs.getString(60));
				insertStmt.setString(62, rs.getString(61));
				insertStmt.setString(63, rs.getString(62));
				insertStmt.setString(64, rs.getString(63));
				insertStmt.setBigDecimal(65, rs.getBigDecimal(64));
				insertStmt.setBigDecimal(66, rs.getBigDecimal(65));
				insertStmt.setBigDecimal(67, rs.getBigDecimal(66));
				insertStmt.setString(68, rs.getString(67));
				insertStmt.setBigDecimal(69, rs.getBigDecimal(68));
				insertStmt.setBigDecimal(70, rs.getBigDecimal(69));
				insertStmt.setBigDecimal(71, rs.getBigDecimal(70));
				insertStmt.setString(72, rs.getString(71));
				insertStmt.setString(73, rs.getString(72));
				insertStmt.setString(74, rs.getString(73));
				insertStmt.setString(75, rs.getString(74));
				insertStmt.setString(76, rs.getString(75));
				insertStmt.setBigDecimal(77, rs.getBigDecimal(76));
				insertStmt.setBigDecimal(78, rs.getBigDecimal(77));
				insertStmt.setBigDecimal(79, rs.getBigDecimal(78));
				insertStmt.setBigDecimal(80, rs.getBigDecimal(79));
				insertStmt.setBigDecimal(81, rs.getBigDecimal(80));
				insertStmt.setBigDecimal(82, rs.getBigDecimal(81));
				insertStmt.setBigDecimal(83, rs.getBigDecimal(82));
				insertStmt.setBigDecimal(84, rs.getBigDecimal(83));
				insertStmt.setBigDecimal(85, rs.getBigDecimal(84));
				insertStmt.setString(86, rs.getString(85));
				insertStmt.setString(87, rs.getString(86));
				insertStmt.setString(88, rs.getString(87));
				insertStmt.setString(89, rs.getString(88));
				insertStmt.setString(90, rs.getString(89));
				insertStmt.setBigDecimal(91, rs.getBigDecimal(90));
				insertStmt.setBigDecimal(92, rs.getBigDecimal(91));
				insertStmt.setBigDecimal(93, rs.getBigDecimal(92));
				insertStmt.setString(94, rs.getString(93));
				insertStmt.setInt(95, rs.getInt(94));
				insertStmt.setString(96, rs.getString(95));
				insertStmt.setBigDecimal(97, rs.getBigDecimal(96));
				insertStmt.setBigDecimal(98, rs.getBigDecimal(97));
				insertStmt.setString(99, mainID);
				insertStmt.executeUpdate();
			}
		} catch (Exception e) {
			if (con != null) {
				con.close();
			}
		} finally {
			rs.close();
			insertStmt.close();
			selectStmt.close();
			con.close();
		}
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderH
	 *            nc.vo.so.so001.SaleVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public ArrayList updateBodys(SaleorderBVO[] saleorderB, String strMainID, String creceipttype)
			throws java.sql.SQLException, nc.bs.pub.SystemException {
		ArrayList listRet = new ArrayList();
		listRet.add(strMainID);
		if (saleorderB == null || saleorderB.length <= 0)
			return listRet;

		// 补充以后版本增加的数据库字段
		SOField[] addfields = SaleorderBVO.getSoSaleOrderBAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		StringBuffer addfieldvaluesql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldvaluesql.append(",?");
				}
			}
		}
		// 订单表体sql
		String sql = "insert into so_saleorder_b("
				+ "corder_bid, csaleid, pk_corp, creceipttype, csourcebillid, "
				+ "csourcebillbodyid, cinventoryid, cunitid, cpackunitid, nnumber, "
				// 10
				+ "npacknumber, cbodywarehouseid, dconsigndate, ddeliverdate, blargessflag, "
				+ "crowno, ts, veditreason, ccurrencytypeid, nitemdiscountrate, "
				// 20
				+ "ndiscountrate, nexchangeotobrate, ntaxrate, noriginalcurprice, noriginalcurtaxprice, "
				+ "noriginalcurnetprice, noriginalcurtaxnetprice, noriginalcurtaxmny, noriginalcurmny, noriginalcursummny, "
				// 30
				+ "noriginalcurdiscountmny, nprice, ntaxprice, nnetprice, ntaxnetprice, "
				+ "ntaxmny, nmny, nsummny, ndiscountmny, coperatorid, "
				// 40
				+ "frowstatus, frownote, cinvbasdocid, cbatchid, fbatchstatus, "
				+ "cbomorderid, ct_manageid, cfreezeid, cadvisecalbodyid, boosflag, "
				// 50
				+ "bsupplyflag, creceiptareaid, vreceiveaddress, creceiptcorpid " + addfieldsql.toString() + ") "
				+ "values(" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ? " + addfieldvaluesql.toString() + ")";
		PreparedStatement stmt = null;

		// 订单执行表sql
		// 补充以后版本增加的数据库字段
		addfields = SaleorderBVO.getSoSaleExecuteAddFields();
		addfieldsql.setLength(0);
		addfieldsql.append("");
		addfieldvaluesql.setLength(0);
		addfieldvaluesql.append("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldvaluesql.append(",?");
				}
			}
		}
		String sqlexe = "insert into so_saleexecute(csale_bid, csaleid, creceipttype, ntotalpaymny, ntotalreceivenumber, ntotalinvoicenumber, ntotalinventorynumber, bifinvoicefinish, bifreceivefinish, bifinventoryfinish, bifpayfinish, cprojectid3, cprojectphaseid, cprojectid, vfree5, vfree4, vfree3, vfree2, vfree1, vdef6, vdef5, vdef4, vdef3, vdef2, vdef1, ts "
				+ addfieldsql.toString()
				+ ")"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
				+ addfieldvaluesql.toString() + ")";
		PreparedStatement stmtexe = null;

		// 设置其他增加的字段
		addfields = SaleorderBVO.getSoSaleOrderBAddFields();
		addfieldsql.setLength(0);
		addfieldsql.append("");
		addfieldvaluesql.setLength(0);
		addfieldvaluesql.append("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldsql.append(" = ? ");
				}
			}
		}
		String sqlupdate = "update so_saleorder_b set "
				+ "csaleid = ?, pk_corp = ?, creceipttype = ?, csourcebillid = ?, csourcebillbodyid = ?, "
				+ "cinventoryid = ?, cunitid = ?, cpackunitid = ?, nnumber = ?, npacknumber = ?, "
				+ "cbodywarehouseid = ?, dconsigndate = ?, ddeliverdate = ?, blargessflag = ?, crowno = ? , "
				+ "ts = ?, veditreason = ?, ccurrencytypeid = ?, nitemdiscountrate = ?, ndiscountrate = ?, "
				+ "nexchangeotobrate = ?, ntaxrate = ?, noriginalcurprice = ?, noriginalcurtaxprice = ?, "
				+ "noriginalcurnetprice = ?, noriginalcurtaxnetprice = ?, noriginalcurtaxmny = ?, noriginalcurmny = ?, noriginalcursummny = ?, "
				+ "noriginalcurdiscountmny = ?, nprice = ?, ntaxprice = ?, nnetprice = ?, ntaxnetprice = ?, "
				+ "ntaxmny = ?, nmny = ?, nsummny = ?, ndiscountmny = ?, coperatorid = ?, "
				+ "frowstatus = ?, frownote = ?,cinvbasdocid = ?,cbatchid = ?,fbatchstatus = ?,"
				+ "cbomorderid=?,cfreezeid=?,ct_manageid=?, cadvisecalbodyid = ?, boosflag = ?, "
				+ "bsupplyflag = ?, creceiptareaid= ?, vreceiveaddress= ?, creceiptcorpid= ?" + addfieldsql.toString()
				+ " where corder_bid = ?";
		PreparedStatement stmtupdate = null;

		// 设置其他增加的字段
		addfields = SaleorderBVO.getSoSaleExecuteAddFields();
		addfieldsql.setLength(0);
		addfieldsql.append("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldsql.append(" = ? ");
				}
			}
		}
		String sqlfollow = "update so_saleexecute set "

		+ "cprojectid3 = ?, cprojectphaseid = ?, "
				+ "cprojectid = ?, vfree5 = ?, vfree4 = ?, vfree3 = ?, vfree2 = ?, vfree1 = ?, "
				+ "vdef6 = ?, vdef5 = ?, vdef4 = ?, vdef3 = ?, vdef2 = ?, vdef1 = ?, ts = ? " + addfieldsql.toString()
				+ " where csale_bid = ?";
		String sqlfollow2 = "update so_saleexecute set csaleid = ?, creceipttype = ?, "
				+ "cprojectid3 = ?, cprojectphaseid = ?, "
				+ "cprojectid = ?, vfree5 = ?, vfree4 = ?, vfree3 = ?, vfree2 = ?, vfree1 = ?, "
				+ "vdef6 = ?, vdef5 = ?, vdef4 = ?, vdef3 = ?, vdef2 = ?, vdef1 = ?, ts = ? " + addfieldsql.toString()
				+ " where csale_bid = ? and creceipttype = '30' ";
		PreparedStatement stmtfollow = null;
		PreparedStatement stmtfollow2 = null;

		String deleteBodySql = " delete from so_saleorder_b where corder_bid = ? ";
		PreparedStatement stmtdeleteBody = null;

		String deleteExeSql = " delete from so_saleexecute where csale_bid = ? and creceipttype = '30' ";
		PreparedStatement stmtdeleteExe = null;

		Connection con = null;
		try {
			con = getConnection();
			((CrossDBConnection) con).setAddTimeStamp(false);
			stmt = prepareStatement(con, sql);
			stmtexe = prepareStatement(con, sqlexe);
			stmtupdate = prepareStatement(con, sqlupdate);
			stmtfollow = prepareStatement(con, sqlfollow);
			stmtfollow2 = prepareStatement(con, sqlfollow2);

			stmtdeleteBody = prepareStatement(con, deleteBodySql);
			stmtdeleteExe = prepareStatement(con, deleteExeSql);

			for (int i = 0; i < saleorderB.length; i++) {
				// BOM配置
				String bomID = (String) saleorderB[i].getCbomorderid();
				if (bomID != null && bomID.trim().length() != 0) {
					// 需求数量
					updateBomVO(saleorderB[i]);
				}
				switch (saleorderB[i].getStatus()) {
				case VOStatus.NEW: { // 缺货存货不存入订单
					if (saleorderB[i].getBoosflag() == null || !saleorderB[i].getBoosflag().booleanValue()) {

						String idRow = insertBody(saleorderB[i], strMainID, creceipttype, stmt, stmtexe);
						listRet.add(idRow);
					}
					break;
				}
				case VOStatus.DELETED: {
					deleteBodyAndExe(stmtdeleteBody, stmtdeleteExe, saleorderB[i].getPrimaryKey());
					// deleteBody(saleorderB[i].getPrimaryKey());
					break;
				}
				case VOStatus.UPDATED: {
					updateBody(saleorderB[i], creceipttype, stmtupdate, stmtfollow2);
					break;
				}
				case nc.vo.pub.bill.BillVOStatus.MODIFICATION: {
					// modificationBody(saleorderB[i], strMainID, creceipttype,
					// stmt, stmtexe, stmtupdate, stmtfollow);
					updateBody(saleorderB[i], stmtupdate, stmtfollow);
					break;
				}
				}
			}
			executeBatch(stmt);
			executeBatch(stmtexe);
			executeBatch(stmtfollow);
			executeBatch(stmtfollow2);
			executeBatch(stmtupdate);

			executeBatch(stmtdeleteBody);
			executeBatch(stmtdeleteExe);

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
			try {
				if (stmtexe != null) {
					stmtexe.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
			try {
				if (stmtfollow != null) {
					stmtfollow.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
			try {
				if (stmtfollow2 != null) {
					stmtfollow2.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
			try {
				if (stmtupdate != null) {
					stmtupdate.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
			try {
				if (stmtdeleteBody != null) {
					stmtdeleteBody.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
			try {
				if (stmtdeleteExe != null) {
					stmtdeleteExe.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}

			try {
				if (con != null) {
					((CrossDBConnection) con).setAddTimeStamp(true);
					con.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
		}

		return listRet;
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2002-4-16)
	 * 
	 * @param bomorderHeader
	 *            nc.vo.so.so001.BomorderHeaderVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateBom(String bomID, String saleID, String saleBID) throws java.sql.SQLException {

		String sql = "update so_bomorder set csaleid = '" + saleID + "', corder_bid = '" + saleBID
				+ "' where cbomorderid = '" + bomID + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2002-4-16)
	 * 
	 * @param bomorderHeader
	 *            nc.vo.so.so001.BomorderHeaderVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateBomVO(SaleorderBVO voBody) throws java.sql.SQLException {
		String bomID = voBody.getCbomorderid();
		String sql = "update so_bomorder set nrequirenumber = " + voBody.getNnumber() + " where cbomorderid = '"
				+ bomID + "'";
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateFollowBody(SaleorderBVO saleexecute, String creceipttype, PreparedStatement stmt)
			throws java.sql.SQLException, nc.bs.pub.SystemException {

		try {
			// update non PK fields:
			if (saleexecute.getCsaleid() == null) {
				stmt.setNull(1, Types.CHAR);
			} else {
				stmt.setString(1, saleexecute.getCsaleid());
			}
			if (creceipttype == null) {
				stmt.setNull(2, Types.CHAR);
			} else {
				stmt.setString(2, creceipttype);
			}

			if (saleexecute.getCprojectid3() == null) {
				stmt.setNull(3, Types.CHAR);
			} else {
				stmt.setString(3, saleexecute.getCprojectid3());
			}
			if (saleexecute.getCprojectphaseid() == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, saleexecute.getCprojectphaseid());
			}
			if (saleexecute.getCprojectid() == null) {
				stmt.setNull(5, Types.CHAR);
			} else {
				stmt.setString(5, saleexecute.getCprojectid());
			}
			if (saleexecute.getVfree5() == null) {
				stmt.setNull(6, Types.CHAR);
			} else {
				stmt.setString(6, saleexecute.getVfree5());
			}
			if (saleexecute.getVfree4() == null) {
				stmt.setNull(7, Types.CHAR);
			} else {
				stmt.setString(7, saleexecute.getVfree4());
			}
			if (saleexecute.getVfree3() == null) {
				stmt.setNull(8, Types.CHAR);
			} else {
				stmt.setString(8, saleexecute.getVfree3());
			}
			if (saleexecute.getVfree2() == null) {
				stmt.setNull(9, Types.CHAR);
			} else {
				stmt.setString(9, saleexecute.getVfree2());
			}
			if (saleexecute.getVfree1() == null) {
				stmt.setNull(10, Types.CHAR);
			} else {
				stmt.setString(10, saleexecute.getVfree1());
			}
			if (saleexecute.getVdef6() == null) {
				stmt.setNull(11, Types.CHAR);
			} else {
				stmt.setString(11, saleexecute.getVdef6());
			}
			if (saleexecute.getVdef5() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleexecute.getVdef5());
			}
			if (saleexecute.getVdef4() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleexecute.getVdef4());
			}
			if (saleexecute.getVdef3() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, saleexecute.getVdef3());
			}
			if (saleexecute.getVdef2() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, saleexecute.getVdef2());
			}
			if (saleexecute.getVdef1() == null) {
				stmt.setNull(16, Types.CHAR);
			} else {
				stmt.setString(16, saleexecute.getVdef1());
			}
			if (saleexecute.getTs() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, saleexecute.getTs().toString());
			}

			SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();
			// 设置v30后加入的其他字段值

			setPreStatementOrdE(stmt, 18, saleexecute);

			// find record by PK fields:
			stmt.setString(18 + (addfields == null ? 0 : addfields.length), saleexecute.getPrimaryKey());

			executeUpdate(stmt);
		} finally {
		}

	}

	/**
	 * 用一个VO对象的属性更新数据库中的值。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateFollowBody(SaleorderBVO saleexecute, PreparedStatement stmt) throws java.sql.SQLException,
			nc.bs.pub.SystemException {

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });
		/** ********************************************************** */

		try {

			if (saleexecute.getCprojectid3() == null) {
				stmt.setNull(1, Types.CHAR);
			} else {
				stmt.setString(1, saleexecute.getCprojectid3());
			}
			if (saleexecute.getCprojectphaseid() == null) {
				stmt.setNull(2, Types.CHAR);
			} else {
				stmt.setString(2, saleexecute.getCprojectphaseid());
			}
			if (saleexecute.getCprojectid() == null) {
				stmt.setNull(3, Types.CHAR);
			} else {
				stmt.setString(3, saleexecute.getCprojectid());
			}
			if (saleexecute.getVfree5() == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, saleexecute.getVfree5());
			}
			if (saleexecute.getVfree4() == null) {
				stmt.setNull(5, Types.CHAR);
			} else {
				stmt.setString(5, saleexecute.getVfree4());
			}
			if (saleexecute.getVfree3() == null) {
				stmt.setNull(6, Types.CHAR);
			} else {
				stmt.setString(6, saleexecute.getVfree3());
			}
			if (saleexecute.getVfree2() == null) {
				stmt.setNull(7, Types.CHAR);
			} else {
				stmt.setString(7, saleexecute.getVfree2());
			}
			if (saleexecute.getVfree1() == null) {
				stmt.setNull(8, Types.CHAR);
			} else {
				stmt.setString(8, saleexecute.getVfree1());
			}
			if (saleexecute.getVdef6() == null) {
				stmt.setNull(9, Types.CHAR);
			} else {
				stmt.setString(9, saleexecute.getVdef6());
			}
			if (saleexecute.getVdef5() == null) {
				stmt.setNull(10, Types.CHAR);
			} else {
				stmt.setString(10, saleexecute.getVdef5());
			}
			if (saleexecute.getVdef4() == null) {
				stmt.setNull(11, Types.CHAR);
			} else {
				stmt.setString(11, saleexecute.getVdef4());
			}
			if (saleexecute.getVdef3() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleexecute.getVdef3());
			}
			if (saleexecute.getVdef2() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleexecute.getVdef2());
			}
			if (saleexecute.getVdef1() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, saleexecute.getVdef1());
			}
			if (saleexecute.getTs() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, saleexecute.getTs().toString());
			}
			SOField[] addfields = SaleorderBVO.getSoSaleExecuteAddFields();
			// 设置v30后加入的其他字段值

			setPreStatementOrdE(stmt, 16, saleexecute);

			// find record by PK fields:
			stmt.setString(16 + (addfields == null ? 0 : addfields.length), saleexecute.getPrimaryKey());
			// stmt.setString(23, saleexecute.getPrimaryKey());
			//
			// stmt.executeUpdate();
			executeUpdate(stmt);
		} finally {
		}

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateFollowBody", new Object[] { saleexecute });
		/** ********************************************************** */
	}

	/**
	 * 
	 * 2008-06-25 周长胜 定单修订备份表头数据
	 * 
	 */
	public String insertHistoryHead(SaleorderHVO saleHeader) throws Exception {
		String key = saleHeader.getCsaleid();
		String history_records_ID = getOID(saleHeader.getPk_corp());
		String selectSql = "select csaleid,pk_corp,vreceiptcode,creceipttype,cbiztype,finvoiceclass,finvoicetype,vaccountyear,"
				+ "binitflag,dbilldate,ccustomerid,cdeptid,cemployeeid,coperatorid,ctermprotocolid,csalecorpid,ccalbodyid,"
				+ "creceiptcustomerid,vreceiveaddress,creceiptareaid,creceiptcorpid,ccustbankid,ctransmodeid,ndiscountrate,cwarehouseid,"
				+ "veditreason,bfreecustflag,cfreecustid,ibalanceflag,nsubscription,ccreditnum,nevaluatecarriage,dmakedate,capproveid,"
				+ "dapprovedate,fstatus,vnote,bretinvflag,boutendflag,binvoicendflag,breceiptendflag,npreceiverate,npreceivemny,btransendflag,"
				+ "naccountperiod,bpayendflag,iprintcount,vdef1,vdef2,vdef3,vdef4,vdef5,vdef6,vdef7,vdef8,vdef9,"
				+ "vdef10,vdef11,vdef12,vdef13,vdef14,vdef15,vdef16,vdef17,vdef18,vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,"
				+ "pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,"
				+ "pk_defdoc15,pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,nheadsummny,dbilltime,daudittime,dmoditime,"
				+ "editionnum,editdate,editauthor,bcooptopo,bpocooptome,ccooppohid,cbaltypeid,dr,ts from so_sale where dr=0 and csaleid='"
				+ key + "'";
		String insertSql = "insert into so_sale_history (csalehistoryid,csaleid,pk_corp,vreceiptcode,creceipttype,cbiztype,finvoiceclass,"
				+ "finvoicetype,vaccountyear,binitflag,dbilldate,ccustomerid,cdeptid,cemployeeid,coperatorid,ctermprotocolid,csalecorpid,"
				+ "ccalbodyid,creceiptcustomerid,vreceiveaddress,creceiptareaid,creceiptcorpid,ccustbankid,ctransmodeid,ndiscountrate,"
				+ "cwarehouseid,veditreason,bfreecustflag,cfreecustid,ibalanceflag,nsubscription,ccreditnum,nevaluatecarriage,dmakedate,"
				+ "capproveid,dapprovedate,fstatus,vnote,bretinvflag,boutendflag,binvoicendflag,breceiptendflag,npreceiverate,npreceivemny,"
				+ "btransendflag,naccountperiod,bpayendflag,iprintcount,vdef1,vdef2,vdef3,vdef4,vdef5,vdef6,vdef7,vdef8,"
				+ "vdef9,vdef10,vdef11,vdef12,vdef13,vdef14,vdef15,vdef16,vdef17,vdef18,vdef19,vdef20,pk_defdoc1,pk_defdoc2,pk_defdoc3,pk_defdoc4,"
				+ "pk_defdoc5,pk_defdoc6,pk_defdoc7,pk_defdoc8,pk_defdoc9,pk_defdoc10,pk_defdoc11,pk_defdoc12,pk_defdoc13,pk_defdoc14,pk_defdoc15,"
				+ "pk_defdoc16,pk_defdoc17,pk_defdoc18,pk_defdoc19,pk_defdoc20,nheadsummny,dbilltime,daudittime,dmoditime,"
				+ "editionnum,editdate,editauthor,bcooptopo,bpocooptome,ccooppohid,cbaltypeid,dr,ts) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement selectStmt = null;
		PreparedStatement insertStmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			selectStmt = con.prepareStatement(selectSql);
			rs = selectStmt.executeQuery();
			while (rs.next()) {
				insertStmt = con.prepareStatement(insertSql);
				insertStmt.setString(1, history_records_ID);
				insertStmt.setString(2, rs.getString(1));
				insertStmt.setString(3, rs.getString(2));
				insertStmt.setString(4, rs.getString(3));
				insertStmt.setString(5, rs.getString(4));
				insertStmt.setString(6, rs.getString(5));
				insertStmt.setInt(7, rs.getInt(6));
				insertStmt.setInt(8, rs.getInt(7));
				insertStmt.setString(9, rs.getString(8));
				insertStmt.setString(10, rs.getString(9));
				insertStmt.setString(11, rs.getString(10));
				insertStmt.setString(12, rs.getString(11));
				insertStmt.setString(13, rs.getString(12));
				insertStmt.setString(14, rs.getString(13));
				insertStmt.setString(15, rs.getString(14));
				insertStmt.setString(16, rs.getString(15));
				insertStmt.setString(17, rs.getString(16));
				insertStmt.setString(18, rs.getString(17));
				insertStmt.setString(19, rs.getString(18));
				insertStmt.setString(20, rs.getString(19));
				insertStmt.setString(21, rs.getString(20));
				insertStmt.setString(22, rs.getString(21));
				insertStmt.setString(23, rs.getString(22));
				insertStmt.setString(24, rs.getString(23));
				insertStmt.setBigDecimal(25, rs.getBigDecimal(24));
				insertStmt.setString(26, rs.getString(25));
				insertStmt.setString(27, rs.getString(26));
				insertStmt.setString(28, rs.getString(27));
				insertStmt.setString(29, rs.getString(28));
				insertStmt.setInt(30, rs.getInt(29));
				insertStmt.setBigDecimal(31, rs.getBigDecimal(30));
				insertStmt.setString(32, rs.getString(31));
				insertStmt.setBigDecimal(33, rs.getBigDecimal(32));
				insertStmt.setString(34, rs.getString(33));
				insertStmt.setString(35, rs.getString(34));
				insertStmt.setString(36, rs.getString(35));
				insertStmt.setInt(37, rs.getInt(36));
				insertStmt.setString(38, rs.getString(37));
				insertStmt.setString(39, rs.getString(38));
				insertStmt.setString(40, rs.getString(39));
				insertStmt.setString(41, rs.getString(40));
				insertStmt.setString(42, rs.getString(41));
				insertStmt.setBigDecimal(43, rs.getBigDecimal(42));
				insertStmt.setBigDecimal(44, rs.getBigDecimal(43));
				insertStmt.setString(45, rs.getString(44));
				insertStmt.setBigDecimal(46, rs.getBigDecimal(45));// naccountperiod
				// insertStmt.setString(47, rs.getString(46));//boverdate
				insertStmt.setString(47, rs.getString(46));
				insertStmt.setInt(48, rs.getInt(47));
				insertStmt.setString(49, rs.getString(48));
				insertStmt.setString(50, rs.getString(49));
				insertStmt.setString(51, rs.getString(50));
				insertStmt.setString(52, rs.getString(51));
				insertStmt.setString(53, rs.getString(52));
				insertStmt.setString(54, rs.getString(53));
				insertStmt.setString(55, rs.getString(54));
				insertStmt.setString(56, rs.getString(55));
				insertStmt.setString(57, rs.getString(56));
				insertStmt.setString(58, rs.getString(57));
				insertStmt.setString(59, rs.getString(58));
				insertStmt.setString(60, rs.getString(59));
				insertStmt.setString(61, rs.getString(60));
				insertStmt.setString(62, rs.getString(61));
				insertStmt.setString(63, rs.getString(62));
				insertStmt.setString(64, rs.getString(63));
				insertStmt.setString(65, rs.getString(64));
				insertStmt.setString(66, rs.getString(65));
				insertStmt.setString(67, rs.getString(66));
				insertStmt.setString(68, rs.getString(67));
				insertStmt.setString(69, rs.getString(68));
				insertStmt.setString(70, rs.getString(69));
				insertStmt.setString(71, rs.getString(70));
				insertStmt.setString(72, rs.getString(71));
				insertStmt.setString(73, rs.getString(72));
				insertStmt.setString(74, rs.getString(73));
				insertStmt.setString(75, rs.getString(74));
				insertStmt.setString(76, rs.getString(75));
				insertStmt.setString(77, rs.getString(76));
				insertStmt.setString(78, rs.getString(77));
				insertStmt.setString(79, rs.getString(78));
				insertStmt.setString(80, rs.getString(79));
				insertStmt.setString(81, rs.getString(80));
				insertStmt.setString(82, rs.getString(81));
				insertStmt.setString(83, rs.getString(82));
				insertStmt.setString(84, rs.getString(83));
				insertStmt.setString(85, rs.getString(84));
				insertStmt.setString(86, rs.getString(85));
				insertStmt.setString(87, rs.getString(86));
				insertStmt.setString(88, rs.getString(87));
				insertStmt.setBigDecimal(89, rs.getBigDecimal(88));
				insertStmt.setString(90, rs.getString(89));
				insertStmt.setString(91, rs.getString(90));
				insertStmt.setString(92, rs.getString(91));
				insertStmt.setString(93, rs.getString(92));// editionnum
				insertStmt.setString(94, rs.getString(93));
				insertStmt.setString(95, rs.getString(94));
				insertStmt.setString(96, rs.getString(95));
				insertStmt.setString(97, rs.getString(96));
				insertStmt.setString(98, rs.getString(97));
				insertStmt.setString(99, rs.getString(98));
				insertStmt.setInt(100, rs.getInt(99));
				insertStmt.setString(101, rs.getString(100));
				insertStmt.executeUpdate();
			}
		} catch (Exception e) {
			if (con != null) {
				con.close();
			}
		} finally {
			rs.close();
			selectStmt.close();
			insertStmt.close();
			con.close();
		}
		return history_records_ID;
	}

	/**
	 * 
	 * 修改日期：2003-10-16 修改人：杨涛 修改内容：增加表头“预收款比例”和“预收款金额”
	 * 
	 */
	public void updateHead(SaleorderHVO saleHeader) throws java.sql.SQLException, nc.bs.pub.SystemException {

		// 设置其他增加的字段
		SOField[] addfields = SaleorderHVO.getAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getDatabasename());
					addfieldsql.append(" = ?");
				}
			}
		}

		String sql = "update so_sale set pk_corp = ?, vreceiptcode = ?, creceipttype = ?, cbiztype = ?,"
				+ " finvoiceclass = ?, finvoicetype = ?, vaccountyear = ?, binitflag = ?, dbilldate = ?,"
				+ " ccustomerid = ?, cdeptid = ?, cemployeeid = ?, coperatorid = ?, ctermprotocolid = ?, "
				+ "csalecorpid = ?, creceiptcustomerid = ?, vreceiveaddress = ?, creceiptcorpid = ?, "
				+ "ctransmodeid = ?, ndiscountrate = ?, cwarehouseid = ?, veditreason = ?, bfreecustflag = ?,"
				+ " cfreecustid = ?, ibalanceflag = ?, nsubscription = ?, ccreditnum = ?, nevaluatecarriage = ?, "
				+ "dmakedate = ?, capproveid = ?, dapprovedate = ?, fstatus = ?, vnote = ?, vdef1 = ?, vdef2 = ?,"
				+ " vdef3 = ?, vdef4 = ?, vdef5 = ?, vdef6 = ?, vdef7 = ?, vdef8 = ?, vdef9 = ?, vdef10 = ?,"
				+ "ccalbodyid = ?,bretinvflag = ?,boutendflag = ?,binvoicendflag = ?,breceiptendflag = ?,npreceiverate = ?,"
				+ "npreceivemny = ?,ts = ?,nheadsummny = ? ,editionnum=?,editdate=?,editauthor=? "
				+ addfieldsql.toString() + "" + " where csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			((CrossDBConnection) con).setAddTimeStamp(false);
			stmt = con.prepareStatement(sql);
			// update non PK fields:
			if (saleHeader.getPk_corp() == null) {
				stmt.setNull(1, Types.CHAR);
			} else {
				stmt.setString(1, saleHeader.getPk_corp());
			}
			if (saleHeader.getVreceiptcode() == null) {
				stmt.setNull(2, Types.CHAR);
			} else {
				stmt.setString(2, saleHeader.getVreceiptcode());
			}
			if (saleHeader.getCreceipttype() == null) {
				stmt.setNull(3, Types.CHAR);
			} else {
				stmt.setString(3, saleHeader.getCreceipttype());
			}
			if (saleHeader.getCbiztype() == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, saleHeader.getCbiztype());
			}
			if (saleHeader.getFinvoiceclass() == null) {
				stmt.setNull(5, Types.INTEGER);
			} else {
				stmt.setInt(5, saleHeader.getFinvoiceclass().intValue());
			}
			if (saleHeader.getFinvoicetype() == null) {
				stmt.setNull(6, Types.INTEGER);
			} else {
				stmt.setInt(6, saleHeader.getFinvoicetype().intValue());
			}
			if (saleHeader.getVaccountyear() == null) {
				stmt.setNull(7, Types.CHAR);
			} else {
				stmt.setString(7, saleHeader.getVaccountyear());
			}
			if (saleHeader.getBinitflag() == null) {
				stmt.setString(8, "N");
			} else {
				stmt.setString(8, saleHeader.getBinitflag().toString());
			}
			if (saleHeader.getDbilldate() == null) {
				stmt.setNull(9, Types.CHAR);
			} else {
				stmt.setString(9, saleHeader.getDbilldate().toString());
			}
			if (saleHeader.getCcustomerid() == null) {
				stmt.setNull(10, Types.CHAR);
			} else {
				stmt.setString(10, saleHeader.getCcustomerid());
			}
			if (saleHeader.getCdeptid() == null) {
				stmt.setNull(11, Types.CHAR);
			} else {
				stmt.setString(11, saleHeader.getCdeptid());
			}
			if (saleHeader.getCemployeeid() == null) {
				stmt.setNull(12, Types.CHAR);
			} else {
				stmt.setString(12, saleHeader.getCemployeeid());
			}
			if (saleHeader.getCoperatorid() == null) {
				stmt.setNull(13, Types.CHAR);
			} else {
				stmt.setString(13, saleHeader.getCoperatorid());
			}
			if (saleHeader.getCtermprotocolid() == null) {
				stmt.setNull(14, Types.CHAR);
			} else {
				stmt.setString(14, saleHeader.getCtermprotocolid());
			}
			if (saleHeader.getCsalecorpid() == null) {
				stmt.setNull(15, Types.CHAR);
			} else {
				stmt.setString(15, saleHeader.getCsalecorpid());
			}
			if (saleHeader.getCreceiptcustomerid() == null) {
				stmt.setNull(16, Types.CHAR);
			} else {
				stmt.setString(16, saleHeader.getCreceiptcustomerid());
			}
			if (saleHeader.getVreceiveaddress() == null) {
				stmt.setNull(17, Types.CHAR);
			} else {
				stmt.setString(17, saleHeader.getVreceiveaddress());
			}
			if (saleHeader.getCreceiptcorpid() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, saleHeader.getCreceiptcorpid());
			}
			if (saleHeader.getCtransmodeid() == null) {
				stmt.setNull(19, Types.CHAR);
			} else {
				stmt.setString(19, saleHeader.getCtransmodeid());
			}
			if (saleHeader.getNdiscountrate() == null) {
				stmt.setNull(20, Types.INTEGER);
			} else {
				stmt.setBigDecimal(20, saleHeader.getNdiscountrate().toBigDecimal());
			}
			if (saleHeader.getCwarehouseid() == null) {
				stmt.setNull(21, Types.CHAR);
			} else {
				stmt.setString(21, saleHeader.getCwarehouseid());
			}
			if (saleHeader.getVeditreason() == null) {
				stmt.setNull(22, Types.CHAR);
			} else {
				stmt.setString(22, saleHeader.getVeditreason());
			}
			if (saleHeader.getBfreecustflag() == null) {
				stmt.setNull(23, Types.CHAR);
			} else {
				stmt.setString(23, saleHeader.getBfreecustflag().toString());
			}
			if (saleHeader.getCfreecustid() == null) {
				stmt.setNull(24, Types.CHAR);
			} else {
				stmt.setString(24, saleHeader.getCfreecustid());
			}
			if (saleHeader.getIbalanceflag() == null) {
				stmt.setNull(25, Types.INTEGER);
			} else {
				stmt.setInt(25, saleHeader.getIbalanceflag().intValue());
			}
			if (saleHeader.getNsubscription() == null) {
				stmt.setNull(26, Types.INTEGER);
			} else {
				stmt.setBigDecimal(26, saleHeader.getNsubscription().toBigDecimal());
			}
			if (saleHeader.getCcreditnum() == null) {
				stmt.setNull(27, Types.CHAR);
			} else {
				stmt.setString(27, saleHeader.getCcreditnum());
			}
			if (saleHeader.getNevaluatecarriage() == null) {
				stmt.setNull(28, Types.INTEGER);
			} else {
				stmt.setBigDecimal(28, saleHeader.getNevaluatecarriage().toBigDecimal());
			}
			if (saleHeader.getDmakedate() == null) {
				stmt.setNull(29, Types.CHAR);
			} else {
				stmt.setString(29, saleHeader.getDmakedate().toString());
			}
			if (saleHeader.getCapproveid() == null) {
				stmt.setNull(30, Types.CHAR);
			} else {
				stmt.setString(30, saleHeader.getCapproveid());
			}
			if (saleHeader.getDapprovedate() == null) {
				stmt.setNull(31, Types.CHAR);
			} else {
				stmt.setString(31, saleHeader.getDapprovedate().toString());
			}
			if (saleHeader.getFstatus() == null) {
				stmt.setNull(32, Types.INTEGER);
			} else {
				stmt.setInt(32, saleHeader.getFstatus().intValue());
			}
			if (saleHeader.getVnote() == null) {
				stmt.setNull(33, Types.CHAR);
			} else {
				stmt.setString(33, saleHeader.getVnote());
			}
			if (saleHeader.getVdef1() == null) {
				stmt.setNull(34, Types.CHAR);
			} else {
				stmt.setString(34, saleHeader.getVdef1());
			}
			if (saleHeader.getVdef2() == null) {
				stmt.setNull(35, Types.CHAR);
			} else {
				stmt.setString(35, saleHeader.getVdef2());
			}
			if (saleHeader.getVdef3() == null) {
				stmt.setNull(36, Types.CHAR);
			} else {
				stmt.setString(36, saleHeader.getVdef3());
			}
			if (saleHeader.getVdef4() == null) {
				stmt.setNull(37, Types.CHAR);
			} else {
				stmt.setString(37, saleHeader.getVdef4());
			}
			if (saleHeader.getVdef5() == null) {
				stmt.setNull(38, Types.CHAR);
			} else {
				stmt.setString(38, saleHeader.getVdef5());
			}
			if (saleHeader.getVdef6() == null) {
				stmt.setNull(39, Types.CHAR);
			} else {
				stmt.setString(39, saleHeader.getVdef6());
			}
			if (saleHeader.getVdef7() == null) {
				stmt.setNull(40, Types.CHAR);
			} else {
				stmt.setString(40, saleHeader.getVdef7());
			}
			if (saleHeader.getVdef8() == null) {
				stmt.setNull(41, Types.CHAR);
			} else {
				stmt.setString(41, saleHeader.getVdef8());
			}
			if (saleHeader.getVdef9() == null) {
				stmt.setNull(42, Types.CHAR);
			} else {
				stmt.setString(42, saleHeader.getVdef9());
			}
			if (saleHeader.getVdef10() == null) {
				stmt.setNull(43, Types.CHAR);
			} else {
				stmt.setString(43, saleHeader.getVdef10());
			}
			//
			if (saleHeader.getCcalbodyid() == null) {
				stmt.setNull(44, Types.CHAR);
			} else {
				stmt.setString(44, saleHeader.getCcalbodyid());
			}
			if (saleHeader.getBretinvflag() == null) {
				stmt.setString(45, "N");
			} else {
				stmt.setString(45, saleHeader.getBretinvflag().toString());
			}
			if (saleHeader.getBoutendflag() == null) {
				stmt.setString(46, "N");
			} else {
				stmt.setString(46, saleHeader.getBoutendflag().toString());
			}
			if (saleHeader.getBinvoicendflag() == null) {
				stmt.setString(47, "N");
			} else {
				stmt.setString(47, saleHeader.getBinvoicendflag().toString());
			}
			if (saleHeader.getBreceiptendflag() == null) {
				stmt.setString(48, "N");
			} else {
				stmt.setString(48, saleHeader.getBreceiptendflag().toString());
			}
			// yt modify 2003-10-16

			// yt add 2003-10-16
			if (saleHeader.getNpreceiverate() == null) {
				stmt.setNull(49, Types.INTEGER);
			} else {
				stmt.setBigDecimal(49, saleHeader.getNpreceiverate().toBigDecimal());
			}
			if (saleHeader.getNpreceivemny() == null) {
				stmt.setNull(50, Types.INTEGER);
			} else {
				stmt.setBigDecimal(50, saleHeader.getNpreceivemny().toBigDecimal());
			}
			if (saleHeader.getTs() == null) {
				stmt.setNull(51, Types.CHAR);
			} else {
				stmt.setString(51, saleHeader.getTs().toString());
			}

			if (saleHeader.getAttributeValue("nheadsummny") == null) {
				stmt.setNull(52, Types.INTEGER);
			} else {
				stmt.setBigDecimal(52, ((UFDouble) saleHeader.getAttributeValue("nheadsummny")).toBigDecimal());
			}

			if (saleHeader.getEditionNum() == null) {
				stmt.setNull(53, Types.CHAR);
			} else {
				stmt.setString(53, saleHeader.getEditionNum().toString());
			}

			if (saleHeader.getEditDate() == null) {
				stmt.setNull(54, Types.CHAR);
			} else {
				stmt.setString(54, saleHeader.getEditDate().toString());
			}

			if (saleHeader.getEditAuthor() == null) {
				stmt.setNull(55, Types.CHAR);
			} else {
				stmt.setString(55, saleHeader.getEditAuthor().toString());
			}

			// 设置v30后加入的其他字段值
			int curpos = setPreStatementOrdH(stmt, 56, saleHeader);

			// find record by PK fields:
			stmt.setString(curpos, saleHeader.getPrimaryKey());

			//
			stmt.executeUpdate();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
			try {
				if (con != null) {
					((CrossDBConnection) con).setAddTimeStamp(true);
					con.close();
				}
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			}
		}
	}

	/**
	 * 更新公式值。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateLock(String id, String cfreezid) throws java.sql.SQLException {

		String sql = "update so_saleorder_b set cfreezeid = '" + cfreezid + "' where corder_bid = '" + id + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}

	public void updateOrderEnd(String[] hids, ClientLink cl) throws BusinessException {
		// //////////////1.加锁//////////////////////////////////////
		LockTool.setDynamicLockForPks(hids,null);

		// //////////////2.过滤出真正需要整单关闭的VO//////////////////
		try {
			// hids对应的HVO
			SaleorderHVO[] hvos = (SaleorderHVO[]) queryAllHeadData(hids);
			// 真正需要整单关闭的HVOs
			SaleorderHVO[] closehvos = null;
			List<SaleorderHVO> closehvos_l = new ArrayList<SaleorderHVO>();

			// 查询已经整单关闭的销售订单Hids
			HashMap endhids = nc.impl.scm.so.so016.SOToolsDMO.getAnyValue("so_sale", "csaleid", "csaleid", hids,
					" fstatus = " + nc.ui.pub.bill.BillStatus.FINISH);

			// 真正需要整单关闭的HVOs = 原始HVOs
			if (endhids == null || endhids.size() == 0)
				closehvos_l.addAll(Arrays.asList(hvos));
			else {// 过滤出真正需要整单关闭的VO
				for (SaleorderHVO hvo : hvos)
					if (!endhids.containsKey(hvo.getPrimaryKey()))
						closehvos_l.add(hvo);
			}

			if (closehvos_l.size() > 0)
				closehvos = (SaleorderHVO[]) closehvos_l.toArray(new SaleorderHVO[closehvos_l.size()]);
			else
				// 原始HVOs都已关闭
				return;

			// //////////////3.更新整单关闭状态//////////////////
			AggregatedValueObject[] agggvos = getAggVoFromHVOs(closehvos);

			// 可用量更新（操作前）
			ArrayList listatpbefore = null;
			SOATP atp = new SOATP();
			listatpbefore = atp.modifyATPBefore(agggvos);

			// 整单关闭
			closeSaleOrder(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(closehvos, "csaleid"));

			//上游订单出库关闭--自动关闭下游已审批的发货单、删除自由状态的发货单 
			processOrderOutEndForAll((SaleOrderVO[])agggvos);			
			
			// 可用量更新（操作后）
			atp.modifyATPAfter(agggvos, listatpbefore);

		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException("更新销售订单整单关闭状态失败！", e);
		}
	}

	/**
	 * 根据表体id整单关闭销售订单（不含可用量更新）
	 * 
	 * @param hids
	 */
	public void closeSaleOrder(String[] hids) throws BusinessException {
		String sql_exe = " update so_saleexecute set bifinvoicefinish = 'Y', bifreceivefinish = 'Y', "
				+ "bifinventoryfinish = 'Y', bifpayfinish = 'Y' where csaleid = ? ";

		String sql_b = "update so_saleorder_b set frowstatus = " + nc.ui.pub.bill.BillStatus.FINISH
				+ " where csaleid = ? ";

		String sql = "update so_sale set binvoicendflag = 'Y', breceiptendflag = 'Y', boutendflag = 'Y', "
				+ "bpayendflag = 'Y',btransendflag = 'Y',fstatus = " + nc.ui.pub.bill.BillStatus.FINISH
				+ " where csaleid = ? ";

		updateByIDs(sql, hids);
		updateByIDs(sql_b, hids);
		updateByIDs(sql_exe, hids);
	}

	/**
	 * 根据id更新执行UpdateSQL
	 */
	private void updateByIDs(String sql, String[] ids) throws BusinessException {
		if (ids == null || ids.length < 0)
			return;

		try {
			ArrayList alValue = new ArrayList();
			ArrayList alitem = null;
			for (int i = 0; i < ids.length; i++) {
				alitem = new ArrayList();
				alitem.add(ids[i]);
				alValue.add(alitem);
			}

			ArrayList<Integer> alType = new ArrayList<Integer>();
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_STRING));

			new SmartDMO().executeUpdateBatch(sql, alValue, alType);

		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 订单结束管理。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateOrderEnd(SaleorderBVO[] saleexecute) throws BusinessException {
		if (saleexecute == null || saleexecute.length <= 0)
			return;

		// 获取锁定pks
		ArrayList<String> lockpks = new ArrayList<String>();
		for (int i = 0, loop = saleexecute.length; i < loop; i++) {
			if (!lockpks.contains(saleexecute[i].getCsaleid()))
				lockpks.add(saleexecute[i].getCsaleid());

			if (!lockpks.contains(saleexecute[i].getCorder_bid()))
				lockpks.add(saleexecute[i].getCorder_bid());
		}

		Connection con = null;
		PreparedStatement stmt = null;
//		PKLock lock = null;

		SaleorderHVO[] oldordhvos = null;
		SaleorderBVO[] oldordbvos = null;
		try {
      // 锁定要更新的行及表头

    // 加锁,进行并发控制
    nc.itf.scm.pub.lock.LockTool.setDynamicLockForPks(lockpks.toArray(new String[0]), null);

			// 检查订单表体是否存在并发
			String[] bids = new String[saleexecute.length];
			String[] csaleids = new String[saleexecute.length];
			for (int i = 0; i < saleexecute.length; i++) {
				bids[i] = saleexecute[i].getPrimaryKey();
				csaleids[i] = saleexecute[i].getCsaleid();
			}
			Hashtable hsexets = getAnyValue("so_saleexecute", "ts", "csale_bid", bids);
			Hashtable hsbts = getAnyValue("so_saleorder_b", "ts", "corder_bid", bids);
			Hashtable hsbinvs = getAnyValue("so_saleorder_b", "cinventoryid", "corder_bid", bids);
			// cinventoryid
			for (int i = 0; i < saleexecute.length; i++) {
				String bts = saleexecute[i].getTs() == null ? "" : saleexecute[i].getTs().toString();
				String exets = saleexecute[i].getExets() == null ? "" : saleexecute[i].getExets().toString();
				if (hsexets != null) {
					if (!exets.equals(hsexets.get(saleexecute[i].getPrimaryKey()))) {
						throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000015")
						/* @res "并发问题，请刷新界面再试" */);
						// throw new
						// nc.vo.pub.BusinessException("并发问题，请刷新界面再试");
					}
				}
				if (hsbts != null) {
					if (!bts.equals(hsbts.get(saleexecute[i].getPrimaryKey()))) {
						throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000015")
						/* @res "并发问题，请刷新界面再试" */);
						// throw new
						// nc.vo.pub.BusinessException("并发问题，请刷新界面再试");
					}
				}
				saleexecute[i].setCinventoryid((String) hsbinvs.get(saleexecute[i].getPrimaryKey()));
			}
			// 检查订单表头是否存在并发
			Hashtable hshts = getAnyValue("so_sale", "ts", "csaleid", csaleids);
			Map<String,SaleorderBVO> uiVo = new HashMap<String,SaleorderBVO>(); 
			for (int i = 0; i < saleexecute.length; i++) {
				uiVo.put(saleexecute[i].getCorder_bid(), saleexecute[i]);
				String hts = saleexecute[i].m_headts == null ? "" : saleexecute[i].m_headts.toString();
				if (hshts != null) {
					if (!hts.equals(hshts.get(saleexecute[i].getCsaleid()))) {
						throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000015")
						/* @res "并发问题，请刷新界面再试" */);
						// throw new
						// nc.vo.pub.BusinessException("并发问题，请刷新界面再试");
					}
				}
			}

			// 查询订单行信息
			oldordbvos = (SaleorderBVO[]) queryAllBodyDataByBIDs(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(saleexecute,
					"corder_bid"));
			if (oldordbvos == null || oldordbvos.length <= 0)
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000016")
				/* @res "查询订单行信息失败" */);

			// 查询订单头信息
			oldordhvos = (SaleorderHVO[]) queryHeadDataForUpdateStatus(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(
					saleexecute, "csaleid"));
			if (oldordhvos == null || oldordhvos.length <= 0)
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000017")
				/* @res "查询订单头信息失败" */);

			String pk_corp = oldordbvos[0].getPkcorp();
			String cbiztype = ((Object[]) new SmartDMO()
					.selectBy2("select cbiztype from so_sale,so_saleorder_b where so_saleorder_b.csaleid = so_sale.csaleid and corder_bid = '"
							+ oldordbvos[0].getPrimaryKey() + "'")[0]).toString();

			int len1 = oldordbvos.length;
			String[] bids1 = new String[len1];
			String[] biztypes = new String[len1];
			for (int i = 0; i < len1; i++) {
				bids1[i] = oldordbvos[i].getPrimaryKey();
				biztypes[i] = cbiztype;
			}

			/** 没有区分是否所有行都需要更新；统一使用了关闭动作来处理* */
			// 对信用算法造成的影响：出库关闭要加入结算关闭逻辑
			// 标准实现：提供批量调用接口 把所有要更新的行分为：结算关闭 结算打开 出库关闭 出库打开 开票关闭 开票打开...... 组织后一次性传递到后台
       /** 信用 begin* */
      ICreateCorpQueryService corpService = (ICreateCorpQueryService) nc.bs.framework.common.NCLocator
      .getInstance().lookup(ICreateCorpQueryService.class.getName());
      
      Object creditObject = null;
      SOCreditPara para = null;
      boolean creditEnabled = corpService.isEnabled(saleexecute[0].getPk_corp(), "SO6");
      if(creditEnabled){
      IBillInvokeCreditManager creditManager = (IBillInvokeCreditManager) nc.bs.framework.common.NCLocator
					.getInstance().lookup(IBillInvokeCreditManager.class.getName());
      creditObject = creditManager;
			 para = new SOCreditPara(null, bids1, CreditConst.ICREDIT_ACT_OUTCLOSE, biztypes, pk_corp,
					creditManager);
			creditManager.renovateARByBidsBegin(para);
      }
      /** 信用 begin* */
			// 处理销售订单结算关闭带来的出库关闭、开票状态的联动
			processStateLink(saleexecute);

			/**=== v5.5更新ATP(before)--可用量更新（操作前） ======================================*/
			ArrayList listatpbefore = null;
			SOATP atp = new SOATP();
			for (int i = 0 ;i<saleexecute.length;i++){
				oldordbvos[i].setBCheckATP(uiVo.get(oldordbvos[i].getCorder_bid()).isBCheckATP());
			}
			AggregatedValueObject[] atpvos = RedunVOTool.getBillVos(SaleOrderVO.class.getName(), "csaleid", oldordhvos,
					oldordbvos);
			listatpbefore = atp.modifyATPBefore(atpvos);
			/**=== v5.5更新ATP(before)--可用量更新（操作前） ======================================*/

			// 更新订单执行表及子表信息
			con = getConnection();
			String sql = " update so_saleexecute set bifinvoicefinish = ?, bifreceivefinish = ?, bifinventoryfinish = ?,bsquareendflag = ? where csale_bid = ? and csaleid = ?";
			stmt = prepareStatement(con, sql);
			updateOrderEndExeStatus(saleexecute, stmt);
			executeBatch(stmt);
			stmt.close();
			sql = "update so_saleorder_b set frowstatus = ? where csaleid = ? and corder_bid = ? ";
			stmt = prepareStatement(con, sql);
			updateOrderEndDetailStatus(saleexecute, stmt);
			executeBatch(stmt);
			stmt.close();
			con.close();

			// V5.5 订单手工结算关闭
			processOrderBalEnd(saleexecute);
			// V5.5 订单手工结算打开
			processOrderBalOpen(saleexecute);

			// v5.5上游订单出库关闭--自动关闭下游已审批的发货单、删除自由状态的发货单
			// V5.5 订单手工出库关闭可能导致订单行结算关闭
			processOrderOutEnd(saleexecute);

			// v5.5处理跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开
			processOrderOutEndForMultiCorp(oldordhvos, saleexecute);

			// V5.5 订单手工开票关闭可能导致订单行结算关闭
			processOrderInvoiceEnd(saleexecute);

			// V55 订单手工出库打开可能导致订单行结算打开
			preocessOrderOutOpen(saleexecute);

			// V55 订单手工开票打开可能导致订单行结算打开
			processOrderInvoiceOpen(saleexecute);

			// 查询更新后的订单行信息
			SaleorderBVO[] curordbvos = (SaleorderBVO[]) queryAllBodyDataByBIDs(nc.vo.so.so016.SoVoTools
					.getVOsOnlyValues(saleexecute, "corder_bid"));
			if (oldordbvos == null || oldordbvos.length <= 0)
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000016")
				/* @res "查询订单行信息失败" */);

			// 更新订单头状态
			setOrdHeadStatus(oldordhvos);

			// 更新订单核销关系
			updateOrdBalanceByOrdRows(oldordhvos, oldordbvos, curordbvos);

			updateAdjustForEnd(oldordhvos, oldordbvos, curordbvos);

			// 更新应收及合同执行数量
			updateArByOrdRows(oldordhvos, oldordbvos, curordbvos);
			
			// 处理订单关闭时，订单行的库存硬锁定
			processOrderLock(saleexecute, oldordhvos);

			/** ==== v5.5更新ATP(after)可用量更新（操作后）==========================*/
			atp.modifyATPAfter(atpvos, listatpbefore);
			/** ==== v5.5更新ATP(after)可用量更新（操作后）==========================*/
			
      /** 信用 end* */
      if(creditEnabled){
       ((nc.itf.so.so120.IBillInvokeCreditManager) creditObject).renovateARByBidsEnd(para);
      }
      /** 信用 end* */
      
		} catch (Exception e) {
			// ##################################################

			if (oldordhvos != null && oldordbvos != null && oldordhvos.length > 0 && oldordbvos.length > 0) {
				ArrayList rowlist = new ArrayList();
				for (int i = 0, loop = oldordbvos == null ? 0 : oldordbvos.length; i < loop; i++) {
					rowlist.add(oldordbvos[i]);
				}
				// 记录日志
				nc.impl.scm.so.pub.DataControlDMO datactldmo = null;
				try {
					datactldmo = new nc.impl.scm.so.pub.DataControlDMO();
				} catch (Exception exp) {
					if (exp instanceof BusinessException) {
						throw (BusinessException) exp;
					} else {
						throw new BusinessRuntimeException(exp.getMessage());
					}
				}
				SaleOrderVO[] ordvos = toSaleOrderVO(oldordhvos, rowlist);
				nc.vo.sm.log.OperatelogVO[] logvos = new nc.vo.sm.log.OperatelogVO[ordvos.length];
				// 更新应收
				String sbtnname = NCLangResOnserver.getInstance().getStrByID("common", "UC001-0000001");/* @res "保存" */
				String snodename = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000300");/*
				 * @res "订单结束管理"
				 */

				// 操作员
				String sCuser = saleexecute[0].getCoperatorid();
				if (sCuser == null || sCuser.trim().equals(""))
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
							"UPP40060301-000013")
					/* @res "操作员不能为空，不能进行相关操作！" */);
				// throw new nc.vo.pub.BusinessException("操作员不能为空，不能进行相关操作！");

				String smsg = e.getMessage();
				for (int i = 0, loop = ordvos.length; i < loop; i++) {

					logvos[i] = datactldmo.getOperLogVO(smsg, sbtnname, "WARNNING", sCuser, null, ordvos[i]);
					logvos[i].setEnterfunction(snodename);
					logvos[i].setEnterbutton(sbtnname);
				}

				datactldmo.insertBusinesslog(logvos);
			}
			// ##################################################

			// ##################################################
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
				throw new BusinessRuntimeException(e.getMessage());
			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 处理订单关闭时，订单行的库存硬锁定
	 * @param saleexecute
	 * @throws BusinessException 
	 */
	private void processOrderLock(SaleorderBVO[] saleexecute,SaleorderHVO[] oldordhvos) throws BusinessException{
		
		List<SaleorderBVO> bidoutlist = new ArrayList<SaleorderBVO>();
		for (SaleorderBVO bvo : saleexecute)
			// 过滤出库关闭的的行
			if (bvo.getBifinventoryfinish().booleanValue() && !bvo.getBifinventoryfinish_init().booleanValue())
				bidoutlist.add(bvo);
		if (bidoutlist.size() > 0) {
			// 自动解除库存硬锁定(因为表体数据不区分业务类型,所以只能单个解锁)
			IICToSO iictoso = (IICToSO) nc.bs.framework.common.NCLocator.getInstance().lookup(
					IICToSO.class.getName());
			String userid = saleexecute[0].getClientLink().getUser();
			UFDate logindate = saleexecute[0].getClientLink().getLogonDate();
			
			SaleorderBVO saleorderBVO = null;
			for (Iterator iterator = bidoutlist.iterator(); iterator.hasNext();) {
				saleorderBVO = (SaleorderBVO) iterator.next();
				userid = saleorderBVO.getClientLink().getUser();
				logindate = saleorderBVO.getClientLink().getLogonDate();
				iictoso.unLockInv(findHeadBillType(saleorderBVO.getCsaleid(), oldordhvos),
						new String[] { saleorderBVO.getPrimaryKey() }, userid, logindate);
			}
		}
	}

	/**
	 * 方法功能描述：简要描述本方法的功能。 <b>参数说明</b>
	 * 
	 * @param saleexecute
	 * @author fengjb
	 * @throws BusinessException
	 * @time 2008-10-19 下午08:38:17
	 */
	private void processOrderInvoiceOpen(SaleorderBVO[] saleexecute) throws BusinessException {

		List<String> bidinvoicelist = new ArrayList<String>();
		// 过滤开票关闭的的行
		for (SaleorderBVO bvo : saleexecute)
			if (!bvo.getBifinvoicefinish().booleanValue() && bvo.getBifinvoicefinish_init().booleanValue())
				bidinvoicelist.add(bvo.getPrimaryKey());
		if (bidinvoicelist.size() > 0) {
			String[] bidsinvocie = bidinvoicelist.toArray(new String[bidinvoicelist.size()]);

			// V55 结算关闭 订单手工出库关闭可能导致订单行结算 begin
			ClientLink cl = saleexecute[0].getClientLink();
			SaleordBalEndVO saleordbalvo = new SaleordBalEndVO(cl.getLogonDate().toString(), cl.getUser(),
					cl.getCorp(), bidsinvocie, SaleOrdBalConst.ISALEORDBAL_TRIG_32OPEN);

			ISaleOrdBalEndSrv saleordbalendsrv = (ISaleOrdBalEndSrv) nc.bs.framework.common.NCLocator.getInstance()
					.lookup(ISaleOrdBalEndSrv.class.getName());

			saleordbalendsrv.processAutoOpen(saleordbalvo);
			// end

		}
	}

	/**
	 * 方法功能描述：简要描述本方法的功能。 <b>参数说明</b>
	 * 
	 * @param saleexecute
	 * @author fengjb
	 * @throws BusinessException
	 * @time 2008-10-19 下午08:32:14
	 */
	private void preocessOrderOutOpen(SaleorderBVO[] saleexecute) throws BusinessException {

		List<String> bidoutlist = new ArrayList<String>();
		// 过滤开票关闭的的行
		for (SaleorderBVO bvo : saleexecute)
			if (!bvo.getBifinventoryfinish().booleanValue() && bvo.getBifinventoryfinish_init().booleanValue())
				bidoutlist.add(bvo.getPrimaryKey());
		if (bidoutlist.size() > 0) {
			String[] bidsout = bidoutlist.toArray(new String[bidoutlist.size()]);

			// V55 结算关闭 订单手工出库关闭可能导致订单行结算 begin
			ClientLink cl = saleexecute[0].getClientLink();
			SaleordBalEndVO saleordbalvo = new SaleordBalEndVO(cl.getLogonDate().toString(), cl.getUser(),
					cl.getCorp(), bidsout, SaleOrdBalConst.ISALEORDBAL_TRIG_ICOPEN);

			ISaleOrdBalEndSrv saleordbalendsrv = (ISaleOrdBalEndSrv) nc.bs.framework.common.NCLocator.getInstance()
					.lookup(ISaleOrdBalEndSrv.class.getName());

			saleordbalendsrv.processAutoOpen(saleordbalvo);
			// end

		}
	}

	/**
	 * 方法功能描述：V55销售结算手工关闭联动销售订单开票关闭和出库关闭状态+。 <b>参数说明</b>
	 * 
	 * @param saleexecute
	 * @author fengjb
	 * @throws BusinessException
	 * @time 2008-10-14 下午01:43:44
	 */
	private void processStateLink(SaleorderBVO[] saleexecute) throws BusinessException {
		/**
		 * 订单手工结算关闭时 如果出库单结算，订单结算关闭会导致订单出库关闭 如果发票结算，订单结算关闭会导致订单开票关闭
		 */
		ArrayList<String> saleordbid = new ArrayList<String>();
		ArrayList<String> cbiztype = new ArrayList<String>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		// 过滤手工结算关闭的的行
		for (int i = 0, loop = saleexecute.length; i < loop; i++) {

			if (saleexecute[i].getBsquareendflag().booleanValue()
					&& !saleexecute[i].getBifinvoicefinish_init().booleanValue()
					&& !(saleexecute[i].getBifinventoryfinish().booleanValue() && saleexecute[i].getBifinvoicefinish()
							.booleanValue())) {
				saleordbid.add(saleexecute[i].getPrimaryKey());
				cbiztype.add(saleexecute[i].getCbiztype());
				index.add(i);
			}
		}

		try {
//			SaleOrdBalEndDMO balenddmo = new SaleOrdBalEndDMO();
			if (saleordbid.size() > 0) {
				// 返回订单行对应的结算单据
				int[] balbilltype = getBalType(cbiztype);
				for (int i = 0, loop = saleordbid.size(); i < loop; i++) {
					if (SaleOrdBalConst.BOTHBAL == balbilltype[i]) {
						saleexecute[index.get(i)].setBifinventoryfinish(UFBoolean.TRUE);
						saleexecute[index.get(i)].setBifinvoicefinish(UFBoolean.TRUE);
					} else if (SaleOrdBalConst.OUTSTOCKBAL == balbilltype[i]) {
						saleexecute[index.get(i)].setBifinventoryfinish(UFBoolean.TRUE);
					} else if (SaleOrdBalConst.SALEINVOICEBAL == balbilltype[i]) {
						saleexecute[index.get(i)].setBifinvoicefinish(UFBoolean.TRUE);
					}
				}
			}
			/**
			 * 如果是出库单结算，则订单行出库打开，需要同时进行结算打开 如果是发票结算，则订单行开票打开，需要同时进行结算打开
			 */
			index.clear();
			saleordbid.clear();
			for (int i = 0, loop = saleexecute.length; i < loop; i++) {
				if ((!saleexecute[i].getBifinventoryfinish().booleanValue() && saleexecute[i]
						.getBifinventoryfinish_init().booleanValue())
						|| (!saleexecute[i].getBifinvoicefinish().booleanValue() && saleexecute[i]
								.getBifinvoicefinish_init().booleanValue())
						&& saleexecute[i].getBsquareendflag().booleanValue()) {
					saleordbid.add(saleexecute[i].getPrimaryKey());
					index.add(i);
				}
			}
			if (saleordbid.size() > 0) { // 返回订单行对应的结算单据
				int[] balbilltype = getBalType(saleordbid);
				for (int i = 0, loop = saleordbid.size(); i < loop; i++) {
					if (!saleexecute[index.get(i)].getBifinventoryfinish().booleanValue()) {
						if (SaleOrdBalConst.BOTHBAL == balbilltype[i] || SaleOrdBalConst.OUTSTOCKBAL == balbilltype[i])
							saleexecute[index.get(i)].setBsquareendflag(UFBoolean.FALSE);
					} else if (!saleexecute[index.get(i)].getBifinvoicefinish().booleanValue()) {
						if (SaleOrdBalConst.BOTHBAL == balbilltype[i]
								|| SaleOrdBalConst.SALEINVOICEBAL == balbilltype[i])
							saleexecute[index.get(i)].setBsquareendflag(UFBoolean.FALSE);

					}
				}
			}
			/**
			 * 如果流程为先出库后开票，订单开票关闭会导致订单行出库关闭
			 */
			index.clear();
			// 过滤开票关闭的的行
			for (int i = 0, loop = saleexecute.length; i < loop; i++) {
				if (saleexecute[i].getBifinvoicefinish().booleanValue()
						&& !saleexecute[i].getBifinvoicefinish_init().booleanValue())
					index.add(i);
			}
			if (index.size() > 0) {
				for (int i = 0, loop = index.size(); i < loop; i++) {
					if (isOutFirstFromV55(saleexecute[index.get(i)].getPk_corp(), saleexecute[index.get(i)]
							.getCbiztype())) {
						saleexecute[index.get(i)].setBifinventoryfinish(UFBoolean.TRUE);
						saleexecute[index.get(i)].setBifreceivefinish(UFBoolean.TRUE);
					}
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 方法功能描述：出入订单行ID，返回对应的结算类型。 <b>参数说明</b>
	 * 
	 * @param saleordbid
	 * @return
	 * @throws BusinessException
	 * @author fengjb
	 * @time 2008-10-19 下午07:03:48
	 */

	public int[] getBalType(ArrayList<String> biztype) throws BusinessException {

		int[] balbilltype = new int[biztype.size()];
		HashMap<String, Integer> hsbalbilltype = new HashMap<String, Integer>();
		for (int i = 0, loop = biztype.size(); i < loop; i++) {
			String cbiztype = biztype.get(i);
			if (hsbalbilltype.containsKey(cbiztype)) {
				balbilltype[i] = hsbalbilltype.get(cbiztype);
				continue;
			}

			// 获取销售发票上配置的动作
			boolean isinvoicebal = false;
			IPFMetaModel bo = (IPFMetaModel) NCLocator.getInstance().lookup(IPFMetaModel.class.getName());
			MessagedriveVO[] message = bo.queryAllMsgdrvVOs(null, "32", cbiztype, "APPROVE");

			if (message != null && message.length > 0) {
				for (MessagedriveVO mdvo : message) {
					if ("AdjustIncome".equals(mdvo.getActiontype()) || "AutoIncomeBal".equals(mdvo.getActiontype())
							|| "AutoBalance".equals(mdvo.getActiontype())
							|| "ManualIncomeBal".equals(mdvo.getActiontype())
							|| "ManualBalance".equals(mdvo.getActiontype())
							|| "AutoCostBal".equals(mdvo.getActiontype())
							|| "ManualCostBal".equals(mdvo.getActiontype()))
						isinvoicebal = true;
				}
			}
			// 获取销售出库单上配置的动作
			boolean isoutbal = false;
			message = bo.queryAllMsgdrvVOs(null, "4C", cbiztype, "SIGN");

			if (message != null && message.length > 0) {
				for (MessagedriveVO mdvo : message) {
					if ("EstimationIncome".equals(mdvo.getActiontype()) || "AutoIncomeBal".equals(mdvo.getActiontype())
							|| "AutoBalance".equals(mdvo.getActiontype())
							|| "ManualIncomeBal".equals(mdvo.getActiontype())
							|| "ManualBalance".equals(mdvo.getActiontype())
							|| "AutoCostBal".equals(mdvo.getActiontype())
							|| "ManualCostBal".equals(mdvo.getActiontype())
							|| "ManualRegister".equals(mdvo.getActiontype())
							|| "AutoRegister".equals(mdvo.getActiontype()))
						isoutbal = true;
				}
			}

			if (isinvoicebal && isoutbal) {
				balbilltype[i] = SaleOrdBalConst.BOTHBAL;
				hsbalbilltype.put(cbiztype, SaleOrdBalConst.BOTHBAL);
			} else if (isinvoicebal) {
				balbilltype[i] = SaleOrdBalConst.SALEINVOICEBAL;
				hsbalbilltype.put(cbiztype, SaleOrdBalConst.SALEINVOICEBAL);
			} else if (isoutbal) {
				balbilltype[i] = SaleOrdBalConst.OUTSTOCKBAL;
				hsbalbilltype.put(cbiztype, SaleOrdBalConst.OUTSTOCKBAL);
			} else {
				balbilltype[i] = SaleOrdBalConst.NONEBAL;
				hsbalbilltype.put(cbiztype, SaleOrdBalConst.NONEBAL);
			}
		}
		return balbilltype;
	}

	HashMap<String, Boolean> hsIsoutfirst = new HashMap<String, Boolean>();
	BusiUtil util = new BusiUtil();

	/**
	 * 方法功能描述：判断传入业务类型是否是先货后票。
	 * <b>参数说明</b>
	 * @param pk_corp
	 * @param cbiztype
	 * @return
	 * @throws BusinessException
	 * @author fengjb
	 * @time 2008-11-13 下午03:11:04
	 */
	private boolean isOutFirstFromV55(String pk_corp, String cbiztype) throws BusinessException {
		if (hsIsoutfirst.containsKey(cbiztype))
			return hsIsoutfirst.get(cbiztype);
		boolean isoutfirst = util.isOutFirstFromV55(pk_corp, cbiztype);
		hsIsoutfirst.put(cbiztype, isoutfirst);
		return isoutfirst;
	}

	/**
	 * 方法功能描述：手工结算关闭时若出库单暂估应收，则回冲。 <b>参数说明</b>
	 * 
	 * @param saleexecute
	 * @author fengjb
	 * @throws BusinessException
	 * @time 2008-10-14 上午09:37:44
	 */
	private void processOrderBalEnd(SaleorderBVO[] saleexecute) throws BusinessException {

		ArrayList<String> bidballist = new ArrayList<String>();

		// 过滤手工结算关闭的的行
		for (SaleorderBVO bvo : saleexecute)
			if (bvo.getBsquareendflag().booleanValue() && !bvo.getBifinvoicefinish_init().booleanValue()) {
				bidballist.add(bvo.getPrimaryKey());
			}
		if (bidballist.size() > 0) {
			String[] bidsbal = bidballist.toArray(new String[bidballist.size()]);
			// V55 结算关闭 begin
			ClientLink cl = saleexecute[0].getClientLink();
			// 出库单暂估应收时，回冲
			try {
				new SquareAdjust().process4CAdjustFor30BalEnd(bidsbal, cl.getUser(), cl.getLogonDate().toString(), cl
						.getCorp());
			} catch (Exception e) {
				SCMEnv.out(e);
			} 
			// end

		}

	}

	/**
	 * 方法功能描述：手工结算打开时若出库单暂估应收，则应收反回冲。 <b>参数说明</b>
	 * 
	 * @param saleexecute
	 * @author fengjb
	 * @throws BusinessException
	 * @time 2008-10-14 上午09:37:44
	 */
	private void processOrderBalOpen(SaleorderBVO[] saleexecute) throws BusinessException {

		ArrayList<String> bidbalopenlist = new ArrayList<String>();

		// 过滤手工结算关闭的的行
		for (SaleorderBVO bvo : saleexecute)
			if (!bvo.getBsquareendflag().booleanValue() && bvo.getBifinvoicefinish_init().booleanValue()) {
				bidbalopenlist.add(bvo.getPrimaryKey());
			}
		if (bidbalopenlist.size() > 0) {
			String[] bidsbal = bidbalopenlist.toArray(new String[bidbalopenlist.size()]);
			// V55 结算关闭 begin
			ClientLink cl = saleexecute[0].getClientLink();
			// 出库单暂估应收时，回冲
			try {
				new SquareAdjust().processUN4CAdjustFor30BalEnd(bidsbal, cl.getCorp());
			} catch (Exception e) {
				SCMEnv.out(e);
			} 
			// end

		}

	}

	/**
	 * 方法功能描述：V55订单手工开票关闭时可能导致订单结算关闭。 <b>参数说明</b>
	 * 
	 * @param saleexecute
	 * @author fengjb
	 * @throws BusinessException
	 * @time 2008-10-14 上午09:10:52
	 */
	private void processOrderInvoiceEnd(SaleorderBVO[] saleexecute) throws BusinessException {

		List<String> bidinvoicelist = new ArrayList<String>();
		// 过滤开票关闭的的行
		for (SaleorderBVO bvo : saleexecute)
			if (bvo.getBifinvoicefinish().booleanValue() && !bvo.getBifinvoicefinish_init().booleanValue())
				bidinvoicelist.add(bvo.getPrimaryKey());
		if (bidinvoicelist.size() > 0) {
			String[] bidsinvoice = bidinvoicelist.toArray(new String[bidinvoicelist.size()]);

			// V55 结算关闭 订单手工出库关闭可能导致订单行结算 begin
			ClientLink cl = saleexecute[0].getClientLink();
			SaleordBalEndVO saleordbalvo = new SaleordBalEndVO(cl.getLogonDate().toString(), cl.getUser(),
					cl.getCorp(), bidsinvoice, SaleOrdBalConst.ISALEORDBAL_TRIG_32CLOSE);

			ISaleOrdBalEndSrv saleordbalendsrv = (ISaleOrdBalEndSrv) nc.bs.framework.common.NCLocator.getInstance()
					.lookup(ISaleOrdBalEndSrv.class.getName());

			saleordbalendsrv.processAutoClose(saleordbalvo);
			// end

		}
	}

	/**
	 * v5.5上游订单出库关闭--自动关闭下游已审批的发货单、删除自由状态的发货单
	 */
	private void processOrderOutEnd(SaleorderBVO[] saleexecute) throws BusinessException {
		List<String> bidoutlist = new ArrayList<String>();
		for (SaleorderBVO bvo : saleexecute)
			// 过滤出库关闭的的行
			if (bvo.getBifinventoryfinish().booleanValue() && !bvo.getBifinventoryfinish_init().booleanValue())
				bidoutlist.add(bvo.getPrimaryKey());
		if (bidoutlist.size() > 0) {
			String[] bidsout = bidoutlist.toArray(new String[bidoutlist.size()]);
			// 调用接口
			IReceiveService rsbo = (IReceiveService) NCLocator.getInstance().lookup(IReceiveService.class.getName());
			rsbo.processOrderOutEnd(bidsout);
			// V55 结算关闭 订单手工出库关闭可能导致订单行结算 begin
			ClientLink cl = saleexecute[0].getClientLink();
			SaleordBalEndVO saleordbalvo = new SaleordBalEndVO(cl.getLogonDate().toString(), cl.getUser(),
					cl.getCorp(), bidsout, SaleOrdBalConst.ISALEORDBAL_TRIG_ICCLOSE);

			ISaleOrdBalEndSrv saleordbalendsrv = (ISaleOrdBalEndSrv) nc.bs.framework.common.NCLocator.getInstance()
					.lookup(ISaleOrdBalEndSrv.class.getName());

			saleordbalendsrv.processAutoClose(saleordbalvo);
			// end

		}

	}
	
	/**
	 * N_30_OrderFinish脚本调用
	 * v5.5上游订单出库关闭--自动关闭下游已审批的发货单、删除自由状态的发货单
	 */
	public void processOrderOutEndForAll(SaleOrderVO[] svos) throws BusinessException {
		
		List<SaleorderBVO> list = new ArrayList<SaleorderBVO>();
		for (SaleOrderVO svo : svos )
			list.addAll(Arrays.asList(svo.getBodyVOs()));
		
		// 调用接口
		IReceiveService rsbo = (IReceiveService) NCLocator.getInstance().lookup(IReceiveService.class.getName());
		rsbo.processOrderOutEnd(SoVoTools.getVOsOnlyValues(
				list.toArray(new SaleorderBVO[list.size()]),"corder_bid"));
	}

	/**
	 * v5.5处理跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开
	 */
	private void processOrderOutEndForMultiCorp(SaleorderHVO[] oldordhvos, SaleorderBVO[] saleexecute)
			throws BusinessException {
		List<SaleorderBVO> bvooutlist = new ArrayList<SaleorderBVO>();
		// 过滤出库状态发生变化的行
		for (SaleorderBVO bvo : saleexecute)
			if ( bvo.getBifinventoryfinish().booleanValue() != bvo.getBifinventoryfinish_init().booleanValue())
				bvooutlist.add(bvo);
		if (bvooutlist.size() > 0)
			set5XOutEndFlag(toSaleOrderVO(oldordhvos, bvooutlist));
	}

	/**
	 * 查询表头的业务类型<br>
	 * 库存硬解锁时调用<br>
	 * 在之前的查询中默认表头和表体是相互匹配的，所以不做空检查<br>
	 * <br>
	 * 
	 * @param csaleid
	 * @param heads
	 * @return
	 */
	private String findHeadBillType(String csaleid, SaleorderHVO[] heads) {
		for (SaleorderHVO head : heads) {
			if (head.getCsaleid().equals(csaleid)) {
				return head.getCreceipttype();
			}
		}
		// it should never happen
		return null;
	}

	/**
	 * 订单结束管理。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateOrderEnd(SaleOrderVO vo) throws BusinessException {

		if (vo == null || vo.getParentVO() == null)
			return;

		Connection con = null;

		PreparedStatement stmt = null;
		try {

			BusinessControlDMO busidmo = new BusinessControlDMO();

			String csaleid = ((SaleorderHVO) vo.getParentVO()).getCsaleid();
			String cbilltype = ((SaleorderHVO) vo.getParentVO()).getCreceipttype();

			busidmo.setBillFinish(csaleid, cbilltype);

			con = getConnection();
			String sql = " update so_saleexecute set bifinvoicefinish = 'Y', bifreceivefinish = 'Y', bifinventoryfinish = 'Y', bifpayfinish = 'Y' where csaleid = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, csaleid);
			stmt.executeUpdate();
			stmt.close();

			con.close();

		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
				throw new BusinessRuntimeException(e.getMessage());
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 订单结束管理。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	private int updateOrderEndDetailStatus(SaleorderBVO[] saleexecute, PreparedStatement stmt)
			throws nc.vo.pub.BusinessException, nc.bs.pub.SystemException, javax.naming.NamingException,
			java.sql.SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderDetailStatus", new Object[] { saleexecute });
		/** ********************************************************** */

		if (saleexecute == null || saleexecute.length <= 0)
			throw new nc.vo.pub.BusinessException("SaleorderBVO[] is null ");

		ArrayList updataline = new ArrayList();
		for (int i = 0; i < saleexecute.length; i++) {
			if (
			// saleexecute[i].getBifpayfinish().booleanValue()&&
			saleexecute[i].getBifinventoryfinish().booleanValue()
					&& saleexecute[i].getBifreceivefinish().booleanValue()
					&& saleexecute[i].getBifinvoicefinish().booleanValue()
					&& saleexecute[i].getBsquareendflag().booleanValue()) {

				updataline.add(saleexecute[i]);

			} else if (
			// !saleexecute[i].getBifpayfinish().booleanValue()||
			!saleexecute[i].getBifinventoryfinish().booleanValue()
					|| !saleexecute[i].getBifreceivefinish().booleanValue()
					|| !saleexecute[i].getBifinvoicefinish().booleanValue()
					|| !saleexecute[i].getBsquareendflag().booleanValue()) {

				updataline.add(saleexecute[i]);
			}
		}

		if (updataline.size() <= 0)
			return 0;

		if (stmt == null)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000019")
			/* @res "执行语句为空" */);
		// throw new java.sql.SQLException("执行语句为空");

		int updatelinecount = 0;
		try {

			for (int i = 0; i < updataline.size(); i++) {

				// update non PK fields:
				SaleorderBVO updateli = (SaleorderBVO) updataline.get(i);

				if (
				// updateli.getBifpayfinish().booleanValue() &&
				updateli.getBifinventoryfinish().booleanValue() && updateli.getBifreceivefinish().booleanValue()
						&& updateli.getBifinvoicefinish().booleanValue() && updateli.getBsquareendflag().booleanValue()) {

					updateli.setStatus(BillStatus.FINISH);
					stmt.setInt(1, BillStatus.FINISH);

				} else if (
				// !updateli.getBifpayfinish().booleanValue() ||
				!updateli.getBifinventoryfinish().booleanValue() || !updateli.getBifreceivefinish().booleanValue()
						|| !updateli.getBifinvoicefinish().booleanValue()
						|| !updateli.getBsquareendflag().booleanValue()) {

					updateli.setStatus(BillStatus.AUDIT);
					stmt.setInt(1, nc.ui.pub.bill.BillStatus.AUDIT);
				}

				// find record by PK fields:
				stmt.setString(2, updateli.getCsaleid());
				// find record by PK fields:
				stmt.setString(3, updateli.getPrimaryKey());

				executeUpdate(stmt);
				updatelinecount++;
			}

		} finally {

		}

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderDetailStatus", new Object[] { saleexecute });
		/** ********************************************************** */

		return updatelinecount;

	}

	/**
	 * 订单结束管理。
	 * 
	 * 创建日期：(2001-6-14)
	 * 
	 * @param saleexecute
	 *            nc.vo.pub.bill.SaleexecuteVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	private int updateOrderEndExeStatus(SaleorderBVO[] saleexecute, PreparedStatement stmt)
			throws nc.vo.pub.BusinessException, nc.bs.pub.SystemException, javax.naming.NamingException,
			java.sql.SQLException {
		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderExeStatus", new Object[] { saleexecute });
		/** ********************************************************** */

		if (saleexecute == null || saleexecute.length <= 0)
			throw new nc.vo.pub.BusinessException(" SaleorderBVO[] is null ");

		if (stmt == null)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000019")
			/* @res "执行语句为空" */);
		// throw new java.sql.SQLException("执行语句为空");

		int updatelinecount = 0;

		try {

			// stmt = prepareStatement(con,sql);
			for (int i = 0; i < saleexecute.length; i++) {
				// update non PK fields:
				if (saleexecute[i].getBifinvoicefinish() == null) {
					stmt.setNull(1, Types.CHAR);
				} else {
					stmt.setString(1, saleexecute[i].getBifinvoicefinish().toString());
				}
				if (saleexecute[i].getBifreceivefinish() == null) {

					stmt.setNull(2, Types.CHAR);
				} else {
					stmt.setString(2, saleexecute[i].getBifreceivefinish().toString());
				}
				if (saleexecute[i].getBifinventoryfinish() == null) {
					stmt.setNull(3, Types.CHAR);
				} else {
					stmt.setString(3, saleexecute[i].getBifinventoryfinish().toString());
				}
				if (saleexecute[i].getBsquareendflag() == null) {
					stmt.setNull(4, Types.CHAR);
				} else {
					stmt.setString(4, saleexecute[i].getBsquareendflag().toString());
				}
				// if (saleexecute[i].getBifpayfinish() == null) {
				// stmt.setNull(4, Types.CHAR);
				// } else {
				// stmt.setString(4, saleexecute[i].getBifpayfinish()
				// .toString());
				// }
				// find record by PK fields:
				stmt.setString(5, saleexecute[i].getPrimaryKey());
				// find record by PK fields:
				stmt.setString(6, saleexecute[i].getCsaleid());

				executeUpdate(stmt);
				updatelinecount++;
			}

		} finally {

		}

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.pub.bill.SaleOrderDMO", "updateOrderExeStatus", new Object[] { saleexecute });
		/** ********************************************************** */

		return updatelinecount;

	}

	/**
	 * 更新公式值。
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateRetinvFlag(String id) throws java.sql.SQLException {

		/** ********************************************************** */
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.so.so001.SaleOrderDMO", "updateRetinvFlag", new Object[] { id });
		/** ********************************************************** */

		String sql = "update so_sale set bretinvflag = 'Y' where csaleid = '" + id + "'";

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "updateRetinvFlag", new Object[] { id });
		/** ********************************************************** */
	}

	/**
	 * 更改单据状态。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateStatus(SaleorderHVO saleHeader) throws java.sql.SQLException {

		String sql = "update so_sale set fstatus = ? where csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			// update non PK fields:
			if (saleHeader.getFstatus() == null) {
				stmt.setNull(1, Types.INTEGER);
			} else {
				stmt.setInt(1, saleHeader.getFstatus().intValue());
			}
			// find record by PK fields:
			stmt.setString(2, saleHeader.getPrimaryKey());
			//
			stmt.executeUpdate();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-11 15:58:18)
	 * 
	 * @param ordid
	 *            java.lang.String
	 */
	public void approvedOrdByID(String ordid) throws Exception {
		if (ordid == null)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000022")
			/* @res "订单ID为空，不能审批！" */);
		// throw new nc.vo.pub.BusinessException("订单ID为空，不能审批！");

		SaleOrderVO ordvo = queryData(ordid);
		if (ordvo == null)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000023")
			/* @res "订单为空，不能审批！" */);
		// throw new nc.vo.pub.BusinessException("订单为空，不能审批！");

		SaleorderHVO ordhvo = (SaleorderHVO) ordvo.getParentVO();
		ordhvo.setCapproveid(ordhvo.getCoperatorid());
		ordhvo.setDapprovedate(ordhvo.getDmakedate());

		nc.bs.pub.pf.PfUtilBO pfbo = null;
		try {

			pfbo = new nc.bs.pub.pf.PfUtilBO();
			pfbo.processAction("APPROVE", "30", ordhvo.getDmakedate().toString(), null, ordvo, null);

		} finally {
		}
	}
	
	/**
	 * 销售订单推式保存前数据校验
	 * @param vo
	 * @throws BusinessException 
	 */
	public void prepareForPushSave(nc.vo.pub.AggregatedValueObject vo) throws BusinessException{
		/**===1.数据校验===========================================*/
		checkVOForPushSave(vo);
		
		/**===2.保存前初始化数据===================================*/
		initVOForPushSave(vo);
	}
	
	/**
	 * 销售订单推式保存前数据校验
	 * @throws BusinessException 
	 */
	public void checkVOForPushSave(nc.vo.pub.AggregatedValueObject vo) throws BusinessException{
		
		if (vo == null)
			return;

		if (!(vo instanceof nc.vo.so.so001.SaleOrderVO))
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000028"));
		
		SaleOrderVO svo = (SaleOrderVO)vo;
		
		try {

			checkSaleOrderVO(svo);

			SaleorderHVO ordhvo = (SaleorderHVO) vo.getParentVO();
			SaleorderBVO[] ordbvos = (SaleorderBVO[]) vo.getChildrenVO();

			if (ordbvos == null || ordbvos.length <= 0)
				throw new nc.vo.pub.BusinessException(NCLangResOnserver
						.getInstance().getStrByID("40060301",
								"UPP40060301-000029"));

			if (ordhvo.getPk_corp() == null)
				throw new nc.vo.pub.BusinessException(NCLangResOnserver
						.getInstance().getStrByID("40060301",
								"UPP40060301-000031"));

			UFDate dbilldate = ordhvo.getDbilldate();
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {

				// 订单的发货日期小于订单日期
				UFDate dconsigndate = ordbvos[i].getDconsigndate();
				UFDate deliverdate = ordbvos[i].getDdeliverdate();
				if (dconsigndate == null
						|| dconsigndate.toString().length() == 0
						|| deliverdate == null
						|| deliverdate.toString().length() == 0) {
					throw new nc.vo.pub.BusinessException(NCLangResOnserver
							.getInstance().getStrByID("40060301",
									"UPP40060301-000032")
					/* @res "发货日期、交货日期不能为空!" */);
					// throw new nc.vo.pub.BusinessException("发货日期、交货日期不能为空!");
				}
				if (dconsigndate != null && dbilldate != null) {
					if (dbilldate.after(dconsigndate)
							&& dbilldate != dconsigndate)
						throw new nc.vo.pub.BusinessException(NCLangResOnserver
								.getInstance().getStrByID("40060301",
										"UPP40060301-000033")
						/* @res "发货日期应大于等于单据日期!" */);
					// throw new nc.vo.pub.BusinessException("发货日期应大于等于单据日期!");
				}
				if (dconsigndate != null && deliverdate != null) {
					if (dconsigndate.after(deliverdate)
							&& deliverdate != dconsigndate)
						throw new nc.vo.pub.BusinessException(NCLangResOnserver
								.getInstance().getStrByID("40060301",
										"UPP40060301-000034")
						/* @res "交货日期应大于等于发货日期!" */);
					// throw new nc.vo.pub.BusinessException("交货日期应大于等于发货日期!");
				}

			}

		} catch (SystemException e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		} catch (NamingException e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		} catch (SQLException e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
        
	}
	
	/**
	 * 销售订单推式保存前初始化数据
	 * @throws BusinessException 
	 */
	public void initVOForPushSave(nc.vo.pub.AggregatedValueObject vo) throws BusinessException{
		SaleOrderVO svo = (SaleOrderVO)vo;
		SaleorderHVO ordhvo = svo.getHeadVO();
		SaleorderBVO[] ordbvos = svo.getBodyVOs();
		
		//聚合VO状态
		svo.setStatus(VOStatus.NEW);
		svo.setIAction(ISaleOrderAction.A_ADD);
		
		// 设置表头信息
		ordhvo.setStatus(VOStatus.NEW);
		ordhvo.setFstatus(nc.ui.pub.bill.BillStatus.FREE);
		ordhvo.setBinitflag(UFBoolean.FALSE);
		ordhvo.setBretinvflag(UFBoolean.FALSE);
		ordhvo.setBinvoicendflag(UFBoolean.FALSE);
		ordhvo.setBoutendflag(UFBoolean.FALSE);
		ordhvo.setBpayendflag(UFBoolean.FALSE);
		ordhvo.setBreceiptendflag(UFBoolean.FALSE);
		ordhvo.setBtransendflag(UFBoolean.FALSE);
		ordhvo.setCapproveid(null);
		if (ordhvo.getCcalbodyid() == null)//表头库存组织
			ordhvo.setCcalbodyid(ordbvos[0].getCadvisecalbodyid());
		
		// 设置表体信息
		try {
			
			processSaleOrderVO(svo);
			
			UFDate dbilldate = ordhvo.getDbilldate();
			String creceiptcorpid = ordhvo.getCreceiptcorpid();
			String vreceiveaddress = ordhvo.getVreceiveaddress();

			String[] deskeys = new String[] { "cquoteunitid", // 报价计量单位id
					"nquoteunitnum", // 报价单位数量
					
					"norgqttaxprc", // 报价单位含税单价
					"norgqtprc", // 报价单位无税单价
					"norgqttaxnetprc", // 报价单位含税净价
					"norgqtnetprc", // 报价单位无税净价

					"nqttaxnetprc", // 报价单位本币含税净价
					"nqtnetprc",// 报价单位本币无税净价
					"nqttaxprc",// 报价单位本币含税单价
					"nqtprc"// 报价单位本币无税单价
			};
			String[] sourcekeys = new String[] {
			"cunitid", "nnumber",
			"noriginalcurtaxprice", "noriginalcurprice", "noriginalcurtaxnetprice", "noriginalcurnetprice",
			"ntaxnetprice", "nnetprice", "ntaxprice", "nprice" };

			String ccalbodyid_h = null;

			for (int i = 0, loop = ordbvos.length; i < loop; i++) {
				
				ordbvos[i].setFrowstatus(1);
				ordbvos[i].setStatus(VOStatus.NEW);
				
				if (ordbvos[i].getNorgqtprc() == null && ordbvos[i].getNorgqttaxprc() == null
						&& ordbvos[i].getNquoteunitnum() == null) {
					nc.vo.so.so016.SoVoTools.copyVOByVO(ordbvos[i], deskeys, ordbvos[i], sourcekeys);
				}

				if (ordbvos[i].getNitemdiscountrate() == null)
					ordbvos[i].setNitemdiscountrate(new UFDouble(100));
				if (ordbvos[i].getNdiscountrate() == null)
					ordbvos[i].setNdiscountrate(new UFDouble(100));

				if (ordbvos[i].getDconsigndate() == null)
					ordbvos[i].setDconsigndate(dbilldate);

				if (ordbvos[i].getDdeliverdate() == null)
					ordbvos[i].setDdeliverdate(dbilldate);

				if (ordbvos[i].getPkcorp() == null)
					ordbvos[i].setPkcorp(ordhvo.getPk_corp());

				// 处理状态
				if (ordbvos[i].getDiscountflag() == null)
					ordbvos[i].setDiscountflag(new UFBoolean(false));
				if (ordbvos[i].getLaborflag() == null)
					ordbvos[i].setLaborflag(new UFBoolean(false));

				if (ordbvos[i].getIsappendant() == null)
					ordbvos[i].setIsappendant(new UFBoolean(false));

				if (ordbvos[i].getBoosflag() == null)
					ordbvos[i].setBoosflag(new UFBoolean(false));
				if (ordbvos[i].getBsupplyflag() == null)
					ordbvos[i].setBoosflag(new UFBoolean(false));

				ordbvos[i].setBifinventoryfinish(new UFBoolean(false));
				ordbvos[i].setBifpayfinish(new UFBoolean(false));
				ordbvos[i].setBifreceivefinish(new UFBoolean(false));
				ordbvos[i].setBifinventoryfinish(new UFBoolean(false));
				ordbvos[i].setBiftransfinish(new UFBoolean(false));

				if (ordbvos[i].getCreceiptcorpid() == null)
					ordbvos[i].setCreceiptcorpid(creceiptcorpid);

				if (ordbvos[i].getVreceiveaddress() == null)
					ordbvos[i].setVreceiveaddress(vreceiveaddress);

				if (ccalbodyid_h == null) {
					if (!SoVoTools.isEmptyString(ordbvos[i].getCreccalbodyid())) {
						ccalbodyid_h = ordbvos[i].getCreccalbodyid();
					} else {
						if (!SoVoTools.isEmptyString(ordbvos[i].getCadvisecalbodyid()))
							ccalbodyid_h = ordbvos[i].getCadvisecalbodyid();
					}
				}

			}

			String oldcalbodyid = ordhvo.getCcalbodyid();
			ordhvo.setCcalbodyid(ccalbodyid_h);
			if (SoVoTools.isEmptyString(ordhvo.getCcalbodyid())) {
				ordhvo.setCcalbodyid(oldcalbodyid);
			}

			ordhvo.setNdiscountrate(ordbvos[0].getNdiscountrate());
			if (ordhvo.getNdiscountrate() == null)
				ordhvo.setNdiscountrate(new UFDouble(100));
			
		} catch (SystemException e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		} catch (NamingException e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		} catch (SQLException e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	public void checkVOForPreOrd(nc.vo.pub.AggregatedValueObject vo) throws javax.naming.NamingException,
			nc.vo.pub.BusinessException, java.sql.SQLException, nc.bs.pub.SystemException {
		if (vo == null)
			return;
		if (!(vo instanceof nc.vo.so.so001.SaleOrderVO))
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000028")
			/* @res "预订单推式生成销售订单类型异常!" */);
		// throw new nc.vo.pub.BusinessException("预订单推式生成销售订单类型异常！");
		SaleOrderVO inordvo = (SaleOrderVO) vo;

		inordvo.setStatus(VOStatus.NEW);
		// 设置更新VO动作
		inordvo.setIAction(ISaleOrderAction.A_ADD);

		SaleorderHVO ordhvo = (SaleorderHVO) vo.getParentVO();
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) vo.getChildrenVO();

		if (ordbvos == null || ordbvos.length <= 0)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000029")
			/* @res "预订单推式生成销售订单类型异常,表体不能为空!" */);
		// /throw new nc.vo.pub.BusinessException("预订单推式生成销售订单类型异常,表体不能为空！");

		/** v5.5 去掉 */
		// 获取多公司订单行
		/*
		 * ArrayList othervolist = new ArrayList(); for (int i = 0, loop =
		 * ordbvos.length; i < loop; i++) { if (ordbvos[i].getCconsigncorpid() !=
		 * null && ordbvos[i].getCconsigncorpid().trim().length() > 0) { if
		 * (!ordbvos[i].getCconsigncorpid().equals(ordhvo.getPk_corp()))
		 * othervolist.add(ordbvos[i]); } } SaleorderBVO[] otherordbvos = null;
		 * if (othervolist.size() > 0) otherordbvos = (SaleorderBVO[])
		 * othervolist .toArray(new SaleorderBVO[othervolist.size()]);
		 */

		try {

			/** v5.5 去掉 */
			/*
			 * nc.impl.scm.so.pub.FetchValueDMO fetdmo = new
			 * nc.impl.scm.so.pub.FetchValueDMO(); // 如果是多公司订单行，设置收货库存组织信息 if
			 * (otherordbvos != null) { String crevcalbodyid = null; String
			 * defaultcalbodyid = null;
			 * 
			 * defaultcalbodyid = fetdmo .getColValue( "bd_calbody",
			 * "pk_calbody", " pk_corp='" + ordhvo.getPk_corp() + "' and (
			 * sealflag is null or sealflag = 'N' ) and ( property = 0 or
			 * property = 1) "); if (defaultcalbodyid == null ||
			 * defaultcalbodyid.trim().length() <= 0) { throw new
			 * nc.vo.pub.BusinessException(NCLangResOnserver
			 * .getInstance().getStrByID("40060301", "UPP40060301-000030") @res
			 * "销售公司未分配库存组织" ); // throw new
			 * nc.vo.pub.BusinessException("销售公司未分配库存组织"); } HashMap
			 * revcalbodyhs = nc.impl.scm.so.so016.SOToolsDMO
			 * .getAnyValue("bd_produce", "pk_calbody", "pk_invmandoc",
			 * nc.vo.so.so016.SoVoTools .getVOsOnlyValues(otherordbvos,
			 * "cinventoryid"), null); for (int i = 0, loop =
			 * otherordbvos.length; i < loop; i++) { if (revcalbodyhs != null) {
			 * crevcalbodyid = (String) revcalbodyhs
			 * .get(otherordbvos[i].getCinventoryid()); } if (crevcalbodyid !=
			 * null && crevcalbodyid.trim().length() > 0)
			 * otherordbvos[i].setCreccalbodyid(crevcalbodyid); else
			 * otherordbvos[i].setCreccalbodyid(defaultcalbodyid); } }
			 */

			// 设置表头状态
			ordhvo.setFstatus(new Integer(1));
			ordhvo.setBinitflag(new UFBoolean(false));
			ordhvo.setBretinvflag(new UFBoolean(false));
			ordhvo.setBinvoicendflag(new UFBoolean(false));
			ordhvo.setBoutendflag(new UFBoolean(false));
			ordhvo.setBpayendflag(new UFBoolean(false));
			ordhvo.setBreceiptendflag(new UFBoolean(false));
			ordhvo.setBtransendflag(new UFBoolean(false));

			if (ordhvo.getCcalbodyid() == null)
				ordhvo.setCcalbodyid(ordbvos[0].getCadvisecalbodyid());

			if (ordhvo.getPk_corp() == null)
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000031")
				/* @res "预订单推式生成销售订单类型异常,公司不能为空！" */);
			// throw new nc.vo.pub.BusinessException("预订单推式生成销售订单类型异常,公司不能为空！");

			ordhvo.setStatus(VOStatus.NEW);
			ordhvo.setFstatus(new Integer(BillStatus.FREE));
			ordhvo.setBretinvflag(new UFBoolean(false));

			processSaleOrderVO(inordvo);

			UFDate dbilldate = ordhvo.getDbilldate();

			String creceiptcorpid = ordhvo.getCreceiptcorpid();
			String vreceiveaddress = ordhvo.getVreceiveaddress();

			// String ccurrencytypeid=
			// (String)ordhvo.getAttributeValue("ccurrencytypeid");
			// UFDouble
			// nexchangeotoarate=(UFDouble)ordhvo.getAttributeValue("nexchangeotoarate");
			// UFDouble
			// nexchangeotobrate=(UFDouble)ordhvo.getAttributeValue("nexchangeotobrate");

			ordhvo.setCapproveid(null);

			String[] deskeys = new String[] { "cquoteunitid", // 报价计量单位id
					"nquoteunitnum", // 报价单位数量

					"norgqttaxprc", // 报价单位含税单价
					"norgqtprc", // 报价单位无税单价
					"norgqttaxnetprc", // 报价单位含税净价
					"norgqtnetprc", // 报价单位无税净价

					"nqttaxnetprc", // 报价单位本币含税净价
					"nqtnetprc",// 报价单位本币无税净价
					"nqttaxprc",// 报价单位本币含税单价
					"nqtprc"// 报价单位本币无税单价
			};
			String[] sourcekeys = new String[] {

			"cunitid", "nnumber",

			"noriginalcurtaxprice", "noriginalcurprice", "noriginalcurtaxnetprice", "noriginalcurnetprice",

			"ntaxnetprice", "nnetprice", "ntaxprice", "nprice" };

			String ccalbodyid_h = null;

			for (int i = 0, loop = ordbvos.length; i < loop; i++) {

				if (ordbvos[i].getNorgqtprc() == null && ordbvos[i].getNorgqttaxprc() == null
						&& ordbvos[i].getNquoteunitnum() == null) {
					nc.vo.so.so016.SoVoTools.copyVOByVO(ordbvos[i], deskeys, ordbvos[i], sourcekeys);
				}

				if (ordbvos[i].getNitemdiscountrate() == null)
					ordbvos[i].setNitemdiscountrate(new UFDouble(100));
				if (ordbvos[i].getNdiscountrate() == null)
					ordbvos[i].setNdiscountrate(new UFDouble(100));

				if (ordbvos[i].getDconsigndate() == null)
					ordbvos[i].setDconsigndate(dbilldate);

				if (ordbvos[i].getDdeliverdate() == null)
					ordbvos[i].setDdeliverdate(dbilldate);

				if (ordbvos[i].getPkcorp() == null)
					ordbvos[i].setPkcorp(ordhvo.getPk_corp());

				// 处理状态
				if (ordbvos[i].getDiscountflag() == null)
					ordbvos[i].setDiscountflag(new UFBoolean(false));
				if (ordbvos[i].getLaborflag() == null)
					ordbvos[i].setLaborflag(new UFBoolean(false));

				if (ordbvos[i].getIsappendant() == null)
					ordbvos[i].setIsappendant(new UFBoolean(false));

				if (ordbvos[i].getBoosflag() == null)
					ordbvos[i].setBoosflag(new UFBoolean(false));
				if (ordbvos[i].getBsupplyflag() == null)
					ordbvos[i].setBoosflag(new UFBoolean(false));

				ordbvos[i].setBifinventoryfinish(new UFBoolean(false));
				ordbvos[i].setBifpayfinish(new UFBoolean(false));
				ordbvos[i].setBifreceivefinish(new UFBoolean(false));
				ordbvos[i].setBifinventoryfinish(new UFBoolean(false));
				ordbvos[i].setBiftransfinish(new UFBoolean(false));

				ordbvos[i].setFrowstatus(new Integer(1));
				ordbvos[i].setStatus(VOStatus.NEW);

				// ordbvos[i].setCrowno(""+i);
				// if(ordbvos[i].getCcurrencytypeid()==null)
				// ordbvos[i].setCcurrencytypeid(ccurrencytypeid);
				// ordbvos[i].setNexchangeotoarate(nexchangeotoarate);
				// ordbvos[i].setNexchangeotobrate(nexchangeotobrate);

				if (ordbvos[i].getCreceiptcorpid() == null)
					ordbvos[i].setCreceiptcorpid(creceiptcorpid);

				if (ordbvos[i].getVreceiveaddress() == null)
					ordbvos[i].setVreceiveaddress(vreceiveaddress);

				// 订单的发货日期小于订单日期
				UFDate dconsigndate = ordbvos[i].getDconsigndate();
				UFDate deliverdate = ordbvos[i].getDdeliverdate();
				if (dconsigndate == null || dconsigndate.toString().length() == 0 || deliverdate == null
						|| deliverdate.toString().length() == 0) {
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
							"UPP40060301-000032")
					/* @res "发货日期、交货日期不能为空!" */);
					// throw new nc.vo.pub.BusinessException("发货日期、交货日期不能为空!");
				}
				if (dconsigndate != null && dbilldate != null) {
					if (dbilldate.after(dconsigndate) && dbilldate != dconsigndate)
						throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000033")
						/* @res "发货日期应大于等于单据日期!" */);
					// throw new nc.vo.pub.BusinessException("发货日期应大于等于单据日期!");
				}
				if (dconsigndate != null && deliverdate != null) {
					if (dconsigndate.after(deliverdate) && deliverdate != dconsigndate)
						throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000034")
						/* @res "交货日期应大于等于发货日期!" */);
					// throw new nc.vo.pub.BusinessException("交货日期应大于等于发货日期!");
				}

				if (ccalbodyid_h == null) {
					if (!SoVoTools.isEmptyString(ordbvos[i].getCreccalbodyid())) {
						ccalbodyid_h = ordbvos[i].getCreccalbodyid();
					} else {
						if (!SoVoTools.isEmptyString(ordbvos[i].getCadvisecalbodyid()))
							ccalbodyid_h = ordbvos[i].getCadvisecalbodyid();
					}
				}

			}

			String oldcalbodyid = ordhvo.getCcalbodyid();
			ordhvo.setCcalbodyid(ccalbodyid_h);
			if (SoVoTools.isEmptyString(ordhvo.getCcalbodyid())) {
				ordhvo.setCcalbodyid(oldcalbodyid);
			}

			ordhvo.setNdiscountrate(ordbvos[0].getNdiscountrate());
			if (ordhvo.getNdiscountrate() == null)
				ordhvo.setNdiscountrate(new UFDouble(100));

			// inordvo.validate();

			checkSaleOrderVO(inordvo);

			/*
			 * for(int i=0,loop=ordbvos.length;i <loop;i++){
			 * ordbvos[i].validate(); }
			 */

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessRuntimeException(e.getMessage());
		}
		return;
	}

	/**
	 * 订单审批推调拨订单时调用,从多公司销售订单转换得到调拨订单VOS5D。 创建日期：(2004-4-20 20:14:41) v5.5
	 * 调拨订单单据类型改为5X
	 * 
	 * @return nc.vo.pub.AggregatedValueObject[]
	 * @param ordvo
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	public void PushSave5D(nc.vo.pub.AggregatedValueObject ordvo, Object clientojb, String curdate)
			throws javax.naming.NamingException, javax.ejb.CreateException, java.sql.SQLException, BusinessException {
		if (ordvo == null)
			return;
		SaleOrderVO[] otherOrdVos = ((SaleOrderVO) ordvo).getOrdVOsOfOtherCorpFor5D();
		if (otherOrdVos == null || otherOrdVos.length <= 0)
			return;

		// 取收货库存组织的收货地区，收货地址，收货地点
		HashMap hscalbody = null;
		// 取收获仓库的收货地区，收货地址，收货地点
		HashMap hswareaddr = null;

		nc.vo.to.pub.BillVO voBilltep = null;

		nc.bs.pub.pf.PfUtilBO pfbo = null;
		try {
			// 将订单vo转化为调拨订单5D
			nc.vo.pub.AggregatedValueObject[] vos5d = PfUtilTools.runChangeDataAry("30", "5X", otherOrdVos);
			if (vos5d == null || vos5d.length <= 0)
				return;

			ArrayList ojblist = new ArrayList();
			ojblist.add(null);
			ojblist.add(((SaleOrderVO) ordvo).getClientLink());

			String[] addrs = null;
			String[] wareaddrs = null;

			pfbo = new nc.bs.pub.pf.PfUtilBO();

			Object[] userobjarr = new Object[vos5d.length];

			// 直运仓
			HashMap hsware = null;

			for (int i = 0, loop = vos5d.length; i < loop; i++) {

				userobjarr[i] = ojblist;

				if (vos5d[i] == null || vos5d[i].getParentVO() == null)
					return;

				// if(otherOrdVos[i].getChildrenVO()==null || )
				// 设置调拨类型,根据订单是否直运
				if (((SaleorderBVO) otherOrdVos[i].getChildrenVO()[0]).getBdericttrans() != null
						&& ((SaleorderBVO) otherOrdVos[i].getChildrenVO()[0]).getBdericttrans().booleanValue()) {

					// 取收货库存组织的收货地区，收货地址，收货地点
					if (hsware == null) {
						hsware = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_stordoc",
								new String[] { "pk_stordoc" }, "pk_calbody", nc.vo.so.so016.SoVoTools.getVOsOnlyValues(
										ordvo.getChildrenVO(), "creccalbodyid"), " isdirectstore='Y' ");
						if (hsware == null)
							hsware = new HashMap();
					}

					// 直运调拨取直运仓
					for (int m = 0, loopm = vos5d[i].getChildrenVO().length; m < loopm; m++) {
						// vos5d[i].getChildrenVO()[m].setAttributeValue("crowno",(""+(m+1)));
						addrs = (String[]) hsware.get(otherOrdVos[i].getBodyVOs()[m].getCreccalbodyid() + "");
						if (  (vos5d[i].getChildrenVO()[m].getAttributeValue("cinwhid")==null
							    || vos5d[i].getChildrenVO()[m].getAttributeValue("cinwhid").toString().trim().length()==0)
								&& addrs != null ) {
							vos5d[i].getChildrenVO()[m].setAttributeValue("cinwhid", addrs[0]);
						}
					}

					// 直运调拨
					// (((nc.vo.to.pub.BillVO) vos5d[i]).getHeaderVO())
					// .setFallocflag(new Integer(
					// nc.vo.to.pub.ConstVO.ITransferType_DIRECT));
					vos5d[i].getParentVO().setAttributeValue("fallocflag", new Integer(0));
				} else {
					// 取收货库存组织的收货地区，收货地址，收货地点
					if (hscalbody == null) {
						hscalbody = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_calbody", new String[] {
								"pk_areacl", "area", "pk_address" }, "pk_calbody", nc.vo.so.so016.SoVoTools
								.getVOsOnlyValues(ordvo.getChildrenVO(), "creccalbodyid"), null);
						if (hscalbody == null)
							hscalbody = new HashMap();
					}

					// 取收货仓库的收货地址，收货地点
					if (hswareaddr == null) {
						hswareaddr = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_stordoc", new String[] {
								"pk_calbody", "storaddr", "pk_address" }, "pk_stordoc", nc.vo.so.so016.SoVoTools
								.getVOsOnlyValues(ordvo.getChildrenVO(), "crecwareid"), null);
						if (hswareaddr == null)
							hswareaddr = new HashMap();
					}

					// 如果入库调拨，置收货单位为空，设置收货库存组织的收货地区，收货地址，收货地点
					for (int m = 0, loopm = vos5d[i].getChildrenVO().length; m < loopm; m++) {

						vos5d[i].getChildrenVO()[m].setAttributeValue("crowno", ("" + (m + 1)));

						vos5d[i].getChildrenVO()[m].setAttributeValue("creceieveid", null);

						addrs = (String[]) hscalbody.get(otherOrdVos[i].getBodyVOs()[m].getCreccalbodyid() + "");

						wareaddrs = (String[]) hswareaddr.get(otherOrdVos[i].getBodyVOs()[m].getCrecwareid() + "");

						if (wareaddrs == null) {

							if (addrs == null) {

								vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);
								vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", null);
								vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", null);

							} else {

								vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);
								vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);
								vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);

							}

						} else {

							if (addrs == null) {

								vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);
								vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
								vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);

							} else {

								vos5d[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

								if (wareaddrs[1] != null && wareaddrs[1].trim().length() > 0)
									vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
								else
									vos5d[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);

								if (wareaddrs[2] != null && wareaddrs[2].trim().length() > 0)
									vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
								else
									vos5d[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);

							}

						}
					}
					// 入库调拨
					// (((nc.vo.to.pub.BillVO) vos5d[i]).getHeaderVO())
					// .setFallocflag(new Integer(
					// nc.vo.to.pub.ConstVO.ITransferType_INSTORE));
					vos5d[i].getParentVO().setAttributeValue("fallocflag", new Integer(1));
				}
				// 置操作人
				voBilltep = (nc.vo.to.pub.BillVO) vos5d[i];
				voBilltep.setOperator(otherOrdVos[i].getParentVO().getAttributeValue("capproveid").toString());

				// pfbo.processAction(
				// "PUSHSAVE",
				// "5D",
				// curdate,
				// null,
				// vos5d[i],
				// ojblist);
			}

			// 保存订单
			pfbo.processBatch("PUSHWRITE", "5X", curdate, vos5d, userobjarr, null);

		} catch (Exception e) {
			SCMEnv.out(e);
			e = nc.vo.so.pub.ExceptionUtils.marshException(e);
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException(e);
		} finally {
		}
	}

	/**
	 * 更新订单表头的状态。
	 * 
	 * 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param String[]
	 *            csaleid
	 * @param String
	 *            statefield
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void setBillStatus(SaleorderHVO[] hvos, String statefield) throws Exception {
		if (hvos == null || hvos.length == 0 || statefield == null)
			return;
		// 处理整单开票结束
		if (statefield.equals("binvoicendflag")) {
			HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO
					.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
							nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
							" bifinvoicefinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
			// " bifinvoicefinish='N' group by csaleid ");
			UFDouble count = null;
			for (int i = 0; i < hvos.length; i++) {
				if (statehs == null) {
					hvos[i].setBinvoicendflag(new UFBoolean(true));
				} else {
					count = (UFDouble) statehs.get(hvos[i].getCsaleid());
					if (count != null && count.doubleValue() > 0) {
						hvos[i].setBinvoicendflag(new UFBoolean(false));
					} else {
						hvos[i].setBinvoicendflag(new UFBoolean(true));
					}
				}
			}
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch(hvos, new String[] { "binvoicendflag" }, "so_sale",
					new String[] { "csaleid" });

			// 处理整单发货结束
		} else if (statefield.equals("breceiptendflag")) {
			HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO
					.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
							nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
							" bifreceivefinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
			// " bifreceivefinish='N' group by csaleid ");
			UFDouble count = null;
			for (int i = 0; i < hvos.length; i++) {
				if (statehs == null) {
					hvos[i].setBreceiptendflag(new UFBoolean(true));
				} else {
					count = (UFDouble) statehs.get(hvos[i].getCsaleid());
					if (count != null && count.doubleValue() > 0) {
						hvos[i].setBreceiptendflag(new UFBoolean(false));
					} else {
						hvos[i].setBreceiptendflag(new UFBoolean(true));
					}
				}
			}
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch(hvos, new String[] { "breceiptendflag" }, "so_sale",
					new String[] { "csaleid" });
			// 处理整单出库结束
		} else if (statefield.equals("boutendflag")) {
			HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO
					.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
							nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
							" bifinventoryfinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
			// " bifinventoryfinish='N' group by csaleid ");
			UFDouble count = null;
			for (int i = 0; i < hvos.length; i++) {
				if (statehs == null) {
					hvos[i].setBoutendflag(new UFBoolean(true));
				} else {
					count = (UFDouble) statehs.get(hvos[i].getCsaleid());
					if (count != null && count.doubleValue() > 0) {
						hvos[i].setBoutendflag(new UFBoolean(false));
					} else {
						hvos[i].setBoutendflag(new UFBoolean(true));
					}
				}
			}
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch(hvos, new String[] { "boutendflag" }, "so_sale",
					new String[] { "csaleid" });
			// 处理整单运输结束
		} else if (statefield.equals("btransendflag")) {
			HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO
					.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
							nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
							" biftransfinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
			// " biftransfinish='N' group by csaleid ");
			UFDouble count = null;
			for (int i = 0; i < hvos.length; i++) {
				if (statehs == null) {
					hvos[i].setBtransendflag(new UFBoolean(true));
				} else {
					count = (UFDouble) statehs.get(hvos[i].getCsaleid());
					if (count != null && count.doubleValue() > 0) {
						hvos[i].setBtransendflag(new UFBoolean(false));
					} else {
						hvos[i].setBtransendflag(new UFBoolean(true));
					}
				}
			}
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch(hvos, new String[] { "btransendflag" }, "so_sale",
					new String[] { "csaleid" });
			// 处理整单付款结束
		} else if (statefield.equals("bpayendflag")) {
			HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b",
					"count(*)", "so_saleorder_b.csaleid", nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
					" so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
			UFDouble count = null;
			for (int i = 0; i < hvos.length; i++) {
				if (statehs == null) {
					hvos[i].setBpayendflag(new UFBoolean(true));
				} else {
					count = (UFDouble) statehs.get(hvos[i].getCsaleid());
					if (count != null && count.doubleValue() > 0) {
						hvos[i].setBpayendflag(new UFBoolean(false));
					} else {
						hvos[i].setBpayendflag(new UFBoolean(true));
					}
				}
			}
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch(hvos, new String[] { "bpayendflag" }, "so_sale",
					new String[] { "csaleid" });
			// 处理整单结算关闭
		} else if (statefield.equals("bsquareendflag")) {

			// HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO
			// .getAnyValueUFDouble(
			// "so_saleexecute,so_saleorder_b",
			// "count(*)",
			// "so_saleorder_b.csaleid",
			// nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
			// " bsquareendflag='N' and
			// so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by
			// so_saleorder_b.csaleid ");
			// UFDouble count = null;
			// for (int i = 0; i < hvos.length; i++) {
			// if (statehs == null) {
			// hvos[i].setBsquareendflag(new UFBoolean(true));
			// } else {
			// count = (UFDouble) statehs.get(hvos[i].getCsaleid());
			// if (count != null && count.doubleValue() > 0) {
			// hvos[i].setBsquareendflag(new UFBoolean(false));
			// } else {
			// hvos[i].setBsquareendflag(new UFBoolean(true));
			// }
			// }
			// }
			// 处理整单状态,如果单据行的状态都为完成，则设置整单状态为完成，否则不处理
		} else if (statefield.equals("fstatus")) {
			HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)",
					"csaleid", nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
					" frowstatus = 6 group by csaleid ");

			HashMap allstatehs = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)",
					"csaleid", nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " dr =0 group by csaleid ");
			if (allstatehs == null)
				return;

			UFDouble allcount = null;
			ArrayList updatehvolist = new ArrayList();
			for (int i = 0; i < hvos.length; i++) {
				if (statehs == null) {
					if (hvos[i].getFstatus().intValue() == nc.ui.pub.bill.BillStatus.FINISH) {
						hvos[i].setFstatus(new Integer(nc.ui.pub.bill.BillStatus.AUDIT));
						updatehvolist.add(hvos[i]);
					}
					continue;
				}
				allcount = (UFDouble) allstatehs.get(hvos[i].getCsaleid());
				if (allcount.equals(statehs.get(hvos[i].getCsaleid()))) {
					hvos[i].setFstatus(new Integer(nc.ui.pub.bill.BillStatus.FINISH));
					updatehvolist.add(hvos[i]);
				}
			}
			if (updatehvolist.size() > 0) {
				nc.impl.scm.so.so016.SOToolsDMO.updateBatch((SaleorderHVO[]) updatehvolist
						.toArray(new SaleorderHVO[updatehvolist.size()]), new String[] { "fstatus" }, "so_sale",
						new String[] { "csaleid" });
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-18 10:50:56)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public void setPreOrdStatus(nc.vo.pub.AggregatedValueObject vo) throws javax.naming.NamingException,
			nc.vo.pub.BusinessException, java.sql.SQLException, nc.bs.pub.SystemException {
		// /////////////////////////////////////////////////////////////////
		// 必要条件判断
		if (vo == null)
			return;

		if (!(vo instanceof nc.vo.so.so001.SaleOrderVO))
			return;

		SaleOrderVO ordvo = (SaleOrderVO) vo;
		// SaleorderHVO hvo = (SaleorderHVO)ordvo.getParentVO();
		SaleorderBVO[] bvos = (SaleorderBVO[]) vo.getChildrenVO();
		
		//过滤没有来源的bvo
		SaleorderBVO[] ordbvos = null;
		List<SaleorderBVO> list_bvos = new ArrayList<SaleorderBVO>();
		for (SaleorderBVO bvo : bvos){
			if (bvo.getCreceipttype() != null && bvo.getCreceipttype().equals(SaleBillType.SalePreorder))
				list_bvos.add(bvo);
		}
		if (list_bvos.size()>0)
			ordbvos = list_bvos.toArray(new SaleorderBVO[list_bvos.size()]);
		
		if (ordbvos == null
				|| ordbvos.length <= 0
				|| (ordbvos[0].getCreceipttype() == null || !ordbvos[0].getCreceipttype().equals(
						SaleBillType.SalePreorder)))
			return;

		// /////////////////////////////////////////////////////////////////
		// 修改、删除时查询原有订单数量
		// <表体id,订单old数量>
		Hashtable bid_oldnum = null;
		if (ordvo.getActionInt() != ISaleOrderAction.A_ADD) {
			ArrayList<String> bid = new ArrayList<String>();
			for (int i = 0, loop = ordbvos.length; i < loop; i++)
				bid.add(ordbvos[i].getCorder_bid());
			String[] bids = new String[bid.size()];
			bid.toArray(bids);
			bid_oldnum = getAnyValue("SO_SALEORDER_B", "nnumber", "corder_bid", bids);
		}

		// /////////////////////////////////////////////////////////////////
		// 汇总源头bid、差异数量、是否预订单关闭

		// 表体id、来源表体id
		String bid, sourcebid_s = null;
		// 是否预订单关闭
		UFBoolean bpreclose_b = null;
		// 本次数量值
		UFDouble newnum = null;
		// 差异数量
		UFDouble unum = null;
		UFDouble zero = new UFDouble(.0);

		// 汇总(源头bid,<差异数量,是否预订单关闭,订单行号>)
		Map id_num = new HashMap();
		Object[] temp = null;
		
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			// 表体id
			bid = ordbvos[i].getCorder_bid();
			// 来源表体id
			sourcebid_s = ordbvos[i].getCsourcebillbodyid();
			// 是否预订单关闭
			bpreclose_b = ordbvos[i].getBpreorderclose();
			// 本次数量值
			newnum = ordbvos[i].getNnumber();

			// 删除
			if (ordvo.getActionInt() == ISaleOrderAction.A_BLANKOUT
					|| ordvo.getChildrenVO()[i].getStatus()==VOStatus.DELETED )
				newnum = zero;
			// 新增
			if (ordvo.getActionInt() == ISaleOrderAction.A_ADD)
				unum = ordbvos[i].getNnumber();
			// 修改
			else
				unum = bid == null ? newnum : newnum
						.sub(SmartVODataUtils
								.getUFDouble(bid_oldnum.get(bid)));

			// 汇总(源头bid,<差异数量,是否预订单关闭>)
			if (!id_num.containsKey(sourcebid_s)) {
				// 0 -- 差异数量 1 -- 是否预订单关闭
				Object[] obj = new Object[3];
				obj[0] = unum;
				obj[1] = bpreclose_b;
				obj[2] = ordbvos[i].getCrowno();
				id_num.put(sourcebid_s, obj);
			} else {
				temp = (Object[]) id_num.get(sourcebid_s);
				temp[0] = unum.add((UFDouble) temp[0]);
				temp[1] = bpreclose_b;
			}
		}

		// /////////////////////////////////////////////////////////////////
		// 组织返回结果
		Set key = id_num.keySet();
		Collection values = id_num.values();

		// 源头bid
		String[] sourcebids = (String[]) key.toArray(new String[key.size()]);

		Object[] svalues = (Object[]) values.toArray(new Object[values.size()]);
		// 是否预订单关闭
		UFBoolean[] bprecloses = new UFBoolean[svalues.length];
		// 差异数量
		UFDouble[] nums = new UFDouble[svalues.length];
		//行号
		String[] sRowNos = new String[svalues.length];
		for (int i = 0; i < svalues.length; i++) {
			nums[i] = (UFDouble) ((Object[]) svalues[i])[0];
			bprecloses[i] = (UFBoolean) ((Object[]) svalues[i])[1];
			sRowNos[i] = ((Object[]) svalues[i])[2].toString();
		}

		// /////////////////////////////////////////////////////////////////
		// 调用接口
		try {
			/** 预订单回写接口变更 v5.5 */
			IPreorder ipreord = (IPreorder) NCLocator.getInstance().lookup(IPreorder.class.getName());

			ipreord.rewritePreorderNarrnum(sRowNos,sourcebids, nums, bprecloses);

		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
		/** 预订单回写接口变更 v5.5 */

	}

	/**
	 * 检测是否超出允差 wsy 2005-9-13
	 */
	private void checkAllowDiffer(SaleorderBVO bvo, int i, UFBoolean SO23, UFDouble SO24, UFBoolean SO25, UFDouble SO26)
			throws BusinessException {

		// 超订单出库
		if (SO23 == null)
			SO23 = getParaUFBooleanValue(bvo.getPkcorp(), "SO23", null);
		if (SO24 == null)
			SO24 = getParaUFDoubleValue(bvo.getPkcorp(), "SO24", null);

		// 超订单开票
		if (SO25 == null)
			SO25 = getParaUFBooleanValue(bvo.getPkcorp(), "SO25", null);
		if (SO26 == null)
			SO26 = getParaUFDoubleValue(bvo.getPkcorp(), "SO26", null);

		UFDouble nnumber = bvo.getNnumber() == null ? new UFDouble(0) : bvo.getNnumber();

		// 负数量
		boolean bisNeg = false;
		if (nnumber.doubleValue() < 0) {
			bisNeg = true;
			nnumber = nnumber.multiply(-1);
		}

		// 累计发货数量
		UFDouble ntotalreceivenumber = bvo.getNtotalreceivenumber() == null ? new UFDouble(0) : bvo
				.getNtotalreceivenumber();
		// 累计开票数量
		UFDouble ntotalinvoicenumber = bvo.getNtotalinvoicenumber() == null ? new UFDouble(0) : bvo
				.getNtotalinvoicenumber();
		// 累计出库数量
		UFDouble ntotalinventorynumber = (bvo.getNtotalinventorynumber() == null ? new UFDouble(0) : bvo
				.getNtotalinventorynumber()).add(bvo.getNtotalshouldoutnum() == null ? new UFDouble(0) : bvo
				.getNtotalshouldoutnum());
		// 累计途损数量
		UFDouble ntranslossnum = bvo.getNtranslossnum() == null ? new UFDouble(0) : bvo.getNtranslossnum();

		ntotalinventorynumber = ntotalinventorynumber.sub(ntranslossnum);

		if (bisNeg) {
			ntotalreceivenumber = ntotalreceivenumber.multiply(-1);
			ntotalinvoicenumber = ntotalinvoicenumber.multiply(-1);
			ntotalinventorynumber = ntotalinventorynumber.multiply(-1);
		}

		UFDouble postiveNum = null;

		// 第{0}行修订数量不能超过允许出库数量。
//		String FHMSG = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000524");
		String CKMSG = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000526");
		String KPMSG = NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000525");

		ArrayList al = new ArrayList();
		postiveNum = nnumber;

		// 出库单回写订单，不判断是否超订单发货 v5.5
		/*
		 * if (postiveNum.compareTo(ntotalreceivenumber) < 0) { al.add(FHMSG); }
		 */

		// 超订单开票
		if (SO25.booleanValue())
			postiveNum = nnumber.multiply(1.0 + SO26.doubleValue() / 100.);
		else
			postiveNum = nnumber;
		if (postiveNum.compareTo(ntotalinvoicenumber) < 0) {
			al.add(KPMSG);
		}

		// 超订单出库
		if (SO23.booleanValue())
			postiveNum = nnumber.multiply(1.0 + SO24.doubleValue() / 100.);
		else
			postiveNum = nnumber;
		if (postiveNum.compareTo(ntotalinventorynumber) < 0) {
			al.add(CKMSG);
		}

		// 第[]行修订数量{}超出[]数量{}的允差范围[]

		if (al.size() > 0) {
			String sScoremsg = "";
			for (int j = 0; j < al.size(); j++) {
				sScoremsg += (String) al.get(j) + "\n";
			}
			throw new BusinessException(sScoremsg);

		}

	}

	/**
	 * 销售出库单保存回写销售订单累计出库数量，同时最后更新订单状态
	 * 处理订单出库状态，同时处理相关业务信息的更新。
	 * 
	 * 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param bvos
	 *            nc.vo.so.so001.SaleorderBVO[]
	 * @param changestate
	 *            java.util.ArrayList
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 * 
	 * @comment 被动调用，不再处理信用
	 * 
	 */
	public void processOutState(SaleorderBVO[] bvos) throws Exception {

		if (bvos == null || bvos.length == 0)
			return;

		nc.bs.pub.para.SysInitBO sbo = new nc.bs.pub.para.SysInitBO();

		Hashtable htTemp = sbo.queryBatchParaValues(bvos[0].getPkcorp(), new String[] { "SO23", "SO24", "SO25", "SO26","SO29",
				"SO47","SO68","SO69"});

		// 超订单出库
		UFBoolean SO23 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO23", htTemp);
		UFDouble SO24 = getParaUFDoubleValue(bvos[0].getPkcorp(), "SO24", htTemp);

		// 超订单、出库开票
		UFBoolean SO25 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO25", htTemp);
		UFDouble SO26 = getParaUFDoubleValue(bvos[0].getPkcorp(), "SO26", htTemp);
		
		//2009-10-29 fengjb 是否出库关闭看参数“SO29” 出库关闭下限
		UFDouble so29 = getParaUFDoubleValue(bvos[0].getPkcorp(), "SO29", htTemp);

		// 订单是否自动关闭
		UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", htTemp);

		SaleorderBVO[] bakbvos = (SaleorderBVO[]) nc.vo.so.so016.SoVoTools.getVOsByVOs("nc.vo.so.so001.SaleorderBVO",
				bvos, bvos[0].getAttributeNames(), bvos[0].getAttributeNames());

		UFDouble ufzero = new UFDouble(0);
		UFBoolean uftrue = new UFBoolean(true);
		UFBoolean uffalse = new UFBoolean(false);
		UFDouble num = null, outnum = null, ntranslossnum = null, tempnum = null, tempnum1 = null;

		// UFDouble outendrate = getOutSendEndRate(bvos[0].getPkcorp());

		// 记录出库结束和打开的行
		ArrayList outendvolist = new ArrayList();
		ArrayList outopenvolist = new ArrayList();

		// 记录行状态结束和打开的行
		ArrayList rowendvolist = new ArrayList();
		ArrayList rowopenvolist = new ArrayList();

		ArrayList rowsendopenlist = new ArrayList();
		SaleorderBVO sendendbvo = null;

		UFDouble ntotalinventorynumber_old = null;

		for (int i = 0, loop = bvos.length; i < loop; i++) {

			// 先查看手动关闭标记
			/** V502 jindongmei yangbo zhongwei* */
			// 只有新增出库单,此字段才有值
			// 暂没有考虑标记改变对其它属性的影响，需以后完善
			if (bvos[i].bsaleoutclosed != null) {
				// //强制关闭
				if (bvos[i].bsaleoutclosed.booleanValue()) {
					bvos[i].setBifinventoryfinish(UFBoolean.TRUE);
					/** 发运在出库之前，出库关闭，发运一定关闭* */
					bvos[i].setBifreceivefinish(UFBoolean.TRUE);
					bvos[i].setStatus(VOStatus.UPDATED);
					bvos[i].setCreceipttype("30");
					if ((bvos[i].getBifinventoryfinish() != null) && (!bvos[i].getBifinventoryfinish().booleanValue())) {
						outendvolist.add(bvos[i]);
					}
				}
				// //强制打开
				else if (!bvos[i].bsaleoutclosed.booleanValue()) {
					/** 如果没有关闭，则不用强行打开，涉及信用回写问题* */
					if ((bvos[i].getBifinventoryfinish() != null) && (bvos[i].getBifinventoryfinish().booleanValue())) {
						outopenvolist.add(bvos[i]);
					}

					bvos[i].setBifinventoryfinish(UFBoolean.FALSE);

					/** 不处理发运自动打开的情况，旧需求规定 wushengping wangsenyang zhongwei* */

					bvos[i].setStatus(VOStatus.UPDATED);
					bvos[i].setCreceipttype("30");

				}
			}
			// 标记为空，按照以前逻辑处理
			else {
				
				/**add by ouyangzhb 2012-12-08 注销掉，骏杰项目需求：在销售订单关闭之后，出库单还需要修改 */
	/**			
				if (bvos[i].getBifinventoryfinish() != null
						&& bvos[i].getBifinventoryfinish().booleanValue()
						&& bvos[i].getNtotalinventorynumber() != null
						&& bvos[i].getNtotalinventorynumber_old() != null
						&& bvos[i].getNtotalinventorynumber().abs().doubleValue() > bvos[i]
								.getNtotalinventorynumber_old().abs().doubleValue()) {
					// 如果此时订单已经出库结束，则抛异常 20060425
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
							"UPP40060301-000516"));

				}
		*/
				
				// 检测容差
				checkAllowDiffer(bvos[i], i, SO23, SO24, SO25, SO26);

				bvos[i].setStatus(VOStatus.UNCHANGED);
				bvos[i].setCreceipttype("30");
				bakbvos[i].setCreceipttype("30");

				// 订单数量
				num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();

				// 原累计出库数量
				ntotalinventorynumber_old = bvos[i].getNtotalinventorynumber_old();
				if (ntotalinventorynumber_old == null)
					ntotalinventorynumber_old = SoVoConst.duf0;

				// 现累计出库数量
				outnum = bvos[i].getNtotalinventorynumber() == null ? ufzero : bvos[i].getNtotalinventorynumber();

				// 现累计途损数量
				ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

				// 如果订单数量*出库结束比例-出库数量+运输货损数量<0,则出库结束
				// 如果此时订单已经出库结束，则抛异常 20060425
				num = num.abs().multiply(1.0 - so29.doubleValue() / 100.);
				outnum = outnum.abs();
				ntranslossnum = ntranslossnum.abs();
				ntotalinventorynumber_old = ntotalinventorynumber_old.abs();

				// 订单数量*出库结束比例-出库数量+运输货损数量
				tempnum = num.sub(outnum).add(ntranslossnum);

				// 订单数量*出库结束比例-回写前的订单累计出库数量+运输货损数量
				tempnum1 = num.sub(ntotalinventorynumber_old).add(ntranslossnum);			    
			    
				// 检查退货 = 累计退货订单数量+累计退货申请单数量
				UFDouble ntotalreturnnumber = bvos[i].getNtotalreturnnumber();
				// outnum<=0，表示当前库存已经可以满足要求（相当于入库）
				
				//更新后的累计出库数量>=累计退货数量(累计途损数量+累计退货订单数量+累计退货申请单数量)
				if (ntotalreturnnumber != null
						&& ntotalreturnnumber.abs().doubleValue() + ntranslossnum.doubleValue() > outnum
									.doubleValue()) {
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000036")
						/* @res "累计出库数量不能小于累计退货数量" */);
						// throw new
						// nc.vo.pub.BusinessException("累计出库数量不能小于累计退货数量");
				}
							
				//////////////////////////// 订单出库、发货关闭
				if (tempnum.doubleValue() <= 0) {
					// 2005-9-7 wsy 先给华孚打一个不自动结束的，以后改之
					// 2005-9-12 得用參數控制
					if (SO47.booleanValue()) {
						if (bvos[i].getBifinventoryfinish() == null || !bvos[i].getBifinventoryfinish().booleanValue()) {
							outendvolist.add(bvos[i]);
							bvos[i].setBifinventoryfinish(uftrue);
						}
						// 如果出库结束，同时结束发货
						bvos[i].setBifreceivefinish(uftrue);
						bvos[i].setStatus(VOStatus.UPDATED);
					}

				} 
				//////////////////////////// 订单出库、发货打开
				else {
					// 2005-9-27 得用參數控制
					if (SO47.booleanValue()) {
						if (bvos[i].getBifinventoryfinish() == null || bvos[i].getBifinventoryfinish().booleanValue()) {
							outopenvolist.add(bvos[i]);
							bvos[i].setBifinventoryfinish(uffalse);
							bvos[i].setStatus(VOStatus.UPDATED);
						}
						
						/**====处理累计途损对订单发货关闭状态的影响 V5.5=========================*/
						if (ntranslossnum.compareTo(ufzero)!=0){
							// 超订单发货
							UFBoolean SO68 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO68", htTemp);
							UFDouble SO69 = getParaUFDoubleValue(bvos[0].getPkcorp(), "SO69", htTemp);
							// 订单数量
							UFDouble ordernum = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();
							// 订单数量*订单发货结束比例+运输货损数量
							UFDouble ntotalnumber = null;
							if (SO68.booleanValue())
								ntotalnumber = ordernum.abs().multiply(1.0 + SO69.doubleValue() / 100.).add(ntranslossnum);
							else
								ntotalnumber = ordernum.abs().add(ntranslossnum);
							// 累计发货数量
						    UFDouble ntotalreceivenumber = bvos[i].getNtotalreceivenumber()== null ? ufzero : bvos[i].getNtotalreceivenumber();
							
							// 累计发货数量 < 订单数量*订单发货结束比例+运输货损数量:订单发货自动打开
						    if (bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue()) {
						    	if (ntotalreceivenumber.compareTo(ntotalnumber)<0){
									bvos[i].setBifreceivefinish(uffalse);
									bvos[i].setStatus(VOStatus.UPDATED);
						    	}
							}
						}
						/**====处理累计途损对订单发货关闭状态的影响 V5.5=========================*/
						
					}

					if (bvos[i].getBifinventoryfinish() != null && bvos[i].getBifinventoryfinish().booleanValue()) {
						// 判断（1-下允差比率）*订单数量是否小于等于回写前的订单累计出库数量
						if (bvos[i].getNtotalinventorynumber_old() != null) {
							if (tempnum1.doubleValue() <= 0) {

								if (bvos[i].getNtotalreceivenumber() == null
										|| bvos[i].getNtotalreceivenumber().doubleValue() == 0) {
									if (bvos[i].getBifreceivefinish() != null
											&& bvos[i].getBifreceivefinish().booleanValue()) {
										sendendbvo = new SaleorderBVO();
										sendendbvo.setCorder_bid(bvos[i].getCorder_bid());
										sendendbvo.setCreceipttype("30");
										sendendbvo.setBifreceivefinish(uffalse);
										rowsendopenlist.add(sendendbvo);
									}
								}
								outopenvolist.add(bvos[i]);
								bvos[i].setBifinventoryfinish(uffalse);
								bvos[i].setStatus(VOStatus.UPDATED);

							}
						} else {
							if (bvos[i].getNtotalreceivenumber() == null
									|| bvos[i].getNtotalreceivenumber().doubleValue() == 0) {
								if (bvos[i].getBifreceivefinish() != null
										&& bvos[i].getBifreceivefinish().booleanValue()) {
									sendendbvo = new SaleorderBVO();
									sendendbvo.setCorder_bid(bvos[i].getCorder_bid());
									sendendbvo.setCreceipttype("30");
									sendendbvo.setBifreceivefinish(uffalse);
									rowsendopenlist.add(sendendbvo);
								}
							}
							outopenvolist.add(bvos[i]);
							bvos[i].setBifinventoryfinish(uffalse);
							bvos[i].setStatus(VOStatus.UPDATED);
						}
					}
				}//////////////////////////// 订单出库、发货打开
				
			}// end else  自动处理订单出库、发货状态


			// 处理整行状态
			if (bvos[i].getBifinventoryfinish() != null && bvos[i].getBifinventoryfinish().booleanValue()
					&& bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue()
					&& bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()
					&& bvos[i].getBsquareendflag() != null && bvos[i].getBsquareendflag().booleanValue()) {

				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.AUDIT) {
					rowendvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.FINISH));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			} else {
				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.FINISH) {
					rowopenvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.AUDIT));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			}
		}

		// 根据vo状态，选出发生状态发生变化的vo
		ArrayList updatebvolist = new ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getStatus() == VOStatus.UPDATED) {
				updatebvolist.add(bvos[i]);
			}
		}

		int count = updatebvolist.size();
		if (count <= 0)
			return;
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) updatebvolist.toArray(new SaleorderBVO[count]);

		String[] updatefields = { "bifreceivefinish", "bifinventoryfinish" };
		String[] wherefields = new String[] { "corder_bid" };

		// 更新执行表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute",
				new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

		updatefields = new String[] { "frowstatus" };
		// 更新子表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields,
				wherefields);

		// 查询订单表头

		SaleorderHVO[] hvos = (SaleorderHVO[]) queryHeadDataForUpdateStatus(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(
				bvos, "csaleid"));

		SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

		// 处理整单状态
		SaleorderBVO[] tempbvos = null;
		for (int i = 0, loop = vos.length; i < loop; i++) {
			vos[i].getHeadVO().setStatus(VOStatus.UNCHANGED);
			tempbvos = vos[i].getBodyVOs();
			for (int j = 0, loopj = tempbvos.length; j < loopj; j++) {
				// 处理整单状态
				if (tempbvos[j].getFrowstatus() == null || tempbvos[j].getFrowstatus().intValue() == BillStatus.AUDIT) {
					// 如果订单的表体存在状态为审批的行，并且表头的状态为完成，则打开表头状态为审批
					if (vos[i].getHeadVO().getFstatus() != null
							&& vos[i].getHeadVO().getFstatus().intValue() == BillStatus.FINISH) {
						vos[i].getHeadVO().setFstatus(new Integer(BillStatus.AUDIT));
						vos[i].getHeadVO().setStatus(VOStatus.UPDATED);
					}
				}
			}
		}

		// 处理整单出库结束
		setBillStatus(hvos, "boutendflag");

		// 处理整单发货结束
		setBillStatus(hvos, "breceiptendflag");

		// 将订单表头分为两类：（1）本次更新导致订单行frowstatus打开,则必然导致订单表头fstatus打开，
		// （2）剩余的则为订单行关闭的行，它们可能导致订单头状态fstatus结束,也可能不会（如果其他未更新的订单行为结束）
		ArrayList openhvolist = new ArrayList();
		ArrayList closehvolist = new ArrayList();
		for (int i = 0, loop = vos.length; i < loop; i++) {
			if (vos[i].getHeadVO().getStatus() == VOStatus.UPDATED) {
				openhvolist.add(vos[i].getHeadVO());
			} else {
				closehvolist.add(vos[i].getHeadVO());
			}
		}
		// 更新整单状态
		if (openhvolist.size() > 0) {
			String headsql = " update so_sale set fstatus = " + nc.ui.pub.bill.BillStatus.AUDIT + " where csaleid = ? ";
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch((SaleorderHVO[]) openhvolist
					.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
		}

		if (closehvolist.size() > 0) {
			setBillStatus((SaleorderHVO[]) closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
		}

		if (rowsendopenlist.size() > 0) {

			SaleorderBVO[] sendendbvos = (SaleorderBVO[]) rowsendopenlist.toArray(new SaleorderBVO[rowsendopenlist
					.size()]);

			// 更新执行表状态
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch(sendendbvos, new String[] { "bifreceivefinish" },
					new String[] { "bifreceivefinish" }, "so_saleexecute",
					new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

			// 处理整单发货结束
			setBillStatus(hvos, "breceiptendflag");

		}

		// ///////////更新相关业务数据///////////////////////////////////////////////
		// 1更新ATP
		// v5.5-ATP更新方式变更,下游回写数量造成的订单关闭不更新可用量
		// updateAtpByOrdRows(hvos, bakbvos, bvos);

		// 1.上游订单出库关闭--自动关闭下游已审批的发货单、删除自由状态的发货单
		String[] bids = new String[outendvolist.size()];
		for (int i = 0; i < outendvolist.size(); i++)
			bids[i] = ((SaleorderBVO) outendvolist.get(i)).getPrimaryKey();
		// 调用接口
		IReceiveService rsbo = (IReceiveService) NCLocator.getInstance().lookup(IReceiveService.class.getName());
		rsbo.processOrderOutEnd(bids);

		// 2更新订单出库核销关系及更新合同执行数量
		// 处理出库结束的行

		// 出库结束、打开的vos
		List<SaleOrderVO> outopenend = new ArrayList<SaleOrderVO>();
		if (outendvolist.size() > 0) {

			vos = toSaleOrderVO(hvos, outendvolist);
			outopenend = Arrays.asList(vos);

			for (int i = 0, loop = vos.length; i < loop; i++) {
				// 更新订单核销关系
				// 20061114 dw wusp要求不更新，如果有问题请考虑从此着手 --wsy
				// baldmo.updateSoBalance(vos[i], ioutendaction);
				if (vos[i].getBodyVOs()[0].getAttributeValue("ifRetOut") != null
						&& ((UFBoolean) vos[i].getBodyVOs()[0].getAttributeValue("ifRetOut")).booleanValue() == true) {
					vos[i].setIAction(ISaleOrderAction.A_OUTOPEN);
				} else {
					vos[i].setIAction(ISaleOrderAction.A_OUTEND);
				}

			}

		}

		// 处理出库打开的行
		if (outopenvolist.size() > 0) {
			vos = toSaleOrderVO(hvos, outopenvolist);
			/*add by ouyangzhb 2012-12-12 outopenend.addAll(Arrays.asList(vos))
			会报：java.lang.UnsupportedOperationException 的错误
			list 不支持addAll 方法？莫名奇妙*/
			outopenend.clear();
			for(int i=0;vos!= null &&i<vos.length;i++){
				outopenend.add(vos[i]);
			}
//			outopenend.addAll(Arrays.asList(vos));
			/*add by ouyangzhb 2012-12-12  end */
		}

		// 3.处理跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开 v5.5
		if (outopenend.size() > 0) {
			// 所有需要出库关闭、打开的vos
			SaleOrderVO[] allvos = outopenend.toArray(new SaleOrderVO[outopenend.size()]);

			// 必须有直运调拨标志
			ArrayList<SaleorderBVO> list = new ArrayList<SaleorderBVO>();
			for (SaleOrderVO svo : allvos)
				for (SaleorderBVO bvo : svo.getBodyVOs())
					// 有直运调拨标志
					if (bvo.getBdericttrans() != null && bvo.getBdericttrans().booleanValue())
						list.add(bvo);

			// 跨公司直运销售订单行出库关闭/打开的销售订单
			if (list.size() > 0) {
				SaleOrderVO[] allendopenvos = toSaleOrderVO(hvos, list);
				set5XOutEndFlag(allendopenvos);
			}
		}

		// 4.处理应收及信用数据
		// 处理行状态结束的行
		if (rowendvolist.size() > 0) {
			// 更新合同执行数量
			setSaleCT((SaleorderBVO[]) rowendvolist.toArray(new SaleorderBVO[rowendvolist.size()]),
					ISaleOrderAction.A_CLOSE);
		}

		// 处理行状态打开的行
		if (rowopenvolist.size() > 0) {
			// 更新合同数量
			setSaleCT((SaleorderBVO[]) rowopenvolist.toArray(new SaleorderBVO[rowopenvolist.size()]),
					ISaleOrderAction.A_OPEN);
		}

	}

	/**
	 * @param sPk_corp
	 * @param sPara
	 * @return
	 * @throws BusinessException
	 */
	private UFBoolean getParaUFBooleanValue(String sPk_corp, String sPara, Hashtable htTemp) throws BusinessException {
		if (htTemp == null) {
			nc.bs.pub.para.SysInitBO sbo = new nc.bs.pub.para.SysInitBO();

			htTemp = sbo.queryBatchParaValues(sPk_corp, new String[] { sPara });
		}
		UFBoolean SO47 = null;
		if (htTemp == null || !htTemp.containsKey(sPara)) {
			SO47 = new UFBoolean(true);
		} else {
			SO47 = new UFBoolean(htTemp.get(sPara) == null ? "Y" : htTemp.get(sPara).toString());
		}
		return SO47;

	}

	/**
	 * @param sPk_corp
	 * @param sPara
	 * @return
	 * @throws BusinessException
	 */
	private UFDouble getParaUFDoubleValue(String sPk_corp, String sPara, Hashtable htTemp) throws BusinessException {
		if (htTemp == null) {
			nc.bs.pub.para.SysInitBO sbo = new nc.bs.pub.para.SysInitBO();

			htTemp = sbo.queryBatchParaValues(sPk_corp, new String[] { sPara });
		}
		UFDouble SO47 = null;
		if (htTemp == null || !htTemp.containsKey(sPara)) {
			SO47 = new UFDouble(0);
		} else {
			SO47 = new UFDouble(htTemp.get(sPara) == null ? "0" : htTemp.get(sPara).toString());
		}
		return SO47;

	}

	/**
	 * 将表头表体组合成单据VO（hvos与bvoslist可以配对出的单据VO）创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param SaleorderHVO[]
	 *            hvos
	 * @param ArrayList
	 *            bvoslist
	 */
	public SaleOrderVO[] toSaleOrderVO(SaleorderHVO[] hvos, List bvoslist) {
		if (hvos == null || hvos.length == 0 || bvoslist == null)
			return null;
		ArrayList volist = null;
		HashMap hsvo = new HashMap();
		SaleorderBVO bvo = null;
		for (int i = 0, loop = bvoslist.size(); i < loop; i++) {
			bvo = (SaleorderBVO) bvoslist.get(i);
			if (bvo.getCsaleid() == null)
				continue;
			volist = (ArrayList) hsvo.get(bvo.getCsaleid());
			if (volist == null) {
				volist = new ArrayList();
				hsvo.put(bvo.getCsaleid(), volist);
			}
			volist.add(bvo);
		}
		if (hsvo.size() <= 0)
			return null;
		ArrayList retvolist = new ArrayList();
		Map.Entry oentry = null;
		Iterator iter = hsvo.entrySet().iterator();
		int pos = -1;
		String[] keys = { "csaleid" };
		Object[] values = new Object[1];
		SaleOrderVO vo = null;
		while (iter.hasNext()) {
			oentry = (Map.Entry) iter.next();
			values[0] = oentry.getKey();
			pos = nc.vo.so.so016.SoVoTools.find(hvos, keys, values);
			if (pos >= 0) {
				vo = new SaleOrderVO();
				vo.setParentVO(hvos[pos]);
				volist = (ArrayList) oentry.getValue();
				vo.setChildrenVO((SaleorderBVO[]) volist.toArray(new SaleorderBVO[volist.size()]));
				retvolist.add(vo);
			}
		}
		if (retvolist.size() <= 0)
			return null;
		return (SaleOrderVO[]) retvolist.toArray(new SaleOrderVO[retvolist.size()]);
	}

	/**
	 * 取得开票结束比例
	 * 
	 * 创建日期：(2001-7-5)
	 * 
	 * @return nc.vo.prm.prm003.EvalindexmodelItemVO
	 * @param key
	 *            String
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	private UFDouble getOutInvoiceEndRate(String corpid) {
		UFDouble retrate = null;// new UFDouble(1);
		UFDouble SO48 = null;
		try {
			nc.bs.pub.para.SysInitDMO sysinitdmo = new nc.bs.pub.para.SysInitDMO();
			SO48 = sysinitdmo.getParaDbl(corpid, "SO48");
			if (SO48 == null)
				SO48 = new UFDouble(0);
		} catch (Exception e) {
			/** 如果出错，SO29取0 */
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			SO48 = new UFDouble(0);
		}

		retrate = (new UFDouble(1)).sub(SO48.abs().div(new UFDouble(100)));
		return retrate;
	}

	/**
	 * SO77赠品是否开票为否,则赠品订单行审核自动做开票关闭(V5.5)
	 * 
	 * @param avo
	 * @throws BusinessException
	 */
	public void processInvoicendStateWhenApprove(AggregatedValueObject avo) throws Exception {
		SaleOrderVO svo = (SaleOrderVO) avo;
		// 赠品bvo
		ArrayList<SaleorderBVO> bvolist = new ArrayList<SaleorderBVO>();
		for (SaleorderBVO bvo : svo.getBodyVOs())
			if (bvo.getBlargessflag().booleanValue())
				bvolist.add(bvo);

		if (bvolist.size() <= 0)
			return;

		// 赠品是否开票
		UFBoolean SO77 = getParaUFBooleanValue(svo.getPk_corp(), "SO77", null);
		if (SO77 != null && !SO77.booleanValue()) {
			SaleorderBVO[] bvos = bvolist.toArray(new SaleorderBVO[bvolist.size()]);
			UFBoolean uftrue = new UFBoolean(true);
			// 赠品订单行自动做开票关闭
			for (SaleorderBVO bvo : bvos) {
				bvo.setBifinvoicefinish(uftrue);
				bvo.setStatus(VOStatus.UPDATED);
				bvo.setCreceipttype(SaleBillType.SaleOrder);
			}
			processInvoicendStateInner(bvos);
		}

	}

	/**
	 * 处理订单开票状态，同时处理相关业务信息的更新。 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param bvos
	 *            nc.vo.so.so001.SaleorderBVO[]
	 * @param changestate
	 *            java.util.ArrayList
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void processInvoicendState(SaleorderBVO[] bvos) throws Exception {
		if (bvos == null || bvos.length == 0)
			return;

		UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);
		UFDouble SO48 = getOutInvoiceEndRate(bvos[0].getPkcorp());

		UFDouble ufzero = new UFDouble(0);
		UFBoolean uftrue = new UFBoolean(true);
		UFBoolean uffalse = new UFBoolean(false);
		UFDouble num = null, invoicenum = null, tempnum = null, ntotalinvoicenumber_old = null;
		UFDouble rushnum = null;
		// UFDouble outendrate = new UFDouble(1);

		UFDouble nsummny = null;
		UFDouble ntotalinvoicemny = null;

		/** added by liubing 处理开票结束对三个应收的影响* */
		ArrayList invoiceendvolist = new ArrayList();
		ArrayList invoiceopenvolist = new ArrayList();
		/** added by liubing 处理开票结束对三个应收的影响* */

		for (int i = 0, loop = bvos.length; i < loop; i++) {
			bvos[i].setStatus(VOStatus.UNCHANGED);
			bvos[i].setCreceipttype("30");

			num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();

			if (bvos[i].getBifinvoicefinish() != null
					&& bvos[i].getBifinvoicefinish().booleanValue()
					&& bvos[i].getNtotalinvoicenumber() != null
					&& bvos[i].getNtotalinvoicenumber_old() != null
					&& bvos[i].getNtotalinvoicenumber().abs().doubleValue() > bvos[i].getNtotalinvoicenumber_old()
							.abs().doubleValue()
              ) {
				// 如果此时订单已经出库结束，则抛异常 20060425
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000519"));

			}
			// 如果数量为空或0，则用金额判断
			if (SO47.booleanValue()) {
				if (num.doubleValue() == 0) {

					// 本币价税合计
					nsummny = bvos[i].getNsummny() == null ? ufzero : bvos[i].getNsummny();

					// 已开票金额
					ntotalinvoicemny = bvos[i].getNtotalinvoicemny() == null ? ufzero : bvos[i].getNtotalinvoicemny();

					// 如果订单价税合计-已开票金额<=0,则开票结束

					nsummny = nsummny.abs();
					ntotalinvoicemny = ntotalinvoicemny.abs();

					tempnum = nsummny.multiply(SO48).sub(ntotalinvoicemny);
					if (tempnum.doubleValue() <= 0) {
						if (bvos[i].getBifinvoicefinish() == null || !bvos[i].getBifinvoicefinish().booleanValue()) {
							bvos[i].setBifinvoicefinish(uftrue);
							bvos[i].setStatus(VOStatus.UPDATED);
							/** added by liubing 处理开票结束对三个应收的影响* */
							invoiceendvolist.add(bvos[i]);
						}
					} else {
						if (bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()) {
							bvos[i].setBifinvoicefinish(uffalse);
							bvos[i].setStatus(VOStatus.UPDATED);
							/** added by liubing 处理开票结束对三个应收的影响* */
							invoiceopenvolist.add(bvos[i]);
						}
					}

				} else {

					invoicenum = bvos[i].getNtotalinvoicenumber() == null ? ufzero : bvos[i].getNtotalinvoicenumber();

					// 如果订单数量-开票主数量<=0,则开票结束
					num = num.abs();
					invoicenum = invoicenum.abs();

					/** 订单数量-开票数量-对冲数量 from V502* */
					rushnum = bvos[i].getNrushnum() == null ? ufzero : bvos[i].getNrushnum();
					rushnum = rushnum.abs();
					tempnum = num.multiply(SO48).sub(invoicenum).sub(rushnum);
					if (tempnum.doubleValue() <= 0) {
						if (bvos[i].getBifinvoicefinish() == null || !bvos[i].getBifinvoicefinish().booleanValue()) {
							bvos[i].setBifinvoicefinish(uftrue);
							bvos[i].setStatus(VOStatus.UPDATED);
							/** added by liubing 处理开票结束对三个应收的影响* */
							invoiceendvolist.add(bvos[i]);
						}
					} else {
						if (bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()) {

							if (bvos[i].getNtotalinvoicenumber_old() != null) {
								ntotalinvoicenumber_old = bvos[i].getNtotalinvoicenumber_old().abs();

								/** 订单数量-开票数量-对冲数量 from V502* */
								tempnum = num.multiply(SO48).sub(ntotalinvoicenumber_old).sub(rushnum);

								if (tempnum.doubleValue() <= 0) {
									bvos[i].setBifinvoicefinish(uffalse);
									bvos[i].setStatus(VOStatus.UPDATED);
									/** added by liubing 处理开票结束对三个应收的影响* */
									invoiceopenvolist.add(bvos[i]);
								}
							} else {
								bvos[i].setBifinvoicefinish(uffalse);
								bvos[i].setStatus(VOStatus.UPDATED);
								/** added by liubing 处理开票结束对三个应收的影响* */
								invoiceopenvolist.add(bvos[i]);
							}
						}
					}
				}
			}// if (SO47.booleanValue())

		}// for (int i = 0, loop = bvos.length; i < loop; i++)

		processInvoicendStateInner(bvos);

	}

	/**
	 * 订单开票结束（内部调用）
	 * 
	 * @param bvos
	 *            --- 待处理的所有bvos
	 * @param rowendvolist
	 *            行状态结束的行
	 * @param rowopenvolist
	 *            行状态打开的行
	 */
	private void processInvoicendStateInner(SaleorderBVO[] bvos) throws Exception {

		// 记录行状态结束和打开的行
		ArrayList<SaleorderBVO> rowendvolist = new ArrayList<SaleorderBVO>();
		ArrayList<SaleorderBVO> rowopenvolist = new ArrayList<SaleorderBVO>();

		// 处理整行状态
		for (int i = 0, loop = bvos.length; i < loop; i++) {

			if (bvos[i].getBifinventoryfinish() != null && bvos[i].getBifinventoryfinish().booleanValue()
					&& bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue()
					&& bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()
					&& bvos[i].getBsquareendflag() != null && bvos[i].getBsquareendflag().booleanValue()) {

				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.AUDIT) {
					rowendvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.FINISH));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			} else {
				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.FINISH) {
					rowopenvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.AUDIT));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			}
		}

		// 根据vo状态，选出发生状态发生变化的vo
		ArrayList updatebvolist = new ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getStatus() == VOStatus.UPDATED) {
				updatebvolist.add(bvos[i]);
			}
		}

		int count = updatebvolist.size();
		if (count <= 0)
			return;
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) updatebvolist.toArray(new SaleorderBVO[count]);
    
		String[] updatefields = { "bifinvoicefinish" };
		String[] wherefields = new String[] { "corder_bid" };

		// 更新执行表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute",
				new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

		updatefields = new String[] { "frowstatus" };
		// 更新子表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields,
				wherefields);

		// 查询订单表头

		SaleorderHVO[] hvos = (SaleorderHVO[]) queryHeadDataForUpdateStatus(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(
				bvos, "csaleid"));

		SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

		// 处理整单状态
		SaleorderBVO[] tempbvos = null;
		for (int i = 0, loop = vos.length; i < loop; i++) {
			vos[i].getHeadVO().setStatus(VOStatus.UNCHANGED);
			tempbvos = vos[i].getBodyVOs();
			for (int j = 0, loopj = tempbvos.length; j < loopj; j++) {
				// 处理整单状态
				if (tempbvos[j].getFrowstatus() == null || tempbvos[j].getFrowstatus().intValue() == BillStatus.AUDIT) {
					// 如果订单的表体存在状态为审批的行，并且表头的状态为完成，则打开表头状态为审批
					if (vos[i].getHeadVO().getFstatus() != null
							&& vos[i].getHeadVO().getFstatus().intValue() == BillStatus.FINISH) {
						vos[i].getHeadVO().setFstatus(new Integer(BillStatus.AUDIT));
						vos[i].getHeadVO().setStatus(VOStatus.UPDATED);
					}
				}
			}
		}

		// 处理整单开票结束
		setBillStatus(hvos, "binvoicendflag");

		// 将订单表头分为两类：（1）本次更新导致订单行frowstatus打开,则必然导致订单表头fstatus打开，
		// （2）剩余的则为订单行关闭的行，它们可能导致订单头状态fstatus结束,也可能不会（如果其他未更新的订单行为结束）
		ArrayList openhvolist = new ArrayList();
		ArrayList closehvolist = new ArrayList();
		for (int i = 0, loop = vos.length; i < loop; i++) {
			if (vos[i].getHeadVO().getStatus() == VOStatus.UPDATED) {
				openhvolist.add(vos[i].getHeadVO());
			} else {
				closehvolist.add(vos[i].getHeadVO());
			}
		}
		// 更新整单状态
		if (openhvolist.size() > 0) {
			String headsql = " update so_sale set fstatus = " + nc.ui.pub.bill.BillStatus.AUDIT + " where csaleid = ? ";
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch((SaleorderHVO[]) openhvolist
					.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
		}

		if (closehvolist.size() > 0) {
			setBillStatus((SaleorderHVO[]) closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
		}

		// 处理行状态结束的行
		if (rowendvolist.size() > 0) {
			// 更新合同数量
			setSaleCT((SaleorderBVO[]) rowendvolist.toArray(new SaleorderBVO[rowendvolist.size()]),
					ISaleOrderAction.A_CLOSE);
		}

		// 处理行状态打开的行
		if (rowopenvolist.size() > 0) {
			// 更新合同数量
			setSaleCT((SaleorderBVO[]) rowopenvolist.toArray(new SaleorderBVO[rowopenvolist.size()]),
					ISaleOrderAction.A_OPEN);
		}
    /**销售发票自动开票打开，联动销售发票自动结算打开 begin **/
    ArrayList<String> autoinvoiceopen = new ArrayList<String>();
    for(SaleorderBVO orderbvo:ordbvos){
      if(null == orderbvo.getBifinvoicefinish()
          || ! orderbvo.getBifinvoicefinish().booleanValue())
        autoinvoiceopen.add(orderbvo.getCorder_bid());
    }
    if(autoinvoiceopen.size() >0){
    SaleordBalEndVO saleordbalvo = new SaleordBalEndVO(SOGenMethod.getBSDate().toString(), SOGenMethod.getBSUser(),
    		SOGenMethod.getBSCorp(), autoinvoiceopen.toArray(new String[0]), SaleOrdBalConst.ISALEORDBAL_TRIG_32OPEN);

    ISaleOrdBalEndSrv saleordbalendsrv = (ISaleOrdBalEndSrv) nc.bs.framework.common.NCLocator.getInstance()
        .lookup(ISaleOrdBalEndSrv.class.getName());

    saleordbalendsrv.processAutoOpen(saleordbalvo);
    }
    /**销售发票自动开票打开，联动销售发票自动结算打开 end **/
	}

	/**
	 * 处理订单状态，不更新相关业务信息的更新（订单修订时调用）。
	 * 
	 * 创建日期：(2004-4-24 11:09:56)
	 * 
	 */
	public void processOrdState(nc.vo.pub.AggregatedValueObject vo) throws Exception {

		if (vo == null)
			return;
		// 获取修订后的订单VO
		SaleOrderVO ordvo = (SaleOrderVO) vo;
		if (ordvo == null || ordvo.getParentVO() == null || ordvo.getChildrenVO() == null
				|| ordvo.getChildrenVO().length <= 0)
			return;
		SaleorderBVO[] bvos = ordvo.getBodyVOs();
		SaleorderHVO hvo = ordvo.getHeadVO();

		SaleorderBVO[] old_bvos = null;

		// liubing 修改三个应收
		// SaleorderHVO old_hvo = null;

		try {
			old_bvos = (SaleorderBVO[]) nc.vo.scm.pub.smart.ObjectUtils.serializableClone(bvos);
			// old_hvo = (SaleorderHVO) nc.vo.scm.pub.smart.ObjectUtils
			// .serializableClone(hvo);
		} catch (Exception e) {
			throw new BusinessException("remote call", e);
		}

		UFDouble ufzero = new UFDouble(0);
		UFBoolean uftrue = new UFBoolean(true);
		UFBoolean uffalse = new UFBoolean(false);
		UFDouble num = null, outnum = null, receiptnum = null;
		UFDouble ntranslossnum = null, invoicenum = null, summny = null;
		UFDouble yetmny = null, tempnum = null, tempmny = null;

		// UFDouble outendrate = getOutSendEndRate(bvos[0].getPkcorp());

		// 纪律出库，发货结束，运输结束，开票结束，收款结束，行结束的行数
		int outfinishnum = 0, receiptfinishnum = 0, transfinishnum = 0, invoicefinishnum = 0, payfinishnum = 0, rowfinishnum = 0;
		UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);

		for (int i = 0, loop = bvos.length; i < loop; i++) {
			bvos[i].setStatus(VOStatus.UNCHANGED);

			num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();
			outnum = bvos[i].getNtotalinventorynumber() == null ? ufzero : bvos[i].getNtotalinventorynumber();

			ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

			// 如果订单数量*出库结束比例-出库数量+运输货损数量+运输退货数量<0,则出库结束
			num = num.abs();
			outnum = outnum.abs();
			ntranslossnum = ntranslossnum.abs();

			// 检查订单修订后的数量
			// tempnum1 = num.sub(outnum);
			// if (tempnum1 != null && tempnum1.doubleValue() < 0) {
			// throw new nc.vo.pub.BusinessException(
			// NCLangResOnserver.getInstance().getStrByID("40060301","UPP40060301-000222",null,new
			// String[]{""+(i+1)})
			// /*"修订后的数量不能小于累计出库数量"*/);
			// }

			// 处理出库状态
			// 如果订单数量*出库结束比例-出库数量+运输货损数量+运输退货数量<0,则出库结束
			tempnum = num.sub(outnum).add(ntranslossnum);
			if (num.doubleValue() > 0 && tempnum.doubleValue() <= 0) {
				// 如果出库结束，则自动结束发运
				if (SO47.booleanValue()) {
					bvos[i].setBifinventoryfinish(uftrue);
					bvos[i].setBifreceivefinish(uftrue);
				}

			} else {
				if (SO47.booleanValue()) {
					bvos[i].setBifinventoryfinish(uffalse);
				}
			}

			// 处理发运状态
			receiptnum = bvos[i].getNtotalreceivenumber() == null ? ufzero : bvos[i].getNtotalreceivenumber();
			receiptnum = receiptnum.abs();

			// tempnum1 = num.sub(receiptnum);
			// if (tempnum1 != null && tempnum1.doubleValue() < 0) {
			// throw new nc.vo.pub.BusinessException(
			// NCLangResOnserver.getInstance().getStrByID("40060301","UPP40060301-000218",null,new
			// String[]{""+(i+1)})
			// /*"修订后的数量不能小于累计发运数量"*/);
			// }
			// 如果订单数量*出库(发货)结束比例-发货数量+运输货损数量+运输退货数量<0,则发货结束
			tempnum = num.sub(receiptnum).add(ntranslossnum);
			if (num.doubleValue() > 0 && tempnum.doubleValue() <= 0) {
				bvos[i].setBifreceivefinish(uftrue);
			} else {
				// 只有出库状态为打开时，才能打开发运
				if (!bvos[i].getBifinventoryfinish().booleanValue())
					bvos[i].setBifreceivefinish(uffalse);
			}

			// 处理开票状态
			invoicenum = bvos[i].getNtotalinvoicenumber() == null ? ufzero : bvos[i].getNtotalinvoicenumber();
			invoicenum = invoicenum.abs();
			tempnum = num.sub(invoicenum);
			// 如果订单数量-开票主数量<=0
			// if (tempnum != null && tempnum.doubleValue() < 0) {
			// throw new nc.vo.pub.BusinessException(
			// NCLangResOnserver.getInstance().getStrByID("40060301","UPP40060301-000221",null,new
			// String[]{""+(i+1)})
			// /*"修订后的数量不能小于累计开票数量"*/);
			// }
			// 如果订单数量-开票主数量=0,则开票结束
			if (num.doubleValue() > 0 && tempnum.doubleValue() == 0) {
				bvos[i].setBifinvoicefinish(uftrue);
			} else {
				if (num.doubleValue() == 0) {
					// 本币价税合计
					UFDouble nsummny = bvos[i].getNsummny() == null ? ufzero : bvos[i].getNsummny();

					// 已开票金额
					UFDouble ntotalinvoicemny = bvos[i].getNtotalinvoicemny() == null ? ufzero : bvos[i]
							.getNtotalinvoicemny();

					// 如果订单价税合计-已开票金额<=0,则开票结束
					nsummny = nsummny.abs();
					ntotalinvoicemny = ntotalinvoicemny.abs();

					tempnum = nsummny.sub(ntotalinvoicemny);
					if (nsummny.doubleValue() > 0 && tempnum.doubleValue() <= 0) {
						if (bvos[i].getBifinvoicefinish() == null || !bvos[i].getBifinvoicefinish().booleanValue()) {
							bvos[i].setBifinvoicefinish(uftrue);
						}
					} else {
						if (bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()) {
							bvos[i].setBifinvoicefinish(uffalse);
						}
					}
				} else {
					bvos[i].setBifinvoicefinish(uffalse);
				}
			}

			// 处理付款状态
			summny = bvos[i].getNsummny() == null ? ufzero : bvos[i].getNsummny();
			yetmny = bvos[i].getNtotalpaymny() == null ? ufzero : bvos[i].getNtotalpaymny();

			// 如果订单价税合计-已付款金额<=0,则付款结束
			summny = summny.abs();
			yetmny = yetmny.abs();

			tempmny = summny.sub(yetmny);
			// if (tempmny.doubleValue() < 0) {
			// throw new nc.vo.pub.BusinessException("修订后的价税合计不能小于累计付款金额");
			// }

			// 如果订单价税合计-已付款金额=0,则付款结束
			if (summny.doubleValue() > 0 && tempmny.doubleValue() == 0) {
				bvos[i].setBifpayfinish(uftrue);
			} else {
				bvos[i].setBifpayfinish(uffalse);
			}

			// 处理运输状态
			// 订货主数量-（运输主数量-运输退货主数量-运输货损主数量）<=0,则运输结束
			ntranslossnum = bvos[i].getNtaltransnum() == null ? ufzero : bvos[i].getNtaltransnum();
			ntranslossnum = ntranslossnum.abs();
			tempnum = num.sub(ntranslossnum).add(ntranslossnum);
			if (tempnum.doubleValue() <= 0) {
				bvos[i].setBiftransfinish(uftrue);
			} else {
				bvos[i].setBiftransfinish(uffalse);
			}

			// 检查退货
			// UFDouble ntotalreturnnumber = bvos[i].getNtotalreturnnumber();
			// if (ntotalreturnnumber != null
			// && ntotalreturnnumber.doubleValue() > outnum.doubleValue()) {
			// throw new nc.vo.pub.BusinessException("累计出库数量不能小于累计退货数量");
			// }

			// 处理整行状态
			if (bvos[i].getBifinventoryfinish() != null && bvos[i].getBifinventoryfinish().booleanValue()
					&& bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue()
					&& bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()
			// 取消收款结束判断条件 v51需求
			// && bvos[i].getBifpayfinish() != null
			// && bvos[i].getBifpayfinish().booleanValue()
			) {

				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.AUDIT) {

					bvos[i].setFrowstatus(new Integer(BillStatus.FINISH));

				}
			} else {
				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.FINISH) {

					bvos[i].setFrowstatus(new Integer(BillStatus.AUDIT));

				}
			}

			if (bvos[i].getBifinventoryfinish() != null && bvos[i].getBifinventoryfinish().booleanValue())
				outfinishnum++;

			if (bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue())
				receiptfinishnum++;

			if (bvos[i].getBiftransfinish() != null && bvos[i].getBiftransfinish().booleanValue())
				transfinishnum++;

			if (bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue())
				invoicefinishnum++;

			if (bvos[i].getBifpayfinish() != null && bvos[i].getBifpayfinish().booleanValue())
				payfinishnum++;

			if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.FINISH)
				rowfinishnum++;
		}

		String[] updatefields = { "bifreceivefinish", "bifinventoryfinish", "biftransfinish", "bifinvoicefinish",
				"bifpayfinish" };
		String[] wherefields = new String[] { "corder_bid" };

		// 更新执行表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(bvos, updatefields, updatefields, "so_saleexecute", wherefields,
				new String[] { "csale_bid" });

		updatefields = new String[] { "frowstatus" };
		// 更新子表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(bvos, updatefields, updatefields, "so_saleorder_b", wherefields,
				wherefields);

		// 处理表头状态
		if (outfinishnum >= bvos.length) {
			hvo.setBoutendflag(uftrue);
		} else {
			hvo.setBoutendflag(uffalse);
		}
		if (receiptfinishnum >= bvos.length) {
			hvo.setBreceiptendflag(uftrue);
		} else {
			hvo.setBreceiptendflag(uffalse);
		}
		if (transfinishnum >= bvos.length) {
			hvo.setBtransendflag(uftrue);
		} else {
			hvo.setBtransendflag(uffalse);
		}
		if (invoicefinishnum >= bvos.length) {
			hvo.setBinvoicendflag(uftrue);
		} else {
			hvo.setBinvoicendflag(uffalse);
		}
		if (payfinishnum >= bvos.length) {
			hvo.setBpayendflag(uftrue);
		} else {
			hvo.setBpayendflag(uffalse);
		}
		if (rowfinishnum >= bvos.length) {
			if (hvo.getFstatus() == null || hvo.getFstatus().intValue() == BillStatus.AUDIT)
				hvo.setFstatus(new Integer(BillStatus.FINISH));
		} else {
			if (hvo.getFstatus() == null || hvo.getFstatus().intValue() == BillStatus.FINISH)
				hvo.setFstatus(new Integer(BillStatus.AUDIT));
		}
		// 更新表头状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(new SaleorderHVO[] { hvo }, new String[] { "binvoicendflag",
				"breceiptendflag", "boutendflag", "btransendflag", "bpayendflag", "fstatus" }, "so_sale",
				new String[] { "csaleid" });

		// 更新应收
		updateArByOrdRowsForOrderAlter(hvo, old_bvos, bvos, false);

	}

	private void updateArByOrdRowsForOrderAlter(SaleorderHVO hvo,
			SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos,
			boolean bupdateCT) throws Exception {
		String[] biztypes = new String[oldordbvos.length];
		String biztype = hvo.getCbiztype();
		for (int i = 0; i < oldordbvos.length; i++) {
			biztypes[i] = biztype;
		}
		updateArByOrdRows(new SaleorderHVO[] { hvo }, oldordbvos, curordbvos,
				bupdateCT, biztypes);
	}
	
	/**
	 * 处理订单付款状态，同时处理相关业务信息的更新。 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param bvos
	 *            nc.vo.so.so001.SaleorderBVO[]
	 * @param changestate
	 *            java.util.ArrayList
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void processPayState(SaleorderBVO[] bvos) throws javax.naming.NamingException, java.sql.SQLException,
			nc.bs.pub.SystemException, nc.vo.pub.BusinessException {
		// 取消收款关闭 v51需求 xiaxin
		return;
	}

	/**
	 * 处理订单发货状态，同时处理相关业务信息的更新。
	 * 
	 * 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param bvos
	 *            nc.vo.so.so001.SaleorderBVO[]
	 * @param changestate
	 *            java.util.ArrayList
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void processReceiptfinishState(SaleorderBVO[] bvos) throws Exception {
		if (bvos == null || bvos.length == 0)
			return;

		SaleorderBVO[] bakbvos = (SaleorderBVO[]) nc.vo.so.so016.SoVoTools.getVOsByVOs("nc.vo.so.so001.SaleorderBVO",
				bvos, bvos[0].getAttributeNames(), bvos[0].getAttributeNames());

		UFDouble ufzero = new UFDouble(0);
		UFBoolean uftrue = new UFBoolean(true);
		UFBoolean uffalse = new UFBoolean(false);
		UFDouble num = null, receiptnum = null, ntranslossnum = null, tempnum = null;

		// UFDouble outendrate = getOutSendEndRate(bvos[0].getPkcorp());

		// 记录发货结束和打开的行
		ArrayList receiptendvolist = new ArrayList();
		ArrayList receiptopenvolist = new ArrayList();
		// 记录行状态结束和打开的行
		ArrayList rowendvolist = new ArrayList();
		ArrayList rowopenvolist = new ArrayList();
		UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);

		for (int i = 0, loop = bvos.length; i < loop; i++) {
			bvos[i].setStatus(VOStatus.UNCHANGED);
			bvos[i].setCreceipttype("30");
			bakbvos[i].setCreceipttype("30");

			num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();
			receiptnum = bvos[i].getNtotalreceivenumber() == null ? ufzero : bvos[i].getNtotalreceivenumber();

			ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

			// 如果订单数量*出库(发货)结束比例-发货数量+运输货损数量<0,则发货结束
			num = num.abs();
			receiptnum = receiptnum.abs();

			ntranslossnum = ntranslossnum.abs();
			tempnum = num.sub(receiptnum).add(ntranslossnum);
			if (tempnum.doubleValue() <= 0) {
				if (bvos[i].getBifreceivefinish() == null || !bvos[i].getBifreceivefinish().booleanValue()) {
					if (SO47.booleanValue()) {
						receiptendvolist.add(bvos[i]);
						bvos[i].setBifreceivefinish(uftrue);
						bvos[i].setStatus(VOStatus.UPDATED);
					}
				}
			} else {
				if (bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue()) {
					receiptopenvolist.add(bvos[i]);
					bvos[i].setBifreceivefinish(uffalse);
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			}
			// 处理整行状态
			if (bvos[i].getBifinventoryfinish() != null && bvos[i].getBifinventoryfinish().booleanValue()
					&& bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue()
					&& bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()
			// && bvos[i].getBifpayfinish() != null
			// && bvos[i].getBifpayfinish().booleanValue()
			) {

				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.AUDIT) {
					rowendvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.FINISH));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			} else {
				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.FINISH) {
					rowopenvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.AUDIT));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			}
		}

		// 根据vo状态，选出发生状态发生变化的vo
		ArrayList updatebvolist = new ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getStatus() == VOStatus.UPDATED) {
				updatebvolist.add(bvos[i]);
			}
		}

		int count = updatebvolist.size();
		if (count <= 0)
			return;
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) updatebvolist.toArray(new SaleorderBVO[count]);

		String[] updatefields = { "bifreceivefinish" };
		String[] wherefields = new String[] { "corder_bid" };

		// 更新执行表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute",
				new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

		updatefields = new String[] { "frowstatus" };
		// 更新子表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields,
				wherefields);

		// 查询订单表头

		SaleorderHVO[] hvos = (SaleorderHVO[]) queryHeadDataForUpdateStatus(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(
				bvos, "csaleid"));

		SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

		// 处理整单状态
		SaleorderBVO[] tempbvos = null;
		for (int i = 0, loop = vos.length; i < loop; i++) {
			vos[i].getHeadVO().setStatus(VOStatus.UNCHANGED);
			tempbvos = vos[i].getBodyVOs();
			for (int j = 0, loopj = tempbvos.length; j < loopj; j++) {
				// 处理整单状态
				if (tempbvos[j].getFrowstatus() == null || tempbvos[j].getFrowstatus().intValue() == BillStatus.AUDIT) {
					// 如果订单的表体存在状态为审批的行，并且表头的状态为完成，则打开表头状态为审批
					if (vos[i].getHeadVO().getFstatus() != null
							&& vos[i].getHeadVO().getFstatus().intValue() == BillStatus.FINISH) {
						vos[i].getHeadVO().setFstatus(new Integer(BillStatus.AUDIT));
						vos[i].getHeadVO().setStatus(VOStatus.UPDATED);
					}
				}
			}
		}

		// 处理整单发货结束
		setBillStatus(hvos, "breceiptendflag");

		// 将订单表头分为两类：（1）本次更新导致订单行frowstatus打开,则必然导致订单表头fstatus打开，
		// （2）剩余的则为订单行关闭的行，它们可能导致订单头状态fstatus结束,也可能不会（如果其他未更新的订单行为结束）
		ArrayList openhvolist = new ArrayList();
		ArrayList closehvolist = new ArrayList();
		for (int i = 0, loop = vos.length; i < loop; i++) {
			if (vos[i].getHeadVO().getStatus() == VOStatus.UPDATED) {
				openhvolist.add(vos[i].getHeadVO());
			} else {
				closehvolist.add(vos[i].getHeadVO());
			}
		}
		// 更新整单状态
		if (openhvolist.size() > 0) {
			String headsql = " update so_sale set fstatus = " + nc.ui.pub.bill.BillStatus.AUDIT + " where csaleid = ? ";
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch((SaleorderHVO[]) openhvolist
					.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
		}

		if (closehvolist.size() > 0) {
			setBillStatus((SaleorderHVO[]) closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
		}

		// 更新相关业务数据

		// 2更新应收及信用
		// 处理行状态结束的行
		if (rowendvolist.size() > 0) {
			// 更新合同数量
			setSaleCT((SaleorderBVO[]) rowendvolist.toArray(new SaleorderBVO[rowendvolist.size()]),
					ISaleOrderAction.A_CLOSE);
		}

		// 处理行状态打开的行
		if (rowopenvolist.size() > 0) {
			// 更新合同数量
			setSaleCT((SaleorderBVO[]) rowopenvolist.toArray(new SaleorderBVO[rowopenvolist.size()]),
					ISaleOrderAction.A_OPEN);
		}

	}

	/**
	 * 处理订单运输状态，同时处理相关业务信息的更新。 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param bvos
	 *            nc.vo.so.so001.SaleorderBVO[]
	 * @param changestate
	 *            java.util.ArrayList
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void processTransState(SaleorderBVO[] bvos) throws Exception {
		if (bvos == null || bvos.length == 0)
			return;
		UFBoolean SO47 = getParaUFBooleanValue(bvos[0].getPkcorp(), "SO47", null);

		UFDouble ufzero = new UFDouble(0);
		UFBoolean uftrue = new UFBoolean(true);
		UFBoolean uffalse = new UFBoolean(false);
		UFDouble num = null, transnum = null, ntranslossnum = null, tempnum = null;
		// UFDouble outendrate = new UFDouble(1);

		// 记录行状态结束和打开的行
		ArrayList rowendvolist = new ArrayList();
		ArrayList rowopenvolist = new ArrayList();

		for (int i = 0, loop = bvos.length; i < loop; i++) {

			bvos[i].setStatus(VOStatus.UNCHANGED);
			bvos[i].setCreceipttype("30");

			if (SO47.booleanValue()) {
				num = bvos[i].getNnumber() == null ? ufzero : bvos[i].getNnumber();
				ntranslossnum = bvos[i].getNtaltransnum() == null ? ufzero : bvos[i].getNtaltransnum();

				ntranslossnum = bvos[i].getNtranslossnum() == null ? ufzero : bvos[i].getNtranslossnum();

				// 订货主数量-（运输主数量-运输退货主数量-运输货损主数量）<=0,则运输结束
				num = num.abs();
				transnum = transnum.abs();

				ntranslossnum = ntranslossnum.abs();
				tempnum = num.sub(ntranslossnum).add(ntranslossnum);
				if (tempnum.doubleValue() <= 0) {
					if (bvos[i].getBiftransfinish() == null || !bvos[i].getBiftransfinish().booleanValue()) {
						bvos[i].setBiftransfinish(uftrue);
						bvos[i].setStatus(VOStatus.UPDATED);
					}
				} else {
					if (bvos[i].getBiftransfinish() != null && bvos[i].getBiftransfinish().booleanValue()) {
						bvos[i].setBiftransfinish(uffalse);
						bvos[i].setStatus(VOStatus.UPDATED);
					}
				}
			}
			// 处理整行状态
			if (bvos[i].getBifinventoryfinish() != null && bvos[i].getBifinventoryfinish().booleanValue()
					&& bvos[i].getBifreceivefinish() != null && bvos[i].getBifreceivefinish().booleanValue()
					&& bvos[i].getBifinvoicefinish() != null && bvos[i].getBifinvoicefinish().booleanValue()
			// && bvos[i].getBifpayfinish() != null
			// && bvos[i].getBifpayfinish().booleanValue()
			) {

				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.AUDIT) {
					rowendvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.FINISH));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			} else {
				if (bvos[i].getFrowstatus() != null && bvos[i].getFrowstatus().intValue() == BillStatus.FINISH) {
					rowopenvolist.add(bvos[i]);
					bvos[i].setFrowstatus(new Integer(BillStatus.AUDIT));
					bvos[i].setStatus(VOStatus.UPDATED);
				}
			}
		}

		// 根据vo状态，选出发生状态发生变化的vo
		ArrayList updatebvolist = new ArrayList();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getStatus() == VOStatus.UPDATED) {
				updatebvolist.add(bvos[i]);
			}
		}

		int count = updatebvolist.size();
		if (count <= 0)
			return;
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) updatebvolist.toArray(new SaleorderBVO[count]);

		String[] updatefields = { "biftransfinish" };
		String[] wherefields = new String[] { "corder_bid" };

		// 更新执行表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleexecute",
				new String[] { "corder_bid", "creceipttype" }, new String[] { "csale_bid", "creceipttype" });

		updatefields = new String[] { "frowstatus" };
		// 更新子表状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, updatefields, updatefields, "so_saleorder_b", wherefields,
				wherefields);

		// 查询订单表头

		SaleorderHVO[] hvos = (SaleorderHVO[]) queryHeadDataForUpdateStatus(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(
				bvos, "csaleid"));

		SaleOrderVO[] vos = toSaleOrderVO(hvos, Arrays.asList(bvos));

		// 处理整单状态
		SaleorderBVO[] tempbvos = null;
		for (int i = 0, loop = vos.length; i < loop; i++) {
			vos[i].getHeadVO().setStatus(VOStatus.UNCHANGED);
			tempbvos = vos[i].getBodyVOs();
			for (int j = 0, loopj = tempbvos.length; j < loopj; j++) {
				// 处理整单状态
				if (tempbvos[j].getFrowstatus() == null || tempbvos[j].getFrowstatus().intValue() == BillStatus.AUDIT) {
					// 如果订单的表体存在状态为审批的行，并且表头的状态为完成，则打开表头状态为审批
					if (vos[i].getHeadVO().getFstatus() != null
							&& vos[i].getHeadVO().getFstatus().intValue() == BillStatus.FINISH) {
						vos[i].getHeadVO().setFstatus(new Integer(BillStatus.AUDIT));
						vos[i].getHeadVO().setStatus(VOStatus.UPDATED);
					}
				}
			}
		}

		// 处理整单开票结束
		setBillStatus(hvos, "btransendflag");

		// 将订单表头分为两类：（1）本次更新导致订单行frowstatus打开,则必然导致订单表头fstatus打开，
		// （2）剩余的则为订单行关闭的行，它们可能导致订单头状态fstatus结束,也可能不会（如果其他未更新的订单行为结束）
		ArrayList openhvolist = new ArrayList();
		ArrayList closehvolist = new ArrayList();
		for (int i = 0, loop = vos.length; i < loop; i++) {
			if (vos[i].getHeadVO().getStatus() == VOStatus.UPDATED) {
				openhvolist.add(vos[i].getHeadVO());
			} else {
				closehvolist.add(vos[i].getHeadVO());
			}
		}
		// 更新整单状态
		if (openhvolist.size() > 0) {
			String headsql = " update so_sale set fstatus = " + nc.ui.pub.bill.BillStatus.AUDIT + " where csaleid = ? ";
			nc.impl.scm.so.so016.SOToolsDMO.updateBatch((SaleorderHVO[]) openhvolist
					.toArray(new SaleorderHVO[openhvolist.size()]), new String[] { "csaleid" }, headsql);
		}

		if (closehvolist.size() > 0) {
			setBillStatus((SaleorderHVO[]) closehvolist.toArray(new SaleorderHVO[closehvolist.size()]), "fstatus");
		}

	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryAllBodyDataByBIDsAndFreeItems(String[] bodykeys)
			throws BusinessException {
		// 查询数据
		nc.vo.pub.CircularlyAccessibleValueObject[] bvos = queryAllBodyDataByBIDs(bodykeys);

		if (bvos == null || bvos.length <= 0)
			return bvos;
		// 查询自由项
		try {
			initFreeItem(bvos);
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		return bvos;
	}

	public SaleorderBVO[] queryAllBodyDataByIDsAndFreeItems(String[] hkeys) throws Exception {

		// 查询数据
		SaleorderBVO[] bvos = queryAllBodyDataByHIDs(hkeys);

		if (bvos == null || bvos.length <= 0)
			return bvos;

		// 查询自由项
		initFreeItem(bvos);

		return bvos;
	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyDataForUpdateStatus(String[] bodykeys)
			throws SQLException {

		if (bodykeys == null || bodykeys.length <= 0)
			return null;

		long s = System.currentTimeMillis();

		String sql =

		"SELECT so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype, "
				+ " so_saleorder_b.csourcebillid, so_saleorder_b.csourcebillbodyid, so_saleorder_b.cinventoryid, "
				+ " so_saleorder_b.nnumber, so_saleorder_b.dconsigndate, so_saleorder_b.ddeliverdate, "
				+ " so_saleorder_b.blargessflag, so_saleorder_b.ccurrencytypeid, so_saleorder_b.nexchangeotobrate, "
				+ " so_saleorder_b.bdericttrans, so_saleorder_b.nnetprice, so_saleorder_b.ntaxnetprice, "
				+ " so_saleorder_b.nmny, so_saleorder_b.nsummny,  so_saleorder_b.frowstatus, "
				+ " so_saleorder_b.cinvbasdocid, so_saleorder_b.ts, so_saleorder_b.cadvisecalbodyid,"
				+ " so_saleorder_b.boosflag, so_saleorder_b.bsupplyflag,  so_saleexecute.ntotalpaymny, "
				+ " so_saleexecute.ntotalreceivenumber, so_saleexecute.ntotalinvoicenumber, so_saleexecute.ntotalinventorynumber, "
				+ " so_saleexecute.ntotalbalancenumber, so_saleexecute.ntotalsignnumber, so_saleexecute.bifinvoicefinish, "
				+ " so_saleexecute.bifreceivefinish, so_saleexecute.bifinventoryfinish, so_saleexecute.bifpayfinish, "
				+ " so_saleexecute.nrushnum, so_saleorder_b.cbodywarehouseid, so_saleexecute.vfree1, "
				+ " so_saleexecute.vfree2, so_saleexecute.vfree3, so_saleexecute.vfree4, "
				+ " so_saleexecute.vfree5, so_saleorder_b.crecwareid,so_saleexecute.ntotalreturnnumber, "
				+ " so_saleorder_b.cconsigncorpid, so_saleorder_b.creccalbodyid, so_saleorder_b.cprolineid, "
				+ " so_saleorder_b.cinventoryid1, so_saleexecute.ntaltransnum, so_saleexecute.ntalbalancemny, "
				+ " so_saleorder_b.cbatchid, so_saleexecute.ntranslossnum, so_saleexecute.biftransfinish, "
				+ " so_saleorder_b.noriginalcurnetprice, so_saleorder_b.noriginalcurtaxnetprice,"
				+ " so_saleorder_b.noriginalcurmny, so_saleorder_b.noriginalcursummny,so_saleorder_b.pk_corp,so_saleexecute.ntotalinvoicemny,so_saleexecute.bsquareendflag "
				+ " FROM so_saleorder_b, so_saleexecute " + " WHERE  "
				+ " so_saleexecute.creceipttype='30' AND so_saleorder_b.csaleid = so_saleexecute.csaleid AND "
				+ " so_saleorder_b.corder_bid = so_saleexecute.csale_bid "
				+ GeneralSqlString.formInSQL("so_saleorder_b.corder_bid", bodykeys);

		SaleorderBVO[] saleItems = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		con = getConnection();
		try {
			stmt = con.prepareStatement(sql);
			// stmt.setString(1, key);
			ResultSet rs = stmt.executeQuery();

			nc.vo.scm.pub.SCMEnv.out("queryBodyDataForUpdateStatus1:" + (System.currentTimeMillis() - s));

			s = System.currentTimeMillis();
			//
			while (rs.next()) {
				SaleorderBVO saleItem = new SaleorderBVO();
				//
				// 1so_saleorder_b.corder_bid, so_saleorder_b.csaleid,
				// so_saleorder_b.pk_corp, so_saleorder_b.creceipttype,
				String corder_bid = rs.getString(1);
				saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());
				//
				String csaleid = rs.getString(2);
				saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());
				//
				String ccorpid = rs.getString(3);
				saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());
				//
				String creceipttype = rs.getString(4);
				saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
				//
				// 2" so_saleorder_b.csourcebillid,
				// so_saleorder_b.csourcebillbodyid,
				// so_saleorder_b.cinventoryid, "
				String csourcebillid = rs.getString(5);
				saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());
				//
				String csourcebillbodyid = rs.getString(6);
				saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());
				//
				String cinventoryid = rs.getString(7);
				saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());
				//
				// "3 so_saleorder_b.nnumber, so_saleorder_b.dconsigndate,
				// so_saleorder_b.ddeliverdate, "
				BigDecimal nnumber = (BigDecimal) rs.getObject(8);
				saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));
				//
				String dconsigndate = rs.getString(9);
				saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));
				//
				String ddeliverdate = rs.getString(10);
				saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));
				//
				// "4 so_saleorder_b.blargessflag,
				// so_saleorder_b.ccurrencytypeid,
				// so_saleorder_b.nexchangeotobrate, "
				String blargessflag = rs.getString(11);
				saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));
				//
				String ccurrencytypeid = rs.getString(12);
				saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());
				//
				BigDecimal nexchangeotobrate = (BigDecimal) rs.getObject(13);
				saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

				// bdericttrans
				String bdericttrans = rs.getString(14);
				saleItem.setBdericttrans(bdericttrans == null ? null : new UFBoolean(bdericttrans.trim()));

				//
				BigDecimal nnetprice = (BigDecimal) rs.getObject(15);
				saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));
				//
				BigDecimal ntaxnetprice = (BigDecimal) rs.getObject(16);
				saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));
				// "6 so_saleorder_b.nmny, so_saleorder_b.nsummny,
				// so_saleorder_b.frowstatus, "
				//
				BigDecimal nmny = (BigDecimal) rs.getObject(17);
				saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));
				//
				BigDecimal nsummny = (BigDecimal) rs.getObject(18);
				saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));
				//
				Integer frowstatus = (Integer) rs.getObject(19);
				saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);
				// 7+" so_saleorder_b.cinvbasdocid, so_saleorder_b.ts,
				// so_saleorder_b.cadvisecalbodyid,"
				//
				String cinvbasdocid = rs.getString(20);
				saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());
				//
				String ts = rs.getString(21);
				saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));
				// cadvisecalbodyid
				String cadvisecalbodyid = rs.getString(22);
				saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());

				// 8+" so_saleorder_b.boosflag, so_saleorder_b.bsupplyflag,
				// so_saleexecute.ntotalpaymny, "
				// boosflag
				String boosflag = rs.getString(23);
				saleItem.setBoosflag(boosflag == null ? null : new UFBoolean(boosflag.trim()));
				// bsupplyflag
				String bsupplyflag = rs.getString(24);
				saleItem.setBsupplyflag(bsupplyflag == null ? null : new UFBoolean(bsupplyflag.trim()));
				//
				BigDecimal ntotalpaymny = (BigDecimal) rs.getObject(25);
				saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));

				// 9+" so_saleexecute.ntotalreceivenumber,
				// so_saleexecute.ntotalinvoicenumber,
				// so_saleexecute.ntotalinventorynumber, "
				// ntotalreceivenumber :
				BigDecimal ntotalreceivenumber = (BigDecimal) rs.getObject(26);
				saleItem.setNtotalreceivenumber(ntotalreceivenumber == null ? null : new UFDouble(ntotalreceivenumber));
				// ntotalinvoicenumber :
				BigDecimal ntotalinvoicenumber = (BigDecimal) rs.getObject(27);
				saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));
				// ntotalinventorynumber :
				BigDecimal ntotalinventorynumber = (BigDecimal) rs.getObject(28);
				saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(
						ntotalinventorynumber));

				// 10+" so_saleexecute.ntotalbalancenumber,
				// so_saleexecute.ntotalsignnumber,
				// so_saleexecute.bifinvoicefinish, "
				// ntotalbalancenumber :
				BigDecimal ntotalbalancenumber = (BigDecimal) rs.getObject(29);
				saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));
				// ntotalsignnumber :
				BigDecimal ntotalsignnumber = (BigDecimal) rs.getObject(30);
				saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));
				// bifinvoicefinish :
				String bifinvoicefinish = rs.getString(31);
				saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));

				// 11" so_saleexecute.bifreceivefinish,
				// so_saleexecute.bifinventoryfinish,
				// so_saleexecute.bifpayfinish, "
				// bifreceivefinish :
				String bifreceivefinish = rs.getString(32);
				saleItem.setBifreceivefinish(bifreceivefinish == null ? null : new UFBoolean(bifreceivefinish.trim()));

				// bifinventoryfinish :
				String bifinventoryfinish = rs.getString(33);
				saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish
						.trim()));
				// bifpayfinish :
				String bifpayfinish = rs.getString(34);
				saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

				BigDecimal nrushnum = (BigDecimal) rs.getObject(35);
				saleItem.setAttributeValue("nrushnum", nrushnum == null ? null : new UFDouble(nrushnum));

				// cbodywarehouseid
				String cbodywarehouseid = rs.getString(36);
				saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());
				// vfree1 :
				String vfree1 = rs.getString(37);
				saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());

				// 13+" so_saleexecute.vfree2, so_saleexecute.vfree3,
				// so_saleexecute.vfree4, "
				// vfree2 :
				String vfree2 = rs.getString(38);
				saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());
				// vfree3 :
				String vfree3 = rs.getString(39);
				saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());
				// vfree4 :
				String vfree4 = rs.getString(40);
				saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());

				// 14+" so_saleexecute.vfree5,
				// so_saleexecute.ntotalreturnnumber, "
				// vfree5 :
				String vfree5 = rs.getString(41);
				saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());

				// crecwareid
				String crecwareid = rs.getString(42);
				saleItem.setCrecwareid(crecwareid == null ? null : crecwareid.trim());

				// ntotalreturnnumber
				BigDecimal ntotalreturnnumber = (BigDecimal) rs.getObject(43);
				saleItem.setNtotalreturnnumber(ntotalreturnnumber == null ? null : new UFDouble(ntotalreturnnumber));

				// 15+" so_saleorder_b.cconsigncorpid,
				// so_saleorder_b.creccalbodyid, so_saleorder_b.cprolineid, "
				String cconsigncorpid = rs.getString(44);
				saleItem.setCconsigncorpid(cconsigncorpid == null ? null : cconsigncorpid.trim());

				String creccalbodyid = rs.getString(45);
				saleItem.setCreccalbodyid(creccalbodyid == null ? null : creccalbodyid.trim());

				String cprolineid = rs.getString(46);
				saleItem.setCprolineid(cprolineid == null ? null : cprolineid.trim());

				// 16+" so_saleorder_b.cinventoryid1,
				// so_saleexecute.ntaltransnum, so_saleexecute.ntalbalancemny, "
				String cinventoryid1 = rs.getString(47);
				saleItem.setCinventoryid1(cinventoryid1 == null ? null : cinventoryid1.trim());

				BigDecimal ntaltransnum = (BigDecimal) rs.getObject(48);
				saleItem.setNtaltransnum(ntaltransnum == null ? null : new UFDouble(ntaltransnum));

				BigDecimal ntalbalancemny = (BigDecimal) rs.getObject(49);
				saleItem.setNtalbalancemny(ntalbalancemny == null ? null : new UFDouble(ntalbalancemny));

				// cbatchid
				String cbatchid = rs.getString(50);
				saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());

				BigDecimal ntranslossnum = (BigDecimal) rs.getObject(51);
				saleItem.setNtranslossnum(ntranslossnum == null ? null : new UFDouble(ntranslossnum));

				String biftransfinish = rs.getString(52);
				saleItem.setBiftransfinish(biftransfinish == null ? null : new UFBoolean(biftransfinish.trim()));

				// 18+" so_saleorder_b.noriginalcurnetprice,
				// so_saleorder_b.noriginalcurtaxnetprice,"
				BigDecimal noriginalcurnetprice = (BigDecimal) rs.getObject(53);
				saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(
						noriginalcurnetprice));

				BigDecimal noriginalcurtaxnetprice = (BigDecimal) rs.getObject(54);
				saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(
						noriginalcurtaxnetprice));

				// 19+" so_saleorder_b.noriginalcurmny,
				// so_saleorder_b.noriginalcursummny "
				BigDecimal noriginalcurmny = (BigDecimal) rs.getObject(55);
				saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));

				BigDecimal noriginalcursummny = (BigDecimal) rs.getObject(56);
				saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));

				String pk_corp = rs.getString(57);
				saleItem.setPkcorp(pk_corp == null ? null : pk_corp.trim());

				// ntotalinvoicemny
				BigDecimal ntotalinvoicemny = (BigDecimal) rs.getObject(58);
				saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));
				String bsquareendflag = rs.getString(59);
				saleItem.setBsquareendflag(bsquareendflag == null ? null : new UFBoolean(bsquareendflag));

				v.addElement(saleItem);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		saleItems = new SaleorderBVO[v.size()];
		if (v.size() > 0) {
			v.copyInto(saleItems);
		}

		nc.vo.scm.pub.SCMEnv.out("queryBodyDataForUpdateStatus2:" + (System.currentTimeMillis() - s));

		/** ********************************************************** */
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.so.so001.SaleOrderDMO", "findItemsForHeader", bodykeys);
		/** ********************************************************** */
		return saleItems;
	}

	/*
	 * 查询订单现收数据 修改日期：2004-05-08 修改人：杨涛
	 * 
	 */
	public UFDouble queryCachPayByOrdId(String csaleid) throws BusinessException {
		OrdVO[] balvos = null;
		try {
			BalanceDMO baldmo = new BalanceDMO();

			balvos = baldmo.queryOrdVO(" so_sale.csaleid ='" + csaleid + "' ");
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		if (balvos != null && balvos.length > 0 && balvos[0] != null)
			return balvos[0].getNorigbalsummny();

		return null;

	}

	/*
	 * 该接口为回写订单状态使用，仅查询订单表头有限的信息。 修改日期：2003-10-16 修改人：杨涛 修改内容：
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryHeadDataForUpdateStatus(String[] ids) throws SQLException {
		if (ids == null || ids.length <= 0)
			return null;

		// 设置其他增加的字段
		SOField[] addfields = SaleorderHVO.getAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getTablename());
					addfieldsql.append(".");
					addfieldsql.append(addfields[i].getDatabasename());
				}
			}
		}
		String strSql = "SELECT so_sale.pk_corp, so_sale.vreceiptcode, so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass, so_sale.finvoicetype,"
				+ "so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate, so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, "
				+ "so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid, so_sale.vreceiveaddress, so_sale.creceiptcorpid, "
				+ "so_sale.ctransmodeid, so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag, so_sale.cfreecustid, "
				+ "so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, "
				+ "so_sale.dapprovedate, so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6, so_sale.vdef7, so_sale.vdef8,"
				+ "so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,so_sale.csaleid,so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,"
				+ "so_sale.breceiptendflag,so_sale.ts,so_sale.npreceiverate,so_sale.npreceivemny,so_sale.bpayendflag "
				+ addfieldsql.toString()
				+ " FROM so_sale WHERE 1=1 "
				+ GeneralSqlString.formInSQL("so_sale.csaleid", ids);

		SaleorderHVO[] saleorderHs = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		con = getConnection();
		try {
			stmt = con.prepareStatement(strSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				SaleorderHVO saleHeader = new SaleorderHVO();
				// pk_corp :
				String pk_corp = rs.getString(1);
				saleHeader.setPk_corp(pk_corp == null ? null : pk_corp.trim());
				// vreceiptcode :
				String vreceiptcode = rs.getString(2);
				saleHeader.setVreceiptcode(vreceiptcode == null ? null : vreceiptcode.trim());
				// creceipttype :
				String creceipttype = rs.getString(3);
				saleHeader.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
				// cbiztype :
				String cbiztype = rs.getString(4);
				saleHeader.setCbiztype(cbiztype == null ? null : cbiztype.trim());
				// finvoiceclass :
				Integer finvoiceclass = (Integer) rs.getObject(5);
				saleHeader.setFinvoiceclass(finvoiceclass == null ? null : finvoiceclass);
				// finvoicetype :
				Integer finvoicetype = (Integer) rs.getObject(6);
				saleHeader.setFinvoicetype(finvoicetype == null ? null : finvoicetype);
				// vaccountyear :
				String vaccountyear = rs.getString(7);
				saleHeader.setVaccountyear(vaccountyear == null ? null : vaccountyear.trim());
				// binitflag :
				String binitflag = rs.getString(8);
				saleHeader.setBinitflag(binitflag == null ? null : new UFBoolean(binitflag.trim()));
				// dbilldate :
				String dbilldate = rs.getString(9);
				saleHeader.setDbilldate(dbilldate == null ? null : new UFDate(dbilldate.trim()));
				// ccustomerid :
				String ccustomerid = rs.getString(10);
				saleHeader.setCcustomerid(ccustomerid == null ? null : ccustomerid.trim());
				// cdeptid :
				String cdeptid = rs.getString(11);
				saleHeader.setCdeptid(cdeptid == null ? null : cdeptid.trim());
				// cemployeeid :
				String cemployeeid = rs.getString(12);
				saleHeader.setCemployeeid(cemployeeid == null ? null : cemployeeid.trim());
				// coperatorid :
				String coperatorid = rs.getString(13);
				saleHeader.setCoperatorid(coperatorid == null ? null : coperatorid.trim());
				// ctermprotocolid :
				String ctermprotocolid = rs.getString(14);
				saleHeader.setCtermprotocolid(ctermprotocolid == null ? null : ctermprotocolid.trim());
				// csalecorpid :
				String csalecorpid = rs.getString(15);
				saleHeader.setCsalecorpid(csalecorpid == null ? null : csalecorpid.trim());
				// creceiptcustomerid :
				String creceiptcustomerid = rs.getString(16);
				saleHeader.setCreceiptcustomerid(creceiptcustomerid == null ? null : creceiptcustomerid.trim());
				// vreceiveaddress :
				String vreceiveaddress = rs.getString(17);
				saleHeader.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());
				// creceiptcorpid :
				String creceiptcorpid = rs.getString(18);
				saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());
				// ctransmodeid :
				String ctransmodeid = rs.getString(19);
				saleHeader.setCtransmodeid(ctransmodeid == null ? null : ctransmodeid.trim());
				// ndiscountrate :
				BigDecimal ndiscountrate = (BigDecimal) rs.getObject(20);
				saleHeader.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));
				// cwarehouseid :
				String cwarehouseid = rs.getString(21);
				saleHeader.setCwarehouseid(cwarehouseid == null ? null : cwarehouseid.trim());
				// veditreason :
				String veditreason = rs.getString(22);
				saleHeader.setVeditreason(veditreason == null ? null : veditreason.trim());
				// bfreecustflag :
				String bfreecustflag = rs.getString(23);
				saleHeader.setBfreecustflag(bfreecustflag == null ? null : new UFBoolean(bfreecustflag.trim()));
				// cfreecustid :
				String cfreecustid = rs.getString(24);
				saleHeader.setCfreecustid(cfreecustid == null ? null : cfreecustid.trim());
				// ibalanceflag :
				Integer ibalanceflag = (Integer) rs.getObject(25);
				saleHeader.setIbalanceflag(ibalanceflag == null ? null : ibalanceflag);
				// nsubscription :
				BigDecimal nsubscription = (BigDecimal) rs.getObject(26);
				saleHeader.setNsubscription(nsubscription == null ? null : new UFDouble(nsubscription));
				// ccreditnum :
				String ccreditnum = rs.getString(27);
				saleHeader.setCcreditnum(ccreditnum == null ? null : ccreditnum.trim());
				// nevaluatecarriage :
				BigDecimal nevaluatecarriage = (BigDecimal) rs.getObject(28);
				saleHeader.setNevaluatecarriage(nevaluatecarriage == null ? null : new UFDouble(nevaluatecarriage));
				// dmakedate :
				String dmakedate = rs.getString(29);
				saleHeader.setDmakedate(dmakedate == null ? null : new UFDate(dmakedate.trim()));
				// capproveid :
				String capproveid = rs.getString(30);
				saleHeader.setCapproveid(capproveid == null ? null : capproveid.trim());
				// dapprovedate :
				String dapprovedate = rs.getString(31);
				saleHeader.setDapprovedate(dapprovedate == null ? null : new UFDate(dapprovedate.trim()));
				// fstatus :
				Integer fstatus = (Integer) rs.getObject(32);
				saleHeader.setFstatus(fstatus == null ? null : fstatus);
				// vnote :
				String vnote = rs.getString(33);
				saleHeader.setVnote(vnote == null ? null : vnote.trim());
				// vdef1 :
				String vdef1 = rs.getString(34);
				saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());
				// vdef2 :
				String vdef2 = rs.getString(35);
				saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());
				// vdef3 :
				String vdef3 = rs.getString(36);
				saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());
				// vdef4 :
				String vdef4 = rs.getString(37);
				saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());
				// vdef5 :
				String vdef5 = rs.getString(38);
				saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());
				// vdef6 :
				String vdef6 = rs.getString(39);
				saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());
				// vdef7 :
				String vdef7 = rs.getString(40);
				saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());
				// vdef8 :
				String vdef8 = rs.getString(41);
				saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());
				// vdef9 :
				String vdef9 = rs.getString(42);
				saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());
				// vdef10 :
				String vdef10 = rs.getString(43);
				saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());
				// ccalbodyid :
				String ccalbodyid = rs.getString(44);
				saleHeader.setCcalbodyid(ccalbodyid == null ? null : ccalbodyid.trim());
				// csaleid :
				String csaleid = rs.getString(45);
				saleHeader.setCsaleid(csaleid == null ? null : csaleid.trim());
				// bretinvflag :
				String bretinvflag = rs.getString(46);
				saleHeader.setBretinvflag(bretinvflag == null ? null : new UFBoolean(bretinvflag.trim()));
				// boutendflag :
				String boutendflag = rs.getString(47);
				saleHeader.setBoutendflag(boutendflag == null ? null : new UFBoolean(boutendflag.trim()));
				// binvoicendflag :
				String binvoicendflag = rs.getString(48);
				saleHeader.setBinvoicendflag(binvoicendflag == null ? null : new UFBoolean(binvoicendflag.trim()));
				// breceiptendflag :
				String breceiptendflag = rs.getString(49);
				saleHeader.setBreceiptendflag(breceiptendflag == null ? null : new UFBoolean(breceiptendflag.trim()));
				// ts
				String ts = rs.getString(50);
				saleHeader.setTs(ts == null ? null : new UFDateTime(ts.trim()));

				// yt add 2003-10-16
				// npreceiverate
				BigDecimal npreceiverate = (BigDecimal) rs.getObject(51);
				saleHeader.setNpreceiverate(npreceiverate == null ? null : new UFDouble(npreceiverate));
				// npreceivemny
				BigDecimal npreceivemny = (BigDecimal) rs.getObject(52);
				saleHeader.setNpreceivemny(npreceivemny == null ? null : new UFDouble(npreceivemny));

				String bpayendflag = rs.getString(53);
				saleHeader.setBpayendflag(bpayendflag == null ? null : new UFBoolean(bpayendflag.trim()));

				// 获取v30后加入的其他字段值

				getOrdHValueFromResultSet(rs, 54, saleHeader);

				// 获取v30后加入的其他字段值
				// if(addfields!=null){
				// for (int i = 0, loop = addfields.length; i < loop; i++) {
				// getValueFromResultSet(rs,54+i,addfields[i],saleHeader);
				// }
				// }

				v.addElement(saleHeader);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		if (v.size() > 0) {
			saleorderHs = new SaleorderHVO[v.size()];
			v.copyInto(saleorderHs);
		}

		return saleorderHs;

	}

	/**
	 * 更新订单表头的状态。
	 * 
	 * 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param String[]
	 *            csaleid
	 * @param String
	 *            statefield
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void setOrdHeadStatus(SaleorderHVO[] hvos) throws Exception {
		if (hvos == null || hvos.length == 0)
			return;
		// 处理整单开票结束

		HashMap statehs = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
						nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
						" bifinvoicefinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
		UFDouble count = null;
		for (int i = 0; i < hvos.length; i++) {
			if (statehs == null) {
				hvos[i].setBinvoicendflag(new UFBoolean(true));
			} else {
				count = (UFDouble) statehs.get(hvos[i].getCsaleid());
				if (count != null && count.doubleValue() > 0) {
					hvos[i].setBinvoicendflag(new UFBoolean(false));
				} else {
					hvos[i].setBinvoicendflag(new UFBoolean(true));
				}
			}
		}
		// nc.bs.so.so016.SOToolsDMO.updateBatch(
		// hvos,
		// new String[] { "binvoicendflag" },
		// "so_sale",
		// new String[] { "csaleid" });

		// 处理整单发货结束

		statehs = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
						nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
						" bifreceivefinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
		for (int i = 0; i < hvos.length; i++) {
			if (statehs == null) {
				hvos[i].setBreceiptendflag(new UFBoolean(true));
			} else {
				count = (UFDouble) statehs.get(hvos[i].getCsaleid());
				if (count != null && count.doubleValue() > 0) {
					hvos[i].setBreceiptendflag(new UFBoolean(false));
				} else {
					hvos[i].setBreceiptendflag(new UFBoolean(true));
				}
			}
		}
		// nc.bs.so.so016.SOToolsDMO.updateBatch(
		// hvos,
		// new String[] { "breceiptendflag" },
		// "so_sale",
		// new String[] { "csaleid" });

		// 处理整单出库结束

		statehs = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
						nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
						" bifinventoryfinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
		for (int i = 0; i < hvos.length; i++) {
			if (statehs == null) {
				hvos[i].setBoutendflag(new UFBoolean(true));
			} else {
				count = (UFDouble) statehs.get(hvos[i].getCsaleid());
				if (count != null && count.doubleValue() > 0) {
					hvos[i].setBoutendflag(new UFBoolean(false));
				} else {
					hvos[i].setBoutendflag(new UFBoolean(true));
				}
			}
		}
		// nc.bs.so.so016.SOToolsDMO.updateBatch(
		// hvos,
		// new String[] { "boutendflag" },
		// "so_sale",
		// new String[] { "csaleid" });

		// 处理整单运输结束

		statehs = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)", "so_saleorder_b.csaleid",
						nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
						" biftransfinish='N' and so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
		for (int i = 0; i < hvos.length; i++) {
			if (statehs == null) {
				hvos[i].setBtransendflag(new UFBoolean(true));
			} else {
				count = (UFDouble) statehs.get(hvos[i].getCsaleid());
				if (count != null && count.doubleValue() > 0) {
					hvos[i].setBtransendflag(new UFBoolean(false));
				} else {
					hvos[i].setBtransendflag(new UFBoolean(true));
				}
			}
		}
		// nc.bs.so.so016.SOToolsDMO.updateBatch(
		// hvos,
		// new String[] { "btransendflag" },
		// "so_sale",
		// new String[] { "csaleid" });

		// 处理整单付款结束

		statehs = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueUFDouble("so_saleexecute,so_saleorder_b", "count(*)",
				"so_saleorder_b.csaleid", nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"),
				"so_saleexecute.csale_bid=so_saleorder_b.corder_bid group by so_saleorder_b.csaleid ");
		for (int i = 0; i < hvos.length; i++) {
			if (statehs == null) {
				hvos[i].setBpayendflag(new UFBoolean(true));
			} else {
				count = (UFDouble) statehs.get(hvos[i].getCsaleid());
				if (count != null && count.doubleValue() > 0) {
					hvos[i].setBpayendflag(new UFBoolean(false));
				} else {
					hvos[i].setBpayendflag(new UFBoolean(true));
				}
			}
		}
		// nc.bs.so.so016.SOToolsDMO.updateBatch(
		// hvos,
		// new String[] { "bpayendflag" },
		// "so_sale",
		// new String[] { "csaleid" });

		// 处理整单状态,如果单据行的状态都为完成，则设置整单状态为完成，否则不处理
		// frowstatus = 6 -- 整单关闭
		statehs = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)", "csaleid",
				nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " frowstatus = 6 group by csaleid ");

		HashMap allstatehs = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueUFDouble("so_saleorder_b", "count(*)",
				"csaleid", nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"), " dr=0 group by csaleid ");

		// 每一个hid---对应的bid个数
		UFDouble allcount = null;
		// 需要整单关闭的销售订单
		ArrayList<SaleorderHVO> updatehvolist = new ArrayList<SaleorderHVO>();

		for (int i = 0; i < hvos.length; i++) {
			if (statehs == null) {
				if (hvos[i].getFstatus().intValue() == nc.ui.pub.bill.BillStatus.FINISH)
					hvos[i].setFstatus(new Integer(nc.ui.pub.bill.BillStatus.AUDIT));
				continue;
			}
			allcount = (UFDouble) allstatehs.get(hvos[i].getCsaleid());
			if (allcount.equals(statehs.get(hvos[i].getCsaleid()))) {
				hvos[i].setFstatus(new Integer(nc.ui.pub.bill.BillStatus.FINISH));
				updatehvolist.add(hvos[i]);
			} else {
				if (hvos[i].getFstatus().intValue() == nc.ui.pub.bill.BillStatus.FINISH)
					hvos[i].setFstatus(new Integer(nc.ui.pub.bill.BillStatus.AUDIT));
			}
		}

		// 更新vo状态
		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(hvos, new String[] { "binvoicendflag", "breceiptendflag",
				"boutendflag", "btransendflag", "bpayendflag", "fstatus" }, "so_sale", new String[] { "csaleid" });

	}

	/**
	 * （整单关闭时调用） 跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开
	 * 
	 * @param vos
	 * @throws BusinessException
	 */
	public void set5XOutEndFlagForAllClose(nc.vo.so.so001.SaleOrderVO[] vos) throws BusinessException {
		UFBoolean ufTrue = new UFBoolean(true);
		for (SaleOrderVO vo : vos)
			SoVoTools.setVOsValue(vo.getBodyVOs(), "bifinventoryfinish", ufTrue);
		set5XOutEndFlag(vos);
	}

	/**
	 * 跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开 业务类型审批动作脚本为：PushSave205A5D vos --
	 * 出库状态发生变化的vo vos中的表体vo -- 仅仅是出库状态发生变化的行
	 * 
	 * @throws BusinessException
	 */
	public void set5XOutEndFlag(nc.vo.so.so001.SaleOrderVO[] vos) throws BusinessException {
		// 跨公司直运销售订单(业务类型审批动作脚本为：PushSave205A5D)
		List<SaleorderBVO> voslist = new ArrayList<SaleorderBVO>();

		SaleOrderTools sotools = new SaleOrderTools();

		// 循环过滤出跨公司直运销售订单
		for (SaleOrderVO avo : vos)
			if (sotools.ifPushSave205A5D(avo.getBizTypeid(), avo.getPk_corp()))
				voslist.addAll(Arrays.asList(avo.getBodyVOs()));

		if (voslist.size() > 0)
			set5XOutEndFlag(voslist.toArray(new SaleorderBVO[voslist.size()]));
	}

	/**
	 * 跨公司直运销售订单行出库关闭/打开，联带调拨订单行出库关闭/打开 
	 * @throws BusinessException
	 */
	private void set5XOutEndFlag(SaleorderBVO[] bvos) throws BusinessException {
		
		//因为传进来得bvos中Bdericttrans有可能为空，所以重新读取信息
		SaleorderBVO[] newbvos = null;
		HashMap<String,SaleorderBVO> map = new HashMap<String,SaleorderBVO>();
		try {
			newbvos = (SaleorderBVO[])queryBodyDataForUpdateStatus(SoVoTools.getVOsOnlyValues(bvos, "corder_bid"));
		    for (int i=0;i<newbvos.length;i++)
		    	map.put(newbvos[i].getPrimaryKey(), newbvos[i]);
		} catch (SQLException e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e);
		}
		
		// 循环准备参数
		// 表体行bids
		String[] bids = null;
		// 出库关闭/打开--true 关闭 ，false 打开
		UFBoolean[] outendflag = null;	
		
		List<String> idList = new ArrayList<String>();
		List<UFBoolean> outendflagList = new ArrayList<UFBoolean>();
		for (int i = 0; i < bvos.length; i++) {
			bvos[i].setBdericttrans(map.get(bvos[i].getPrimaryKey()).getBdericttrans());
			if (bvos[i].getBdericttrans()!=null 
					&& bvos[i].getBdericttrans().booleanValue()){
				idList.add(bvos[i].getPrimaryKey());
				outendflagList.add(bvos[i].getBifinventoryfinish());
			}
		}

		if (idList.size()>0){
			bids = idList.toArray(new String[idList.size()]);
			outendflag = outendflagList.toArray(new UFBoolean[outendflagList.size()]);
			// 调用内部交易接口：调拨订单行出库关闭/打开
			IOuter4SO tobo = (IOuter4SO) NCLocator.getInstance().lookup(IOuter4SO.class.getName());
			tobo.outEnd4SO30(bids, outendflag);
		}
	}

	/**
	 * @return 由表头VO获得所属的AggVO
	 * @throws BusinessException
	 */
	private AggregatedValueObject[] getAggVoFromHVOs(CircularlyAccessibleValueObject[] hvos) throws BusinessException {
		// 查询表头vo对应的表体vo
		SaleorderBVO[] bvos;
		try {
			bvos = (SaleorderBVO[]) queryAllBodyDataByHIDs(nc.vo.so.so016.SoVoTools.getVOsOnlyValues(hvos, "csaleid"));
			// 根据表头、表体vo合成聚合VO
			return RedunVOTool.getBillVos(SaleOrderVO.class.getName(), "csaleid", hvos, bvos);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException("获取聚合VO失败！", e);
		}
	}

	/**
	 * 更新订单最后出库日期，订单最后发货日期，订单最后运输日期，最后开票日期，最后付款日期。
	 * 
	 * 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param String[]
	 *            csaleid
	 * @param String
	 *            statefield
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void setOrdLastDate(String datefield, SaleorderBVO[] ordbvos, UFDate dlastdate) throws Exception {
		if (datefield == null || ordbvos == null || ordbvos.length <= 0)
			return;

		if (dlastdate == null) {
			dlastdate = new UFDate(System.currentTimeMillis());
		}

		if (dlastdate != null) {

			// 更新
			nc.vo.so.so016.SoVoTools.setVOsValue(ordbvos, datefield, dlastdate);

		} else {

			String[] corder_bids = nc.vo.so.so016.SoVoTools.getVOsOnlyValues(ordbvos, "corder_bid");

			HashMap hsdate = null;

			// 最近一次发货日期
			if ("dlastconsigdate".equals(datefield)) {

				// 最近一次运输日期
			} else if ("dlasttransdate".equals(datefield)) {

				// 最近一次出库日期
			} else if ("dlastoutdate".equals(datefield)) {

				hsdate = nc.impl.scm.so.so016.SOToolsDMO
						.getAnyValueSORow(
								"ic_general_h,ic_general_b",
								new String[] { "ic_general_h.dbilldate" },
								"ic_general_b.cfirstbillbid",
								corder_bids,
								" ic_general_h.cgeneralhid=ic_general_b.cgeneralhid and ic_general_h.dr=0 and ic_general_b.cfirsttype='30' order by ic_general_h.dbilldate asc ");

				// 最近一次开票日期
			} else if ("dlastinvoicedt".equals(datefield)) {

				hsdate = nc.impl.scm.so.so016.SOToolsDMO
						.getAnyValueSORow(
								"so_saleinvoice,so_saleinvoice_b",
								new String[] { "so_saleinvoice.dbilldate" },
								"so_saleinvoice_b.csourcebillbodyid",
								corder_bids,
								" so_saleinvoice.csaleid=so_saleinvoice_b.csaleid and so_saleinvoice.dr=0 and so_saleinvoice.creceipttype='30' order by so_saleinvoice.dbilldate asc ");

				// 最近一次收款日期
			} else if ("dlastpaydate".equals(datefield)) {

				hsdate = nc.impl.scm.so.so016.SOToolsDMO
						.getAnyValueSORow(
								"arap_djfb,arap_djclb",
								new String[] { "arap_djclb.clrq" },
								"arap_djfb.cksqsh",
								corder_bids,
								" arap_djfb.ph='30' and arap_djclb.dr=0 and arap_djclb.dydjdl='ys' and arap_djclb.dydjzbid=arap_djfb.vouchid and arap_djclb.dydjfbid=arap_djfb.fb_oid order by arap_djclb.clrq asc ");

			}

			if (hsdate != null) {
				SORowData row = null;
				for (int i = 0, loop = ordbvos.length; i < loop; i++) {
					row = (SORowData) hsdate.get(ordbvos[i].getCorder_bid());
					if (row != null)
						ordbvos[i].setAttributeValue(datefield, row.getUFDate(0));
					else
						ordbvos[i].setAttributeValue(datefield, null);
				}

			}
		}

		nc.vo.so.so016.SoVoTools.setVOsValue(ordbvos, "creceipttype", "30");

		nc.impl.scm.so.so016.SOToolsDMO.updateBatch(ordbvos, new String[] { datefield }, new String[] { datefield },
				"so_saleexecute", new String[] { "corder_bid", "csaleid", "creceipttype" }, new String[] { "csale_bid",
						"csaleid", "creceipttype" });

	}

	/**
	 * 更新订单退货数量。 创建日期：(2004-4-24 11:09:56)
	 * 
	 * @param String[]
	 *            csaleid
	 * @param String
	 *            statefield
	 * @exception javax.naming.NamingException
	 *                异常说明。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 * @exception java.sql.SQLException
	 *                异常说明。
	 * @exception nc.bs.pub.SystemException
	 *                异常说明。
	 * @exception nc.vo.pub.BusinessException
	 *                异常说明。
	 */
	public void setRetNum(SaleOrderVO ordvo) throws javax.naming.NamingException, java.sql.SQLException,
			nc.bs.pub.SystemException, nc.vo.pub.BusinessException {
		if (ordvo == null || ordvo.getChildrenVO() == null || ordvo.getChildrenVO().length == 0)
			return;

		// 检查退货标志
		if (ordvo.getHeadVO().getBretinvflag() == null || !ordvo.getHeadVO().getBretinvflag().booleanValue())
			return;
		UFDouble uf0 = new UFDouble(0);

		// 新增
		if (ordvo.getIAction() == ISaleOrderAction.A_ADD) {

			SaleorderBVO[] ordbvos = ordvo.getBodyVOs();
			HashMap hsordbvo = new HashMap();
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {
				if (ordbvos[i].getCsourcebillbodyid() != null && ordbvos[i].getCsourcebillbodyid().trim().length() > 0)
					hsordbvo.put(ordbvos[i].getCsourcebillbodyid(), ordbvos[i]);
			}

			if (hsordbvo.size() <= 0)
				return;

			// 查询对应的订单行
			SaleorderBVO[] sourceOrdbvos = (SaleorderBVO[]) queryAllBodyDataByBIDs((String[]) hsordbvo.keySet()
					.toArray(new String[hsordbvo.size()]));
			if (sourceOrdbvos == null || sourceOrdbvos.length <= 0)
				return;

			ArrayList retvolist = new ArrayList();
			nc.vo.so.service.ToRetOrdVO retordvo = null;
			SaleorderBVO ordbvo = null;
			UFDouble dtemp = null;
			for (int i = 0, loop = sourceOrdbvos.length; i < loop; i++) {
				ordbvo = (SaleorderBVO) hsordbvo.get(sourceOrdbvos[i].getCorder_bid());
				if (ordbvo == null || ordbvo.getNnumber() == null)
					continue;
				// 计算累计退货数量
				dtemp = nc.vo.so.so016.SoVoTools.getMnyAdd(sourceOrdbvos[i].getNtotalreturnnumber(), ordbvo
						.getNnumber().abs());

				/** 如果业务类型正向销售的核算规则为直运销售采购，不进行此检查 */
				SaleOrderTools sotools = new SaleOrderTools();
				if (!sotools.getVerifyRuleFromBusiTypeByPK(ordvo.getBizTypeid()).equals("Z")) {
					if (dtemp != null
							&& (sourceOrdbvos[i].getNtotalinventorynumber() == null || dtemp.abs().doubleValue() > (sourceOrdbvos[i]
									.getNtotalinventorynumber().abs().doubleValue() - (sourceOrdbvos[i]
									.getNtranslossnum() == null ? 0 : sourceOrdbvos[i].getNtranslossnum().doubleValue()))))
						throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000039")
								/* @res "退货订单的退货数量不能大于订单的订单的累计出库数量-途损数量)" */+ "\n行号：" + ordbvo.getCrowno());
				}
				/** 如果业务类型正向销售的核算规则为直运销售采购，不进行此检查 */

				retordvo = new nc.vo.so.service.ToRetOrdVO();
				retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
				retordvo.setNtotalreturnnumber(ordbvo.getNnumber().abs());
				retvolist.add(retordvo);
			}
			if (retvolist.size() > 0) {
				nc.impl.so.sointerface.SaleToICDRPDMO dmo = new nc.impl.so.sointerface.SaleToICDRPDMO();
				dmo.setReturnNum((nc.vo.so.service.ToRetOrdVO[]) retvolist
						.toArray(new nc.vo.so.service.ToRetOrdVO[retvolist.size()]));
			}

			// 修改
		} else if (ordvo.getIAction() == ISaleOrderAction.A_EDIT || ordvo.getIAction() == ISaleOrderAction.A_MODIFY) {

			// 修改前的VO
			SaleorderBVO[] oldordbvos = ordvo.getOldSaleOrderVO().getBodyVOs();

			HashMap hsoldordvo = new HashMap();

			for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
				if (oldordbvos[i].getCsourcebillbodyid() != null
						&& oldordbvos[i].getCsourcebillbodyid().trim().length() > 0)
					hsoldordvo.put(oldordbvos[i].getCsourcebillbodyid(), oldordbvos[i]);
			}

			if (hsoldordvo.size() <= 0)
				return;

			// 查询对应的订单行
			SaleorderBVO[] sourceOrdbvos = (SaleorderBVO[]) queryAllBodyDataByBIDs((String[]) hsoldordvo.keySet()
					.toArray(new String[hsoldordvo.size()]));
			if (sourceOrdbvos == null || sourceOrdbvos.length <= 0)
				return;

			// 首先按旧的退货订单恢复退货数量
			ArrayList retvolist = new ArrayList();
			nc.vo.so.service.ToRetOrdVO retordvo = null;
			SaleorderBVO ordbvo = null;
			UFDouble dtemp = null;
			for (int i = 0, loop = sourceOrdbvos.length; i < loop; i++) {
				ordbvo = (SaleorderBVO) hsoldordvo.get(sourceOrdbvos[i].getCorder_bid());
				if (ordbvo == null || ordbvo.getNnumber() == null)
					continue;
				retordvo = new nc.vo.so.service.ToRetOrdVO();
				retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
				retordvo.setNtotalreturnnumber(uf0);
				retordvo.setNtotalreturnnumber(nc.vo.so.so016.SoVoTools.getMnySub(retordvo.getNtotalreturnnumber(),
						ordbvo.getNnumber().abs()));
				sourceOrdbvos[i].setNtotalreturnnumber(nc.vo.so.so016.SoVoTools.getMnySub(sourceOrdbvos[i]
						.getNtotalreturnnumber(), ordbvo.getNnumber().abs()));
				retvolist.add(retordvo);
			}
			if (retvolist.size() > 0) {
				nc.impl.so.sointerface.SaleToICDRPDMO dmo = new nc.impl.so.sointerface.SaleToICDRPDMO();
				dmo.setReturnNum((nc.vo.so.service.ToRetOrdVO[]) retvolist
						.toArray(new nc.vo.so.service.ToRetOrdVO[retvolist.size()]));
			}

			retvolist.clear();

			HashMap hsordbvo = new HashMap();

			SaleorderBVO[] ordbvos = ordvo.getAllSaleOrderVO().getBodyVOs();
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {
				if (ordbvos[i].getCsourcebillbodyid() != null && ordbvos[i].getCsourcebillbodyid().trim().length() > 0)
					hsordbvo.put(ordbvos[i].getCsourcebillbodyid(), ordbvos[i]);
			}

			// 再按修改后的退货订单更新退货数量
			for (int i = 0, loop = sourceOrdbvos.length; i < loop; i++) {
				ordbvo = (SaleorderBVO) hsordbvo.get(sourceOrdbvos[i].getCorder_bid());
				if (ordbvo == null || ordbvo.getNnumber() == null)
					continue;
				// 计算累计退货数量
				dtemp = nc.vo.so.so016.SoVoTools.getMnyAdd(sourceOrdbvos[i].getNtotalreturnnumber(), ordbvo
						.getNnumber().abs());
				// 须要加上累记途损数量 20060823
				dtemp = nc.vo.so.so016.SoVoTools.getMnyAdd(dtemp, sourceOrdbvos[i].getNtranslossnum());

				if (dtemp != null
						&& (sourceOrdbvos[i].getNtotalinventorynumber() == null || dtemp.abs().doubleValue() > sourceOrdbvos[i]
								.getNtotalinventorynumber().abs().doubleValue()))
					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
							"UPP40060301-000039")
					/* @res "退货订单的退货数量不能大于订单的累计出库数量！" */);
				// throw new nc.vo.pub.BusinessException(
				// "退货订单的退货数量不能大于订单的累计出库数量");
				retordvo = new nc.vo.so.service.ToRetOrdVO();
				retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
				retordvo.setNtotalreturnnumber(ordbvo.getNnumber().abs());
				retvolist.add(retordvo);
			}
			if (retvolist.size() > 0) {
				nc.impl.so.sointerface.SaleToICDRPDMO dmo = new nc.impl.so.sointerface.SaleToICDRPDMO();
				dmo.setReturnNum((nc.vo.so.service.ToRetOrdVO[]) retvolist
						.toArray(new nc.vo.so.service.ToRetOrdVO[retvolist.size()]));
			}

			// 作废
		} else if (ordvo.getIAction() == ISaleOrderAction.A_BLANKOUT) {

			SaleorderBVO[] ordbvos = ordvo.getBodyVOs();
			HashMap hsordbvo = new HashMap();
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {
				if (ordbvos[i].getCsourcebillbodyid() != null && ordbvos[i].getCsourcebillbodyid().trim().length() > 0)
					hsordbvo.put(ordbvos[i].getCsourcebillbodyid(), ordbvos[i]);
			}

			if (hsordbvo.size() <= 0)
				return;

			// 查询对应的订单行
			SaleorderBVO[] sourceOrdbvos = (SaleorderBVO[]) queryAllBodyDataByBIDs((String[]) hsordbvo.keySet()
					.toArray(new String[hsordbvo.size()]));
			if (sourceOrdbvos == null || sourceOrdbvos.length <= 0)
				return;
			ArrayList retvolist = new ArrayList();
			nc.vo.so.service.ToRetOrdVO retordvo = null;
			SaleorderBVO ordbvo = null;
			for (int i = 0, loop = sourceOrdbvos.length; i < loop; i++) {
				ordbvo = (SaleorderBVO) hsordbvo.get(sourceOrdbvos[i].getCorder_bid());
				if (ordbvo == null || ordbvo.getNnumber() == null)
					continue;
				retordvo = new nc.vo.so.service.ToRetOrdVO();
				retordvo.setCsale_bid(sourceOrdbvos[i].getCorder_bid());
				retordvo.setNtotalreturnnumber(uf0);
				retordvo.setNtotalreturnnumber(nc.vo.so.so016.SoVoTools.getMnySub(retordvo.getNtotalreturnnumber(),
						ordbvo.getNnumber().abs()));
				retvolist.add(retordvo);
			}
			if (retvolist.size() > 0) {
				nc.impl.so.sointerface.SaleToICDRPDMO dmo = new nc.impl.so.sointerface.SaleToICDRPDMO();
				dmo.setReturnNum((nc.vo.so.service.ToRetOrdVO[]) retvolist
						.toArray(new nc.vo.so.service.ToRetOrdVO[retvolist.size()]));
			}

		}
	}

	public void updateArByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, 
			SaleorderBVO[] curordbvos,boolean bupdateCT,String[] biztypes) throws Exception {
		
		String pk_corp = curordbvos[0].getPkcorp();
		int len = curordbvos.length;
		String[] bids = new String[len];
		for (int i = 0; i < len; i++) 
			bids[i] = curordbvos[i].getPrimaryKey();
		
		/** 没有区分是否所有行都需要更新；统一使用了关闭动作来处理* */
    /** 信用 begin* */
    ICreateCorpQueryService corpService = (ICreateCorpQueryService) nc.bs.framework.common.NCLocator
    .getInstance().lookup(ICreateCorpQueryService.class.getName());
    
    Object creditObject = null;
    SOCreditPara para = null;
    boolean creditEnabled = corpService.isEnabled(pk_corp, "SO6");
    if(creditEnabled){
      IBillInvokeCreditManager creditManager = (IBillInvokeCreditManager) nc.bs.framework.common.NCLocator
      .getInstance().lookup(IBillInvokeCreditManager.class.getName());
      creditObject = creditManager;
      para = new SOCreditPara(null, bids, CreditConst.ICREDIT_ACT_OUTCLOSE, biztypes, pk_corp,
      creditManager);
     creditManager.renovateARByBidsBegin(para);
    }
    /** 信用 begin* */
	

		HashMap hsoldbvos = new HashMap();
		for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
			hsoldbvos.put(oldordbvos[i].getCorder_bid(), oldordbvos[i]);
		}

		// 根据行状态的变化组织数据
		ArrayList rowendlist = new ArrayList();
		ArrayList rowopenlist = new ArrayList();

		// 记录出库结束和打开的行
		ArrayList outendvolist = new ArrayList();
		ArrayList outopenvolist = new ArrayList();

		// 记录开票结束和打开的行 added by liubing
		ArrayList invoiceendvolist = new ArrayList();
		ArrayList invoiceopenvolist = new ArrayList();

		SaleorderBVO oldbvo = null;

		UFBoolean curbifinventoryfinish = null;
		UFBoolean oldbifinventoryfinish = null;

		UFBoolean curbifinvoicefinish = null;
		UFBoolean oldbifinvoicefinish = null;

		UFBoolean uffalse = new UFBoolean(false);

		for (int i = 0, loop = curordbvos.length; i < loop; i++) {
			oldbvo = (SaleorderBVO) hsoldbvos.get(curordbvos[i].getCorder_bid());
			if (oldbvo == null)
				continue;

			oldbifinventoryfinish = oldbvo.getBifinventoryfinish() == null ? uffalse : oldbvo.getBifinventoryfinish();
			curbifinventoryfinish = curordbvos[i].getBifinventoryfinish() == null ? uffalse : curordbvos[i]
					.getBifinventoryfinish();

			if (!curbifinventoryfinish.equals(oldbifinventoryfinish)) {
				if (curbifinventoryfinish.booleanValue()) {
					outendvolist.add(curordbvos[i]);
				} else {
					outopenvolist.add(curordbvos[i]);
				}
			}

			// 开票结束打开判断 begin
			oldbifinvoicefinish = oldbvo.getBifinvoicefinish() == null ? uffalse : oldbvo.getBifinvoicefinish();
			curbifinvoicefinish = curordbvos[i].getBifinvoicefinish() == null ? uffalse : curordbvos[i]
					.getBifinvoicefinish();

			if (!curbifinvoicefinish.equals(oldbifinvoicefinish)) {
				if (curbifinvoicefinish.booleanValue()) {
					invoiceendvolist.add(curordbvos[i]);
				} else {
					invoiceopenvolist.add(curordbvos[i]);
				}
			}
			// 开票结束打开判断 end

			// 行状态未变化
			if (curordbvos[i].getFrowstatus().equals(oldbvo.getFrowstatus()))
				continue;

			// 行状态已变化,变为结束
			if (curordbvos[i].getFrowstatus().intValue() == BillStatus.FINISH
					&& oldbvo.getFrowstatus().intValue() == BillStatus.AUDIT)
				rowendlist.add(oldbvo);

			// 行状态已变化,变为打开
			if (curordbvos[i].getFrowstatus().intValue() == BillStatus.AUDIT
					&& oldbvo.getFrowstatus().intValue() == BillStatus.FINISH)
				rowopenlist.add(oldbvo);

		}

		// 处理行状态结束的行
		if (rowendlist.size() > 0) {
			if (bupdateCT) {
				// 更新合同数量
				try {
					setSaleCT((SaleorderBVO[]) rowendlist.toArray(new SaleorderBVO[rowendlist.size()]),
							ISaleOrderAction.A_CLOSE);
				} catch (SystemException e) {
					throw new BusinessException(e.getMessage(), e);
				}
			}
		}

		// 处理行状态打开的行
		if (rowopenlist.size() > 0) {
			if (bupdateCT) {
				// 更新合同数量
				try {
					setSaleCT((SaleorderBVO[]) rowopenlist.toArray(new SaleorderBVO[rowopenlist.size()]),
							ISaleOrderAction.A_OPEN);
				} catch (SystemException e) {
					throw new BusinessException(e.getMessage(), e);
				}
			}
		}
    /** 信用 end* */
    if(creditEnabled){
     ((nc.itf.so.so120.IBillInvokeCreditManager) creditObject).renovateARByBidsEnd(para);
    }
    /** 信用 end* */
	}
	
	/**
	 * 根据订单状态变化信息更新应收及信用。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateArByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
			throws Exception {
		updateArByOrdRows(ordhvos, oldordbvos, curordbvos, true);
	}

	/**
	 * 根据订单状态变化信息更新应收及信用。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateArByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos,
			boolean bupdateCT) throws Exception {

		if (ordhvos == null || oldordbvos == null || curordbvos == null)
			return;
		
		String cbiztype = ((Object[]) new SmartDMO()
				.selectBy2("select so_sale.cbiztype from so_sale,so_saleorder_b where so_sale.csaleid=so_saleorder_b.csaleid and so_saleorder_b.corder_bid = '"
						+ curordbvos[0].getPrimaryKey() + "'")[0]).toString();

		int len = curordbvos.length;
		String[] bids = new String[len];
		String[] biztypes = new String[len];
		for (int i = 0; i < len; i++) {
			bids[i] = curordbvos[i].getPrimaryKey();
			biztypes[i] = cbiztype;
		}

		updateArByOrdRows(ordhvos, oldordbvos, curordbvos, bupdateCT, biztypes);
	}

	/**
	 * 根据订单状态变化信息更新ATP。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateAtpByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
			throws javax.naming.NamingException, nc.vo.pub.BusinessException, java.sql.SQLException {/*
	 * 
	 * if (ordhvos ==
	 * null ||
	 * oldordbvos ==
	 * null ||
	 * curordbvos ==
	 * null) return;
	 * 
	 * HashMap
	 * hsoldbvos =
	 * new
	 * HashMap();
	 * for (int i =
	 * 0, loop =
	 * oldordbvos.length;
	 * i < loop;
	 * i++) {
	 * hsoldbvos.put(oldordbvos[i].getCorder_bid(),
	 * oldordbvos[i]); } //
	 * 根据出库状态，及发运状态的变化组织数据
	 * ArrayList
	 * sendendlist =
	 * new
	 * ArrayList();
	 * ArrayList
	 * sendopenlist =
	 * new
	 * ArrayList();
	 * ArrayList
	 * outendlist =
	 * new
	 * ArrayList();
	 * ArrayList
	 * outopenlist =
	 * new
	 * ArrayList();
	 * SaleorderBVO
	 * oldbvo =
	 * null; for
	 * (int i = 0,
	 * loop =
	 * curordbvos.length;
	 * i < loop;
	 * i++) {
	 * 
	 * if
	 * (curordbvos[i].getBdericttrans() !=
	 * null &&
	 * curordbvos[i].getBdericttrans().booleanValue())
	 * continue;
	 * 
	 * oldbvo =
	 * (SaleorderBVO)
	 * hsoldbvos.get(curordbvos[i].getCorder_bid());
	 * if (oldbvo ==
	 * null)
	 * continue; //
	 * 打开时发运优先考虑 if
	 * (curordbvos[i].getBifreceivefinish() ==
	 * null ||
	 * !curordbvos[i].getBifreceivefinish().booleanValue()) {
	 * 
	 * if
	 * (oldbvo.getBifreceivefinish() !=
	 * null &&
	 * oldbvo.getBifreceivefinish().booleanValue()) { //
	 * 发运状态已变化,变为发运打开
	 * sendopenlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_SENDOPEN);
	 * continue; } } //
	 * 关闭时出库优先考虑 if
	 * (curordbvos[i].getBifinventoryfinish() !=
	 * null &&
	 * curordbvos[i].getBifinventoryfinish().booleanValue()) {
	 * 
	 * if
	 * (!curordbvos[i].getBifinventoryfinish().equals(oldbvo.getBifinventoryfinish())) { //
	 * 出库状态已变化,变为出库结束
	 * outendlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_OUTEND);
	 * continue; } } //
	 * 首先检查出库状态 if
	 * (curordbvos[i].getBifinventoryfinish() !=
	 * null &&
	 * !curordbvos[i].getBifinventoryfinish()
	 * .equals(oldbvo.getBifinventoryfinish())) { //
	 * 出库状态已变化,变为出库结束
	 * if
	 * (curordbvos[i].getBifinventoryfinish() !=
	 * null &&
	 * curordbvos[i].getBifinventoryfinish().booleanValue()) {
	 * outendlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_OUTEND); //
	 * 变为出库打开 } else {
	 * outopenlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_OUTOPEN); }
	 * continue; }
	 * else if
	 * (oldbvo.getBifinventoryfinish() !=
	 * null &&
	 * !oldbvo.getBifinventoryfinish()
	 * .equals(curordbvos[i].getBifinventoryfinish())) { //
	 * 出库状态已变化,变为出库结束
	 * if
	 * (curordbvos[i].getBifinventoryfinish() !=
	 * null &&
	 * curordbvos[i].getBifinventoryfinish().booleanValue()) {
	 * outendlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_OUTEND); //
	 * 变为出库打开 } else {
	 * outopenlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_OUTOPEN); }
	 * continue; } //
	 * 检查发运状态 if
	 * (curordbvos[i].getBifreceivefinish() !=
	 * null &&
	 * !curordbvos[i].getBifreceivefinish().equals(oldbvo.getBifreceivefinish())) { //
	 * 发运状态已变化,变为发运结束
	 * if
	 * (curordbvos[i].getBifreceivefinish() !=
	 * null &&
	 * curordbvos[i].getBifreceivefinish().booleanValue()) {
	 * sendendlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_SENDEND); //
	 * 变为发运打开 } else {
	 * sendopenlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_SENDOPEN); }
	 * continue; }
	 * else if
	 * (oldbvo.getBifreceivefinish() !=
	 * null &&
	 * !oldbvo.getBifreceivefinish().equals(curordbvos[i].getBifreceivefinish())) { //
	 * 发运状态已变化,变为发运结束
	 * if
	 * (curordbvos[i].getBifreceivefinish() !=
	 * null &&
	 * curordbvos[i].getBifreceivefinish().booleanValue()) {
	 * sendendlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_SENDEND); //
	 * 变为发运打开 } else {
	 * sendopenlist.add(oldbvo);
	 * oldbvo.setIAction(ISaleOrderAction.A_SENDOPEN); }
	 * continue; } }
	 * 
	 * nc.impl.so.sointerface.SOATP
	 * soatp = new
	 * nc.impl.so.sointerface.SOATP(); //
	 * 更新ATP,行状态变为出库结束的
	 * SaleOrderVO[]
	 * ordvos =
	 * toSaleOrderVO(ordhvos,
	 * outendlist);
	 * if (ordvos !=
	 * null) { for
	 * (int i = 0,
	 * loop =
	 * ordvos.length;
	 * i < loop;
	 * i++) {
	 * soatp.modifyATPWhenCloseBill(ordvos[i]); } } //
	 * 更新ATP,行状态变为出库打开的
	 * ordvos =
	 * toSaleOrderVO(ordhvos,
	 * outopenlist);
	 * if (ordvos !=
	 * null) { for
	 * (int i = 0,
	 * loop =
	 * ordvos.length;
	 * i < loop;
	 * i++) {
	 * soatp.modifyATPWhenOpenBill(ordvos[i]); } } //
	 * 更新ATP,行状态变为发运结束的
	 * ordvos =
	 * toSaleOrderVO(ordhvos,
	 * sendendlist);
	 * if (ordvos !=
	 * null) { for
	 * (int i = 0,
	 * loop =
	 * ordvos.length;
	 * i < loop;
	 * i++) {
	 * soatp.modifyATPWhenCloseBill(ordvos[i]); } } //
	 * 更新ATP,行状态变为发运打开的
	 * ordvos =
	 * toSaleOrderVO(ordhvos,
	 * sendopenlist);
	 * if (ordvos !=
	 * null) { for
	 * (int i = 0,
	 * loop =
	 * ordvos.length;
	 * i < loop;
	 * i++) {
	 * soatp.modifyATPWhenOpenBill(ordvos[i]); } }
	 * 
	 */
	}

	/**
	 * 根据订单状态变化信息更新订单核销关系。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateAdjustForEnd(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
			throws javax.naming.NamingException, nc.vo.pub.BusinessException, java.sql.SQLException {

		if (ordhvos == null || oldordbvos == null || curordbvos == null)
			return;

		HashMap hsoldbvos = new HashMap();
		for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
			hsoldbvos.put(oldordbvos[i].getCorder_bid(), oldordbvos[i]);
		}

		// 根据出库状态，及发运状态的变化组织数据
		ArrayList endlist = new ArrayList(curordbvos.length);

		ArrayList openlist = new ArrayList(curordbvos.length);

		SaleorderBVO oldbvo = null;
		for (int i = 0, loop = curordbvos.length; i < loop; i++) {
			oldbvo = (SaleorderBVO) hsoldbvos.get(curordbvos[i].getCorder_bid());
			if (oldbvo == null)
				continue;

			// 行状态已变化,变为结束
			if (curordbvos[i].getFrowstatus().intValue() == BillStatus.FINISH
					&& oldbvo.getFrowstatus().intValue() == BillStatus.AUDIT) {

				endlist.add(oldbvo);
			}

			if (curordbvos[i].getFrowstatus().intValue() == BillStatus.AUDIT
					&& oldbvo.getFrowstatus().intValue() == BillStatus.FINISH) {

				openlist.add(oldbvo);
			}

		}

	}

	/**
	 * 根据订单状态变化信息更新订单核销关系。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param saleorderB
	 *            nc.vo.so.so001.SaleorderBVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateOrdBalanceByOrdRows(SaleorderHVO[] ordhvos, SaleorderBVO[] oldordbvos, SaleorderBVO[] curordbvos)
			throws javax.naming.NamingException, nc.vo.pub.BusinessException, java.sql.SQLException {

		if (ordhvos == null || oldordbvos == null || curordbvos == null)
			return;

		HashMap hsoldbvos = new HashMap();
		for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
			hsoldbvos.put(oldordbvos[i].getCorder_bid(), oldordbvos[i]);
		}

		// 根据出库状态，及发运状态的变化组织数据
		ArrayList outendlist = new ArrayList();

		SaleorderBVO oldbvo = null;
		for (int i = 0, loop = curordbvos.length; i < loop; i++) {
			oldbvo = (SaleorderBVO) hsoldbvos.get(curordbvos[i].getCorder_bid());
			if (oldbvo == null)
				continue;
			// 首先检查出库状态
			if (curordbvos[i].getBifinventoryfinish() != null
					&& !curordbvos[i].getBifinventoryfinish().equals(oldbvo.getBifinventoryfinish())) {
				// 出库状态已变化,变为出库结束
				if (curordbvos[i].getBifinventoryfinish() != null
						&& curordbvos[i].getBifinventoryfinish().booleanValue()) {
					outendlist.add(oldbvo);
				}
				continue;
			} else if (oldbvo.getBifinventoryfinish() != null
					&& !oldbvo.getBifinventoryfinish().equals(curordbvos[i].getBifinventoryfinish())) {
				// 出库状态已变化,变为出库结束
				if (curordbvos[i].getBifinventoryfinish() != null
						&& curordbvos[i].getBifinventoryfinish().booleanValue()) {
					outendlist.add(oldbvo);
				}
				continue;
			}

			// 行状态已变化,变为结束
			if (curordbvos[i].getFrowstatus().intValue() == BillStatus.FINISH
					&& oldbvo.getFrowstatus().intValue() == BillStatus.AUDIT) {

				outendlist.add(oldbvo);
			}

		}

		// 更新订单出库核销关系
		if (outendlist.size() > 0) {
			SaleOrderVO[] vos = toSaleOrderVO(ordhvos, outendlist);
			nc.impl.scm.so.so016.BalanceDMO baldmo;
			try {
				baldmo = new nc.impl.scm.so.so016.BalanceDMO();

				Integer ioutendaction = new Integer(ISaleOrderAction.A_OUTEND);
				for (int i = 0, loop = vos.length; i < loop; i++) {
					// 更新订单核销关系
					baldmo.updateSoBalance(vos[i], ioutendaction);
				}
			} catch (SystemException e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 检测收货库存组织与存货是否匹配。 创建日期：(2001-11-28 9:10:38)
	 * 
	 * @param saleVO
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	private void checkRecStoreStructure(SaleOrderVO saleVO, String newHeadID) throws java.sql.SQLException,
			nc.vo.pub.BusinessException {
		if (saleVO == null || !saleVO.isMultCorpOrd())
			return;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT so_saleorder_b.cinventoryid, bd_produce.pk_calbody ");
		sql.append("FROM so_saleorder_b LEFT OUTER JOIN ");
		sql.append("bd_produce ON bd_produce.pk_invmandoc = so_saleorder_b.cinventoryid AND ");
		sql.append("bd_produce.pk_calbody = so_saleorder_b.creccalbodyid ");
		sql.append("where so_saleorder_b.csaleid=?");
		// SaleorderHVO saleHVOs = (SaleorderHVO) saleVO.getParentVO();
		SaleorderBVO[] saleBVOs = (SaleorderBVO[]) saleVO.getChildrenVO();
		Connection con = null;
		PreparedStatement stmt = null;
		if (saleBVOs != null) {
			try {
				StringBuffer sbfErr = new StringBuffer();
				HashMap htTemp = new HashMap();
				String invid = null;
				String calbodyid = null;
				con = getConnection();
				stmt = con.prepareStatement(sql.toString());
				stmt.setString(1, newHeadID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					invid = rs.getString(1);
					invid = (invid == null ? null : invid.trim());
					calbodyid = rs.getString(2);
					calbodyid = (calbodyid == null ? null : calbodyid.trim());
					if (invid != null && invid.length() > 0 && calbodyid != null && calbodyid.length() > 0) {
						htTemp.put(invid, calbodyid);
					}
				}
				rs.close();
				Object oTemp = null;
				for (int i = 0; i < saleBVOs.length; i++) {
					// yb 修改'保存订单后，删行后保存出错的问题' 03-09-05 排除对删除行的检查
					if (saleBVOs[i].getStatus() == VOStatus.DELETED)
						continue;

					if (saleBVOs[i].getLaborflag() != null && saleBVOs[i].getLaborflag().booleanValue())
						continue;

					if (saleBVOs[i].getDiscountflag() != null && saleBVOs[i].getDiscountflag().booleanValue())
						continue;

					if (saleBVOs[i].getBoosflag() == null || saleBVOs[i].getBoosflag().booleanValue() == false) {

						if (saleBVOs[i].getCreccalbodyid() == null
								|| saleBVOs[i].getCreccalbodyid().trim().length() <= 0)
							continue;
						// 排除缺货登记的存货
						oTemp = htTemp.get(saleBVOs[i].getCinventoryid());
						if (oTemp == null) {
							sbfErr.append(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000040",
									null,
									new String[] { saleBVOs[i].getCrowno() == null ? "" : saleBVOs[i].getCrowno() }));
							sbfErr.append("\n");
							// sbfErr.append("行号为");
							// sbfErr.append(saleBVOs[i].getCrowno() == null ?
							// ""
							// : saleBVOs[i].getCrowno());
							// sbfErr.append("的行存货不属于收货库存组织！\n");
						}
					}
				}
				if (sbfErr.toString().trim().length() > 0) {
					throw new nc.vo.pub.BusinessException(sbfErr.toString());
				}
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (Exception e) {
				}
				try {
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 检测发货库存组织与发货公司是否匹配。 
	 * 
	 * @param saleVO
	 *            nc.vo.so.so001.SaleOrderVO
	 * @author liuzwc  创建日期：(2009-06-03 17:12:38)
	 */
	private void checkCalbody(SaleOrderVO saleVO) throws java.sql.SQLException,
			nc.vo.pub.BusinessException {
		if (saleVO == null )
			return;

		StringBuffer sql = new StringBuffer();
		sql.append("select pk_calbody,pk_corp from bd_calbody where dr = 0 ");
		SaleorderBVO[] saleBVOs = (SaleorderBVO[]) saleVO.getChildrenVO();
		HashSet<String> set = new HashSet<String>();
		ArrayList<String[]> al = new ArrayList<String[]>();
		Connection con = null;
		PreparedStatement stmt = null;
		
		if(saleBVOs != null) {	
			for (int i = 0; i < saleBVOs.length; i++) {
				if (saleBVOs[i].getStatus() == VOStatus.DELETED)
					continue;
	
				if (saleBVOs[i].getLaborflag() != null && saleBVOs[i].getLaborflag().booleanValue()) {
					//记录劳务行的发货库存组织id
					set.add(saleBVOs[i].getCadvisecalbodyid());
					//记录劳务行的发货库存组织id,发货公司,行号
					al.add(new String[]{saleBVOs[i].getCadvisecalbodyid(),
							(saleBVOs[i].getCconsigncorpid()==null?saleBVOs[i].getPk_corp():saleBVOs[i].getCconsigncorpid()),
							saleBVOs[i].getCrowno()});
				}
				if (saleBVOs[i].getDiscountflag() != null && saleBVOs[i].getDiscountflag().booleanValue()) {
					//记录折扣行的发货库存组织id
					set.add(saleBVOs[i].getCadvisecalbodyid());
					//记录折扣行的发货库存组织id,发货公司,行号
					al.add(new String[]{saleBVOs[i].getCadvisecalbodyid(),
							(saleBVOs[i].getCconsigncorpid()==null?saleBVOs[i].getPk_corp():saleBVOs[i].getCconsigncorpid()),
							saleBVOs[i].getCrowno()});
				}
			}
			String[] pk_calbodys = set.toArray(new String[0]);
			if(pk_calbodys == null || pk_calbodys.length==0)
				return;
			sql.append(GeneralSqlString.formInSQL("pk_calbody", pk_calbodys));
			//查询发货库存组织对应公司
			try {
				StringBuffer sbfErr = new StringBuffer();
				HashMap htTemp = new HashMap();
				String pk_corp = null;
				String pk_calbody = null;
				con = getConnection();
				stmt = con.prepareStatement(sql.toString());
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					pk_calbody = rs.getString(1);
					pk_calbody = (pk_calbody == null ? null : pk_calbody.trim());
					pk_corp = rs.getString(2);
					pk_corp = (pk_corp == null ? null : pk_corp.trim());
					if (pk_calbody != null && pk_calbody.length() > 0 && pk_corp != null && pk_corp.length() > 0) {
						htTemp.put(pk_calbody, pk_corp);
					}
				}
				rs.close();
				
				//匹配检查
				String[] sTemp = new String[3]; //{发货库存组织,发货公司,行号}
//				HashSet<String> hsCrowno = new HashSet<String>();
				for(int i= 0 ;i<al.size(); i++) {
					sTemp = al.get(i);
					if(!sTemp[1].equals(htTemp.get(sTemp[0]))){
//						hsCrowno.add(sTemp[2]);
						sbfErr.append(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000565",
								null,
								new String[] { sTemp[2] }));
						sbfErr.append("\n");
					}
				}
//				if (hsCrowno.size() > 0) {
//					String[] sCrowno = hsCrowno.toArray(new String[0]);
//					throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301", "UPP40060301-000040",
//							null,sCrowno));
//				}
				if (sbfErr.toString().trim().length() > 0) {
					throw new nc.vo.pub.BusinessException(sbfErr.toString());
				}
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (Exception e) {
				}
				try {
					if (con != null) {
						con.close();
					}
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 从结果集获取值，同时设置相应vo字段值。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param rs
	 *            ResultSet
	 * @param pos
	 *            int 如果位置参数小于0，则按名字从结果集获取值
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private void getOrdBAndExeValueFromResultSet(ResultSet rs, int starpos, SaleorderBVO bvo)
			throws java.sql.SQLException {
		if (rs == null || bvo == null)
			return;

		// Object value = null;
		//
		// String stemp = null;
		// BigDecimal dectemp = null;

		if (starpos < 0) {

			getOrdBValueFromResultSet(rs, -1, bvo);

			getOrdExeValueFromResultSet(rs, -1, bvo);

		} else {

			int pos = getOrdBValueFromResultSet(rs, starpos, bvo);

			getOrdExeValueFromResultSet(rs, pos + 1, bvo);
		}

	}

	/**
	 * 从结果集获取值，同时设置相应vo字段值。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param rs
	 *            ResultSet
	 * @param pos
	 *            int 如果位置参数小于0，则按名字从结果集获取值
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private int getOrdBValueFromResultSet(ResultSet rs, int starpos, SaleorderBVO bvo) throws java.sql.SQLException {
		if (rs == null || bvo == null)
			return starpos;

		// Object value = null;

		String stemp = null;
		BigDecimal dectemp = null;

		if (starpos < 0) {

			stemp = rs.getString("cconsigncorpid");
			bvo.setCconsigncorpid(stemp == null ? null : stemp.trim());

			dectemp = (BigDecimal) rs.getObject("nreturntaxrate");
			bvo.setNreturntaxrate(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString("creccalbodyid");
			bvo.setCreccalbodyid(stemp == null ? null : stemp.trim());

			stemp = rs.getString("crecwareid");
			bvo.setCrecwareid(stemp == null ? null : stemp.trim());

			stemp = rs.getString("bdericttrans");
			bvo.setBdericttrans(stemp == null ? null : new UFBoolean(stemp.trim()));

			stemp = rs.getString("tconsigntime");
			bvo.setTconsigntime(stemp == null ? null : stemp.trim());

			stemp = rs.getString("tdelivertime");
			bvo.setTdelivertime(stemp == null ? null : stemp.trim());

			stemp = rs.getString("bsafeprice");
			bvo.setBsafeprice(stemp == null ? null : new UFBoolean(stemp.trim()));

			dectemp = (BigDecimal) rs.getObject("ntaldcnum");
			bvo.setNtaldcnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nasttaldcnum");
			bvo.setNasttaldcnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("ntaldcmny");
			bvo.setNtaldcmny(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString("breturnprofit");
			bvo.setBreturnprofit(stemp == null ? null : new UFBoolean(stemp.trim()));

			dectemp = (BigDecimal) rs.getObject("nretprofnum");
			bvo.setNretprofnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nastretprofnum");
			bvo.setNastretprofnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nretprofmny");
			bvo.setNretprofmny(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString("cpricepolicyid");
			bvo.setCpricepolicyid(stemp == null ? null : stemp.trim());

			stemp = rs.getString("cpriceitemid");
			bvo.setCpriceitemid(stemp == null ? null : stemp.trim());

			stemp = rs.getString("cpriceitemtable");
			bvo.setCpriceitemtable(stemp == null ? null : stemp.trim());

			stemp = rs.getString("cpricecalproc");
			bvo.setCpricecalproc(stemp == null ? null : stemp.trim());

			stemp = rs.getString("cquoteunitid");
			bvo.setCquoteunitid(stemp == null ? null : stemp.trim());

			dectemp = (BigDecimal) rs.getObject("nquoteunitnum");
			bvo.setNquoteunitnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("norgqttaxprc");
			bvo.setNorgqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("norgqtprc");
			bvo.setNorgqtprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("norgqttaxnetprc");
			bvo.setNorgqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("norgqtnetprc");
			bvo.setNorgqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nqttaxnetprc");
			bvo.setNqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nqtnetprc");
			bvo.setNqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nqttaxprc");
			bvo.setNqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nqtprc");
			bvo.setNqtprc(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString("cprolineid");
			bvo.setCprolineid(stemp == null ? null : stemp.trim());

			stemp = rs.getString("crecaddrnode");
			bvo.setCrecaddrnode(stemp == null ? null : stemp.trim());

			stemp = rs.getString("cinventoryid1");
			bvo.setCinventoryid1(stemp == null ? null : stemp.trim());

			stemp = rs.getString("cchantypeid");
			bvo.setCchantypeid(stemp == null ? null : stemp.trim());

			dectemp = (BigDecimal) rs.getObject("nqtorgprc");
			bvo.setNqtorgprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nqtorgtaxprc");
			bvo.setNqtorgtaxprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nqtorgnetprc");
			bvo.setNqtorgnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nqtorgtaxnetprc");
			bvo.setNqtorgtaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("nquoteunitrate");
			bvo.setNqtscalefactor(dectemp == null ? null : new UFDouble(dectemp));
			
			stemp = rs.getString("ct_managebid");
			bvo.setCt_managebid(stemp == null ? null : stemp.trim());

			SOField[] plgins = SaleorderBVO.getPluginFields();
			Object value;
			String key;
			for (SOField plgin : plgins) {
				// SOField.setVoname --> SmartFieldMeta.getName
				key = plgin.getVoname();
				
				// SOField.setUftype --> SmartFieldMeta.getType
				value = getStmt(key, plgin.getUftype(), rs);
				bvo.setAttributeValue(key, value);
			}
		} else {

			stemp = rs.getString(starpos);
			bvo.setCconsigncorpid(stemp == null ? null : stemp.trim());

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNreturntaxrate(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString(++starpos);
			bvo.setCreccalbodyid(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setCrecwareid(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setBdericttrans(stemp == null ? null : new UFBoolean(stemp.trim()));

			stemp = rs.getString(++starpos);
			bvo.setTconsigntime(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setTdelivertime(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setBsafeprice(stemp == null ? null : new UFBoolean(stemp.trim()));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNtaldcnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNasttaldcnum(dectemp == null ? null : new UFDouble(dectemp));

			// 10
			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNtaldcmny(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString(++starpos);
			bvo.setBreturnprofit(stemp == null ? null : new UFBoolean(stemp.trim()));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNretprofnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNastretprofnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNretprofmny(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString(++starpos);
			bvo.setCpricepolicyid(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setCpriceitemid(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setCpriceitemtable(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setCpricecalproc(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setCquoteunitid(stemp == null ? null : stemp.trim());

			// 20
			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNquoteunitnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNorgqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNorgqtprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNorgqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNorgqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqttaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqtnetprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqttaxprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqtprc(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString(++starpos);
			bvo.setCprolineid(stemp == null ? null : stemp.trim());

			// 30
			stemp = rs.getString(++starpos);
			bvo.setCrecaddrnode(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setCinventoryid1(stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setCchantypeid(stemp == null ? null : stemp.trim());

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqtorgprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqtorgtaxprc(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqtscalefactor(dectemp == null ? null : new UFDouble(dectemp));

			// v5.5 nqtorgtaxnetprc 询价原币含税净价
			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqtorgtaxnetprc(dectemp == null ? null : new UFDouble(dectemp));

			// 5.5 nqtorgnetprc 询价原币无税净价
			dectemp = (BigDecimal) rs.getObject(++starpos);
			bvo.setNqtorgnetprc(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString(++starpos);
			bvo.setAttributeValue("clargessrowno", stemp == null ? null : stemp.trim());

			stemp = rs.getString(++starpos);
			bvo.setBbindflag(stemp == null ? null : new UFBoolean(stemp.trim()));

			stemp = rs.getString(++starpos);
			bvo.setCt_managebid(stemp == null ? null : stemp.trim());
			
			SOField[] plgins = SaleorderBVO.getPluginFields();
			Object value;
			String key;
			for (SOField plgin : plgins) {
				// SOField.setVoname --> SmartFieldMeta.getName
				key = plgin.getVoname();
				
				// SOField.setUftype --> SmartFieldMeta.getType
				value = getStmt(key, plgin.getUftype(), rs);
				bvo.setAttributeValue(key, value);
				
				starpos++;
			}
		}

		return starpos;

	}

	/**
	 * 从结果集获取值，同时设置相应vo字段值。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param rs
	 *            ResultSet
	 * @param pos
	 *            int 如果位置参数小于0，则按名字从结果集获取值
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private int getOrdExeValueFromResultSet(ResultSet rs, int starpos, SaleorderBVO bvo) throws java.sql.SQLException {
		if (rs == null || bvo == null)
			return starpos;

		// Object value = null;

		String stemp = null;
		BigDecimal dectemp = null;

		if (starpos < 0) {

			// 最后货源安排时间 :
			stemp = rs.getString("tlastarrangetime");
			bvo.setTlastarrangetime(stemp == null ? null : new UFDateTime(stemp.trim()));

			dectemp = (BigDecimal) rs.getObject("ntaltransnum");
			bvo.setNtaltransnum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("ntalbalancemny");
			bvo.setNtalbalancemny(dectemp == null ? null : new UFDouble(dectemp));

			// 累计安排生产订单数量
			dectemp = (BigDecimal) rs.getObject("narrangemonum");
			bvo.setNarrangemonum(dectemp == null ? null : new UFDouble(dectemp));

			dectemp = (BigDecimal) rs.getObject("ntranslossnum");
			bvo.setNtranslossnum(dectemp == null ? null : new UFDouble(dectemp));

			stemp = rs.getString("biftransfinish");
			bvo.setBiftransfinish(stemp == null ? null : new UFBoolean(stemp.trim()));

			stemp = rs.getString("dlastconsigdate");
			bvo.setDlastconsigdate(stemp == null ? null : new UFDate(stemp.trim()));

			stemp = rs.getString("dlasttransdate");
			bvo.setDlasttransdate(stemp == null ? null : new UFDate(stemp.trim()));

			stemp = rs.getString("dlastoutdate");
			bvo.setDlastoutdate(stemp == null ? null : new UFDate(stemp.trim()));

			stemp = rs.getString("dlastinvoicedt");
			bvo.setDlastinvoicedt(stemp == null ? null : new UFDate(stemp.trim()));

			stemp = rs.getString("dlastpaydate");
			bvo.setDlastpaydate(stemp == null ? null : new UFDate(stemp.trim()));

			// vdef6 :
			// stemp = rs.getString("vdef6");
			// bvo.setVdef6(stemp == null ? null : stemp.trim());

			// vdef7 :
			stemp = rs.getString("vdef7");
			bvo.setVdef7(stemp == null ? null : stemp.trim());

			// vdef8 :
			stemp = rs.getString("vdef8");
			bvo.setVdef8(stemp == null ? null : stemp.trim());

			// vdef9 :
			stemp = rs.getString("vdef9");
			bvo.setVdef9(stemp == null ? null : stemp.trim());

			// vdef10 :
			stemp = rs.getString("vdef10");
			bvo.setVdef10(stemp == null ? null : stemp.trim());

			// vdef11 :
			stemp = rs.getString("vdef11");
			bvo.setVdef11(stemp == null ? null : stemp.trim());

			// vdef12 :
			stemp = rs.getString("vdef12");
			bvo.setVdef12(stemp == null ? null : stemp.trim());

			// vdef13 :
			stemp = rs.getString("vdef13");
			bvo.setVdef13(stemp == null ? null : stemp.trim());

			// vdef14 :
			stemp = rs.getString("vdef14");
			bvo.setVdef14(stemp == null ? null : stemp.trim());

			// vdef15 :
			stemp = rs.getString("vdef15");
			bvo.setVdef15(stemp == null ? null : stemp.trim());

			// vdef16 :
			stemp = rs.getString("vdef16");
			bvo.setVdef16(stemp == null ? null : stemp.trim());

			// vdef17 :
			stemp = rs.getString("vdef17");
			bvo.setVdef17(stemp == null ? null : stemp.trim());

			// vdef18 :
			stemp = rs.getString("vdef18");
			bvo.setVdef18(stemp == null ? null : stemp.trim());

			// vdef19 :
			stemp = rs.getString("vdef19");
			bvo.setVdef19(stemp == null ? null : stemp.trim());

			// vdef20 :
			stemp = rs.getString("vdef20");
			bvo.setVdef20(stemp == null ? null : stemp.trim());

			// pk_defdoc1 :
			stemp = rs.getString("pk_defdoc1");
			bvo.setPk_defdoc1(stemp == null ? null : stemp.trim());

			// pk_defdoc2 :
			stemp = rs.getString("pk_defdoc2");
			bvo.setPk_defdoc2(stemp == null ? null : stemp.trim());

			// pk_defdoc3 :
			stemp = rs.getString("pk_defdoc3");
			bvo.setPk_defdoc3(stemp == null ? null : stemp.trim());

			// pk_defdoc4 :
			stemp = rs.getString("pk_defdoc4");
			bvo.setPk_defdoc4(stemp == null ? null : stemp.trim());

			// pk_defdoc5 :
			stemp = rs.getString("pk_defdoc5");
			bvo.setPk_defdoc5(stemp == null ? null : stemp.trim());

			// pk_defdoc6 :
			stemp = rs.getString("pk_defdoc6");
			bvo.setPk_defdoc6(stemp == null ? null : stemp.trim());

			// pk_defdoc7 :
			stemp = rs.getString("pk_defdoc7");
			bvo.setPk_defdoc7(stemp == null ? null : stemp.trim());

			// pk_defdoc8 :
			stemp = rs.getString("pk_defdoc8");
			bvo.setPk_defdoc8(stemp == null ? null : stemp.trim());

			// pk_defdoc9 :
			stemp = rs.getString("pk_defdoc9");
			bvo.setPk_defdoc9(stemp == null ? null : stemp.trim());

			// pk_defdoc10 :
			stemp = rs.getString("pk_defdoc10");
			bvo.setPk_defdoc10(stemp == null ? null : stemp.trim());

			// pk_defdoc11 :
			stemp = rs.getString("pk_defdoc11");
			bvo.setPk_defdoc11(stemp == null ? null : stemp.trim());

			// pk_defdoc12 :
			stemp = rs.getString("pk_defdoc12");
			bvo.setPk_defdoc12(stemp == null ? null : stemp.trim());

			// pk_defdoc13 :
			stemp = rs.getString("pk_defdoc13");
			bvo.setPk_defdoc13(stemp == null ? null : stemp.trim());

			// pk_defdoc14 :
			stemp = rs.getString("pk_defdoc14");
			bvo.setPk_defdoc14(stemp == null ? null : stemp.trim());

			// pk_defdoc15 :
			stemp = rs.getString("pk_defdoc15");
			bvo.setPk_defdoc15(stemp == null ? null : stemp.trim());

			// pk_defdoc16 :
			stemp = rs.getString("pk_defdoc16");
			bvo.setPk_defdoc16(stemp == null ? null : stemp.trim());

			// pk_defdoc17 :
			stemp = rs.getString("pk_defdoc17");
			bvo.setPk_defdoc17(stemp == null ? null : stemp.trim());

			// pk_defdoc18 :
			stemp = rs.getString("pk_defdoc18");
			bvo.setPk_defdoc18(stemp == null ? null : stemp.trim());

			// pk_defdoc19 :
			stemp = rs.getString("pk_defdoc19");
			bvo.setPk_defdoc19(stemp == null ? null : stemp.trim());

			// pk_defdoc20 :
			stemp = rs.getString("pk_defdoc20");
			bvo.setPk_defdoc20(stemp == null ? null : stemp.trim());

			// 累计安排委外订单数量
			dectemp = (BigDecimal) rs.getObject("narrangescornum");
			bvo.setNarrangescornum(dectemp == null ? null : new UFDouble(dectemp));

			// 累计安排请购单数量
			dectemp = (BigDecimal) rs.getObject("narrangepoapplynum");
			bvo.setNarrangepoapplynum(dectemp == null ? null : new UFDouble(dectemp));

			// 累计安排调拨订单数量
			dectemp = (BigDecimal) rs.getObject("narrangetoornum");
			bvo.setNarrangetoornum(dectemp == null ? null : new UFDouble(dectemp));

			// 累计安排调拨申请数量
			dectemp = (BigDecimal) rs.getObject("norrangetoapplynum");
			bvo.setNorrangetoapplynum(dectemp == null ? null : new UFDouble(dectemp));

			// 累计安排采购订单数量
			dectemp = (BigDecimal) rs.getObject("narrangepoordernum");
			bvo.setNarrangepoordernum(dectemp == null ? null : new UFDouble(dectemp));
			
			// 是否货源安排完毕 :
			stemp = rs.getString("barrangedflag");
			bvo.setBarrangedflag(stemp == null ? null : new UFBoolean(stemp.trim()));

			// 最后货源安排人 :
			stemp = rs.getString("carrangepersonid");
			bvo.setCarrangepersonid(stemp == null ? null : stemp.trim());

		} else {

			// 最后货源安排时间 :
			stemp = rs.getString(starpos);
			bvo.setTlastarrangetime(stemp == null ? null : new UFDateTime(stemp.trim()));

			starpos++;

			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNtaltransnum(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			// 最后货源安排人 :
			stemp = rs.getString(starpos);
			bvo.setCarrangepersonid(stemp == null ? null : stemp.trim());

			starpos++;

			// 是否货源安排完毕 :
			stemp = rs.getString(starpos);
			bvo.setBarrangedflag(stemp == null ? null : new UFBoolean(stemp.trim()));

			starpos++;
			//
			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNtalbalancemny(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			// 累计安排生产订单数量
			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNarrangemonum(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNtranslossnum(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			stemp = rs.getString(starpos);
			bvo.setBiftransfinish(stemp == null ? null : new UFBoolean(stemp.trim()));

			starpos++;

			stemp = rs.getString(starpos);
			bvo.setDlastconsigdate(stemp == null ? null : new UFDate(stemp.trim()));

			// 10
			starpos++;

			stemp = rs.getString(starpos);
			bvo.setDlasttransdate(stemp == null ? null : new UFDate(stemp.trim()));

			starpos++;

			stemp = rs.getString(starpos);
			bvo.setDlastoutdate(stemp == null ? null : new UFDate(stemp.trim()));

			starpos++;

			stemp = rs.getString(starpos);
			bvo.setDlastinvoicedt(stemp == null ? null : new UFDate(stemp.trim()));

			starpos++;

			stemp = rs.getString(starpos);
			bvo.setDlastpaydate(stemp == null ? null : new UFDate(stemp.trim()));

			starpos++;

			// //vdef6 :
			// stemp = rs.getString(starpos);
			// bvo.setVdef6(stemp == null ? null : stemp.trim());

			// starpos++;

			// vdef7 :
			stemp = rs.getString(starpos);
			bvo.setVdef7(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef8 :
			stemp = rs.getString(starpos);
			bvo.setVdef8(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef9:
			stemp = rs.getString(starpos);
			bvo.setVdef9(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef10:
			stemp = rs.getString(starpos);
			bvo.setVdef10(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef11 :
			stemp = rs.getString(starpos);
			bvo.setVdef11(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef12 :
			stemp = rs.getString(starpos);
			bvo.setVdef12(stemp == null ? null : stemp.trim());

			// 20
			starpos++;

			// vdef13 :
			stemp = rs.getString(starpos);
			bvo.setVdef13(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef14 :
			stemp = rs.getString(starpos);
			bvo.setVdef14(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef15 :
			stemp = rs.getString(starpos);
			bvo.setVdef15(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef16 :
			stemp = rs.getString(starpos);
			bvo.setVdef16(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef17 :
			stemp = rs.getString(starpos);
			bvo.setVdef17(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef18 :
			stemp = rs.getString(starpos);
			bvo.setVdef18(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef19 :
			stemp = rs.getString(starpos);
			bvo.setVdef19(stemp == null ? null : stemp.trim());

			starpos++;

			// vdef20 :
			stemp = rs.getString(starpos);
			bvo.setVdef20(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc1 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc1(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc2 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc2(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc3 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc3(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc4 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc4(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc5 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc5(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc6 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc6(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc7 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc7(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc8 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc8(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc9 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc9(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc10 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc10(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc11 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc11(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc12 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc12(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc13 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc13(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc14 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc14(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc15 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc15(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc16 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc16(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc17 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc17(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc18 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc18(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc19 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc19(stemp == null ? null : stemp.trim());

			starpos++;

			// pk_defdoc20 :
			stemp = rs.getString(starpos);
			bvo.setPk_defdoc20(stemp == null ? null : stemp.trim());

			starpos++;

			// 累计安排委外订单数量
			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNarrangescornum(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			// 累计安排请购单数量
			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNarrangepoapplynum(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			// 累计安排调拨订单数量
			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNarrangetoornum(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			// 累计安排调拨申请数量
			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNorrangetoapplynum(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;
			
			// 累计安排采购订单数量
			dectemp = (BigDecimal) rs.getObject(starpos);
			bvo.setNarrangepoordernum(dectemp == null ? null : new UFDouble(dectemp));
		}

		return starpos;

	}

	/*
	 * 查询订单现收数据 修改日期：2004-05-08 修改人：杨涛
	 * 
	 */
	public HashMap queryCachPayByOrdIds(String[] csaleids) throws BusinessException {

		if (csaleids == null || csaleids.length <= 0)
			return null;

		OrdVO[] balvos = null;
		try {
			BalanceDMO baldmo = new BalanceDMO();
			balvos = baldmo.queryOrdVO(" so_sale.csaleid  in "
					+ GeneralSqlString.formSubSql("so_balance.csaleid", csaleids));
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		HashMap retvalues = new HashMap();
		if (balvos != null && balvos.length > 0) {
			for (int i = 0; i < balvos.length; i++) {
				retvalues.put(balvos[i].getCsaleid(), balvos[i].getNorigbalsummny());
			}
		}
		return retvalues;

	}

	/**
	 * 从结果集获取值，同时设置相应vo字段值。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param rs
	 *            ResultSet
	 * @param pos
	 *            int 如果位置参数小于0，则按名字从结果集获取值
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private int getOrdHValueFromResultSet(ResultSet rs, int starpos, SaleorderHVO hvo) throws java.sql.SQLException {
		if (rs == null || hvo == null)
			return starpos;

		// Object value = null;

		String stemp = null;
		BigDecimal dectemp = null;
		Integer itemp = null;

		if (starpos < 0) {

			// btransendflag :
			stemp = rs.getString("btransendflag");
			hvo.setBtransendflag(stemp == null ? null : new UFBoolean(stemp.trim()));

			// naccountperiod :
			dectemp = (BigDecimal) rs.getObject("naccountperiod");
			hvo.setNaccountperiod(dectemp == null ? null : new UFDouble(dectemp));

			// boverdate :
			stemp = rs.getString("boverdate");
			hvo.setBoverdate(stemp == null ? null : new UFBoolean(stemp.trim()));

			// iprintcount
			itemp = (Integer) rs.getObject("iprintcount");
			hvo.setIprintcount(itemp == null ? null : itemp);

			// cbaltypeid :
			stemp = rs.getString("cbaltypeid");
			hvo.setCbaltypeid(stemp == null ? null : stemp);

			// vdef11 :
			stemp = rs.getString("vdef11");
			hvo.setVdef11(stemp == null ? null : stemp);

			// vdef12 :
			stemp = rs.getString("vdef12");
			hvo.setVdef12(stemp == null ? null : stemp);

			// vdef13 :
			stemp = rs.getString("vdef13");
			hvo.setVdef13(stemp == null ? null : stemp);

			// vdef14 :
			stemp = rs.getString("vdef14");
			hvo.setVdef14(stemp == null ? null : stemp);

			// vdef15 :
			stemp = rs.getString("vdef15");
			hvo.setVdef15(stemp == null ? null : stemp);

			// vdef16 :
			stemp = rs.getString("vdef16");
			hvo.setVdef16(stemp == null ? null : stemp);

			// vdef17 :
			stemp = rs.getString("vdef17");
			hvo.setVdef17(stemp == null ? null : stemp);

			// vdef18 :
			stemp = rs.getString("vdef18");
			hvo.setVdef18(stemp == null ? null : stemp);

			// vdef19 :
			stemp = rs.getString("vdef19");
			hvo.setVdef19(stemp == null ? null : stemp);

			// vdef20 :
			stemp = rs.getString("vdef20");
			hvo.setVdef20(stemp == null ? null : stemp);

			// pk_defdoc1 :
			stemp = rs.getString("pk_defdoc1");
			hvo.setPk_defdoc1(stemp == null ? null : stemp);

			// pk_defdoc2 :
			stemp = rs.getString("pk_defdoc2");
			hvo.setPk_defdoc2(stemp == null ? null : stemp);

			// pk_defdoc3 :
			stemp = rs.getString("pk_defdoc3");
			hvo.setPk_defdoc3(stemp == null ? null : stemp);

			// pk_defdoc4 :
			stemp = rs.getString("pk_defdoc4");
			hvo.setPk_defdoc4(stemp == null ? null : stemp);

			// pk_defdoc5 :
			stemp = rs.getString("pk_defdoc5");
			hvo.setPk_defdoc5(stemp == null ? null : stemp);

			// pk_defdoc6 :
			stemp = rs.getString("pk_defdoc6");
			hvo.setPk_defdoc6(stemp == null ? null : stemp);

			// pk_defdoc7 :
			stemp = rs.getString("pk_defdoc7");
			hvo.setPk_defdoc7(stemp == null ? null : stemp);

			// pk_defdoc8 :
			stemp = rs.getString("pk_defdoc8");
			hvo.setPk_defdoc8(stemp == null ? null : stemp);

			// pk_defdoc9 :
			stemp = rs.getString("pk_defdoc9");
			hvo.setPk_defdoc9(stemp == null ? null : stemp);

			// pk_defdoc10 :
			stemp = rs.getString("pk_defdoc10");
			hvo.setPk_defdoc10(stemp == null ? null : stemp);

			// pk_defdoc11 :
			stemp = rs.getString("pk_defdoc11");
			hvo.setPk_defdoc11(stemp == null ? null : stemp);

			// pk_defdoc12 :
			stemp = rs.getString("pk_defdoc12");
			hvo.setPk_defdoc12(stemp == null ? null : stemp);

			// pk_defdoc13 :
			stemp = rs.getString("pk_defdoc13");
			hvo.setPk_defdoc13(stemp == null ? null : stemp);

			// pk_defdoc14 :
			stemp = rs.getString("pk_defdoc14");
			hvo.setPk_defdoc14(stemp == null ? null : stemp);

			// pk_defdoc15 :
			stemp = rs.getString("pk_defdoc15");
			hvo.setPk_defdoc15(stemp == null ? null : stemp);

			// pk_defdoc16 :
			stemp = rs.getString("pk_defdoc16");
			hvo.setPk_defdoc16(stemp == null ? null : stemp);

			// pk_defdoc17 :
			stemp = rs.getString("pk_defdoc17");
			hvo.setPk_defdoc17(stemp == null ? null : stemp);

			// pk_defdoc18 :
			stemp = rs.getString("pk_defdoc18");
			hvo.setPk_defdoc18(stemp == null ? null : stemp);

			// pk_defdoc19 :
			stemp = rs.getString("pk_defdoc19");
			hvo.setPk_defdoc19(stemp == null ? null : stemp);

			// pk_defdoc20 :
			stemp = rs.getString("pk_defdoc20");
			hvo.setPk_defdoc20(stemp == null ? null : stemp);

			// pk_defdoc20 :
			stemp = rs.getString("dbilltime");
			hvo.setAttributeValue("dbilltime", stemp == null ? null : new UFDateTime(stemp));
			stemp = rs.getString("daudittime");
			hvo.setAttributeValue("daudittime", stemp == null ? null : new UFDateTime(stemp));
			stemp = rs.getString("dmoditime");
			hvo.setAttributeValue("dmoditime", stemp == null ? null : new UFDateTime(stemp));

			stemp = rs.getString("bcooptopo");
			hvo.setAttributeValue("bcooptopo", stemp == null ? null : new UFBoolean(stemp));

			stemp = rs.getString("bpocooptome");
			hvo.setAttributeValue("bpocooptome", stemp == null ? null : new UFBoolean(stemp));

			stemp = rs.getString("ccooppohid");
			hvo.setAttributeValue("ccooppohid", stemp == null ? null : stemp);

			SOField[] plgins = SaleorderHVO.getPluginFields();
			Object value;
			String key;
			for (SOField plgin : plgins) {
				// SOField.setVoname --> SmartFieldMeta.getName
				key = plgin.getVoname();
				
				// SOField.setUftype --> SmartFieldMeta.getType
				value = getStmt(key, plgin.getUftype(), rs);
				hvo.setAttributeValue(key, value);
			}
			
		} else {

			// btransendflag :
			stemp = rs.getString(starpos);
			hvo.setBtransendflag(stemp == null ? null : new UFBoolean(stemp.trim()));

			starpos++;

			// naccountperiod :
			dectemp = (BigDecimal) rs.getObject(starpos);
			hvo.setNaccountperiod(dectemp == null ? null : new UFDouble(dectemp));

			starpos++;

			// boverdate :
			stemp = rs.getString(starpos);
			hvo.setBoverdate(stemp == null ? null : new UFBoolean(stemp.trim()));

			starpos++;

			// iprintcount
			itemp = (Integer) rs.getObject(starpos);
			hvo.setIprintcount(itemp == null ? null : itemp);

			starpos++;

			// cbaltypeid :
			stemp = rs.getString(starpos);
			hvo.setCbaltypeid(stemp == null ? null : stemp);

			starpos++;

			// vdef11 :
			stemp = rs.getString(starpos);
			hvo.setVdef11(stemp == null ? null : stemp);

			starpos++;

			// vdef12 :
			stemp = rs.getString(starpos);
			hvo.setVdef12(stemp == null ? null : stemp);

			starpos++;

			// vdef13 :
			stemp = rs.getString(starpos);
			hvo.setVdef13(stemp == null ? null : stemp);

			starpos++;

			// vdef14 :
			stemp = rs.getString(starpos);
			hvo.setVdef14(stemp == null ? null : stemp);

			starpos++;

			// vdef15 :
			stemp = rs.getString(starpos);
			hvo.setVdef15(stemp == null ? null : stemp);

			starpos++;

			// vdef16 :
			stemp = rs.getString(starpos);
			hvo.setVdef16(stemp == null ? null : stemp);

			starpos++;

			// vdef17 :
			stemp = rs.getString(starpos);
			hvo.setVdef17(stemp == null ? null : stemp);

			starpos++;

			// vdef18 :
			stemp = rs.getString(starpos);
			hvo.setVdef18(stemp == null ? null : stemp);

			starpos++;

			// vdef19 :
			stemp = rs.getString(starpos);
			hvo.setVdef19(stemp == null ? null : stemp);

			starpos++;

			// vdef20 :
			stemp = rs.getString(starpos);
			hvo.setVdef20(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc1 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc1(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc2 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc2(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc3 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc3(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc4 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc4(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc5 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc5(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc6 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc6(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc7 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc7(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc8 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc8(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc9 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc9(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc10 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc10(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc11 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc11(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc12 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc12(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc13 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc13(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc14 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc14(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc15 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc15(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc16 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc16(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc17 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc17(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc18 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc18(stemp == null ? null : stemp);

			starpos++;

			// pk_defdoc19 :
			stemp = rs.getString(starpos);
			hvo.setPk_defdoc19(stemp == null ? null : stemp);

			starpos++;

			stemp = rs.getString(starpos);
			hvo.setPk_defdoc20(stemp == null ? null : stemp);
			starpos++;
			stemp = rs.getString(starpos);
			hvo.setAttributeValue("dbilltime", stemp == null ? null : new UFDateTime(stemp));
			starpos++;
			stemp = rs.getString(starpos);
			hvo.setAttributeValue("daudittime", stemp == null ? null : new UFDateTime(stemp));
			starpos++;
			stemp = rs.getString(starpos);
			hvo.setAttributeValue("dmoditime", stemp == null ? null : new UFDateTime(stemp));
			starpos++;
			stemp = rs.getString("bcooptopo");
			hvo.setAttributeValue("bcooptopo", stemp == null ? null : new UFBoolean(stemp));
			starpos++;

			stemp = rs.getString("bpocooptome");
			hvo.setAttributeValue("bpocooptome", stemp == null ? null : new UFBoolean(stemp));
			starpos++;

			stemp = rs.getString("ccooppohid");
			hvo.setAttributeValue("ccooppohid", stemp == null ? null : stemp);
			
			SOField[] plgins = SaleorderHVO.getPluginFields();
			Object value;
			String key;
			for (SOField plgin : plgins) {
				// SOField.setVoname --> SmartFieldMeta.getName
				key = plgin.getVoname();
				
				// SOField.setUftype --> SmartFieldMeta.getType
				value = getStmt(key, plgin.getUftype(), rs);
				hvo.setAttributeValue(key, value);
				
				starpos++;
			}
			
		}

		return starpos;

	}

	/**
	 * 拷贝SmartDMO实现
	 * 
	 * @param key
	 * @param javafieldtype
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	private Object getStmt(String key, int javafieldtype, java.sql.ResultSet rs) throws java.sql.SQLException {
		switch (javafieldtype) {
		case SmartFieldMeta.JAVATYPE_INTEGER:
			return rs.getObject(key);
		case SmartFieldMeta.JAVATYPE_STRING:
			return rs.getString(key);
		case SmartFieldMeta.JAVATYPE_UFBOOLEAN:
			String ret1 = (String) rs.getString(key);
			return ret1 == null ? null : new nc.vo.pub.lang.UFBoolean(ret1.trim());
		case SmartFieldMeta.JAVATYPE_UFDATE:
			String ret2 = rs.getString(key);
			return ret2 == null ? null : new nc.vo.pub.lang.UFDate(ret2.trim(), false);
		case SmartFieldMeta.JAVATYPE_UFDOUBLE:
			java.math.BigDecimal ret3 = rs.getBigDecimal(key);
			return ret3 == null ? null : new nc.vo.pub.lang.UFDouble(ret3);
		case SmartFieldMeta.JAVATYPE_UFDATETIME:
			String ret4 = (String) rs.getString(key);
			return ret4 == null ? null : new nc.vo.pub.lang.UFDateTime(ret4.trim(), false);
		case SmartFieldMeta.JAVATYPE_UFTIME:
			String ret5 = (String) rs.getString(key);
			return ret5 == null ? null : new nc.vo.pub.lang.UFTime(ret5.trim(), false);
		default:
			throw new SQLException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("nc_scm_smart",
					"UPPnc_scm_smart-000013")/*
												 * @res "程序错误：不支持的JAVA-NC数据类型."
												 */);
		}
	}
	
	/**
	 * 设置预编译语句参数。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param pos
	 *            int
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private int setPreStatementOrdB(PreparedStatement statement, int starpos, SaleorderBVO bvo)
			throws java.sql.SQLException, nc.bs.pub.SystemException {

		if (statement == null || bvo == null)
			return starpos;

		// cconsigncorpid ---1
		if (bvo.getCconsigncorpid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCconsigncorpid());
		}

		starpos++;

		// nreturntaxrate--2
		if (bvo.getNreturntaxrate() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNreturntaxrate().toBigDecimal());
		}

		starpos++;

		// creccalbodyid--3
		if (bvo.getCreccalbodyid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCreccalbodyid());
		}

		starpos++;

		// crecwareid--4
		if (bvo.getCrecwareid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCrecwareid());
		}

		starpos++;

		// bdericttrans--5
		if (bvo.getBdericttrans() == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, bvo.getBdericttrans().toString());
		}

		starpos++;

		// tconsigntime--6
		if (bvo.getTconsigntime() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getTconsigntime());
		}

		starpos++;

		// tdelivertime--7
		if (bvo.getTdelivertime() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getTdelivertime());
		}

		starpos++;

		// bsafeprice--8
		if (bvo.getBsafeprice() == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, bvo.getBsafeprice().toString());
		}

		starpos++;

		// ntaldcnum--9
		if (bvo.getNtaldcnum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNtaldcnum().toBigDecimal());
		}

		starpos++;

		// nasttaldcnum--10
		if (bvo.getNasttaldcnum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNasttaldcnum().toBigDecimal());
		}

		starpos++;

		// ntaldcmny--11
		if (bvo.getNtaldcmny() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNtaldcmny().toBigDecimal());
		}

		starpos++;

		// breturnprofit--12
		if (bvo.getBreturnprofit() == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, bvo.getBreturnprofit().toString());
		}

		starpos++;

		// nretprofnum--13
		if (bvo.getNretprofnum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNretprofnum().toBigDecimal());
		}

		starpos++;

		// nastretprofnum--14
		if (bvo.getNastretprofnum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNastretprofnum().toBigDecimal());
		}

		starpos++;

		// nretprofmny--15
		if (bvo.getNretprofmny() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNretprofmny().toBigDecimal());
		}

		starpos++;

		// cpricepolicyid--16
		if (bvo.getCpricepolicyid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCpricepolicyid());
		}

		starpos++;

		// cpriceitemid--17
		if (bvo.getCpriceitemid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCpriceitemid());
		}

		starpos++;

		// cpriceitemtable--18
		if (bvo.getCpriceitemtable() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCpriceitemtable());
		}

		starpos++;

		// cpricecalproc--19
		if (bvo.getCpricecalproc() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCpricecalproc());
		}

		starpos++;

		// cquoteunitid--20
		if (bvo.getCquoteunitid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCquoteunitid());
		}

		starpos++;

		// nquoteunitnum--21
		if (bvo.getNquoteunitnum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNquoteunitnum().toBigDecimal());
		}

		starpos++;

		// norgqttaxprc--22
		if (bvo.getNorgqttaxprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNorgqttaxprc().toBigDecimal());
		}

		starpos++;

		// norgqtprc--23
		if (bvo.getNorgqtprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNorgqtprc().toBigDecimal());
		}

		starpos++;

		// norgqttaxnetprc--24
		if (bvo.getNorgqttaxnetprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNorgqttaxnetprc().toBigDecimal());
		}

		starpos++;

		// norgqttaxnetprc--25
		if (bvo.getNorgqtnetprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNorgqtnetprc().toBigDecimal());
		}

		starpos++;

		// nqttaxnetprc--26
		if (bvo.getNqttaxnetprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqttaxnetprc().toBigDecimal());
		}

		starpos++;

		// nqtnetprc--27
		if (bvo.getNqtnetprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqtnetprc().toBigDecimal());
		}

		starpos++;

		// nqttaxprc--28
		if (bvo.getNqttaxprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqttaxprc().toBigDecimal());
		}

		starpos++;

		// nqtprc--29
		if (bvo.getNqtprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqtprc().toBigDecimal());
		}

		starpos++;

		// cprolineid--30
		if (bvo.getCprolineid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCprolineid());
		}

		starpos++;

		// crecaddrnode--31
		if (bvo.getCrecaddrnode() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCrecaddrnode());
		}

		starpos++;

		// cinventoryid1--32
		if (bvo.getCinventoryid1() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCinventoryid1());
		}

		starpos++;

		// cchantypeid--33
		if (bvo.getCchantypeid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCchantypeid());
		}

		starpos++;

		// nqtorgprc--34
		if (bvo.getNqtorgprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqtorgprc().toBigDecimal());
		}

		starpos++;

		// nqtorgtaxprc--35
		if (bvo.getNqtorgtaxprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqtorgtaxprc().toBigDecimal());
		}

		starpos++;

		// nqtscalefactor--36
		UFDouble nqtscalefactor = bvo.getNqtscalefactor();
		if (nqtscalefactor == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, nqtscalefactor.toBigDecimal());
		}

		starpos++;
		// nqtorgtaxnetprc--41
		if (bvo.getNqtorgtaxnetprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqtorgtaxnetprc().toBigDecimal());
		}

		starpos++;
		// nqtorgnetprc--38
		if (bvo.getNqtorgnetprc() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNqtorgnetprc().toBigDecimal());
		}

		starpos++;
		// nqtscalefactor--39
		String clargessrowno = (String) bvo.getAttributeValue("clargessrowno");
		if (clargessrowno == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, clargessrowno);
		}
		starpos++;
		// bbindflag--40
		UFBoolean bbindflag = bvo.getBbindflag();
		if (bbindflag == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, bbindflag.toString());
		}

		starpos++;
		
		// ct_managebid--41
		if (bvo.getCt_managebid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCt_managebid());
		}
		
		starpos++;

		starpos = setBodyPluginFieldValue(starpos, bvo, statement);

		return starpos;
	}

	private int setBodyPluginFieldValue(int starpos, SaleorderBVO bvo, PreparedStatement stmt) throws SQLException {
		SOField[] fields = SaleorderBVO.getPluginFields();
		for (SOField field : fields) {
			setStmt(starpos++, field.getUftype(), bvo.getAttributeValue(field.getVoname()), stmt);
		}// end for

		return starpos;
	}

	/**
	 * 设置预编译语句参数。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param pos
	 *            int
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private int setPreStatementOrdE(PreparedStatement statement, int starpos, SaleorderBVO bvo)
			throws java.sql.SQLException, nc.bs.pub.SystemException {

		if (statement == null || bvo == null)
			return starpos;

		// 最后货源安排时间--1
		if (bvo.getTlastarrangetime() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getTlastarrangetime().toString());
		}

		starpos++;

		// ntaltransnum--2
		if (bvo.getNtaltransnum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNtaltransnum().toBigDecimal());
		}

		starpos++;

		// 最后货源安排人--4
		if (bvo.getCarrangepersonid() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getCarrangepersonid());
		}

		starpos++;

		// 是否货源安排完毕--5
		if (bvo.getBarrangedflag() == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, bvo.getBarrangedflag().toString());
		}

		starpos++;

		// ntalbalancemny--6
		if (bvo.getNtalbalancemny() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNtalbalancemny().toBigDecimal());
		}

		starpos++;

		// 累计安排调拨申请数量--57
		if (bvo.getNarrangemonum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNarrangemonum().toBigDecimal());
		}

		starpos++;

		// ntranslossnum--8
		if (bvo.getNtranslossnum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNtranslossnum().toBigDecimal());
		}

		starpos++;

		// biftransfinish--9
		if (bvo.getBiftransfinish() == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, bvo.getBiftransfinish().toString());
		}

		starpos++;

		// dlastconsigdate--10
		if (bvo.getDlastconsigdate() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getDlastconsigdate().toString());
		}

		starpos++;

		// dlasttransdate--11
		if (bvo.getDlasttransdate() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getDlasttransdate().toString());
		}

		starpos++;

		// dlastoutdate--12
		if (bvo.getDlastoutdate() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getDlastoutdate().toString());
		}

		starpos++;

		// dlastinvoicedt--13
		if (bvo.getDlastinvoicedt() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getDlastinvoicedt().toString());
		}

		starpos++;

		// dlastpaydate--14
		if (bvo.getDlastpaydate() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getDlastpaydate().toString());
		}

		starpos++;

		// vdef7--16
		if (bvo.getVdef7() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef7());
		}

		starpos++;

		// vdef8--17
		if (bvo.getVdef8() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef8());
		}

		starpos++;

		// vdef9--18
		if (bvo.getVdef9() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef9());
		}

		starpos++;

		// vdef10--19
		if (bvo.getVdef10() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef10());
		}

		starpos++;

		// vdef11--20
		if (bvo.getVdef11() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef11());
		}

		starpos++;

		// vdef12--21
		if (bvo.getVdef12() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef12());
		}

		starpos++;

		// vdef13--22
		if (bvo.getVdef13() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef13());
		}

		starpos++;

		// vdef14--23
		if (bvo.getVdef14() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef14());
		}

		starpos++;

		// vdef15--24
		if (bvo.getVdef15() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef15());
		}

		starpos++;

		// vdef16--25
		if (bvo.getVdef16() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef16());
		}

		starpos++;

		// vdef17--26
		if (bvo.getVdef17() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef17());
		}

		starpos++;

		// vdef18--27
		if (bvo.getVdef18() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef18());
		}

		starpos++;

		// vdef19--28
		if (bvo.getVdef19() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef19());
		}

		starpos++;

		// vdef20--29
		if (bvo.getVdef20() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getVdef20());
		}

		starpos++;

		// pk_defdoc1--30
		if (bvo.getPk_defdoc1() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc1());
		}

		starpos++;

		// pk_defdoc2--31
		if (bvo.getPk_defdoc2() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc2());
		}

		starpos++;

		// pk_defdoc3--32
		if (bvo.getPk_defdoc3() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc3());
		}

		starpos++;

		// pk_defdoc4--33
		if (bvo.getPk_defdoc4() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc4());
		}

		starpos++;

		// pk_defdoc5--34
		if (bvo.getPk_defdoc5() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc5());
		}

		starpos++;

		// pk_defdoc6--35
		if (bvo.getPk_defdoc6() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc6());
		}

		starpos++;

		// pk_defdoc7--36
		if (bvo.getPk_defdoc7() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc7());
		}

		starpos++;

		// pk_defdoc8--37
		if (bvo.getPk_defdoc8() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc8());
		}

		starpos++;

		// pk_defdoc9--38
		if (bvo.getPk_defdoc9() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc9());
		}

		starpos++;

		// pk_defdoc10--39
		if (bvo.getPk_defdoc10() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc10());
		}

		starpos++;

		// pk_defdoc11--40
		if (bvo.getPk_defdoc11() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc11());
		}

		starpos++;

		// pk_defdoc12--41
		if (bvo.getPk_defdoc12() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc12());
		}

		starpos++;

		// pk_defdoc13--42
		if (bvo.getPk_defdoc13() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc13());
		}

		starpos++;

		// pk_defdoc14--43
		if (bvo.getPk_defdoc14() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc14());
		}

		starpos++;

		// pk_defdoc15--44
		if (bvo.getPk_defdoc15() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc15());
		}

		starpos++;

		// pk_defdoc16--45
		if (bvo.getPk_defdoc16() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc16());
		}

		starpos++;

		// pk_defdoc17--46
		if (bvo.getPk_defdoc17() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc17());
		}

		starpos++;

		// pk_defdoc18--47
		if (bvo.getPk_defdoc18() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc18());
		}

		starpos++;

		// pk_defdoc19--48
		if (bvo.getPk_defdoc19() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc19());
		}

		starpos++;

		// pk_defdoc20--49
		if (bvo.getPk_defdoc20() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, bvo.getPk_defdoc20());
		}

		starpos++;

		// 累计安排委外订单数量--50
		if (bvo.getNarrangescornum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNarrangescornum().toBigDecimal());
		}

		starpos++;

		// 累计安排请购单数量--51
		if (bvo.getNarrangepoapplynum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNarrangepoapplynum().toBigDecimal());
		}

		starpos++;

		// 累计安排调拨订单数量--52
		if (bvo.getNarrangetoornum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNarrangetoornum().toBigDecimal());
		}

		starpos++;

		// 累计安排调拨申请数量--53
		if (bvo.getNorrangetoapplynum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNorrangetoapplynum().toBigDecimal());
		}
		
		starpos++;

		// 累计安排采购订单数量
		if (bvo.getNarrangepoordernum() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, bvo.getNarrangepoordernum().toBigDecimal());
		}

		return starpos;

	}

	/**
	 * 设置预编译语句参数。 创建日期：(2004-2-18 14:48:17)
	 * 
	 * @param pos
	 *            int
	 * @param field
	 *            nc.vo.so.so001.SOField
	 * @param vo
	 *            nc.vo.pub.CircularlyAccessibleValueObject
	 */
	private int setPreStatementOrdH(PreparedStatement statement, int starpos, SaleorderHVO hvo) throws SQLException {

		if (statement == null || hvo == null)
			return starpos;

		// btransendflag--1
		if (hvo.getBtransendflag() == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, hvo.getBtransendflag().toString());
		}

		starpos++;

		// naccountperiod--2
		if (hvo.getNaccountperiod() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setBigDecimal(starpos, hvo.getNaccountperiod().toBigDecimal());
		}

		starpos++;

		// boverdate--3
		if (hvo.getBoverdate() == null) {
			statement.setString(starpos, "N");
		} else {
			statement.setString(starpos, hvo.getBoverdate().toString());
		}

		starpos++;

		// iprintcount--4
		if (hvo.getIprintcount() == null) {
			statement.setNull(starpos, Types.INTEGER);
		} else {
			statement.setInt(starpos, hvo.getIprintcount().intValue());
		}

		starpos++;

		// 结算方式
		if (hvo.getAttributeValue("cbaltypeid") == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getAttributeValue("cbaltypeid").toString());
		}

		starpos++;

		// vdef11--6
		if (hvo.getVdef11() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef11());
		}

		starpos++;

		// vdef12--7
		if (hvo.getVdef12() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef12());
		}

		starpos++;

		// vdef13--8
		if (hvo.getVdef13() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef13());
		}

		starpos++;

		// vdef14--9
		if (hvo.getVdef14() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef14());
		}

		starpos++;

		// vdef15--10
		if (hvo.getVdef15() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef15());
		}

		starpos++;

		// vdef16--11
		if (hvo.getVdef16() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef16());
		}

		starpos++;

		// vdef17--12
		if (hvo.getVdef17() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef17());
		}

		starpos++;

		// vdef18--13
		if (hvo.getVdef18() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef18());
		}

		starpos++;

		// vdef19--14
		if (hvo.getVdef19() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef19());
		}

		starpos++;

		// vdef20--15
		if (hvo.getVdef20() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getVdef20());
		}

		starpos++;

		// pk_defdoc1--16
		if (hvo.getPk_defdoc1() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc1());
		}

		starpos++;

		// pk_defdoc2--17
		if (hvo.getPk_defdoc2() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc2());
		}

		starpos++;

		// pk_defdoc3--18
		if (hvo.getPk_defdoc3() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc3());
		}

		starpos++;

		// pk_defdoc4--19
		if (hvo.getPk_defdoc4() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc4());
		}

		starpos++;

		// pk_defdoc5--20
		if (hvo.getPk_defdoc5() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc5());
		}

		starpos++;

		// pk_defdoc6--21
		if (hvo.getPk_defdoc6() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc6());
		}

		starpos++;

		// pk_defdoc7--22
		if (hvo.getPk_defdoc7() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc7());
		}

		starpos++;

		// pk_defdoc8--23
		if (hvo.getPk_defdoc8() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc8());
		}

		starpos++;

		// pk_defdoc9--24
		if (hvo.getPk_defdoc9() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc9());
		}

		starpos++;

		// pk_defdoc10--25
		if (hvo.getPk_defdoc10() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc10());
		}

		starpos++;

		// pk_defdoc11--26
		if (hvo.getPk_defdoc11() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc11());
		}

		starpos++;

		// pk_defdoc12--27
		if (hvo.getPk_defdoc12() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc12());
		}

		starpos++;

		// pk_defdoc13--28
		if (hvo.getPk_defdoc13() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc13());
		}

		starpos++;

		// pk_defdoc14--29
		if (hvo.getPk_defdoc14() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc14());
		}

		starpos++;

		// pk_defdoc15--30
		if (hvo.getPk_defdoc15() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc15());
		}

		starpos++;

		// pk_defdoc16--31
		if (hvo.getPk_defdoc16() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc16());
		}

		starpos++;

		// pk_defdoc17--32
		if (hvo.getPk_defdoc17() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc17());
		}

		starpos++;

		// pk_defdoc18--33
		if (hvo.getPk_defdoc18() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc18());
		}

		starpos++;

		// pk_defdoc19--34
		if (hvo.getPk_defdoc19() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc19());
		}

		starpos++;

		if (hvo.getPk_defdoc20() == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getPk_defdoc20());
		}
		starpos++;

		// 系统时间

		UFDateTime serverTime = new UFDateTime(System.currentTimeMillis());
		// 制单时间
		if (hvo.getAttributeValue("dbilltime") == null) {
			statement.setString(starpos, serverTime.toString());
		} else {
			statement.setString(starpos, hvo.getAttributeValue("dbilltime").toString());
		}
		starpos++;

		if (hvo.getAttributeValue("daudittime") == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getAttributeValue("daudittime").toString());
		}
		starpos++;

		// 最后修改时间
		if (hvo.getAttributeValue("dmoditime") == null) {
			statement.setString(starpos, serverTime.toString());
		} else {
			statement.setString(starpos, hvo.getAttributeValue("dmoditime").toString());
		}

		starpos++;

		if (hvo.getAttributeValue("bcooptopo") == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getAttributeValue("bcooptopo").toString());
		}

		starpos++;

		if (hvo.getAttributeValue("bpocooptome") == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getAttributeValue("bpocooptome").toString());
		}

		starpos++;

		if (hvo.getAttributeValue("ccooppohid") == null) {
			statement.setNull(starpos, Types.CHAR);
		} else {
			statement.setString(starpos, hvo.getAttributeValue("ccooppohid").toString());
		}

		starpos++;

		starpos = setHeadPluginFieldValue(starpos, hvo, statement);

		return starpos;

	}

	private int setHeadPluginFieldValue(int starpos, SaleorderHVO hvo, PreparedStatement stmt) throws SQLException {
		SOField[] fields = SaleorderHVO.getPluginFields();
		for (SOField field : fields) {
			setStmt(starpos++, field.getUftype(), hvo.getAttributeValue(field.getVoname()), stmt);
		}// end for

		return starpos;
	}

	private void setStmt(int index, int fieldtype, Object value, PreparedStatement stmt) throws SQLException {

		switch (fieldtype) {
		case SmartFieldMeta.JAVATYPE_INTEGER:
			if (value == null || (value instanceof String && ((String) value).trim().length() == 0)) {
				stmt.setNull(index, java.sql.Types.INTEGER);
			} else {
				if (value instanceof Integer)
					stmt.setInt(index, ((Integer) value).intValue());
				else if (value instanceof Long)
					stmt.setInt(index, ((Long) value).intValue());
				else if (value instanceof Short)
					stmt.setInt(index, ((Short) value).intValue());
				else if (value instanceof Byte)
					stmt.setInt(index, ((Byte) value).intValue());
				else
					stmt.setInt(index, (new Integer(value.toString())).intValue());
			}
			break;
		case SmartFieldMeta.JAVATYPE_STRING:
		case SmartFieldMeta.JAVATYPE_UFDATE:
		case SmartFieldMeta.JAVATYPE_UFDATETIME:
		case SmartFieldMeta.JAVATYPE_UFTIME:
			if (value == null || (value instanceof String && ((String) value).trim().length() == 0))
				stmt.setNull(index, java.sql.Types.CHAR);
			else {
				if (value instanceof String)
					stmt.setString(index, (String) value);
				else
					stmt.setString(index, value.toString());
			}
			break;
		case SmartFieldMeta.JAVATYPE_UFBOOLEAN:
			if (value == null || (value instanceof String && ((String) value).trim().length() == 0))
				stmt.setNull(index, java.sql.Types.CHAR);
			else {
				if (value instanceof String)
					stmt.setString(index, ((String) value).trim().toUpperCase().equals("Y") ? "Y" : "N");
				else if (value instanceof Boolean)
					stmt.setString(index, ((Boolean) value).booleanValue() ? "Y" : "N");
				else
					stmt.setString(index, value.toString());
			}
			break;
		case SmartFieldMeta.JAVATYPE_UFDOUBLE:
			if (value == null || (value instanceof String && ((String) value).trim().length() == 0))
				stmt.setNull(index, java.sql.Types.NUMERIC);
			else {
				if (value instanceof nc.vo.pub.lang.UFDouble)
					stmt.setBigDecimal(index, ((nc.vo.pub.lang.UFDouble) value).toBigDecimal());
				else if (value instanceof String)
					stmt.setBigDecimal(index, new java.math.BigDecimal((String) value));
				else if (value instanceof Double)
					stmt.setBigDecimal(index, new java.math.BigDecimal(((Double) value).doubleValue()));
				else if (value instanceof Float)
					stmt.setBigDecimal(index, new java.math.BigDecimal(((Float) value).doubleValue()));
				else if (value instanceof java.math.BigDecimal)
					stmt.setBigDecimal(index, (java.math.BigDecimal) value);
				else
					stmt.setBigDecimal(index, new java.math.BigDecimal(value.toString()));
			}
			break;
		default:
			throw new SQLException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("nc_scm_smart",
					"UPPnc_scm_smart-000011")/*
			 * @res
			 * "程序错误：元数据中出现了未预定义的JAVA-NC数据类型。"
			 */);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-18 10:50:56)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public void checkSaleOrderVO(nc.vo.so.so001.SaleOrderVO vo) throws javax.naming.NamingException,
			nc.vo.pub.BusinessException, java.sql.SQLException, nc.bs.pub.SystemException {
		if (vo == null)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000041")
			/* @res "订单为空！" */);
		// throw new nc.vo.pub.BusinessException("订单为空！");

		SaleorderHVO ordhvo = vo.getHeadVO();
		SaleorderBVO[] ordbvos = vo.getBodyVOs();

		if (ordhvo == null)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000042")
			/* @res "订单表头为空！" */);
		// throw new nc.vo.pub.BusinessException("订单表头为空！");

		if (ordbvos == null || ordbvos.length <= 0)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000043")
			/* @res "订单表体为空！" */);
		// throw new nc.vo.pub.BusinessException("订单表体为空！");

		String pk_corp = ordhvo.getPk_corp();

		if (pk_corp == null || pk_corp.trim().length() <= 0)
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000044")
			/* @res "订单的公司为空！" */);
		// throw new nc.vo.pub.BusinessException("订单的公司为空！");

		// 单据限行SO_01
		// SO03 相同存货可否共存一单（赠品除外）
		// 产品线SO27
		String[] paracodes = { "SO01", "SO03", "SO27", };

		nc.bs.pub.para.SysInitDMO sysdmo = new nc.bs.pub.para.SysInitDMO();
		java.util.Hashtable h = sysdmo.queryBatchParaValues(pk_corp, paracodes);

		Integer SO01 = null;

		String str = (String) h.get("SO01");
		if (str != null && str.length() > 0)
			SO01 = new Integer(str);

		UFBoolean SO03 = new UFBoolean(true);
		str = (String) h.get("SO03");
		if (str != null && str.length() >= 0)
			SO03 = new UFBoolean(str);

		UFBoolean SO27 = new UFBoolean(false);
		str = (String) h.get("SO27");
		if (str != null && str.length() >= 0)
			SO27 = new UFBoolean(str);

		if (SO01 != null && SO01.intValue() > 0) {
			if (ordbvos.length > SO01.intValue()) {
				throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000045", null, new String[] { SO01.toString() }));
				// throw new nc.vo.pub.BusinessException("订单限行，不能超过["
				// + SO01.intValue() + "]行");
			}
		}

		String cprodline = null;
		String cinvtoryid = null;

		ArrayList cprodlinelist = new ArrayList();
		ArrayList cinvtoryidlist = new ArrayList();

		for (int i = 0, loop = ordbvos.length; i < loop; i++) {

			// 跳过赠品
			if (ordbvos[i].getBlargessflag().booleanValue())
				continue;

			cprodline = ordbvos[i].getCprolineid();
			cinvtoryid = ordbvos[i].getCinventoryid();

			if (cprodline != null && cprodline.trim().length() <= 0)
				cprodline = null;

			if (cinvtoryid == null || cinvtoryid.trim().length() <= 0) {
				throw new nc.vo.pub.ValidationException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000047")
				/* @res "存货不能为空！" */);
				// throw new nc.vo.pub.ValidationException("存货不能为空！");
			}

			if (SO27.booleanValue() && cprodlinelist.size() > 0 && !cprodlinelist.contains(cprodline)) {
				throw new nc.vo.pub.ValidationException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000048")
				/* @res "产品线不唯一！" */);
				// throw new nc.vo.pub.ValidationException("产品线不唯一！");
			}

			if (!SO03.booleanValue() && cinvtoryidlist.size() > 0 && cinvtoryidlist.contains(cinvtoryid)) {
				throw new nc.vo.pub.ValidationException(NCLangResOnserver.getInstance().getStrByID("40060301",
						"UPP40060301-000049")
				/* @res "存货重复！" */);
				// throw new nc.vo.pub.ValidationException("存货重复！");
			}

			cprodlinelist.add(cprodline);

			cinvtoryidlist.add(cinvtoryid);

		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-18 10:50:56)
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 */
	public void processSaleOrderVO(nc.vo.so.so001.SaleOrderVO vo) throws javax.naming.NamingException,
			nc.vo.pub.BusinessException, java.sql.SQLException, nc.bs.pub.SystemException {
		if (vo == null)
			return;

		SaleorderHVO ordhvo = vo.getHeadVO();
		SaleorderBVO[] ordbvos = vo.getBodyVOs();

		if (ordhvo == null || ordbvos == null || ordbvos.length <= 0)
			return;

		if (ordhvo.getCcustomerid() == null || ordhvo.getCcustomerid().trim().length() <= 0) {
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000050")
			/* @res "客户为空，不能生成销售订单" */);
			// throw new nc.vo.pub.BusinessException("客户为空，不能生成销售订单");
		}

		ArrayList formulaslist = new ArrayList();

		// 开票单位
		String creceiptcorpid = ordhvo.getCreceiptcorpid();
		if (creceiptcorpid == null || creceiptcorpid.trim().length() <= 0)
			formulaslist.add("creceiptcorpid->getColValue(bd_cumandoc,pk_cusmandoc2,pk_cumandoc,ccustomerid)");

		// 收货单位
		String creceiptcustomerid = ordhvo.getCreceiptcustomerid();
		if (creceiptcustomerid == null || creceiptcustomerid.trim().length() <= 0)
			formulaslist.add("creceiptcustomerid->getColValue(bd_cumandoc,pk_cusmandoc3,pk_cumandoc,ccustomerid)");

		// 部门
		String cdeptid = ordhvo.getCdeptid();
		if (cdeptid == null || cdeptid.trim().length() <= 0)
			formulaslist.add("cdeptid->getColValue(bd_cumandoc,pk_respdept1,pk_cumandoc,ccustomerid)");

		// 业务员
		String cemployeeid = ordhvo.getCemployeeid();
		if (cemployeeid == null || cemployeeid.trim().length() <= 0)
			formulaslist.add("cemployeeid->getColValue(bd_cumandoc,pk_resppsn1,pk_cumandoc,ccustomerid)");

		// 整单扣率
		UFDouble ndiscountrate = ordbvos[0].getNdiscountrate();
		if (ndiscountrate == null)
			ndiscountrate = ordhvo.getNdiscountrate();
		else
			ordhvo.setNdiscountrate(ndiscountrate);
		if (ndiscountrate == null)
			formulaslist.add("ndiscountrate->getColValue(bd_cumandoc,discountrate,pk_cumandoc,ccustomerid)");

		// 发运方式
		String ctransmodeid = ordhvo.getCtransmodeid();
		if (ctransmodeid == null || ctransmodeid.trim().length() <= 0)
			formulaslist.add("ctransmodeid->getColValue(bd_cumandoc,pk_sendtype,pk_cumandoc,ccustomerid)");

		// 默认交易币种
		String ccurrencytypeid = ordbvos[0].getCcurrencytypeid();
		if (ccurrencytypeid == null || ccurrencytypeid.trim().length() <= 0)
			ccurrencytypeid = ordhvo.getCcurrencytypeid();
		else
			ordhvo.setCcurrencytypeid(ccurrencytypeid);
		if (ccurrencytypeid == null || ccurrencytypeid.trim().length() <= 0)
			formulaslist.add("ccurrencytypeid->getColValue(bd_cumandoc,pk_currtype1,pk_cumandoc,ccustomerid)");

		// 收付款协议
		String ctermprotocolid = ordhvo.getCtermprotocolid();
		if (ctermprotocolid == null || ctermprotocolid.trim().length() <= 0)
			formulaslist.add("ctermprotocolid->getColValue(bd_cumandoc,pk_payterm,pk_cumandoc,ccustomerid)");

		// 库存组织
		String ccalbodyid = ordhvo.getCcalbodyid();
		if (ccalbodyid == null || ccalbodyid.trim().length() <= 0)
			formulaslist.add("ccalbodyid->getColValue(bd_cumandoc,pk_calbody,pk_cumandoc,ccustomerid)");

		// 销售组织
		String csalecorpid = ordhvo.getCsalecorpid();
		if (csalecorpid == null || csalecorpid.trim().length() <= 0)
			formulaslist.add("csalecorpid->getColValue(bd_cumandoc,pk_salestru,pk_cumandoc,ccustomerid)");

		// 帐期（在5.3版本就要求删除账期,在5.5版本降缓存项目中补删 by zhouchangsheng）
		/*
		 * formulaslist
		 * .add("naccountperiod->getColValue(bd_cumandoc,acclimit,pk_cumandoc,ccustomerid)");
		 */

		// 渠道id
		formulaslist.add("ccustomername->getColValue(bd_cumandoc,pk_pricegroupcorp,pk_cumandoc,ccustomerid)");

		// if(formulaslist.size()>0)
		nc.vo.so.so016.SoVoTools.execFormulasAtBs((String[]) formulaslist.toArray(new String[formulaslist.size()]),
				new SaleorderHVO[] { ordhvo });

		nc.impl.scm.so.pub.FetchValueDMO fetdmo = new nc.impl.scm.so.pub.FetchValueDMO();

		// 处理默认收货地址地点
		creceiptcustomerid = ordhvo.getCreceiptcustomerid();

		String creceiptareaid = null;

		String vreceiveaddress = null;

		String crecaddrnode = null;

		String cchantypeid = (String) ordhvo.getAttributeValue("ccustomername");

		if (creceiptcustomerid != null && creceiptcustomerid.trim().length() > 0) {
			// 收货单位客户基本档案
			String pk_cubasdoc = fetdmo.getColValue("bd_cumandoc", "pk_cubasdoc", " pk_cumandoc='" + creceiptcustomerid
					+ "' ");
			// 默认收货地址
			String pk_custaddr = fetdmo.getColValue("bd_custaddr", "pk_custaddr", " pk_cubasdoc='" + pk_cubasdoc
					+ "' and defaddrflag = 'Y' ");

			if (pk_custaddr != null && pk_custaddr.trim().length() > 0) {

				Object[] otemps = fetdmo.getColValue("bd_custaddr", new String[] { "addrname", "pk_areacl",
						"pk_address" }, " pk_custaddr='" + pk_custaddr + "' ");

				if (otemps != null && otemps.length == 3) {
					vreceiveaddress = (otemps[0] == null ? null : otemps[0].toString());
					creceiptareaid = (otemps[1] == null ? null : otemps[1].toString());
					crecaddrnode = (otemps[2] == null ? null : otemps[2].toString());
				}

			}

		}

		if (ordhvo.getDbilldate() == null) {
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000051")
			/* @res "单据日期为空，不能生成销售订单" */);
			// throw new nc.vo.pub.BusinessException("单据日期为空，不能生成销售订单");
		}

		String pk_corp = ordhvo.getPk_corp();

		if (pk_corp == null || pk_corp.trim().length() <= 0) {
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000052")
			/* @res "公司为空，不能生成销售订单" */);
			// throw new nc.vo.pub.BusinessException("公司为空，不能生成销售订单");
		}

		if (ordhvo.getVreceiveaddress() == null || ordhvo.getVreceiveaddress().trim().length() <= 0) {
			ordhvo.setVreceiveaddress(vreceiveaddress);
		} else {
			vreceiveaddress = ordhvo.getVreceiveaddress();
		}

		// 处理币种及汇率
		UFDouble uf100 = new UFDouble(100);
		// UFDouble uf0 = new UFDouble(0);
		UFDouble uf1 = new UFDouble(1);

		ccurrencytypeid = ordhvo.getCcurrencytypeid();
		if (ccurrencytypeid == null || ccurrencytypeid.trim().length() <= 0) {
			throw new nc.vo.pub.BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
					"UPP40060301-000053")
			/* @res "币种为空也未设置客户的默认交易币种，不能生成销售订单" */);
			// throw new
			// nc.vo.pub.BusinessException("币种为空也未设置客户的默认交易币种，不能生成销售订单");
		}

		// 整单折扣
		ndiscountrate = null;
		ndiscountrate = ordhvo.getNdiscountrate();
		if (ndiscountrate == null)
			ndiscountrate = ordbvos[0].getNdiscountrate();
		if (ndiscountrate == null)
			ndiscountrate = uf100;

		ordhvo.setNdiscountrate(ndiscountrate);

		// 处理表体默认值
		String bodyfomulas[] = {

		"cinvbasdocid->getColValue(bd_invmandoc,pk_invbasdoc,pk_invmandoc,cinventoryid)",
				"nreturntaxrate->getColValue(bd_invmandoc,expaybacktax,pk_invmandoc,cinventoryid)",
				"isappendant->getColValue(bd_invmandoc,isappendant,pk_invmandoc,cinventoryid)",
				"cinventorycode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cinvbasdocid)",
				"cinventoryname->getColValue(bd_invbasdoc,invname,pk_invbasdoc,cinvbasdocid)",
				"cprolineid->getColValue(bd_invbasdoc,pk_prodline,pk_invbasdoc,cinvbasdocid)",
				"discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)",
				"laborflag->getColValue(bd_invbasdoc,laborflag,pk_invbasdoc,cinvbasdocid)",
				"assistunit->getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,cinvbasdocid)" };

		nc.vo.so.so016.SoVoTools.execFormulasAtBs(bodyfomulas, ordbvos);

		ArrayList invidlist = new ArrayList(ordbvos.length);

		// String cwarehouseid = ordhvo.getCwarehouseid();
		ccalbodyid = ordhvo.getCcalbodyid();

		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			if (ordbvos[i].getCinventoryid() != null)
				invidlist.add(ordbvos[i].getCinventoryid());

			// ordbvos[i].setCcurrencytypeid(ccurrencytypeid);
			// ordbvos[i].setNexchangeotoarate(nexchangeotoarate);
			// ordbvos[i].setNexchangeotobrate(nexchangeotobrate);
			ordbvos[i].setNdiscountrate(ndiscountrate);
			// 单品折扣
			if (ordbvos[i].getNitemdiscountrate() == null)
				ordbvos[i].setNitemdiscountrate(uf100);

			if (ordbvos[i].getCreceiptcorpid() == null || ordbvos[i].getCreceiptcorpid().trim().length() <= 0) {
				ordbvos[i].setCreceiptcorpid(creceiptcustomerid);
			}

			if (ordbvos[i].getCreceiptareaid() == null || ordbvos[i].getCreceiptareaid().trim().length() <= 0) {
				ordbvos[i].setCreceiptareaid(creceiptareaid);
			}

			if (ordbvos[i].getVreceiveaddress() == null || ordbvos[i].getVreceiveaddress().trim().length() <= 0) {
				ordbvos[i].setVreceiveaddress(vreceiveaddress);
			}

			if (ordbvos[i].getCrecaddrnode() == null || ordbvos[i].getCrecaddrnode().trim().length() <= 0) {
				ordbvos[i].setCrecaddrnode(crecaddrnode);
			}

			if (ordbvos[i].getCadvisecalbodyid() == null || ordbvos[i].getCadvisecalbodyid().trim().length() <= 0) {
				ordbvos[i].setCadvisecalbodyid(ccalbodyid);
			}

			// if(ordbvos[i].getCbodywarehouseid()==null ||
			// ordbvos[i].getCbodywarehouseid().trim().length()<=0){
			// ordbvos[i].setCbodywarehouseid(cwarehouseid);
			// }

			if (ordbvos[i].getCquoteunitid() == null || ordbvos[i].getCquoteunitid().trim().length() <= 0) {
				ordbvos[i].setCquoteunitid(ordbvos[i].getCunitid());
				ordbvos[i].setNqtscalefactor(uf1);
			}

			if (ordbvos[i].getNquoteunitnum() == null) {
				ordbvos[i].setNquoteunitnum(ordbvos[i].getNnumber());
			}

			if (ordbvos[i].getNorgqttaxprc() == null) {
				ordbvos[i].setNorgqttaxprc(ordbvos[i].getNoriginalcurtaxprice());
			}

			if (ordbvos[i].getNorgqtprc() == null) {
				ordbvos[i].setNorgqtprc(ordbvos[i].getNoriginalcurprice());
			}

			if (ordbvos[i].getNorgqttaxnetprc() == null) {
				ordbvos[i].setNorgqttaxnetprc(ordbvos[i].getNoriginalcurtaxnetprice());
			}

			if (ordbvos[i].getNorgqtnetprc() == null) {
				ordbvos[i].setNorgqtnetprc(ordbvos[i].getNoriginalcurnetprice());
			}

			if (ordbvos[i].getNqttaxprc() == null) {
				ordbvos[i].setNqttaxprc(ordbvos[i].getNtaxprice());
			}

			if (ordbvos[i].getNorgqtprc() == null) {
				ordbvos[i].setNorgqtprc(ordbvos[i].getNprice());
			}

			if (ordbvos[i].getNorgqttaxnetprc() == null) {
				ordbvos[i].setNorgqttaxnetprc(ordbvos[i].getNtaxnetprice());
			}

			if (ordbvos[i].getNorgqtnetprc() == null) {
				ordbvos[i].setNorgqtnetprc(ordbvos[i].getNnetprice());
			}

			if (ordbvos[i].getDconsigndate() == null) {
				ordbvos[i].setDconsigndate(ordhvo.getDbilldate());
			}

			if (ordbvos[i].getDdeliverdate() == null) {
				ordbvos[i].setDdeliverdate(ordhvo.getDbilldate());
			}

			if (ordbvos[i].getCchantypeid() == null || ordbvos[i].getCchantypeid().trim().length() <= 0) {
				ordbvos[i].setCchantypeid(cchantypeid);
			}

			if (ordbvos[i].getBoosflag() == null)
				ordbvos[i].setBoosflag(new UFBoolean(false));
			if (ordbvos[i].getBsupplyflag() == null)
				ordbvos[i].setBoosflag(new UFBoolean(false));

			if (ordbvos[i].getCconsigncorpid() == null || ordbvos[i].getCconsigncorpid().trim().length() <= 0)
				ordbvos[i].setCconsigncorpid(pk_corp);

			if (ordbvos[i].getCinventoryid1() == null || ordbvos[i].getCinventoryid1().trim().length() <= 0)
				ordbvos[i].setCinventoryid1(ordbvos[i].getCinventoryid());

		}

		// 处理渠道分组

		if (invidlist.size() > 0) {

			nc.impl.scm.so.so017.ChannelGroupDMO groupdmo = new nc.impl.scm.so.so017.ChannelGroupDMO();

			HashMap hschantypeid = groupdmo.getChantypeidOfCust(ordhvo.getPk_corp(), ordhvo.getCcustomerid());

			HashMap hscinvclassid = nc.impl.scm.so.so016.SOToolsDMO.getAnyValue("bd_invbasdoc", "pk_invcl",
					"pk_invbasdoc", nc.vo.so.so016.SoVoTools.getVOsOnlyValues(ordbvos, "cinvbasdocid"), null);

			// "cchantypeid->getColValue(bd_cumandoc,pk_pricegroupcorp,pk_cumandoc,ccustomerid)",
			nc.impl.scm.so.pub.FetchValueDMO ftdmo = new nc.impl.scm.so.pub.FetchValueDMO();
			String pk_pricegroupcorp = ftdmo.getColValue("bd_cumandoc", "pk_pricegroupcorp", " pk_cumandoc='"
					+ ordhvo.getCcustomerid() + "' ");

			String cinvclassid = null;
			String cinventoryid = null;
			String chantypeid = null;
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {

				cinventoryid = ordbvos[i].getCinventoryid();
				if (cinventoryid == null)
					continue;

				cinvclassid = null;
				if (hscinvclassid != null)
					cinvclassid = (String) hscinvclassid.get(cinventoryid);

				if (hschantypeid != null) {
					chantypeid = (String) hschantypeid.get(cinventoryid);
					if (chantypeid == null || chantypeid.trim().length() <= 0) {
						if (cinvclassid != null)
							chantypeid = (String) hschantypeid.get(cinvclassid);
					}
				}

				if (chantypeid == null || chantypeid.trim().length() <= 0) {
					chantypeid = pk_pricegroupcorp;
				}

				ordbvos[i].setCchantypeid(chantypeid);
			}

		}

	}

	/**
	 * 订单审批推调拨订单时调用,从多公司销售订单转换得到调拨订单VOS5D。 创建日期：(2004-4-20 20:14:41)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject[]
	 * @param ordvo
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	public void PushSave5A(nc.vo.pub.AggregatedValueObject ordvo, Object clientojb, String curdate)
			throws javax.naming.NamingException, javax.ejb.CreateException, java.sql.SQLException, BusinessException {
		if (ordvo == null)
			return;

		// 取收货库存组织的收货地区，收货地址，收货地点
		HashMap hscalbody = null;
		// 取收获仓库的收货地区，收货地址，收货地点
		HashMap hswareaddr = null;
		nc.vo.to.pub.BillVO voBilltep = null;

		nc.bs.pub.pf.PfUtilBO pfbo = null;
		try {

			// 按发货公司,是否直运,调入库存组织分单
			// SaleOrderVO[] ordvos = (SaleOrderVO[])
			// nc.vo.scm.pub.vosplit.SplitBillVOs
			// .getSplitVO("nc.vo.so.so001.SaleOrderVO",
			// "nc.vo.so.so001.SaleorderHVO",
			// "nc.vo.so.so001.SaleorderBVO", ordvo, null,
			// new String[] { "cconsigncorpid" });

			// 按发货公司,是否直运,调入库存组织分单
			ArrayList abvolist = null;
			SaleorderBVO[] ordbvos = (SaleorderBVO[]) ordvo.getChildrenVO();
			String vokey = null;
			HashMap hssplitvo = new HashMap();

			ArrayList calbodylist = new ArrayList();
			ArrayList warelist = new ArrayList();
			for (int i = 0, loop = ordbvos.length; i < loop; i++) {
				vokey = "";
				if (ordbvos[i].getCconsigncorpid() == null || ordbvos[i].getCconsigncorpid().trim().length() <= 0
						|| ordbvos[i].getCconsigncorpid().equals(ordbvos[i].getPkcorp())) {
					vokey += ordbvos[i].getPkcorp();
					if (ordbvos[i].getCadvisecalbodyid() != null
							&& ordbvos[i].getCadvisecalbodyid().trim().length() > 0) {
						vokey += ordbvos[i].getCadvisecalbodyid();
					}
				} else {
					vokey += ordbvos[i].getCconsigncorpid();
					if (ordbvos[i].getCreccalbodyid() != null && ordbvos[i].getCreccalbodyid().trim().length() > 0) {
						vokey += ordbvos[i].getCreccalbodyid();
					}
				}
				if (ordbvos[i].getBdericttrans() == null)
					vokey += SoVoConst.buffalse;
				else
					vokey += ordbvos[i].getBdericttrans();
				abvolist = (ArrayList) hssplitvo.get(vokey);
				if (abvolist == null) {
					abvolist = new ArrayList();
					hssplitvo.put(vokey, abvolist);
				}
				abvolist.add(ordbvos[i]);
				if (ordbvos[i].getCadvisecalbodyid() != null && !calbodylist.contains(ordbvos[i].getCadvisecalbodyid()))
					calbodylist.add(ordbvos[i].getCadvisecalbodyid());
				if (ordbvos[i].getCreccalbodyid() != null && !calbodylist.contains(ordbvos[i].getCreccalbodyid()))
					calbodylist.add(ordbvos[i].getCreccalbodyid());

				if (ordbvos[i].getCbodywarehouseid() != null && !warelist.contains(ordbvos[i].getCbodywarehouseid()))
					warelist.add(ordbvos[i].getCbodywarehouseid());
				if (ordbvos[i].getCrecwareid() != null && !warelist.contains(ordbvos[i].getCrecwareid()))
					warelist.add(ordbvos[i].getCrecwareid());

			}
			if (hssplitvo.size() <= 0)
				return;

			String[] calbodyids = (String[]) calbodylist.toArray(new String[calbodylist.size()]);
			String[] warehouseids = (String[]) warelist.toArray(new String[warelist.size()]);

			SaleOrderVO[] ordvos = new SaleOrderVO[hssplitvo.size()];
			Iterator iter = hssplitvo.values().iterator();
			int count = 0;
			while (iter.hasNext()) {
				abvolist = (ArrayList) iter.next();
				ordvos[count] = new SaleOrderVO();
				ordvos[count].setParentVO(ordvo.getParentVO());
				ordvos[count].setChildrenVO((SaleorderBVO[]) abvolist.toArray(new SaleorderBVO[abvolist.size()]));
				count++;
			}

			// 将订单vo转化为调拨申请单5A
			nc.vo.pub.AggregatedValueObject[] vos5A = PfUtilTools.runChangeDataAry("30", "5A", ordvos);
			if (vos5A == null)
				return;

			ArrayList ojblist = new ArrayList();
			ojblist.add(null);
			ojblist.add(((SaleOrderVO) ordvo).getClientLink());

			String[] addrs = null;
			String[] wareaddrs = null;

			pfbo = new nc.bs.pub.pf.PfUtilBO();

			Object[] userobjarr = new Object[vos5A.length];

			// 直运仓
			HashMap hsware = null;
			String consigncorpid = null;

			for (int i = 0, loop = ordvos.length; i < loop; i++) {

				userobjarr[i] = ojblist;

				if (vos5A[i] == null || vos5A[i].getParentVO() == null)
					continue;
				// 置操作人
				voBilltep = (nc.vo.to.pub.BillVO) vos5A[i];
				voBilltep.setOperator(ordvos[i].getParentVO().getAttributeValue("capproveid").toString());

				consigncorpid = ordvos[i].getBodyVOs()[0].getCconsigncorpid();
				if (consigncorpid == null || consigncorpid.trim().length() <= 0
						|| consigncorpid.equals(ordvos[i].getHeadVO().getPk_corp())) {

					// vos5A[i].getParentVO().setAttributeValue("cincbid",);
					vos5A[i].getParentVO()
							.setAttributeValue("cincbid", ordvos[i].getBodyVOs()[0].getCadvisecalbodyid());
					if (vos5A[i].getParentVO().getAttributeValue("cincbid") == null)
						vos5A[i].getParentVO().setAttributeValue("cincbid", ordvos[i].getHeadVO().getCcalbodyid());
					for (int m = 0, loopm = ordvos[i].getBodyVOs().length; m < loopm; m++) {
						// 置行号
						vos5A[i].getChildrenVO()[m].setAttributeValue("crowno", ("" + (m + 1)));
						// 设置调入仓库为调出仓库
						vos5A[i].getChildrenVO()[m].setAttributeValue("cinwhid", vos5A[i].getChildrenVO()[m]
								.getAttributeValue("coutwhid"));
						// 设置调出公司为空coutcorpid
						vos5A[i].getChildrenVO()[m].setAttributeValue("coutcorpid", null);
						// 设置调出库存组织为空
						vos5A[i].getChildrenVO()[m].setAttributeValue("coutcbid", null);
						// 设置调出仓库为空
						vos5A[i].getChildrenVO()[m].setAttributeValue("coutwhid", null);
						if (ordvos[i].getBodyVOs()[m].getBdericttrans() != null
								&& ordvos[i].getBodyVOs()[m].getBdericttrans().booleanValue()) {
							// 直运调拨
							vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(0));
						} else {
							// 入库调拨
							// 清空收货客户，收货地区，收货地址
							// 取收货库存组织的收货地区，收货地址，收货地点
							if (hscalbody == null) {
								hscalbody = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_calbody",
										new String[] { "pk_areacl", "area", "pk_address" }, "pk_calbody", calbodyids,
										null);
								if (hscalbody == null)
									hscalbody = new HashMap();
							}

							// 取收货仓库的收货地址，收货地点
							if (hswareaddr == null) {
								hswareaddr = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_stordoc",
										new String[] { "pk_calbody", "storaddr", "pk_address" }, "pk_stordoc",
										warehouseids, null);
								if (hswareaddr == null)
									hswareaddr = new HashMap();
							}

							// 如果入库调拨，置收货单位为空，设置收货库存组织的收货地区，收货地址，收货地点
							vos5A[i].getChildrenVO()[m].setAttributeValue("creceieveid", null);

							addrs = (String[]) hscalbody.get(ordvos[i].getBodyVOs()[m].getCadvisecalbodyid() + "");

							wareaddrs = (String[]) hswareaddr.get(ordvos[i].getBodyVOs()[m].getCbodywarehouseid() + "");

							if (wareaddrs == null) {

								if (addrs == null) {

									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);
									vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", null);
									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", null);

								} else {

									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);
									vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);
									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);

								}

							} else {

								if (addrs == null) {

									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);
									vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);

								} else {

									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

									if (wareaddrs[1] != null && wareaddrs[1].trim().length() > 0)
										vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
									else
										vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);

									if (wareaddrs[2] != null && wareaddrs[2].trim().length() > 0)
										vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
									else
										vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);

								}
							}
							// 入库调拨
							vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(1));

						}
					}
					continue;
				}

				for (int m = 0, loopm = ordvos[i].getBodyVOs().length; m < loopm; m++) {
					// 设置调拨类型,根据订单是否直运
					if (ordvos[i].getBodyVOs()[m].getBdericttrans() != null
							&& ordvos[i].getBodyVOs()[m].getBdericttrans().booleanValue()) {

						// 取直运仓
						if (hsware == null) {
							hsware = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_stordoc",
									new String[] { "pk_stordoc" }, "pk_calbody", nc.vo.so.so016.SoVoTools
											.getVOsOnlyValues(ordvo.getChildrenVO(), "creccalbodyid"),
									" isdirectstore='Y' ");
							if (hsware == null)
								hsware = new HashMap();
						}

						addrs = (String[]) hsware.get(ordvos[i].getBodyVOs()[m].getCreccalbodyid() + "");
						if (addrs != null) {

							vos5A[i].getChildrenVO()[m].setAttributeValue("cinwhid", addrs[0]);

						}
						// 直运调拨
						vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(0));
					} else {
						// 取收货库存组织的收货地区，收货地址，收货地点
						if (hscalbody == null) {
							hscalbody = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_calbody", new String[] {
									"pk_areacl", "area", "pk_address" }, "pk_calbody", calbodyids, null);
							if (hscalbody == null)
								hscalbody = new HashMap();
						}

						// 取收货仓库的收货地址，收货地点
						if (hswareaddr == null) {
							hswareaddr = nc.impl.scm.so.so016.SOToolsDMO.getAnyValueArray("bd_stordoc", new String[] {
									"pk_calbody", "storaddr", "pk_address" }, "pk_stordoc", warehouseids, null);
							if (hswareaddr == null)
								hswareaddr = new HashMap();
						}

						// 如果入库调拨，置收货单位为空，设置收货库存组织的收货地区，收货地址，收货地点
						vos5A[i].getChildrenVO()[m].setAttributeValue("creceieveid", null);

						addrs = (String[]) hscalbody.get(ordvos[i].getBodyVOs()[m].getCreccalbodyid() + "");

						wareaddrs = (String[]) hswareaddr.get(ordvos[i].getBodyVOs()[m].getCrecwareid() + "");

						if (wareaddrs == null) {

							if (addrs == null) {

								vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);
								vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", null);
								vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", null);

							} else {

								vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);
								vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);
								vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);

							}

						} else {

							if (addrs == null) {

								vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", null);
								vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
								vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);

							} else {

								vos5A[i].getChildrenVO()[m].setAttributeValue("pk_arrivearea", addrs[0]);

								if (wareaddrs[1] != null && wareaddrs[1].trim().length() > 0)
									vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", wareaddrs[1]);
								else
									vos5A[i].getChildrenVO()[m].setAttributeValue("vreceiveaddress", addrs[1]);

								if (wareaddrs[2] != null && wareaddrs[2].trim().length() > 0)
									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", wareaddrs[2]);
								else
									vos5A[i].getChildrenVO()[m].setAttributeValue("pk_areacl", addrs[2]);

							}
						}
						// 入库调拨
						vos5A[i].getChildrenVO()[m].setAttributeValue("fallocflag", new Integer(1));
					}
					// 置行号
					// vos5A[i].getChildrenVO()[m].setAttributeValue("crowno",(""+(m+1)));

				}

			}

			try {
				// 保存调拨申请单
				pfbo.processBatch("PUSHSAVEVO", "5A", curdate, vos5A, userobjarr, null);
			} catch (Exception e) {
				reportException(e);
				if (nc.vo.so.pub.ExceptionUtils.marshException(e) instanceof ATPNotEnoughException)
					throw new BusinessException(e);

				throw new BusinessException(e.getMessage());
			}

		} finally {
		}
	}

	/**
	 * 订单审批推请购单,调拨申请,调拨订单时调用,从销售订单分单得到生成请购单,调拨申请,调拨订单的VO。 创建日期：(2004-4-20
	 * 20:14:41)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject[]
	 * @param ordvo
	 *            nc.vo.so.so001.SaleOrderVO
	 * 
	 * @deprecated V5.01
	 * @see SaleOrderSrvDMO.pushBillinSingleCorp(...)
	 * @see SaleOrderSrvDMO.pushBillinMultiCorp(...)
	 */
	public HashMap splitOrdVOFor20_5A_5D(nc.vo.pub.AggregatedValueObject ordvo) throws javax.naming.NamingException,
			javax.ejb.CreateException, java.sql.SQLException, BusinessException {

		HashMap hmreturn = new HashMap();

		SaleOrderVO[] retordvos = new SaleOrderVO[3];
		if (ordvo == null)
			return hmreturn;

		// 取so35参数
		nc.bs.pub.para.SysInitDMO sysdmo = new nc.bs.pub.para.SysInitDMO();
		String so35 = sysdmo.getParaString("0001", "SO35");
		boolean isPush5D = false;
		if ("调拨订单".equals(so35))/*-=notranslate=-*/
			isPush5D = true;

		// 取存货生产档案中物料类型:DB内部交易 MR采购 PR制造件
		HashMap hsinvprop = null;
		SaleorderHVO ordhvo = (SaleorderHVO) ordvo.getParentVO();
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) ordvo.getChildrenVO();
		if (ordhvo == null || ordbvos == null || ordbvos.length <= 0)
			return hmreturn;
		String pk_corp = ordhvo.getPk_corp();
		ArrayList bvo20list = new ArrayList();
		ArrayList bvo5Alist = new ArrayList();
		ArrayList bvo5Dlist = new ArrayList();
		String consigncorpid = null, cmatertype = null;
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			consigncorpid = ordbvos[i].getCconsigncorpid();
			// 1) 如果销售订单行的发货公司为销售订单公司，则表示销售订单需要形成请购单或调拨申请：
			if (consigncorpid == null || consigncorpid.trim().length() <= 0 || consigncorpid.equals(pk_corp)) {
				if (consigncorpid != null && consigncorpid.trim().length() <= 0)
					ordbvos[i].setCconsigncorpid(null);
				if (hsinvprop == null) {
					// 取存货生产档案中物料类型:DB内部交易 MR采购 PR制造件
					hsinvprop = nc.impl.scm.so.so016.SOToolsDMO
							.getAnyValue("bd_produce,so_saleorder_b", "bd_produce.matertype",
									"so_saleorder_b.corder_bid", nc.vo.so.so016.SoVoTools.getSArrayVOsValue(ordvo
											.getChildrenVO(), "corder_bid"),
									" bd_produce.pk_invmandoc=so_saleorder_b.cinventoryid and bd_produce.pk_calbody=so_saleorder_b.cadvisecalbodyid ");
					if (hsinvprop == null)
						hsinvprop = new HashMap();
				}
				// 去掉数量为负的存货
				if (ordbvos[i].getNnumber() == null || ordbvos[i].getNnumber().doubleValue() <= 0)
					continue;
				cmatertype = (String) hsinvprop.get(ordbvos[i].getCorder_bid());
				// a) 如果存货生产档案中物料类型为“采购件”，则销售订单行形成销售公司的采购申请；
				if ("MR".equals(cmatertype)) {
					bvo20list.add(ordbvos[i]);
					// b) 如果存货生产档案中物料类型为“内部调拨”，则销售订单行形成销售公司的调拨申请
				} else if ("DB".equals(cmatertype)) {
					bvo5Alist.add(ordbvos[i]);
				}

				// 2) 如果销售订单行的发货公司与销售订单公司不相同，则表示销售订单需要形成调拨申请或调拨订单
			} else {
				// a) 如果跨公司销售订单与内部交易接口单据参数为“调拨申请”，则销售订单行形成销售公司的调拨申请
				if (isPush5D) {
					bvo5Dlist.add(ordbvos[i]);
				} else {
					// b) 如果跨公司销售订单与内部交易接口单据参数为“调拨订单”，则销售订单行形成发货公司的调拨订单
					// 去掉数量为负的存货
					if (ordbvos[i].getNnumber() == null || ordbvos[i].getNnumber().doubleValue() <= 0)
						continue;
					bvo5Alist.add(ordbvos[i]);
				}
			}
		}
		String SO58 = null;
		try {
			SO58 = sysdmo.getParaString(pk_corp, "SO58");
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		}
		if (SO58 == null || SO58.trim().length() == 0)
			SO58 = "按订单数量";
		Integer iBD501 = sysdmo.getParaInt(pk_corp, "BD501");
		if (iBD501 == null)
			iBD501 = new Integer(2);

		// 构造生成采购申请20的vo
		if (bvo20list.size() > 0) {
			retordvos[0] = new SaleOrderVO();
			((SaleOrderVO) retordvos[0]).setClientLink(((SaleOrderVO) ordvo).getClientLink());
			retordvos[0].setParentVO(ordhvo);
			retordvos[0].setChildrenVO((SaleorderBVO[]) bvo20list.toArray(new SaleorderBVO[bvo20list.size()]));
			// hmreturn.put("CGSQ",new SaleOrderVO[]{retordvos[0]});
			if (SO58.equals("按净需求"))
				retordvos[0] = getNumberBySO58(retordvos[0], getLabors((SaleorderBVO[]) retordvos[0].getChildrenVO()),
						iBD501.intValue());
			if (retordvos[0] != null)
				hmreturn.put("CGSQ", splitBodyItemsByOuttype(retordvos[0]));
		}
		// 构造生成调拨申请5A的vo
		if (bvo5Alist.size() > 0) {
			retordvos[1] = new SaleOrderVO();
			((SaleOrderVO) retordvos[1]).setClientLink(((SaleOrderVO) ordvo).getClientLink());
			retordvos[1].setParentVO(ordhvo);
			retordvos[1].setChildrenVO((SaleorderBVO[]) bvo5Alist.toArray(new SaleorderBVO[bvo5Alist.size()]));
			if (SO58.equals("按净需求"))
				retordvos[1] = getNumberBySO58(retordvos[1], getLabors((SaleorderBVO[]) retordvos[1].getChildrenVO()),
						iBD501.intValue());
			if (retordvos[1] != null)
				hmreturn.put("DBSQ", retordvos[1]);
		}
		// 构造生成调拨订单5D的vo
		if (bvo5Dlist.size() > 0) {
			retordvos[2] = new SaleOrderVO();
			((SaleOrderVO) retordvos[2]).setClientLink(((SaleOrderVO) ordvo).getClientLink());
			retordvos[2].setParentVO(ordhvo);
			retordvos[2].setChildrenVO((SaleorderBVO[]) bvo5Dlist.toArray(new SaleorderBVO[bvo5Dlist.size()]));
			if (SO58.equals("按净需求"))
				retordvos[2] = getNumberBySO58(retordvos[2], getLabors((SaleorderBVO[]) retordvos[2].getChildrenVO()),
						iBD501.intValue());
			if (retordvos[2] != null)
				hmreturn.put("DBDD", retordvos[2]);
		}

		return hmreturn;
	}

	/**
	 * 根据委外类型拆单
	 * 
	 * @param bvos
	 * @return
	 */
	private SaleOrderVO[] splitBodyItemsByOuttype(SaleOrderVO vo) throws SQLException {

		HashMap hsinvprop = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValue("bd_produce,so_saleorder_b", "bd_produce.outtype", "so_saleorder_b.corder_bid",
						nc.vo.so.so016.SoVoTools.getSArrayVOsValue(vo.getChildrenVO(), "corder_bid"),
						" bd_produce.pk_invmandoc=so_saleorder_b.cinventoryid and bd_produce.pk_calbody=so_saleorder_b.cadvisecalbodyid ");
		if (hsinvprop == null)
			hsinvprop = new HashMap();
		SaleorderBVO[] ordbvos = vo.getBodyVOs();
		Vector vt = null;
		Hashtable ht = new Hashtable();
		Object outtype = null;
		for (int i = 0; i < ordbvos.length; i++) {
			outtype = hsinvprop.get(ordbvos[i].getCorder_bid());
			if (outtype == null)
				outtype = "NullItems";
			if (ht.containsKey(outtype)) {
				vt = (Vector) ht.get(outtype);
			} else {
				vt = new Vector();
				ht.put(outtype, vt);
			}
			vt.add(ordbvos[i].clone());
		}
		SaleOrderVO[] returnvos = new SaleOrderVO[ht.size()];
		Object[] sKeys = new Object[ht.size()];
		ht.keySet().toArray(sKeys);
		SaleorderBVO[] ordtmp = null;
		for (int i = 0; i < ht.size(); i++) {
			vt = (Vector) ht.get(sKeys[i]);
			//
			returnvos[i] = new SaleOrderVO();
			returnvos[i].setClientLink(vo.getClientLink());
			ordtmp = new SaleorderBVO[vt.size()];
			vt.copyInto(ordtmp);
			returnvos[i].setParentVO((CircularlyAccessibleValueObject) (vo.getParentVO().clone()));
			returnvos[i].setChildrenVO(ordtmp);
		}
		return returnvos;
	}

	/**
	 * 订单审批推请购单,调拨申请,调拨订单时调用,从销售订单分单得到生成请购单,调拨申请,调拨订单的VO。 创建日期：(2004-4-20
	 * 20:14:41)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject[]
	 * @param ordvo
	 *            nc.vo.so.so001.SaleOrderVO
	 */
	public nc.vo.pub.AggregatedValueObject[] splitOrdVOFor20_5D_5A(nc.vo.pub.AggregatedValueObject ordvo)
			throws javax.naming.NamingException, javax.ejb.CreateException, java.sql.SQLException, BusinessException {

		nc.vo.pub.AggregatedValueObject[] retordvos = new nc.vo.pub.AggregatedValueObject[3];
		if (ordvo == null)
			return retordvos;

		// 取so35参数
		nc.bs.pub.para.SysInitDMO sysdmo = new nc.bs.pub.para.SysInitDMO();
		String so35 = sysdmo.getParaString("0001", "SO35");
		boolean isPush5D = false;
		if ("调拨订单".equals(so35))/*-=notranslate=-*/
			isPush5D = true;

		// 取存货生产档案中物料类型:DB内部交易 MR采购 PR制造件
		HashMap hsinvprop = null;
		SaleorderHVO ordhvo = (SaleorderHVO) ordvo.getParentVO();
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) ordvo.getChildrenVO();
		if (ordhvo == null || ordbvos == null || ordbvos.length <= 0)
			return retordvos;
		String pk_corp = ordhvo.getPk_corp();
		ArrayList bvo20list = new ArrayList();
		ArrayList bvo5Alist = new ArrayList();
		ArrayList bvo5Dlist = new ArrayList();
		String consigncorpid = null, cmatertype = null;
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			consigncorpid = ordbvos[i].getCconsigncorpid();
			// 1) 如果销售订单行的发货公司为销售订单公司，则表示销售订单需要形成请购单或调拨申请：
			if (consigncorpid == null || consigncorpid.trim().length() <= 0 || consigncorpid.equals(pk_corp)) {
				if (consigncorpid != null && consigncorpid.trim().length() <= 0)
					ordbvos[i].setCconsigncorpid(null);
				if (hsinvprop == null) {
					// 取存货生产档案中物料类型:DB内部交易 MR采购 PR制造件
					hsinvprop = nc.impl.scm.so.so016.SOToolsDMO
							.getAnyValue("bd_produce,so_saleorder_b", "bd_produce.matertype",
									"so_saleorder_b.corder_bid", nc.vo.so.so016.SoVoTools.getSArrayVOsValue(ordvo
											.getChildrenVO(), "corder_bid"),
									" bd_produce.pk_invmandoc=so_saleorder_b.cinventoryid and bd_produce.pk_calbody=so_saleorder_b.cadvisecalbodyid ");
					if (hsinvprop == null)
						hsinvprop = new HashMap();
				}
				// 去掉数量为负的存货
				if (ordbvos[i].getNnumber() == null || ordbvos[i].getNnumber().doubleValue() <= 0)
					continue;
				cmatertype = (String) hsinvprop.get(ordbvos[i].getCorder_bid());
				// a) 如果存货生产档案中物料类型为“采购件”，则销售订单行形成销售公司的采购申请；
				if ("MR".equals(cmatertype)) {
					bvo20list.add(ordbvos[i]);
					// b) 如果存货生产档案中物料类型为“内部调拨”，则销售订单行形成销售公司的调拨申请
				} else if ("DB".equals(cmatertype)) {
					bvo5Alist.add(ordbvos[i]);
				}

				// 2) 如果销售订单行的发货公司与销售订单公司不相同，则表示销售订单需要形成调拨申请或调拨订单
			} else {
				// a) 如果跨公司销售订单与内部交易接口单据参数为“调拨申请”，则销售订单行形成销售公司的调拨申请
				if (isPush5D) {
					bvo5Dlist.add(ordbvos[i]);
				} else {
					// b) 如果跨公司销售订单与内部交易接口单据参数为“调拨订单”，则销售订单行形成发货公司的调拨订单
					// 去掉数量为负的存货
					if (ordbvos[i].getNnumber() == null || ordbvos[i].getNnumber().doubleValue() <= 0)
						continue;
					bvo5Alist.add(ordbvos[i]);
				}
			}
		}

		// 构造生成采购申请20的vo
		if (bvo20list.size() > 0) {
			retordvos[0] = new SaleOrderVO();
			((SaleOrderVO) retordvos[0]).setClientLink(((SaleOrderVO) ordvo).getClientLink());
			retordvos[0].setParentVO(ordhvo);
			retordvos[0].setChildrenVO((SaleorderBVO[]) bvo20list.toArray(new SaleorderBVO[bvo20list.size()]));
		}
		// 构造生成调拨申请5A的vo
		if (bvo5Alist.size() > 0) {
			retordvos[1] = new SaleOrderVO();
			((SaleOrderVO) retordvos[1]).setClientLink(((SaleOrderVO) ordvo).getClientLink());
			retordvos[1].setParentVO(ordhvo);
			retordvos[1].setChildrenVO((SaleorderBVO[]) bvo5Alist.toArray(new SaleorderBVO[bvo5Alist.size()]));
		}
		// 构造生成调拨订单5D的vo
		if (bvo5Dlist.size() > 0) {
			retordvos[2] = new SaleOrderVO();
			((SaleOrderVO) retordvos[2]).setClientLink(((SaleOrderVO) ordvo).getClientLink());
			retordvos[2].setParentVO(ordhvo);
			retordvos[2].setChildrenVO((SaleorderBVO[]) bvo5Dlist.toArray(new SaleorderBVO[bvo5Dlist.size()]));
		}

		return retordvos;
	}

	public SaleorderBVO[] queryAllBodyDataByWhere(String swhere, String orderby) throws Exception {

		// 设置子表其他增加的字段
		SOField[] addfields = SaleorderBVO.getAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getTablename());
					addfieldsql.append(".");
					addfieldsql.append(addfields[i].getDatabasename());
				}
			}
		}

		String sql = "select so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype, "
				+ "so_saleorder_b.csourcebillid, so_saleorder_b.csourcebillbodyid, so_saleorder_b.cinventoryid, "
				+ "so_saleorder_b.cunitid, so_saleorder_b.cpackunitid, so_saleorder_b.nnumber, "
				// 10
				+ "so_saleorder_b.npacknumber, so_saleorder_b.cbodywarehouseid, so_saleorder_b.dconsigndate, so_saleorder_b.ddeliverdate, "
				+ "so_saleorder_b.blargessflag, so_saleexecute.ntotlbalcostnum , bd_invbasdoc.discountflag, so_saleorder_b.veditreason, "
				+ "so_saleorder_b.ccurrencytypeid, so_saleorder_b.nitemdiscountrate, "
				// 20
				+ "so_saleorder_b.ndiscountrate, so_saleorder_b.nexchangeotobrate, so_saleexecute.nrushnum, so_saleorder_b.ntaxrate, "
				+ "so_saleorder_b.noriginalcurprice, so_saleorder_b.noriginalcurtaxprice, so_saleorder_b.noriginalcurnetprice, "
				+ "so_saleorder_b.noriginalcurtaxnetprice, so_saleorder_b.noriginalcurtaxmny, so_saleorder_b.noriginalcurmny, "
				// 30
				+ "so_saleorder_b.noriginalcursummny, so_saleorder_b.noriginalcurdiscountmny, so_saleorder_b.nprice, "
				+ "so_saleorder_b.ntaxprice, so_saleorder_b.nnetprice, so_saleorder_b.ntaxnetprice, so_saleorder_b.ntaxmny, "
				+ "so_saleorder_b.nmny, so_saleorder_b.nsummny, so_saleorder_b.ndiscountmny, "
				// 40
				+ "so_saleorder_b.coperatorid, so_saleorder_b.frowstatus, so_saleorder_b.frownote, so_saleorder_b.cinvbasdocid, so_saleorder_b.cbatchid, "
				+ "so_saleorder_b.fbatchstatus, so_saleorder_b.cbomorderid, so_saleorder_b.cfreezeid, so_saleorder_b.ct_manageid, so_saleorder_b.ts, "
				// 50
				+ "so_saleorder_b.cadvisecalbodyid, so_saleorder_b.boosflag, so_saleorder_b.bsupplyflag, "
				+ "so_saleorder_b.creceiptareaid, so_saleorder_b.vreceiveaddress, so_saleorder_b.creceiptcorpid, "
				+ "so_saleorder_b.crowno, "
				// exe
				+ "so_saleexecute.creceipttype, so_saleexecute.ntotalpaymny, so_saleexecute.ntotalreceivenumber, "
				// 60
				+ "so_saleexecute.ntotalinvoicenumber, so_saleexecute.ntotalinvoicemny, so_saleexecute.ntotalinventorynumber, "
				+ "so_saleexecute.ntotalbalancenumber, so_saleexecute.ntotalsignnumber, so_saleexecute.ntotalcostmny, "
				+ "so_saleexecute.bifinvoicefinish, so_saleexecute.bifreceivefinish, so_saleexecute.bifinventoryfinish, so_saleexecute.bifpayfinish, "
				// 70
				+ "so_saleexecute.ts, so_saleexecute.cprojectid, so_saleexecute.cprojectphaseid, so_saleexecute.cprojectid3, so_saleexecute.vfree1, "
				+ "so_saleexecute.vfree2, so_saleexecute.vfree3, so_saleexecute.vfree4, so_saleexecute.vfree5, so_saleexecute.vdef1, "
				// 80
				+ "so_saleexecute.vdef2, so_saleexecute.vdef3, so_saleexecute.vdef4, so_saleexecute.vdef5, so_saleexecute.vdef6, "
				+ "so_saleexecute.ntotalshouldoutnum, so_saleexecute.ntotalreturnnumber, so_saleexecute.bsquareendflag "

				// 90
				+ addfieldsql.toString()
				+ " from so_saleorder_b, so_saleexecute, so_sale, bd_invbasdoc "
				+ " where so_saleorder_b.csaleid = so_saleexecute.csaleid "
				+ " AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid "
				+ " AND so_saleexecute.creceipttype = '30' "
				+ " and so_saleorder_b.csaleid=so_sale.csaleid "
				+ " and bd_invbasdoc.pk_invbasdoc = so_saleorder_b.cinvbasdocid "
				+ " and so_sale.dr = 0 and so_saleorder_b.dr = 0 and so_saleexecute.dr = 0 ";

		if (swhere != null) {
			swhere = swhere.trim();
			if (swhere.length() > 0) {
				if (swhere.indexOf("and") == 0 || swhere.indexOf("AND") == 0)
					sql += swhere;
				else
					sql += " AND " + swhere + " ";
			}
		}

		if (orderby != null && orderby.trim().length() > 0) {
			if (orderby.indexOf("order by") >= 0 || orderby.indexOf("ORDER BY") >= 0)
				sql += orderby;
			else
				sql += " order by " + orderby;
		}

		SaleorderBVO[] saleItems = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		con = getConnection();
		try {
			stmt = con.prepareStatement(sql);
			// stmt.setString(1, key);
			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {
				SaleorderBVO saleItem = new SaleorderBVO();
				//
				String corder_bid = rs.getString(1);
				saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());
				//
				String csaleid = rs.getString(2);
				saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());
				//
				String ccorpid = rs.getString(3);
				saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());
				//
				String creceipttype = rs.getString(4);
				saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
				//
				String csourcebillid = rs.getString(5);
				saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());
				//
				String csourcebillbodyid = rs.getString(6);
				saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());
				//
				String cinventoryid = rs.getString(7);
				saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());
				//
				String cunitid = rs.getString(8);
				saleItem.setCunitid(cunitid == null ? null : cunitid.trim());
				//
				String cpackunitid = rs.getString(9);
				saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());
				//
				BigDecimal nnumber = (BigDecimal) rs.getObject(10);
				saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));
				//
				BigDecimal npacknumber = (BigDecimal) rs.getObject(11);
				saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));
				//
				String cbodywarehouseid = rs.getString(12);
				saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());
				//
				String dconsigndate = rs.getString(13);
				saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));
				//
				String ddeliverdate = rs.getString(14);
				saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));
				//
				String blargessflag = rs.getString(15);
				saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));
				//
				// ntotlbalcostnum
				BigDecimal ntotlbalcostnum = (BigDecimal) rs.getObject(16);
				saleItem.setNtotlbalcostnum(ntotlbalcostnum == null ? new UFDouble(0) : new UFDouble(ntotlbalcostnum));
				//
				// discountflag
				boolean discountflag = false;

				Object oTemp = rs.getObject(17);
				if (oTemp != null) {
					if (oTemp.toString() == "Y")
						discountflag = true;
				}

				if (!discountflag)
					saleItem.setDiscountflag(UFBoolean.FALSE);
				else
					saleItem.setDiscountflag(UFBoolean.TRUE);
				//
				String veditreason = rs.getString(18);
				saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());
				//
				String ccurrencytypeid = rs.getString(19);
				saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());
				//
				BigDecimal nitemdiscountrate = (BigDecimal) rs.getObject(20);
				saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));
				//
				BigDecimal ndiscountrate = (BigDecimal) rs.getObject(21);
				saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));
				//
				BigDecimal nexchangeotobrate = (BigDecimal) rs.getObject(22);
				saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

				// nrushnum
				BigDecimal nrushnum = (BigDecimal) rs.getObject(23);
				saleItem.setNrushnum(nrushnum == null ? new UFDouble(0) : new UFDouble(nrushnum));

				//
				BigDecimal ntaxrate = (BigDecimal) rs.getObject(24);
				saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));
				//
				BigDecimal noriginalcurprice = (BigDecimal) rs.getObject(25);
				saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));
				//
				BigDecimal noriginalcurtaxprice = (BigDecimal) rs.getObject(26);
				saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(
						noriginalcurtaxprice));
				//
				BigDecimal noriginalcurnetprice = (BigDecimal) rs.getObject(27);
				saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(
						noriginalcurnetprice));
				//
				BigDecimal noriginalcurtaxnetprice = (BigDecimal) rs.getObject(28);
				saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(
						noriginalcurtaxnetprice));
				//
				BigDecimal noriginalcurtaxmny = (BigDecimal) rs.getObject(29);
				saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));
				//
				BigDecimal noriginalcurmny = (BigDecimal) rs.getObject(30);
				saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));
				//
				BigDecimal noriginalcursummny = (BigDecimal) rs.getObject(31);
				saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));
				//
				BigDecimal noriginalcurdiscountmny = (BigDecimal) rs.getObject(32);
				saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(
						noriginalcurdiscountmny));
				//
				BigDecimal nprice = (BigDecimal) rs.getObject(33);
				saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));
				//
				BigDecimal ntaxprice = (BigDecimal) rs.getObject(34);
				saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));
				//
				BigDecimal nnetprice = (BigDecimal) rs.getObject(35);
				saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));
				//
				BigDecimal ntaxnetprice = (BigDecimal) rs.getObject(36);
				saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));
				//
				BigDecimal ntaxmny = (BigDecimal) rs.getObject(37);
				saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));
				//
				BigDecimal nmny = (BigDecimal) rs.getObject(38);
				saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));
				//
				BigDecimal nsummny = (BigDecimal) rs.getObject(39);
				saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));
				//
				BigDecimal ndiscountmny = (BigDecimal) rs.getObject(40);
				saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));
				//
				String coperatorid = rs.getString(41);
				saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());
				//
				Integer frowstatus = (Integer) rs.getObject(42);
				saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);
				//
				String frownote = rs.getString(43);
				saleItem.setFrownote(frownote == null ? null : frownote.trim());
				// //
				String cinvbasdocid = rs.getString(44);
				saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());
				//
				String cbatchid = rs.getString(45);
				saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());
				// fbatchstatus :
				Integer fbatchstatus = (Integer) rs.getObject(46);
				saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);
				//
				String cbomorderid = rs.getString(47);
				saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());
				//
				String cfreezeid = rs.getString(48);
				saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());
				//
				String ct_manageid = rs.getString(49);
				saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());
				// ts
				String ts = rs.getString(50);
				saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));
				// cadvisecalbodyid
				String cadvisecalbodyid = rs.getString(51);
				saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());
				// boosflag
				String boosflag = rs.getString(52);
				saleItem.setBoosflag(boosflag == null ? null : new UFBoolean(boosflag.trim()));
				// bsupplyflag
				String bsupplyflag = rs.getString(53);
				saleItem.setBsupplyflag(bsupplyflag == null ? null : new UFBoolean(bsupplyflag.trim()));
				// creceiptareaid
				String creceiptareaid = rs.getString(54);
				saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());
				// vreceiveaddress
				String vreceiveaddress = rs.getString(55);
				saleItem.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());
				// creceiptcorpid
				String creceiptcorpid = rs.getString(56);
				saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());
				// crowno
				String crowno = rs.getString(57);
				saleItem.setCrowno(crowno == null ? null : crowno.trim());

				//
				// 执行情况表
				// creceipttype :
				creceipttype = rs.getString(58);
				// saleItem.setCreceipttype(creceipttype == null ? null :
				// creceipttype.trim());
				// ntotalpaymny :
				BigDecimal ntotalpaymny = (BigDecimal) rs.getObject(59);
				saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));
				// ntotalreceivenumber :
				BigDecimal ntotalreceivenumber = (BigDecimal) rs.getObject(60);
				saleItem.setNtotalreceivenumber(ntotalreceivenumber == null ? null : new UFDouble(ntotalreceivenumber));
				// ntotalinvoicenumber :
				BigDecimal ntotalinvoicenumber = (BigDecimal) rs.getObject(61);
				saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));
				// ntotalinvoicemny :
				BigDecimal ntotalinvoicemny = (BigDecimal) rs.getObject(62);
				saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));
				// ntotalinventorynumber :
				BigDecimal ntotalinventorynumber = (BigDecimal) rs.getObject(63);
				saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(
						ntotalinventorynumber));
				// ntotalbalancenumber :
				BigDecimal ntotalbalancenumber = (BigDecimal) rs.getObject(64);
				saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));
				// ntotalsignnumber :
				BigDecimal ntotalsignnumber = (BigDecimal) rs.getObject(65);
				saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));
				// ntotalcostmny :
				BigDecimal ntotalcostmny = (BigDecimal) rs.getObject(66);
				saleItem.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));
				// bifinvoicefinish :
				String bifinvoicefinish = rs.getString(67);
				saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));
				// bifreceivefinish :
				String bifreceivefinish = rs.getString(68);
				saleItem.setBifreceivefinish(bifreceivefinish == null ? null : new UFBoolean(bifreceivefinish.trim()));
				// bifinventoryfinish :
				String bifinventoryfinish = rs.getString(69);
				saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish
						.trim()));
				// bifpayfinish :
				String bifpayfinish = rs.getString(70);
				saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

				// exets
				String exets = rs.getString(71);
				saleItem.setExets(exets == null ? null : new UFDateTime(exets.trim()));

				// cprojectid :
				String cprojectid = rs.getString(72);
				saleItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());
				// cprojectphaseid :
				String cprojectphaseid = rs.getString(73);
				saleItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());
				// cprojectid3 :
				String cprojectid3 = rs.getString(74);
				saleItem.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());
				// vfree1 :
				String vfree1 = rs.getString(75);
				saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());
				// vfree2 :
				String vfree2 = rs.getString(76);
				saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());
				// vfree3 :
				String vfree3 = rs.getString(77);
				saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());
				// vfree4 :
				String vfree4 = rs.getString(78);
				saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());
				// vfree5 :
				String vfree5 = rs.getString(79);
				saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());
				// vdef1 :
				String vdef1 = rs.getString(80);
				saleItem.setVdef1(vdef1 == null ? null : vdef1.trim());
				// vdef2 :
				String vdef2 = rs.getString(81);
				saleItem.setVdef2(vdef2 == null ? null : vdef2.trim());
				// vdef3 :
				String vdef3 = rs.getString(82);
				saleItem.setVdef3(vdef3 == null ? null : vdef3.trim());
				// vdef4 :
				String vdef4 = rs.getString(83);
				saleItem.setVdef4(vdef4 == null ? null : vdef4.trim());
				// vdef5 :
				String vdef5 = rs.getString(84);
				saleItem.setVdef5(vdef5 == null ? null : vdef5.trim());
				// vdef6 :
				String vdef6 = rs.getString(85);
				saleItem.setVdef6(vdef6 == null ? null : vdef6.trim());

				// 获取v30后加入的其他字段值
				BigDecimal ntotalshouldoutnum = (BigDecimal) rs.getObject(86);
				saleItem.setNtotalshouldoutnum(ntotalshouldoutnum == null ? new UFDouble(0) : new UFDouble(
						ntotalshouldoutnum));

				// ntotalreturnnumber
				BigDecimal ntotalreturnnumber = (BigDecimal) rs.getObject(87);
				saleItem.setNtotalreturnnumber(ntotalreturnnumber == null ? new UFDouble(0) : new UFDouble(
						ntotalreturnnumber));
				// add by fengjb V55 结算关闭
				String bsquareendflag = rs.getString(88);
				saleItem.setBsquareendflag(bsquareendflag == null ? null : new UFBoolean(bsquareendflag));

				getOrdBAndExeValueFromResultSet(rs, 89, saleItem);

				v.addElement(saleItem);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		saleItems = new SaleorderBVO[v.size()];
		if (v.size() > 0) {
			v.copyInto(saleItems);
			//v5.5将累计安排数量，和可安排数量计算出来
			UFDouble temp = null;
			for (SaleorderBVO bvo : saleItems){
				temp = bvo.getNarrangemonum().add(bvo.getNarrangetoornum())
				         .add(bvo.getNorrangetoapplynum()).add(bvo.getNarrangepoapplynum())
				         .add(bvo.getNarrangescornum()).add(bvo.getNarrangepoordernum());
				bvo.setNarrangetotolenum(temp);
				bvo.setNarrangenum((bvo.getNnumber() == null ? UFDouble.ZERO_DBL : bvo.getNnumber()).sub(temp));
			}
		}
		
		initFreeItem(saleItems);
		
		return saleItems;
	}
	/**
	 * <表体行上游单据行id,与该id相关联的所有销售订单表体行>
	 * 
	 * @param swhere
	 * @param orderby
	 * @return
	 * @throws SQLException
	 */
	public Hashtable queryAllBodyDataHashByWhere(String swhere, String orderby) throws SQLException {

		Hashtable<String, ArrayList<SaleorderBVO>> ht = new Hashtable();

		// 设置子表其他增加的字段
		SOField[] addfields = SaleorderBVO.getAddFields();
		StringBuffer addfieldsql = new StringBuffer("");
		if (addfields != null) {
			for (int i = 0, loop = addfields.length; i < loop; i++) {
				if (addfields[i].getDatabasename() != null) {
					addfieldsql.append(",");
					addfieldsql.append(addfields[i].getTablename());
					addfieldsql.append(".");
					addfieldsql.append(addfields[i].getDatabasename());
				}
			}
		}

		String sql = "select so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, so_saleorder_b.creceipttype, "
				+ "so_saleorder_b.csourcebillid, so_saleorder_b.csourcebillbodyid, so_saleorder_b.cinventoryid, "
				+ "so_saleorder_b.cunitid, so_saleorder_b.cpackunitid, so_saleorder_b.nnumber, so_saleorder_b.npacknumber, "
				+ "so_saleorder_b.cbodywarehouseid, so_saleorder_b.dconsigndate, so_saleorder_b.ddeliverdate, "
				+ "so_saleorder_b.blargessflag, bd_invbasdoc.discountflag , so_saleexecute.nrushnum, so_saleorder_b.veditreason, "
				+ "so_saleorder_b.ccurrencytypeid, so_saleorder_b.nitemdiscountrate, so_saleorder_b.ndiscountrate, "
				+ "so_saleorder_b.nexchangeotobrate, so_saleexecute.ntotlbalcostnum, so_saleorder_b.ntaxrate, "
				+ "so_saleorder_b.noriginalcurprice, so_saleorder_b.noriginalcurtaxprice, so_saleorder_b.noriginalcurnetprice, "
				+ "so_saleorder_b.noriginalcurtaxnetprice, so_saleorder_b.noriginalcurtaxmny, so_saleorder_b.noriginalcurmny, "
				+ "so_saleorder_b.noriginalcursummny, so_saleorder_b.noriginalcurdiscountmny, so_saleorder_b.nprice, "
				+ "so_saleorder_b.ntaxprice, so_saleorder_b.nnetprice, so_saleorder_b.ntaxnetprice, so_saleorder_b.ntaxmny, "
				+ "so_saleorder_b.nmny, so_saleorder_b.nsummny, so_saleorder_b.ndiscountmny, so_saleorder_b.coperatorid, "
				+ "so_saleorder_b.frowstatus, so_saleorder_b.frownote,so_saleorder_b.cinvbasdocid,so_saleorder_b.cbatchid, "
				+ "so_saleorder_b.fbatchstatus,so_saleorder_b.cbomorderid,so_saleorder_b.cfreezeid,so_saleorder_b.ct_manageid, "
				+ "so_saleorder_b.ts, so_saleorder_b.cadvisecalbodyid, so_saleorder_b.boosflag, so_saleorder_b.bsupplyflag, "
				+ "so_saleorder_b.creceiptareaid, so_saleorder_b.vreceiveaddress, so_saleorder_b.creceiptcorpid, "
				+ "so_saleorder_b.crowno, "
				// exe
				+ "so_saleexecute.creceipttype, so_saleexecute.ntotalpaymny, so_saleexecute.ntotalreceivenumber, "
				+ "so_saleexecute.ntotalinvoicenumber, so_saleexecute.ntotalinvoicemny, so_saleexecute.ntotalinventorynumber, "
				+ "so_saleexecute.ntotalbalancenumber, so_saleexecute.ntotalsignnumber, so_saleexecute.ntotalcostmny, "
				+ "so_saleexecute.bifinvoicefinish, so_saleexecute.bifreceivefinish, so_saleexecute.bifinventoryfinish, "
				+ "so_saleexecute.bifpayfinish, so_saleexecute.ts, so_saleexecute.cprojectid, "
				+ "so_saleexecute.cprojectphaseid, so_saleexecute.cprojectid3, so_saleexecute.vfree1, so_saleexecute.vfree2, "
				+ "so_saleexecute.vfree3, so_saleexecute.vfree4, so_saleexecute.vfree5, so_saleexecute.vdef1, so_saleexecute.vdef2, "
				+ "so_saleexecute.vdef3, so_saleexecute.vdef4, so_saleexecute.vdef5, so_saleexecute.vdef6, "
				+ "so_saleexecute.ntotalshouldoutnum,so_saleexecute.ntotalreturnnumber "
				+ addfieldsql.toString()
				+ " from so_saleorder_b, so_saleexecute, so_sale, bd_invbasdoc "
				+ " where so_saleorder_b.csaleid = so_saleexecute.csaleid "
				+ " AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid "
				+ " AND so_saleexecute.creceipttype = '30' "
				+ " and so_saleorder_b.csaleid=so_sale.csaleid "
				+ " and bd_invbasdoc.pk_invbasdoc = so_saleorder_b.cinvbasdocid "
				+ " and so_sale.dr = 0 and so_saleorder_b.dr = 0 and so_saleexecute.dr = 0 ";

		if (swhere != null) {
			swhere = swhere.trim();
			if (swhere.length() > 0) {
				if (swhere.indexOf("and") == 0 || swhere.indexOf("AND") == 0)
					sql += swhere;
				else
					sql += " AND " + swhere + " ";
			}
		}

		if (orderby != null && orderby.trim().length() > 0) {
			if (orderby.indexOf("order by") >= 0 || orderby.indexOf("ORDER BY") >= 0)
				sql += orderby;
			else
				sql += " order by " + orderby;
		}

		SaleorderBVO[] saleItems = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		con = getConnection();
		try {
			stmt = con.prepareStatement(sql);
			// stmt.setString(1, key);
			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {
				SaleorderBVO saleItem = new SaleorderBVO();
				//
				String corder_bid = rs.getString(1);
				saleItem.setCorder_bid(corder_bid == null ? null : corder_bid.trim());
				//
				String csaleid = rs.getString(2);
				saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());
				//
				String ccorpid = rs.getString(3);
				saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());
				//
				String creceipttype = rs.getString(4);
				saleItem.setCreceipttype(creceipttype == null ? null : creceipttype.trim());
				//
				String csourcebillid = rs.getString(5);
				saleItem.setCsourcebillid(csourcebillid == null ? null : csourcebillid.trim());
				//
				String csourcebillbodyid = rs.getString(6);
				saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null : csourcebillbodyid.trim());
				//
				String cinventoryid = rs.getString(7);
				saleItem.setCinventoryid(cinventoryid == null ? null : cinventoryid.trim());
				//
				String cunitid = rs.getString(8);
				saleItem.setCunitid(cunitid == null ? null : cunitid.trim());
				//
				String cpackunitid = rs.getString(9);
				saleItem.setCpackunitid(cpackunitid == null ? null : cpackunitid.trim());
				//
				BigDecimal nnumber = (BigDecimal) rs.getObject(10);
				saleItem.setNnumber(nnumber == null ? null : new UFDouble(nnumber));
				//
				BigDecimal npacknumber = (BigDecimal) rs.getObject(11);
				saleItem.setNpacknumber(npacknumber == null ? null : new UFDouble(npacknumber));
				//
				String cbodywarehouseid = rs.getString(12);
				saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null : cbodywarehouseid.trim());
				//
				String dconsigndate = rs.getString(13);
				saleItem.setDconsigndate(dconsigndate == null ? null : new UFDate(dconsigndate.trim()));
				//
				String ddeliverdate = rs.getString(14);
				saleItem.setDdeliverdate(ddeliverdate == null ? null : new UFDate(ddeliverdate.trim()));
				//
				String blargessflag = rs.getString(15);
				saleItem.setBlargessflag(blargessflag == null ? null : new UFBoolean(blargessflag.trim()));

				// discountflag
				boolean discountflag = false;

				Object oTemp = rs.getObject(16);
				if (oTemp != null) {
					if (oTemp.toString() == "Y")
						discountflag = true;
				}

				if (!discountflag)
					saleItem.setDiscountflag(UFBoolean.FALSE);
				else
					saleItem.setDiscountflag(UFBoolean.TRUE);
				// nrushnum
				BigDecimal nrushnum = (BigDecimal) rs.getObject(17);
				saleItem.setNrushnum(nrushnum == null ? new UFDouble(0) : new UFDouble(nrushnum));
				//
				String veditreason = rs.getString(18);
				saleItem.setVeditreason(veditreason == null ? null : veditreason.trim());
				//
				String ccurrencytypeid = rs.getString(19);
				saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null : ccurrencytypeid.trim());
				//
				BigDecimal nitemdiscountrate = (BigDecimal) rs.getObject(20);
				saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null : new UFDouble(nitemdiscountrate));
				//
				BigDecimal ndiscountrate = (BigDecimal) rs.getObject(21);
				saleItem.setNdiscountrate(ndiscountrate == null ? null : new UFDouble(ndiscountrate));
				//
				BigDecimal nexchangeotobrate = (BigDecimal) rs.getObject(22);
				saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null : new UFDouble(nexchangeotobrate));

				// ntotlbalcostnum
				BigDecimal ntotlbalcostnum = (BigDecimal) rs.getObject(23);
				saleItem.setNtotlbalcostnum(ntotlbalcostnum == null ? new UFDouble(0) : new UFDouble(ntotlbalcostnum));
				//
				BigDecimal ntaxrate = (BigDecimal) rs.getObject(24);
				saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(ntaxrate));
				//
				BigDecimal noriginalcurprice = (BigDecimal) rs.getObject(25);
				saleItem.setNoriginalcurprice(noriginalcurprice == null ? null : new UFDouble(noriginalcurprice));
				//
				BigDecimal noriginalcurtaxprice = (BigDecimal) rs.getObject(26);
				saleItem.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null : new UFDouble(
						noriginalcurtaxprice));
				//
				BigDecimal noriginalcurnetprice = (BigDecimal) rs.getObject(27);
				saleItem.setNoriginalcurnetprice(noriginalcurnetprice == null ? null : new UFDouble(
						noriginalcurnetprice));
				//
				BigDecimal noriginalcurtaxnetprice = (BigDecimal) rs.getObject(28);
				saleItem.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null : new UFDouble(
						noriginalcurtaxnetprice));
				//
				BigDecimal noriginalcurtaxmny = (BigDecimal) rs.getObject(29);
				saleItem.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null : new UFDouble(noriginalcurtaxmny));
				//
				BigDecimal noriginalcurmny = (BigDecimal) rs.getObject(30);
				saleItem.setNoriginalcurmny(noriginalcurmny == null ? null : new UFDouble(noriginalcurmny));
				//
				BigDecimal noriginalcursummny = (BigDecimal) rs.getObject(31);
				saleItem.setNoriginalcursummny(noriginalcursummny == null ? null : new UFDouble(noriginalcursummny));
				//
				BigDecimal noriginalcurdiscountmny = (BigDecimal) rs.getObject(32);
				saleItem.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null : new UFDouble(
						noriginalcurdiscountmny));
				//
				BigDecimal nprice = (BigDecimal) rs.getObject(33);
				saleItem.setNprice(nprice == null ? null : new UFDouble(nprice));
				//
				BigDecimal ntaxprice = (BigDecimal) rs.getObject(34);
				saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(ntaxprice));
				//
				BigDecimal nnetprice = (BigDecimal) rs.getObject(35);
				saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(nnetprice));
				//
				BigDecimal ntaxnetprice = (BigDecimal) rs.getObject(36);
				saleItem.setNtaxnetprice(ntaxnetprice == null ? null : new UFDouble(ntaxnetprice));
				//
				BigDecimal ntaxmny = (BigDecimal) rs.getObject(37);
				saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(ntaxmny));
				//
				BigDecimal nmny = (BigDecimal) rs.getObject(38);
				saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));
				//
				BigDecimal nsummny = (BigDecimal) rs.getObject(39);
				saleItem.setNsummny(nsummny == null ? null : new UFDouble(nsummny));
				//
				BigDecimal ndiscountmny = (BigDecimal) rs.getObject(40);
				saleItem.setNdiscountmny(ndiscountmny == null ? null : new UFDouble(ndiscountmny));
				//
				String coperatorid = rs.getString(41);
				saleItem.setCoperatorid(coperatorid == null ? null : coperatorid.trim());
				//
				Integer frowstatus = (Integer) rs.getObject(42);
				saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);
				//
				String frownote = rs.getString(43);
				saleItem.setFrownote(frownote == null ? null : frownote.trim());
				// //
				String cinvbasdocid = rs.getString(44);
				saleItem.setCinvbasdocid(cinvbasdocid == null ? null : cinvbasdocid.trim());
				//
				String cbatchid = rs.getString(45);
				saleItem.setCbatchid(cbatchid == null ? null : cbatchid.trim());
				// fbatchstatus :
				Integer fbatchstatus = (Integer) rs.getObject(46);
				saleItem.setFbatchstatus(fbatchstatus == null ? null : fbatchstatus);
				//
				String cbomorderid = rs.getString(47);
				saleItem.setCbomorderid(cbomorderid == null ? null : cbomorderid.trim());
				//
				String cfreezeid = rs.getString(48);
				saleItem.setCfreezeid(cfreezeid == null ? null : cfreezeid.trim());
				//
				String ct_manageid = rs.getString(49);
				saleItem.setCt_manageid(ct_manageid == null ? null : ct_manageid.trim());
				// ts
				String ts = rs.getString(50);
				saleItem.setTs(ts == null ? null : new UFDateTime(ts.trim()));
				// cadvisecalbodyid
				String cadvisecalbodyid = rs.getString(51);
				saleItem.setCadvisecalbodyid(cadvisecalbodyid == null ? null : cadvisecalbodyid.trim());
				// boosflag
				String boosflag = rs.getString(52);
				saleItem.setBoosflag(boosflag == null ? null : new UFBoolean(boosflag.trim()));
				// bsupplyflag
				String bsupplyflag = rs.getString(53);
				saleItem.setBsupplyflag(bsupplyflag == null ? null : new UFBoolean(bsupplyflag.trim()));
				// creceiptareaid
				String creceiptareaid = rs.getString(54);
				saleItem.setCreceiptareaid(creceiptareaid == null ? null : creceiptareaid.trim());
				// vreceiveaddress
				String vreceiveaddress = rs.getString(55);
				saleItem.setVreceiveaddress(vreceiveaddress == null ? null : vreceiveaddress.trim());
				// creceiptcorpid
				String creceiptcorpid = rs.getString(56);
				saleItem.setCreceiptcorpid(creceiptcorpid == null ? null : creceiptcorpid.trim());
				// crowno
				String crowno = rs.getString(57);
				saleItem.setCrowno(crowno == null ? null : crowno.trim());

				//
				// 执行情况表
				// creceipttype :
				creceipttype = rs.getString(58);
				// saleItem.setCreceipttype(creceipttype == null ? null :
				// creceipttype.trim());
				// ntotalpaymny :
				BigDecimal ntotalpaymny = (BigDecimal) rs.getObject(59);
				saleItem.setNtotalpaymny(ntotalpaymny == null ? null : new UFDouble(ntotalpaymny));
				// ntotalreceivenumber :
				BigDecimal ntotalreceivenumber = (BigDecimal) rs.getObject(60);
				saleItem.setNtotalreceivenumber(ntotalreceivenumber == null ? null : new UFDouble(ntotalreceivenumber));
				// ntotalinvoicenumber :
				BigDecimal ntotalinvoicenumber = (BigDecimal) rs.getObject(61);
				saleItem.setNtotalinvoicenumber(ntotalinvoicenumber == null ? null : new UFDouble(ntotalinvoicenumber));
				// ntotalinvoicemny :
				BigDecimal ntotalinvoicemny = (BigDecimal) rs.getObject(62);
				saleItem.setNtotalinvoicemny(ntotalinvoicemny == null ? null : new UFDouble(ntotalinvoicemny));
				// ntotalinventorynumber :
				BigDecimal ntotalinventorynumber = (BigDecimal) rs.getObject(63);
				saleItem.setNtotalinventorynumber(ntotalinventorynumber == null ? null : new UFDouble(
						ntotalinventorynumber));
				// ntotalbalancenumber :
				BigDecimal ntotalbalancenumber = (BigDecimal) rs.getObject(64);
				saleItem.setNtotalbalancenumber(ntotalbalancenumber == null ? null : new UFDouble(ntotalbalancenumber));
				// ntotalsignnumber :
				BigDecimal ntotalsignnumber = (BigDecimal) rs.getObject(65);
				saleItem.setNtotalsignnumber(ntotalsignnumber == null ? null : new UFDouble(ntotalsignnumber));
				// ntotalcostmny :
				BigDecimal ntotalcostmny = (BigDecimal) rs.getObject(66);
				saleItem.setNtotalcostmny(ntotalcostmny == null ? null : new UFDouble(ntotalcostmny));
				// bifinvoicefinish :
				String bifinvoicefinish = rs.getString(67);
				saleItem.setBifinvoicefinish(bifinvoicefinish == null ? null : new UFBoolean(bifinvoicefinish.trim()));
				// bifreceivefinish :
				String bifreceivefinish = rs.getString(68);
				saleItem.setBifreceivefinish(bifreceivefinish == null ? null : new UFBoolean(bifreceivefinish.trim()));

				// bifinventoryfinish :
				String bifinventoryfinish = rs.getString(69);
				saleItem.setBifinventoryfinish(bifinventoryfinish == null ? null : new UFBoolean(bifinventoryfinish
						.trim()));
				// bifpayfinish :
				String bifpayfinish = rs.getString(70);
				saleItem.setBifpayfinish(bifpayfinish == null ? null : new UFBoolean(bifpayfinish.trim()));

				// exets
				String exets = rs.getString(71);
				saleItem.setExets(exets == null ? null : new UFDateTime(exets.trim()));

				// cprojectid :
				String cprojectid = rs.getString(72);
				saleItem.setCprojectid(cprojectid == null ? null : cprojectid.trim());
				// cprojectphaseid :
				String cprojectphaseid = rs.getString(73);
				saleItem.setCprojectphaseid(cprojectphaseid == null ? null : cprojectphaseid.trim());
				// cprojectid3 :
				String cprojectid3 = rs.getString(74);
				saleItem.setCprojectid3(cprojectid3 == null ? null : cprojectid3.trim());
				// vfree1 :
				String vfree1 = rs.getString(75);
				saleItem.setVfree1(vfree1 == null ? null : vfree1.trim());
				// vfree2 :
				String vfree2 = rs.getString(76);
				saleItem.setVfree2(vfree2 == null ? null : vfree2.trim());
				// vfree3 :
				String vfree3 = rs.getString(77);
				saleItem.setVfree3(vfree3 == null ? null : vfree3.trim());
				// vfree4 :
				String vfree4 = rs.getString(78);
				saleItem.setVfree4(vfree4 == null ? null : vfree4.trim());
				// vfree5 :
				String vfree5 = rs.getString(79);
				saleItem.setVfree5(vfree5 == null ? null : vfree5.trim());
				// vdef1 :
				String vdef1 = rs.getString(80);
				saleItem.setVdef1(vdef1 == null ? null : vdef1.trim());
				// vdef2 :
				String vdef2 = rs.getString(81);
				saleItem.setVdef2(vdef2 == null ? null : vdef2.trim());
				// vdef3 :
				String vdef3 = rs.getString(82);
				saleItem.setVdef3(vdef3 == null ? null : vdef3.trim());
				// vdef4 :
				String vdef4 = rs.getString(83);
				saleItem.setVdef4(vdef4 == null ? null : vdef4.trim());
				// vdef5 :
				String vdef5 = rs.getString(84);
				saleItem.setVdef5(vdef5 == null ? null : vdef5.trim());
				// vdef6 :
				String vdef6 = rs.getString(85);
				saleItem.setVdef6(vdef6 == null ? null : vdef6.trim());

				// 获取v30后加入的其他字段值
				BigDecimal ntotalshouldoutnum = (BigDecimal) rs.getObject(86);
				saleItem.setNtotalshouldoutnum(ntotalshouldoutnum == null ? new UFDouble(0) : new UFDouble(
						ntotalshouldoutnum));
				// ntotalreturnnumber
				BigDecimal ntotalreturnnumber = (BigDecimal) rs.getObject(87);
				saleItem.setNtotalreturnnumber(ntotalreturnnumber == null ? new UFDouble(0) : new UFDouble(
						ntotalreturnnumber));

				getOrdBAndExeValueFromResultSet(rs, 88, saleItem);

				v.addElement(saleItem);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		saleItems = new SaleorderBVO[v.size()];
		if (v.size() > 0) {
			v.copyInto(saleItems);
		}

		if (saleItems != null) {
			ArrayList<SaleorderBVO> array = null;
			for (SaleorderBVO bvo : saleItems) {
				if (!ht.containsKey(bvo.getCsourcebillbodyid())) {
					array = new ArrayList<SaleorderBVO>();
					array.add(bvo);
					ht.put(bvo.getCsourcebillbodyid(), array);
				} else {
					array = ht.get(bvo.getCsourcebillbodyid());
					array.add(bvo);
				}
			}
		}
		return ht;
	}

	/**
	 * 回写销售合同的定购数量 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void setSaleCT(SaleorderBVO[] ordbvos, int iAction) throws SQLException, nc.vo.pub.BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {
		if (ordbvos == null || ordbvos.length <= 0)
			return;
		boolean bhvct = false;
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			if (nc.ui.scm.so.SaleBillType.SoContract.equals(ordbvos[0].getCreceipttype())
					|| nc.ui.scm.so.SaleBillType.SoInitContract.equals(ordbvos[0].getCreceipttype())) {
				bhvct = true;
				break;
			}
		}
		if (!bhvct) {
			return;
		}

		// 检查合同模块存在
		// try {
		// Class cl = Class.forName("nc.bs.ct.out.PurBackToCtBO");
		// if(cl==null)
		// return ;
		if (SO39 == null) {
			nc.bs.pub.para.SysInitDMO sysinitdmo = new nc.bs.pub.para.SysInitDMO();
			SO39 = sysinitdmo.getParaBoolean(ordbvos[0].getPkcorp(), "SO39");
		}
		if (SO39 == null)
			SO39 = SoVoConst.buffalse;
		// 销售订单结束时是否用订单出库数量修订销售合同执行数量
		if (!SO39.booleanValue())
			return;
		// } catch (Exception e) {
		// /** 检查合同模块是否安装，实例化类不成功，返回false*/
		// nc.vo.scm.pub.SCMEnv.out(e.getMessage());
		// SO39 = SoVoConst.buffalse;
		// return ;
		//
		// }

		ParaPoToCtRewriteVO ctvo = null;
		ArrayList alist = new ArrayList();
		UFDouble nnumber = null, nmny = null;
		UFDouble duf_1 = new UFDouble(-1);
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			if (ordbvos[i].getNnumber() == null)
				continue;
			if (nc.ui.scm.so.SaleBillType.SoContract.equals(ordbvos[i].getCreceipttype())
					|| nc.ui.scm.so.SaleBillType.SoInitContract.equals(ordbvos[i].getCreceipttype())) {

				nnumber = SoVoTools.getMnySub(ordbvos[i].getNnumber(), ordbvos[i].getNtotalinventorynumber());
				if (nnumber == null || nnumber.compareTo(SoVoConst.duf0) == 0)
					continue;
				nmny = nnumber.div(ordbvos[i].getNnumber()).multiply(
						ordbvos[i].getNsummny() == null ? SoVoConst.duf0 : ordbvos[i].getNsummny());

				if (ordbvos[i].getBlargessflag() != null && ordbvos[i].getBlargessflag().booleanValue()) {
					nnumber = SoVoConst.duf0;
					nmny = SoVoConst.duf0;
				}

				ctvo = new ParaPoToCtRewriteVO();
				ctvo.setCContractRowID(ordbvos[i].getCsourcebillbodyid());
				ctvo.setFirstTime(false);
				if (iAction == ISaleOrderAction.A_OPEN) {
					ctvo.setDNum(nnumber);
					ctvo.setDSummny(nmny);
					alist.add(ctvo);

				} else if (iAction == ISaleOrderAction.A_CLOSE) {
					ctvo.setDNum(nnumber.multiply(duf_1));
					ctvo.setDSummny(nmny.multiply(duf_1));
					alist.add(ctvo);
				}

			}

		}

		if (alist.size() > 0) {
			ParaPoToCtRewriteVO[] ctvos = (ParaPoToCtRewriteVO[]) alist.toArray(new ParaPoToCtRewriteVO[alist.size()]);

			try {
				// 20060817 wsy 使用新接口
				ICtToPo_BackToCt backtoCt = (ICtToPo_BackToCt) NCLocator.getInstance().lookup(
						ICtToPo_BackToCt.class.getName());
				if (backtoCt != null)
					backtoCt.writeBackAccuOrdData(ctvos);

			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				nc.vo.scm.pub.SCMEnv.out(e.getMessage());
				throw new BusinessRuntimeException(e.getMessage());
			}
			// clsSaleCT.writeBackAccuOrdData(ctvos);
		}
	}

	/**
	 * 回写销售合同的定购数量 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void setSaleCTWhenClose(AggregatedValueObject vo) throws SQLException, nc.vo.pub.BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {
		if (vo == null)
			return;
		String creceipttype = ((SaleOrderVO) vo).getBodyVOs()[0].getCreceipttype();
		if (nc.ui.scm.so.SaleBillType.SoContract.equals(creceipttype)
				|| nc.ui.scm.so.SaleBillType.SoInitContract.equals(creceipttype)) {
			setSaleCT(((SaleOrderVO) vo).getBodyVOs(), ISaleOrderAction.A_CLOSE);
		}

	}

	/**
	 * 回写销售合同的定购数量 创建日期：(2001-9-18 11:36:22)
	 * 
	 * @return boolean
	 * @param 单据附表ID:ReceiptDetailID
	 *            java.lang.StringBuffer
	 * @param 单据主键ID:ReceiptID
	 *            java.lang.StringBuffer
	 */
	public void setSaleCTWhenOpen(AggregatedValueObject vo) throws SQLException, nc.vo.pub.BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {
		if (vo == null)
			return;
		String creceipttype = ((SaleOrderVO) vo).getBodyVOs()[0].getCreceipttype();
		if (nc.ui.scm.so.SaleBillType.SoContract.equals(creceipttype)
				|| nc.ui.scm.so.SaleBillType.SoInitContract.equals(creceipttype)) {
			setSaleCT(((SaleOrderVO) vo).getBodyVOs(), ISaleOrderAction.A_OPEN);
		}
	}
	
	public String getOID(String pk_corp) {
		return super.getOID(pk_corp);
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-25 14:22:09) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.util.ArrayList
	 * @param vo
	 *            nc.vo.so.so001.SaleOrderVO
	 * @exception 异常说明。
	 */
	private ArrayList fillPkAndTs(SaleOrderVO vo) throws BusinessException {

		ArrayList retlist = new ArrayList();
		if (vo == null || vo.getHeadVO() == null)
			return retlist;
		boolean ishead = false;
		if ((vo.getHeadVO().getCsaleid() == null || vo.getHeadVO().getCsaleid().trim().length() <= 0)
				&& vo.getHeadVO().getAttributeValue(SaleorderHVO.CSALEID_TEMP)==null)
			ishead = true;
		SaleorderBVO[] bvos = vo.getBodyVOs();
		// ServiceProviderBO servbo = new ServiceProviderBO();

		UFDateTime ts = new UFDateTime(System.currentTimeMillis());
		vo.getHeadVO().setTs(ts);
		int count = ishead ? 1 : 0;
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getStatus() == VOStatus.NEW
					&& (bvos[i].getBoosflag() == null || !bvos[i].getBoosflag().booleanValue())) {
				count++;
			}
			if (bvos[i].getStatus() != VOStatus.UNCHANGED && bvos[i].getStatus() != VOStatus.DELETED) {
				bvos[i].setTs(ts);
			}
		}
		String[] coids = getOIDs(vo.getHeadVO().getPk_corp(), count);
		if (ishead) {
			vo.getHeadVO().setCsaleid(coids[0]);
			retlist.add(vo.getHeadVO().getCsaleid());
			retlist.add(vo.getHeadVO().getVreceiptcode());
		}else if (vo.getHeadVO().getAttributeValue(SaleorderHVO.CSALEID_TEMP)!=null){
			vo.getHeadVO().setCsaleid(vo.getHeadVO().getAttributeValue(SaleorderHVO.CSALEID_TEMP).toString());
			retlist.add(vo.getHeadVO().getCsaleid());
			retlist.add(vo.getHeadVO().getVreceiptcode());
		}else {
			retlist.add(vo.getHeadVO().getCsaleid());
		}

		int ipos = ishead ? 1 : 0;
		String csaleid = vo.getHeadVO().getCsaleid();
		for (int i = 0, loop = bvos.length; i < loop; i++) {
			if (bvos[i].getStatus() == VOStatus.NEW
					&& (bvos[i].getBoosflag() == null || !bvos[i].getBoosflag().booleanValue())) {
				bvos[i].setCsaleid(csaleid);
				bvos[i].setCorder_bid(coids[ipos]);
				retlist.add(coids[ipos]);
				ipos++;
			}
		}
		retlist.add(ts.toString());

		return retlist;

	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-1-25 14:22:09) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.util.ArrayList
	 * @param vo
	 *            nc.vo.so.so001.SaleOrderVO
	 * @exception 异常说明。
	 * @throws BusinessException
	 */
	public void getAfterSaveInfo(SaleOrderVO vo, ArrayList retlist) throws java.sql.SQLException, BusinessException {

		if (vo == null || retlist == null)
			return;

		String csaleid = vo.getHeadVO().getCsaleid();
		if (csaleid == null)
			return;
		String sCustManID = vo.getHeadVO().getCcustomerid();
		String sPKCorp = vo.getHeadVO().getPk_corp();
		if (sCustManID == null || sPKCorp == null)
			return;

		String sbfSQL1 = " select corder_bid,cconsigncorpid,cadvisecalbodyid,cbodywarehouseid,cinventoryid1 from so_saleorder_b where csaleid = ? ";

		Connection con = null;
		PreparedStatement stmt = null;
		HashMap reths = new HashMap();
		if (vo.getErrMsg() != null && vo.getErrMsg().trim().length() > 0)
			reths.put("ErrMsg", "ERR" + vo.getErrMsg());
		try {
			con = getConnection();

			stmt = con.prepareStatement(sbfSQL1);
			stmt.setString(1, csaleid);
			HashMap hsids = new HashMap();
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String[] row = new String[4];
				// corder_bid
				String corder_bid = rs.getString(1);
				// cconsigncorpid
				row[0] = rs.getString(2);
				// cadvisecalbodyid
				row[1] = rs.getString(3);
				// cbodywarehouseid
				row[2] = rs.getString(4);
				// cinventoryid1
				row[3] = rs.getString(5);

				hsids.put(corder_bid, row);
			}
			reths.put("reLoadConsignCorpAndCalbody", hsids);

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		if (vo.getIAction() != nc.vo.so.so001.ISaleOrderAction.A_ADD) {
			reths.put("queryCachPayByOrdId", queryCachPayByOrdId(csaleid));
		}

		retlist.add(reths);

	}

	/**
	 * 根据主键在数据库中删除一个VO对象。
	 * 
	 * 创建日期：(2001-4-20)
	 * 
	 * @param key
	 *            nc.vo.pub.oid.OID
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public void updateBomID(SaleOrderVO vo) throws Exception {

		if (vo == null || vo.getHeadVO() == null || vo.getHeadVO().getCsaleid() == null)
			return;

		SaleorderBVO[] ordbvos = vo.getBodyVOs();
		if (ordbvos == null || ordbvos.length <= 0)
			return;
		ArrayList alist = new ArrayList();
		SOToolVO toolvo = null;
		for (int i = 0, loop = ordbvos.length; i < loop; i++) {
			if (SoVoTools.isEmptyString(ordbvos[i].getCbomorderid()))
				continue;
			toolvo = new SOToolVO(5);
			toolvo.setAttributeValue("cbomorderid", ordbvos[i].getCbomorderid());
			toolvo.setAttributeValue("csaleid", ordbvos[i].getCsaleid());
			toolvo.setAttributeValue("corder_bid", ordbvos[i].getCorder_bid());
			alist.add(toolvo);
		}

		if (alist.size() > 0) {
			SOToolVO[] toolvos = (SOToolVO[]) alist.toArray(new SOToolVO[alist.size()]);
			SOToolsDMO.updateBatch(toolvos, new String[] { "csaleid", "corder_bid" }, "so_bomorder",
					new String[] { "cbomorderid" });
		}

	}

	/**
	 * 向数据库插入一个VO对象。
	 * 
	 * 创建日期：(2004-3-17)
	 * 
	 * @param node
	 *            nc.vo.so.so016.BalanceVO
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public static void checkSaleorderBVOTs(SaleorderBVO[] vos) throws java.sql.SQLException, nc.bs.pub.SystemException,
			nc.vo.pub.BusinessException {

		if (vos == null || vos.length <= 0)
			return;

		HashMap hsvo = new HashMap();
		String keyvalue = null;
		String csaleid = null;
		for (int i = 0, loop = vos.length; i < loop; i++) {
			if (vos[i] == null)
				continue;
			keyvalue = (String) vos[i].getCorder_bid();
			if (keyvalue == null || keyvalue.trim().length() <= 0)
				continue;
			hsvo.put(keyvalue, vos[i]);

			if (csaleid == null) {
				keyvalue = (String) vos[i].getCsaleid();
				if (keyvalue != null && keyvalue.trim().length() > 0)
					csaleid = keyvalue;
			}
		}
		if (csaleid == null || hsvo.size() <= 0)
			return;
		SORowData[] rows = nc.impl.scm.so.so016.SOToolsDMO
				.getAnyValueSORow(" select so_saleorder_b.corder_bid,so_saleorder_b.ts,so_saleexecute.ts from so_saleorder_b,so_saleexecute "
						+ " where so_saleorder_b.csaleid = so_saleexecute.csaleid and so_saleorder_b.corder_bid = so_saleexecute.csale_bid "
						+ " and so_saleexecute.creceipttype = '30' "
						+ " and so_saleorder_b.dr = 0 and so_saleorder_b.csaleid = '" + csaleid + "' ");

		if (rows == null || rows.length <= 0)
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115")/*
			 * @res "发生并发操作，请刷新界面再试"
			 */);

		UFDateTime ts1 = null, ts0 = null;
		SaleorderBVO vo = null;
		String corder_bid = null;
		for (int i = 0, loop = rows.length; i < loop; i++) {
			corder_bid = rows[i].getString(0);
			if (corder_bid == null)
				continue;
			vo = (SaleorderBVO) hsvo.get(corder_bid);
			if (vo == null)
				continue;
			ts0 = vo.getTs();
			ts1 = rows[i].getUFDateTime(1);

			// if (ts1 == null) {
			// if (ts0 != null)
			// throw new
			// BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000115")/*@res
			// "发生并发操作，请刷新界面再试"*/);
			// } else {
			// if(!ts1.equals(ts1))
			// throw new
			// BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub","UPPsopub-000115")/*@res
			// "发生并发操作，请刷新界面再试"*/);
			// }

			if (ts0 != null && ts1 != null) {
				if (!ts0.toString().trim().equals(ts1.toString().trim()))
					throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115")/*
					 * @res "发生并发操作，请刷新界面再试"
					 */);
			}

			ts0 = vo.getExets();
			ts1 = rows[i].getUFDateTime(2);
			if (ts0 != null && ts1 != null) {
				if (!ts0.toString().trim().equals(ts1.toString().trim()))
					throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("sopub", "UPPsopub-000115")/*
					 * @res "发生并发操作，请刷新界面再试"
					 */);
			}

		}

	}

	/**
	 * 接口函数功能:新单据参照查询源头单据使用，包括表头，表体数据， 需要在平台注册的单据DMO实现
	 * 
	 * 参数: String sheadsql ----- 主查询条件 String sbodysql ----- 辅查询条件
	 * 
	 * 
	 * 返回值:Object[3] 0 ------ 查询的单据主VO数组 CircularlyAccessibleValueObject[] 1
	 * ------ 查询的单据子VO数组 CircularlyAccessibleValueObject[] 2 ------ 查询数据量过大的提示
	 * String 实现注意：在查询的单据数据量过大时，如子表，可以仅返回前1000条数据， 此时必须返回数据量过大的提示，以便提示给用户！！！
	 * 异常:SQLException,RemoteException
	 * 
	 */
	/**
	 * @param sheadsql
	 * @param sbodysql
	 * @return
	 * @throws BusinessException
	 */
	public Object[] queryAllBillDatas(String sheadsql, String sbodysql) throws BusinessException {
		if (sheadsql == null || sheadsql.trim().length() <= 0)
			return null;

	    //创建时间工具类实例
		nc.vo.scm.pu.Timer timer = new nc.vo.scm.pu.Timer();
		timer.start();
		
		try {

			String sheadsqlnew = sheadsql;

			if (sbodysql != null && sbodysql.trim().length() > 0)
				if (sbodysql.trim().startsWith("and") || sbodysql.trim().startsWith("AND"))
					sheadsqlnew += " " + sbodysql;
				else
					sheadsqlnew += " and " + sbodysql;

			CircularlyAccessibleValueObject[] headvos = queryAllHeadData(sheadsqlnew);

			if (headvos == null || headvos.length <= 0)
				return null;

			Object[] retobjs = new Object[3];

			retobjs[0] = headvos;

			/*if (headvos != null && headvos.length > 5000) {
				CircularlyAccessibleValueObject[] bakvos = headvos;
				headvos = new SaleorderHVO[5000];
				System.arraycopy(bakvos, 0, headvos, 0, headvos.length);
				retobjs[2] = "本次查询数据量太大，只能返回前5000行数据";
			}*/
			timer.addExecutePhase(">>>>>>>>>销售订单[queryAllBillDatas接口查询--表头查询]用时：>>>>>>>>>>>");			
			
			SaleorderBVO voaItem[] = queryAllBodyDataByWhere(sbodysql, null);

			retobjs[1] = voaItem;
			/*if (voaItem != null && voaItem.length > 5000) {
				CircularlyAccessibleValueObject[] bakvos = voaItem;
				voaItem = new SaleorderBVO[5000];
				System.arraycopy(bakvos, 0, voaItem, 0, voaItem.length);
				retobjs[2] = "本次查询数据量太大，只能返回前5000行数据";
			}*/
			timer.addExecutePhase(">>>>>>>>>销售订单[queryAllBillDatas接口查询--表体查询]用时：>>>>>>>>>>>");

			//输出各个业务操作占用时间分布
			timer.showAllExecutePhase(">>>>>>>>>销售订单[queryAllBillDatas接口查询]结束：>>>>>>>>>>>");
			
			return retobjs;

		} catch (Exception ee) {
			nc.vo.scm.pub.SCMEnv.out(ee.getMessage());
			throw new BusinessException(ee.getMessage());
		}

		// return null;
	}

	// /**
	// * 检查买赠设置
	// *
	// * @param vo
	// */
	// private void checkBuyLargess(SaleOrderVO vo) throws Exception {
	// // 1、检查目前设置了买赠的行
	// HashMap<String, ArrayList<SaleorderBVO>> hp = new HashMap();
	//
	// SaleorderBVO[] bvos;
	// if (vo.getOldSaleOrderVO() == null) {
	// bvos = vo.getBodyVOs();
	// }
	// /** 为修改保存添加 V502 zhongwei* */
	// // 非新增保存的情况下，表体只会传入状态变化的行
	// // 需要把变化的新行，和没有变化的行合并，组成完整表体，才能校验买赠关系
	// else {
	// HashMap<String, SaleorderBVO> hm = new HashMap();
	// SaleorderBVO[] items = vo.getBodyVOs();
	// for (SaleorderBVO sobvo : items) {
	// hm.put(sobvo.getCorder_bid(), sobvo);
	// }
	//
	// items = vo.getOldSaleOrderVO().getBodyVOs();
	// ArrayList<SaleorderBVO> al = new ArrayList(items.length);
	// for (SaleorderBVO sobvo : items) {
	// al.add(hm.containsKey(sobvo.getCorder_bid()) ? hm.get(sobvo
	// .getCorder_bid()) : sobvo);
	// }
	//
	// bvos = new SaleorderBVO[al.size()];
	// al.toArray(bvos);
	// }
	// /** 为修改保存添加 V502 zhongwei* */
	//
	// ArrayList<SaleorderBVO> albvos = new ArrayList();
	// for (int i = 0; i < bvos.length; i++) {
	// if (bvos[i].getAttributeValue("clargessrowno") != null
	// && bvos[i].getAttributeValue("clargessrowno").toString()
	// .length() > 0 && bvos[i].getBlargessflag() != null
	// && bvos[i].getBlargessflag().booleanValue()) {
	// albvos = hp.get(bvos[i].getAttributeValue("clargessrowno"));
	//
	// if (albvos == null)
	// albvos = new ArrayList();
	//
	// albvos.add(bvos[i]);
	// hp.put(bvos[i].getAttributeValue("clargessrowno").toString(),
	// albvos);
	// }
	// }// end for
	//
	// if (hp.size() == 0)
	// return;
	//
	// // 2、查找买赠行对应的主存货行
	// Vector vInvs = new Vector();
	// Vector vnewVOs = new Vector();
	// Vector vtchantype = new Vector();
	// for (int i = 0; i < bvos.length; i++) {
	// if (hp.containsKey(bvos[i].getCrowno())) {
	// vInvs.add(bvos[i].getCinventoryid());
	// vnewVOs.add(bvos[i]);
	// if (bvos[i].getCchantypeid() != null
	// && bvos[i].getCchantypeid().trim().length() > 0)
	// vtchantype.add(bvos[i].getCchantypeid());
	// }
	// }
	// // 3、查找目前主存貨對應的条件
	// String[] UnitInvs = new String[vInvs.size()];
	// vInvs.copyInto(UnitInvs);
	//
	// SaleorderHVO hvo = vo.getHeadVO();
	// String pk_corp = hvo.getPk_corp();
	// String dBillDate = hvo.getDbilldate().toString();
	// String pk_cumandoc = hvo.getCcustomerid();
	// String ccurrencytypeid = hvo.getCcurrencytypeid();
	// String[] cchantypeid = null;
	// if (vtchantype.size() > 0) {
	// cchantypeid = new String[vtchantype.size()];
	// vtchantype.copyInto(cchantypeid);
	// }
	// BuyLargessImpl buy = new BuyLargessImpl();
	// BuylargessVO[] blgvos = null;
	//
	// HashMap hmp = getCustgroupCodes(cchantypeid);
	//
	// // 区配买赠
	// SaleorderBVO[] bnewvos = new SaleorderBVO[vnewVOs.size()];
	// vnewVOs.copyInto(bnewvos);
	// boolean bfind = false;
	// SaleorderBVO bmainlarvo = null;// 记录对应赠品存货行
	// BuylargessVO larvo = null;
	//
	// // 针对每个主存货行
	// for (int i = 0, len = bnewvos.length; i < len; i++) {
	// // bfind= false;
	// try {
	// // 查询当前主存货所关连到的买赠关系
	// blgvos = buy.queryLargessItems(pk_corp, UnitInvs, dBillDate,
	// bnewvos[i].getCchantypeid() == null ? null
	// : new String[] { bnewvos[i].getCchantypeid() },
	// pk_cumandoc, ccurrencytypeid,null);
	// } catch (Exception e) {
	// SCMEnv.out(e);
	// throw new BusinessException(e);
	// }
	//
	// if (blgvos == null || blgvos.length == 0) {
	// throw new BusinessException(NCLangResOnserver.getInstance()
	// .getStrByID("40060301", "UPP40060301-000522"));
	// // "买赠关系已变化，请按新的买赠设置修改订单。");
	// }
	//
	// Hashtable htlargess = new Hashtable();
	//
	// // 存货管理档案ID+主单位ID
	// String key = null;
	// ArrayList al = null;
	// for (int j = 0; j < blgvos.length; j++) {
	// key = ((BuylargessHVO) blgvos[j].getParentVO())
	// .getPk_invmandoc()
	// + ((BuylargessHVO) blgvos[j].getParentVO())
	// .getCunitid();
	//
	// if (htlargess.containsKey(key)) {
	// al = (ArrayList) htlargess.get(key);
	// al.add(blgvos[j]);
	//
	// } else {
	// al = new ArrayList();
	// al.add(blgvos[j]);
	// htlargess.put(((BuylargessHVO) blgvos[j].getParentVO())
	// .getPk_invmandoc()
	// + ((BuylargessHVO) blgvos[j].getParentVO())
	// .getCunitid(), al);
	//
	// }
	// }
	//
	// larvo = findLargess(vo.getHeadVO(), bnewvos[i], htlargess, hmp);
	//
	// if (larvo == null)
	// throw new BusinessException(NCLangResOnserver.getInstance()
	// .getStrByID("40060301", "UPP40060301-000522"));
	//
	// // bmainlarvo = (SaleorderBVO) hp.get(bnewvos[i].getCrowno());
	// albvos = hp.get(bnewvos[i].getCrowno());
	//
	// for (int ii = 0, lenn = albvos.size(); ii < lenn; ii++) {
	// bmainlarvo = albvos.get(ii);
	//
	// for (int k = 0; k < larvo.getChildrenVO().length; k++) {
	// if (bmainlarvo.getCinventoryid().equals(
	// larvo.getChildrenVO()[k]
	// .getAttributeValue("pk_invmandoc"))
	// && bmainlarvo.getCunitid().equals(
	// larvo.getChildrenVO()[k]
	// .getAttributeValue("cunitid"))) {
	// compareMainInvWithLargess((BuylargessHVO) larvo
	// .getParentVO(), (BuylargessBVO) larvo
	// .getChildrenVO()[k], bmainlarvo);
	// bfind = true;
	// break;
	// }
	// }// end for k
	//
	// }// end for ii
	//
	// if (!bfind)
	// throw new BusinessException(NCLangResOnserver.getInstance()
	// .getStrByID("40060301", "UPP40060301-000522"));
	// // "买赠关系已变化，请按新的买赠设置修改订单。");
	//
	// }// end for each main inv
	//
	// }
	//
	// private HashMap getCustgroupCodes(String[] sKeys) throws Exception {
	//
	// HashMap hp = new HashMap();
	// if (sKeys == null || sKeys.length == 0)
	// return hp;
	// String swheres = "pk_defdoc in(";
	// for (int i = 0; i < sKeys.length; i++) {
	// if (i > 0)
	// swheres += ",";
	//
	// swheres += "'" + sKeys[i] + "'";
	// }
	// swheres += ")";
	// SmartDMO dmo = new SmartDMO();
	// Object[] o = dmo
	// .selectBy2("select pk_defdoc,doccode from bd_defdoc where "
	// + swheres);
	//
	// Object[] o1 = null;
	// if (o == null || o.length == 0)
	// return hp;
	// for (int i = 0; i < o.length; i++) {
	// o1 = (Object[]) o[i];
	//
	// hp.put(o1[0], o1[1]);
	// }
	// return hp;
	// }
	//
	// /**
	// * 找到需要的买赠设置
	// *
	// * @return
	// */
	// private BuylargessVO findLargess(SaleorderHVO hsalevo,
	// SaleorderBVO bsalevo, Hashtable htLargess, HashMap hmp) {
	//
	// // 存货管理档案ID+主单位ID
	// String sInvPk = bsalevo.getCinventoryid() + bsalevo.getCunitid();
	//
	// // if(sInvPk==null)return null ;
	// if (!htLargess.containsKey(sInvPk))
	// return null;
	// //
	// ArrayList allargess = (ArrayList) htLargess.get(sInvPk);
	// // 报价单位数量
	// UFDouble nnum = bsalevo.getNquoteunitnum();
	// if (nnum == null)
	// nnum = new UFDouble(0);
	//
	// BuylargessVO vo = null;
	// BuylargessHVO bhvo = null;
	//
	// BuylargessVO votmp = null;
	// BuylargessHVO btmphvo = null;
	//
	// String scustgroup = null;
	// String stmpcustgroup = null;
	// for (int i = 0, isize = allargess.size(); i < isize; i++) {
	// votmp = (BuylargessVO) allargess.get(i);
	// btmphvo = (BuylargessHVO) votmp.getParentVO();
	//
	// if (btmphvo.getNbuynum().compareTo(nnum) > 0)
	// continue;
	// if (vo == null)
	// vo = votmp;
	// else {
	// // 按客户->客户分组->上层客户分组
	// bhvo = (BuylargessHVO) vo.getParentVO();
	// // 如果新行的客户不为空，则应为首选
	// if (btmphvo.getPk_cumandoc() != null
	// && btmphvo.getPk_cumandoc().trim().length() > 0) {
	// vo = votmp;
	// } else if (btmphvo.getPk_custgroup() != null
	// && btmphvo.getPk_custgroup().trim().length() > 0) {
	// // 如果新行为客户分组
	// // 1、旧行为客户分组,取下级
	// if (bhvo.getPk_custgroup() != null
	// && bhvo.getPk_custgroup().trim().length() > 0) {
	// scustgroup = (String) hmp.get(bhvo.getPk_custgroup());
	// stmpcustgroup = (String) hmp.get(btmphvo
	// .getPk_custgroup());
	// if (scustgroup != null
	// && stmpcustgroup != null
	// && scustgroup.trim().length() < stmpcustgroup
	// .trim().length())
	// vo = votmp;
	// }
	// // 2、旧行为客户分组客户均为空
	// else if ((bhvo.getPk_cumandoc() == null || bhvo
	// .getPk_cumandoc().trim().length() == 0)
	// && (bhvo.getPk_custgroup() == null || bhvo
	// .getPk_custgroup().trim().length() == 0)) {
	// vo = votmp;
	// }
	// }
	//
	// }
	//
	// }
	// return vo;
	// }
	//
	// /**
	// * @param bvo
	// * @param irow
	// * 当前行
	// * @param imainrow
	// * 主存货行
	// */
	// private void compareMainInvWithLargess(BuylargessHVO hvo,
	// BuylargessBVO bvo, SaleorderBVO bmainvo) throws BusinessException {
	// // 设置数量、金额
	// // 1. 如果控制为数量上限：=min(捆绑件设置中的数量*主件数量, 上限值)
	// // 2. 如果控制为金额上限：(金额=订单中的价税合计；单价=含税单价(原币))
	// // a) 数量*单价>金额上限？金额=金额上限,数量=金额/单价
	// // 主存货行数量
	// UFDouble umainnum = bmainvo.getNquoteunitnum();
	// if (umainnum == null)
	// umainnum = new UFDouble(0);
	// // 除基数
	// umainnum = umainnum.div(hvo.getNbuynum());
	//
	// if (bvo.getFtoplimittype() != null
	// && bvo.getFtoplimittype().intValue() == 0
	// && bmainvo.getNoriginalcursummny().compareTo(
	// bvo.getNtoplimitvalue() == null ? new UFDouble(0) : bvo
	// .getNtoplimitvalue()) > 0) {// 控制金额
	// // 数量*单价>金额
	// throw new BusinessException(NCLangResOnserver.getInstance()
	// .getStrByID(
	// "40060301",
	// "UPP40060301-000520",
	// null,
	// new String[] {
	// bvo.getNtoplimitvalue() == null ? "" : bvo
	// .getNtoplimitvalue().toString(),
	// bmainvo.getCrowno() }));
	//
	// // "超过买赠设置金额上限"+bvo.getNtoplimitvalue()+",不能保存,行号为"+bmainvo.getCrowno());
	//
	// } else if (bvo.getFtoplimittype() != null
	// && bvo.getFtoplimittype().intValue() == 1
	// && bmainvo.getNquoteunitnum().compareTo(bvo.ntoplimitvalue) > 0) {// 控制数量
	// throw new BusinessException(NCLangResOnserver.getInstance()
	// .getStrByID(
	// "40060301",
	// "UPP40060301-000521",
	// null,
	// new String[] {
	// bvo.getNtoplimitvalue() == null ? "" : bvo
	// .getNtoplimitvalue().toString(),
	// bmainvo.getCrowno() }));
	//
	// // throw new
	// //
	// BusinessException("超过买赠设置数量上限"+bvo.getNtoplimitvalue()+",不能保存,行号为"+bmainvo.getCrowno());
	//
	// }
	//
	// }

	/**
	 * 本公司<br>
	 * 形成的数量受参数“销售转需求数量”的控制，<br>
	 * 如果设置为“按订单数量”则形成的请购单/调拨申请单数量=销售订单数量；<br>
	 * 如果设置为“按净需求”则形成的请购单/调拨申请单数量=销售订单数量-当前库存组织的可用量<br>
	 * （结果大于0则形成请购单/调拨申请单，结果小于0则不形成请购单/调拨申请单）；<br>
	 * 
	 * 跨公司<br>
	 * 对于是直运销售，则直接按照订单数量形成调拨申请单/调拨订单，<br>
	 * 对于是非直运销售，调拨申请单/调拨订单数量受参数“销售转需求数量”的控制：<br>
	 * 如果设置为“按订单数量”则形成的调拨申请单/调拨订单数量=销售订单数量；<br>
	 * 如果设置为“按净需求”则形成的调拨申请单/调拨订单数量=销售订单数量-当前销售订单收货库存组织的可用量<br>
	 * （结果大于0则形成调拨申请/调拨订单，结果小于0则不形成调拨申请/调拨订单）<br>
	 * 
	 * 
	 * @param vo
	 * @param 销售转请购:按净需求的情况
	 * @param laborItems
	 *            劳务
	 * @param bothercorp
	 *            是否跨公司
	 * @return
	 * 
	 * @注：因为此方法从不处理混合类型数据，所以可以根据参数区分vo类型<br>
	 *                                   （请购、单公司调拨申请、跨公司调拨申请、跨公司调拨订单），简化处理过程<br>
	 * 
	 */
	protected SaleOrderVO getNumberBySO58(SaleOrderVO vo, Vector laborItems, int BD501) throws BusinessException {
		if (vo == null || vo.getParentVO() == null || vo.getChildrenVO() == null || vo.getChildrenVO().length <= 0)
			return vo = null;
		SaleorderBVO[] items = (SaleorderBVO[]) vo.getChildrenVO();

		// 过滤掉请购数量为负、为空、为零的行
		// items = (SaleorderBVO[]) getItemsWithPlus(items);
		if (items == null || items.length <= 0) {
			return vo = null;
		}
		try {
			// 先记录直运跨公司情况
			Vector vtDir = new Vector();
			Vector vtLeft = new Vector();
			Vector vItem = new Vector();

			String pk_corp = (String) vo.getParentVO().getAttributeValue("pk_corp");
			String consigncorpid = null;

			for (int i = 0; i < items.length; i++) {
				consigncorpid = items[i].getCconsigncorpid();

				// 直运 跨公司
				if (items[i].getBdericttrans() != null
						&& items[i].getBdericttrans().booleanValue()
						&& (consigncorpid != null && consigncorpid.trim().length() > 0 && !consigncorpid
								.equals(pk_corp))) {
					vtDir.add(items[i]);

				} else {
					vtLeft.add(items[i]);
				}

			}
			if (vtLeft.size() > 0) {
				items = new SaleorderBVO[vtLeft.size()];
				vtLeft.copyInto(items);
				// 合并
				nc.vo.scm.merge.DefaultVOMerger vom = new nc.vo.scm.merge.DefaultVOMerger();
				try {
					vom.setGroupingAttr(new String[] { "cinventoryid", "vfree1", "vfree2", "vfree3", "vfree4",
							"vfree5", "dconsigndate" });
					vom.setSummingAttr(new String[] { "nnumber" });
					items = (SaleorderBVO[]) vom.mergeByGroup(items);
					// 如果库存可用量能全部满足本次请购，则返回空
					if (items == null || items.length <= 0) {
						return vo = null;
					}
				} catch (Exception e) {
					throw e;
				}
				// 获取可用量值[](对应合并后的表体VO[])
				UFDouble[] vals = getAtpNum(vo, items);
				// 如果可用量为空则初始化为零
				if (vals == null) {
					vals = new UFDouble[items.length];
					for (int i = 0; i < vals.length; i++) {
						vals[i] = new UFDouble(0);
					}
				} else {
					// 加回本次占用 2006-11-22
					// 此时的可用量是订单预占用后的值，需要复原回真正的数值
					// 注意处理折扣类存货数量为0的情况
					for (int i = 0; i < vals.length; i++) {
						vals[i] = vals[i].add(items[i].getNnumber() == null ? new UFDouble(0) : items[i].getNnumber());
					}
				}
				// 精度
				int prec = BD501;
				/*
				 * 需求:如果"可用量>=零",则不请购;反之则按"可用量*(-1)"请购(whj2003-05-05)
				 * 处理:如果"可用量>=零",则忽略; 反之则"items[i]=0,items[i]=脏"
				 */

				for (int i = 0; i < items.length; i++) {

					// 只有具有折扣属性的存货，数量才可能为空
					// 折扣类存货转需求无意义，过滤掉
					if (items[i].getNnumber() == null)
						continue;

					if (items[i].getNnumber().doubleValue() - vals[i].doubleValue() < 0
							&& !laborItems.contains(items[i].getCinvbasdocid()))
						continue;
					consigncorpid = items[i].getCconsigncorpid();

					// 回写
					SaleorderBVO[] tempVO = null;
					if (!laborItems.contains(items[i].getCinvbasdocid())) {
						// items[i].setNnumber(new UFDouble(0));
						items[i].setDirty(true);
						tempVO = (SaleorderBVO[]) vom.writeBackValue(items[i], "nnumber", vals[i], prec);
					}
					if ((tempVO == null || tempVO.length <= 0) && laborItems.contains(items[i].getCinvbasdocid())) {
						vItem.add(items[i]);
						continue;
					} else if ((tempVO == null || tempVO.length <= 0)) {
						continue;
					}
					for (int j = 0; j < tempVO.length; j++) {
						vItem.addElement(tempVO[j]);
					}

				}// end for

			}

			if (vItem.size() <= 0 && vtDir.size() <= 0) {
				SCMEnv.out("当前库存可用量能够满足此次销售转成的需求");
				vo = null;
			}
			items = null;
			if (vItem.size() > 0) {
				items = new SaleorderBVO[vItem.size()];
				vItem.copyInto(items);
			}
			if (vtDir.size() > 0) {
				SaleorderBVO[] tmpitems = new SaleorderBVO[vtDir.size()];
				vtDir.copyInto(tmpitems);
				if (items != null) {
					SaleorderBVO[] tmpitems1 = new SaleorderBVO[items.length + tmpitems.length];
					for (int i = 0; i < items.length; i++) {
						tmpitems1[i] = items[i];
					}
					for (int i = 0, iLen = tmpitems.length; i < iLen; i++) {
						tmpitems1[items.length + i] = tmpitems[i];
					}
					items = tmpitems1;
				} else {
					items = tmpitems;
				}
				// vo.setChildrenVO(items);
			}
			if (items != null)
				vo.setChildrenVO(items);
			// 返回
			return vo;
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException(e.getMessage());
		}

	}

	/**
	 * 功能：获取可用量数组 作者：wsy 日期：(2003-3-5 13:49:21)
	 */
	private UFDouble[] getAtpNum(AggregatedValueObject vo, SaleorderBVO[] items) throws Exception {
		UFDouble[] ufdRslt = null;
		try {
			String pk_corp = (String) vo.getParentVO().getAttributeValue("pk_corp");
			String consigncorpid = null;
			String sCstore = null;
			// if(bOtherCorp)
			// sCstore = (String)
			// vo.getParentVO().getAttributeValue("creccalbodyid");//收货库存组织
			// else
			// sCstore = (String)
			// vo.getParentVO().getAttributeValue("cadvisecalbodyid");//发货库存组织

			// 如果公司或库存组织存在空则直接返回空
			if (pk_corp == null || pk_corp.trim().equals(""))
				return null;
			// 查询可用量
			// nc.bs.ic.pub.InvATPDMO dmo = new nc.bs.ic.pub.InvATPDMO(); czp
			// V31 解藕
			IICPub_InvATPDMO dmo = // (IICPub_InvATPDMO) new
			// InterServBO().getInterInstance(ProductCode.PROD_IC,InterRegister.IC0021);
			(IICPub_InvATPDMO) NCLocator.getInstance().lookup(IICPub_InvATPDMO.class.getName());
			if (items == null || items.length <= 0)
				return null;
			ufdRslt = new UFDouble[items.length];
			// String sCmangid = null;
			for (int i = 0; i < items.length; i++) {
				consigncorpid = items[i].getCconsigncorpid();
				// 1) 如果销售订单行的发货公司为销售订单公司，则表示销售订单需要形成请购单或调拨申请：
				if (consigncorpid == null || consigncorpid.trim().length() <= 0 || consigncorpid.equals(pk_corp)) {
					sCstore = items[i].getCadvisecalbodyid();// 发货库存组织
				} else {
					sCstore = items[i].getCreccalbodyid();// 收货库存组织
				}
				if (sCstore == null)
					return null;

				ufdRslt[i] = dmo.getATPNum(pk_corp,
						sCstore,
						(String) items[i].getAttributeValue("cinventoryid"),// 存货管理档案ID
						(String) items[i].getAttributeValue("vfree1"), (String) items[i].getAttributeValue("vfree2"),
						(String) items[i].getAttributeValue("vfree3"), (String) items[i].getAttributeValue("vfree4"),
						(String) items[i].getAttributeValue("vfree5"), null, null, null, null, null, items[i]
								.getAttributeValue("dconsigndate") == null ? null : items[i].getAttributeValue(
								"dconsigndate").toString());
				//
			}
		} catch (Exception e) {
			// PubDMO.throwRemoteException(e);
			throw e;
		}
		return ufdRslt;
	}

	protected Vector getLabors(SaleorderBVO[] items) throws BusinessException {
		try {
			String[] basids = new String[items.length];
			for (int i = 0; i < items.length; i++) {
				basids[i] = items[i].getCinvbasdocid();
			}

			HashMap laborFlags = isLabor(basids);
			Vector laborItems = new Vector();
			String isLabor = "N";
			for (int i = 0; i < items.length; i++) {
				isLabor = laborFlags.get(items[i].getCinvbasdocid()).toString();
				if (isLabor.equals("Y")) {
					laborItems.add(items[i].getCinvbasdocid());
				}
			}
			return laborItems;
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException(e.getMessage());
		}

	}

	/**
	 * 此处插入方法说明。 功能描述:判断存货是否为劳务属性。 输入参数:存货管理ID
	 * 返回值:根据存货管理ID搜索订单体，如果存在未关闭的订单行，则返回TRUE；否则，返回FALSE 异常处理: 日期:2005/06/28 zx
	 */
	private HashMap isLabor(String[] cMangID) throws SQLException {

		if (cMangID == null || cMangID.length == 0) {
			return null;
		}
		StringBuffer subcon = new StringBuffer();
		HashMap retIDs = new HashMap();
		for (int i = 0; i < cMangID.length; i++) {
			if (i < cMangID.length - 1) {
				subcon.append("'");
				subcon.append(cMangID[i] + "',");
			} else if (i == cMangID.length - 1) {
				subcon.append("'");
				subcon.append(cMangID[i] + "'");
			}
		}
		if (subcon.toString() == null) {
			return null;
		}
		String sql = "select laborflag,pk_invbasdoc from bd_invbasdoc  where pk_invbasdoc in (" + subcon.toString()
				+ ")";

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			//
			while (rs.next()) {
				String laborFlag = rs.getString(1);
				String invbas = rs.getString(2);
				if (laborFlag == null || laborFlag.trim().length() == 0) {
					laborFlag = "N";
				}
				retIDs.put(invbas, laborFlag);
			}

		} finally {
			// 关闭结果集，即时释放资源
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
			}
		}

		return retIDs;
	}

	/**
	 * 推式保存对照下游单据前的合法性检查接口,同时设置平台流转
	 * 
	 * @since v50
	 * @author czp
	 * @date 2006-08-23
	 */
	public boolean checkValidOrNeed(AggregatedValueObject srcBillVo, String destBilltype, String drivedAction,
			String arg3) throws BusinessException {

		return true;
	}

	/**
	 * 拉式消息过滤处理
	 * 
	 * @since v50
	 */
	public HashSet filterUsers(String srcBilltype, String destBilltype, AggregatedValueObject billvo, RoleVO[] roles) {
		if (billvo == null || billvo.getChildrenVO() == null || billvo.getChildrenVO().length == 0) {
			SCMEnv.out("billvo == null || billvo.getChildrenVO() == null || billvo.getChildrenVO().length == 0，直接返回");
			return null;
		}

		HashSet<String> hashRet = new HashSet();
		SaleorderBVO[] items = (SaleorderBVO[]) billvo.getChildrenVO();
		int iLen = items.length;

		// 销售出库单
		if ("4C".equalsIgnoreCase(destBilltype)) {
			StoreadminBodyVO[] vosToIc = new StoreadminBodyVO[iLen];
			for (int i = 0; i < iLen; i++) {
				vosToIc[i] = new StoreadminBodyVO();
				vosToIc[i].setPk_corp((String) items[i].getAttributeValue("pk_corp"));
				vosToIc[i].setCcalbodyid(items[i].getCadvisecalbodyid());
				vosToIc[i].setCinventoryid(items[i].getCinventoryid());
				vosToIc[i].setCwarehouseid(items[i].getCbodywarehouseid());
			}
			IICToPU_StoreadminDMO icFilterSrv = (IICToPU_StoreadminDMO) NCLocator.getInstance().lookup(
					IICToPU_StoreadminDMO.class.getName());
			try {
				ArrayList<String> listRetFromIc = icFilterSrv.getUserArrayForStore(vosToIc, roles);
				if (listRetFromIc != null) {
					hashRet.addAll(listRetFromIc);
				}
			} catch (Exception e) {
				SCMEnv.out("调用 IC 服务IICToPU_StoreadminDMO.getUserArrayForStore(vosToIc, roles)异常：");/*-=notranslate=-*/
				SCMEnv.out(e);
			}
		}// end 4C

		// 销售发票
		else if (SaleBillType.SaleInvoice.equalsIgnoreCase(destBilltype)) {
			try {
				// 通过角色获得用户PK
				IRoleManageQuery roleQry = (IRoleManageQuery) NCLocator.getInstance().lookup(
						IRoleManageQuery.class.getName());
				UserVO[] voaUser = null;
				int kLen = roles.length;

				for (int i = 0; i < kLen; i++) {
					voaUser = roleQry.getUsers(roles[i].getPk_role(), roles[i].getPk_corp());
					if (voaUser != null) {
						for (int j = 0; j < voaUser.length; j++) {
							hashRet.add(voaUser[j].getPrimaryKey());
						}
					}
				}

			} catch (Exception e) {
				System.err.println("订单给发票下游消息获取信息出错，消息接收用户设置出错");
				SCMEnv.out(e);
			}
		}// end 32

		return hashRet;
	}

	/**
	 * @param sIds:表体ID
	 * @throws BusinessException
	 *             检查执行表是否合法
	 */
	public static void checkIsExecuteNumRigth(String[] sIds) throws BusinessException {
		try {
			HashMap hp = SOToolsDMO
					.getAnyValueArray(
							"so_saleexecute,so_sale,bd_busitype",
							new String[] { "ntotalinventorynumber", "ntotalreturnnumber", "ntranslossnum", "verifyrule" },
							"csale_bid",
							sIds,
							" so_sale.creceipttype = '30' "
									+ " and so_sale.cbiztype=bd_busitype.pk_busitype and so_sale.csaleid=so_saleexecute.csaleid ");

			if (hp == null || hp.size() == 0)
				return;
			UFDouble ntotalinventorynumber = null;
			UFDouble ntotalreturnnumber = null;
			UFDouble ntranslossnum = null;
			Object[] sValues = null;
			// 核算规则
			String verifyrule = null;
			for (int i = 0; i < sIds.length; i++) {

				if (hp.containsKey(sIds[i])) {
					sValues = (Object[]) hp.get(sIds[i]);
					if (sValues == null || sValues.length == 0)
						return;

					// 对"直运"类的，直接跳过
					verifyrule = sValues[3] == null ? null : sValues[3].toString();
					if (verifyrule != null && verifyrule.equals("Z"))
						continue;

					ntotalinventorynumber = sValues[0] == null ? new UFDouble(0) : SmartVODataUtils
							.getUFDouble(sValues[0]);
					ntotalreturnnumber = sValues[1] == null ? new UFDouble(0) : SmartVODataUtils
							.getUFDouble(sValues[1]);
					ntranslossnum = sValues[2] == null ? new UFDouble(0) : SmartVODataUtils
							.getUFDouble(sValues[2]);
					if (ntotalinventorynumber.doubleValue() < (ntotalreturnnumber.doubleValue() + ntranslossnum
							.doubleValue())) {
						throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("40060301",
								"UPP40060301-000039", null, new String[] { String.valueOf(i + 1) })
						/* @res "退货+途损数量不能超过已出库数量" */);
					}
				}

			}

		} catch (Exception e) {
			SCMEnv.out(e);
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else
				throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 返回上游单据信息：上游单据ID，制单人PK，审批人PK
	 * 
	 * @param pk_srcBilltype
	 *            消息接收单据，如果不是当前单据的来源单据，则返回空
	 * @param billVO
	 *            当前单据聚合VO
	 * 
	 * @return
	 * 
	 */
	public SourceBillInfo[] findSourceBill(String pk_srcBilltype, Object billEntity) {
		AggregatedValueObject billVO = (AggregatedValueObject) billEntity;
		if (billVO == null || billVO.getParentVO() == null)
			return null;

		String cbilltypecode = (String) billVO.getParentVO().getAttributeValue("cbilltypecode");

		if (cbilltypecode == null)
			return null;

		// 销售订单支持上游消息
		if (SaleBillType.SaleOrder.equals(cbilltypecode)) {
			try {
				// 调用公共方法查询
				return CommonDataDMO.findSourceBill(pk_srcBilltype, cbilltypecode, billVO);
			} catch (Exception e) {
				SCMEnv.out(e);
			}
		}// end if sale order

		return null;
	}

	/**
	 * 支持邮件短信审批
	 */
	public IDataSource getPrintDs(String billId, String billtype, String checkman) throws BusinessException {
		try {
			AggregatedValueObject voBill = queryData(billId);
			return new SOPrintDataSourceForMailAudit(voBill);
		} catch (Exception e) {
			SCMEnv.out(e);
		}
		return null;
	}

	/**
	 * 支持邮件短信审批
	 */
	public CircularlyAccessibleValueObject queryHeadData(String billId) throws BusinessException {
		return queryAllHeadData(" so_sale.csaleid = '" + billId + "'")[0];
	}

	/**
	 * 方法功能描述：送审时改变单据状态。 <b>参数说明</b>
	 * 
	 * @param key
	 * @throws java.sql.SQLException
	 * @author fengjb
	 * @throws BusinessException
	 * @time 2008-9-17 下午08:35:50
	 */
	public void sendAudit(SaleOrderVO sale) throws java.sql.SQLException, BusinessException {
		if (sale == null || sale.getPrimaryKey() == null)
			return;

		String sql = "update so_sale set  fstatus=" + nc.ui.pub.bill.BillStatus.AUDITING + " where csaleid = ?";
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			;
			stmt.setString(1, sale.getPrimaryKey());
			stmt.executeUpdate();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 设置销售订单已协同生成采购订单
	 * 
	 * @param soId
	 * @param isTrue
	 * @throws java.sql.SQLException
	 * @throws BusinessException
	 */
	public void setSoRefferedByPO(String[] soIds, boolean isTrue) throws java.sql.SQLException, BusinessException {
		for (String id : soIds) {
			updateBlColumn("bcooptopo", isTrue, id);
		}

	}

	/**
	 * 是否由采购订单协同生成
	 * 
	 * @param soId
	 * @param isTrue
	 * @throws java.sql.SQLException
	 * @throws BusinessException
	 */
	public void setPOPushSO(String[] soIds, boolean isTrue) throws java.sql.SQLException, BusinessException {
		for (String id : soIds) {
			updateBlColumn("bpocooptome", isTrue, id);
		}

	}

	public void rewritePOCoopSO(String[] soIds, boolean isTrue, String poOrderCode) throws java.sql.SQLException,
			BusinessException {
		for (String id : soIds) {
			if (poOrderCode == null) {
				//如果是删除操作，把是否协同生成采购订单也清空。
				updateBlColumn("bcooptopo", false, id);
			}
			updateBlColumn("bcooptopo", isTrue, id);
			String code = (poOrderCode == null) ? "" : poOrderCode;
			updateHeadColumn("ccooppohid", code, id);
		}

	}

	public void updateBlColumn(String columnName, boolean isTrue, String recordId) throws java.sql.SQLException,
			BusinessException {
		String btrue = isTrue ? "Y" : "N";
		String sql = "update so_sale set " + columnName + "='" + btrue + "' where csaleid='" + recordId + "'";
		try {
			SmartDMO dmo = new SmartDMO();
			dmo.executeUpdate(sql, new ArrayList(), new ArrayList());

		} catch (Exception e) {
			SCMEnv.error(e);
			throw new BusinessException(e.getMessage(), e);
		}

	}

	public void updateHeadColumn(String columnName, String columnValue, String recordId) throws java.sql.SQLException,
			BusinessException {

		String sql = "update so_sale set " + columnName + "='" + columnValue + "' where csaleid='" + recordId + "'";
		try {
			SmartDMO dmo = new SmartDMO();
			dmo.executeUpdate(sql, new ArrayList(), new ArrayList());

		} catch (Exception e) {
			SCMEnv.error(e);
			throw new BusinessException(e.getMessage(), e);
		}

	}

	public String queryHeadColumn(String columnName, String recordId) throws java.sql.SQLException, BusinessException {
		String value = null;
		String sql = "select " + columnName + " from so_sale  where csaleid='" + recordId + "'";
		try {
			SmartDMO dmo = new SmartDMO();
			Object[] results = dmo.selectBy2(sql);
			if (results != null
				&& results.length > 0
				&& results[0] != null
				&& ((Object[]) results[0])[0] != null) {
				value = ((Object[]) results[0])[0].toString();
			}

		} catch (Exception e) {
			SCMEnv.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return value;

	}

	public String queryBodyColumn(String columnName, String recordId) throws java.sql.SQLException, BusinessException {
		String value = null;
		String sql = "select " + columnName + " from so_saleorder_b  where corder_bid='" + recordId + "'";
		try {
			SmartDMO dmo = new SmartDMO();
			Object[] results = dmo.selectBy2(sql);
			if (results != null
					&& results.length > 0
					&& results[0] != null
					&& ((Object[]) results[0])[0] != null) {
				value = ((Object[]) results[0])[0].toString();
			}

		} catch (Exception e) {
			SCMEnv.error(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return value;

	}

	/**
	 * 经支持单id的ts校验
	 * 
	 * @param orderId
	 * @param ts
	 * @throws Exception
	 */
	public void sychronizeById(String[] orderId, String ts) throws Exception {
		String newTs = queryHeadColumn("ts", orderId[0]);
		if (!ts.equals(newTs)) {
			throw new BusinessException("销售订单已被更新，请稍刷新后再试");
		}

	}

	private void reWritePO(String[] poID, boolean isReferred, String orderCode, String[] ts, String operatorId)
			throws Exception {
		String opid = InvocationInfoProxy.getInstance().getUserCode();

		nc.itf.po.IOrder bo = (nc.itf.po.IOrder) nc.bs.framework.common.NCLocator.getInstance().lookup(
				nc.itf.po.IOrder.class.getName());
		bo.updateCoopFlag(isReferred, poID, orderCode, ts, opid);

	}

	protected void handleException(Exception e)throws BusinessException{
		SCMEnv.out(e);
		if(e instanceof BusinessException)
			throw (BusinessException)e;
		else
			throw new BusinessException(e);
	}
	/**
	 * 方法功能描述：订单修改时在传CRM前填充销售订单表体信息。
	 * <b>参数说明</b>
	 * @param saleorder
	 * @throws BusinessException
	 * @author 
	 * @time 2009-7-7 上午09:32:28
	 */
	public void fillBodyDataWhenModify(SaleOrderVO saleorder) throws BusinessException{
		//参数合法性校验
		if(null == saleorder || null == saleorder.getHeadVO())
			return ;
		//根据订单主表主键查询所有的子表信息
		String pk_saleorder = saleorder.getHeadVO().getPrimaryKey();	
		saleorder.setChildrenVO(queryAllBodyData(pk_saleorder));
	}
	/**
	 * 
	 * 父类方法重写
	 * 
	 * @see nc.itf.scm.crm.ICRMService#CRMQueryBillByID(java.lang.String)
	 * 
	 * 实现CRM服务接口，根据单据ID查询单据VO，用于预警同步超时数据
	 */
	public AggregatedValueObject CRMQueryBillByID(String billid) throws BusinessException {
		SaleOrderVO saleorder = null;
		try{
			saleorder =  queryData(billid);
			//如果是删除时超时，查询不到对应的销售订单数据
			if(null == saleorder || null == saleorder.getHeadVO()){
				saleorder = new SaleOrderVO();
				SaleorderHVO head = new SaleorderHVO(billid);
				saleorder.setParentVO(head);
			}
				
		}catch(Exception e){
			throw new BusinessException(e);
		}
		return saleorder;
	}
	/**
	 * 
	 * 父类方法重写
	 * 
	 * @see nc.itf.scm.crm.ICRMService#synchronizeSCM(nc.vo.pub.AggregatedValueObject, int)
	 * 
	 * 实现CRM服务接口，根据单据VO同步数据到CRM系统
	 */
	public void synchronizeSCM(AggregatedValueObject scmvo, int crmaction) throws BusinessException {
		try{
		SaleOrderVO saleorder = (SaleOrderVO)scmvo;
		new SaleToCRM().synchronizeSO(saleorder,crmaction);
		}catch(Exception e){
			throw new BusinessException(e);
		}
	}
	
	//wanglei  2014-05-14 计算预分配费用处理
	private boolean reCalcateInvFee(SaleOrderVO ordvo) throws Exception{
		UFDouble nSumFeeMny = UFDouble.ZERO_DBL; //wanglei 2014-05-14 重新计算预分配费用
		UFDouble nSumInvNum = UFDouble.ZERO_DBL; 
		SaleorderBVO[] ordbvos = (SaleorderBVO[]) (ordvo.getAllSaleOrderVO()==null? ordvo.getChildrenVO(): ordvo.getAllSaleOrderVO().getChildrenVO());
		for (SaleorderBVO ordbvo : ordbvos) {
			if(ordbvo.getLaborflag().booleanValue() && ordbvo.getBlargessflag().booleanValue())
				nSumFeeMny = nSumFeeMny.add(ordbvo.getNsummny());
			//else   yiming 2014-5-16 重量合计的条件为非费用即可，注释掉，调整如下
			//	nSumInvNum = nSumInvNum.add(ordbvo.getNnumber());
			if(!ordbvo.getLaborflag().booleanValue())  //yiming 2014-5-16 重量合计的条件为非费用即可
			    nSumInvNum = nSumInvNum.add(ordbvo.getNnumber());
		}

		//wanglei 2014-05-14 重新计算预分配费用
		if (!nSumFeeMny.equals(UFDouble.ZERO_DBL) &&
				!nSumInvNum.equals(UFDouble.ZERO_DBL)){
			UFDouble nMnyBal = nSumFeeMny;  //记录下余额初始值
			UFDouble nNumBal = nSumInvNum;  //记录数量余额初始值
			UFDouble nFeeMny = UFDouble.ZERO_DBL;
			
//			BusinessCurrencyRateUtil currtype = new BusinessCurrencyRateUtil(ordvo.getHeadVO().getPk_corp());
//			nc.vo.bd.b21.CurrinfoVO currVO = currtype.getCurrinfoVO(ordvo.getHeadVO().getCcurrencytypeid(), null);
//			// v30取业务精度
//			int digit = currVO.getCurrdigit() == null ? 4 : currVO.getCurrdigit().intValue();
			int digit = 2;
			for (SaleorderBVO ordbvo : ordbvos) {
				//if(ordbvo.getLaborflag().booleanValue() && ordbvo.getBlargessflag().booleanValue()) {  yiming 2014-5-16 非费用存货行跳过费用金额分配，条件修改如下
				if(ordbvo.getLaborflag().booleanValue()) {  // yiming 2014-5-16 非费用存货行跳过费用金额分配
//					ordbvo.setStatus(VOStatus.UPDATED);
					continue;
				}
				nFeeMny = nNumBal.doubleValue() > ordbvo.getNnumber().doubleValue() ? 
						ordbvo.getNnumber().multiply(nMnyBal.div(nNumBal)).setScale(digit, UFDouble.ROUND_HALF_UP):nMnyBal;
				ordbvo.setAttributeValue("ndistfeemny",nFeeMny);
				nMnyBal = nMnyBal.sub(nFeeMny);
				nNumBal = nNumBal.sub(ordbvo.getNnumber());
				if(ordbvo.getStatus() == VOStatus.UNCHANGED )
					ordbvo.setStatus(VOStatus.UPDATED) ;
//				else
//					ordbvo.setStatus(VOStatus.UPDATED);
			}
		}
		else {
			return true;
		}
		
		ArrayList<SaleorderBVO> albvos= new ArrayList<SaleorderBVO>();
		
		SaleorderBVO[]  ordbvos1 = (SaleorderBVO[]) ordvo.getChildrenVO();
		for (SaleorderBVO ordbvo : ordbvos1){
			if (ordbvo.getStatus() == VOStatus.DELETED )
				albvos.add(ordbvo);
		}
		
		for (SaleorderBVO ordbvo : ordbvos){
				albvos.add(ordbvo);
		}
		
		SaleorderBVO[] retvos = new SaleorderBVO[albvos.size()];
		ordvo.setChildrenVO(albvos.toArray(retvos));
		return true;
	}
}
