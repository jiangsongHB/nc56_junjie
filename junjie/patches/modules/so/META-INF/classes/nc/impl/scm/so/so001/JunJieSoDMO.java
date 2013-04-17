package nc.impl.scm.so.so001;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.scm.so.receive.IReceiveService;
import nc.vo.ic.sd.MdsdVO;
import nc.vo.pub.BusinessException;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so016.SoVoTools;



/**
 * 骏杰专用的Sale的DMO类。
 * 
 * 创建日期：(2013-4-14)
 * 
 * @author：chenjianhua
 * 
 *
 */
public class JunJieSoDMO {
	/**
	 * N_30_OrderFinish脚本调用
	 * 自动关闭时释放已锁定的码单
	 */
	public void freeMdsd(SaleOrderVO[] svos) throws BusinessException {
		if(svos==null || svos.length==0){
			return ;
		}
		SaleorderBVO[] bvos=null;
		
		String sBodyIds="(";
		
		for(int i=0;i<svos.length;i++){
			bvos=svos[i].getBodyVOs();
			if(bvos!=null){
				for(int j=0;j<bvos.length;j++){
					sBodyIds +="'";
					sBodyIds +=bvos[j].getPrimaryKey();
					sBodyIds +="',";
				}
			}
			
		}
		sBodyIds=sBodyIds.substring(0, sBodyIds.lastIndexOf(","));
		
		sBodyIds +=")";
		String sWhere =" xsddbt_pk in " +sBodyIds + " and dr=0 ";
		
		BaseDAO dao=new BaseDAO();
		
		dao.deleteByClause(MdsdVO.class, sWhere);
		
	}
	
	
	/**
	 * N_30_SoBlankout脚本调用
	 * 作废时释放已锁定的码单
	 */
	public void freeMdsd(SaleOrderVO svo) throws BusinessException {
		if(svo==null ){
			return ;
		}
		SaleorderBVO[] bvos=null;
		
		String sBodyIds="(";
		
		 
			bvos=svo.getBodyVOs();
			if(bvos!=null){
				for(int j=0;j<bvos.length;j++){
					sBodyIds +="'";
					sBodyIds +=bvos[j].getPrimaryKey();
					sBodyIds +="',";
				}
			}
			
		 
		sBodyIds=sBodyIds.substring(0, sBodyIds.lastIndexOf(","));
		
		sBodyIds +=")";
		String sWhere =" xsddbt_pk in " +sBodyIds + " and dr=0 ";
		
		BaseDAO dao=new BaseDAO();
		
		dao.deleteByClause(MdsdVO.class, sWhere);
		
	}

}
