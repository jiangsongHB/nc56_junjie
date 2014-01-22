package nc.ui.ja.entityrecrpit;

import java.util.Hashtable;


/**
 *
 *�����ҵ�������
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
		
	   Hashtable dataHashTable = new Hashtable();
	                         
           nc.vo.ja.entityrecrpit.JaEntityReceiptBVO[] bodyVOs1 =
                       (nc.vo.ja.entityrecrpit.JaEntityReceiptBVO[])queryByCondition(nc.vo.ja.entityrecrpit.JaEntityReceiptBVO.class,
                                                    getBodyCondition(nc.vo.ja.entityrecrpit.JaEntityReceiptBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("ja_entity_receipt_b",bodyVOs1);
            } 
            nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO[] bodyVOs2 =
                (nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO[])queryByCondition(nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO.class,
                                             getBodyCondition(nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO.class,key));   
     if(bodyVOs2 != null && bodyVOs2.length > 0){
                   
       dataHashTable.put("ja_entity_receipt_ck",bodyVOs2);
     } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.ja.entityrecrpit.JaEntityReceiptBVO.class)
	   return "ja_entity_receipt_b.pk_entity_receipt = '" + key + "' and isnull(ja_entity_receipt_b.dr,0)=0 ";
       	if(bodyClass ==  nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO.class)
     	   return "ja_entity_receipt_ck.pk_entity_receipt = '" + key + "' and isnull(ja_entity_receipt_ck.dr,0)=0 ";
	 return null;
       } 
	
}
