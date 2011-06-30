package nc.ui.dbcache.version;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dbcache.intf.ICacheVersionBS;
import nc.bs.framework.aop.Wrapper;
import nc.bs.logging.Logger;
import nc.vo.dbcache.version.SyncPointVersions;
import nc.vo.dbcache.version.Version;
import nc.vo.pub.BusinessRuntimeException;

/**
 * ǰ̨�汾�������
 * 
 * @author linmin
 * @date 2009-4-22
 * 
 */
public class ClientCacheVersionBS implements ICacheVersionBS, Wrapper {

	/**
	 * ��������ʵ��
	 */
	private ICacheVersionBS serverVersionBS = null;

	/**
	 * �ͻ��˰汾���������
	 */
	private MirrorVersionAccess mirror = null;

	public ClientCacheVersionBS() {
		mirror = new MirrorVersionAccess();
	}

	public Set<Version> getOutDateVersions(Version[] oldVersions)
			throws BusinessRuntimeException {
		if (oldVersions == null || oldVersions.length < 1) {
			throw new IllegalArgumentException(
					"getOutDateVersions(Version[] oldVersions)��������Ϊ��");
		}
		Logger.debug("getOutDateVersions(Version[] oldVersions)");
		Map<String, String> clientVersions = mirror.getMemVersion();
		List<String> notFounds = new ArrayList<String>();
		boolean allContains = true;
		for (Version v : oldVersions) {
			if (v == null || v.getObjKey() == null) {
				continue;
			}
			String key = v.getObjKey().trim().toLowerCase();
			if (!clientVersions.containsKey(key)) {
				allContains = false;
				notFounds.add(key);
				Logger.debug("key�İ汾��ǰ̨δ�ҵ�����Ҫ�Ӻ�̨��ѯ:key=" + key);
			}
		}
		if (!allContains) {
			Logger.debug("not allContains");
			getOutDateVersions(mirror.getSyncPointVersionTs(), notFounds
					.toArray(new String[0]), false);
		}
		Set<Version> outDateVersions = new HashSet<Version>();
		for (Version oldV : oldVersions) {
			if (oldV == null || oldV.getObjKey() == null) {
				continue;
			}
			String key = oldV.getObjKey().trim().toLowerCase();
			if (!clientVersions.containsKey(key)) {
				Logger.error("��" + oldV.getObjKey()
						+ "���°汾��Ϣ�����ڣ�(impossible, maybe error)");
				Version version = new Version("-1", oldV.getObjKey());
				outDateVersions.add(version);
			} else {
				String v = clientVersions.get(oldV.getObjKey().toLowerCase());
				if (oldV.getVersion() == null
						|| oldV.getVersion().compareTo(v) < 0) {
					Version version = new Version(v, oldV.getObjKey());
					outDateVersions.add(version);
				}
			}
		}
		return outDateVersions;
	}

	public SyncPointVersions getOutDateVersions(String afterTs)
			throws BusinessRuntimeException {
		Logger.debug("��ʼ���غ�̨�汾��ϢgetOutDateVersions(String afterTs); afterTs:"
				+ afterTs);
		if (afterTs == null) {
			afterTs = mirror.getSyncPointVersionTs();
		}
		SyncPointVersions spv = serverVersionBS.getOutDateVersions(afterTs);
		if (spv != null) {
			mirror.updateMemVersion(spv.getTs(), spv.getVersions());
			Logger.debug("���ص�syncPointVersionTs��Ϊ��:	syncPoint:" + spv.getTs());
		}
		return spv;
	}

	/**
	 * ȡ��İ汾������ǰ̨�����ǲ�ѯ����������ڣ�����̨ͬ���汾��Ϣ���ٴβ�ѯ��
	 * 
	 * @see nc.bs.dbcache.intf.ICacheVersionBS#getVersion(java.lang.String)
	 * @param key
	 * @return
	 * @throws BusinessRuntimeException
	 */
	public String getVersion(String key) throws BusinessRuntimeException {
		if (key == null) {
			throw new IllegalArgumentException("key can not be null!");
		}
		Logger.debug("��ʼ����getVersion(String key)  param key is :" + key);
		Map<String, String> clientVersions = mirror.getMemVersion();
		if (clientVersions == null
				|| !clientVersions.containsKey(key.toLowerCase())) {
			getOutDateVersions(mirror.getSyncPointVersionTs(),
					new String[] { key.toLowerCase() }, false);
		}
		if (clientVersions != null
				&& clientVersions.containsKey(key.toLowerCase())) {
			return clientVersions.get(key.toLowerCase());
		} else {
			// throw new BusinessRuntimeException("��" + key + "�İ汾�����ڣ�");
			Logger.error("��" + key + "���°汾��Ϣ�����ڣ�");
			return "-1";
		}
	}

	/**
	 * ȡ�������Ӧ�İ汾���飬����ǰ̨�����ǲ�ѯ����������ڣ�����̨ͬ���汾��Ϣ���ٴβ�ѯ��
	 * 
	 * @see nc.bs.dbcache.intf.ICacheVersionBS#getVersions(java.lang.String[])
	 * @param keys
	 * @return
	 * @throws BusinessRuntimeException
	 */
	public String[] getVersions(String[] keys) throws BusinessRuntimeException {
		if (keys == null) {
			return new String[0];
		}
		Logger.debug("��ʼ����getVersions(String[] keys)");
		Map<String, String> clientVersions = mirror.getMemVersion();
		boolean allContains = true;
		List<String> notFounds = new ArrayList<String>();
		if (clientVersions == null) {
			allContains = false;
		}
		if (allContains) {
			for (String key : keys) {
				if (!clientVersions.containsKey(key.toLowerCase())) {
					allContains = false;
					notFounds.add(key);
					Logger.debug("key�İ汾��ǰ̨δ�ҵ�����Ҫ�Ӻ�̨��ѯ:key=" + key);
				}
			}
		}
		if (!allContains) {
			getOutDateVersions(mirror.getSyncPointVersionTs(), notFounds
					.toArray(new String[0]), false);
		}
		String[] result = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			if (!clientVersions.containsKey(keys[i].toLowerCase())) {
				// throw new BusinessRuntimeException("��" + keys[i] +
				// "�İ汾�����ڣ�");
				Logger.error("��" + keys[i] + "���°汾��Ϣ�����ڣ�");
				result[i] = "-1";
			} else {
				result[i] = clientVersions.get(keys[i].toLowerCase());
			}
		}
		return result;

	}

	/**
	 * @param version
	 * @param key
	 * @return
	 * @throws BusinessRuntimeException
	 */
	public boolean isOutDate(String version, String key)
			throws BusinessRuntimeException {
		String v = getVersion(key);
		if (v != null && version != null && version.compareTo(v) < 0) {
			return true;
		}
		return false;
	}

	/**
	 * @param key
	 * @throws BusinessRuntimeException
	 */
	public synchronized void updateVersion(String key)
			throws BusinessRuntimeException {
		// ˢ��key�İ汾�������������µİ汾��Ϣ
		Logger.debug("��ʼ����updateVersion(String key)");
		getOutDateVersions(mirror.getSyncPointVersionTs(),
				new String[] { key }, true);
	}

	public synchronized void updateVersions(String[] keys)
			throws BusinessRuntimeException {
		Logger.debug("��ʼ����updateVersions(String[] keys)");
		getOutDateVersions(mirror.getSyncPointVersionTs(), keys, true);
	}

	public void updateVersions() throws BusinessRuntimeException {
		Logger.debug("��ʼ����updateVersions()");
		getOutDateVersions((String) null);
	}

	public void setWrapping(Object wrapping) {
		this.serverVersionBS = (ICacheVersionBS) wrapping;
	}

	public SyncPointVersions getOutDateVersions(String afterTs,
			String[] needFetchs, boolean withupdate)
			throws BusinessRuntimeException {
		Logger
				.debug("��ʼ���غ�̨�汾��ϢgetOutDateVersions(String afterTs, String[] needFetchs)");
		SyncPointVersions spv = serverVersionBS.getOutDateVersions(afterTs,
				needFetchs, withupdate);
		if (spv != null) {
			mirror.updateMemVersion(spv.getTs(), spv.getVersions());
			Logger.debug("���ص�syncPointVersionTs��Ϊ��:	syncPoint:" + spv.getTs());
		}
		return spv;
	}

	/**
	 * ��¶�������ع��ߣ����ڲ鿴�����ڴ����ݡ�
	 * 
	 * @return
	 */
	public Map<String, String> getMemVersions() {
		return mirror.getMemVersion();
	}

	public String getMemSyncPointVersionTs() {
		return mirror.getSyncPointVersionTs();
	}
}
