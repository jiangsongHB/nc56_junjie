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
			throw new BusinessException("该码单信息已被其他单据引用,不能执行该操作");
		}

	}

	/**
	 * 删除码单信息
	 * 
	 * @param cgeneralbid
	 *            出入库表体主键
	 * @throws BusinessException
	 */
	public boolean deleteMDrk(String cgeneralbid) throws BusinessException {

		checkRef(cgeneralbid);

		SQLParameter param = new SQLParameter();
		param.addParam(cgeneralbid);

		String del_mdxcl = " delete nc_mdxcl_b where pk_mdxcl_b in ( select pk_mdxcl_b from NC_MDCRK where isnull(dr,0)=0 and  CGENERALBID = ? ) ";
		String del_md = " delete  NC_MDCRK where  CGENERALBID = ? ";

		String del_sd = " delete nc_mdsd where pk_mdxcl_b in ( select pk_mdxcl_b from NC_MDCRK where isnull(dr,0)=0 and  CGENERALBID = ? ) ";
		
		// 删除码单锁定记录
		int t = getDAO().executeUpdate(del_sd, param);
		System.out.println("一共删除锁定记录:" + t);

		// 删除现存量记录
		int i = getDAO().executeUpdate(del_mdxcl, param);
		System.out.println("一共删除现存量记录:" + i);

		// 删除码单信息
		int j = getDAO().executeUpdate(del_md, param);
		System.out.println("一共删除码单记录:" + j);

		if (i > 0 || j > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 2010-12-22 MeiChao 修改: 如果码单维护了对应的存货明细子表信息,那么删除原有的存货明细子表,再重新插入
	 */
	public boolean saveMDrk(MdcrkVO[] mdvos, MdxclVO xclvo, String cgeneralbid,InvDetailCVO[] invDetailCVOs)
			throws BusinessException {

		checkJBH(mdvos);
		boolean delete_date = deleteMDrk(cgeneralbid);
		if(delete_date){//如果成功删除了原有的码单信息.那么将原有的码单信息对应存货明细子表信息也删除.
			//对应的入库单表体pk
			String deleteScm_invdetail_c="update scm_invdetail_c t set t.dr=1 where t.cgeneralbid='"+cgeneralbid+"' and t.dr=0";
			int deleterowcount=this.getDAO().executeUpdate(deleteScm_invdetail_c);
			Logger.debug("新码单保存时,删除了"+deleterowcount+"条存货明细子表记录!");
		}
		/**
		 * 现存量表没有被其他单据引用: 1,删除现存量记录. 2,删除原有码单信息. 3,重新生成现存量记录. 4,重新生成码单记录
		 */

		String sel_mdxclHVO = " select pk_mdxcl from  nc_mdxcl where isnull(dr,0)=0 and  pk_corp = ? and ccalbodyidb=? and cwarehouseidb =?  and cinventoryidb=? ";

		SQLParameter param1 = new SQLParameter();
		param1.addParam(xclvo.getPk_corp());
		param1.addParam(xclvo.getCcalbodyidb());
		param1.addParam(xclvo.getCwarehouseidb());
		param1.addParam(xclvo.getCinventoryidb());

		// 查询现存量表头VO,如果没有刚重新保存一个,
		Object pk_mdxcl = getDAO().executeQuery(sel_mdxclHVO, param1,
				new ColumnProcessor());
		if (pk_mdxcl == null || pk_mdxcl.toString().trim() == "") {// 有现存量表头数据.
			pk_mdxcl = getDAO().insertVO(xclvo);
		}

		UFDouble zl = new UFDouble(0);
		UFDouble zs = new UFDouble(0);

		/**
		 * 重新生成码单信息. 先生成现存量,在生成码单信息.
		 */
		int i=0;//循环游标
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
			bvo.setRemark(mdcrkVO.getRemark());//备注
			bvo.setZhongliang(mdcrkVO.getSrkzl());//验收重量
			bvo.setZhishu(mdcrkVO.getSrkzs());
			bvo.setDr(0);
			bvo.setDef1(mdcrkVO.getDef1());//钢厂重量
			bvo.setDef2(mdcrkVO.getDef2());//自定义项2
			bvo.setDef3(mdcrkVO.getDef3());//自定义项3
			bvo.setDef4(mdcrkVO.getDef4());//非计算标识
			bvo.setDef7(mdcrkVO.getDef7());//自定义项7
			bvo.setDef8(mdcrkVO.getDef8());//自定义项8
			bvo.setDef9(mdcrkVO.getDef9());//自定义项9
			//2010-11-20 MeiChao add begin
			bvo.setDef10(mdcrkVO.getDef10());//自定义项10
			bvo.setDef11(mdcrkVO.getDef11());//自定义项11
			bvo.setDef12(mdcrkVO.getDef12());//自定义项12
			bvo.setDef13(mdcrkVO.getDef13());//自定义项13
			bvo.setDef14(mdcrkVO.getDef14());//自定义项14
			bvo.setDef15(mdcrkVO.getDef15());//自定义项15
			//2010-11-20 MeiChao add end
			bvo.setPk_mdxcl(pk_mdxcl.toString());

			bvo.setStatus(VOStatus.NEW);

			String xcl_b = getDAO().insertVO(bvo);

			mdcrkVO.setDr(0);
			mdcrkVO.setStatus(VOStatus.NEW);
			mdcrkVO.setPk_mdxcl_b(xcl_b);
			String thisMDPK=getDAO().insertVO(mdcrkVO);
			//2010-12-22 MeiChao 在码单保存时,将对应的存货明细记录保存起来.
			if(invDetailCVOs!=null
					&&invDetailCVOs.length==mdvos.length
					&&invDetailCVOs.length>i
					&&invDetailCVOs[i]!=null){
				invDetailCVOs[i].setPk_mdcrk(thisMDPK);//将刚才保存的码单PK,存入对应的明细子表VO中
				getDAO().insertVO(invDetailCVOs[i]);
			}
			i++;//累加游标
			zl = zl.add(mdcrkVO.getSrkzl());
			zs = zs.add(mdcrkVO.getSrkzs());
		}
		Logger.debug("保存码单时,成功保存"+i+"条存货明细子表记录.");
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
	 * 检查件编号是否重复
	 * 
	 * @param mdvos
	 *            需要检查的VO
	 * @throws BusinessException
	 *             如果发现重复会抛出该异常
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
				String jbhAndSpace = mdcrkVO.getJbh()+mdcrkVO.getCspaceid();//2010-12-09 MeiChao 用以比较的字符串加上了货位
				String pk_md = mdcrkVO.getPk_mdxcl_b();

				if (jbh_list.contains(jbhAndSpace)) {
					throw new BusinessException("表体第"+i+"行存在相同的货位+件编号的组合,件编号: "+mdcrkVO.getJbh() );
				} else {
					jbh_list.add(jbhAndSpace);
				}

				String sql = " select jbh from nc_mdxcl_b where isnull(dr,0)=0 and zhishu<>0 and jbh='"
						+ mdcrkVO.getJbh() + "' and cspaceid='"+mdcrkVO.getCspaceid()+"' ";//2010-12-09 MeiChao 件编号唯一修改为 件编号+货位唯一
				if (pk_md != null) {
					sql = sql + " and pk_mdxcl_b != '" + pk_md + "' ";
				}

				List list = (List) getDAO().executeQuery(sql,
						new ColumnListProcessor());
				if (!list.isEmpty()) {
					throw new BusinessException("第" + i + "行,对应货位中已存在 件编号" + mdcrkVO.getJbh()
							+ "的记录! ");//2010-12-09 MeiChao 件编号唯一修改为 件编号+货位唯一
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

	//增加系统货位表的同步更新方法实现。by 阮睿 2010-10-18
	public void updateCargoInfo(String cgeneralbid) throws BusinessException 
	{
		String del_huowei = "delete ic_general_bb1 where cgeneralbid=?";
		SQLParameter param = new SQLParameter();
		param.addParam(cgeneralbid);
		int k = getDAO().executeUpdate(del_huowei, param);
		System.out.println("一共删除库存表体行"+cgeneralbid+"的货位记录:" + k);
		
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
	 * 根据销售订单表体id,更新对应的销售执行情况表中的"以出库数量"字段.
	 */
	public boolean updateNewOutToSo(String csalebid, UFDouble newNumber)
			throws BusinessException {//多此一举,不用将数量变动加入其中,系统会自动计算,只需将"是否出库关闭"等字段修改为N即可.
		//String updateNewNum="update so_saleexecute t set t.ntotalinventorynumber=t.ntotalinventorynumber+'"+newNumber+"',t.bifinventoryfinish='N',t.bifreceivefinish='N' where t.csale_bid='"+csalebid+"'";
		String updateNewNum="update so_saleexecute t set t.bifinventoryfinish='N',t.bifreceivefinish='N' where t.csale_bid='"+csalebid+"'";
		int updateRowNumber=this.getDAO().executeUpdate(updateNewNum);
		if(updateRowNumber==1)
			return true;
		else 
			return false;
	}
	
	
	/**add by ouyangzhb 2012-04-26 在取消调整单时的码单处理*/
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
	/**add by ouyangzhb 2012-04-26 在取消调整单时的码单处理 end */
	

}
