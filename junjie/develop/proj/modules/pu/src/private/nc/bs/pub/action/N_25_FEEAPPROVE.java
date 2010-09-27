package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע���ɹ���Ʊ������
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2008-10-7)
 * @author ƽ̨�ű�����
 */
public class N_25_FEEAPPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_25_APPROVE ������ע�⡣
 */
public N_25_FEEAPPROVE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	Object[] inObject  =getVos ();
	Object inObject1  = getUserObj ();
	Hashtable m_sysHasNoPassAndGonging = null;
	if (inObject == null) throw new nc.vo.pub.BusinessException ( "������ϣ�������Ĳɹ���Ʊû������");
	nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;
	setParameter ( "INVO",inVO);
	setParameter ( "INVO1",inObject1);
  
	Object oLockRet = null;
	try {
		//�Բɹ���Ʊ����ҵ����
		oLockRet=runClass( "nc.bs.pu.pub.PubDMO", "setDynamicLockForPksOfVos", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
  
		//��������
		runClass( "nc.bs.pu.pub.PubDMO", "checkVosNoChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
		runClass( "nc.bs.pu.pub.PubDMO", "isBillStateChanged", "&INVO:nc.vo.pub.AggregatedValueObject[]",vo,m_keyHas,m_methodReturnHas);
  
		m_sysHasNoPassAndGonging = procFlowBacth(vo);
      
		String key[] = new String[inVO.length];
		for(int i = 0; i < key.length; i++) key[i] = inVO[i].getHeadVO().getCinvoiceid();
			setParameter("KEYS",key);
			Hashtable t = (Hashtable)runClass( "nc.bs.pi.InvoiceDMO", "queryForSaveAuditBatch", "&KEYS:String[]",vo,m_keyHas,m_methodReturnHas);
			if(t != null && t.size() > 0){
				for(int i = 0; i < inVO.length; i++){
					java.util.ArrayList list = (java.util.ArrayList)t.get(inVO[i].getHeadVO().getCinvoiceid());
					inVO[i].getHeadVO().setDauditdate((nc.vo.pub.lang.UFDate)list.get(0));
					inVO[i].getHeadVO().setCauditpsn((String)list.get(1));
 					inVO[i].getHeadVO().setIbillstatus((Integer)list.get(2));
					inVO[i].getHeadVO().setTs((String)list.get(3));
				}
			}
	}catch (Exception e) {
		nc.bs.pu.pub.PubDMO.throwBusinessException("nc.bs.pub.action.N_25_APPROVE",e);
	}
    
	setParameter ( "PFDATE",getUserDate().toString());
	//����ʱ�Զ�����
	Object retJS = runClass("nc.bs.pi.InvoiceImpl","doSettleArray","nc.vo.pi.InvoiceVO[]:25,&PFDATE:String",vo,m_keyHas,m_methodReturnHas);
    
	//V55 1��  ע�͵����˵����ⷢƱ����;2�� ע�͵����˵�����ݹ�Ӧ������
	//����˵��:�˵����ⷢƱ,���ط����ⷢƱ����
	Object filteredObj = runClass("nc.bs.pi.InvoiceImpl","filterVirtualInvoice","nc.vo.pi.InvoiceVO[]:25",vo,m_keyHas,m_methodReturnHas);
  
	//�˵����ⷢƱ����֯�ݹ�Ӧ�����VO, ����Ӧ���ĵ���ӿ�, ����ݹ�Ӧ��
	if (filteredObj != null){
		nc.vo.pub.lang.UFBoolean bSucceed = new nc.vo.pub.lang.UFBoolean(false);
		nc.vo.pi.InvoiceVO[] filteredVOs = (nc.vo.pi.InvoiceVO[]) filteredObj;
		if(retJS != null){
			java.util.ArrayList list = (java.util.ArrayList)retJS;
			if(list != null && list.size() > 1){
				bSucceed = (nc.vo.pub.lang.UFBoolean)list.get(1);
				if(bSucceed.booleanValue()){
					//����ɹ���,�����ۼƽ����������ۼƽ�����,����������ݹ�Ӧ�����
					for(int i = 0; i < filteredVOs.length; i++){
						nc.vo.pi.InvoiceItemVO bodyVO[] = filteredVOs[i].getBodyVO();
						for(int j = 0; j < bodyVO.length; j++){
							bodyVO[j].setNaccumsettnum(bodyVO[j].getNinvoicenum());
							bodyVO[j].setNaccumsettmny(bodyVO[j].getNmoney());
						}
					}
				}
			}
		}
		//�����ݹ�Ӧ����嵥
		//add by QuSida (��ɽ����) 2010-9-25
		nc.bs.pi.InvoiceImpl zgyf = new nc.bs.pi.InvoiceImpl();
		zgyf.adjustForFeeZGYF(filteredVOs);
    	}

	//д������־ 
  setParameter("BILLTYPE", "25");
  setParameter("ACTIONNAME", "����");
  runClass("nc.bs.pu.pub.PubImpl","insertOperateLog","&INVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String",vo,m_keyHas,m_methodReturnHas);
  
	nc.bs.pub.compiler.BatchWorkflowRet bwr = new nc.bs.pub.compiler.BatchWorkflowRet();
	bwr.setNoPassAndGoing(m_sysHasNoPassAndGonging);
	bwr.setUserObj(getVos()); 
      	//
	return new Object[]{bwr};
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
	return "	super.m_tmpVo=vo;\n Object[] inObject  =getVos ();\n  Object inObject1  = getUserObj ();\n  Hashtable m_sysHasNoPassAndGonging = null;\n  if (inObject == null) throw new nc.vo.pub.BusinessException ( \"������ϣ�������Ĳɹ���Ʊû������\");\n nc.vo.pi.InvoiceVO[] inVO  = (nc.vo.pi.InvoiceVO[])inObject;\n  setParameter ( \"INVO\",inVO);\n  setParameter ( \"INVO1\",inObject1);\n  \n  Object oLockRet = null;\n try {\n   //�Բɹ���Ʊ����ҵ����\n    oLockRet=runClass( \"nc.bs.pu.pub.PubDMO\", \"setDynamicLockForPksOfVos\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n  \n   //��������\n    runClass( \"nc.bs.pu.pub.PubDMO\", \"checkVosNoChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n   runClass( \"nc.bs.pu.pub.PubDMO\", \"isBillStateChanged\", \"&INVO:nc.vo.pub.AggregatedValueObject[]\",vo,m_keyHas,m_methodReturnHas);\n  \n    m_sysHasNoPassAndGonging = procFlowBacth(vo);\n      \n   String key[] = new String[inVO.length];\n   for(int i = 0; i < key.length; i++) key[i] = inVO[i].getHeadVO().getCinvoiceid();\n     setParameter(\"KEYS\",key);\n     Hashtable t = (Hashtable)runClass( \"nc.bs.pi.InvoiceDMO\", \"queryForSaveAuditBatch\", \"&KEYS:String[]\",vo,m_keyHas,m_methodReturnHas);\n      if(t != null && t.size() > 0){\n        for(int i = 0; i < inVO.length; i++){\n         java.util.ArrayList list = (java.util.ArrayList)t.get(inVO[i].getHeadVO().getCinvoiceid());\n         inVO[i].getHeadVO().setDauditdate((nc.vo.pub.lang.UFDate)list.get(0));\n          inVO[i].getHeadVO().setCauditpsn((String)list.get(1));\n          inVO[i].getHeadVO().setIbillstatus((Integer)list.get(2));\n         inVO[i].getHeadVO().setTs((String)list.get(3));\n       }\n     }\n }catch (Exception e) {\n    nc.bs.pu.pub.PubDMO.throwBusinessException(\"nc.bs.pub.action.N_25_APPROVE\",e);\n  }\n    \n setParameter ( \"PFDATE\",getUserDate().toString());\n  //����ʱ�Զ�����\n Object retJS = runClass(\"nc.bs.pi.InvoiceImpl\",\"doSettleArray\",\"nc.vo.pi.InvoiceVO[]:25,&PFDATE:String\",vo,m_keyHas,m_methodReturnHas);\n    \n //V55 1��  ע�͵����˵����ⷢƱ����;2�� ע�͵����˵�����ݹ�Ӧ������\n  //����˵��:�˵����ⷢƱ,���ط����ⷢƱ����\n Object filteredObj = runClass(\"nc.bs.pi.InvoiceImpl\",\"filterVirtualInvoice\",\"nc.vo.pi.InvoiceVO[]:25\",vo,m_keyHas,m_methodReturnHas);\n  \n //�˵����ⷢƱ����֯�ݹ�Ӧ�����VO, ����Ӧ���ĵ���ӿ�, ����ݹ�Ӧ��\n if (filteredObj != null){\n   nc.vo.pub.lang.UFBoolean bSucceed = new nc.vo.pub.lang.UFBoolean(false);\n    nc.vo.pi.InvoiceVO[] filteredVOs = (nc.vo.pi.InvoiceVO[]) filteredObj;\n    if(retJS != null){\n      java.util.ArrayList list = (java.util.ArrayList)retJS;\n      if(list != null && list.size() > 1){\n        bSucceed = (nc.vo.pub.lang.UFBoolean)list.get(1);\n       if(bSucceed.booleanValue()){\n          //����ɹ���,�����ۼƽ����������ۼƽ�����,����������ݹ�Ӧ�����\n         for(int i = 0; i < filteredVOs.length; i++){\n            nc.vo.pi.InvoiceItemVO bodyVO[] = filteredVOs[i].getBodyVO();\n           for(int j = 0; j < bodyVO.length; j++){\n             bodyVO[j].setNaccumsettnum(bodyVO[j].getNinvoicenum());\n             bodyVO[j].setNaccumsettmny(bodyVO[j].getNmoney());\n            }\n         }\n       }\n     }\n   }\n     }\n //д������־ \n  setParameter(\"BILLTYPE\", \"25\");\n  setParameter(\"ACTIONNAME\", \"����\");\n  runClass(\"nc.bs.pu.pub.PubImpl\",\"insertOperateLog\",\"&INVO:nc.vo.pub.AggregatedValueObject[],&ACTIONNAME:java.lang.String,&BILLTYPE:java.lang.String\",vo,m_keyHas,m_methodReturnHas);\n  \n  nc.bs.pub.compiler.BatchWorkflowRet bwr = new nc.bs.pub.compiler.BatchWorkflowRet();\n  bwr.setNoPassAndGoing(m_sysHasNoPassAndGonging);\n  bwr.setUserObj(getVos()); \n        //\n  return new Object[]{bwr};\n";}
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
}

