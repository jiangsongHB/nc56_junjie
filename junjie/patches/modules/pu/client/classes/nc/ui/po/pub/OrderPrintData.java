package nc.ui.po.pub;

/**
 * 打印数据组织类
 * 作者：李亮
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

	//需打印的卡片Panel
	private BillCardPanel      	m_pnlCard = null;

	private	String				m_sPrintID = null ;

	//业务或财务精度
	private	PoPrintDigitManager	m_mngDigit = null ;

    //采购订单到货计划单据模板：合并打印
    private BillCardPanel m_bcp_po_rp = null;
    //当前要打印的订单VO所属公司
    private String m_pk_corp = null;
    /**
 * OrderPrintData 构造子注解。
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
 * 得到所有的数据项表达式数组
 * 也就是返回所有定义的数据项的表达式
 * @param
 * @return		String[]  字段键值，可能是一个表达式
 * @exception
 * @see
 * @since		2001-07-17
*/
public java.lang.String[] getAllDataItemExpress() {

	return	getItemExpressFromCard();
}
	/*
	下面三种表格模版得到的结果都一样，但它的定义方式不同：	←↑→↓

		(1) 每一个数据项(制表人除外) 都是向下扩展,没有任何依赖关系
		------------------------------------------
	 	金额\科目	|  科目01	    |	 科目02
	  	----------------------------------
	   	(日期)	↓	| (科目01)↓	|	(科目02)↓
	    ------------------------------------------
	    制表人:	(制表人)

		(2) (日期) 下扩展 (科目) 右扩展 (金额) 依赖于 (科目)
	    ------------------------
	 	金额\科目	| (科目) →
	  	------------------------
	   	(日期)	↓	| (金额)
	     -----------------------
	    制表人:	(制表人)

	   	(3) (日期) 下扩展 (科目) 右扩展 (金额) 依赖于 (科目 日期)
		------------------------
	 	金额\科目	| (科目) →
	  	------------------------
	   	(日期)	↓	| (金额)
	    ------------------------
	 	制表人:	(制表人)

	    打印结果:
	     --------------------------------
	 	 金额\科目	|	科目1 	| 科目2
	 	 --------------------------------
	 		1999	|  	100	  	|	400
	 		2000	|  	200	 	| 	500
	 		3001	|  	300	 	| 	600
	 	 --------------------------------
	 	 制表人: xxx
	  */
public java.lang.String[] getAllDataItemNames() {

	return	getItemNamesFromCard();
}
/**
 *
 * 返回依赖项的名称数组，该数据项长度只能为 1 或者 2
 * 返回 null : 		没有依赖
 * 长度 1 :			单项依赖
 * 长度 2 :			双向依赖
 *
 */
public java.lang.String[] getDependentItemExpressByExpress(String itemName) {
	return null;
}
/**
 * 根据卡片界面得到所有的数据项表达式数组
 * 也就是返回所有定义的数据项的表达式
 * @param
 * @return		String[]  字段键值，可能是一个表达式
 * @exception
 * @see
 * @since		2001-07-17
*/
private java.lang.String[] getItemExpressFromCard() {

	if (m_pnlCard == null){
		return null;
	}

	Vector vecExpress = new Vector();

	//表头
	if (m_pnlCard.getHeadItems() != null) {
		for (int i = 0; i < m_pnlCard.getHeadItems().length; i++) {
			vecExpress.addElement((m_pnlCard.getHeadItems())[i].getKey());
		}
	}

	//表体
	if (m_pnlCard.getBodyItems() != null) {
		for (int i = 0; i < m_pnlCard.getBodyItems().length; i++) {
			vecExpress.addElement((m_pnlCard.getBodyItems())[i].getKey());
		}
	}

	//表尾
	if (m_pnlCard.getTailItems() != null) {
		for (int i = 0; i < m_pnlCard.getTailItems().length; i++) {
			vecExpress.addElement((m_pnlCard.getTailItems())[i].getKey());
		}
	}

	return vecExpress.size()==0 ? null : (String[])vecExpress.toArray(new	String[vecExpress.size()]);
}
/**
 * 根据卡片界面得到所有的数据项名称数组
 * 也就是返回所有定义的数据项的名称
 * @param
 * @return		String[]  字段键值，可能是一个表达式
 * @exception
 * @see
 * @since		2001-07-17
*/
private java.lang.String[] getItemNamesFromCard() {

	if (m_pnlCard == null){
		return null;
	}

	Vector vecName = new Vector();

	//表头
	if (m_pnlCard.getHeadItems() != null) {
		for (int i = 0; i < m_pnlCard.getHeadItems().length; i++) {
			vecName.addElement((m_pnlCard.getHeadItems())[i].getName());
		}
	}

	//表体
	if (m_pnlCard.getBodyItems() != null) {
		for (int i = 0; i < m_pnlCard.getBodyItems().length; i++) {
			vecName.addElement((m_pnlCard.getBodyItems())[i].getName());
		}
	}

	//表尾
	if (m_pnlCard.getTailItems() != null) {
		for (int i = 0; i < m_pnlCard.getTailItems().length; i++) {
			vecName.addElement((m_pnlCard.getTailItems())[i].getName());
		}
	}

	return vecName.size()==0 ? null : (String[])vecName.toArray(new	String[vecName.size()]);
}
/*
 * 返回所有的数据项对应的内容
 * 参数： 数据项的名字
 * 返回： 数据项对应的内容，只能为 String[]；
 * 		  如果 itemName 拥有依赖项，则：
 * 		  1 个依赖项：打印系统将根据依赖项的内容的顺序来判断 String[] 中的存放的数据
 *		  2 个依赖项：打印系统将根据两个依赖项的索引来决定数据

 	模板 2 的情况:
 			[科目]      ==>	  [100 200 300 -->  400 500 600]

 	模板 3 的情况: 如果 getDependItemNamesByName("金额") ==

			[科目 日期]  ==>  [100 200 300 400 500 600] 先列后行
			[日期 科目]  ==>  [100 400 200 500 300 600]	先行后列
 * 2002-09-23	wyf		加入一个新的函数，对报表另行处理
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
 * 作者：czp
 * 功能：根据当前VO、及KEY返回相应数据内容
 * 参数：String KEY；BillCardPanelTool 单据模板工具
 * 返回：无
 * 例外：无
 * 日期：(2004-12-14 11:24:16)
 * 修改日期，修改人，修改原因，注释标志：
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
        SCMEnv.out("根据订单ID查询到货计划时出现异常"+e.getMessage());
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
	
	//=====================表体数据
	/*V31 Del {Error : “否”显示“false”}
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
	//返回内容：数组
	int iLen = vValue.size();
	String[] saAll = null;
	if(iLen > 0){
	    saAll = new String[iLen];
	    vValue.copyInto(saAll);
	}
	return saAll;
}
/**
 * 作者：czp
 * 功能：显示到货计划界面
 * 参数：int[]				iaRow		在哪些行上设置，不可为空
 * 返回：无
 * 例外：无
 * 日期：(2004-12-14 11:24:16)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void execLoadValue(int[] iaRow,OrderReceivePlanVO[] voaRP) {

	//设置VO
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
		//空行
		if (voaRP[i]==null) {
			continue ;
		}
		//关闭
		getBillCardPanel_po_rp(false).setBodyValueAt(
			voaRP[i].isActive() ? VariableConst.UFBOOLEAN_FALSE : VariableConst.UFBOOLEAN_TRUE,
			iRow,
			"borderrowclose");
		//是否赠品
		getBillCardPanel_po_rp(false).setBodyValueAt(
			(voaRP[i].getBlargess() != null && voaRP[i].getBlargess().booleanValue()) ? VariableConst.UFBOOLEAN_TRUE : VariableConst.UFBOOLEAN_FALSE,
			iRow,
			"blargess");
		//行号
		getBillCardPanel_po_rp(false).setBodyValueAt(mapBidRowNo.get(voaRP[i].getCorder_bid()), iRow, "crowno");
	}

	//执行公式
	getBillCardPanel_po_rp(false).getBillModel().execLoadFormula();
	//加载换算率
	//				[0]	存货基本ID		NAME
	//				[1] 辅单位ID			NAME
	//				[2] 主数量			NAME
	//				[3] 辅数量			NAME
	//				[4] 换算率			NAME
	PuTool.setBillModelConvertRate(
		getBillCardPanel_po_rp(false).getBillModel(),
		new String[] { "cbaseid", "cassistunit", "nordernum", "nassistnum", "nconvertrate" });
	//设置自由项
	PoPublicUIClass.setFreeColValue(getBillCardPanel_po_rp(false).getBillModel(), "vfree");

	//重置行状态
	getBillCardPanel_po_rp(false).updateValue();
}
/*
 * 返回所有的数据项对应的内容
 * 参数： 数据项的名字
 * 返回： 数据项对应的内容，只能为 String[]；
 * 		  如果 sItemExpress 拥有依赖项，则：
 * 		  1 个依赖项：打印系统将根据依赖项的内容的顺序来判断 String[] 中的存放的数据
 *		  2 个依赖项：打印系统将根据两个依赖项的索引来决定数据

 	模板 2 的情况:
 			[科目]      ==>	  [100 200 300 -->  400 500 600]

 	模板 3 的情况: 如果 getDependitemExpresssByName("金额") ==

			[科目 日期]  ==>  [100 200 300 400 500 600] 先列后行
			[日期 科目]  ==>  [100 400 200 500 300 600]	先行后列

 */
private java.lang.String[] getItemValuesByExpressFromCard(String sItemExpress) {

	if (m_pnlCard == null){
		return null;
	}


	Vector vecValue = new Vector();
	//==================表头数据
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

	//=====================表尾
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

	//=====================表体数据
	if (!bFound && m_pnlCard.getBodyItems()!=null) {
		int		iBLen = m_pnlCard.getBodyItems().length ;
		
			
			
				for(int i=0;i<iBLen;i++){

		    if ( m_pnlCard.getBodyItems()[i].getKey().trim().equals(
			    sItemExpress.trim()) ){
		    	
		    	
				for(int j=0;j<m_pnlCard.getRowCount();j++){
//				  //过滤赠品行  by zhaoyha 根据问题NCdp201070734
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

				
				//处理合计行
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
				 * 功能：在订单维护的打印模板中增加费用信息的打印，
				 * 在模板配置时在相应的字段前加上“i_”为前缀，这样能与"存货信息"里的某些字段相区别
				 * 否则取值会混乱，按相应的字段去取vo里相应的值。
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

	//构造数据数组
	return (String[])vecValue.toArray(new	String[vecValue.size()]);
}
/*
 * 返回所有的数据项对应的内容
 * 参数： 数据项的名字
 * 返回： 数据项对应的内容，只能为 String[]；
 * 		  如果 sItemExpress 拥有依赖项，则：
 * 		  1 个依赖项：打印系统将根据依赖项的内容的顺序来判断 String[] 中的存放的数据
 *		  2 个依赖项：打印系统将根据两个依赖项的索引来决定数据

 	模板 2 的情况:
 			[科目]      ==>	  [100 200 300 -->  400 500 600]

 	模板 3 的情况: 如果 getDependitemExpresssByName("金额") ==

			[科目 日期]  ==>  [100 200 300 400 500 600] 先列后行
			[日期 科目]  ==>  [100 400 200 500 300 600]	先行后列
 * 2002-09-23		wyf		不对head及body进行重新定义KEY的动作
 */
private java.lang.String[] getItemValuesByExpressFromReport(String sItemExpress) {

	if (m_pnlCard == null){
		return null;
	}
	////==================表头数据
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

	//=====================表尾数据
	if(bflag && sItemExpress.indexOf("t_")==0 && m_pnlCard.getTailItems()!=null){
		for(int i=0;i<m_pnlCard.getTailItems().length;i++){

			if (m_pnlCard.getTailItems()[i].getKey().trim().equals(sItemExpress.trim())){
				listValue.add( getValueForCardHeadTail(m_pnlCard.getTailItems()[i]) ) ;
				bflag = false;
				break;
			}
		}
	}

	//=====================表体数据
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
 *  返回该数据源对应的模块名称
 */
public String getModuleName() {
	return m_sPrintID;
}
/**
 * 作者：王印芬
 * 功能：根据ITEM得到界面上的值
 * 参数：BillItem	itemCheck		待检查的ITEM
 * 返回：无
 * 例外：无
 * 日期：(2003-12-02 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private String getValueForCardBody(BillItem	itemCheck,int	iRow) {
    return getValueForCardBody(m_pnlCard,itemCheck,iRow);
}
/**
 * 作者：晁志平
 * 功能：根据单据卡片及ITEM得到界面上的值
 * 参数：BillItem	itemCheck		待检查的ITEM
 * 返回：无
 * 例外：无
 * 日期：(2005-06-23 16:18:21)
 * 修改日期，修改人，修改原因，注释标志：
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
			oValue =  "否" ;
		}else{
			oValue =  "是" ;
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

			//使用精度管理器进行计算
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
 * 作者：王印芬
 * 功能：根据ITEM得到界面上的值
 * 参数：BillItem	itemCheck		待检查的ITEM
 * 返回：无
 * 例外：无
 * 日期：(2003-12-02 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
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
			oValue = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPPSCMCommon-000108")/*@res "否"*/ ;
		}else{
			oValue = nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPPSCMCommon-000244")/*@res "是"*/ ;
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
 * 返回该数据项是否为数字项
 * 数字项可参与运算；非数字项只作为字符串常量
 * 如“数量”为数字项、“存货编码”为非数字项
 * @param
 * @return		boolean  true 数字  false 非数字
 * @exception
 * @see
 * @since		2001-07-17
*/
public boolean isNumber(String itemExpress) {

	return isNumberByCard(itemExpress);
}
/**
 * 根据卡片界面返回该数据项是否为数字项
 * 数字项可参与运算；非数字项只作为字符串常量
 * 如“数量”为数字项、“存货编码”为非数字项
 * @param
 * @return		boolean  true 数字  false 非数字
 * @exception
 * @see
 * @since		2001-07-17
*/
private boolean isNumberByCard(String sItemExpress) {

	int iType = IBillItem.STRING;
	boolean bFound = false;
	//表头
	if (sItemExpress.indexOf("h_")==0 && m_pnlCard.getHeadItems()!=null ) {
		for(int i=0;i<m_pnlCard.getHeadItems().length;i++){
			if ((m_pnlCard.getHeadItems())[i].getName().trim().equals(sItemExpress.trim())){
				iType = (m_pnlCard.getHeadItems())[i].getDataType();
				bFound = true;
				break ;
			}
		}
	}
	//表尾
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

	//表体
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
 * 设置单据数据
 * @param		BillCardPanel 卡片Panel
 * @return
 * @exception
 * @see
 * @since		2001-07-17
*/
public void setBillCardPanel(BillCardPanel pnlCard) {

	this.m_pnlCard = pnlCard;
}
/**
 * 作者：王印芬
 * 功能：设置精度管理器
 * 参数：PoPrintDigitManager mngDigit		精度管理器
 * 返回：无
 * 例外：无
 * 日期：(2004-06-15 11:39:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
public void setManagerDigit(PoPrintDigitManager mngDigit) {

	m_mngDigit = mngDigit ;
}
/**
 * 作者：czp
 * 功能：获取单据到货计划单据模板
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-14  10:08:21)
 * 修改日期，修改人，修改原因，注释标志：
 * */
private BillCardPanel getBillCardPanel_po_rp(boolean bCrtFlag){
    //初始化：第一次创建或公司主键发生变化
	if (m_bcp_po_rp == null || bCrtFlag) {

		//---------------加载模板
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
 * 作者：czp
 * 功能：初始化订单卡片
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-14  10:08:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initiPnl(BillData	bd) {

	//数量精度
	initiPnlDigit(bd) ;
	//最大最小值
	initiPnlMinMaxValue(bd) ;
	//设置参照风格
	initiPnlRefPane(bd);
	//处理自定义项
	nc.ui.scm.pub.def.DefSetTool.updateBillCardPanelUserDef(
	    new BillCardPanel(bd),			//当前模板DATA
	    m_pk_corp, 	//公司主键
      ScmConst.PO_Order,
	    null, 		//单据模板中单据头的自定义项前缀
	    "vdef" 		//单据模板中单据体的自定义项前缀
	) ;

	//---------------设置数据
	m_bcp_po_rp.setBillData(bd);
	//千分位显示
	m_bcp_po_rp.setBodyShowThMark(true);
	//翻译
	PuTool.setTranslateRender(m_bcp_po_rp);
}

/**
 * 作者：czp
 * 功能：对是小数的控件设置其可输入的最小值
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-14  10:08:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initiPnlMinMaxValue(BillData	bd){

	//税率	折本汇率	折辅汇率
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
 * 作者：czp
 * 功能：设置参照的可用性。
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-14  10:08:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initiPnlRefPane(BillData	bd){

	//自由项
	FreeItemRefPane firpFreeItemRefPane = new FreeItemRefPane();
	firpFreeItemRefPane.setMaxLength(bd.getBodyItem("vfree").getLength());
	bd.getBodyItem("vfree").setComponent(firpFreeItemRefPane);
	//批次号
//	nc.ui.ic.pub.lot.LotNumbRefPane lotRef = new nc.ui.ic.pub.lot.LotNumbRefPane();
//	IICPub_LotNumbRefPane lotRef = null;
	LotNumbRefPane lotRef = null;
//	try{
//	    lotRef = (IICPub_LotNumbRefPane) InterServUI.getInterInstance(ProductCode.PROD_IC,InterRegister.IC0012);
		lotRef = new LotNumbRefPane();
//	}catch(Exception e){
//	    SCMEnv.out("获取存货批次号时出现异常，批次号参照不能正常使用！");
//	}
	if(lotRef != null){
	    lotRef.setMaxLength(bd.getBodyItem("vproducenum").getLength());
	}
	bd.getBodyItem("vproducenum").setComponent((JComponent)lotRef);
	//辅计量
	((UIRefPane)bd.getBodyItem("cassistunitname").getComponent()).setReturnCode(false) ;
	((UIRefPane)bd.getBodyItem("cassistunitname").getComponent()).setRefInputType(1);
	((UIRefPane)bd.getBodyItem("cassistunitname").getComponent()).setCacheEnabled(false);
	//行号参照
	((UIRefPane)bd.getBodyItem("crowno").getComponent()).setReturnCode(true) ;
	((UIRefPane)bd.getBodyItem("crowno").getComponent()).setAutoCheck(false) ;
	((UIRefPane)bd.getBodyItem("crowno").getComponent()).getUITextField().setEnabled(false) ;

    //收发货地址
    UIRefPane paneBodyAddr = ((UIRefPane) bd.getBodyItem("vdevaddr").getComponent());
    paneBodyAddr.setRefModel(new PoReceiveAddrRefModel(m_pk_corp,null));
    paneBodyAddr.setButtonVisible(true);
    paneBodyAddr.setReturnCode(false);
    paneBodyAddr.setAutoCheck(false);
    paneBodyAddr.setRefInputType(1);

    //供应商收发货地址
    UIRefPane paneBodyAddrVendor = ((UIRefPane) bd.getBodyItem("vvenddevaddr").getComponent());
    paneBodyAddrVendor.setRefModel(new PoReceiveAddrRefModel(m_pk_corp,null));
    paneBodyAddrVendor.setButtonVisible(true);
    paneBodyAddrVendor.setReturnCode(false);
    paneBodyAddrVendor.setAutoCheck(false);
    paneBodyAddrVendor.setRefInputType(1);
}
/**
 * 作者：czp
 * 功能：设置数量精度
 * 参数：无
 * 返回：无
 * 例外：无
 * 日期：(2004-12-14  10:08:21)
 * 修改日期，修改人，修改原因，注释标志：
 */
private void initiPnlDigit(BillData	bd){

	int[]	iaDigit = (int[])PoPublicUIClass.getShowDigits(m_pk_corp) ;

	//数量
	bd.getBodyItem("nordernum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumarrvnum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumstorenum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumwastnum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("nbackarrvnum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("nbackstorenum").setDecimalDigits(iaDigit[0]);
	bd.getBodyItem("naccumreceivenum").setDecimalDigits(iaDigit[0]);
	//辅数量
	bd.getBodyItem("nassistnum").setDecimalDigits(iaDigit[1]);
	//换算率
	bd.getBodyItem("nconvertrate").setDecimalDigits(iaDigit[3]);

}
}