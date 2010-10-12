package nc.ui.ic.mdck;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.sd.MdsdVO;
import nc.vo.ic.xcl.MdxclBVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 码单业务逻辑处理Bean
 * 
 * @author heyq
 * 
 */

public class MdProcessBean {

	// 构造保存VO
	public MdcrkVO[] buliderMdcrkVOs(MdcrkVO[] vos, ChInfoVO infoVO)
			throws BusinessException {
		if (vos == null || vos.length == 0)
			return null;
		List tempList = new ArrayList();
		List jbhList = new ArrayList();
		String jbh = null;
		for (int i = 0; i < vos.length; i++) {
			jbh = vos[i].getJbh();
			if (jbh == null || jbh.equals(""))
				continue;
			if (jbhList.contains(jbh))
				throw new BusinessException("表体键编号" + jbh + "不能重复！");
			else
				jbhList.add(jbh);
			if (vos[i].getSrkzs() == null
					|| vos[i].getSrkzs().doubleValue() == 0)
				throw new BusinessException("表体键编号" + jbh + "的出库支数不能为0");
			vos[i].setPk_corp(infoVO.getCorpVo().getPk_corp()); // 公司
			vos[i].setDr(0);
			vos[i].setDmakedate(infoVO.getUfdate());// 操作日期；
			vos[i].setVoperatorid(infoVO.getUserVo().getPrimaryKey());// 操作员
			vos[i].setCcalbodyidb(infoVO.getCcalbodyidb());// 库存组织
			vos[i].setCwarehouseidb(infoVO.getCwarehouseidb());// /仓库PK
			vos[i].setCbodybilltypecode(infoVO.getCbodybilltypecode());// 单据类型
			vos[i].setCrkfx(1);// 出入库方向
			vos[i].setDjfx(0);// 单据方向
			vos[i].setCgeneralbid(infoVO.getCgeneralbid());// 出入库单表体PK
			vos[i].setDef1(null);
			vos[i].setDef2(null);
			vos[i].setSfbj(infoVO.getSfbj());
			tempList.add(vos[i]);
		}
		if (tempList.size() == 0)
			return null;
		MdcrkVO[] rs = new MdcrkVO[tempList.size()];
		tempList.toArray(rs);
		return rs;
	}

	// 构造并更新现存量主子表
	public void updateXcl(MdcrkVO[] vos) throws BusinessException {
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// 查询表体VOS
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Map crkvoMap = new HashMap();
		String pk_mdxclStr = " pk_mdxcl_b in (";
		for (int i = 0; i < vos.length; i++) {
			pk_mdxclStr += "'" + vos[i].getPk_mdxcl_b() + "',";
			crkvoMap.put(vos[i].getPk_mdxcl_b(), vos[i]);
		}
		pk_mdxclStr = pk_mdxclStr.substring(0, pk_mdxclStr.length() - 1);
		pk_mdxclStr = pk_mdxclStr + ") and dr=0";
		Collection coll = iUAPQueryBS.retrieveByClause(MdxclBVO.class,
				pk_mdxclStr);
		MdxclBVO[] xclbvos = new MdxclBVO[coll.size()];
		coll.toArray(xclbvos);
		// String pk_mdxcl = null;
		// UFDouble sum_zhishu = new UFDouble(0);
		// UFDouble sum_zhongliang = new UFDouble(0);
		for (int j = 0; j < xclbvos.length; j++) {
			// pk_mdxcl = xclbvos[0].getPk_mdxcl();// 现存量表头PK
			MdcrkVO crkvo = (MdcrkVO) crkvoMap.get(xclbvos[j].getPk_mdxcl_b());
			// 出库后的支数
			xclbvos[j].setZhishu(xclbvos[j].getZhishu().sub(crkvo.getSrkzs(),
					MDConstants.ZS_XSW));
			if (crkvo.getSrkzl() == null || crkvo.getSrkzl().doubleValue() == 0)
				xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().sub(
						new UFDouble(0), MDConstants.ZL_XSW));
			else
				// 重量
				xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().sub(
						crkvo.getSrkzl(), MDConstants.ZL_XSW));
			// sum_zhishu = sum_zhishu.add(crkvo.getSrkzs(),
			// MDConstants.ZS_XSW);
			// sum_zhongliang = sum_zhongliang.add(crkvo.getSrkzl(),
			// MDConstants.ZL_XSW);
		}
		iVOPersistence.updateVOArray(xclbvos);
		// 表头VO
		// MdxclVO hvo = (MdxclVO) iUAPQueryBS.retrieveByPK(MdxclVO.class,
		// pk_mdxcl);
		// hvo.setSum_zhishu(hvo.getSum_zhishu().sub(sum_zhishu,
		// MDConstants.ZS_XSW));
		// hvo.setSum_zhongliang(hvo.getSum_zhongliang().sub(sum_zhongliang,
		// MDConstants.ZL_XSW));
		// iVOPersistence.updateVO(hvo);
	}

	// 查询表体VOS
	public MdcrkVO[] queryCrkVOS(ChInfoVO infoVO) throws BusinessException {
		// 查询表体VOS
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Collection coll = iUAPQueryBS.retrieveByClause(MdcrkVO.class,
				" cgeneralbid='" + infoVO.getCgeneralbid() + "' and dr=0 ");
		if (coll == null || coll.size() == 0)
			return null;

		MdcrkVO[] rsvos = new MdcrkVO[coll.size()];
		coll.toArray(rsvos);
		/*
		 * // 查询现存量表的支数与重量 Map crkvoMap = new HashMap(); String pk_mdxclStr = "
		 * pk_mdxcl_b in ("; for (int i = 0; i < rsvos.length; i++) {
		 * pk_mdxclStr += "'" + rsvos[i].getPk_mdxcl_b() + "',"; } pk_mdxclStr =
		 * pk_mdxclStr.substring(0, pk_mdxclStr.length() - 1); pk_mdxclStr =
		 * pk_mdxclStr + ") and dr=0"; Collection coll2 =
		 * iUAPQueryBS.retrieveByClause(MdxclBVO.class, pk_mdxclStr); MdxclBVO[]
		 * xclbvos = new MdxclBVO[coll.size()]; coll2.toArray(xclbvos); for (int
		 * j = 0; j < xclbvos.length; j++)
		 * crkvoMap.put(xclbvos[j].getPk_mdxcl_b(), xclbvos[j]); for (int t = 0;
		 * t < rsvos.length; t++) { String pk_mdxcl_b =
		 * rsvos[t].getPk_mdxcl_b(); MdxclBVO xclvo = (MdxclBVO)
		 * crkvoMap.get(pk_mdxcl_b);
		 * rsvos[t].setDef1(rsvos[t].getSrkzs().add(xclvo.getZhishu(),
		 * MDConstants.ZS_XSW));
		 * rsvos[t].setDef2(rsvos[t].getSrkzl().add(xclvo.getZhongliang(),
		 * MDConstants.ZL_XSW)); }
		 */
		return rsvos;
	}

	// 删除码单出入库单信息，并对原有单据做入库处理
	public boolean deleteAndRK(ChInfoVO infoVO) throws BusinessException {
		// 查询表体VOS
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// 历史出库VO
		MdcrkVO[] lsckVos = queryCrkVOS(infoVO);
		if (lsckVos == null || lsckVos.length == 0)
			return false;
		// 作出库操作。
		Map crkvoMap = new HashMap();
		String pk_mdxclStr = " pk_mdxcl_b in (";
		for (int i = 0; i < lsckVos.length; i++) {
			pk_mdxclStr += "'" + lsckVos[i].getPk_mdxcl_b() + "',";
			crkvoMap.put(lsckVos[i].getPk_mdxcl_b(), lsckVos[i]);
		}
		pk_mdxclStr = pk_mdxclStr.substring(0, pk_mdxclStr.length() - 1);
		pk_mdxclStr = pk_mdxclStr + ") and dr=0";
		Collection coll = iUAPQueryBS.retrieveByClause(MdxclBVO.class,
				pk_mdxclStr);
		MdxclBVO[] xclbvos = new MdxclBVO[coll.size()];
		coll.toArray(xclbvos);
		// String pk_mdxcl = null;
		// UFDouble sum_zhishu = new UFDouble(0);
		// UFDouble sum_zhongliang = new UFDouble(0);
		for (int j = 0; j < xclbvos.length; j++) {
			// pk_mdxcl = xclbvos[0].getPk_mdxcl();// 现存量表头PK
			MdcrkVO crkvo = (MdcrkVO) crkvoMap.get(xclbvos[j].getPk_mdxcl_b());
			// 出库后的支数
			xclbvos[j].setZhishu(xclbvos[j].getZhishu().add(crkvo.getSrkzs(),
					MDConstants.ZS_XSW));
			// 重量
			xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().add(
					crkvo.getSrkzl(), MDConstants.ZL_XSW));
			// sum_zhishu = sum_zhishu.add(crkvo.getSrkzs(),
			// MDConstants.ZS_XSW);
			// sum_zhongliang = sum_zhongliang.add(crkvo.getSrkzl(),
			// MDConstants.ZL_XSW);
		}
		iVOPersistence.updateVOArray(xclbvos);
		// 表头VO
		// MdxclVO hvo = (MdxclVO) iUAPQueryBS.retrieveByPK(MdxclVO.class,
		// pk_mdxcl);
		// hvo.setSum_zhishu(hvo.getSum_zhishu().add(sum_zhishu,
		// MDConstants.ZL_XSW));
		// hvo.setSum_zhongliang(hvo.getSum_zhongliang().add(sum_zhongliang,
		// MDConstants.ZS_XSW));
		// iVOPersistence.updateVO(hvo);
		// 删除历史出库VO
		iVOPersistence.deleteVOArray(lsckVos);
		return true;
	}

	// 更新出入库单数据
	public void updateBill(MdwhDlg dlg, MdcrkVO[] rsvos)
			throws BusinessException {
		UFDouble zhishu = new UFDouble(0);
		UFDouble zhongliang = new UFDouble(0);
		String pk_ptzj = null;
		for (int i = 0; i < rsvos.length; i++) {
			pk_ptzj = rsvos[0].getCgeneralbid();
			zhishu = zhishu.add(rsvos[i].getSrkzs(), MDConstants.ZS_XSW);
			zhongliang = zhongliang
					.add(rsvos[i].getSrkzl(), MDConstants.ZL_XSW);
		}
		dlg.setNoutnum(zhongliang);
		dlg.setNoutassistnum(zhishu);
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			iUAPQueryBS.executeQuery("update ic_general_b set noutnum="
					+ zhongliang.doubleValue() + ",noutassistnum="
					+ zhishu.doubleValue() + " where cgeneralbid='" + pk_ptzj
					+ "'", new ArrayListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	// 码单全部删除后，将实出库支数、重量清空
	public void updateBillNull(ChInfoVO infoVO) {
		String pk_ptzj = infoVO.getCgeneralbid();
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			iUAPQueryBS.executeQuery(
					"update ic_general_b set noutnum=null,noutassistnum=null where cgeneralbid='"
							+ pk_ptzj + "'", new ArrayListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	// 查询销售订单锁定的数据
	public MdcrkVO[] queryCrkVOSByXsdd(ChInfoVO infoVO, MdwhDlg dlg)
			throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Collection coll = iUAPQueryBS
				.retrieveByClause(
						MdsdVO.class,
						" xsddbt_pk='"
								+ infoVO.getLydjh()
								+ "' and dr=0 and sxrq>=(select to_char(sysdate,'yyyy-MM-dd')from dual)");
		if (coll == null || coll.size() == 0)
			return null;
		MdsdVO[] sdvos = new MdsdVO[coll.size()];
		coll.toArray(sdvos);
		List yxsdList = new ArrayList();
		boolean tt = false;
		for (int i = 0; i < sdvos.length; i++) {
			MdcrkVO vo = new MdcrkVO();
			vo.setPk_mdxcl_b(sdvos[i].getPk_mdxcl_b());// 出入库单主键
			vo.setSrkzs(sdvos[i].getSdzs());// 支数
			vo.setSfbj(new UFBoolean(true)); // 是否磅计

			// 现存量表头VO
			MdxclBVO bvo = (MdxclBVO) iUAPQueryBS.retrieveByPK(MdxclBVO.class,
					vo.getPk_mdxcl_b());
			// 现存量表体VO
			MdxclVO hvo = (MdxclVO) iUAPQueryBS.retrieveByPK(MdxclVO.class, bvo
					.getPk_mdxcl());
			if (!hvo.getCwarehouseidb().equals(infoVO.getCwarehouseidb())) {
				tt = true;
				continue;
			}
			yxsdList.add(vo);
		}
		if (tt == true)
			MessageDialog.showWarningDlg(dlg, "警告",
					"被锁定码单的仓库与实际出库仓库不一致，部分或全部码单锁定记录加载失败，请调整仓库！");
		MdcrkVO[] rsVO = new MdcrkVO[yxsdList.size()];
		yxsdList.toArray(rsVO);
		return rsVO;
	}

	public void updateSdbs(ChInfoVO infoVO, String status)
			throws BusinessException {
		if (infoVO.getLydjh() == null)
			return;
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			iUAPQueryBS.executeQuery("update nc_mdsd set sfsx='" + status
					+ "' where xsddbt_pk='" + infoVO.getLydjh() + "'",
					new ArrayListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	// 查询出出入库单可用量
	public MdcrkVO queryMdCrkKyl(MdcrkVO evo, UFBoolean bsfbj)
			throws BusinessException {
		String pk_mdxcl_b = evo.getPk_mdxcl_b();
		String sql = "select ttt.* from (select t1.PK_MDXCL_B, t1.dr, t4.cscode as cspaceid, "
				+ "t1.jbh, t1.md_width, t1.md_length, t1.md_meter, t1.md_note, t1.md_lph, t1.md_zyh,"
				+ " t1.md_zlzsh, t1.remark, t1.zhishu, (t1.zhishu-nvl(b.sdzs,0)) as kyzs, t1.zhongliang,"
				+ " t2.pk_corp, t2.cwarehouseidb, t2.ccalbodyidb, t2.cinvbasid, t2.cinventoryidb, t3.invspec "
				+ "from nc_mdxcl_b t1 left join nc_mdxcl t2 on t1.pk_mdxcl = t2.pk_mdxcl"
				+ " left join bd_invbasdoc t3 on t2.cinvbasid = t3.pk_invbasdoc "
				+ "left join bd_cargdoc t4 on t1.cspaceid = t4.pk_cargdoc"
				+ " left join (select pk_mdxcl_b, sum(to_number(sdzs)) as sdzs"
				+ " from nc_mdsd left join so_saleorder_b on so_saleorder_b.corder_bid= nc_mdsd.xsddbt_pk"
				+ " where sfsx='0' and so_saleorder_b.dr=0 and to_date(sxrq, 'yyyy-mm-dd') > sysdate group by pk_mdxcl_b) b on b.pk_mdxcl_b=t1.PK_MDXCL_B) ttt"
				+ " where ttt.dr=0 and ttt.kyzs>0 and ttt.pk_mdxcl_b='"
				+ pk_mdxcl_b + "' and ttt.dr=0";
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		HashMap rsmap = (HashMap) iUAPQueryBS.executeQuery(sql,
				new MapProcessor());
		if (rsmap == null) {
			evo.setDef1(new UFDouble(0));
			evo.setDef2(new UFDouble(0));
			return evo;
		}
		UFDouble zhishu = new UFDouble((BigDecimal) rsmap.get("zhishu"));// 现存支数
		UFDouble zhongliang = new UFDouble((BigDecimal) rsmap.get("zhongliang"));// 现存重量
		UFDouble kyzs = new UFDouble((Integer) rsmap.get("kyzs")); // 可用支数
		UFDouble kyzl = kyzs.multiply(zhongliang).div(zhishu,
				MDConstants.ZL_XSW);// 可用重量
		// 如果是理计
		if (bsfbj.booleanValue() == false) {
			evo.setSrkzs(kyzs);
			evo.setSrkzl(kyzl);
			evo.setDef1(kyzs);
			evo.setDef2(kyzl);
		}
		// 磅计
		else {
			evo.setSrkzs(kyzs);
			evo.setSrkzl(new UFDouble(0));
			evo.setDef1(kyzs);
			evo.setDef2(kyzl);
		}
		return evo;
	}

}
