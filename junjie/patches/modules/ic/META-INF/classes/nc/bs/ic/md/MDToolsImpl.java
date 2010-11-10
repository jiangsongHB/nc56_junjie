package nc.bs.ic.md;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.itf.ic.md.IMDTools;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.md.dialog.MDUtils;
import nc.vo.ic.md.CargInfVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.xcl.MdxclBVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;

public class MDToolsImpl implements IMDTools {

	public void checkRef(String cgeneralbid) throws BusinessException {
		String sql1 = " select b.pk_mdxcl_b from NC_MDCRK a inner join NC_MDCRK b on a.PK_MDXCL_B = b.PK_MDXCL_B where isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and a.PK_MDCRK!=b.PK_MDCRK and b.CRKFX=1 and b.PK_MDXCL_B is not null and a.cgeneralbid = ? ";

		SQLParameter param = new SQLParameter();
		param.addParam(cgeneralbid);

		Object obj = getDAO().executeQuery(sql1, param, new ColumnProcessor());
		if (obj != null && obj.toString().trim() != "") {
			throw new BusinessException("���뵥��Ϣ�ѱ�������������,����ִ�иò���");
		}

	}

	/**
	 * ɾ���뵥��Ϣ
	 * 
	 * @param cgeneralbid
	 *            ������������
	 * @throws BusinessException
	 */
	public boolean deleteMDrk(String cgeneralbid) throws BusinessException {

		checkRef(cgeneralbid);

		SQLParameter param = new SQLParameter();
		param.addParam(cgeneralbid);

		String del_mdxcl = " delete nc_mdxcl_b where pk_mdxcl_b in ( select pk_mdxcl_b from NC_MDCRK where isnull(dr,0)=0 and  CGENERALBID = ? ) ";
		String del_md = " delete  NC_MDCRK where  CGENERALBID = ? ";

		String del_sd = " delete nc_mdsd where pk_mdxcl_b in ( select pk_mdxcl_b from NC_MDCRK where isnull(dr,0)=0 and  CGENERALBID = ? ) ";
		
		// ɾ���뵥������¼
		int t = getDAO().executeUpdate(del_sd, param);
		System.out.println("һ��ɾ��������¼:" + t);

		// ɾ���ִ�����¼
		int i = getDAO().executeUpdate(del_mdxcl, param);
		System.out.println("һ��ɾ���ִ�����¼:" + i);

		// ɾ���뵥��Ϣ
		int j = getDAO().executeUpdate(del_md, param);
		System.out.println("һ��ɾ���뵥��¼:" + j);

		if (i > 0 || j > 0) {
			return true;
		}
		return false;
	}

	public boolean saveMDrk(MdcrkVO[] mdvos, MdxclVO xclvo, String cgeneralbid)
			throws BusinessException {

		checkJBH(mdvos);
		boolean delete_date = deleteMDrk(cgeneralbid);
		/**
		 * �ִ�����û�б�������������: 1,ɾ���ִ�����¼. 2,ɾ��ԭ���뵥��Ϣ. 3,���������ִ�����¼. 4,���������뵥��¼
		 */

		String sel_mdxclHVO = " select pk_mdxcl from  nc_mdxcl where isnull(dr,0)=0 and  pk_corp = ? and ccalbodyidb=? and cwarehouseidb =?  and cinventoryidb=? ";

		SQLParameter param1 = new SQLParameter();
		param1.addParam(xclvo.getPk_corp());
		param1.addParam(xclvo.getCcalbodyidb());
		param1.addParam(xclvo.getCwarehouseidb());
		param1.addParam(xclvo.getCinventoryidb());

		// ��ѯ�ִ�����ͷVO,���û�и����±���һ��,
		Object pk_mdxcl = getDAO().executeQuery(sel_mdxclHVO, param1,
				new ColumnProcessor());
		if (pk_mdxcl == null || pk_mdxcl.toString().trim() == "") {// ���ִ�����ͷ����.
			pk_mdxcl = getDAO().insertVO(xclvo);
		}

		UFDouble zl = new UFDouble(0);
		UFDouble zs = new UFDouble(0);

		/**
		 * ���������뵥��Ϣ. �������ִ���,�������뵥��Ϣ.
		 */
		for (MdcrkVO mdcrkVO : mdvos) {
			MdxclBVO bvo = new MdxclBVO();
			bvo.setCspaceid(mdcrkVO.getCspaceid());
			bvo.setJbh(mdcrkVO.getJbh());
			bvo.setMd_width(mdcrkVO.getMd_width());
			bvo.setMd_length(mdcrkVO.getMd_length());
			bvo.setMd_lph(mdcrkVO.getMd_lph());
			bvo.setMd_meter(mdcrkVO.getMd_meter());
			bvo.setMd_note(mdcrkVO.getMd_note());
			bvo.setMd_zlzsh(mdcrkVO.getMd_zlzsh());
			bvo.setMd_zyh(mdcrkVO.getMd_zyh());
			bvo.setDef6(mdcrkVO.getDef6());
			bvo.setRemark(mdcrkVO.getRemark());//��ע
			bvo.setZhongliang(mdcrkVO.getSrkzl());
			bvo.setZhishu(mdcrkVO.getSrkzs());
			bvo.setDr(0);
			bvo.setDef4(mdcrkVO.getDef4());//�Ǽ����ʶ
			bvo.setDef7(mdcrkVO.getDef7());//�Զ�����7
			bvo.setDef8(mdcrkVO.getDef8());//�Զ�����8
			bvo.setDef9(mdcrkVO.getDef9());//�Զ�����9
			bvo.setPk_mdxcl(pk_mdxcl.toString());

			bvo.setStatus(VOStatus.NEW);

			String xcl_b = getDAO().insertVO(bvo);

			mdcrkVO.setDr(0);
			mdcrkVO.setStatus(VOStatus.NEW);
			mdcrkVO.setPk_mdxcl_b(xcl_b);
			getDAO().insertVO(mdcrkVO);

			zl = zl.add(mdcrkVO.getSrkzl());
			zs = zs.add(mdcrkVO.getSrkzs());
			
		}

		/*
		if (mdvos != null && mdvos.length > 0) {
			String updateZL = " update ic_general_b set ninnum = '"
					+ zl.doubleValue() + "' , ninassistnum = '"
					+ zs.doubleValue() + "' where cgeneralbid = '"
					+ cgeneralbid + "' ";
			System.out.println("#####" + updateZL);
			getDAO().executeUpdate(updateZL);
			return true;
		}
		*/
		return false;
	}

	/**
	 * ��������Ƿ��ظ�
	 * 
	 * @param mdvos
	 *            ��Ҫ����VO
	 * @throws BusinessException
	 *             ��������ظ����׳����쳣
	 */
	boolean checkJBH(MdcrkVO[] mdvos) throws BusinessException {
		boolean result = false;
		int i = 1;
		ArrayList<String> jbh_list = new ArrayList<String>();

		for (MdcrkVO mdcrkVO : mdvos) {
			if (mdcrkVO.getStatus() == VOStatus.UNCHANGED) {
				result = true;
			} else {
				result = false;
			}
			if (mdcrkVO.getCrkfx() == MDUtils.KC_IN) {
				String jbh = mdcrkVO.getJbh();
				String pk_md = mdcrkVO.getPk_mdxcl_b();

				if (jbh_list.contains(jbh)) {
					throw new BusinessException("���������ͬ�ļ����: " + jbh);
				} else {
					jbh_list.add(jbh);
				}

				String sql = " select jbh from nc_mdxcl_b where isnull(dr,0)=0 and zhishu<>0 and jbh='"
						+ jbh + "' ";
				if (pk_md != null) {
					sql = sql + " and pk_mdxcl_b != '" + pk_md + "' ";
				}

				List list = (List) getDAO().executeQuery(sql,
						new ColumnListProcessor());
				if (!list.isEmpty()) {
					throw new BusinessException("��" + i + "��,�����" + jbh
							+ "�Ѿ�����");
				}
			}
			i++;
		}
		return result;
	}

	private BaseDAO baseDAO = null;

	public BaseDAO getDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	//����ϵͳ��λ���ͬ�����·���ʵ�֡�by ��� 2010-10-18
	public void updateCargoInfo(String cgeneralbid) throws BusinessException 
	{
		String del_huowei = "delete ic_general_bb1 where cgeneralbid=?";
		SQLParameter param = new SQLParameter();
		param.addParam(cgeneralbid);
		int k = getDAO().executeUpdate(del_huowei, param);
		System.out.println("һ��ɾ����������"+cgeneralbid+"�Ļ�λ��¼:" + k);
		
		Collection c = getDAO().retrieveByClause(MdcrkVO.class, "cgeneralbid='"+cgeneralbid+"'");
		if (c!=null && c.size()>0)
		{
			Object[] o = c.toArray();
			for(int i=0;i<o.length;i++)
			{
				MdcrkVO mdcrkVO = (MdcrkVO)o[i];
				CargInfVO CargInfVO = new CargInfVO();
				CargInfVO.setCgeneralbid(cgeneralbid);
				CargInfVO.setCspaceid(mdcrkVO.getCspaceid());
				CargInfVO.setDr(0);
				CargInfVO.setPk_corp(mdcrkVO.getPk_corp());
				if(mdcrkVO.getCbodybilltypecode().trim().equals("4I")||mdcrkVO.getCbodybilltypecode().trim().equals("4C"))
				{
					CargInfVO.setNoutspaceassistnum(mdcrkVO.getSrkzs());
					CargInfVO.setNoutspacenum(mdcrkVO.getSrkzs());
				}
				else
				{
					CargInfVO.setNinspaceassistnum(mdcrkVO.getSrkzs());
					CargInfVO.setNinspacenum(mdcrkVO.getSrkzl());
				}
				getDAO().insertVO(CargInfVO);
			}
			
		}
		else
		{
			
		}
	}

}
