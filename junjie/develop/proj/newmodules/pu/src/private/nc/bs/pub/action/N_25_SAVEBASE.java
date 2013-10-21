package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：采购发票的保存
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2008-10-22)
 * @author 平台脚本生成
 */
public class N_25_SAVEBASE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_SAVEBASE 构造子注解。
 */
public N_25_SAVEBASE() {
  super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
  super.m_tmpVo=vo;
  Object inObject =getVo ();
    Object inObject1  = getUserObj ();
    if (! (inObject instanceof nc.vo.pi.InvoiceVO)) throw new nc.vo.pub.BusinessException ("错误：您希望保存的采购发票类型不匹配");
    if (inObject  == null) throw new nc.vo.pub.BusinessException ("错误：您希望保存的采购发票没有数据");
    nc.vo.pi.InvoiceVO inVO = (nc.vo.pi.InvoiceVO)inObject;
    inObject =null;
    setParameter ( "INVO",inVO);
    setParameter ( "INVO1",inObject1);
    Object retObj =null;
    Object oLockRet = null;
    try {
      //二次开发插件支持 by zhaoyha at 2009.1.19
      nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy("pu",BillTypeConst.PO_INVOICE);
      iep.beforeAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);
 
      //对订单申请业务锁
      oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "setDynamicLockForPksOfVo", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
  
      //并发控制
      Object isContinueLine = runClass("nc.bs.pi.InvoiceImpl", "checkVoNoChanged","&INVO:nc.vo.pi.InvoiceVO",vo,m_keyHas,m_methodReturnHas);
      if(isContinueLine!= null && !((nc.vo.pub.lang.UFBoolean)isContinueLine).booleanValue()){
        runClass("nc.bs.pu.pub.PubDMO", "checkVoNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
      }
      
      /**add by ouyangzhb 2013-10-14 如果上游为暂估应付单的，需要回写累计开票数量**/
      runClass("nc.bs.pi.InvoiceImpl", "reWriteInvoicenumByAdd","&INVO:nc.vo.pi.InvoiceVO",vo,m_keyHas,m_methodReturnHas);
       
      //检查存货辅计量
      runClass("nc.bs.pi.InvoiceImpl", "validateAssistUnit","&INVO:nc.vo.pi.InvoiceVO",vo,m_keyHas,m_methodReturnHas);
      retObj =runClass( "nc.bs.pi.InvoiceImpl", "saveInvoice", "&INVO:nc.vo.pi.InvoiceVO,&INVO1:OBJECT",vo,m_keyHas,m_methodReturnHas);
      //二次开发插件支持 by zhaoyha at 2009.1.19
      iep.afterAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);

//    }catch (Exception e) {
//      if (e instanceof nc.vo.pub.BusinessException)
//        throw (nc.vo.pub.BusinessException) e;
//      else
//        throw new nc.vo.pub.BusinessException(e.getMessage());
    }finally {
      //解业务锁
//      if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())
//        runClass("nc.bs.pu.pub.PubDMO", "freePkForVo", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
    }
    
    Object retObj4 = null;
    try {
      //对发票表体存货管理id加锁
      retObj4 =runClass("nc.bs.pu.pub.PubDMO","lockPkForInv","nc.vo.pi.InvoiceVO:25",vo,m_keyHas,m_methodReturnHas);
      runClass("nc.bs.pi.InvoiceImpl","updateCostPriceForInv","nc.vo.pi.InvoiceVO:25",vo,m_keyHas,m_methodReturnHas);
    
      // 退回单据号,本操作要求放在本脚本最后
      setParameter("CODEKEY", "vinvoicecode");
      if(inObject1 != null && inObject1 instanceof java.util.ArrayList && ((java.util.ArrayList)inObject1).size() > 5 && ((java.util.ArrayList)inObject1).get(5) != null){
        nc.vo.pi.InvoiceVO oldVO = (nc.vo.pi.InvoiceVO)((java.util.ArrayList)inObject1).get(5);
        setParameter("OLDVO", oldVO);
      }
      runClass("nc.bs.pu.pub.GetSysBillCode", "returnBillNoWhenModify","&INVO:nc.vo.pub.AggregatedValueObject,&OLDVO:nc.vo.pub.AggregatedValueObject,&CODEKEY:String",vo,m_keyHas,m_methodReturnHas);
    }catch(Exception ex) {
      if (ex instanceof nc.vo.pub.BusinessException) throw (nc.vo.pub.BusinessException)ex;
      else throw new nc.vo.pub.BusinessException (ex.getMessage());
    }finally {
      //为发票表体存货管理id解锁
      if(retObj4!= null && ((nc.vo.pub.lang.UFBoolean)retObj4).booleanValue())
        runClass("nc.bs.pu.pub.PubDMO","freePkForInv","nc.vo.pi.InvoiceVO:25",vo,m_keyHas,m_methodReturnHas);
    }
  
    if (retObj != null && ! (retObj instanceof java.util.ArrayList)) throw new nc.vo.pub.BusinessException ("错误：采购发票保存动作的返回值类型错误。");
    //写操作日志 
    setParameter("SAVEDVO", new nc.vo.pub.AggregatedValueObject[]{(nc.vo.pub.AggregatedValueObject) ((java.util.ArrayList)retObj).get(1)});
    setParameter("BILLTYPE", "25");
    setParameter("ACTIONNAME", "保存");
    runClass("nc.bs.pu.pub.PubImpl","insertOperateLog","&SAVEDVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String",vo,m_keyHas,m_methodReturnHas);
    //设置是否需要“送审”标志
    setParameter("SAVEDVO", new nc.vo.pi.InvoiceVO[]{(nc.vo.pi.InvoiceVO) ((java.util.ArrayList)retObj).get(1)});
    runClass("nc.bs.pi.InvoiceImpl","setNeedSendAuditFlag","&SAVEDVO:nc.vo.pi.InvoiceVO[]",vo,m_keyHas,m_methodReturnHas);
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
  return "  super.m_tmpVo=vo;\n  Object inObject =getVo ();\n    Object inObject1  = getUserObj ();\n    if (! (inObject instanceof nc.vo.pi.InvoiceVO)) throw new nc.vo.pub.BusinessException (\"错误：您希望保存的采购发票类型不匹配\");\n    if (inObject  == null) throw new nc.vo.pub.BusinessException (\"错误：您希望保存的采购发票没有数据\");\n    nc.vo.pi.InvoiceVO inVO = (nc.vo.pi.InvoiceVO)inObject;\n    inObject =null;\n    setParameter ( \"INVO\",inVO);\n    setParameter ( \"INVO1\",inObject1);\n    Object retObj =null;\n    Object oLockRet = null;\n    try {\n      //二次开发插件支持 by zhaoyha at 2009.1.19\n      nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy(\"pu\",BillTypeConst.PO_INVOICE);\n      iep.beforeAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);\n \n      //对订单申请业务锁\n      oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"setDynamicLockForPksOfVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\",vo,m_keyHas,m_methodReturnHas);\n  \n      //并发控制\n      Object isContinueLine = runClass(\"nc.bs.pi.InvoiceImpl\", \"checkVoNoChanged\",\"&INVO:nc.vo.pi.InvoiceVO\",vo,m_keyHas,m_methodReturnHas);\n      if(isContinueLine!= null && !((nc.vo.pub.lang.UFBoolean)isContinueLine).booleanValue()){\n        runClass(\"nc.bs.pu.pub.PubDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\",vo,m_keyHas,m_methodReturnHas);\n      }\n       \n      //检查存货辅计量\n      runClass(\"nc.bs.pi.InvoiceImpl\", \"validateAssistUnit\",\"&INVO:nc.vo.pi.InvoiceVO\",vo,m_keyHas,m_methodReturnHas);\n      retObj =runClass( \"nc.bs.pi.InvoiceImpl\", \"saveInvoice\", \"&INVO:nc.vo.pi.InvoiceVO,&INVO1:OBJECT\",vo,m_keyHas,m_methodReturnHas);\n      //二次开发插件支持 by zhaoyha at 2009.1.19\n      iep.afterAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);\n//    }catch (Exception e) {\n//      if (e instanceof nc.vo.pub.BusinessException)\n//        throw (nc.vo.pub.BusinessException) e;\n//      else\n//        throw new nc.vo.pub.BusinessException(e.getMessage());\n    }finally {\n      //解业务锁\n      //if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())\n        //runClass(\"nc.bs.pu.pub.PubDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\",vo,m_keyHas,m_methodReturnHas);\n    }\n    \n    Object retObj4 = null;\n    try {\n      //对发票表体存货管理id加锁\n      retObj4 =runClass(\"nc.bs.pu.pub.PubDMO\",\"lockPkForInv\",\"nc.vo.pi.InvoiceVO:25\",vo,m_keyHas,m_methodReturnHas);\n      runClass(\"nc.bs.pi.InvoiceImpl\",\"updateCostPriceForInv\",\"nc.vo.pi.InvoiceVO:25\",vo,m_keyHas,m_methodReturnHas);\n    \n      // 退回单据号,本操作要求放在本脚本最后\n      setParameter(\"CODEKEY\", \"vinvoicecode\");\n      if(inObject1 != null && inObject1 instanceof java.util.ArrayList && ((java.util.ArrayList)inObject1).size() > 5 && ((java.util.ArrayList)inObject1).get(5) != null){\n        nc.vo.pi.InvoiceVO oldVO = (nc.vo.pi.InvoiceVO)((java.util.ArrayList)inObject1).get(5);\n        setParameter(\"OLDVO\", oldVO);\n      }\n      runClass(\"nc.bs.pu.pub.GetSysBillCode\", \"returnBillNoWhenModify\",\"&INVO:nc.vo.pub.AggregatedValueObject,&OLDVO:nc.vo.pub.AggregatedValueObject,&CODEKEY:String\",vo,m_keyHas,m_methodReturnHas);\n    }catch(Exception ex) {\n      if (ex instanceof nc.vo.pub.BusinessException) throw (nc.vo.pub.BusinessException)ex;\n      else throw new nc.vo.pub.BusinessException (ex.getMessage());\n    }finally {\n      //为发票表体存货管理id解锁\n      if(retObj4!= null && ((nc.vo.pub.lang.UFBoolean)retObj4).booleanValue())\n        runClass(\"nc.bs.pu.pub.PubDMO\",\"freePkForInv\",\"nc.vo.pi.InvoiceVO:25\",vo,m_keyHas,m_methodReturnHas);\n    }\n  \n    if (retObj != null && ! (retObj instanceof java.util.ArrayList)) throw new nc.vo.pub.BusinessException (\"错误：采购发票保存动作的返回值类型错误。\");\n    //写操作日志 \n    setParameter(\"SAVEDVO\", new nc.vo.pub.AggregatedValueObject[]{(nc.vo.pub.AggregatedValueObject) ((java.util.ArrayList)retObj).get(1)});\n    setParameter(\"BILLTYPE\", \"25\");\n    setParameter(\"ACTIONNAME\", \"保存\");\n    runClass(\"nc.bs.pu.pub.PubImpl\",\"insertOperateLog\",\"&SAVEDVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String\",vo,m_keyHas,m_methodReturnHas);\n    //设置是否需要“送审”标志\n    setParameter(\"SAVEDVO\", new nc.vo.pi.InvoiceVO[]{(nc.vo.pi.InvoiceVO) ((java.util.ArrayList)retObj).get(1)});\n    runClass(\"nc.bs.pi.InvoiceImpl\",\"setNeedSendAuditFlag\",\"&SAVEDVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n    return retObj;\n";}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)  {
  if (m_keyHas==null){
    m_keyHas=new Hashtable();
  }
  if (val!=null)  {
    m_keyHas.put(key,val);
  }
}
}
