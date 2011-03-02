package nc.ui.so.so002;

import java.util.ArrayList;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.po.pub.PoPrintDigitManager;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.print.IDataSource;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;

public class SalePrintData implements IDataSource {

	// 需打印的卡片Panel
	private BillCardPanel m_pnlCard = null;
	
	//add by ouyangzhb 2011-03-01  begin 
	//打印对应的整个Panel 从对应的panel中把界面VO传过来
	private SaleinvoiceVO vo= null;

	public SaleinvoiceVO getVo() {
		return vo;
	}

	public void setVo(SaleinvoiceVO vo) {
		this.vo = vo;
	}

	private String m_sPrintID = null;

	// 业务或财务精度
	private PoPrintDigitManager m_mngDigit = null;

	// 采购订单到货计划单据模板：合并打印
	private BillCardPanel m_bcp_po_rp = null;
	// 当前要打印的订单VO所属公司
	private String m_pk_corp = null;

	/**
	 * 得到所有的数据项表达式数组 也就是返回所有定义的数据项的表达式
	 * 
	 * @param
	 * @return String[] 字段键值，可能是一个表达式
	 * @exception
	 * @see
	 * @since 2001-07-17
	 */
	public String[] getAllDataItemExpress() {
		return getItemExpressFromCard();
	}

	private java.lang.String[] getItemExpressFromCard() {

		if (m_pnlCard == null) {
			return null;
		}

		Vector vecExpress = new Vector();

		// 表头
		if (m_pnlCard.getHeadItems() != null) {
			for (int i = 0; i < m_pnlCard.getHeadItems().length; i++) {
				vecExpress.addElement((m_pnlCard.getHeadItems())[i].getKey());
			}
		}

		// 表体
		if (m_pnlCard.getBodyItems() != null) {
			for (int i = 0; i < m_pnlCard.getBodyItems().length; i++) {
				vecExpress.addElement((m_pnlCard.getBodyItems())[i].getKey());
			}
		}

		// 表尾
		if (m_pnlCard.getTailItems() != null) {
			for (int i = 0; i < m_pnlCard.getTailItems().length; i++) {
				vecExpress.addElement((m_pnlCard.getTailItems())[i].getKey());
			}
		}
		return vecExpress.size() == 0 ? null : (String[]) vecExpress
				.toArray(new String[vecExpress.size()]);
	}

	public String[] getAllDataItemNames() {
		return getItemNamesFromCard();
	}

	private java.lang.String[] getItemNamesFromCard() {

		if (m_pnlCard == null) {
			return null;
		}

		Vector vecName = new Vector();

		// 表头
		if (m_pnlCard.getHeadItems() != null) {
			for (int i = 0; i < m_pnlCard.getHeadItems().length; i++) {
				vecName.addElement((m_pnlCard.getHeadItems())[i].getName());
			}
		}

		// 表体
		if (m_pnlCard.getBodyItems() != null) {
			for (int i = 0; i < m_pnlCard.getBodyItems().length; i++) {
				vecName.addElement((m_pnlCard.getBodyItems())[i].getName());
			}
		}

		// 表尾
		if (m_pnlCard.getTailItems() != null) {
			for (int i = 0; i < m_pnlCard.getTailItems().length; i++) {
				vecName.addElement((m_pnlCard.getTailItems())[i].getName());
			}
		}

		return vecName.size() == 0 ? null : (String[]) vecName
				.toArray(new String[vecName.size()]);
	}

	/**
	 * 
	 * 返回依赖项的名称数组，该数据项长度只能为 1 或者 2 返回 null : 没有依赖 长度 1 : 单项依赖 长度 2 : 双向依赖
	 * 
	 */
	public String[] getDependentItemExpressByExpress(String itemExpress) {
		return null;
	}

	public String[] getItemValuesByExpress(String itemExpress) {
		String[] saData = null;
		saData = getItemValuesByExpressFromCard(itemExpress);
		return saData;
	}

	private java.lang.String[] getItemValuesByExpressFromCard(
			String sItemExpress) {

		if (m_pnlCard == null) {
			return null;
		}
		Vector vecValue = new Vector();
		// ==================表头数据
		boolean bFound = false;
		if (sItemExpress.indexOf("h_") == 0 && m_pnlCard.getHeadItems() != null) {
			sItemExpress = sItemExpress.substring("h_".length(), sItemExpress
					.length());

			int iHLen = m_pnlCard.getHeadItems().length;
			for (int i = 0; i < iHLen; i++) {

				BillItem itemH = m_pnlCard.getHeadItems()[i];

				if (itemH.getKey().trim().equals(sItemExpress.trim())) {
					vecValue.addElement(getValueForCardHeadTail(itemH));
					bFound = true;
					break;
				}
			}
		}

		// =====================表尾
		if (!bFound && sItemExpress.indexOf("t_") == 0
				&& m_pnlCard.getTailItems() != null) {
			sItemExpress = sItemExpress.substring("t_".length(), sItemExpress
					.length());

			for (int i = 0; i < m_pnlCard.getTailItems().length; i++) {
				if (m_pnlCard.getTailItems()[i].getKey().trim().equals(
						sItemExpress.trim())) {
					vecValue.addElement(getValueForCardHeadTail(m_pnlCard
							.getTailItems()[i]));
					bFound = true;
					break;
				}
			}
		}

		// =====================表体数据
		if (!bFound && m_pnlCard.getBodyItems() != null) {
			int iBLen = m_pnlCard.getBodyItems().length;

			for (int i = 0; i < iBLen; i++) {

				if (m_pnlCard.getBodyItems()[i].getKey().trim().equals(
						sItemExpress.trim())) {

					for (int j = 0; j < m_pnlCard.getRowCount(); j++) {
						// //过滤赠品行 by zhaoyha 根据问题NCdp201070734
						// UFBoolean blargess= PuPubVO.getUFBoolean_NullAs(
						// m_pnlCard.getBodyValueAt(j,
						// "blargess"),UFBoolean.FALSE);
						//
						// // PuPubVO.getUFBoolean_NullAs(
						// //
						// m_pnlCard.getBodyItem("blargess").getValueObject(),UFBoolean.FALSE);
						// if(blargess.booleanValue() &&
						// ("noriginaltaxpricemny".equals(sItemExpress)
						// || "noriginalcurmny".equals(sItemExpress)
						// || "noriginaltaxmny".equals(sItemExpress)
						// )
						// ){
						// vecValue.addElement(null);
						// }
						// else
						vecValue.addElement(getValueForCardBody(m_pnlCard
								.getBodyItem(sItemExpress), j));
					}

					// 处理合计行
					if (m_pnlCard.getBodyPanel().isTatolRow()) {
						Object total = m_pnlCard.getTotalTableModel()
								.getValueAt(
										0,
										m_pnlCard.getBillModel()
												.getBodyColByKey(sItemExpress));
						// rslt[rowCount] = "--------";
						vecValue.addElement("");
						if (total == null) {
							vecValue.addElement("--------");
						} else {
							vecValue.addElement(total.toString());
						}
					}

					break;
				}

			}
			//add by ouyangzhb beging 为打印模板中固定的字段赋值
			if(sItemExpress.equals("c_costname")){//费用名称
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( getVOs()[j].getAttributeValue("cinventoryname").toString() );
				}
			}
			if(sItemExpress.equals("costcode")){//费用编码
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( getVOs()[j].getAttributeValue("cinventorycode").toString() );
				}
			}
			if(sItemExpress.equals("c-gg")){//规格
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( getVOs()[j].getAttributeValue("GG").toString() );
				}
			}
			if(sItemExpress.equals("c_ccustomername")){//费用单位
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( getVOs()[j].getAttributeValue("ccustomername"));
				}
			}
			if(sItemExpress.equals("c_noriginalcursummny")){//费用金额
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( (getVOs()[j].getAttributeValue("noriginalcursummny")==null? UFDouble.ZERO_DBL:getVOs()[j].getAttributeValue("noriginalcursummny")).toString() );
				}
			}
			if(sItemExpress.equals("c_nnumber")){//重量
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( (getVOs()[j].getAttributeValue("nnumber")==null? UFDouble.ZERO_DBL:getVOs()[j].getAttributeValue("nnumber")).toString()  );
				}
			}
			if(sItemExpress.equals("c_noriginalcurtaxprice")){//单价
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( (getVOs()[j].getAttributeValue("noriginalcurtaxprice")==null? UFDouble.ZERO_DBL:getVOs()[j].getAttributeValue("noriginalcurtaxprice")).toString() );
				}
			}
			if(sItemExpress.equals("c_vdef1")){//车船号
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( getVOs()[j].getAttributeValue("vdef1") );
				}
			}
			if(sItemExpress.equals("c_vdef2")){//司机
				for(int j=0;j<this.getVOs().length;j++){
					vecValue.addElement( getVOs()[j].getAttributeValue("vdef2") );
				}
			}
		}
		// 构造数据数组
		return (String[]) vecValue.toArray(new String[vecValue.size()]);
	} 
	//add by ouyangzhb 2011-03-02 
	
	
	/*
	 * add by ouyangzhb 2011-03-02
	 * 当存货信息为费用时，取出并保存到LIST中
	 */
	 public SaleinvoiceBVO[] getVOs(){
		  SaleinvoiceBVO[] newBodyVO ;
		  ArrayList<SaleinvoiceBVO> bodyVOList = new ArrayList<SaleinvoiceBVO>();
		  SaleinvoiceVO saleinvoice = this.getVo();
		   SaleinvoiceBVO[] childrenVO = saleinvoice.getChildrenVO();
		  for(int i=0;i<childrenVO.length;i++){
			  String cinvbasdocid = childrenVO[i].getCinvbasdocid();
			  IUAPQueryBS queryBS=NCLocator.getInstance().lookup(IUAPQueryBS.class);
			  String flagsql = "select laborflag from bd_invbasdoc where  pk_invbasdoc='"+cinvbasdocid+"'and dr=0";
			  String flag = null;
			  
			try {
				flag = (String) queryBS.executeQuery(flagsql, new ColumnProcessor());
				if(flag!=null&&"Y".equals(flag)){
					bodyVOList.add(childrenVO[i]);
				  }
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		  }
		  newBodyVO = new SaleinvoiceBVO[bodyVOList.size()];
		   bodyVOList.toArray(newBodyVO);
		  
//		  saleinvoice.setChildrenVO(newBodyVO);
		return newBodyVO;
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
	private String getValueForCardHeadTail(BillItem itemCheck) {

		Object oValue = null;

		if (itemCheck.getDataType() == IBillItem.UFREF) {
			nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) itemCheck
					.getComponent();
			oValue = ref.getUITextField().getText();
		} else if (itemCheck.getDataType() == IBillItem.BOOLEAN) {
			oValue = nc.vo.scm.pu.PuPubVO.getUFBoolean_NullAs(itemCheck
					.getValue(), nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE);
			if (nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE.equals(oValue)) {
				oValue = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40040200", "UPPSCMCommon-000108")/* @res "否" */;
			} else {
				oValue = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"40040200", "UPPSCMCommon-000244")/* @res "是" */;
			}
		} else if (itemCheck.getDataType() == IBillItem.DECIMAL) {
			if (null == PuPubVO.getString_TrimZeroLenAsNull(itemCheck
					.getValue()))
				oValue = "";
			else
				oValue = new nc.vo.pub.lang.UFDouble(itemCheck.getValue(),
						itemCheck.getDecimalDigits());
		} else if (IBillItem.COMBO == itemCheck.getDataType()) {
			if (itemCheck.getComponent() instanceof UIComboBox)
				oValue = ((UIComboBox) itemCheck.getComponent())
						.getSelectdItemValue();
			else
				oValue = itemCheck.getValueObject();

		} else {
			oValue = itemCheck.getValue();
		}

		return nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oValue) == null ? ""
				: oValue.toString();

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
	private String getValueForCardBody(BillCardPanel cardPnl,
			BillItem itemCheck, int iRow) {

		String sKey = itemCheck.getKey();
		Object oValue = null;

		/*
		 * if (itemCheck.getDataType() == IBillItem.COMBO){
		 * nc.ui.pub.beans.UIComboBox ref =
		 * (nc.ui.pub.beans.UIComboBox)itemCheck.getComponent(); oValue =
		 * ref.getSelectedItem().toString(); }else
		 */if (itemCheck.getDataType() == IBillItem.BOOLEAN) {
			oValue = nc.vo.scm.pu.PuPubVO.getUFBoolean_NullAs(cardPnl
					.getBodyValueAt(iRow, sKey),
					nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE);
			if (nc.vo.scm.pu.VariableConst.UFBOOLEAN_FALSE.equals(oValue)) {
				oValue = "否";
			} else {
				oValue = "是";
			}
		} else if (itemCheck.getDataType() == IBillItem.DECIMAL) {
			int iDigit = PoPrintDigitManager.NOT_EXIST;
			// if (m_mngDigit!=null) {
			// iDigit = m_mngDigit.getBodyMnyDigit(iRow,sKey) ;
			// }

			if (iDigit == PoPrintDigitManager.NOT_EXIST) {
				oValue = cardPnl.getBillModel().getValueAt(iRow, sKey);
			} else {
				int iOrgDigit = itemCheck.getDecimalDigits();
				itemCheck
						.setDecimalDigits(nc.vo.scm.field.pu.FieldMaxLength.DECIMALDIGIT_MONEY);
				oValue = nc.vo.scm.pu.PuPubVO
						.getString_TrimZeroLenAsNull(cardPnl.getBillModel()
								.getValueAt(iRow, sKey));

				// 使用精度管理器进行计算
				if (oValue != null) {
					String sRet = ((String) oValue);
					sRet = sRet.substring(0, sRet.indexOf(".") + iDigit + 1);
					oValue = sRet;
				}

				itemCheck.setDecimalDigits(iOrgDigit);
			}
		} else {
			oValue = cardPnl.getBillModel().getValueAt(iRow, sKey);
		}

		return nc.vo.scm.pu.PuPubVO.getString_TrimZeroLenAsNull(oValue) == null ? ""
				: oValue.toString();
	}

	
	/**
	 * 功能：根据ITEM得到界面上的值
	 * 参数：BillItem	itemCheck		待检查的ITEM
	 * 返回：无
	 * 例外：无
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	private String getValueForCardBody(BillItem itemCheck, int iRow) {
		return getValueForCardBody(m_pnlCard, itemCheck, iRow);
	}

	/*
	 *  返回该数据源对应的模块名称
	 */
	public String getModuleName() {
		return m_sPrintID;
	}

	public boolean isNumber(String itemExpress) {
		return false;
	}

	/**
	 * 设置单据数据
	 * 
	 * @param BillCardPanel
	 *            卡片Panel
	 * @return
	 * @exception
	 * @see
	 * @since 2001-07-17
	 */
	public void setBillCardPanel(BillCardPanel pnlCard) {

		this.m_pnlCard = pnlCard;
	}

	
}
