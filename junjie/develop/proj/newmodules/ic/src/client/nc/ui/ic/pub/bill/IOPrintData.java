package nc.ui.ic.pub.bill;

import java.util.ArrayList;
import java.util.Vector;

import nc.ui.po.pub.PoPrintDigitManager;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.print.IDataSource;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class IOPrintData implements IDataSource {
	
	//需打印的卡片Panel
	private BillCardPanel      	m_pnlCard = null;

	private	String				m_sPrintID = null ;

	//业务或财务精度
	private	PoPrintDigitManager	m_mngDigit = null ;

    //采购订单到货计划单据模板：合并打印
    private BillCardPanel m_bcp_po_rp = null;
    //当前要打印的订单VO所属公司
    private String m_pk_corp = null;
    
    /*
     * 得到所有的数据项表达式数组
     * 也就是返回所有定义的数据项的表达式
     */

	public String[] getAllDataItemExpress() {
		return	getItemExpressFromCard();
	}

	/*
	 * 取得卡片的ITEM
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
	
	public String[] getAllDataItemNames() {
		return	getItemNamesFromCard();
	}
	
	
	/*
	 * 根据卡片界面得到所有的数据项名称数组
	 * 也就是返回所有定义的数据项的名称
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

	public String[] getDependentItemExpressByExpress(String saItemExpress) {
		
		return null;
	}

	public String[] getItemValuesByExpress(String saItemExpress) {
		String[] saData = null;
		saData = getItemValuesByExpressFromCard(saItemExpress);
		return saData;
	}

	public String getModuleName() {
		return m_sPrintID;
	}

	public boolean isNumber(String itemExpress) {
		return false;
	}
	
	/*
	 * 返回所有的数据项对应的内容
	 * 参数： 数据项的名字
	 * 返回： 数据项对应的内容，只能为 String[]；
	 * 		  如果 sItemExpress 拥有依赖项，则：
	 * 		  1 个依赖项：打印系统将根据依赖项的内容的顺序来判断 String[] 中的存放的数据
	 *		  2 个依赖项：打印系统将根据两个依赖项的索引来决定数据
	 */
	
	private java.lang.String[] getItemValuesByExpressFromCard(String sItemExpress) {

		if (m_pnlCard == null){
			return null;
		}
   ArrayList<String> headitem = new ArrayList<String> ();
   ArrayList<String> titem = new ArrayList<String> ();
   int		HLen = m_pnlCard.getHeadItems().length ;
   for(int i=0;i<HLen;i++){
	   headitem.add(m_pnlCard.getHeadItems()[i].getKey().toString());
   }
		Vector vecValue = new Vector();
		//==================表头数据
		boolean 	bFound = false ;
		if(sItemExpress.indexOf("h_")==0){
			sItemExpress = sItemExpress.substring("h_".length(),sItemExpress.length()) ;
		}
		if (headitem.contains(sItemExpress)) {
			int		iHLen = m_pnlCard.getHeadItems().length ;
			for(int i=0;i<iHLen;i++){
				BillItem	itemH = m_pnlCard.getHeadItems()[i] ;
				if ( itemH.getKey().trim().equals(sItemExpress.trim()) ){
					if(sItemExpress.trim().equals("freplenishflag")){
						String flag = m_pnlCard.getHeadItem("freplenishflag").getValue().toString();
						if(flag!=null&&flag.equals("false")){
							vecValue.addElement("否") ;
						}else{
							vecValue.addElement("是") ;
						}
					}else{
						vecValue.addElement( getValueForCardHeadTail(itemH) ) ;
					}
					bFound = true;
					break;
				}
			}
		}
		//=====================表尾
		 int tLen = m_pnlCard.getTailItems().length ;
		 for(int i=0;i<tLen;i++){
			   titem.add(m_pnlCard.getTailItems()[i].getKey().toString());
		   }
		 if(sItemExpress.indexOf("t_")==0){
				sItemExpress = sItemExpress.substring("h_".length(),sItemExpress.length()) ;
			}
		if(!bFound && titem.contains(sItemExpress)) {
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
					    vecValue.addElement( getValueForCardBody(
					        m_pnlCard.getBodyItem(sItemExpress),j) )  ;
					}
					
					/**add by ouyangzhb 2012-04-23 取消合计行*/
//					//处理合计行
//					if (m_pnlCard.getBodyPanel().isTatolRow()) {
//			      		Object total = m_pnlCard.getTotalTableModel().getValueAt(0, m_pnlCard.getBillModel().getBodyColByKey(sItemExpress));
////			      		vecValue.addElement("");
//	  		    		if (total == null) {
//		 		 		 vecValue.addElement("--------");
//		  				}
//		  			else {
//		   				vecValue.addElement(total.toString());
//		  				}
//					}
//					break;
				}
			    
			}
					/**
					 * add by ouyangzhb 2010-11-19  begin
					 * 功能：在订单维护的打印模板中增加费用信息的打印，
					 * 在模板配置时在相应的字段前加上“i_”为前缀，这样能与"存货信息"里的某些字段相区别
					 * 否则取值会混乱，按相应的字段去取vo里相应的值。
					 * 
					 * add by ouyangzhb 2012-04-09 采购入库单的费用信息折行打印，分单双行打印
					 */
					InformationCostVO[] inforcost = (InformationCostVO[]) this.m_pnlCard.getBillData().getBodyValueChangeVOs("jj_scm_informationcost", InformationCostVO.class.getName()) ;
					
					/**add by ouyanghzb 2012-04-23 新增成本单价及成本金额字段的取值 begin*/
					UFDouble costprice = UFDouble.ZERO_DBL;
					if ( sItemExpress.equalsIgnoreCase("i_sumcostprice")||sItemExpress.equalsIgnoreCase("i_sumcostmny")){
						UFDouble sumcostmny = UFDouble.ZERO_DBL;
						UFDouble sumnum = UFDouble.ZERO_DBL;
						 costprice = UFDouble.ZERO_DBL;
						for(int y=0;y<inforcost.length;y++){
							sumcostmny=sumcostmny.add(inforcost[y].getNoriginalcurmny());
							if(inforcost[y].getNnumber()!=null&&inforcost[y].getNnumber().compareTo(UFDouble.ZERO_DBL)!=0&&sumnum.compareTo(UFDouble.ZERO_DBL)==0){
								sumnum = inforcost[y].getNnumber();
							}
						}
						costprice = sumcostmny.div(sumnum);
					}
					if ( sItemExpress.equalsIgnoreCase("i_sumcostprice")){
						for(int j=0;j<m_pnlCard.getRowCount();j++){
						    vecValue.addElement( costprice.add(new UFDouble(getValueForCardBody(
						        m_pnlCard.getBodyItem("nprice"),j))).setScale(2, 2).toString())  ;
						}
					}
					
					if ( sItemExpress.equalsIgnoreCase("i_sumcostmny")){
						for(int j=0;j<m_pnlCard.getRowCount();j++){
						    vecValue.addElement( costprice.add(new UFDouble(getValueForCardBody(
						        m_pnlCard.getBodyItem("nprice"),j)).multiply(new UFDouble(getValueForCardBody(
						        m_pnlCard.getBodyItem("ninnum"),j)))).setScale(2, 2).toString())  ;
						}
					}
					/**add by ouyanghzb 2012-04-23 新增成本单价及成本金额字段的取值 end */
					
					
					if ( sItemExpress.equalsIgnoreCase("i_costname"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0){
								vecValue.addElement( inforcost[y].getCostname() );
							}
							
						}
					if ( sItemExpress.equalsIgnoreCase("i_costname2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1){
								vecValue.addElement( inforcost[y].getCostname() );
							}
						}
					
					if(sItemExpress.equalsIgnoreCase("i_costcode"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
					    	vecValue.addElement( inforcost[y].getCostcode() )  ;
						}
					if(sItemExpress.equalsIgnoreCase("i_costcode2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
					    	vecValue.addElement( inforcost[y].getCostcode() )  ;
						}
					
					if(sItemExpress.equalsIgnoreCase("i_ccostunitid"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
					    	vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "costunit"))  ;
						}
					if(sItemExpress.equalsIgnoreCase("i_ccostunitid2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
					    	vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "costunit"))  ;
						}
					
					if(sItemExpress.equalsIgnoreCase("i_cmeasdocid"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
							vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "currname"))  ;
						}
					if(sItemExpress.equalsIgnoreCase("i_cmeasdocid2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
							vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "currname"))  ;
						}
					
					if(sItemExpress.equalsIgnoreCase("i_noriginalcurmny"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
					    	vecValue.addElement( inforcost[y].getNoriginalcurmny().toString() );
						}
					if(sItemExpress.equalsIgnoreCase("i_noriginalcurmny2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
					    	vecValue.addElement( inforcost[y].getNoriginalcurmny().toString() );
						}
					
					if(sItemExpress.equalsIgnoreCase("i_noriginalcurprice"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
					    	vecValue.addElement( inforcost[y].getNoriginalcurprice().toString() );
					    }
					if(sItemExpress.equalsIgnoreCase("i_noriginalcurprice2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
					    	vecValue.addElement( inforcost[y].getNoriginalcurprice().toString() );
					    }
					
					if(sItemExpress.equalsIgnoreCase("i_nnumber"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
					    	vecValue.addElement( inforcost[y].getNnumber().toString() );
					    }
					if(sItemExpress.equalsIgnoreCase("i_nnumber2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
					    	vecValue.addElement( inforcost[y].getNnumber().toString() );
					    }
					
					if(sItemExpress.equalsIgnoreCase("i_vdef2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
					    	vecValue.addElement( inforcost[y].getVdef2() );
					    }
					if(sItemExpress.equalsIgnoreCase("i_vdef22"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
					    	vecValue.addElement( inforcost[y].getVdef2() );
					    }
					
					if(sItemExpress.equalsIgnoreCase("i_vcosttype"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
					    	vecValue.addElement( inforcost[y].getVcosttype() );
					    }
					if(sItemExpress.equalsIgnoreCase("i_vcosttype2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
					    	vecValue.addElement( inforcost[y].getVcosttype() );
					    }
					
					if(sItemExpress.equalsIgnoreCase("i_vmemo"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
							vecValue.addElement( inforcost[y].getVmemo() ) ;
				    	}
					if(sItemExpress.equalsIgnoreCase("i_vmemo2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
							vecValue.addElement( inforcost[y].getVmemo() ) ;
				    	}
					
					if(sItemExpress.equalsIgnoreCase("i_currtypeid"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
							vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "mea"));
				    	}
					if(sItemExpress.equalsIgnoreCase("i_currtypeid2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
							vecValue.addElement(m_pnlCard.getBillData().getBillModel("jj_scm_informationcost").getValueAt(y, "mea"));
				    	}
					
					if(sItemExpress.equalsIgnoreCase("i_ismny"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
							vecValue.addElement( inforcost[y].getIsmny().toString() );
				    	}
					if(sItemExpress.equalsIgnoreCase("i_ismny2"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
							vecValue.addElement( inforcost[y].getIsmny().toString() );
				    	}
					
					if(sItemExpress.equalsIgnoreCase("i_vdef3"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
							vecValue.addElement( inforcost[y].getVdef3() ) ;
						}
					if(sItemExpress.equalsIgnoreCase("i_vdef32"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
							vecValue.addElement( inforcost[y].getVdef3() ) ;
						}
					
					if(sItemExpress.equalsIgnoreCase("i_vdef1"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==0)
							vecValue.addElement( inforcost[y].getVdef1() )  ;
						}
					if(sItemExpress.equalsIgnoreCase("i_vdef12"))
						for(int y=0;y<inforcost.length;y++){
							if(y%2==1)
							vecValue.addElement( inforcost[y].getVdef1() )  ;
						}
					/*
					 * add by ouyangzhb 2012-04-09      新增费用行折行打印
					 * add by ouyangzhb end
					 */
		}

		//构造数据数组
		return (String[])vecValue.toArray(new	String[vecValue.size()]);
	}
	
	/*
	 * 
	 */
	private String getValueForCardBody(BillItem	itemCheck,int	iRow) {
	    return getValueForCardBody(m_pnlCard,itemCheck,iRow);
	}
	
	
	/*
	 * 取得表体ITEM相对应的值
	 */
	private String getValueForCardBody(BillCardPanel cardPnl,BillItem	itemCheck,int	iRow) {

		String		sKey = itemCheck.getKey() ;
		Object 		oValue = null;
		if (itemCheck.getDataType() == IBillItem.BOOLEAN){
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
//			if (m_mngDigit!=null) {
//				iDigit = m_mngDigit.getBodyMnyDigit(iRow,sKey) ;
//			}

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
	
	
	/*
	 * 取得表头表尾ITEM的相应值
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
	/*
	 * 取得要打印的界面
	 */
	public void setBillCardPanel(BillCardPanel pnlCard) {

		this.m_pnlCard = pnlCard;
	}

}
