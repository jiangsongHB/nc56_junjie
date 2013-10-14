package nc.impl.jj.update;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.jj.update.IUpdateArap;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBVO;

public class UpdateArapImpl implements IUpdateArap {

	public void updateArap(String pk, String action) {

		BaseDAO dao = new BaseDAO();

		String sql = ("update arap_djzb set zyx20='Y' where vouchid='" + pk + "' and isnull(dr,0)=0");
		String sqldel = ("update arap_djzb set zyx20='N' where vouchid='" + pk + "' and isnull(dr,0)=0");
		try {
			if ("save".equals(action)) {
				dao.executeUpdate(sql);
			}
			if ("del".equals(action)) {
				dao.executeUpdate(sqldel);
			}

		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteArap(ArrayList<DJZBVO> listvo) {
		
		BaseDAO dao = new BaseDAO();
		for(int i=0;i<listvo.size();i++){
		String  pk=((DJZBHeaderVO)listvo.get(i).getParentVO()).getVouchid();
		String sqlh="update arap_djzb set dr=1 where vouchid='"+pk+"'";
		
		String sqlb="update arap_djfb set dr=1 where vouchid='"+pk+"'";
		
		try {
			dao.executeUpdate(sqlb);
			dao.executeUpdate(sqlh);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}

}
