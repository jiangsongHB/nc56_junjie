package nc.ui.po.pub;

/**
 * ��ӡ������֯��
 * ���ߣ�����
 * @version		2001/07/17
 * @see			IDataSource
 */

//JAVA
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComponent;

import nc.ui.ic.pub.lot.LotNumbRefPane;
import nc.ui.po.ref.PoReceiveAddrRefModel;
import nc.ui.po.rp.PoReceivePlanHelper;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.print.IDataSource;
import nc.ui.scm.ic.freeitem.FreeItemRefPane;
import nc.vo.po.rp.OrderReceivePlanVO;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;

public class OrderPrintData implements IDataSource {

	//���ӡ�Ŀ�ƬPanel
	private BillCardPanel      	m_pnlCard = null;

	private	String				m_sPrintID = null ;

	//ҵ�����񾫶�
	private	PoPrintDigitManager	m_mngDigit = null ;

    //�ɹ����������ƻ�����ģ�壺�ϲ���ӡ
    private BillCardPanel m_bcp_po_rp = null;
    //��ǰҪ��ӡ�Ķ���VO������˾
    private String m_pk_corp = null;
    /**
 * OrderPrintData ������ע�⡣
 * @param
 * @return
 * @exception
 * @see
 * @since		2001-07-17
*/
public OrderPrintData() {
	super();
}
/**
 * �õ����е���������ʽ����
 * Ҳ���Ƿ������ж����������ı��ʽ
 * @param
 * @return		String[]  �ֶμ�ֵ��������һ�����ʽ
 * @exception
 * @see
 * @since		2001-07-17
*/
public java.lang.String[] getAllDataItemExpress() {

	return	getItemExpressFromCard();
}
	/*
	�������ֱ��ģ��õ��Ľ����һ���������Ķ��巽ʽ��ͬ��	��������

		(1) ÿһ��������(�Ʊ��˳���) ����������չ,û���κ�������ϵ
		------------------------------------------
	 	���\��Ŀ	|  ��Ŀ01	    |	 ��Ŀ02
	  	----------------------------------
	   	(����)	��	| (��Ŀ01)��	|	(��Ŀ02)��
	    ------------------------------------------
	    �Ʊ���:	(�Ʊ���)

		(2) (����) ����չ (��Ŀ) ����չ (���) ������ (��Ŀ)
	    ------------------------
	 	���\��Ŀ	| (��Ŀ) ��
	  	------------------------
	   	(����)	��	| (���)
	     -----------------------
	    �Ʊ���:	(�Ʊ���)

	   	(3) (����) ����չ (��Ŀ) ����չ (���) ������ (��Ŀ ����)
		------------------------
	 	���\��Ŀ	| (��Ŀ) ��
	  	------------------------
	   	(����)	��	| (���)
	    ------------------------
	 	�Ʊ���:	(�Ʊ���)

	    ��ӡ���:
	     --------------------------------
	 	 ���\��Ŀ	|	��Ŀ1 	| ��Ŀ2
	 	 --------------------------------
	 		1999	|  	100	  	|	400
	 		2000	|  	200	 	| 	500
	 		3001	|  	300	 	| 	600
	 	 --------------------------------
	 	 �Ʊ���: xxx
	  */
public java.lang.String[] getAllDataItemNames() {

	return	getItemNamesFromCard();
}
/**
 *
 * ������������������飬���������ֻ��Ϊ 1 ���� 2
 * ���� null : 		û������
 * ���� 1 :			��������
 * ���� 2 :			˫������
 *
 */
public java.lang.String[] getDependentItemExpressByExpress(String itemName) {
	return null;
}
/**
 * ���ݿ�Ƭ����õ����е���������ʽ����
 * Ҳ���Ƿ������ж����������ı��ʽ
 * @param
 * @return		String[]  �ֶμ�ֵ��������һ�����ʽ
 * @exception
 * @see
 * @since		2001-07-17
*/
private java.lang.String[] getItemExpressFromCard() {

	if (m_pnlCard == null){
		return null;
	}

	Vector vecExpress = new Vector();

	//��ͷ
	if (m_pnlCard.getHeadItems() != null) {
		for (int i = 0; i < m_pnlCard.getHeadItems().length; i++) {
			vecExpress.addElement((m_pnlCard.getHeadItems())[i].getKey());
		}
	}

	//����
	if (m_pnlCard.getBodyItems() != null) {
		for (int i = 0; i < m_pnlCard.getBodyItems().length; i++) {
			vecExpress.addElement((m_pnlCard.getBodyItems())[i].getKey());
		}
	}

	//��β
	if (m_pnlCard.getTailItems() != null) {
		for (int i = 0; i < m_pnlCard.getTailItems().length; i++) {
			vecExpress.addElement((m_pnlCard.getTailItems())[i].getKey());
		}
	}

	return vecExpress.size()==0 ? null : (String[])vecExpress.toArray(new	String[vecExpress.size()]);
}
/**
 * ���ݿ�Ƭ����õ����е���������������
 * Ҳ���Ƿ������ж���������������
 * @param
 * @return		String[]  �ֶμ�ֵ��������һ�����ʽ
 * @exception
 * @see
 * @since		2001-07-17
*/
private java.lang.String[] getItemNamesFromCard() {

	if (m_pnlCard == null){
		return null;
	}

	Vector vecName = new Vector();

	//��ͷ
	if (m_pnlCard.getHeadItems() != null) {
		for (int i = 0; i < m_pnlCard.getHeadItems().length; i++) {
			vecName.addElement((m_pnlCard.getHeadItems())[i].getName());
		}
	}

	//����
	if (m_pnlCard.getBodyItems() != null) {
		for (int i = 0; i < m_pnlCard.getBodyItems().length; i++) {
			vecName.addElement((m_pnlCard.getBodyItems())[i].getName());
		}
	}

	//��β
	if (m_pnlCard.getTailItems() != null) {
		for (int i = 0; i < m_pnlCard.getTailItems().length; i++) {
			vecName.addElement((m_pnlCard.getTailItems())[i].getName());
		}
	}

	return vecName.size()==0 ? null : (String[])vecName.toArray(new	String[vecName.size()]);
}
/*
 * �������е��������Ӧ������
 * ������ �����������
 * ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]��
 * 		  ��� itemName ӵ���������
 * 		  1 ���������ӡϵͳ����������������ݵ�˳�����ж� String[] �еĴ�ŵ�����
 *		  2 ���������ӡϵͳ�������������������������������

 	ģ�� 2 �����:
 			[��Ŀ]      ==>	  [100 200 300 -->  400 500 600]

 	ģ�� 3 �����: ��� getDependItemNamesByName("���") ==

			[��Ŀ ����]  ==>  [100 200 300 400 500 600] ���к���
			[���� ��Ŀ]  ==>  [100 400 200 500 300 600]	���к���
 * 2002-09-23	wyf		����һ���µĺ������Ա������д���
 */
public java.lang.String[] getItemValuesByExpress(String saItemExpress){

	String[] saData = null;

	if (m_pnlCard	instanceof	nc.ui.pub.report.ReportBaseClass) {
		saData = getItemValuesByExpressFromReport(saItemExpress);
	} else {
    	if(saItemExpress.startsWith("po_rp_")){
    	    saData = getItemValuesByExpressFromCard_po_rp(saItemExpress);
    	}else{
    	    saData = getItemValuesByExpressFromCard(saItemExpress);
    	}
	}

	return saData;
}
/**
 * ���ߣ�czp
 * ���ܣ����ݵ�ǰVO����KEY������Ӧ��������
 * ������String KEY��BillCardPanelTool ����ģ�幤��
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-14 11:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * */
private java.lang.String[] getItemValuesByExpressFromCard_po_rp(String itemExpress) {
	if (m_pnlCard == null){
		return null;
	}
    OrderReceivePlanVO[] voaRP = null;
	String strHid = null;
	int jLen = 0;
	int[] jaRow = null;
	Vector vValue = new Vector();
	String strOldPkCorp = m_pk_corp;
	strHid = (String) m_pnlCard.getHeadItem("corderid").getValue();
	if(strHid == null){
	    return  null;
	}
	try {
		voaRP = PoReceivePlanHelper.queryPlanVOsByHId(strHid);
    } catch (Exception e) {
        SCMEnv.out("���ݶ���ID��ѯ�����ƻ�ʱ�����쳣"+e.getMessage());
        return null;
    }
	if(voaRP == null || voaRP.length == 0){
	    return null;
	}
	m_pk_corp = voaRP[0].getPk_corp();
	if(m_pk_corp == null) {
	    return null;
	}
	getBillCardPanel_po_rp(!m_pk_corp.equalsIgnoreCase(strOldPkCorp)).getBillModel().setBodyDataVO(voaRP) ;

	jLen = voaRP.length ;
	jaRow = new	int[jLen] ;
	for (int j = 0; j < jLen; j++){
		jaRow[j] = j ;
	}
	execLoadValue(jaRow,voaRP) ;
	
	//=====================��������
	/*V31 Del {Error : ������ʾ��false��}
	oValue= m_bcpTool.getBodyColValues(itemExpress.substring(6),0,jLen);
	if(oValue != null){
	    jLen = oValue.length;
		for (int j = 0; j < jLen; j++){
		    vValue.addElement(oValue[j] == null ? null : oValue[j].toString());
		}
	}
	*/
	if (m_bcp_po_rp.getBodyItems()!=null) {
		int		iBLen = m_bcp_po_rp.getBodyItems().length ;
		for(int i=0;i<iBLen;i++){
		    if ( m_bcp_po_rp.getBodyItems()[i].getKey().trim().equals(itemExpress.substring(6)) ){
				for(int j=0;j<m_bcp_po_rp.getRowCount();j++){
				    vValue.addElement( getValueForCardBody(m_bcp_po_rp,m_bcp_po_rp.getBodyItem(itemExpress.substring(6)),j) )  ;
				}
				break;
			}
		}
	}
	//�������ݣ�����
	int iLen = vValue.size();
	String[] saAll = null;
	if(iLen > 0){
	    saAll = new String[iLen];
	    vValue.copyInto(saAll);
	}
	return saAll;
}
/**
 * ���ߣ�czp
 * ���ܣ���ʾ�����ƻ�����
 * ������int[]				iaRow		����Щ�������ã�����Ϊ��
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-14 11:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void execLoadValue(int[] iaRow,OrderReceivePlanVO[] voaRP) {

	//����VO
	int iLen = (iaRow == null ? getBillCardPanel_po_rp(false).getRowCount() : iaRow.length);
	int iRow = -1;
	HashMap mapBidRowNo = new HashMap();
	if(m_pnlCard != null && m_pnlCard.getBillModel() != null){
	    BillModel bm = m_pnlCard.getBillModel();
	    int iBodyLen = m_pnlCard.getRowCount();
	    for (int i = 0; i < iBodyLen; i++) {
            if (bm.getValueAt(i,"corder_bid") == null) {
                continue;
            }
            if(bm.getValueAt(i,"crowno") == null){
                bm.setValueAt("",i,"crowno") ;
            }
            mapBidRowNo.put(bm.getValueAt(i,"corder_bid"),bm.getValueAt(i,"crowno"));
        }
	}
	for (int i = 0; i < iLen; i++) {
		iRow = iaRow[i];
		//����
		if (voaRP[i]==null) {
			continue ;
		}
		//�ر�
		getBillCardPanel_po_rp(false).setBodyValueAt(
			voaRP[i].isActive() ? VariableConst.UFBOOLEAN_FALSE : VariableConst.UFBOOLEAN_TRUE,
			iRow,
			"borderrowclose");
		//�Ƿ���Ʒ
		getBillCardPanel_po_rp(false).setBodyValueAt(
			(voaRP[i].getBlargess() != null && voaRP[i].getBlargess().booleanValue()) ? VariableConst.UFBOOLEAN_TRUE : VariableConst.UFBOOLEAN_FALSE,
			iRow,
			"blargess");
		//�к�
		getBillCardPanel_po_rp(false).setBodyValueAt(mapBidRowNo.get(voaRP[i].getCorder_bid()), iRow, "crowno");
	}

	//ִ�й�ʽ
	getBillCardPanel_po_rp(false).getBillModel().execLoadFormula();
	//���ػ�����
	//				[0]	�������ID		NAME
	//				[1] ����λID			NAME
	//				[2] ������			NAME
	//				[3] ������			NAME
	//				[4] ������			NAME
	PuTool.setBillModelConvertRate(
		getBillCardPanel_po_rp(false).getBillModel(),
		new String[] { "cbaseid", "cassistunit", "nordernum", "nassistnum", "nconvertrate" });
	//����������
	PoPublicUIClass.setFreeColValue(getBillCardPanel_po_rp(false).getBillModel(), "vfree");

	//������״̬
	getBillCardPanel_po_rp(false).updateValue();
}
/*
 * �������е��������Ӧ������
 * ������ �����������
 * ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]��
 * 		  ��� sItemExpress ӵ���������
 * 		  1 ���������ӡϵͳ����������������ݵ�˳�����ж� String[] �еĴ�ŵ�����
 *		  2 ���������ӡϵͳ�������������������������������

 	ģ�� 2 �����:
 			[��Ŀ]      ==>	  [100 200 300 -->  400 500 600]

 	ģ�� 3 �����: ��� getDependitemExpresssByName("���") ==

			[��Ŀ ����]  ==>  [100 200 300 400 500 600] ���к���
			[���� ��Ŀ]  ==>  [100 400 200 500 300 600]	���к���

 */
private java.lang.String[] getItemValuesByExpressFromCard(String sItemExpress) {

	if (m_pnlCard == null){
		return null;
	}


	Vector vecValue = new Vector();
	//==================��ͷ����
	boolean 	bFound = false ;
	if (sItemExpress.indexOf("h_")==0 && m_pnlCard.getHeadItems()!=null) {
		sItemExpress = sItemExpress.substring("h_".length(),sItemExpress.length()) ;

		int		iHLen = m_pnlCard.getHeadItems().length ;
		for(int i=0;i<iHLen;i++){

			BillItem	itemH = m_pnlCard.getHeadItems()[i] ;

			if ( itemH.getKey().trim().equals(sItemExpress.trim()) ){
				vecValue.addElement( getValueForCardHeadTail(itemH) ) ;
				bFound = true;
				break;
			}
		}
	}

	//=====================��β
	if(!bFound && sItemExpress.indexOf("t_")==0 && m_pnlCard.getTailItems()!=null) {
		sItemExpress = sItemExpress.substring("t_".length(),sItemExpress.length()) ;

		for(int i=0;i<m_pnlCard.getTailItems().length;i++){
			if (m_pnlCard.getTailItems()[i].getKey().trim().equals(sItemExpress.trim())){
				vecValue.addElement( getValueForCardHeadTail(m_pnlCard.getTailItems()[i]) );
				bFound = true;
				break;
			}
		}
	}

	//=====================��������
	if (!bFound && m_pnlCard.getBodyItems()!=null) {
		int		iBLen = m_pnlCard.getBodyItems().length ;
		
			
			
				for(int i=0;i<iBLen;i++){

		    if ( m_pnlCard.getBodyItems()[i].getKey().trim().equals(
			    sItemExpress.trim()) ){
		    	
		    	
				for(int j=0;j<m_pnlCard.getRowCount();j++){
//				  //������Ʒ��  by zhaoyha ��������NCdp201070734
//				  UFBoolean blargess= PuPubVO.getUFBoolean_NullAs(
//				      m_pnlCard.getBodyValueAt(j, "blargess"),UFBoolean.FALSE);
//
////				    PuPubVO.getUFBoolean_NullAs(
////				      m_pnlCard.getBodyItem("blargess").getValueObject(),UFBoolean.FALSE);
//				  if(blargess.booleanValue() && ("noriginaltaxpricemny".equals(sItemExpress)
//				      || "noriginalcurmny".equals(sItemExpress)
//				      || "noriginaltaxmny".equals(sItemExpress)
//				      )
//				      ){
//				      vecValue.addElement(null);
//				  }
//				  else
				    vecValue.addElement( getValueForCardBody(
				        m_pnlCard.getBodyItem(sItemExpress),j) )  ;
				}

				
				//����ϼ���
				if (m_pnlCard.getBodyPanel().isTatolRow()) {
		      		Object total = m_pnlCard.getTotalTableModel().getValueAt(0, m_pnlCard.getBillModel().getBodyColByKey(sItemExpress));
  		    		//rslt[rowCount] = "--------";
			  		vecValue.addElement("");
  		    		if (total == null) {
	 		 		 vecValue.addElement("--------");
	  				}
	  			else {
	   				vecValue.addElement(total.toString());
	  				}
				}
				

				break;
			}
		    
		}
				/**
				 * add by ouyangzhb 2010-11-19  begin
				 * ���ܣ��ڶ���ά���Ĵ�ӡģ�������ӷ�����Ϣ�Ĵ�ӡ��
				 * ��ģ������ʱ����Ӧ���ֶ�ǰ���ϡ�i_��Ϊǰ׺����������"�����Ϣ"���ĳЩ�ֶ�������
				 * ����ȡֵ����ң�����Ӧ���ֶ�ȥȡvo����Ӧ��ֵ��
				 * 
				 */
				InformationCostVO[] inforcost = (InformationCostVO[]) this.m_pnlCard.getBillData().getBodyValueChangeVOs("jj_scm_informationcost", InformationCostVO.class.getName()) ;
				if ( sItemExpress.equalsIgnoreCase("i_costname"))
					for(int y=0;y<inforcost.length;y++){
						vecValue.addElement( inforcost[y].getCostname() );
					}
				
				if(sItemExpress.equalsIgnoreCase("i_costcode"))
					for(int y=0;y<inforcost.length;y++){
				    	vecValue.addElement( inforcost[y].getCostcode() )  ;
					}
				if(sItemExpress.equalsIgnoreCase("i_ccostunitid"))
					for(int y=0;y<inforcost.length;y++){
				    	vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "costunit"))  ;
					}
				if(sItemExpress.equalsIgnoreCase("i_cmeasdocid"))
					for(int y=0;y<inforcost.length;y++){
						vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "currname"))  ;
					}
				if(sItemExpress.equalsIgnoreCase("i_noriginalcurmny"))
					for(int y=0;y<inforcost.length;y++){
				    	vecValue.addElement( inforcost[y].getNoriginalcurmny().toString() );
					}
				if(sItemExpress.equalsIgnoreCase("i_noriginalcurprice"))
					for(int y=0;y<inforcost.length;y++){
				    	vecValue.addElement( inforcost[y].getNoriginalcurprice().toString() );
				    }
				if(sItemExpress.equalsIgnoreCase("i_nnumber"))
					for(int y=0;y<inforcost.length;y++){
				    	vecValue.addElement( inforcost[y].getNnumber().toString() );
				    }
				if(sItemExpress.equalsIgnoreCase("i_vdef2"))
					for(int y=0;y<inforcost.length;y++){
				    	vecValue.addElement( inforcost[y].getVdef2() );
				    }
				if(sItemExpress.equalsIgnoreCase("i_vcosttype"))
					for(int y=0;y<inforcost.length;y++){
				    	vecValue.addElement( inforcost[y].getVcosttype() );
				    }
				if(sItemExpress.equalsIgnoreCase("i_vmemo"))
					for(int y=0;y<inforcost.length;y++){
						vecValue.addElement( inforcost[y].getVmemo() ) ;
			    	}
				if(sItemExpress.equalsIgnoreCase("i_currtypeid"))
					for(int y=0;y<inforcost.length;y++){
						vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "mea"))  ; ;
			    	}
				if(sItemExpress.equalsIgnoreCase("i_ismny"))
					for(int y=0;y<inforcost.length;y++){
						vecValue.addElement( inforcost[y].getIsmny().toString() );
			    	}
				if(sItemExpress.equalsIgnoreCase("i_vdef3"))
					for(int y=0;y<inforcost.length;y++){
						vecValue.addElement( inforcost[y].getVdef3() ) ;
					}
				if(sItemExpress.equalsIgnoreCase("i_vdef1"))
					for(int y=0;y<inforcost.length;y++){
						vecValue.addElement( inforcost[y].getVdef1() )  ;
			}
				/*
				 * add by ouyangzhb end
				 */
	}

	//������������
	return (String[])vecValue.toArray(new	String[vecValue.size()]);
}
/*
 * �������е��������Ӧ������
 * ������ �����������
 * ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]��
 * 		  ��� sItemExpress ӵ���������
 * 		  1 ���������ӡϵͳ����������������ݵ�˳�����ж� String[] �еĴ�ŵ�����
 *		  2 ���������ӡϵͳ�������������������������������

 	ģ�� 2 �����:
 			[��Ŀ]      ==>	  [100 200 300 -->  400 500 600]

 	ģ�� 3 �����: ��� getDependitemExpresssByName("���") ==

			[��Ŀ ����]  ==>  [100 200 300 400 500 600] ���к���
			[���� ��Ŀ]  ==>  [100 400 200 500 300 600]	���к���
 * 2002-09-23		wyf		����head��body�������¶���KEY�Ķ���
 */
private java.lang.String[] getItemValuesByExpressFromReport(String sItemExpress) {

	if (m_pnlCard == null){
		return null;
	}
	////==================��ͷ����
	ArrayList listValue = new ArrayList();
	boolean bflag = true;
	if (sItemExpress.indexOf("h_")==0 && m_pnlCard.getHeadItems()!=null) {
		sItemExpress = sItemExpress.substring("h_".length(),sItemExpress.length()) ;
		for(int i=0;i<m_pnlCard.getHeadItems().length;i++){

			if ( m_pnlCard.getHeadItems()[i].getKey().trim().equals(sItemExpress.trim()) ){
				listValue.add( getValueForCardHeadTail(m_pnlCard.getHeadItems()[i]) ) ;
				bflag = false;
				break;
			}
		}
	}

	//=====================��β����
	if(bflag && sItemExpress.indexOf("t_")==0 && m_pnlCard.getTailItems()!=null){
		for(int i=0;i<m_pnlCard.getTailItems().length;i++){

			if (m_pnlCard.getTailItems()[i].getKey().trim().equals(sItemExpress.trim())){
				listValue.add( getValueForCardHeadTail(m_pnlCard.getTailItems()[i]) ) ;
				bflag = false;
				break;
			}
		}
	}

	//=====================��������
	if (bflag && m_pnlCard.getBodyItems()!=null){
		for(int i=0;i<m_pnlCard.getBodyItems().length;i++){
			if ((m_pnlCard.getBodyItems())[i].getKey().trim().equals(sItemExpress.trim())){
				for(int j=0;j<m_pnlCard.getRowCount();j++){
					listValue.add( getValueForCardBody(m_pnlCard.getBodyItems()[i],j) );
				}
				break;
			}
		}
	}
	String[] saRet = new String[listValue.size()];
	for(int i = 0;i<listValue.size();i++){
		saRet[i] = listValue.get(i) == null ? null :listValue.get(i).toString() ;
	}
	return saRet;
//	return (String[])vecValue.toArray(new	String[vecValue.size()]);
}

/*
 *  ���ظ�����Դ��Ӧ��ģ������
 */
public String getModuleName() {
	return m_sPrintID;
}
/**
 * ���ߣ���ӡ��
 * ���ܣ�����ITEM�õ������ϵ�ֵ
 * ������BillItem	itemCheck		������ITEM
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-12-02 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private String getValueForCardBody(BillItem	itemCheck,int	iRow) {
    return getValueForCardBody(m_pnlCard,itemCheck,iRow);
}
/**
 * ���ߣ���־ƽ
 * ���ܣ����ݵ��ݿ�Ƭ��ITEM�õ������ϵ�ֵ
 * ������BillItem	itemCheck		������ITEM
 * ���أ���
 * ���⣺��
 * ���ڣ�(2005-06-23 16:18:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private String getValueForCardBody(BillCardPanel cardPnl,BillItem	itemCheck,int	iRow) {

	String		sKey = itemCheck.getKey() ;
	Object 		oValue = null;

	/*if (itemCheck.getDataType() == IBillItem.COMBO){
		nc.ui.pub.beans.UIComboBox ref = (nc.ui.pub.beans.UIComboBox)itemCheck.getComponent();
		oValue = ref.getSelectedItem().toString();
	}else*/ if (itemCheck.getDataType() == IBillItem.BOOLEAN){
		oValue = nc.vo.scm.pu.PuPubVO.getUFBoolean_NullAs(
				cardPnl.getBodyValueAt(iRow,sKey),
				nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE
		);
		if ( nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE.equals(oValue) ){
			oValue =  "��" ;
		}else{
			oValue =  "��" ;
		}
	}else	if (itemCheck.getDataType() == IBillItem.DECIMAL){
		int		iDigit = PoPrintDigitManager.NOT_EXIST ;
		if (m_mngDigit!=null) {
			iDigit = m_mngDigit.getBodyMnyDigit(iRow,sKey) ;
		}

		if (iDigit==PoPrintDigitManager.NOT_EXIST) {
			oValue = cardPnl.getBillModel().getValueAt(iRow,sKey);
		}else{
			int		iOrgDigit = itemCheck.getDecimalDigits() ;
			itemCheck.setDecimalDigits(nc.vo.scm.field.pu.FieldMaxLength.DECIMALDIGIT_MONEY) ;
			oValue = nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull( cardPnl.getBillModel().getValueAt(iRow,sKey) );

			//ʹ�þ��ȹ��������м���
			if (oValue!=null) {
				String		sRet = ((String)oValue) ;
				sRet = sRet.substring(0,sRet.indexOf(".")+iDigit+1) ;
				oValue = sRet ;
			}

			itemCheck.setDecimalDigits(iOrgDigit) ;
		}
	}else{
		oValue = cardPnl.getBillModel().getValueAt(iRow,sKey);
	}

	return	nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oValue)==null ? "" : oValue.toString() ;
}
/**
 * ���ߣ���ӡ��
 * ���ܣ�����ITEM�õ������ϵ�ֵ
 * ������BillItem	itemCheck		������ITEM
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-12-02 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private String getValueForCardHeadTail(BillItem	itemCheck) {

	Object oValue = null ;

	if (itemCheck.getDataType() == IBillItem.UFREF){
		nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane)itemCheck.getComponent();
		oValue = ref.getUITextField().getText();
	}else	if (itemCheck.getDataType() == IBillItem.BOOLEAN){
		oValue = nc.vo.scm.pu.PuPubVO.getUFBoolean_NullAs(
				itemCheck.getValue(),
				nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE
		);
		if (nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE.equals(oValue)) {
			oValue = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPPSCMCommon-000108")/*@res "��"*/ ;
		}else{
			oValue = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPPSCMCommon-000244")/*@res "��"*/ ;
		}
	}else	if (itemCheck.getDataType() == IBillItem.DECIMAL){
	  if(null==PuPubVO.getString_TrimZeroLenAsNull(itemCheck.getValue()))
	    oValue="";
	  else
	    oValue = new	nc.vo.pub.lang.UFDouble(itemCheck.getValue(),itemCheck.getDecimalDigits()) ;
	}else if(IBillItem.COMBO == itemCheck.getDataType()){
	  if(itemCheck.getComponent() instanceof UIComboBox )
	    oValue=((UIComboBox)itemCheck.getComponent()).getSelectdItemValue();
	  else
	    oValue=itemCheck.getValueObject();
	  
	}
	else{
		oValue = itemCheck.getValue() ;
	}

	return	nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oValue)==null ? "" : oValue.toString() ;

}
/**
 * ���ظ��������Ƿ�Ϊ������
 * ������ɲ������㣻��������ֻ��Ϊ�ַ�������
 * �硰������Ϊ�������������롱Ϊ��������
 * @param
 * @return		boolean  true ����  false ������
 * @exception
 * @see
 * @since		2001-07-17
*/
public boolean isNumber(String itemExpress) {

	return isNumberByCard(itemExpress);
}
/**
 * ���ݿ�Ƭ���淵�ظ��������Ƿ�Ϊ������
 * ������ɲ������㣻��������ֻ��Ϊ�ַ�������
 * �硰������Ϊ�������������롱Ϊ��������
 * @param
 * @return		boolean  true ����  false ������
 * @exception
 * @see
 * @since		2001-07-17
*/
private boolean isNumberByCard(String sItemExpress) {

	int iType = IBillItem.STRING;
	boolean bFound = false;
	//��ͷ
	if (sItemExpress.indexOf("h_")==0 && m_pnlCard.getHeadItems()!=null ) {
		for(int i=0;i<m_pnlCard.getHeadItems().length;i++){
			if ((m_pnlCard.getHeadItems())[i].getName().trim().equals(sItemExpress.trim())){
				iType = (m_pnlCard.getHeadItems())[i].getDataType();
				bFound = true;
				break ;
			}
		}
	}
	//��β
	if (!bFound && m_pnlCard.getTailItems()!=null ) {
		if (sItemExpress.indexOf("t_")==0) {
			for(int i=0;i<m_pnlCard.getTailItems().length;i++){
				if ((m_pnlCard.getTailItems())[i].getName().trim().equals(sItemExpress.trim())){
					iType = (m_pnlCard.getTailItems())[i].getDataType();
					bFound = true;
					break ;
				}
			}
		}
	}

	//����
	if (!bFound && m_pnlCard.getBodyItems()!=null ){
		for(int i=0;i<m_pnlCard.getBodyItems().length;i++){
			if ((m_pnlCard.getBodyItems())[i].getName().trim().equals(sItemExpress.trim())){
				iType = (m_pnlCard.getBodyItems())[i].getDataType();
				bFound = true;
				break ;
			}
		}
	}

	if ((iType == IBillItem.INTEGER) || (iType == IBillItem.DECIMAL)){
	    return true;
	}

	return false;
}
/**
 * ���õ�������
 * @param		BillCardPanel ��ƬPanel
 * @return
 * @exception
 * @see
 * @since		2001-07-17
*/
public void setBillCardPanel(BillCardPanel pnlCard) {

	this.m_pnlCard = pnlCard;
}
/**
 * ���ߣ���ӡ��
 * ���ܣ����þ��ȹ�����
 * ������PoPrintDigitManager mngDigit		���ȹ�����
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-06-15 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public void setManagerDigit(PoPrintDigitManager mngDigit) {

	m_mngDigit = mngDigit ;
}
/**
 * ���ߣ�czp
 * ���ܣ���ȡ���ݵ����ƻ�����ģ��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-14  10:08:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * */
private BillCardPanel getBillCardPanel_po_rp(boolean bCrtFlag){
    //��ʼ������һ�δ�����˾���������仯
	if (m_bcp_po_rp == null || bCrtFlag) {

		//---------------����ģ��
	    m_bcp_po_rp = new	BillCardPanel() ;
		BillData		bd = new	BillData(
		        m_bcp_po_rp.getTempletData(
						BillTypeConst.PO_RECEIVEPLAN,
						null,
						PoPublicUIClass.getLoginUser(),
						m_pk_corp,
						BillTypeConst.PO_RECEIVEPLAN)//"40040202000000000008")
		) ;

		initiPnl(bd) ;

		//m_bcpTool = new BillCardPanelTool(bd);
	}
	return m_bcp_po_rp;
}
/**
 * ���ߣ�czp
 * ���ܣ���ʼ��������Ƭ
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-14  10:08:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initiPnl(BillData	bd) {

	//��������
	initiPnlDigit(bd) ;
	//�����Сֵ
	initiPnlMinMaxValue(bd) ;
	//���ò��շ��
	initiPnlRefPane(bd);
	//�����Զ�����
	nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
	    new BillCardPanel(bd),			//��ǰģ��DATA
	    m_pk_corp, 	//��˾����
      ScmConst.PO_Order,
	    null, 		//����ģ���е���ͷ���Զ�����ǰ׺
	    "vdef" 		//����ģ���е�������Զ�����ǰ׺
	) ;

	//---------------��������
	m_bcp_po_rp.setBillData(bd);
	//ǧ��λ��ʾ
	m_bcp_po_rp.setBodyShowThMark(true);
	//����
	PuTool.setTranslateRender(m_bcp_po_rp);
}

/**
 * ���ߣ�czp
 * ���ܣ�����С���Ŀؼ���������������Сֵ
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-14  10:08:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initiPnlMinMaxValue(BillData	bd){

	//˰��	�۱�����	�۸�����
	String[] saItem =
		new String[] {
			"nordernum",
			"naccumarrvnum",
			"naccumstorenum",
			"naccumwastnum",
			"nbackarrvnum",
			"nbackstorenum",
			"naccumreceivenum",
			"nassistnum",
			"nconvertrate"};
	int iLen = saItem.length;
	for (int i = 0; i < iLen; i++) {
		BillItem bItem = bd.getBodyItem(saItem[i]);
		if (bItem != null) {
			UIRefPane refPanel = (UIRefPane) bItem.getComponent();
			UITextField textField = (UITextField) refPanel.getUITextField();
			textField.setMinValue(0.0);
		}
	}


	return;

}
/**
 * ���ߣ�czp
 * ���ܣ����ò��յĿ����ԡ�
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-14  10:08:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initiPnlRefPane(BillData	bd){

	//������
	FreeItemRefPane firpFreeItemRefPane = new FreeItemRefPane();
	firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree").getLength());
	bd.getBodyItem("vfree").setComponent(firpFreeItemRefPane);
	//���κ�
//	nc.ui.ic.pub.lot.LotNumbRefPane lotRef = new nc.ui.ic.pub.lot.LotNumbRefPane();
//	IICPub_LotNumbRefPane lotRef = null;
	LotNumbRefPane lotRef = null;
//	try{
//	    lotRef = (IICPub_LotNumbRefPane) InterServUI.getInterInstance(ProductCode.PROD_IC,InterRegister.IC0012);
		lotRef = new LotNumbRefPane();
//	}catch(Exception e){
//	    SCMEnv.out("��ȡ������κ�ʱ�����쳣�����κŲ��ղ�������ʹ�ã�");
//	}
	if(lotRef != null){
	    lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
	}
	bd.getBodyItem("vproducenum").setComponent((JComponent)lotRef);
	//������
	((UIRefPane)bd.getBodyItem("cassistunitname").getComponent()).setReturnCode(false) ;
	((UIRefPane)bd.getBodyItem("cassistunitname").getComponent()).setRefInputType(1);
	((UIRefPane)bd.getBodyItem("cassistunitname").getComponent()).setCacheEnabled(false);
	//�кŲ���
	((UIRefPane)bd.getBodyItem("crowno").getComponent()).setReturnCode(true) ;
	((UIRefPane)bd.getBodyItem("crowno").getComponent()).setAutoCheck(false) ;
	((UIRefPane)bd.getBodyItem("crowno").getComponent()).getUITextField().setEnabled(false) ;

    //�շ�����ַ
    UIRefPane paneBodyAddr = ((UIRefPane) bd.getBodyItem("vdevaddr").getComponent());
    paneBodyAddr.setRefModel(new PoReceiveAddrRefModel(m_pk_corp,null));
    paneBodyAddr.setButtonVisible(true);
    paneBodyAddr.setReturnCode(false);
    paneBodyAddr.setAutoCheck(false);
    paneBodyAddr.setRefInputType(1);

    //��Ӧ���շ�����ַ
    UIRefPane paneBodyAddrVendor = ((UIRefPane) bd.getBodyItem("vvenddevaddr").getComponent());
    paneBodyAddrVendor.setRefModel(new PoReceiveAddrRefModel(m_pk_corp,null));
    paneBodyAddrVendor.setButtonVisible(true);
    paneBodyAddrVendor.setReturnCode(false);
    paneBodyAddrVendor.setAutoCheck(false);
    paneBodyAddrVendor.setRefInputType(1);
}
/**
 * ���ߣ�czp
 * ���ܣ�������������
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-12-14  10:08:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initiPnlDigit(BillData	bd){

	int[]	iaDigit = (int[])PoPublicUIClass.getShowDigits(m_pk_corp) ;

	//����
	bd.getBodyItem("nordernum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumarrvnum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumstorenum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumwastnum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("nbackarrvnum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("nbackstorenum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumreceivenum").setDecimalDigits(iaDigit[0]);
	//������
	bd.getBodyItem("nassistnum").setDecimalDigits(iaDigit[1]);
	//������
	bd.getBodyItem("nconvertrate").setDecimalDigits(iaDigit[3]);

}
}