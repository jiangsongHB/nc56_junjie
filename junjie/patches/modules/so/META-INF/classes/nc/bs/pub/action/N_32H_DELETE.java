package nc.bs.pub.action;

import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;
import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：null的动态执行类。
 *
 * 创建日期：(2014-1-25)
 * @author 平台脚本生成
 */
public class N_32H_DELETE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_32H_DELETE 构造子注解。
 */
public N_32H_DELETE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
 //####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
 Object retObj =null;
	//####重要说明：生成的业务组件方法尽量不要进行修改####
	//方法说明:单据数据的一致性校验
	retObj=runClass("nc.bs.trade.business.HYPubBO", "checkConsistence", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
	//##################################################
	
 //####重要说明：生成的业务组件方法尽量不要进行修改####
	//方法说明:删除单据的主子表数据，即置dr=1
	retObj=runClass("nc.bs.trade.business.HYPubBO", "deleteBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
	//##################################################
	return retObj;
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return " //####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n Object retObj =null;\n	//####重要说明：生成的业务组件方法尽量不要进行修改####\n	//方法说明:单据数据的一致性校验\n	retObj=runClassCom@\"nc.bs.trade.business.HYPubBO\", \"checkConsistence\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	//##################################################\n	\n //####重要说明：生成的业务组件方法尽量不要进行修改####\n	//方法说明:删除单据的主子表数据，即置dr=1\n	retObj=runClassCom@\"nc.bs.trade.business.HYPubBO\", \"deleteBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	//##################################################\n	return retObj;\n";}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}
