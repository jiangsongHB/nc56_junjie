package nc.ui.reportquery.demo;

import java.awt.Container;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.querymodel.ParamSetDlg;
import nc.ui.pub.querymodel.QueryMainUI;
import nc.ui.pub.querymodel.QueryNodeUI;
import nc.ui.pub.querymodel.UIUtil;
import nc.ui.pub.queryplugin.QEUserTempletUtil;
import nc.vo.iuforeport.businessquery.QEEnvParamBean;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.pub.querymodel.ConfigParamVO;
import nc.vo.pub.querymodel.DataPowerUtil;
import nc.vo.pub.querymodel.DefaultQEDataPowerRefMap;
import nc.vo.pub.querymodel.EnvInfo;
import nc.vo.pub.querymodel.FormatModelDef;
import nc.vo.pub.querymodel.IEnvParam;
import nc.vo.pub.querymodel.IQEDataPowerRefMap;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.pub.querymodel.ParamConst;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QEUserTempletVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;

import org.apache.commons.lang.ArrayUtils;

/**
 * Ĭ�ϵĲ�ѯ��������
 * 
 * @author jl created on 2006-11-6
 */
public class DefaultQueryParameterFactory extends AbstractQueryParameterFactory {

	// ����--��ΪParamSetDlg�ĸ�����
	Container container = null;

	public DefaultQueryParameterFactory(FormatModelDef fmd, String defDsName,
			boolean bRun, Container c) {
		super(fmd, defDsName, bRun);
		this.container = c;
	}

	public EnvInfo getEnvInfo() {
		if (envInfo == null) {
			envInfo = UIUtil.makeEnvInfo();
		}
		return envInfo;
	}

	public Hashtable getHashAilasID() {
		// ������ò�ѯ
		String[] quoteIds = getFormatModelDef().getQuoteIds();
		String[] quoteAliases = getFormatModelDef().getQuoteAliases();
		int iLen = (quoteIds == null) ? 0 : quoteIds.length;

		Hashtable hashAliasId = new Hashtable();
		for (int i = 0; i < iLen; i++) {
			hashAliasId.put(quoteAliases[i], quoteIds[i]);
		}
		return hashAliasId;
	}

	public Hashtable getHashAliasParam() {
		try {
			hashAliasParam = new Hashtable();
			// ��ÿɲ�����Դ
			String[] queryDsns = QEEnvParamBean.getDefaultInstance()
					.getQueryDsn();
			// ������ò�ѯ
			String[] quoteIds = getFormatModelDef().getQuoteIds();
			String[] quoteAliases = getFormatModelDef().getQuoteAliases();
			int iLen = ArrayUtils.getLength(quoteIds);
			LinkedList aliasList = new LinkedList();
			LinkedList paramList = new LinkedList();
			for (int i = 0; i < iLen; i++) {
				// ��ò������� jl�� @2007-04-30 ��ȡ��̬�Ĳ�ѯ����
				QueryModelDef qmd = RuntimeQueryModelDefAccessor.getInstance()
						.getRuntimeQmd(container, quoteAliases[i]);
				if (qmd == null) {
					qmd = ModelUtil.getQueryDef(quoteIds[i], getDefDsName());
				}
				
				// ZJB+
				ParamVO[] originParams = cloneParamVOs(qmd.getParamVOs());
				
				//ParamVO[] params = ParamUtil.getQueryParam(originParams);
				int jLen = (originParams == null) ? 0 : originParams.length;
				// ���û�в��������κδ���
				if (jLen != 0) {
					// ��Чִ������Դ����
					String dsNameForExe = qmd.getDsName();
					boolean bDsForExeEnabled = ModelUtil.isElement(
							dsNameForExe, queryDsns);
					if (!bDsForExeEnabled) {
						// �۸�����Դ
						dsNameForExe = getEnvInfo().getAccountDsn();
					}
					for (int j = 0; j < jLen; j++) {
						originParams[j].setDsName(dsNameForExe); // ����Դ�����ڲ���ָ��
					}
					// ˢ��״̬�£����ᴫ��һ��HashParam
					if (isRefreshState && inputhashParam != null) {
						Hashtable htParam = (Hashtable) inputhashParam
								.get(quoteAliases[i]);
						for (int pi = 0, pn = originParams.length; pi < pn; pi++) {
							ParamVO param = (ParamVO) htParam.get(originParams[pi]
									.getParamCode());
							if (param != null) {
								originParams[pi] = param;
							}
						}
					}
					// ����һ����ϣ��key�ǲ������룬value�ǲ�������
					Hashtable hashParam = new Hashtable();
					for (int j = 0; j < jLen; j++) {
						// TODO jl+�Ժ������Ҫ�޸�Ϊinstanceof QueryParamVO
						if (originParams[j].getParamCode() != null)
							hashParam.put(originParams[j].getParamCode(), originParams[j]);
					}
					hashAliasParam.put(quoteAliases[i], hashParam);
					aliasList.add(quoteAliases[i]);
					
					// ZJB+
					// paramList.add(params);
					paramList.add(originParams);
				}
			}
			if (bRun) {
				hashAliasParam = getHashParamFromDlg(aliasList, paramList);
				if (hashAliasParam != null)
					addEnvInfo();
			}
		} catch (Exception e) {
			Logger.error(e);
		}
		return hashAliasParam;
	}

	// ���������ò�ѯ�ӻ�����Ϣ����
	private void addEnvInfo() {

		String[] quoteAliases = fmd.getQuoteAliases();
		String[] quoteIds = fmd.getQuoteIds();
		int iLen = ArrayUtils.getLength(quoteIds);
		for (int i = 0; i < iLen; i++) {
			Hashtable hashParam = (Hashtable) hashAliasParam
					.get(quoteAliases[i]);
			hashParam = UIUtil.addEnvInfo(hashParam);
			// ���Զ��廷������
			QueryModelDef qmd = ModelUtil.getQueryDef(quoteIds[i],
					getDefDsName());
			// jl+ ���˽ڵ���뵽�Զ��廷��������
			Object[] objEnvParams = new Object[] {
					getIEnvParam(qmd).getClass().getName(), qmd.getDsName(),
					UIUtil.makeEnvInfo(), getModuleCode() };
			hashParam.put(QueryConst.ENV_PARAM_KEY, objEnvParams);

			hashAliasParam.put(quoteAliases[i], hashParam);
		}
	}

	/**
	 * ��ö���������� �������ڣ�(2003-8-8 16:47:13)
	 * 
	 * @return java.util.Hashtable
	 * @param allParamsArray
	 *            ParamVO[][]
	 * @param aliases
	 *            String[]
	 */
	private Hashtable getHashParamFromDlg(List aliasList, List paramsList) {
		if (checkifShowDlg(paramsList)) {
			// ����ת��̫�����ˣ������˷�ʱ�䣬�������ڶԻ���Ĳ���
			String[] aliasArray = (String[]) aliasList
					.toArray(new String[aliasList.size()]);
			ParamVO[][] allParamsArray = (ParamVO[][]) paramsList
					.toArray(new ParamVO[paramsList.size()][]);
			int iLen = (allParamsArray == null) ? 0 : allParamsArray.length;
			Container c = getContainer();
			ParamSetDlg dlg = null;
			// V56+
			boolean bNodeUI = (c instanceof QueryNodeUI);
			if (bNodeUI) {
				dlg = new ParamSetDlg(ClientEnvironment.getInstance()
						.getDesktopApplet(), getDefDsName());
				// V56+
				// ����û�ID���ʽ����ID��Ϣ
				String[] strUserFunccodes = getUserFunccodes((QueryNodeUI) c);
				// �����ϴα���Ĳ�����ʷ���ã� �޸Ĳ���ֵΪ�û����Ի�����ֵ
				
				//wanglei 2014-01-03 �����Ƿ�ˢ��״̬������ر���Ĳ�ѯ����
				if (isRefreshState) {
					QEUserTempletVO[] templets = QEUserTempletUtil
							.loadParamSetting(strUserFunccodes, false);
					QEUserTempletUtil.changeParamSetting(allParamsArray[0],
							templets);
				}
				// �����û����ܱ�����Ϣ
				dlg.setUserFunccodes(strUserFunccodes);
			} else if (c instanceof QueryMainUI) {
				dlg = new ParamSetDlg(c, getDefDsName());
			}
			dlg.setIEnvParam(getIEnvParam(null)); // ʹ��Ĭ��ʵ���࣬���ܶ�ҳǩ���ø���
			dlg.setParamsArray(allParamsArray, aliasArray);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				// jl+
				ParamVO[][] newparamsArray = dlg.getParamsArray();
				iLen = (newparamsArray == null) ? 0 : newparamsArray.length;
				// ��ò�����ϣ��
				for (int i = 0; i < iLen; i++) {
					Hashtable hashParam = new Hashtable();
					ParamVO[] invisibleParams = QueryUtil
							.getInvisibleParam(allParamsArray[i]);
					for (int j = 0; j < newparamsArray[i].length; j++) {
						hashParam.put(newparamsArray[i][j].getParamCode(),
								newparamsArray[i][j]);
					}
					int j = (invisibleParams == null) ? 0
							: invisibleParams.length;
					for (int k = 0; k < j; k++) {
						hashParam.put(invisibleParams[k].getParamCode(),
								invisibleParams[k]);
					}
					IQEDataPowerRefMap refMap = new DefaultQEDataPowerRefMap();
					// jl+ ������Ȩ�޿���
					Hashtable dataPowerHash = DataPowerUtil.getDataPowerHash(
							hashParam, refMap);
					hashParam.putAll(dataPowerHash);
					hashAliasParam.put(aliasArray[i], hashParam);
				}

				// V56+
				if (bNodeUI) {
					// ����û�ID���ʽ����ID��Ϣ
					String[] strUserFunccodes = getUserFunccodes((QueryNodeUI) c);
					// �����������Ϣ
					QEUserTempletVO[] templets = QEUserTempletUtil
							.getUserTempletVO(newparamsArray[0],
									strUserFunccodes, null);
					// ���汾�����õĲ���Ϊ��ʷ����
					QEUserTempletUtil.saveParamSetting(strUserFunccodes,
							templets, false);
				}

			} else {
				hashAliasParam = null;
				// md by jl-��ȡ���򲻴�MainFrame
				if (c instanceof QueryNodeUI) {
					((QueryNodeUI) c).setCanceled(true);
				}
			}
		}
		return hashAliasParam;
	}

	/**
	 * ��û��������ӿ� �������ڣ�(2004-11-23 10:34:01)
	 * 
	 * @param newIBusiDatadict
	 *            nc.ui.pub.querymodel.IBusiDatadict
	 */
	private IEnvParam getIEnvParam(QueryModelDef qmd) {
		IEnvParam iEnvParam = null;
		try {
			String className = ParamConst.ENV_PARAM_CLASS;
			// ��ò�ѯ�����б�����Զ��廷�������ӿ�ʵ������
			if (qmd != null && qmd.getQueryBaseVO() != null
					&& qmd.getQueryBaseVO().getIEnvParamClass() != null)
				className = qmd.getQueryBaseVO().getIEnvParamClass();
			// ����
			Class cls = Class.forName(className);
			iEnvParam = (IEnvParam) cls.newInstance();
		} catch (Exception e) {
			Logger.error(e);
		}
		return iEnvParam;
	}
	
	// V55 �޸�Ϊprotected
	protected boolean checkifShowDlg(List paramList) {
		// V55+
		if (!super.checkifShowDlg(paramList)) {
			return false;
		}
		if (paramList != null) {
			Iterator iter = paramList.iterator();
			while (iter.hasNext()) {
				ParamVO[] paramVOArray = (ParamVO[]) iter.next();
				int iLen = paramVOArray == null ? 0 : paramVOArray.length;
				for (int i = 0; i < iLen; i++) {
					// ���������Ͳ���,ͬʱ�����ǿɼ���s
					if (!(paramVOArray[i] instanceof ConfigParamVO)
							&& !paramVOArray[i].isInvisible())
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * ����ԭʼ��ѯ�����еĲ�������--Ϊ�˷�ֹ�޸�ԭʼ�Ĳ���
	 * 
	 * @param paramVOs
	 * @return
	 */
	protected ParamVO[] cloneParamVOs(ParamVO[] paramVOs) {
		int size = ArrayUtils.getLength(paramVOs);
		ParamVO[] result = new ParamVO[size];
		for (int i = 0; i < size; i++) {
			result[i] = (ParamVO) paramVOs[i].clone();
		}
		return result;
	}

	// ��ǰ���ڵ�����
	private Container getContainer() {
		return container;
	}

	public Hashtable refreshWithInputHashParam(Hashtable inputhashAliasParam) {
		isRefreshState = true;
		setInputhashParam(inputhashAliasParam);
		return getHashAliasParam();
	}

	/**
	 * V56+ �����û�ID�뱨��ڵ㹦�ܱ�����Ϣ
	 */
	private String[] getUserFunccodes(QueryNodeUI qn) {
		String[] strUserFunccode = new String[2];
		strUserFunccode[0] = ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		strUserFunccode[1] = qn.getModuleCode();
		// strUserFunccode[1] = getFormatModelDef().getID();
		return strUserFunccode;
	}
}
