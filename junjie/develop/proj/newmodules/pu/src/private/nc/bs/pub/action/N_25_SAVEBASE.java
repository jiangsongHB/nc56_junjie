package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���ɹ���Ʊ�ı���
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-10-22)
 * @author ƽ̨�ű�����
 */
public class N_25_SAVEBASE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_SAVEBASE ������ע�⡣
 */
public N_25_SAVEBASE() {
  super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
  super.m_tmpVo=vo;
  Object inObject =getVo ();
    Object inObject1  = getUserObj ();
    if (! (inObject instanceof nc.vo.pi.InvoiceVO)) throw new nc.vo.pub.BusinessException ("������ϣ������Ĳɹ���Ʊ���Ͳ�ƥ��");
    if (inObject  == null) throw new nc.vo.pub.BusinessException ("������ϣ������Ĳɹ���Ʊû������");
    nc.vo.pi.InvoiceVO inVO = (nc.vo.pi.InvoiceVO)inObject;
    inObject =null;
    setParameter ( "INVO",inVO);
    setParameter ( "INVO1",inObject1);
    Object retObj =null;
    Object oLockRet = null;
    try {
      //���ο������֧�� by zhaoyha at 2009.1.19
      nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy("pu",BillTypeConst.PO_INVOICE);
      iep.beforeAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);
 
      //�Զ�������ҵ����
      oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "setDynamicLockForPksOfVo", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
  
      //��������
      Object isContinueLine = runClass("nc.bs.pi.InvoiceImpl", "checkVoNoChanged","&INVO:nc.vo.pi.InvoiceVO",vo,m_keyHas,m_methodReturnHas);
      if(isContinueLine!= null && !((nc.vo.pub.lang.UFBoolean)isContinueLine).booleanValue()){
        runClass("nc.bs.pu.pub.PubDMO", "checkVoNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
      }
      
      /**add by ouyangzhb 2013-10-14 �������Ϊ�ݹ�Ӧ�����ģ���Ҫ��д�ۼƿ�Ʊ����**/
      runClass("nc.bs.pi.InvoiceImpl", "reWriteInvoicenumByAdd","&INVO:nc.vo.pi.InvoiceVO",vo,m_keyHas,m_methodReturnHas);
       
      //�����������
      runClass("nc.bs.pi.InvoiceImpl", "validateAssistUnit","&INVO:nc.vo.pi.InvoiceVO",vo,m_keyHas,m_methodReturnHas);
      retObj =runClass( "nc.bs.pi.InvoiceImpl", "saveInvoice", "&INVO:nc.vo.pi.InvoiceVO,&INVO1:OBJECT",vo,m_keyHas,m_methodReturnHas);
      //���ο������֧�� by zhaoyha at 2009.1.19
      iep.afterAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);

//    }catch (Exception e) {
//      if (e instanceof nc.vo.pub.BusinessException)
//        throw (nc.vo.pub.BusinessException) e;
//      else
//        throw new nc.vo.pub.BusinessException(e.getMessage());
    }finally {
      //��ҵ����
//      if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())
//        runClass("nc.bs.pu.pub.PubDMO", "freePkForVo", "&INVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
    }
    
    Object retObj4 = null;
    try {
      //�Է�Ʊ����������id����
      retObj4 =runClass("nc.bs.pu.pub.PubDMO","lockPkForInv","nc.vo.pi.InvoiceVO:25",vo,m_keyHas,m_methodReturnHas);
      runClass("nc.bs.pi.InvoiceImpl","updateCostPriceForInv","nc.vo.pi.InvoiceVO:25",vo,m_keyHas,m_methodReturnHas);
    
      // �˻ص��ݺ�,������Ҫ����ڱ��ű����
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
      //Ϊ��Ʊ����������id����
      if(retObj4!= null && ((nc.vo.pub.lang.UFBoolean)retObj4).booleanValue())
        runClass("nc.bs.pu.pub.PubDMO","freePkForInv","nc.vo.pi.InvoiceVO:25",vo,m_keyHas,m_methodReturnHas);
    }
  
    if (retObj != null && ! (retObj instanceof java.util.ArrayList)) throw new nc.vo.pub.BusinessException ("���󣺲ɹ���Ʊ���涯���ķ���ֵ���ʹ���");
    //д������־ 
    setParameter("SAVEDVO", new nc.vo.pub.AggregatedValueObject[]{(nc.vo.pub.AggregatedValueObject) ((java.util.ArrayList)retObj).get(1)});
    setParameter("BILLTYPE", "25");
    setParameter("ACTIONNAME", "����");
    runClass("nc.bs.pu.pub.PubImpl","insertOperateLog","&SAVEDVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String",vo,m_keyHas,m_methodReturnHas);
    //�����Ƿ���Ҫ�����󡱱�־
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
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
  return "  super.m_tmpVo=vo;\n  Object inObject =getVo ();\n    Object inObject1  = getUserObj ();\n    if (! (inObject instanceof nc.vo.pi.InvoiceVO)) throw new nc.vo.pub.BusinessException (\"������ϣ������Ĳɹ���Ʊ���Ͳ�ƥ��\");\n    if (inObject  == null) throw new nc.vo.pub.BusinessException (\"������ϣ������Ĳɹ���Ʊû������\");\n    nc.vo.pi.InvoiceVO inVO = (nc.vo.pi.InvoiceVO)inObject;\n    inObject =null;\n    setParameter ( \"INVO\",inVO);\n    setParameter ( \"INVO1\",inObject1);\n    Object retObj =null;\n    Object oLockRet = null;\n    try {\n      //���ο������֧�� by zhaoyha at 2009.1.19\n      nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy(\"pu\",BillTypeConst.PO_INVOICE);\n      iep.beforeAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);\n \n      //�Զ�������ҵ����\n      oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"setDynamicLockForPksOfVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\",vo,m_keyHas,m_methodReturnHas);\n  \n      //��������\n      Object isContinueLine = runClass(\"nc.bs.pi.InvoiceImpl\", \"checkVoNoChanged\",\"&INVO:nc.vo.pi.InvoiceVO\",vo,m_keyHas,m_methodReturnHas);\n      if(isContinueLine!= null && !((nc.vo.pub.lang.UFBoolean)isContinueLine).booleanValue()){\n        runClass(\"nc.bs.pu.pub.PubDMO\", \"checkVoNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject\",vo,m_keyHas,m_methodReturnHas);\n      }\n       \n      //�����������\n      runClass(\"nc.bs.pi.InvoiceImpl\", \"validateAssistUnit\",\"&INVO:nc.vo.pi.InvoiceVO\",vo,m_keyHas,m_methodReturnHas);\n      retObj =runClass( \"nc.bs.pi.InvoiceImpl\", \"saveInvoice\", \"&INVO:nc.vo.pi.InvoiceVO,&INVO1:OBJECT\",vo,m_keyHas,m_methodReturnHas);\n      //���ο������֧�� by zhaoyha at 2009.1.19\n      iep.afterAction(nc.vo.scm.plugin.Action.SAVE, new nc.vo.pi.InvoiceVO[]{inVO}, null);\n//    }catch (Exception e) {\n//      if (e instanceof nc.vo.pub.BusinessException)\n//        throw (nc.vo.pub.BusinessException) e;\n//      else\n//        throw new nc.vo.pub.BusinessException(e.getMessage());\n    }finally {\n      //��ҵ����\n      //if(oLockRet!= null && ((nc.vo.pub.lang.UFBoolean)oLockRet).booleanValue())\n        //runClass(\"nc.bs.pu.pub.PubDMO\", \"freePkForVo\", \"&INVO:nc.vo.pub.AggregatedValueObject\",vo,m_keyHas,m_methodReturnHas);\n    }\n    \n    Object retObj4 = null;\n    try {\n      //�Է�Ʊ����������id����\n      retObj4 =runClass(\"nc.bs.pu.pub.PubDMO\",\"lockPkForInv\",\"nc.vo.pi.InvoiceVO:25\",vo,m_keyHas,m_methodReturnHas);\n      runClass(\"nc.bs.pi.InvoiceImpl\",\"updateCostPriceForInv\",\"nc.vo.pi.InvoiceVO:25\",vo,m_keyHas,m_methodReturnHas);\n    \n      // �˻ص��ݺ�,������Ҫ����ڱ��ű����\n      setParameter(\"CODEKEY\", \"vinvoicecode\");\n      if(inObject1 != null && inObject1 instanceof java.util.ArrayList && ((java.util.ArrayList)inObject1).size() > 5 && ((java.util.ArrayList)inObject1).get(5) != null){\n        nc.vo.pi.InvoiceVO oldVO = (nc.vo.pi.InvoiceVO)((java.util.ArrayList)inObject1).get(5);\n        setParameter(\"OLDVO\", oldVO);\n      }\n      runClass(\"nc.bs.pu.pub.GetSysBillCode\", \"returnBillNoWhenModify\",\"&INVO:nc.vo.pub.AggregatedValueObject,&OLDVO:nc.vo.pub.AggregatedValueObject,&CODEKEY:String\",vo,m_keyHas,m_methodReturnHas);\n    }catch(Exception ex) {\n      if (ex instanceof nc.vo.pub.BusinessException) throw (nc.vo.pub.BusinessException)ex;\n      else throw new nc.vo.pub.BusinessException (ex.getMessage());\n    }finally {\n      //Ϊ��Ʊ����������id����\n      if(retObj4!= null && ((nc.vo.pub.lang.UFBoolean)retObj4).booleanValue())\n        runClass(\"nc.bs.pu.pub.PubDMO\",\"freePkForInv\",\"nc.vo.pi.InvoiceVO:25\",vo,m_keyHas,m_methodReturnHas);\n    }\n  \n    if (retObj != null && ! (retObj instanceof java.util.ArrayList)) throw new nc.vo.pub.BusinessException (\"���󣺲ɹ���Ʊ���涯���ķ���ֵ���ʹ���\");\n    //д������־ \n    setParameter(\"SAVEDVO\", new nc.vo.pub.AggregatedValueObject[]{(nc.vo.pub.AggregatedValueObject) ((java.util.ArrayList)retObj).get(1)});\n    setParameter(\"BILLTYPE\", \"25\");\n    setParameter(\"ACTIONNAME\", \"����\");\n    runClass(\"nc.bs.pu.pub.PubImpl\",\"insertOperateLog\",\"&SAVEDVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String\",vo,m_keyHas,m_methodReturnHas);\n    //�����Ƿ���Ҫ�����󡱱�־\n    setParameter(\"SAVEDVO\", new nc.vo.pi.InvoiceVO[]{(nc.vo.pi.InvoiceVO) ((java.util.ArrayList)retObj).get(1)});\n    runClass(\"nc.bs.pi.InvoiceImpl\",\"setNeedSendAuditFlag\",\"&SAVEDVO:nc.vo.pi.InvoiceVO[]\",vo,m_keyHas,m_methodReturnHas);\n    return retObj;\n";}
/*
* ��ע�����ýű�������HAS
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
