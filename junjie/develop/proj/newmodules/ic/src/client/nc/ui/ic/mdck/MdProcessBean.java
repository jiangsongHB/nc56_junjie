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
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.ic.md.CargInfVO;
import nc.vo.ic.md.MdcrkTempVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.SpecialBillItemVO;
import nc.vo.ic.pub.locator.LocatorVO;
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
	public MdcrkVO[] buliderMdcrkVOs(MdcrkVO[] vos, ChInfoVO infoVO,
			boolean b_fjs) throws BusinessException {
		if (vos == null || vos.length == 0)
			return null;
		List tempList = new ArrayList();
		List jbhList = new ArrayList();
		String jbh = null;
		UFDouble sumCkzs = new UFDouble(0, 2);
		for (int i = 0; i < vos.length; i++) {
			jbh = vos[i].getJbh();
			String numAndSpace=jbh+vos[i].getCspaceid();//2010-12-04 MeiChao 件编号及货位的复合体用以判断是否重复
			if (jbh == null || jbh.equals(""))
				throw new BusinessException("表体" + i + "件编号不允许为空！");
			if(vos[i].getCspaceid()==null||"".equals(vos[i].getCspaceid())){ //2010-12-04 MeiChao 添加货位不允许为空
				throw new BusinessException("表体" + i + "货位不允许为空！");
			}
			if (jbhList.contains(numAndSpace))//2010-12-04 MeiChao 添加件编号+货位组合不允许重复
				throw new BusinessException("表体件编号" + jbh + "及货位"+vos[i].getCspaceid()+"不允许重复！");
			else
				jbhList.add(numAndSpace);
			if (vos[i].getSrkzs() == null
					|| vos[i].getSrkzs().doubleValue() == 0)
				throw new BusinessException("表体件编号" + jbh + "的出库支数不能为0");
			if (infoVO.getSfbj().booleanValue() == false)
				if (vos[i].getSrkzl() == null
						|| vos[i].getSrkzl().doubleValue() == 0)
					throw new BusinessException("表体件编号" + jbh + "的出库重量不能为0");
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
//			//vos[i].setDef1(null);def1定义为钢厂重量,把原先的置空代码注释掉 2010-12-30 MeiChao
			vos[i].setDef2(null);
			vos[i].setDef3(null);
			vos[i].setSfbj(infoVO.getSfbj());
			vos[i].setDef4(new UFBoolean(b_fjs));// 非计算
			sumCkzs = sumCkzs.add(vos[i].getSrkzs());// 实出库支数
			tempList.add(vos[i]);
		}
		if (sumCkzs.doubleValue() != infoVO.getNoutassistnum().doubleValue())
			throw new BusinessException("码单出库总支数" + sumCkzs.doubleValue()
					+ "不等于实出库辅数量" + infoVO.getNoutassistnum().doubleValue());
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
		for (int j = 0; j < xclbvos.length; j++) {
			MdcrkVO crkvo = (MdcrkVO) crkvoMap.get(xclbvos[j].getPk_mdxcl_b());
			// 出库后的支数
			xclbvos[j].setZhishu(xclbvos[j].getZhishu().sub(crkvo.getSrkzs(),
					MDConstants.ZS_XSW));
			if (crkvo.getSrkzl() == null || crkvo.getSrkzl().doubleValue() == 0){
				xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().sub(
						new UFDouble(0), MDConstants.ZL_XSW));
				xclbvos[j].setDef1(xclbvos[j].getDef1().sub(
						new UFDouble(0), MDConstants.ZL_XSW));//2010-12-29 MeiChao 将钢厂重量也更新
			}else{
				// 重量
				xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().sub(
						crkvo.getSrkzl(), MDConstants.ZL_XSW));
				xclbvos[j].setDef1(xclbvos[j].getDef1().sub(
						crkvo.getDef1(), MDConstants.ZL_XSW));//2010-12-29 MeiChao 将钢厂重量也更新
			}
		}
		iVOPersistence.updateVOArray(xclbvos);
	}
	
	
	// 查询表体VOS
	public MdcrkVO[] queryCrkVOS(ChInfoVO infoVO) throws BusinessException {
		// 查询表体VOS
		boolean istemp=false;//是否查询的是出入库码单临时表(转库码单)
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Collection coll = iUAPQueryBS.retrieveByClause(MdcrkVO.class,
				" cgeneralbid='" + infoVO.getCgeneralbid() + "' and dr=0 ");
		if (coll == null || coll.size() == 0){//2010-12-04 MeiChao改造 如果查不到,那么便去nc_mdcrk_temp中查找
			coll = iUAPQueryBS.retrieveByClause(MdcrkTempVO.class,
					" cgeneralbid='" + infoVO.getCgeneralbid() + "' and dr=0 ");
			if(coll == null || coll.size() == 0){//如果仍旧为空,那么假定其为转库生成的其他出,去查转库的临时码单nc_mdcrk_temp
				Object sourcetype = iUAPQueryBS.executeQuery(
						"select t.csourcebillbid from ic_general_b t where t.cgeneralbid='"
								+ infoVO.getCgeneralbid()
								+ "' and t.cbodybilltypecode='4I'",
						new ColumnProcessor());
				
				coll = iUAPQueryBS.retrieveByClause(MdcrkTempVO.class,
						" cgeneralbid='" + (sourcetype==null?"梅超最帅了":sourcetype.toString()) + "' and dr=0 ");
				if(coll == null || coll.size() == 0)//如果依旧没找到码单信息,那么返回null
					return null;
			}
			istemp=true;
		}
		
		MdcrkVO[] rsvos = new MdcrkVO[coll.size()];
		
		if(istemp){
			MdcrkTempVO[] tempvos=new MdcrkTempVO[coll.size()];//将集合中的值传入TempVO再转化入正常的码单VO
			coll.toArray(tempvos);
			for(int i=0;i<tempvos.length;i++){
				String[] attributeNames=tempvos[i].getAttributeNames();
				MdcrkVO MdcrkTemp=new MdcrkVO();
				for(int j=0;j<attributeNames.length;j++){
					MdcrkTemp.setAttributeValue(attributeNames[j], tempvos[i].getAttributeValue(attributeNames[j]));
				}
				rsvos[i]=MdcrkTemp;
			}
		}else{
			coll.toArray(rsvos);
		}
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
		for (int j = 0; j < xclbvos.length; j++) {
			MdcrkVO crkvo = (MdcrkVO) crkvoMap.get(xclbvos[j].getPk_mdxcl_b());
			// 出库后的支数
			xclbvos[j].setZhishu(xclbvos[j].getZhishu().add(crkvo.getSrkzs(),
					MDConstants.ZS_XSW));
			// 重量
			xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().add(
					crkvo.getSrkzl(), MDConstants.ZL_XSW));
			//钢厂重量    add by ouyang---2011-02-24  在删除出库码单时,将钢厂重量也作重新入库处理.否则钢厂重量在出库维护完码单再清空码单后无法还原
			xclbvos[j].setDef1(xclbvos[j].getDef1().add(
					crkvo.getDef1(), MDConstants.ZL_XSW));

		}
		
		iVOPersistence.updateVOArray(xclbvos);
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
					.add(rsvos[i].getSrkzl(), MDConstants.ZL_XSW);//2011-01-03 MeiChao 取钢厂重量
//					.add(rsvos[i].getSrkzl(), MDConstants.ZL_XSW);
		}
		dlg.setNoutnum(zhongliang);
		dlg.setNoutassistnum(zhishu);
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
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
			vo.setSfbj(new UFBoolean(false)); // 是否磅计
			vo.setDef4(sdvos[i].getDef4());// 非计算
			vo.setSrkzl(sdvos[i].getDef3());// 锁定重量
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
		String sql = "select ttt.* from (select t1.PK_MDXCL_B, t1.dr, t1.cspaceid, "
				+ "t1.jbh, t1.md_width, t1.md_length, t1.md_meter, t1.md_note, t1.md_lph, t1.md_zyh,t1.def1,t1.def7,t1.def8,t1.def9,"//2011-01-03 MeiChao 加入def1 7 8 9 4个自定义字段
				+ " t1.md_zlzsh, t1.remark, t1.zhishu, (t1.zhishu-nvl(b.sdzs,0)) as kyzs, t1.zhongliang,nvl((t1.def1*(t1.zhishu-nvl(b.sdzs,0))/t1.zhishu),0) as factoryweight,"//2010-12-30 MeiChao add t1.def1*(t1.zhishu-nvl(b.sdzs,0))/t1.zhishu as factoryweight,
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
		UFDouble zhongliang = new UFDouble((BigDecimal) rsmap.get("zhongliang"));// 现存验收重量
		UFDouble factoryallweight=new UFDouble((BigDecimal) rsmap.get("def1"));// 现存钢厂重量
		UFDouble kyzs = new UFDouble((Integer) rsmap.get("kyzs")); // 可用支数
		UFDouble kyzl = kyzs.multiply(zhongliang).div(zhishu,
				MDConstants.ZL_XSW);// 可用重量
		UFDouble factoryWeight=new UFDouble(rsmap.get("factoryweight").toString());// 2010-12-30 MeiChao add 可用钢厂重量 
		evo.setCspaceid((String) rsmap.get("cspaceid"));// 货位
		// 如果是理计
		if (bsfbj.booleanValue() == false) {
			evo.setSrkzs(kyzs);
			evo.setSrkzl(kyzl);
			evo.setDef3(kyzs);//2010-12-30 MeiChao add 将临时支数(后续判断不能超出用)加入到def3字段中
			evo.setDef1(factoryWeight);//2010-12-30 MeiChao add 将钢厂重量加入到def1字段中
			evo.setDef2(kyzl);
		}
		// 磅计
		else {
			evo.setSrkzs(kyzs);
			evo.setSrkzl(new UFDouble(0));
			evo.setDef3(kyzs);//2010-12-30 MeiChao add 将临时支数(后续判断不能超出用)加入到def3字段中
			evo.setDef1(factoryWeight);//2010-12-30 MeiChao add 将钢厂重量加入到def1字段中
			evo.setDef2(kyzl);
		}
		return evo;
	}

	// 全部删除码单后，还原码货位表数据
	public boolean returnHw(GeneralBillItemVO itemVOa, String pk_corp,
			String billtype) throws BusinessException {

		String del_huowei = "delete ic_general_bb1 where cgeneralbid='"
				+ itemVOa.getCgeneralbid() + "'";

		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		try {
			iUAPQueryBS.executeQuery(del_huowei, new ArrayListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		CargInfVO CargInfVO = new CargInfVO();
		CargInfVO.setCgeneralbid(itemVOa.getCgeneralbid());
		CargInfVO.setCspaceid("_________N/A________");
		CargInfVO.setDr(0);
		CargInfVO.setPk_corp(pk_corp);
		if (billtype.equals("4I") || billtype.equals("4C")) {
			CargInfVO.setNoutspaceassistnum(itemVOa.getNoutassistnum()); // noutassistnum
			// 实出辅数量
			CargInfVO.setNoutspacenum(itemVOa.getNoutnum()); // noutnum 实出数量
		} else {
			CargInfVO.setNinspaceassistnum(itemVOa.getNinassistnum()); // ninassistnum
			// 实入辅数量
			CargInfVO.setNinspacenum(itemVOa.getNinnum()); // ninnum 实入数量
		}
		IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		ivo.insertVO(CargInfVO);
		return true;
	}

	// 构靠一个货位VOs
	public LocatorVO[] builderHwVos(GeneralBillItemVO itemVO, String billType,
			boolean sfth) throws BusinessException {
		LocatorVO[] rsvo = null;
		String sql = "select t2.cspaceid,t3.cscode,t3.csname,t1.cgeneralbid,t1.srkzs,t1.srkzl,t1.def1"
				+ " from nc_mdcrk t1 left join nc_mdxcl_b t2 on t1.pk_mdxcl_b=t2.pk_mdxcl_b"
				+ " left join bd_cargdoc t3 on t2.cspaceid=t3.pk_cargdoc where t1.cgeneralbid='"
				+ itemVO.getCgeneralbid() + "' and t1.dr=0";
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		List rsList = (List) iUAPQueryBS.executeQuery(sql,
				new MapListProcessor());
		if (rsList == null || rsList.size() == 0)
			return null;
		rsvo = new LocatorVO[rsList.size()];
		for (int i = 0; i < rsList.size(); i++) {
			LocatorVO vo = new LocatorVO();
			Map voMap = (Map) rsList.get(i);
			vo.setCspaceid((String) voMap.get("cspaceid")); // 货位PK值
			vo.setVspacecode((String) voMap.get("cscode")); // 货位编码
			vo.setVspacename((String) voMap.get("csname")); // 货位名称
			//add by ouyangzhb  当做出库操作的时候   把货位数量改为 出库验收重量 当做入库操作的时候 把 货位数量改为    入库钢厂重量
			UFDouble d1 = new UFDouble((BigDecimal) voMap.get("srkzl"));
			UFDouble d12 = new UFDouble((BigDecimal) voMap.get("def1"));
			UFDouble d2 = new UFDouble((BigDecimal) voMap.get("srkzs"));
			// 入库
			if (billType.equals("45") || billType.equals("4A")) {
				if (sfth == true) {
					vo.setNinspacenum(new UFDouble(-d12.doubleValue()));
					vo.setNinspaceassistnum(new UFDouble(-d2.doubleValue()));
					vo.setNingrossnum(null);
				} else {
					vo.setNinspacenum(d12);
					vo.setNinspaceassistnum(d2);
					vo.setNingrossnum(null);
				}
			}
			// 出库
			else {
				if (sfth == true) {
					vo.setNoutspacenum(new UFDouble(-d1.doubleValue()));
					vo.setNoutspaceassistnum(new UFDouble(-d2.doubleValue()));
					vo.setNoutgrossnum(null);
				} else {
					vo.setNoutspacenum(d1);
					vo.setNoutspaceassistnum(d2);
					vo.setNoutgrossnum(null);
				}

			}
			rsvo[i] = vo;
		}
		return rsvo;
	}

	// 判断是否码单维护
	public boolean querySfmdwf(String cinvbasid) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def20 from bd_invmandoc where pk_invbasdoc='"
				+ cinvbasid + "' and pk_corp='"+ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey()+"'"; //2010-12-27 MeiChao 是否码单维护,从存货管理档案中判断.
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("当前存货异常，在存货管理档案中不存在！");
		if (objs[0] == null)
			return false;
		else {
			String bz = (String) objs[0];
			bz = bz.toUpperCase();
			if (bz.equals("Y"))
				return true;
			else
				return false;
		}
	}
	
	/**
	 * 2010-12-09 MeiChao 
	 * 根据SpecialBillItemVO来构造货位,取数源为数据表nc_mdcrk_temp,
	 * 而builderHwVos()是根据GeneralBillItemVO来构造货位,取数源为nc_mdcrk表
	 * @param itemVO
	 * @param billType 单据类型
	 * @param sfth 是否退货
	 * @return
	 * @throws BusinessException
	 */
	// 构靠一个货位VOs
	public LocatorVO[] builderHwVos2(SpecialBillItemVO itemVO, String billType,
			boolean sfth) throws BusinessException {
		LocatorVO[] rsvo = null;
		String sql = "select t2.cspaceid,t3.cscode,t3.csname,t1.cgeneralbid,t1.srkzs,t1.def1"
				+ " from nc_mdcrk_temp t1 left join nc_mdxcl_b t2 on t1.pk_mdxcl_b=t2.pk_mdxcl_b"
				+ " left join bd_cargdoc t3 on t2.cspaceid=t3.pk_cargdoc where t1.cgeneralbid='"
				+ itemVO.getCgeneralbid() + "' and t1.dr=0";
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		List rsList = (List) iUAPQueryBS.executeQuery(sql,
				new MapListProcessor());
		if (rsList == null || rsList.size() == 0)
			return null;
		rsvo = new LocatorVO[rsList.size()];
		for (int i = 0; i < rsList.size(); i++) {
			LocatorVO vo = new LocatorVO();
			Map voMap = (Map) rsList.get(i);
			vo.setCspaceid((String) voMap.get("cspaceid")); // 货位PK值
			vo.setVspacecode((String) voMap.get("cscode")); // 货位编码
			vo.setVspacename((String) voMap.get("csname")); // 货位名称
			UFDouble d1 = new UFDouble((BigDecimal) voMap.get("def1"));
			UFDouble d2 = new UFDouble((BigDecimal) voMap.get("srkzs"));
			// 入库
			if (billType.equals("45") || billType.equals("4A")) {
				if (sfth == true) {
					vo.setNinspacenum(new UFDouble(-d1.doubleValue()));
					vo.setNinspaceassistnum(new UFDouble(-d2.doubleValue()));
					vo.setNingrossnum(null);
				} else {
					vo.setNinspacenum(d1);
					vo.setNinspaceassistnum(d2);
					vo.setNingrossnum(null);
				}
			}
			// 出库
			else {
				if (sfth == true) {
					vo.setNoutspacenum(new UFDouble(-d1.doubleValue()));
					vo.setNoutspaceassistnum(new UFDouble(-d2.doubleValue()));
					vo.setNoutgrossnum(null);
				} else {
					vo.setNoutspacenum(d1);
					vo.setNoutspaceassistnum(d2);
					vo.setNoutgrossnum(null);
				}

			}
			rsvo[i] = vo;
		}
		return rsvo;
	}
	
	
	
	
}
