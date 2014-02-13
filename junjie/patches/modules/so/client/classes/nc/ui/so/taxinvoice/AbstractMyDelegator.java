package nc.ui.so.taxinvoice;

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
           
           nc.vo.so.TaxInvoiceItemVO[] bodyVOs1 =
                       (nc.vo.so.TaxInvoiceItemVO[])queryByCondition(nc.vo.so.TaxInvoiceItemVO.class,
                                                    getBodyCondition(nc.vo.so.TaxInvoiceItemVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("ctaxinvoiceitem",bodyVOs1);
            }  
	         
	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
	 if(bodyClass == nc.vo.so.TaxInvoiceItemVO.class)
	   return "ctaxinvoiceid = '" + key + "' and isnull(dr,0)=0 ";
		
	 return null;
       } 
	
}
