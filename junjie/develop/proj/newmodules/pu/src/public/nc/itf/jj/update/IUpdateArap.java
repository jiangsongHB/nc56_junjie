package nc.itf.jj.update;

import java.util.ArrayList;

import nc.vo.ep.dj.DJZBVO;

public interface IUpdateArap {

	public void updateArap(String pk,String action);
	
	public void deleteArap(ArrayList<DJZBVO> listvo);
	
}
