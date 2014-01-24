package nc.ui.ja.checkreceipt;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillCardUI billUI,ICardController control){
		super(billUI,control);		
	}

	@Override
	protected void onBoBodyQuery() throws Exception {
		// TODO Auto-generated method stub
		Object money=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("money").getValueObject();
		Object yemoney=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("yemoney").getValueObject();
		//===========
		StringBuffer strWhere = new StringBuffer();
		AggregatedValueObject vo=(AggregatedValueObject) Class.forName(getUIController().getBillVoName()[0]).newInstance();
		SuperVO[] queryVos=null;
		String str=null;
		String sql=null;
		if (askForBodyQueryCondition(strWhere) == false)
			return;// �û������˲�ѯ
		//�����ӹ�-���� ��������enddate  ��ʼ����startdate 
		if(strWhere.indexOf("enddate")!=-1){
			str=strWhere.toString().replace("enddate", "def7");
		}else{
			str=strWhere.toString();
		}		
		if(str.indexOf("startdate")!=-1){
			str=str.toString().replace("startdate", "def7");
		}
		//-----end
		str=str.replace("and (isnull(dr,0)=0)"," ");
		StringBuffer strSql = new StringBuffer();
		sql="select  * from v_ja_check where ";
		strSql.append(sql);
		strSql.append(str);
		
		
		System.out.println(strSql );
		
		queryVos=queryBodyVOS(Class.forName(getUIController().getBillVoName()[2]), strSql.toString());
		vo.setChildrenVO(queryVos);
		getBillUI().setBillOperate(IBillOperate.OP_ADD); 
		getBillCardPanelWrapper().getBillCardPanel().setBillValueVO(vo);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		getBillCardPanelWrapper().getBillCardPanel().updateUI();
		getBillCardPanelWrapper().getBillCardPanel().updateValue();
		//====��ѯ�����������ʾ===
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("money", money);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("yemoney", yemoney);
		IUAPQueryBS tools = NCLocator.getInstance().lookup(
				IUAPQueryBS.class);
		int rows=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		
		for(int i=0;i<rows;i++){
			Object syspk=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def10");
			Object taxamount=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "taxamount");
			//ϵͳ��Ʊ�ۻ��������-----start
			sql="select sum(tax)  from ja_entity_receipt_ck " +
			"where def4='"+syspk+"'";
			Object[] sys_sum= (Object[]) tools.executeQuery(sql, new ArrayProcessor());
			if(sys_sum[0]==null){
				sys_sum[0]=0;
			}
			
			//ϵͳ��Ʊ���������=ϵͳ���-ϵͳ��Ʊ�ۻ��������
			Double tempmoney= new Double(taxamount.toString())-(new Double(sys_sum[0].toString()));
			
			//ϵͳ��Ʊ���������		
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt( tempmoney,i,"tempmoney");
			//ϵͳ���������Ϊ0�����ܲ������[����ѡ]
			if(tempmoney<=0d){
				getBillCardPanelWrapper().getBillCardPanel().setCellEditable(i, "ischoice", false);	
			}
		}
	}
	/*
	 * 
	 */
	private SuperVO[] queryBodyVOS(Class className, String sqlStr){
		IUAPQueryBS tools = NCLocator.getInstance().lookup(
				IUAPQueryBS.class);
		ArrayList<SuperVO> listVOs;
		try {
			listVOs = (ArrayList<SuperVO>) tools.executeQuery(sqlStr, new BeanListProcessor(className));
			if(listVOs !=null&&listVOs.size()>0){
				SuperVO[] queryVos = new SuperVO[listVOs.size()];
				listVOs.toArray(queryVos);
				return queryVos;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onBoSelAll() throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		for (int i = 0; i < rows; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", i, "ischoice");
			getBillCardPanelWrapper().getBillCardPanel().setCellEditable(i, "checkamount", true);				
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "taxamount"), i, "checkamount");
		}
	}

	@Override
	protected void onBoSelNone() throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		for (int i = 0; i < rows; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("N", i, "ischoice");
			getBillCardPanelWrapper().getBillCardPanel().setCellEditable(i, "checkamount", false);
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "checkamount");
		}
	}
	
		
}