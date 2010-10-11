package nc.ui.so.so001.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ic.mdck.MDConstants;
import nc.vo.ic.sd.MdsdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * 码单锁定业务处理类
 * 
 * @author heyq
 * 
 */
public class MdsdBean {

	// 查询锁定的VOS
	public MdsdVO[] querySdVOS(SaleorderBVO bvo) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Collection coll = iUAPQueryBS.retrieveByClause(MdsdVO.class,
				" xsddbt_pk='" + bvo.getCorder_bid() + "' and dr=0");
		if (coll == null || coll.size() == 0)
			return null;
		MdsdVO[] rvos = new MdsdVO[coll.size()];
		coll.toArray(rvos);
		return rvos;
	}

	// 删除码单锁定历史数据
	public boolean deleteAndRK(SaleorderBVO bvo) throws BusinessException {
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		iVOPersistence.deleteByClause(MdsdVO.class, " xsddbt_pk='"
				+ bvo.getCorder_bid() + "' and dr=0");
		return true;
	}

	// 重新构造MDsdVO
	public MdsdVO[] buliderMdcrkVOs(MdsdVO[] vos, SaleorderHVO hvo,
			SaleorderBVO bvo) throws BusinessException {
		List pk_mdxcl_bList = new ArrayList();
		List rsList = new ArrayList();
		String pk_mdxcl_b = null;
		for (int i = 0; i < vos.length; i++) {
			pk_mdxcl_b = vos[i].getPk_mdxcl_b();
			if (pk_mdxcl_b == null || pk_mdxcl_b.equals(""))
				continue;
			if (pk_mdxcl_bList.contains(pk_mdxcl_b))
				throw new BusinessException("第" + (i + 1) + "行,表体键编号不能重复！");
			else
				pk_mdxcl_bList.add(pk_mdxcl_b);
			if (vos[i].getSxrq() == null)
				throw new BusinessException("第" + (i + 1) + "行,失效日期不能为空！");
			vos[i].setPk_corp(hvo.getPk_corp());// 公司
			vos[i].setXsddbt_pk(bvo.getCorder_bid());// 主建
			vos[i].setSfsx("0");// 是否生效
			vos[i].setDmakedate(hvo.getDmakedate());// dmakedate 制单日期
			vos[i].setVoperatorid(hvo.getCoperatorid());// voperatorid 制单人
			vos[i].setDr(new Integer(0));
			vos[i].setDef6(null);
			vos[i].setDef1(null);
			rsList.add(vos[i]);
		}
		if (rsList.size() == 0)
			return null;
		MdsdVO[] rsvoArrays = new MdsdVO[rsList.size()];
		rsList.toArray(rsvoArrays);
		return rsvoArrays;
	}

	// 根据编辑后的VO，查询出可用量
	public MdsdVO queryKyl(MdsdVO vo, UFDate cdate) throws BusinessException {
		String pk_mdxcl_b = vo.getPk_mdxcl_b();
		if (pk_mdxcl_b == null)
			return null;
		String sql = "select ttt.* from (select t1.PK_MDXCL_B, t1.dr, t4.cscode as cspaceid, "
				+ "t1.jbh, t1.md_width, t1.md_length, t1.md_meter, t1.md_note, t1.md_lph, t1.md_zyh,"
				+ " t1.md_zlzsh, t1.remark, t1.zhishu, (t1.zhishu-nvl(b.sdzs,0)) as kyzs, t1.zhongliang,"
				+ " t2.pk_corp, t2.cwarehouseidb, t2.ccalbodyidb, t2.cinvbasid, t2.cinventoryidb, t3.invspec "
				+ "from nc_mdxcl_b t1 left join nc_mdxcl t2 on t1.pk_mdxcl = t2.pk_mdxcl"
				+ " left join bd_invbasdoc t3 on t2.cinvbasid = t3.pk_invbasdoc "
				+ "left join bd_cargdoc t4 on t1.cspaceid = t4.pk_cargdoc"
				+ " left join (select pk_mdxcl_b, sum(to_number(sdzs)) as sdzs"
				+ " from nc_mdsd left join so_saleorder_b on so_saleorder_b.corder_bid= nc_mdsd.xsddbt_pk where sfsx='0' and so_saleorder_b.dr=0 and to_date(sxrq, 'yyyy-mm-dd') > sysdate group by pk_mdxcl_b) b on b.pk_mdxcl_b=t1.PK_MDXCL_B) ttt"
				+ " where ttt.dr=0 and ttt.kyzs>0 and ttt.pk_mdxcl_b='"
				+ pk_mdxcl_b + "' and ttt.dr=0";

		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		HashMap rsmap = (HashMap) iUAPQueryBS.executeQuery(sql,
				new MapProcessor());
		if (rsmap == null) {
			vo.setDef1(new UFDouble(0));
			vo.setDef2(new UFDouble(0));
			return vo;
		}
		Integer kyzs = (Integer) rsmap.get("kyzs");
		vo.setSdzs(new UFDouble(kyzs, MDConstants.ZS_XSW));
		vo.setDef1(vo.getSdzs());
		vo.setSxrq(cdate.getDateAfter(3));// 失效日期
		vo.setSdrq(cdate);// 锁定日期
		return vo;
	}
}
