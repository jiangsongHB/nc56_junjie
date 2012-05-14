package nc.bs.ic.md;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.ic.md.IMDTools;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.md.dialog.MDUtils;
import nc.vo.ic.md.CargInfVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.xcl.MdxclBVO;
import nc.vo.ic.xcl.MdxclVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.ic.jjvo.InvDetailCVO;
import nc.vo.logging.Debug;

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

	/**
	 * 2010-12-22 MeiChao �޸�: ����뵥ά���˶�Ӧ�Ĵ����ϸ�ӱ���Ϣ,��ôɾ��ԭ�еĴ����ϸ�ӱ�,�����²���
	 */
	public boolean saveMDrk(MdcrkVO[] mdvos, MdxclVO xclvo, String cgeneralbid,InvDetailCVO[] invDetailCVOs)
			throws BusinessException {

		checkJBH(mdvos);
		boolean delete_date = deleteMDrk(cgeneralbid);
		if(delete_date){//����ɹ�ɾ����ԭ�е��뵥��Ϣ.��ô��ԭ�е��뵥��Ϣ��Ӧ�����ϸ�ӱ���ϢҲɾ��.
			//��Ӧ����ⵥ����pk
			String deleteScm_invdetail_c="update scm_invdetail_c t set t.dr=1 where t.cgeneralbid='"+cgeneralbid+"' and t.dr=0";
			int deleterowcount=this.getDAO().executeUpdate(deleteScm_invdetail_c);
			Logger.debug("���뵥����ʱ,ɾ����"+deleterowcount+"�������ϸ�ӱ��¼!");
		}
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
		int i=0;//ѭ���α�
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
			bvo.setZhongliang(mdcrkVO.getSrkzl());//��������
			bvo.setZhishu(mdcrkVO.getSrkzs());
			bvo.setDr(0);
			bvo.setDef1(mdcrkVO.getDef1());//�ֳ�����
			bvo.setDef2(mdcrkVO.getDef2());//�Զ�����2
			bvo.setDef3(mdcrkVO.getDef3());//�Զ�����3
			bvo.setDef4(mdcrkVO.getDef4());//�Ǽ����ʶ
			bvo.setDef7(mdcrkVO.getDef7());//�Զ�����7
			bvo.setDef8(mdcrkVO.getDef8());//�Զ�����8
			bvo.setDef9(mdcrkVO.getDef9());//�Զ�����9
			//2010-11-20 MeiChao add begin
			bvo.setDef10(mdcrkVO.getDef10());//�Զ�����10
			bvo.setDef11(mdcrkVO.getDef11());//�Զ�����11
			bvo.setDef12(mdcrkVO.getDef12());//�Զ�����12
			bvo.setDef13(mdcrkVO.getDef13());//�Զ�����13
			bvo.setDef14(mdcrkVO.getDef14());//�Զ�����14
			bvo.setDef15(mdcrkVO.getDef15());//�Զ�����15
			//2010-11-20 MeiChao add end
			bvo.setPk_mdxcl(pk_mdxcl.toString());

			bvo.setStatus(VOStatus.NEW);

			String xcl_b = getDAO().insertVO(bvo);

			mdcrkVO.setDr(0);
			mdcrkVO.setStatus(VOStatus.NEW);
			mdcrkVO.setPk_mdxcl_b(xcl_b);
			String thisMDPK=getDAO().insertVO(mdcrkVO);
			//2010-12-22 MeiChao ���뵥����ʱ,����Ӧ�Ĵ����ϸ��¼��������.
			if(invDetailCVOs!=null
					&&invDetailCVOs.length==mdvos.length
					&&invDetailCVOs.length>i
					&&invDetailCVOs[i]!=null){
				invDetailCVOs[i].setPk_mdcrk(thisMDPK);//���ղű�����뵥PK,�����Ӧ����ϸ�ӱ�VO��
				getDAO().insertVO(invDetailCVOs[i]);
			}
			i++;//�ۼ��α�
			zl = zl.add(mdcrkVO.getSrkzl());
			zs = zs.add(mdcrkVO.getSrkzs());
		}
		Logger.debug("�����뵥ʱ,�ɹ�����"+i+"�������ϸ�ӱ��¼.");
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
				String jbhAndSpace = mdcrkVO.getJbh()+mdcrkVO.getCspaceid();//2010-12-09 MeiChao ���ԱȽϵ��ַ��������˻�λ
				String pk_md = mdcrkVO.getPk_mdxcl_b();

				if (jbh_list.contains(jbhAndSpace)) {
					throw new BusinessException("�����"+i+"�д�����ͬ�Ļ�λ+����ŵ����,�����: "+mdcrkVO.getJbh() );
				} else {
					jbh_list.add(jbhAndSpace);
				}

				String sql = " select jbh from nc_mdxcl_b where isnull(dr,0)=0 and zhishu<>0 and jbh='"
						+ mdcrkVO.getJbh() + "' and cspaceid='"+mdcrkVO.getCspaceid()+"' ";//2010-12-09 MeiChao �����Ψһ�޸�Ϊ �����+��λΨһ
				if (pk_md != null) {
					sql = sql + " and pk_mdxcl_b != '" + pk_md + "' ";
				}

				List list = (List) getDAO().executeQuery(sql,
						new ColumnListProcessor());
				if (!list.isEmpty()) {
					throw new BusinessException("��" + i + "��,��Ӧ��λ���Ѵ��� �����" + mdcrkVO.getJbh()
							+ "�ļ�¼! ");//2010-12-09 MeiChao �����Ψһ�޸�Ϊ �����+��λΨһ
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

	/**
	 * 2010-12-11 MeiChao 
	 * �������۶�������id,���¶�Ӧ������ִ��������е�"�Գ�������"�ֶ�.
	 */
	public boolean updateNewOutToSo(String csalebid, UFDouble newNumber)
			throws BusinessException {//���һ��,���ý������䶯��������,ϵͳ���Զ�����,ֻ�轫"�Ƿ����ر�"���ֶ��޸�ΪN����.
		//String updateNewNum="update so_saleexecute t set t.ntotalinventorynumber=t.ntotalinventorynumber+'"+newNumber+"',t.bifinventoryfinish='N',t.bifreceivefinish='N' where t.csale_bid='"+csalebid+"'";
		String updateNewNum="update so_saleexecute t set t.bifinventoryfinish='N',t.bifreceivefinish='N' where t.csale_bid='"+csalebid+"'";
		int updateRowNumber=this.getDAO().executeUpdate(updateNewNum);
		if(updateRowNumber==1)
			return true;
		else 
			return false;
	}
	
	
	/**add by ouyangzhb 2012-04-26 ��ȡ��������ʱ���뵥����*/
	public void cancelMD(String billpk){
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		ArrayList<GeneralBillItemVO> invos = new ArrayList<GeneralBillItemVO>();
		
		String sql = "select * from ic_general_b b  where nvl(b.dr,0)=0 and b.csourcebillhid='"+billpk+"' ";
		try {
			try {
				invos = (ArrayList<GeneralBillItemVO>) bs.executeQuery(sql, new BeanListProcessor(GeneralBillItemVO.class));
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (invos != null && invos.size() > 0) {
				for (int y = 0; y < invos.size(); y++) {
					String cbodybilltypecode = invos.get(y).getCbodybilltypecode();
					String updatestr = null;
					if(cbodybilltypecode.endsWith("4A")){
						updatestr = "update nc_mdxcl_b b set b.zhishu = b.zhishu-(select k.srkzs from nc_mdcrk k where  k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0)  ,b.zhongliang= b.zhongliang- (select k.srkzl from nc_mdcrk k where k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0),b.def1=b.def1-(select k.def1 from nc_mdcrk k where k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0)  where b.pk_mdxcl_b in (select k.pk_mdxcl_b from nc_mdcrk k where k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0) ";
					}else if (cbodybilltypecode.endsWith("4I")){
						updatestr = "update nc_mdxcl_b b set b.zhishu = b.zhishu+(select k.srkzs from nc_mdcrk k  where k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0) ,b.zhongliang= b.zhongliang+(select k.srkzl from nc_mdcrk k where k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0) ,b.def1=b.def1+(select k.def1 from nc_mdcrk k where k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0)  where b.pk_mdxcl_b in (select k.pk_mdxcl_b from nc_mdcrk k where k.cgeneralbid ='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0) ";
					}else{
						return;
					}
					getDAO().executeUpdate(updatestr);
					String delsql = "update nc_mdcrk k set k.dr=1 where k.cgeneralbid='"+invos.get(y).getPrimaryKey()+"' and isnull(k.dr,0)=0";
					getDAO().executeUpdate(delsql);
				}
			}else{
				return;
			}
				
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**add by ouyangzhb 2012-04-26 ��ȡ��������ʱ���뵥���� end */
	

}
