package nc.ui.ic.ic251;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.BusinessException;

/**
 * ��λ��������
 * 
 * @author heyq
 * 
 */
public class HwtzObj {

	private Map zzmap = new HashMap();

	// ����Map
	public void putHwtz(String key, HwtzVO[] voArrays) throws BusinessException {
		if (zzmap.containsKey(key))
			zzmap.remove(key);
		zzmap.put(key, voArrays);
	}

	//public HwtzVO[] getHwtzVOs(String key)
	
	
	private HwtzVO[] voArrays; // ������VO����

	private String zchw; // ת����λ

	private String zrhw;// ת���λ

	public HwtzVO[] getVoArrays() {
		return voArrays;
	}

	public void setVoArrays(HwtzVO[] voArrays) {
		this.voArrays = voArrays;
	}

	public String getZchw() {
		return zchw;
	}

	public void setZchw(String zchw) {
		this.zchw = zchw;
	}

	public String getZrhw() {
		return zrhw;
	}

	public void setZrhw(String zrhw) {
		this.zrhw = zrhw;
	}

}
