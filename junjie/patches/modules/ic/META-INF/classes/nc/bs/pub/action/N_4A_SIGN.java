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
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע�����������ⵥ��ǩ��
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-7-14)
 * @author ƽ̨�ű�����
 */
public class N_4A_SIGN extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4A_SIGN ������ע�⡣
 */
public N_4A_SIGN() {
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
nc.vo.pub.AggregatedValueObject[] inCurObjects=getVos();
StringBuffer sErr=new StringBuffer();
Object retObj=null;
if(inCurObjects== null||inCurObjects.length==0)  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵���û������"));
//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�
if(!(inCurObjects[0] instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵������Ͳ�ƥ��"));
//2,���ݺϷ���������ת��Ϊ��浥�ݡ�
nc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs=null;
inCurVOs=(nc.vo.ic.pub.bill.GeneralBillVO[])inCurObjects;
inCurObjects=null;
//��ȡƽ̨����Ĳ���
setParameter("INCURVOS",inCurVOs);
ArrayList alLockedPK=null;
setParameter("ILOCKS",(nc.vo.ic.pub.ILockIC[])inCurVOs);
try{
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:������ⵥ�ݼ�ҵ����
alLockedPK=(ArrayList)runClass("nc.bs.ic.pub.bill.ICLockBO","lockICBills","&INCURVOS:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:����Ƿ���ʡ�<������>
//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1
runClass("nc.bs.ic.ic2a3.AccountctrlDMO","checkAccountStatus","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//#############################################################
//����˵��:������ⵥ�ݼ�ҵ����
//alLockedPK=runClass("nc.bs.ic.pub.bill.GeneralBillBO","lockBills","&INCURVOS:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
//##################################################
//�÷���<��������>
//����˵��:����浥��ʱ���
runClass("nc.bs.ic.pub.check.CheckDMO","checkTimeStamps","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//�÷���<��������>
//����˵��:���ǩ��ǰ����
runClass("nc.bs.ic.pub.BillActionBase","beforeSign","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//############################
//У�����������ʹ������ �÷�����������
String sBarcodeCheckErr=(String)runClass("nc.bs.ic.pub.check.CheckBarcodeDMO","checkBarcodeAbsent","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
if (sBarcodeCheckErr!=null )
sErr.append(sBarcodeCheckErr);
//#################
//����˵��:����ǩ��
runClass("nc.bs.ic.ic207.GeneralHBO","signBills","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//############################
//�÷���<��������>
//����˵��:���ǩ�ֺ���
runClass("nc.bs.ic.pub.BillActionBase","afterSign","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]",vo,m_keyHas,m_methodReturnHas);
//����ҵ����־���÷�����������
setParameter("ERR",sErr.toString());
setParameter("FUN","ǩ��");
runClass("nc.bs.ic.pub.check.CheckDMO","insertBusinesslog","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//############################
//�������ǰ�����������Ƿ�ƥ��
if(retObj != null && !(retObj instanceof  ArrayList))  throw new nc.vo.pub.BusinessException("Remote Call",new nc.vo.pub.BusinessException("����ǩ�ֶ����ķ���ֵ���ʹ���"));
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:���ɴ�����ݽӿ�
//nc.bs.ic.pub.ictoia.Ic2IaDMO.saveIABills(inCurVOs,"4A","I4",vo);
setParameter("icbilltype","4A");
setParameter("iabilltype","I4");
setParameter("PFPARAVO",vo);
runClass("nc.bs.ic.pub.ictoia.Ic2IaDMO","saveIABills","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO",vo,m_keyHas,m_methodReturnHas);
//##################################################

//########### add by ouyangzhb 2012-12-14 ������ⵥǩ���Ƴ������ݹ�Ӧ���Լ�������###########
ClientLink cl = clientLinkCreate(vo);
setParameter("ClientLink", cl);
runClass("nc.bs.ic.ic211.GeneralHBO","push4AToAPAndIA","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ClientLink:nc.vo.scm.pub.session.ClientLink",vo,m_keyHas,m_methodReturnHas);
//########### add by ouyangzhb 2012-12-14 ������ⵥǩ���Ƴ������ݹ�Ӧ���Լ�������###########

}catch(Exception e){
//############################
//����ҵ����־���÷�����������
setParameter("ERR",e.getMessage());
setParameter("FUN","ǩ�� ");
runClass("nc.bs.ic.pub.check.CheckBO","insertBusinessExceptionlog","&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//###########################
//���������״̬���˻ص��ݺ�
for(int i=0;i<inCurVOs.length;i++){
setParameter("IBC",(nc.vo.scm.pub.IBillCode)inCurVOs[i]);
if(inCurVOs[i].getHeaderVO().getStatus()==nc.vo.pub.VOStatus.NEW)
         runClass("nc.bs.ic.pub.check.CheckDMO","returnBillCode","&IBC:nc.vo.scm.pub.IBillCode",vo,m_keyHas,m_methodReturnHas);
}
          if (e instanceof nc.vo.pub.BusinessException)
      throw (nc.vo.pub.BusinessException) e;
    else
      throw new nc.vo.pub.BusinessException("Remote Call", e);
}finally{
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:������ⵥ�ݽ�ҵ����
if(alLockedPK!=null){
setParameter("ALPK",alLockedPK);
 runClass("nc.bs.ic.pub.bill.ICLockBO","unlockBills","&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
}//##################################################
}
ArrayList alRet=new ArrayList();
if(sErr.toString().trim().length()==0)
  alRet.add(null);
else
  alRet.add(sErr.toString());
alRet.add(retObj);
//���С�͵���VO��ǰ̨���� 
nc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();
alRet.add(smbillvo);
inCurVOs=null;
return new Object[]{alRet};
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
  return "  //####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\nnc.vo.pub.AggregatedValueObject[] inCurObjects=getVos();\nStringBuffer sErr=new StringBuffer();\nObject retObj=null;\nif(inCurObjects== null||inCurObjects.length==0)  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵���û������\"));\n//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif(!(inCurObjects[0] instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵������Ͳ�ƥ��\"));\n//2,���ݺϷ���������ת��Ϊ��浥�ݡ�\nnc.vo.ic.pub.bill.GeneralBillVO[] inCurVOs=null;\ninCurVOs=(nc.vo.ic.pub.bill.GeneralBillVO[])inCurObjects;\ninCurObjects=null;\n//��ȡƽ̨����Ĳ���\nsetParameter(\"INCURVOS\",inCurVOs);\nArrayList alLockedPK=null;\nsetParameter(\"ILOCKS\",(nc.vo.ic.pub.ILockIC[])inCurVOs);\ntry{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݼ�ҵ����\nalLockedPK=(ArrayList)runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockICBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:����Ƿ���ʡ�<������>\n//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//#############################################################\n//����˵��:������ⵥ�ݼ�ҵ����\n//alLockedPK=runClassCom@\"nc.bs.ic.pub.bill.GeneralBillBO\",\"lockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[]\"@;\n//##################################################\n//�÷���<��������>\n//����˵��:����浥��ʱ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamps\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//�÷���<��������>\n//����˵��:���ǩ��ǰ����\nrunClassCom@\"nc.bs.ic.pub.BillActionBase\",\"beforeSign\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//############################\n//У�����������ʹ������ �÷�����������\nString sBarcodeCheckErr=(String)runClassCom@\"nc.bs.ic.pub.check.CheckBarcodeDMO\",\"checkBarcodeAbsent\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\nif (sBarcodeCheckErr!=null )\nsErr.append(sBarcodeCheckErr);\n//#################\n//����˵��:����ǩ��\nrunClassCom@\"nc.bs.ic.ic207.GeneralHBO\",\"signBills\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//############################\n//�÷���<��������>\n//����˵��:���ǩ�ֺ���\nrunClassCom@\"nc.bs.ic.pub.BillActionBase\",\"afterSign\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[]\"@;\n//����ҵ����־���÷�����������\nsetParameter(\"ERR\",sErr.toString());\nsetParameter(\"FUN\",\"ǩ��\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String\"@;\n" +
      "//############################\n//�������ǰ�����������Ƿ�ƥ��\nif(retObj != null && !(retObj instanceof  ArrayList))  throw new nc.vo.pub.BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"����ǩ�ֶ����ķ���ֵ���ʹ���\"));\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:���ɴ�����ݽӿ�\n//nc.bs.ic.pub.ictoia.Ic2IaDMO.saveIABills(inCurVOs,\"4A\",\"I4\",vo);\nsetParameter(\"icbilltype\",\"4A\");\nsetParameter(\"iabilltype\",\"I4\");\nsetParameter(\"PFPARAVO\",vo);\nrunClassCom@\"nc.bs.ic.pub.ictoia.Ic2IaDMO\",\"saveIABills\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&icbilltype:String,&iabilltype:String,&PFPARAVO:nc.vo.pub.compiler.PfParameterVO\"@;\n//##################################################\n}catch(Exception e){\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"ERR\",e.getMessage());\nsetParameter(\"FUN\",\"ǩ�� \");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVOS:nc.vo.ic.pub.bill.GeneralBillVO[],&ERR:String,&FUN:String\"@;\n//###########################\n//���������״̬���˻ص��ݺ�\nfor(int i=0;i<inCurVOs.length;i++){\nsetParameter(\"IBC\",(nc.vo.scm.pub.IBillCode)inCurVOs[i]);\nif(inCurVOs[i].getHeaderVO().getStatus()==nc.vo.pub.VOStatus.NEW)\n         runClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"returnBillCode\",\"&IBC:nc.vo.scm.pub.IBillCode\"@;\n}\n          if (e instanceof nc.vo.pub.BusinessException)\n     throw (nc.vo.pub.BusinessException) e;\n    else\n      throw new nc.vo.pub.BusinessException(\"Remote Call\", e);\n}finally{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݽ�ҵ����\nif(alLockedPK!=null){\nsetParameter(\"ALPK\",alLockedPK);\n runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBills\",\"&INCURVOS:nc.vo.pub.AggregatedValueObject[],&ALPK:ArrayList\"@;\n}//##################################################\n}\nArrayList alRet=new ArrayList();\nif(sErr.toString().trim().length()==0)\n alRet.add(null);\nelse\n  alRet.add(sErr.toString());\nalRet.add(retObj);\n//���С�͵���VO��ǰ̨���� \nnc.vo.ic.pub.smallbill.SMGeneralBillVO smbillvo = inCurVOs[0].getSmallBillVO();\nalRet.add(smbillvo);\ninCurVOs=null;\nreturn new Object[]{alRet};\n//************************************************************************\n";}
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

/**
 * add by ouyangzhb 2012-09-22 ��ȡ ��ǰ�ͻ���������Ϣ
 */
public ClientLink clientLinkCreate(PfParameterVO pfVo)
{
    String pk_corp = pfVo.m_coId;
    String cuserid = pfVo.m_operator;
    UFDate date = new UFDate(pfVo.m_currentDate);
    String accountYear = pfVo.m_currentDate.substring(0, 4);
    String accountMonth = pfVo.m_currentDate.substring(5, 7);;
    String yearMonth = null;
    UFDate monthStart = null;
    UFDate monthEnd = null;
    String language = null;
    boolean isDebug = false;
    String moduleName = null;
    String moduleCode = null;
    String moduleID = null;
    ClientLink clientLink = new ClientLink(pk_corp, cuserid, date, accountYear, accountMonth, yearMonth, monthStart, monthEnd, language, isDebug, moduleName, moduleCode, moduleID);
    return clientLink;
}

}
