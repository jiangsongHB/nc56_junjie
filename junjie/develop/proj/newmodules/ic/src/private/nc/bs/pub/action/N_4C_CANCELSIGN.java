package nc.bs.pub.action;

import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;
import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.impl.ic.CRM.IcToCRM;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.scm.crm.CrmSynchroVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע��������۳��ⵥ��ȡ��ǩ��
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2009-11-19)
 * @author ƽ̨�ű�����
 */
public class N_4C_CANCELSIGN extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4C_CANCELSIGN ������ע�⡣
 */
public N_4C_CANCELSIGN() {
  super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
  super.m_tmpVo=vo;
  //####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********
Object inCurObject=getVo();
Object retObj=null;
//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�
if(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵������Ͳ�ƥ��"));
if(inCurObject == null)  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵���û������"));
//2,���ݺϷ���������ת��Ϊ��浥�ݡ�
nc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;
if(inCurObject !=null)
 inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;
inCurObject=null;
//��ȡƽ̨����Ĳ���
setParameter("INCURVO",inCurVO);
//######################################################
//��д�����Ͽͻ���ҵ��Ӧ�գ�ע������������ô˷����Ļ���Ӧע�͵�SAVEBASE&DELETE����ͬ����
//������ظ���д��������ݴ���
//Ϊ�޸�ҵ��Ӧ�ձ���ĸ���
//if(inCurVO!=null)
// setParameter("INCURVOARAP",(nc.vo.ic.pub.bill.GeneralBillVO)inCurVO.clone());
//######################################################
Object alLockedPK=null;
try{
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:����Ƿ���ʡ�<������>
//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1
runClass("nc.bs.ic.ic2a3.AccountctrlDMO","checkAccountStatus","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//#############################################################
//����˵��:������ⵥ�ݼ�ҵ����
alLockedPK=runClass("nc.bs.ic.pub.bill.ICLockBO","lockICBill","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//##################################################
//�÷���<��������>
//����˵��:����浥��ʱ���
runClass("nc.bs.ic.pub.check.CheckDMO","checkTimeStamp","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//�÷���<��������>
//����˵��:����Ƿ���ǩ�ֵĵ���
runClass("nc.bs.ic.pub.check.CheckBusiDMO","checkCancelSigningUser","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:��������Ƿ�����ȡ��ǩ��
runClass("nc.bs.ic.pub.RewriteDMO","check4CCancelForSale","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:�ж��Ƿ���������Ļ���
runClass("nc.bs.ic.pub.check.CheckDMO","checkBillSumed","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//����˵��:�Ƿ��˻�
runClass("nc.bs.ic.pub.check.CheckDMO","isOutBack","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//##################################################
//����˵��:�ж��Ƿ������;����
runClass("nc.bs.ic.ic700.WastageBillForSOTO","checkWastageBillWhenUnAudit","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//##################################################
//����˵��:����ȡ��ǩ��
retObj=runClass("nc.bs.ic.ic211.GeneralHBO","cancelSignBill","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//�������ǰ�����������Ƿ�ƥ��
if(retObj != null && !(retObj instanceof  ArrayList))  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("����ȡ��ǩ�ֶ����ķ���ֵ���ʹ���"));
  //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
  //����˵��:����ȡ��ǩ��ʱ���г��ⷴ����
//IOutbalanceDMO sobo = (IOutbalanceDMO)NCLocator.getInstance().lookup(IOutbalanceDMO.class.getName());
//sobo.updateOutBalWhenCancelSign(inCurVO);
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:ɾ������ӿڵ���
//����˵��:���VMI�����/�ܴ������������
//Object iPecent=runClass("nc.bs.ic.pub.check.CheckDMO","getVmiInvStatus","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//##################################################
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:ȡ������
runClass("nc.bs.ic.pub.ictoso.Ic2SoDMO","setAfterOutAbandonCheck","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//##################################################
//if(iPecent!=null&&((Integer)iPecent).intValue()==0){
//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);
runClass("nc.bs.ic.pub.ictoia.Ic2IaDMO","deleteIABills","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);

/**############################# add by ouyangzhb 2012-12-08 ȡ��ǩ��ʱ����Ҫ�ж��Ƿ��Ѿ�ɾ���������η�Ʊ ########################**/
runClass( "nc.bs.ic.ic211.GeneralHDMO", "checkInvoiceDel", "&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
/**############################# add by ouyangzhb 2012-12-08 ȡ��ǩ��ʱ����Ҫ�ж��Ƿ��Ѿ�ɾ���������η�Ʊ ########################**/
//}
//************************�����۶������������**************************************************
//String sActionName=nc.vo.scm.recordtime.RecordType.SIGNOUTTRAN; 
//setParameter ( "sAction",sActionName);
//************************************************************************************************** 
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:ȡ������ĵ��ݶ���ִ��ʱ��
//runClass("nc.bs.scm.recordtime.RecordTimeImpl","unrecord","&sAction:STRING,&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//************************�����۶������������**************************************************
//sActionName=nc.vo.scm.recordtime.RecordType.SIGNOUTTRAN; 
//setParameter ( "sAction",sActionName);
//**************************************************************************************************
//**************************************************************************************************
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:ȡ������ĵ��ݶ���ִ��ʱ��
//runClass("nc.bs.scm.recordtime.RecordTimeImpl","unrecord","&sAction:STRING,&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//######################################################
//��д�����Ͽͻ���ҵ��Ӧ�գ�ע������������ô˷����Ļ���Ӧע�͵�SAVEBASE&DELETE����ͬ����
//������ظ���д��������ݴ���
//runClass( "nc.bs.ic.ic211.GeneralHDMO", "updateCustBusiMny", "&USELESSVO:nc.vo.ic.pub.bill.GeneralBillVO,&INCURVOARAP:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//######################################################
//������������,��д���ۼƻ�
//runClass("nc.bs.ic.pub.RewriteDMO","reWriteSLWhenUnSign","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//############################
//����ҵ����־���÷�����������
setParameter("ERR","");
setParameter("FUN","ȡ��ǩ��");
runClass("nc.bs.ic.pub.check.CheckDMO","insertBusinesslog","&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
new IcToCRM().synchronizeSO(inCurVO,CrmSynchroVO.ICRM_DELETE);
//############################
}catch(Exception e){
//############################
//����ҵ����־���÷�����������
setParameter("EXC",e.getMessage());
setParameter("FUN","ȡ��ǩ��");
runClass("nc.bs.ic.pub.check.CheckBO","insertBusinessExceptionlog","&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//###########################
        if (e instanceof nc.vo.pub.BusinessException)
      throw (nc.vo.pub.BusinessException) e;
    else
      throw new nc.vo.pub.BusinessException("Remote Call", e);
}
finally{
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:������ⵥ�ݽ�ҵ����
setParameter("ALLPK",(ArrayList)alLockedPK);
if(alLockedPK!=null)
runClass("nc.bs.ic.pub.bill.ICLockBO","unlockBill","&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
//##################################################
}
inCurVO=null;
return retObj;
//************************************************************************
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
  return "  //####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\nObject inCurObject=getVo();\nObject retObj=null;\n//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵������Ͳ�ƥ��\"));\nif(inCurObject == null)  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵���û������\"));\n//2,���ݺϷ���������ת��Ϊ��浥�ݡ�\nnc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;\nif(inCurObject !=null)\n inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;\ninCurObject=null;\n//��ȡƽ̨����Ĳ���\nsetParameter(\"INCURVO\",inCurVO);\n//######################################################\n//��д�����Ͽͻ���ҵ��Ӧ�գ�ע������������ô˷����Ļ���Ӧע�͵�SAVEBASE&DELETE����ͬ����\n//������ظ���д��������ݴ���\n//Ϊ�޸�ҵ��Ӧ�ձ���ĸ���\n//if(inCurVO!=null)\n// setParameter(\"INCURVOARAP\",(nc.vo.ic.pub.bill.GeneralBillVO)inCurVO.clone());\n//######################################################\nObject alLockedPK=null;\ntry{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:����Ƿ���ʡ�<������>\n//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//#############################################################\n//����˵��:������ⵥ�ݼ�ҵ����\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockICBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//�÷���<��������>\n//����˵��:����浥��ʱ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//�÷���<��������>\n//����˵��:����Ƿ���ǩ�ֵĵ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckBusiDMO\",\"checkCancelSigningUser\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:��������Ƿ�����ȡ��ǩ��\nrunClassCom@\"nc.bs.ic.pub.RewriteDMO\",\"check4CCancelForSale\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:�ж��Ƿ���������Ļ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkBillSumed\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//����˵��:�Ƿ��˻�\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"isOutBack\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//##################################################\n//����˵��:�ж��Ƿ������;����\nrunClassCom@\"nc.bs.ic.ic700.WastageBillForSOTO\",\"checkWastageBillWhenUnAudit\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//����˵��:����ȡ��ǩ��\nretObj=runClassCom@\"nc.bs.ic.ic211.GeneralHBO\",\"cancelSignBill\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//�������ǰ�����������Ƿ�ƥ��\nif(retObj != null && !(retObj instanceof  ArrayList))  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"����ȡ��ǩ�ֶ����ķ���ֵ���ʹ���\"));\n  //####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n  //����˵��:����ȡ��ǩ��ʱ���г��ⷴ����\n//IOutbalanceDMO sobo = (IOutbalanceDMO)NCLocator.getInstance().lookup(IOutbalanceDMO.class.getName());\n//sobo.updateOutBalWhenCancelSign(inCurVO);\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:ɾ������ӿڵ���\n//����˵��:���VMI�����/�ܴ������������\n//Object iPecent=runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"getVmiInvStatus\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:ȡ������\nrunClass(\"nc.bs.ic.pub.ictoso.Ic2SoDMO\",\"setAfterOutAbandonCheck\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\",vo,m_keyHas,m_methodReturnHas);\n//##################################################\n//if(iPecent!=null&&((Integer)iPecent).intValue()==0){\n//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);\nrunClassCom@\"nc.bs.ic.pub.ictoia.Ic2IaDMO\",\"deleteIABills\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//}\n//************************�����۶������������**************************************************\n//String sActionName=nc.vo.scm.recordtime.RecordType.SIGNOUTTRAN; \n//setParameter ( \"sAction\",sActionName);\n//************************************************************************************************** \n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:ȡ������ĵ��ݶ���ִ��ʱ��\n//runClassCom@\"nc.bs.scm.recordtime.RecordTimeImpl\",\"unrecord\",\"&sAction:STRING,&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//************************�����۶������������**************************************************\n//sActionName=nc.vo.scm.recordtime.RecordType.SIGNOUTTRAN; \n//setParameter ( \"sAction\",sActionName);\n//**************************************************************************************************\n//**************************************************************************************************\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:ȡ������ĵ��ݶ���ִ��ʱ��\n//runClassCom@\"nc.bs.scm.recordtime.RecordTimeImpl\",\"unrecord\",\"&sAction:STRING,&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//######################################################\n//��д�����Ͽͻ���ҵ��Ӧ�գ�ע������������ô˷����Ļ���Ӧע�͵�SAVEBASE&DELETE����ͬ����\n//������ظ���д��������ݴ���\n//runClassCom@ \"nc.bs.ic.ic211.GeneralHDMO\", \"updateCustBusiMny\", \"&USELESSVO:nc.vo.ic.pub.bill.GeneralBillVO,&INCURVOARAP:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//######################################################\n//������������,��д���ۼƻ�\n//runClassCom@\"nc.bs.ic.pub.RewriteDMO\",\"reWriteSLWhenUnSign\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"ERR\",\"\");\nsetParameter(\"FUN\",\"ȡ��ǩ��\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String\"@;\nnew IcToCRM().synchronizeSO(inCurVO,CrmSynchroVO.ICRM_DELETE);\n//############################\n}catch(Exception e){\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"EXC\",e.getMessage());\nsetParameter(\"FUN\",\"ȡ��ǩ��\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String\"@;\n//###########################\n        if (e instanceof nc.vo.pub.BusinessException)\n      throw (nc.vo.pub.BusinessException) e;\n    else\n      throw new nc.vo.pub.BusinessException(\"Remote Call\", e);\n}\nfinally{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݽ�ҵ����\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\ninCurVO=null;\nreturn retObj;\n//************************************************************************\n";}
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
