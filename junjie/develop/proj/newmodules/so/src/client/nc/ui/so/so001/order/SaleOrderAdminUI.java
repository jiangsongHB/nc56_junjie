package nc.ui.so.so001.order;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.util.so.junjie.JunjiePubTool;
import nc.vo.pub.BusinessException;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * ���۶������� �������ڣ�(2001-4-13 15:26:05)
 * 
 * @author �ν�
 * 
 * @rebuild V5.1 ���۶���ά�� zhongwei
 */
public class SaleOrderAdminUI extends SaleBillUI {

	// ��ť��ʼ�����
	private boolean b_init;
	
	// ����  ��ť---chenjianhua  2013-04-15
	protected ButtonObject boAdjustPrice;

	public SaleOrderAdminUI() {
		super();
	}

	protected void initCustomerUI() {
		// new OpenSaleOrderForCRM().openNode(this);//test
	}

	public SaleOrderAdminUI(String pk_corp, String billtype, String busitype,
			String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
	}

	@Override
	public String getBillButtonAction(nc.ui.pub.ButtonObject bo) {
		return null;
	}

	@Override
	public String getBillButtonState() {
		return null;
	}

	/**
	 * ��ð�ť���顣
	 * 
	 * ֧�ֲ���˵�,location�������£�0 ��Ƭ�б�;1 �˻�;2 BOM; 3 ����
	 * 
	 * �������ڣ�(2001-11-15 8:58:51)
	 * 
	 * @return nc.ui.pub.ButtonObject[]
	 */
	public nc.ui.pub.ButtonObject[] getBillButtons() {
		if (!b_init) {
			initBtnGrp();
			b_init = true;
		}

		if (strShowState.equals("�б�")) {
			return aryListButtonGroup;
		} else if (strShowState.equals("��Ƭ")) {
			initLineButton();
			return aryButtonGroup;
		} else {
			return aryBatchButtonGroup;
		}
	}

	/**
	 * V51Ҫ��Ƭ���б�İ�ťһ��
	 * 
	 */
	private void initBtnGrp() {

		// �б�ť
		ButtonObject[] aryListButtonGroup = { boBusiType, boAdd, boSave,
				boMaintain, boLine, boAudit, boAction, boQuery, boBrowse,
				boCard, boPrntMgr, boAssistant, boAsstntQry };

		// ��Ƭ��ť---���� ����  ��ť---chenjianhua  2013-04-15
		
		ButtonObject[] aryButtonGroup = { boBusiType, boAdd, boSave,
				boMaintain, boLine, boAudit, boAction, boQuery, boBrowse,
				boReturn, boPrntMgr, boAssistant, boAsstntQry, boMdmx,boAdjustPrice };

		// �˻���Ƭ��ť
		ButtonObject[] aryBatchButtonGroup = { boBatch, boLine,
				boRefundmentDocument, boBack };

		// �������۰�ť��
		ButtonObject[] bomButtonGroup = { boBomSave, boBomEdit, boBomCancel,
				boBomReturn, boOrderQuery,
		// boBomPrint
		};

		/*
		 * IFuncExtend funcExtend = getFuncExtend(); if (funcExtend != null) {
		 * ButtonObject[] boExtend = m_funcExtend.getExtendButton(); if
		 * (boExtend != null && boExtend.length > 0) { // ��Ƭ��ť ButtonObject[]
		 * botempcard = new ButtonObject[aryButtonGroup.length +
		 * boExtend.length];
		 * 
		 * System.arraycopy(aryButtonGroup, 0, botempcard, 0,
		 * aryButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryButtonGroup = botempcard; // �б�ť ButtonObject[] botemplist = new
		 * ButtonObject[aryListButtonGroup.length + boExtend.length];
		 * 
		 * System.arraycopy(aryListButtonGroup, 0, botemplist, 0,
		 * aryListButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryListButtonGroup = botemplist; } } ButtonObject[] boExtend =
		 * getExtendBtns(); if (boExtend != null && boExtend.length > 0) { //
		 * ��Ƭ��ť ButtonObject[] botempcard = new
		 * ButtonObject[aryButtonGroup.length + boExtend.length];
		 * 
		 * System.arraycopy(aryButtonGroup, 0, botempcard, 0,
		 * aryButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryButtonGroup = botempcard; // �б�ť ButtonObject[] botemplist = new
		 * ButtonObject[aryListButtonGroup.length + boExtend.length];
		 * 
		 * System.arraycopy(aryListButtonGroup, 0, botemplist, 0,
		 * aryListButtonGroup.length);
		 * 
		 * System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length,
		 * boExtend.length);
		 * 
		 * aryListButtonGroup = botemplist; }
		 */

		// �������˵�
		ButtonObject[][] ret_butns = new SaleOrderPluginMenuUtil().addMenu(
				aryListButtonGroup, aryButtonGroup, aryBatchButtonGroup,
				bomButtonGroup, getModuleCode());

		this.aryListButtonGroup = ret_butns[0];
		this.aryButtonGroup = ret_butns[1];
		this.aryBatchButtonGroup = ret_butns[2];
		this.bomButtonGroup = ret_butns[3];

	}

	@Override
	public String getBillID() {

		if ("�˻�".equals(strState)) { /*-=notranslate=-*/
			return id;
		}

		if (strShowState == "�б�") {
			return (String) getBillListPanel().getHeadBillModel().getValueAt(
					num, "csaleid");
		} else if (strShowState == "��Ƭ") {
			return (String) getBillCardPanel().getHeadItem("csaleid")
					.getValueObject();
		} else
			return null;
	}

	@Override
	public String getNodeCode() {
		return getModuleCode();
	}

	@Override
	public String getTitle() {
		return getBillCardPanel().getBillData().getTitle();
		// return getBillListPanel().getBillListData().getTitle();
	}

	protected void initButtons() {

		PfUtilClient.retBusiAddBtn(boBusiType, boAdd, getCorpPrimaryKey(),
				SaleBillType.SaleOrder);

		// ҵ������
		// PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(),
		// SaleBillType.SaleOrder);
		if (boBusiType.getChildButtonGroup() != null
				&& boBusiType.getChildButtonGroup().length > 0) {
			boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
			boBusiType.getChildButtonGroup()[0].setSelected(true);
			boBusiType.setCheckboxGroup(true);
			// ����
			// PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(),
			// boBusiType.getChildButtonGroup()[0]);
		}

		// ά��
		boMaintain.removeAllChildren();
		boMaintain.addChildButton(boEdit);
		boMaintain.addChildButton(boCancel);
		boMaintain.addChildButton(boBlankOut);
		boMaintain.addChildButton(boCopyBill);

		// �в���
		boLine.removeAllChildren();
		boLine.addChildButton(boAddLine);
		boLine.addChildButton(boDelLine);
		boLine.addChildButton(boInsertLine);
		boLine.addChildButton(boCopyLine);
		boLine.addChildButton(boPasteLine);
		boLine.addChildButton(boPasteLineToTail);
		boLine.addChildButton(boFindPrice);
		boLine.addChildButton(boCardEdit);
		boLine.addChildButton(boResortRowNo);

		// ���
		boBrowse.removeAllChildren();
		boBrowse.addChildButton(boRefresh);
		boBrowse.addChildButton(boFind);
		boBrowse.addChildButton(boFirst);
		boBrowse.addChildButton(boPre);
		boBrowse.addChildButton(boNext);
		boBrowse.addChildButton(boLast);
		boBrowse.addChildButton(boListSelectAll);
		boBrowse.addChildButton(boListDeselectAll);

		// ִ��
		retElseBtn("Order002", "Order001");
		retElseBtn("Order002", "Order003");

		// ������ѯ
		boAsstntQry.removeAllChildren();
		boAsstntQry.addChildButton(boOrderQuery);
		boAsstntQry.addChildButton(boOnHandShowHidden);
		boAsstntQry.addChildButton(boAuditFlowStatus);
		boAsstntQry.addChildButton(boCustCredit);
		boAsstntQry.addChildButton(boOrderExecRpt);
		boAsstntQry.addChildButton(boCustInfo);
		boAsstntQry.addChildButton(boPrifit);

		// ��ӡ
		boPrntMgr.removeAllChildren();
		boPrntMgr.addChildButton(boPreview);
		boPrntMgr.addChildButton(boPrint);
		boPrntMgr.addChildButton(boSplitPrint);

		// Эͬ
		boAssistant.addChildButton(boCoPushPo);
		boAssistant.addChildButton(boCoRefPo);

		if (strShowState.equals("�б�")) { /*-=notranslate=-*/
			setButtons(getBillButtons());
		}// end list
		else if (strShowState.equals("��Ƭ")) { /*-=notranslate=-*/

			if (strState.equals("BOM")) {
				setButtons(bomButtonGroup);
			} else {
				setButtons(getBillButtons());
			}

		}// end card
	}

	public void setButtonsState() {
		super.setButtonsState();

		getPluginProxy().setButtonStatus();
	}

	@Override
	protected String getClientUI() {
		return SaleOrderAdminUI.class.getName();
	}

	/**
	 * ����onNewʵ��,�൱��ת������
	 * 
	 * @param vo
	 */
	public void addCRMInfo(SaleOrderVO vo) {
		try {
			// �л�����Ƭ
			strShowState = "��Ƭ";
			strState = "����";
			switchInterface();
			setButtons(getBillButtons());
			updateUI();

			// ���۲���ѯ�ۣ�SA15--N,�ָ�ģ���Ĭ��ֵ
			if (!SA_15.booleanValue())
				getBillCardTools().resumeBillBodyItemsEdit(
						getBillCardTools().getSaleOrderItems_Price_Original());

			// PfUtilClient.childButtonClicked(boAdd, getCorpPrimaryKey(),
			// getNodeCode(), getClientEnvironment().getUser()
			// .getPrimaryKey(), getBillType(), this);
			vRowATPStatus = new Vector();

			boolean bisCalculate = getBillCardPanel().getBillModel()
					.isNeedCalculate();
			getBillCardPanel().getBillModel().setNeedCalculate(false);

			if (vo == null) {
				onNewBySelf();
			} else {
				// //ָ��һ��ҵ������
				getBillCardPanel().setBusiType(getSelectedBusyType());
				onNewByOther(new SaleOrderVO[] { vo });
			}

			getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
			if (bisCalculate)
				getBillCardPanel().getBillModel().reCalcurateAll();
			binitOnNewByOther = false;

			// ���ñ���˵�
			setBodyMenuItem(false);
			/** CRM���ۻ��ᴫ�ݹ��������۶�����ʱ�༭������ */
			// �ſ�ҵ�����ͱ༭��
			if (null != getBillCardPanel().getHeadItem("cbiztype")) {
				getBillCardPanel().getHeadItem("cbiztype").setEnabled(true);
				getBillCardPanel().getHeadItem("cbiztype").setEdit(true);
			}

			if (null != getBillCardPanel().getHeadItem("cbiztypename")) {
				getBillCardPanel().getHeadItem("cbiztypename").setEnabled(true);
				getBillCardPanel().getHeadItem("cbiztypename").setEdit(true);
			}
			// ������ע�༭��
			getBillCardPanel().getHeadItem("vnote").setEnabled(false);
			getBillCardPanel().getHeadItem("vnote").setEdit(false);
			/** CRM���ۻ��ᴫ�ݹ��������۶�����ʱ�༭������ */
			// �����������
			getBillCardTools().clearCatheData();

			setButtonsState();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ���������ӿ�ʼ 10-09-15
	@Override
	public void onButtonClicked(ButtonObject bo) {
		super.onButtonClicked(bo);
		if (bo == boMdmx) {
			
			SaleOrderVO oldvo =(SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			getBillCardTools().setOldsaleordervo(oldvo);
			
			// ��ͷVO
			SaleorderHVO hvo = (SaleorderHVO) getVo().getParentVO();
			if (hvo == null) {
				showWarningMessage("��ѡ��Ҫά���뵥��Ϣ�ĵ��ݣ�");
				return;
			}
			int introw = getBillCardPanel().getBodyPanel().getTable()
					.getSelectedRow();
			if (introw < 0) {
				showWarningMessage("��ѡ��Ҫά���뵥��Ϣ�ı����У�");
				return;
			}
			SaleorderBVO bvo = (SaleorderBVO) getBillCardPanel().getBillModel()
					.getBodyValueRowVO(introw, SaleorderBVO.class.getName());
			if (bvo.getCorder_bid() == null)
				return;

			// �ж��Ƿ�����뵥����
			try {
				if (querySfmdwf(bvo.getCinvbasdocid()) == false)
					throw new BusinessException(
							"��ǰ���������ά���뵥�������뵥�����������������������ã�");
			} catch (BusinessException e) {
				e.printStackTrace();
				showWarningMessage(e.getMessage());
				return;
			}
			
			//chenjianhua 2013-04-11
			//SoMdwhDlg dlg = new SoMdwhDlg(hvo, bvo);
			SoMdwhDlg dlg = new SoMdwhDlg(hvo, bvo,this);
			//end 2013-04-11
			
			dlg.showModal();
		}
		//����  ��ť---chenjianhua  2013-04-15
		if (bo == boAdjustPrice) {
			this.getCurrentBillNo();
			SaleOrderVO billvo=(SaleOrderVO) this.getVo();
			//this.getBillID();
			if(billvo==null){
				   MessageDialog.showHintDlg(this,"��ʾ��","��ǰ����Ϊ�գ����Ȳ��Ҫ���۵����۵���"); 
				   return ;
			}
		    String cbiztype =  boBusiType.getTag();		  
		    String businame=JunjiePubTool.getNameByID("bd_busitype", "businame", "pk_busitype", cbiztype);
		    if(businame==null ||!businame.contains("���۷�������")){
			  MessageDialog.showHintDlg(this,"��ʾ��"," ������ѡ�����۷������̣�"); 
			   return ;
		    }		  
		    
		    try {
				super.onCopyBill();//����
			} catch (Exception e) {
			 
				  e.printStackTrace();
				  MessageDialog.showErrorDlg(this,"��ʾ��",e.getMessage()); 
				  return ;
			}
			//���������ι���---ͨ���кŹ���
			SaleorderHVO hvo = (SaleorderHVO) billvo.getParentVO();
			SaleorderBVO[]  bvos = billvo.getBodyVOs();
			String csaleid=hvo.getPrimaryKey();
			
			int rowcount=getBillCardPanel().getRowCount();
			String crowno=null;////�к�
			SaleorderBVO bvo=null;
			for (int i = 0; i < rowcount; i++) {
				crowno= (String) getBillCardPanel().getBodyValueAt(i, "crowno");
				bvo=getSaleorderBVOByCrowno(crowno,bvos);
				getBillCardPanel().setBodyValueAt("30",i, "creceipttype");//��Դ��������
				getBillCardPanel().setBodyValueAt(csaleid,i, "csourcebillid");// ��Դ��������ID
				getBillCardPanel().setBodyValueAt(bvo.getPrimaryKey(),i, "csourcebillbodyid");// ��Դ���ݸ���ID				
			}		
		}//end 2013-04-15
	}
    
	//�����к��ҳ��ӱ�VO chenjianhua--2013-04-17
	private SaleorderBVO getSaleorderBVOByCrowno(String crowno, SaleorderBVO[] bvos) {
		if(crowno==null || bvos==null){
			return null;
		}
		for(int i=0;i<bvos.length;i++){
			if(crowno.equals(bvos[i].getCrowno())){
				return bvos[i];
			}
		}
		return null;
	}

	// �ж��Ƿ��뵥ά��
	public boolean querySfmdwf(String cinvbasid) throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def20 from bd_invbasdoc where pk_invbasdoc='"
				+ cinvbasid + "'";
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("��ǰ����쳣���ڴ�����������в����ڣ�");
		if (objs[0] == null)
			return false;
		else {
			String bz = (String) objs[0];
			bz = bz.toUpperCase();
			if (bz.equals("Y"))
				return true;
			else
				return false;
		}
	}

	// ���������ӿ�ʼ 10-09-15 end
	
	
	
	//chenjianhua  2013-04-11 ���ڵ���
	public boolean onSave() {
		return super.onSave();
	}
	/**
	 * ��ʼ�����а�ť����û��ҵ������
	 * 
	 * �ڳ�ʼ������֮ǰ���ã�����ɸ�д
	 * chenjianhua 2013-04-15
	 * 
	 */
	protected void loadAllBtns() {
	    super.loadAllBtns();
		getBoAdjustPrice();
	}
	// chenjianhua 2013-04-15  ���ӵ���  ��ť
	protected ButtonObject getBoAdjustPrice() {
		if (boAdjustPrice == null) {
			boAdjustPrice = new ButtonObject("����", "����", "����");
		}
		return boAdjustPrice;
	}
}
