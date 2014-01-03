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
 * 默认的查询参数工厂
 * 
 * @author jl created on 2006-11-6
 */
public class DefaultQueryParameterFactory extends AbstractQueryParameterFactory {

	// 容器--作为ParamSetDlg的父容器
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
		// 获得引用查询
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
			// 获得可查数据源
			String[] queryDsns = QEEnvParamBean.getDefaultInstance()
					.getQueryDsn();
			// 获得引用查询
			String[] quoteIds = getFormatModelDef().getQuoteIds();
			String[] quoteAliases = getFormatModelDef().getQuoteAliases();
			int iLen = ArrayUtils.getLength(quoteIds);
			LinkedList aliasList = new LinkedList();
			LinkedList paramList = new LinkedList();
			for (int i = 0; i < iLen; i++) {
				// 获得参数数组 jl改 @2007-04-30 获取动态的查询定义
				QueryModelDef qmd = RuntimeQueryModelDefAccessor.getInstance()
						.getRuntimeQmd(container, quoteAliases[i]);
				if (qmd == null) {
					qmd = ModelUtil.getQueryDef(quoteIds[i], getDefDsName());
				}
				
				// ZJB+
				ParamVO[] originParams = cloneParamVOs(qmd.getParamVOs());
				
				//ParamVO[] params = ParamUtil.getQueryParam(originParams);
				int jLen = (originParams == null) ? 0 : originParams.length;
				// 如果没有参数则不做任何处理
				if (jLen != 0) {
					// 无效执行数据源处理
					String dsNameForExe = qmd.getDsName();
					boolean bDsForExeEnabled = ModelUtil.isElement(
							dsNameForExe, queryDsns);
					if (!bDsForExeEnabled) {
						// 篡改数据源
						dsNameForExe = getEnvInfo().getAccountDsn();
					}
					for (int j = 0; j < jLen; j++) {
						originParams[j].setDsName(dsNameForExe); // 数据源（用于参照指向）
					}
					// 刷新状态下，外界会传入一个HashParam
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
					// 作成一个哈希表key是参数编码，value是参数对象
					Hashtable hashParam = new Hashtable();
					for (int j = 0; j < jLen; j++) {
						// TODO jl+以后可能需要修改为instanceof QueryParamVO
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

	// 对所有引用查询加环境信息参数
	private void addEnvInfo() {

		String[] quoteAliases = fmd.getQuoteAliases();
		String[] quoteIds = fmd.getQuoteIds();
		int iLen = ArrayUtils.getLength(quoteIds);
		for (int i = 0; i < iLen; i++) {
			Hashtable hashParam = (Hashtable) hashAliasParam
					.get(quoteAliases[i]);
			hashParam = UIUtil.addEnvInfo(hashParam);
			// 加自定义环境参数
			QueryModelDef qmd = ModelUtil.getQueryDef(quoteIds[i],
					getDefDsName());
			// jl+ 加了节点编码到自定义环境变量中
			Object[] objEnvParams = new Object[] {
					getIEnvParam(qmd).getClass().getName(), qmd.getDsName(),
					UIUtil.makeEnvInfo(), getModuleCode() };
			hashParam.put(QueryConst.ENV_PARAM_KEY, objEnvParams);

			hashAliasParam.put(quoteAliases[i], hashParam);
		}
	}

	/**
	 * 获得多个参数设置 创建日期：(2003-8-8 16:47:13)
	 * 
	 * @return java.util.Hashtable
	 * @param allParamsArray
	 *            ParamVO[][]
	 * @param aliases
	 *            String[]
	 */
	private Hashtable getHashParamFromDlg(List aliasList, List paramsList) {
		if (checkifShowDlg(paramsList)) {
			// 这种转换太无聊了，纯属浪费时间，根本在于对话框的参数
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
				// 获得用户ID与格式对象ID信息
				String[] strUserFunccodes = getUserFunccodes((QueryNodeUI) c);
				// 根据上次保存的参数历史设置， 修改参数值为用户个性化设置值
				
				//wanglei 2014-01-03 根据是否刷新状态处理加载保存的查询条件
				if (isRefreshState) {
					QEUserTempletVO[] templets = QEUserTempletUtil
							.loadParamSetting(strUserFunccodes, false);
					QEUserTempletUtil.changeParamSetting(allParamsArray[0],
							templets);
				}
				// 设置用户功能编码信息
				dlg.setUserFunccodes(strUserFunccodes);
			} else if (c instanceof QueryMainUI) {
				dlg = new ParamSetDlg(c, getDefDsName());
			}
			dlg.setIEnvParam(getIEnvParam(null)); // 使用默认实现类，不能多页签各用各的
			dlg.setParamsArray(allParamsArray, aliasArray);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				// jl+
				ParamVO[][] newparamsArray = dlg.getParamsArray();
				iLen = (newparamsArray == null) ? 0 : newparamsArray.length;
				// 获得参数哈希表
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
					// jl+ 加数据权限控制
					Hashtable dataPowerHash = DataPowerUtil.getDataPowerHash(
							hashParam, refMap);
					hashParam.putAll(dataPowerHash);
					hashAliasParam.put(aliasArray[i], hashParam);
				}

				// V56+
				if (bNodeUI) {
					// 获得用户ID与格式对象ID信息
					String[] strUserFunccodes = getUserFunccodes((QueryNodeUI) c);
					// 构造待保存信息
					QEUserTempletVO[] templets = QEUserTempletUtil
							.getUserTempletVO(newparamsArray[0],
									strUserFunccodes, null);
					// 保存本次设置的参数为历史设置
					QEUserTempletUtil.saveParamSetting(strUserFunccodes,
							templets, false);
				}

			} else {
				hashAliasParam = null;
				// md by jl-点取消则不打开MainFrame
				if (c instanceof QueryNodeUI) {
					((QueryNodeUI) c).setCanceled(true);
				}
			}
		}
		return hashAliasParam;
	}

	/**
	 * 获得环境参数接口 创建日期：(2004-11-23 10:34:01)
	 * 
	 * @param newIBusiDatadict
	 *            nc.ui.pub.querymodel.IBusiDatadict
	 */
	private IEnvParam getIEnvParam(QueryModelDef qmd) {
		IEnvParam iEnvParam = null;
		try {
			String className = ParamConst.ENV_PARAM_CLASS;
			// 获得查询定义中保存的自定义环境参数接口实现类名
			if (qmd != null && qmd.getQueryBaseVO() != null
					&& qmd.getQueryBaseVO().getIEnvParamClass() != null)
				className = qmd.getQueryBaseVO().getIEnvParamClass();
			// 构型
			Class cls = Class.forName(className);
			iEnvParam = (IEnvParam) cls.newInstance();
		} catch (Exception e) {
			Logger.error(e);
		}
		return iEnvParam;
	}
	
	// V55 修改为protected
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
					// 不是配置型参数,同时必须是可见的s
					if (!(paramVOArray[i] instanceof ConfigParamVO)
							&& !paramVOArray[i].isInvisible())
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * 复制原始查询对象中的参数数组--为了防止修改原始的参数
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

	// 当前窗口的容器
	private Container getContainer() {
		return container;
	}

	public Hashtable refreshWithInputHashParam(Hashtable inputhashAliasParam) {
		isRefreshState = true;
		setInputhashParam(inputhashAliasParam);
		return getHashAliasParam();
	}

	/**
	 * V56+ 构造用户ID与报表节点功能编码信息
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
