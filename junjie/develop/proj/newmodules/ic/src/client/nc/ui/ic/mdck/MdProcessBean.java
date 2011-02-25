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
 * �뵥ҵ���߼�����Bean
 * 
 * @author heyq
 * 
 */

public class MdProcessBean {

	// ���챣��VO
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
			String numAndSpace=jbh+vos[i].getCspaceid();//2010-12-04 MeiChao ����ż���λ�ĸ����������ж��Ƿ��ظ�
			if (jbh == null || jbh.equals(""))
				throw new BusinessException("����" + i + "����Ų�����Ϊ�գ�");
			if(vos[i].getCspaceid()==null||"".equals(vos[i].getCspaceid())){ //2010-12-04 MeiChao ��ӻ�λ������Ϊ��
				throw new BusinessException("����" + i + "��λ������Ϊ�գ�");
			}
			if (jbhList.contains(numAndSpace))//2010-12-04 MeiChao ��Ӽ����+��λ��ϲ������ظ�
				throw new BusinessException("��������" + jbh + "����λ"+vos[i].getCspaceid()+"�������ظ���");
			else
				jbhList.add(numAndSpace);
			if (vos[i].getSrkzs() == null
					|| vos[i].getSrkzs().doubleValue() == 0)
				throw new BusinessException("��������" + jbh + "�ĳ���֧������Ϊ0");
			if (infoVO.getSfbj().booleanValue() == false)
				if (vos[i].getSrkzl() == null
						|| vos[i].getSrkzl().doubleValue() == 0)
					throw new BusinessException("��������" + jbh + "�ĳ�����������Ϊ0");
			vos[i].setPk_corp(infoVO.getCorpVo().getPk_corp()); // ��˾
			vos[i].setDr(0);
			vos[i].setDmakedate(infoVO.getUfdate());// �������ڣ�
			vos[i].setVoperatorid(infoVO.getUserVo().getPrimaryKey());// ����Ա
			vos[i].setCcalbodyidb(infoVO.getCcalbodyidb());// �����֯
			vos[i].setCwarehouseidb(infoVO.getCwarehouseidb());// /�ֿ�PK
			vos[i].setCbodybilltypecode(infoVO.getCbodybilltypecode());// ��������
			vos[i].setCrkfx(1);// ����ⷽ��
			vos[i].setDjfx(0);// ���ݷ���
			vos[i].setCgeneralbid(infoVO.getCgeneralbid());// ����ⵥ����PK
//			//vos[i].setDef1(null);def1����Ϊ�ֳ�����,��ԭ�ȵ��ÿմ���ע�͵� 2010-12-30 MeiChao
			vos[i].setDef2(null);
			vos[i].setDef3(null);
			vos[i].setSfbj(infoVO.getSfbj());
			vos[i].setDef4(new UFBoolean(b_fjs));// �Ǽ���
			sumCkzs = sumCkzs.add(vos[i].getSrkzs());// ʵ����֧��
			tempList.add(vos[i]);
		}
		if (sumCkzs.doubleValue() != infoVO.getNoutassistnum().doubleValue())
			throw new BusinessException("�뵥������֧��" + sumCkzs.doubleValue()
					+ "������ʵ���⸨����" + infoVO.getNoutassistnum().doubleValue());
		if (tempList.size() == 0)
			return null;
		MdcrkVO[] rs = new MdcrkVO[tempList.size()];
		tempList.toArray(rs);
		return rs;
	}

	// ���첢�����ִ������ӱ�
	public void updateXcl(MdcrkVO[] vos) throws BusinessException {
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// ��ѯ����VOS
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
			// ������֧��
			xclbvos[j].setZhishu(xclbvos[j].getZhishu().sub(crkvo.getSrkzs(),
					MDConstants.ZS_XSW));
			if (crkvo.getSrkzl() == null || crkvo.getSrkzl().doubleValue() == 0){
				xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().sub(
						new UFDouble(0), MDConstants.ZL_XSW));
				xclbvos[j].setDef1(xclbvos[j].getDef1().sub(
						new UFDouble(0), MDConstants.ZL_XSW));//2010-12-29 MeiChao ���ֳ�����Ҳ����
			}else{
				// ����
				xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().sub(
						crkvo.getSrkzl(), MDConstants.ZL_XSW));
				xclbvos[j].setDef1(xclbvos[j].getDef1().sub(
						crkvo.getDef1(), MDConstants.ZL_XSW));//2010-12-29 MeiChao ���ֳ�����Ҳ����
			}
		}
		iVOPersistence.updateVOArray(xclbvos);
	}
	
	
	// ��ѯ����VOS
	public MdcrkVO[] queryCrkVOS(ChInfoVO infoVO) throws BusinessException {
		// ��ѯ����VOS
		boolean istemp=false;//�Ƿ��ѯ���ǳ�����뵥��ʱ��(ת���뵥)
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Collection coll = iUAPQueryBS.retrieveByClause(MdcrkVO.class,
				" cgeneralbid='" + infoVO.getCgeneralbid() + "' and dr=0 ");
		if (coll == null || coll.size() == 0){//2010-12-04 MeiChao���� ����鲻��,��ô��ȥnc_mdcrk_temp�в���
			coll = iUAPQueryBS.retrieveByClause(MdcrkTempVO.class,
					" cgeneralbid='" + infoVO.getCgeneralbid() + "' and dr=0 ");
			if(coll == null || coll.size() == 0){//����Ծ�Ϊ��,��ô�ٶ���Ϊת�����ɵ�������,ȥ��ת�����ʱ�뵥nc_mdcrk_temp
				Object sourcetype = iUAPQueryBS.executeQuery(
						"select t.csourcebillbid from ic_general_b t where t.cgeneralbid='"
								+ infoVO.getCgeneralbid()
								+ "' and t.cbodybilltypecode='4I'",
						new ColumnProcessor());
				
				coll = iUAPQueryBS.retrieveByClause(MdcrkTempVO.class,
						" cgeneralbid='" + (sourcetype==null?"÷����˧��":sourcetype.toString()) + "' and dr=0 ");
				if(coll == null || coll.size() == 0)//�������û�ҵ��뵥��Ϣ,��ô����null
					return null;
			}
			istemp=true;
		}
		
		MdcrkVO[] rsvos = new MdcrkVO[coll.size()];
		
		if(istemp){
			MdcrkTempVO[] tempvos=new MdcrkTempVO[coll.size()];//�������е�ֵ����TempVO��ת�����������뵥VO
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

	// ɾ���뵥����ⵥ��Ϣ������ԭ�е�������⴦��
	public boolean deleteAndRK(ChInfoVO infoVO) throws BusinessException {
		// ��ѯ����VOS
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// ��ʷ����VO
		MdcrkVO[] lsckVos = queryCrkVOS(infoVO);
		if (lsckVos == null || lsckVos.length == 0)
			return false;
		// �����������
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
			// ������֧��
			xclbvos[j].setZhishu(xclbvos[j].getZhishu().add(crkvo.getSrkzs(),
					MDConstants.ZS_XSW));
			// ����
			xclbvos[j].setZhongliang(xclbvos[j].getZhongliang().add(
					crkvo.getSrkzl(), MDConstants.ZL_XSW));
			//�ֳ�����    add by ouyang---2011-02-24  ��ɾ�������뵥ʱ,���ֳ�����Ҳ��������⴦��.����ֳ������ڳ���ά�����뵥������뵥���޷���ԭ
			xclbvos[j].setDef1(xclbvos[j].getDef1().add(
					crkvo.getDef1(), MDConstants.ZL_XSW));

		}
		
		iVOPersistence.updateVOArray(xclbvos);
		// ɾ����ʷ����VO
		iVOPersistence.deleteVOArray(lsckVos);
		return true;
	}

	// ���³���ⵥ����
	public void updateBill(MdwhDlg dlg, MdcrkVO[] rsvos)
			throws BusinessException {
		UFDouble zhishu = new UFDouble(0);
		UFDouble zhongliang = new UFDouble(0);
		String pk_ptzj = null;
		for (int i = 0; i < rsvos.length; i++) {
			pk_ptzj = rsvos[0].getCgeneralbid();
			zhishu = zhishu.add(rsvos[i].getSrkzs(), MDConstants.ZS_XSW);
			zhongliang = zhongliang
					.add(rsvos[i].getSrkzl(), MDConstants.ZL_XSW);//2011-01-03 MeiChao ȡ�ֳ�����
//					.add(rsvos[i].getSrkzl(), MDConstants.ZL_XSW);
		}
		dlg.setNoutnum(zhongliang);
		dlg.setNoutassistnum(zhishu);
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	// �뵥ȫ��ɾ���󣬽�ʵ����֧�����������
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

	// ��ѯ���۶�������������
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
			vo.setPk_mdxcl_b(sdvos[i].getPk_mdxcl_b());// ����ⵥ����
			vo.setSrkzs(sdvos[i].getSdzs());// ֧��
			vo.setSfbj(new UFBoolean(false)); // �Ƿ����
			vo.setDef4(sdvos[i].getDef4());// �Ǽ���
			vo.setSrkzl(sdvos[i].getDef3());// ��������
			// �ִ�����ͷVO
			MdxclBVO bvo = (MdxclBVO) iUAPQueryBS.retrieveByPK(MdxclBVO.class,
					vo.getPk_mdxcl_b());
			// �ִ�������VO
			MdxclVO hvo = (MdxclVO) iUAPQueryBS.retrieveByPK(MdxclVO.class, bvo
					.getPk_mdxcl());
			if (!hvo.getCwarehouseidb().equals(infoVO.getCwarehouseidb())) {
				tt = true;
				continue;
			}
			yxsdList.add(vo);
		}
		if (tt == true)
			MessageDialog.showWarningDlg(dlg, "����",
					"�������뵥�Ĳֿ���ʵ�ʳ���ֿⲻһ�£����ֻ�ȫ���뵥������¼����ʧ�ܣ�������ֿ⣡");
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
	

	// ��ѯ������ⵥ������
	public MdcrkVO queryMdCrkKyl(MdcrkVO evo, UFBoolean bsfbj)
			throws BusinessException {
		String pk_mdxcl_b = evo.getPk_mdxcl_b();
		String sql = "select ttt.* from (select t1.PK_MDXCL_B, t1.dr, t1.cspaceid, "
				+ "t1.jbh, t1.md_width, t1.md_length, t1.md_meter, t1.md_note, t1.md_lph, t1.md_zyh,t1.def1,t1.def7,t1.def8,t1.def9,"//2011-01-03 MeiChao ����def1 7 8 9 4���Զ����ֶ�
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
		UFDouble zhishu = new UFDouble((BigDecimal) rsmap.get("zhishu"));// �ִ�֧��
		UFDouble zhongliang = new UFDouble((BigDecimal) rsmap.get("zhongliang"));// �ִ���������
		UFDouble factoryallweight=new UFDouble((BigDecimal) rsmap.get("def1"));// �ִ�ֳ�����
		UFDouble kyzs = new UFDouble((Integer) rsmap.get("kyzs")); // ����֧��
		UFDouble kyzl = kyzs.multiply(zhongliang).div(zhishu,
				MDConstants.ZL_XSW);// ��������
		UFDouble factoryWeight=new UFDouble(rsmap.get("factoryweight").toString());// 2010-12-30 MeiChao add ���øֳ����� 
		evo.setCspaceid((String) rsmap.get("cspaceid"));// ��λ
		// ��������
		if (bsfbj.booleanValue() == false) {
			evo.setSrkzs(kyzs);
			evo.setSrkzl(kyzl);
			evo.setDef3(kyzs);//2010-12-30 MeiChao add ����ʱ֧��(�����жϲ��ܳ�����)���뵽def3�ֶ���
			evo.setDef1(factoryWeight);//2010-12-30 MeiChao add ���ֳ��������뵽def1�ֶ���
			evo.setDef2(kyzl);
		}
		// ����
		else {
			evo.setSrkzs(kyzs);
			evo.setSrkzl(new UFDouble(0));
			evo.setDef3(kyzs);//2010-12-30 MeiChao add ����ʱ֧��(�����жϲ��ܳ�����)���뵽def3�ֶ���
			evo.setDef1(factoryWeight);//2010-12-30 MeiChao add ���ֳ��������뵽def1�ֶ���
			evo.setDef2(kyzl);
		}
		return evo;
	}

	// ȫ��ɾ���뵥�󣬻�ԭ���λ������
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
			// ʵ��������
			CargInfVO.setNoutspacenum(itemVOa.getNoutnum()); // noutnum ʵ������
		} else {
			CargInfVO.setNinspaceassistnum(itemVOa.getNinassistnum()); // ninassistnum
			// ʵ�븨����
			CargInfVO.setNinspacenum(itemVOa.getNinnum()); // ninnum ʵ������
		}
		IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		ivo.insertVO(CargInfVO);
		return true;
	}

	// ����һ����λVOs
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
			vo.setCspaceid((String) voMap.get("cspaceid")); // ��λPKֵ
			vo.setVspacecode((String) voMap.get("cscode")); // ��λ����
			vo.setVspacename((String) voMap.get("csname")); // ��λ����
			//add by ouyangzhb  �������������ʱ��   �ѻ�λ������Ϊ ������������ ������������ʱ�� �� ��λ������Ϊ    ���ֳ�����
			UFDouble d1 = new UFDouble((BigDecimal) voMap.get("srkzl"));
			UFDouble d12 = new UFDouble((BigDecimal) voMap.get("def1"));
			UFDouble d2 = new UFDouble((BigDecimal) voMap.get("srkzs"));
			// ���
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
			// ����
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

	// �ж��Ƿ��뵥ά��
	public boolean querySfmdwf(String cinvbasid) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def20 from bd_invmandoc where pk_invbasdoc='"
				+ cinvbasid + "' and pk_corp='"+ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey()+"'"; //2010-12-27 MeiChao �Ƿ��뵥ά��,�Ӵ�����������ж�.
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("��ǰ����쳣���ڴ���������в����ڣ�");
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
	 * ����SpecialBillItemVO�������λ,ȡ��ԴΪ���ݱ�nc_mdcrk_temp,
	 * ��builderHwVos()�Ǹ���GeneralBillItemVO�������λ,ȡ��ԴΪnc_mdcrk��
	 * @param itemVO
	 * @param billType ��������
	 * @param sfth �Ƿ��˻�
	 * @return
	 * @throws BusinessException
	 */
	// ����һ����λVOs
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
			vo.setCspaceid((String) voMap.get("cspaceid")); // ��λPKֵ
			vo.setVspacecode((String) voMap.get("cscode")); // ��λ����
			vo.setVspacename((String) voMap.get("csname")); // ��λ����
			UFDouble d1 = new UFDouble((BigDecimal) voMap.get("def1"));
			UFDouble d2 = new UFDouble((BigDecimal) voMap.get("srkzs"));
			// ���
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
			// ����
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
