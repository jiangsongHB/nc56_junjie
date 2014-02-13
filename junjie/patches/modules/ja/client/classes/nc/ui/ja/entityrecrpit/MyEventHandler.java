package nc.ui.ja.entityrecrpit;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.workshop.plugins.formdev.IUAPBillQueryService;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.ja.entityreceiptck.JaEntityReceiptCkVO;
import nc.vo.ja.pub.itf.IEntityReceipt;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.ui.pub.ButtonObject;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	
	  @Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		  onBoCard();
		  onBoRefresh();
		  boolean flag=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel("ja_entity_receipt_b").isShowing();
		  boolean ischeck= isCheckedBill();
		  
		  if(flag){	
			  getBillCardPanelWrapper().getBillCardPanel().getBillModel("ja_entity_receipt_b").setEnabledAllItems(true);
			  if(ischeck){
				  this.getBillUI().showWarningMessage("单据已做过核销操作，不能再修改");	
				  return;
			  }
			  getBillUI().setBillOperate(3);
		  }else{		
			  if(ischeck){				
				  getBillCardPanelWrapper().getBillCardPanel().getBillModel("ja_entity_receipt_b").setEnabledAllItems(false);
			  }
			  getBillUI().setBillOperate(3);
		  }
		//super.onBoEdit();
	}
	 //单据是否做过核销
	private boolean isCheckedBill() throws Exception {
		 Object pk=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_entity_receipt").getValueObject();
		  IUAPQueryBS iquery = NCLocator.getInstance().lookup(
				  IUAPQueryBS.class);
		  String sql="select totalamount from ja_entity_receipt_b where pk_entity_receipt='"+pk+"' and nvl(dr,0)=0";
		  ArrayList list=(ArrayList)iquery.executeQuery(sql, new ArrayListProcessor());
		  if(list!=null){
			  for (int j = 0; j < list.size(); j++) {
				  
				  Object[] obj=(Object[]) list.get(j);
				  if(obj[0]!=null&&new Double(obj[0].toString())>0d){
					  		
					 return true;
				  }
			  }
		  }
		  return false;
	}  

	@Override
	protected void onBoLineDel() throws Exception {
		// TODO Auto-generated method stub
		boolean flag=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel("ja_entity_receipt_b").isShowing();
		if(flag){
			super.onBoLineDel();
		}else{
			//表ja_entity_receipt_ck.def3 -- 实体发票明细pk
			//1获取要删除的核销记录的金额及所对应的实体发票明细pk
			CircularlyAccessibleValueObject[] vos=getBillCardPanelWrapper().getSelectedBodyVOs();
			if(vos==null){
				this.getBillUI().showWarningMessage("选择所删除的核销记录");	
				  return;
			}
			
			int show=this.getBillUI().showOkCancelMessage("删除后不可撤销，确认删除?");
			if(show==1){
				for (int i = 0; i < vos.length; i++) {
					JaEntityReceiptCkVO CkVO=new JaEntityReceiptCkVO();
					CkVO.setAttributeValue("def3", vos[i].getAttributeValue("def3"));
					CkVO.setAttributeValue("tax", vos[i].getAttributeValue("tax"));
					//2数据库进行加法，把核销的金额加回，根据实体发票明细pk
					IEntityReceipt ier=NCLocator.getInstance().lookup(IEntityReceipt.class);
					ier.onUnCheck(CkVO);
				}
				super.onBoLineDel();
				super.onBoSave();
			}
		}
		
	}




	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		 getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		super.onBoSave();
	}
	  

	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		
		boolean flag=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel("ja_entity_receipt_b").isShowing();
		if(flag){
			super.onBoLineAdd();
		}		
	}


	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();
		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		// SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
		SuperVO[] queryVos = null;
		//System.out.println(strWhere);
		
		StringBuffer strSql = new StringBuffer();
		String str1="ja_entity_receipt_b";
		String str1fpk="ja_entity_receipt_b.pk_entity_receipt";
		String str2="ja_entity_receipt_ck";
		String str2fpk="ja_entity_receipt_ck.pk_entity_receipt";
		if (strWhere.toString().contains(str1)&&strWhere.toString().contains(str2)) {
			
			String sql = "";
			String sqlwhere = strWhere.toString().replace("(isnull(dr,0)=0) and pk_corp", "ja_entity_receipt.pk_corp");
			System.out.println(sqlwhere);
			sql = "select distinct ja_entity_receipt.* from ja_entity_receipt  "
					+ " inner join "+str1+" on ja_entity_receipt.pk_entity_receipt = "+str1fpk
					+ " inner join "+str2+" on ja_entity_receipt.pk_entity_receipt = "+str2fpk;
			strSql.append(sql);
			strSql.append(" where isnull(ja_entity_receipt.dr,0)=0 and isnull("+str1+".dr,0)=0 and isnull("+str2+".dr,0)=0 and ");
			strSql.append(sqlwhere);
			queryVos = queryHeadVOS(Class.forName(getUIController().getBillVoName()[1]), strSql.toString());
		}else 
		 if (strWhere.toString().contains(str1)||strWhere.toString().contains(str2)) {
			String str=str1;
			String strfpk=str1fpk;
			if(strWhere.toString().contains(str2)){
				str=str2;
				strfpk=str2fpk;
			}
			String sql = "";
			String sqlwhere = strWhere.toString().replace("(isnull(dr,0)=0) and pk_corp", "ja_entity_receipt.pk_corp");
			System.out.println(sqlwhere);
			sql = "select distinct ja_entity_receipt.* from ja_entity_receipt  "
					+ "inner join "+str+" on ja_entity_receipt.pk_entity_receipt = "+strfpk;
			strSql.append(sql);
			strSql.append(" where isnull(ja_entity_receipt.dr,0)=0 and isnull("+str+".dr,0)=0 and ");
			strSql.append(sqlwhere);
			queryVos = queryHeadVOS(Class.forName(getUIController().getBillVoName()[1]), strSql.toString());
		}else {
			queryVos = queryHeadVOs(strWhere.toString());
		}
		System.out.println(strSql);
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	private SuperVO[] queryHeadVOS(Class className, String sqlStr){
		IUAPQueryBS tools = NCLocator.getInstance().lookup(
				IUAPQueryBS.class);
		ArrayList<SuperVO> listVOs;
		try {
			listVOs = (ArrayList<SuperVO>) tools.executeQuery(sqlStr, new BeanListProcessor(className));
			if(listVOs !=null&&listVOs.size()>0){
				SuperVO[] headVOs = new SuperVO[listVOs.size()];
				listVOs.toArray(headVOs);
				return headVOs;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
		public void onBoAdd(ButtonObject bo) throws Exception {
			// TODO Auto-generated method stub
			super.onBoAdd(bo);
			
			 // System.out.println("======start  vbillno==========");
			  String code=getVbillCode(this.getBillCardPanelWrapper().getBillCardPanel().getBillType(),
					  this.getBillCardPanelWrapper().getBillCardPanel().getCorp());
			  //设置单据号到字段vbillno
			  //System.out.println(code);
			  getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setValue(code);
		}

	private String getVbillCode(String billType,String pk_corp){
		try {
			BillcodeGenerater gene  = new BillcodeGenerater ();
			return gene.getBillCode (billType,pk_corp,null,null);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		  
	  }	
}