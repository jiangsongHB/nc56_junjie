package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.itf.cmp.pj.INoteOuter;
import nc.ui.trade.button.IBillButton;
import nc.vo.fbm.pub.BaseinfoVO;
import nc.vo.fbm.pub.constant.FbmActionConstant;
import nc.vo.fbm.pub.constant.FbmBusConstant;
import nc.vo.fbm.register.RegisterVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PFBusinessException;

/**
 * 备注：保存动作处理类
 * 
 * 创建日期：
 * 
 * @author 
 */
public class N_JA01_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	/**
	 * N_36GL_UNAPPROVE 构造子注解。
	 */
	public N_JA01_APPROVE() {
		super();
	}
	
	  /*
	* 备注：平台编写规则类
	* 接口执行类
	*/
	public Object runComClass(PfParameterVO vo) throws BusinessException {
	try{
		super.m_tmpVo=vo;
		//####该组件为单动作工作流处理开始...不能进行修改####
	Object m_sysflowObj= procActionFlow(vo);
		if (m_sysflowObj!=null){
			return m_sysflowObj;
		}
		//####该组件为单动作工作流处理结束...不能进行修改####
		Object retObj  =runClass( "nc.bs.trade.comstatus.BillApprove", "approveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas); 
		return retObj;
	} catch (Exception ex) {
		if (ex instanceof BusinessException)
			throw (BusinessException) ex;
		else 
	    throw new PFBusinessException(ex.getMessage(), ex);
	}
	}

	 /**
	 * @function 根据tablename获得name
	 * @param tablename
	 * @param name
	 * @param colNm
	 * @param id
	 * @return
	 */
	public String getNameByID(String tablename, String name, String colNm,
			String id) {

		nc.bs.pub.formulaparse.FormulaParse parse = new nc.bs.pub.formulaparse.FormulaParse();
		String express = "name->getColValue(\""
				+ tablename
				+ "\", \""
				+ name
				+ "\", \""
				+ colNm
				+ "\", value)";
		// 设置公式
		parse.setExpress(express);
		// 添加参数
		List<String> list = new ArrayList<String>();
		list.add(id);
		parse.addVariable("value", list);
		// 结果
		String[] values = parse.getValueS();
		return values == null ? null : values[0];
	}
	 
	/*
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("3620add", "UPP3620ADD-000173")/*
																									 * @res
																									 * "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n	Object retObj=null;\n	return retObj;\n"
																									 */;
	}

	/*
	 * 备注：设置脚本变量的HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}