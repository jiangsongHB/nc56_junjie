package nc.ui.ja.entityrecrpit;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.workshop.plugins.formdev.IUAPBillQueryService;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
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
		System.out.println(strWhere);
		
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