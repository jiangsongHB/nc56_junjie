package nc.ui.so.so001.revise;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so001.order.SaleOrderPluginMenuUtil;
import nc.ui.so.so001.order.SoMdwhDlg;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.vo.pub.BusinessException;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * ���۶����޶�
 * 
 * @version	V5.1
 * 
 * @author zhongwei
 *
 */
public class SaleOrderReviseUI extends SaleBillUI {

	//��ť��ʼ�����
	private boolean b_init_btn;
	
	private boolean isInitQuery = false;
	
	public SaleOrderReviseUI(){
		super();
		super.b_isRevise = true;
		
		//v5.6���Ӵ������ͣ���Ϊ��һ�β�ѯʱ����
		//resetQueryDlg();
		
		getBillCardPanel().setBillType(SaleBillType.SaleOrder);
		getBillListPanel().setBillType(SaleBillType.SaleOrder);
	}
	
	private void resetQueryDlg(){
		getQueryDlg();
		
		getQueryDlg().m_rdoAll.setSelected(false);
		getQueryDlg().m_rdoAudited.setSelected(true);
		
		getQueryDlg().m_rdoAll.setEnabled(false);
		getQueryDlg().m_rdoFree.setEnabled(false);
		getQueryDlg().m_rdoAuditing.setEnabled(false);
		getQueryDlg().m_rdoAudited.setEnabled(false);
		getQueryDlg().m_rdoFreeze.setEnabled(false);
//		getQueryDlg().m_rdoBatch.setEnabled(false);
		getQueryDlg().m_rdoBlankOut.setEnabled(false);
		getQueryDlg().m_rdoClosed.setEnabled(false);
		
	}
	
	/**
	 * ��ѯ���ݡ�
	 * 
	 */
	protected void onQuery() {

		if (!isInitQuery){
			isInitQuery = true;
			resetQueryDlg();
		}
		
		// ��ʾ��ѯ�Ի���
		if (getQueryDlg().showModal() == QueryConditionClient.ID_CANCEL)
			return;

		onRefresh();

	}
	
	@Override
	public String getBillButtonAction(ButtonObject bo) {
		return null;
	}
	
	@Override
	public String getBillButtonState() {
		return null;
	}
	
	@Override
	public ButtonObject[] getBillButtons() {
		if (!b_init_btn) {
			initBtnGrp();
			b_init_btn = true;
		}

		if (strShowState.equals("�б�")) {
			return aryListButtonGroup;
		} else if (strShowState.equals("��Ƭ")) {	
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
		aryListButtonGroup = new ButtonObject[] { boSave, boMaintain, boLine,
				boQuery, boBrowse, boCard, boPrntMgr, boAsstntQry };

		// ��Ƭ��ť
		aryButtonGroup = new ButtonObject[] { boSave, boMaintain, boLine,
				boQuery, boBrowse, boReturn, boPrntMgr, boAsstntQry ,boMdmx};

		// �������˵�
		ButtonObject[][] ret_butns = new SaleOrderPluginMenuUtil().addMenu(aryListButtonGroup, aryButtonGroup,
				aryBatchButtonGroup, bomButtonGroup, getModuleCode());

		this.aryListButtonGroup = ret_butns[0];
		this.aryButtonGroup = ret_butns[1];
		this.aryBatchButtonGroup = ret_butns[2];
		this.bomButtonGroup = ret_butns[3];
	}
	
	protected void initButtons(){

		// ҵ������
		/** �����Ƿ����ҵ�����ͣ���ť�����ʼ�����������������ť״̬ˢ���쳣* */
		PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(),
				SaleBillType.SaleOrder);
		if (boBusiType.getChildButtonGroup() != null
				&& boBusiType.getChildButtonGroup().length > 0) {
			boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
			boBusiType.getChildButtonGroup()[0].setSelected(true);
			boBusiType.setCheckboxGroup(true);
		}
		/** �����Ƿ����ҵ�����ͣ���ť�����ʼ�����������������ť״̬ˢ���쳣* */

		// ά��
		boMaintain.removeAllChildren();
		boMaintain.addChildButton(boModification);
		boMaintain.addChildButton(boCancel);
		
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
		
		//������ѯ
		boAsstntQry.removeAllChildren();
		boAsstntQry.addChildButton(boOrderQuery);
		boAsstntQry.addChildButton(boOnHandShowHidden);
		boAsstntQry.addChildButton(boAuditFlowStatus);
		boCustCredit.setTag("CustCredited");
		boAsstntQry.addChildButton(boCustCredit);
		boOrderExecRpt.setTag("OrderExecRpt");
		boAsstntQry.addChildButton(boOrderExecRpt);
		boCustInfo.setTag("CustInfo");
		boAsstntQry.addChildButton(boCustInfo);
		boPrifit.setTag("Prifit");
		boAsstntQry.addChildButton(boPrifit);

		//��ӡ
		boPrntMgr.removeAllChildren();
		boPrntMgr.addChildButton(boPreview);
		boPrntMgr.addChildButton(boPrint);
		boPrntMgr.addChildButton(boSplitPrint);
		
		setButtons(getBillButtons());
	}
	
	@Override
	public String getBillID() {
		if ("�˻�".equals(strState)) { /*-=notranslate=-*/
			return id;
		}

		if (num >= 0
				&& num < getBillListPanel().getHeadBillModel().getRowCount())
			return (String) getBillListPanel().getHeadBillModel().getValueAt(
					num, "csaleid");
		else
			return null;
	}

	@Override
	public String getTitle() {
		return getBillListPanel().getBillListData().getTitle();
	}

	@Override
	public String getNodeCode() {
		return "40060301";
	}

	@Override
	protected String getClientUI() {
		return SaleOrderReviseUI.class.getName();
	}

	/**add by ouyangzhb 2013-04-22 �����޶������뵥��������*/
	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO Auto-generated method stub
		super.onButtonClicked(bo);

		
		if (bo == boMdmx) {
			SaleOrderVO oldvo =(SaleOrderVO) getBillCardPanel().getBillValueVO(
					SaleOrderVO.class.getName(), SaleorderHVO.class.getName(),
					SaleorderBVO.class.getName());
			getBillCardTools().setOldsaleordervo(oldvo);
			
			// ��ͷVO
			SaleorderHVO hvo = (SaleorderHVO) getVo().getParentVO().clone();
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
					.getBodyValueRowVO(introw, SaleorderBVO.class.getName()).clone();
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
			
			SoMdwhDlg dlg = new SoMdwhDlg(hvo, bvo,this);
			dlg.showModal();
		}
	}
	
	public boolean onSave() {
		return super.onSave();
	}
	
	public void onModification(){
		super.onModification();
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
	/**add by ouyangzhb 2013-04-22 �����޶������뵥�������� end */
	
}
