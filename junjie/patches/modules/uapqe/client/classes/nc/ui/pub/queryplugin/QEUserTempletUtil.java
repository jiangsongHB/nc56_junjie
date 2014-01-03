package nc.ui.pub.queryplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import nc.bs.logging.Logger;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.pub.querymodel.ConfigParamVO;
import nc.vo.pub.querymodel.MacroParamVO;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QEUserSchemaVO;
import nc.vo.pub.querymodel.QEUserTempletVO;

/**
 * V56+ 查询引擎用户模板信息工具类 创建日期：(2005-11-29 11:06:37)
 * 
 * @author：朱俊彬
 */

public class QEUserTempletUtil {

	/**
	 * V56+ 修改参数值为用户个性化设置值
	 */
	public static void changeParamSetting(ParamVO[] params,
			QEUserTempletVO[] templets) {
		// 修改信息
		int iLen1 = (params == null) ? 0 : params.length;
		int iLen2 = (templets == null) ? 0 : templets.length;
		int k = 0;
		for (int i = 0; i < iLen2; i++) {
			// 把位置对准了
			while (params[k].isInvisible() || params[k] instanceof MacroParamVO
					|| params[k] instanceof ConfigParamVO) {
				k++;
			}
			// 加载值与操作符
			if (templets[i] != null) {
				String value = templets[i].getValue();
				String[] strCodeOperaValues = QueryUtil.delimString(value,
						QEUserTempletVO.VALUE_SEPARATOR);
				int iLen3 = (strCodeOperaValues == null) ? 0
						: strCodeOperaValues.length;
				if (iLen3 >= 3 && k < iLen1) {
					System.out.println(params[k].getParamCode() + " -- "
							+ strCodeOperaValues[0]);
					if (strCodeOperaValues[1] != null
							&& !strCodeOperaValues[1].trim().equals("")) {
						params[k].setSelOpera(strCodeOperaValues[1]);
					}
					params[k].setValue(strCodeOperaValues[2]);
					params[k].setRefPk(strCodeOperaValues[3]);
				}
			}
			k++;
		}
		return;
	}

	/**
	 * V56+ 加载当前用户、功能编码的用户模板信息
	 */
	public static QEUserTempletVO[] loadParamSetting(String[] strUserFunccodes,
			boolean bSchema) {
		QEUserTempletVO[] templets = null;
		// 执行加载
		try {
			IQEUserTemplet iQEUserTemplet = new DefaultQEUserTemplet();
			templets = iQEUserTemplet.loadTempletInfo(strUserFunccodes,
					QEUserTempletVO.QUERY_TEMPLET_TYPE, bSchema);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		return templets;
	}

	/**
	 * V56+ 保存本次设置的参数为历史设置
	 */
	public static void saveParamSetting(String[] strUserFunccodes,
			QEUserTempletVO[] templets, boolean bSchema) {
		if (templets != null) {
			// 执行保存
			try {
				IQEUserTemplet iQEUserTemplet = new DefaultQEUserTemplet();
				iQEUserTemplet.saveTempletInfo(strUserFunccodes,
						QEUserTempletVO.QUERY_TEMPLET_TYPE, templets, bSchema);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * V56+ 删除历史设置
	 */
	public static void removeParamSetting(String[] strUserFunccodes,
			String schema) {
		// 执行保存
		try {
			IQEUserTemplet iQEUserTemplet = new DefaultQEUserTemplet();
			iQEUserTemplet.removeTempletInfo(strUserFunccodes,
					QEUserTempletVO.QUERY_TEMPLET_TYPE, schema);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * V56+ QEUserTempletVO数组转换为QEUserSchemaVO数组
	 */
	public static QEUserSchemaVO[] toUserSchemas(QEUserTempletVO[] templets) {
		int iLen = (templets == null) ? 0 : templets.length;
		ArrayList<QEUserSchemaVO> al = new ArrayList<QEUserSchemaVO>();
		Hashtable<String, QEUserSchemaVO> hashSchema = new Hashtable<String, QEUserSchemaVO>();
		for (int i = 0; i < iLen; i++) {
			if (hashSchema.containsKey(templets[i].getSchemaName())) {
				QEUserSchemaVO schema = (QEUserSchemaVO) hashSchema
						.get(templets[i].getSchemaName());
				List<QEUserTempletVO> listTemplet = schema.getListTemplet();
				listTemplet.add(templets[i]);
			} else {
				QEUserSchemaVO schema = new QEUserSchemaVO();
				schema.setSchema(templets[i].getSchemaName());
				List<QEUserTempletVO> listTemplet = new ArrayList<QEUserTempletVO>();
				listTemplet.add(templets[i]);
				schema.setListTemplet(listTemplet);
				//
				hashSchema.put(templets[i].getSchemaName(), schema);
				al.add(schema);
			}
		}
		QEUserSchemaVO[] schemas = al.toArray(new QEUserSchemaVO[0]);
		return schemas;
	}

	/**
	 * V56+ QEUserSchemaVO数组转换为QEUserTempletVO数组
	 */
	public static QEUserTempletVO[] toUserTemplets(QEUserSchemaVO[] schemas) {
		int iLen = (schemas == null) ? 0 : schemas.length;
		ArrayList<QEUserTempletVO> al = new ArrayList<QEUserTempletVO>();
		for (int i = 0; i < iLen; i++) {
			List<QEUserTempletVO> listTemplet = schemas[i].getListTemplet();
			int iSize = (listTemplet == null) ? 0 : listTemplet.size();
			for (int j = 0; j < iSize; j++) {
				al.add(listTemplet.get(j));
			}
		}
		QEUserTempletVO[] templets = al.toArray(new QEUserTempletVO[0]);
		return templets;
	}

	/**
	 * V56+ 保存本次设置的参数为历史设置
	 */
	public static QEUserTempletVO[] getUserTempletVO(ParamVO[] params,
			String[] strUserFunccodes, String schemaName) {
		QEUserTempletVO[] templets = null;
		Integer iType = QEUserTempletVO.QUERY_TEMPLET_TYPE;
		// 构造待保存信息
		int iLen = (params == null) ? 0 : params.length;
		if (iLen != 0) {
			templets = new QEUserTempletVO[iLen];
			for (int i = 0; i < iLen; i++) {
				templets[i] = new QEUserTempletVO();
				templets[i].setUserId(strUserFunccodes[0]);
				templets[i].setFuncCode(strUserFunccodes[1]);
				templets[i].setType(iType);
				templets[i].setSchemaName(schemaName);
				templets[i].setOrderNo(i);
				// 设置值与操作符
				String selOpera = (params[i].getSelOpera() == null) ? ""
						: params[i].getSelOpera().trim();
				String value = params[i].getParamCode()
						+ QEUserTempletVO.VALUE_SEPARATOR + selOpera
						+ QEUserTempletVO.VALUE_SEPARATOR
						+ params[i].getValue()
						+ QEUserTempletVO.VALUE_SEPARATOR
						+ params[i].getRefPk();
				templets[i].setValue(value);
			}
		}
		return templets;
	}

	/**
	 * 设置当前参数
	 */
	public static QEUserSchemaVO getUserSchemaVO(ParamVO[] params,
			String[] strUserFunccodes, String schemaName) {
		QEUserSchemaVO schema = new QEUserSchemaVO();
		schema.setSchema(schemaName);
		//
		QEUserTempletVO[] templets = getUserTempletVO(params, strUserFunccodes,
				schemaName);
		schema.setListTemplet(Arrays.asList(templets));
		return schema;
	}
}
