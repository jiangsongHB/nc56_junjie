package nc.vo.ja.entityrecrpit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;

import nc.vo.ja.entityrecrpit.JaEntityReceiptVO;
import nc.vo.ja.entityrecrpit.JaEntityReceiptBVO;	

/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO implements IExAggVO{

	/*public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (JaEntityReceiptBVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (JaEntityReceiptVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new JaEntityReceiptBVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((JaEntityReceiptVO)parent);
	}*/
	//����װ�ض��ӱ����ݵ�HashMap
	private HashMap hmChildVOs = new HashMap();
	
	
	/**
	 * ���ض���ӱ�ı���
	 * �����뵥��ģ���ҳǩ�����Ӧ
	 * �������ڣ�2013-11-19
	 * @return String[]
	 */
	public String[] getTableCodes(){
		          
		return new String[]{
		 		 		   		"ja_entity_receipt_b",
		   		 		   		"ja_entity_receipt_ck"
		   		    };
		          
	}
	
	
	/**
	 * ���ض���ӱ����������
	 * �������ڣ�2013-11-19
	 * @return String[]
	 */
	public String[] getTableNames(){
		
		return new String[]{
                                           "��Ʊ��ϸ",
                                            "��Ʊ�����嵥",
                         };
	}
	
	
	/**
	 * ȡ�������ӱ������VO����
	 * �������ڣ�2013-11-19
	 * @return CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getAllChildrenVO(){
		
		ArrayList al = new ArrayList();
		for(int i = 0; i < getTableCodes().length; i++){
			CircularlyAccessibleValueObject[] cvos
			        = getTableVO(getTableCodes()[i]);
			if(cvos != null)
				al.addAll(Arrays.asList(cvos));
		}
		
		return (SuperVO[]) al.toArray(new SuperVO[0]);
	}
	
	
	/**
	 * ����ÿ���ӱ��VO����
	 * �������ڣ�2013-11-19
	 * @return CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getTableVO(String tableCode){
		
		return (CircularlyAccessibleValueObject[])
		            hmChildVOs.get(tableCode);
	}
	
	
	/**
	 * 
	 * �������ڣ�2013-11-19
	 * @param SuperVO item
	 * @param String id
	 */
	public void setParentId(SuperVO item,String id){}
	
	/**
	 * Ϊ�ض��ӱ�����VO����
	 * �������ڣ�2013-11-19
	 * @param String tableCode
	 * @para CircularlyAccessibleValueObject[] vos
	 */
	public void setTableVO(String tableCode,CircularlyAccessibleValueObject[] vos){
		
		hmChildVOs.put(tableCode,vos);
	}
	
	/**
	 * ȱʡ��ҳǩ����
	 * �������ڣ�2013-11-19
	 * @return String 
	 */
	public String getDefaultTableCode(){
		
		return getTableCodes()[0];
	}
	
	/**
	 * 
	 * �������ڣ�2013-11-19
	 * @param String tableCode
	 * @param String parentId
	 * @return SuperVO[]
	 */
	public SuperVO[] getChildVOsByParentId(String tableCode,String parentId){
		
		return null;
	}
	
	
	/**
	 * 
	 * �������ڣ�2013-11-19
	 * @return HashMap
	 */
	public HashMap getHmEditingVOs() throws Exception{
		
		return null;
	}
	
	/**
	 * 
	 * ��������:2013-11-19
	 * @param SuperVO item
	 * @return String
	 */
	public String getParentId(SuperVO item){
		
		return null;
	}
}
