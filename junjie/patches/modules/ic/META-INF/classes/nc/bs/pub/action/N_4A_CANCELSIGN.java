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
 * ��ע�����������ⵥ��ȡ��ǩ��
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2007-4-30)
 * @author ƽ̨�ű�����
 */
public class N_4A_CANCELSIGN extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_4A_CANCELSIGN ������ע�⡣
 */
public N_4A_CANCELSIGN() {
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
if(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵������Ͳ�ƥ��"));
if(inCurObject == null)  throw new BusinessException("Remote Call",new nc.vo.pub.BusinessException("���󣺵���û������"));
//2,���ݺϷ���������ת��Ϊ��浥�ݡ�
nc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;
if(inCurObject !=null)
 inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;
inCurObject=null;
//��ȡƽ̨����Ĳ���
setParameter("INCURVO",inCurVO);
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
//����˵��:�ж��Ƿ���������Ļ���
runClass("nc.bs.ic.pub.check.CheckDMO","checkBillSumed","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
//����˵��:����ȡ��ǩ��
retObj=runClass("nc.bs.ic.ic207.GeneralHBO","cancelSignBill","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//�������ǰ�����������Ƿ�ƥ��
if(retObj != null && !(retObj instanceof  ArrayList))  throw new BusinessException("Remote Call",new nc.vo.pub.BusinessException("����ȡ��ǩ�ֶ����ķ���ֵ���ʹ���"));
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:ɾ������ӿڵ���
//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);
runClass("nc.bs.ic.pub.ictoia.Ic2IaDMO","deleteIABills","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);
//wanglei 2014-06-19
//����˵��:ɾ���ݹ�Ӧ�����ݼ���������
//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);
runClass("nc.bs.ic.ic211.GeneralHBO","delete4A2AP","&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO",vo,m_keyHas,m_methodReturnHas);

//############################
//����ҵ����־���÷�����������
setParameter("ERR","");
setParameter("FUN","ȡ��ǩ�� ");
runClass("nc.bs.ic.pub.check.CheckDMO","insertBusinesslog","&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//############################
//##################################################
}catch(Exception e){
//############################
//����ҵ����־���÷�����������
setParameter("ERR",e.getMessage());
setParameter("FUN","ȡ��ǩ��");
runClass("nc.bs.ic.pub.check.CheckBO","insertBusinessExceptionlog","&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
//###########################
        if (e instanceof BusinessException)
			throw (BusinessException) e;
		else
			throw new BusinessException("Remote Call", e);
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
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\nObject inCurObject=getVo();\nObject retObj=null;\n//1,���ȼ�鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif(!(inCurObject instanceof  nc.vo.ic.pub.bill.GeneralBillVO)) throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵������Ͳ�ƥ��\"));\nif(inCurObject == null)  throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"���󣺵���û������\"));\n//2,���ݺϷ���������ת��Ϊ��浥�ݡ�\nnc.vo.ic.pub.bill.GeneralBillVO inCurVO=null;\nif(inCurObject !=null)\n inCurVO=(nc.vo.ic.pub.bill.GeneralBillVO)inCurObject;\ninCurObject=null;\n//��ȡƽ̨����Ĳ���\nsetParameter(\"INCURVO\",inCurVO);\nObject alLockedPK=null;\ntry{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:����Ƿ���ʡ�<������>\n//Ŀǰ�Ǹ��ݵ��ݱ���ҵ�����ڼ�顣������ݵ�¼���ڼ�飬�뽫checkAccountStatus��ΪcheckAccountStatus1\nrunClassCom@\"nc.bs.ic.ic2a3.AccountctrlDMO\",\"checkAccountStatus\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//#############################################################\n//����˵��:������ⵥ�ݼ�ҵ����\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockICBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n//�÷���<��������>\n//����˵��:����浥��ʱ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//�÷���<��������>\n//����˵��:����Ƿ���ǩ�ֵĵ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckBusiDMO\",\"checkCancelSigningUser\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:�ж��Ƿ���������Ļ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkBillSumed\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//����˵��:����ȡ��ǩ��\nretObj=runClassCom@\"nc.bs.ic.ic207.GeneralHBO\",\"cancelSignBill\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//�������ǰ�����������Ƿ�ƥ��\nif(retObj != null && !(retObj instanceof  ArrayList))  throw new BusinessException(\"Remote Call\",new nc.vo.pub.BusinessException(\"����ȡ��ǩ�ֶ����ķ���ֵ���ʹ���\"));\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:ɾ������ӿڵ���\n//nc.bs.ic.pub.ictoia.Ic2IaDMO.deleteIABills(inCurVO);\nrunClassCom@\"nc.bs.ic.pub.ictoia.Ic2IaDMO\",\"deleteIABills\",\"&INCURVO:nc.vo.ic.pub.bill.GeneralBillVO\"@;\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"ERR\",\"\");\nsetParameter(\"FUN\",\"ȡ��ǩ�� \");\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"insertBusinesslog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String\"@;\n//############################\n//##################################################\n}catch(Exception e){\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"ERR\",e.getMessage());\nsetParameter(\"FUN\",\"ȡ��ǩ��\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ERR:String,&FUN:String\"@;\n//###########################\n        if (e instanceof BusinessException)\n			throw (BusinessException) e;\n		else\n			throw new BusinessException(\"Remote Call\", e);\n}\nfinally{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݽ�ҵ����\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\ninCurVO=null;\nreturn retObj; \n//************************************************************************\n";}
/*
* ��ע�����ýű�������HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
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
