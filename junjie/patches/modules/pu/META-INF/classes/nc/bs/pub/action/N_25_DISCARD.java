package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：采购发票的作废
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2006-10-9)
 * @author 平台脚本生成
 */
public class N_25_DISCARD extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_DISCARD 构造子注解。
 */
public N_25_DISCARD() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	Object[] inObject =getVos ();
		Object[] inObject1  = getUserObjAry ();
		if (inObject  == null) throw new nc.vo.pub.BusinessException ("错误：您希望保存的作废发票没有数据");
		nc.vo.pi.InvoiceVO[] inVO = (nc.vo.pi.InvoiceVO[])inObject;
		Object retObj = null;
		setParameter ( "INVO",inVO);
		setParameter ( "INVO1",inObject1);
		Object oLockRet = null;
		try {
	     //二次开发插件支持 by zhaoyha at 2009.1.19
      nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy("pu",BillTypeConst.PO_INVOICE);
      iep.beforeAction(nc.vo.scm.plugin.Action.DELETE, inVO, null);

			//对采购发票申请业务锁
			oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "lockPkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			//并发控制
			runClass("nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
			 
			/**add by ouyangzhb 2013-10-14 如果上游为暂估应付单的，需要回写累计开票数量**/
		      runClass("nc.bs.pi.InvoiceImpl", "reWriteInvoicenumByDel","&INVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
			
			//方法说明:作废发票
			retObj =runClass( "nc.bs.pi.InvoiceImpl", "discardInvoiceArray", "&INVO:nc.vo.pi.InvoiceVO[],&INVO1:OBJECT[]",vo,m_keyHas,m_methodReturnHas);
			//####单据号退回处理####
			for(int i = 0; i < inVO.length; i ++){
				setParameter ( "RETVO",inVO[i]);
				runClass("nc.bs.pu.pub.GetSysBillCode","returnBillNo","&RETVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
			}
      //二次开发插件支持 by zhaoyha at 2009.1.19
      iep.afterAction(nc.vo.scm.plugin.Action.DELETE, inVO, null);

		}catch (Exception e) {
			nc.bs.pu.pub.PubDMO.throwBusinessException("nc.bs.pub.action.N_25_DISCARD",e);
		}finally {
			//解业务锁
			if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())
				runClass("nc.bs.pu.pub.PubDMO", "freePkForVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		}

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
	return "	Object[] inObject =getVos ();\n		Object[] inObject1  = getUserObjAry ();\n		if (inObject  == null) throw new nc.vo.pub.BusinessException (\"错误：您希望保存的作废发票没有数据\");\n		nc.vo.pi.InvoiceVO[] inVO = (nc.vo.pi.InvoiceVO[])inObject;\n		Object retObj = null;\n		setParameter ( \"INVO\",inVO);\n		setParameter ( \"INVO1\",inObject1);\n		Object oLockRet = null;\n		try {\n			//对采购发票申请业务锁\n			oLockRet=runClassCom@ \"nc.bs.pu.pub.PubDMO\", \"lockPkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n			//并发控制\n			runClassCom@\"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n			//方法说明:作废发票\n			retObj =runClassCom@ \"nc.bs.pi.InvoiceImpl\", \"discardInvoiceArray\", \"&INVO:nc.vo.pi.InvoiceVO[],&INVO1:OBJECT[]\"@;\n			//####单据号退回处理####\n			for(int i = 0; i < inVO.length; i ++){\n				setParameter ( \"RETVO\",inVO[i]);\n				runClassCom@\"nc.bs.pu.pub.GetSysBillCode\",\"returnBillNo\",\"&RETVO:nc.vo.pub.AggregatedValueObject\"@;\n			}\n		}catch (Exception e) {\n			nc.bs.pu.pub.PubDMO.throwBusinessException(\"nc.bs.pub.action.N_25_DISCARD\",e);\n		}finally {\n			//解业务锁\n			if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())\n				runClassCom@\"nc.bs.pu.pub.PubDMO\", \"freePkForVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\"@;\n		}\n		return retObj;\n";}
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
