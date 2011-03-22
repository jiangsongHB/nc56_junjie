package nc.ui.pu.jjpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import bsh.This;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.bd.ref.busi.InvmandocDefaultRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.vo.ic.jjvo.InformationCostVO;
import nc.vo.ml.IProductCode;
import nc.vo.po.OrderItemVO;
import nc.vo.pu.jjvo.InvDetailVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.rc.receive.ArriveorderItemVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * �ɹ����� �� �����ϸά������
 * @author MeiChao
 * @date 2011-01-04
 * 
 */
public class InvDetailDialog421 extends UIDialog implements ActionListener,BillEditListener {

	//�ϼ���Ƭ  by ������ 2010-10-13
	private BillCardPanel parentCardPanel = null;
	
	private BillListPanel billListPanel = null;
	// ȷ����ť
	private nc.ui.pub.beans.UIButton m_btnOK = null;
	// ȡ����ť
	private nc.ui.pub.beans.UIButton m_btnCancel = null;
	//2010-12-24 MeiChao add ���㰴ť 2011��1��18�� ����ȫ��ȡ��,��������ֳ�����(�˴���ע��,ֻ�ڰ�ťע�ᴦȡ��ע��)
	private nc.ui.pub.beans.UIButton m_btnCalculate = null;
	//2011-01-06 MeiChao add ���밴ť
	private nc.ui.pub.beans.UIButton m_btnInput = null;
	//2010-12-24 MeiChao add �Ƿ�������(������ֳ�����) 2011��1��18�� ����ȫ��ȡ��,��������ֳ�����
	private boolean isCalculate=true;
	//��ϸ�ֳ������ܺ�  2011-01-18 MeiChao add
	private UFDouble invdetailWeightSum=new UFDouble(0);
	public UFDouble getInvdetailWeightSum() {
		return invdetailWeightSum;
	}

	public void setInvdetailWeightSum(UFDouble invdetailWeightSum) {
		this.invdetailWeightSum = new UFDouble(invdetailWeightSum.doubleValue());
	}
	//2010-12-27 MeiChao add �����Ƿ�ȫΪ����
	private boolean isLengthAllNumber=true;
	//2011-01-06 MeiChao  add �Ƿ�����(����������,����,ɾ��,�޸�.)
	private boolean isCLocked=true;
	//2011-01-14 MeiChao add ����ʱ���صĴ�����Ϣ�ַ���;
	private String checkErrorInfo="";
	// ���Ӱ�ť
	private nc.ui.pub.beans.UIButton m_btnAdd = null;
	// ɾ����ť
	private nc.ui.pub.beans.UIButton m_btnDel = null;
	// �޸İ�ť
	private nc.ui.pub.beans.UIButton m_btnMod = null;
	// ��˾ID
	private String m_sLoginCorp = null;
	// ����ԱID
	private String m_sOperator = null;
	//�������id
	private String m_sPK_InvBasDoc=null;
	// ��ť���
	private nc.ui.pub.beans.UIPanel m_panelSouth = null;
	// UIDialog�����
	private javax.swing.JPanel m_dialogContentPane = null;
	// ������
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceWest = null;
	private nc.ui.pub.beans.UIPanel m_pnlFillSpaceEast = null;
	private boolean m_closeMark = false;
	// �����ϸ����
	private InvDetailVO[] invDetailVOs = null;
	//�ɴ����ϸ����(��ʼ��ʱ�������)
	private InvDetailVO[] oldInvDetailVOs = new InvDetailVO[0];
	public InvDetailVO[] getOldInvDetailVOs() {
		return oldInvDetailVOs;
	}

	public void setOldInvDetailVOs(InvDetailVO[] oldInvDetailVOs) {
		this.oldInvDetailVOs = oldInvDetailVOs;
	}

	/**
	 * ȡ�����ϸ,�Զ�����.
	 * @return
	 */
	public InvDetailVO[] getInvDetailVOs() {
		return invDetailVOs;
	}

	public void setInvDetailVOs(InvDetailVO[] invDetailVOs) {
		this.invDetailVOs = invDetailVOs;
	}
	//��ѡ������VO
	private OrderItemVO selectedBody=null;
	public OrderItemVO getSelectedOrderBody() {
		return selectedBody;
	}
	public void setSelectedOrderBody(OrderItemVO selectedArriveBody) {
		this.selectedBody = selectedArriveBody;
	}
	// ��ǰ�������� by ������ 2010-10-30
	private String billtype = null;
	
	/**
	 * @function ���븸����,��˾,����Ա�Ĺ��캯��
	 * 
	 * @author MeiChao
	 * 
	 * @date 2010-8-20 ����10:02:36
	 * 
	 */
	public InvDetailDialog421(java.awt.Container parent, String sPk_Corp,
			String sOperatorID) {
		super(parent);
		m_sLoginCorp = sPk_Corp;
		initDialog(); // ��ʼ���Ի���
	}

	/**
	 * @function ��д���ഫ�븸�����Ĺ��캯��
	 * 
	 * @author MeiChao
	 * 
	 * @date 2010-8-20 ����10:12:28
	 * 
	 */
	public InvDetailDialog421(java.awt.Container parent,InvDetailVO[] vos,OrderItemVO selectedOrderBodyRow) {
		super(parent);
		initDialog();
		
		this.getBillListPanel().setHeaderValueVO(vos);
		this.getBillListPanel().getHeadBillModel().execLoadFormula();
		if (vos != null && vos.length > 0) {
			this.setOldInvDetailVOs(vos);
			for (int i = 0; i < vos.length; i++) {// ������״̬Ϊ: ��ͨ. ��VO״̬����Ϊ δ�ı�
				this.getBillListPanel().getHeadBillModel().setRowState(i,
						BillModel.NORMAL);
				this.oldInvDetailVOs[i].setStatus(VOStatus.UNCHANGED);
				if(this.isLengthAllNumber){//�����ǰ�ĳ����Ƿ����ֱ�ʶΪtrue ,��ô�������һ��
					if(!this.isDoueric(vos[i].getContractlength())){
						this.isLengthAllNumber=false;//һ�����ڷ����ֳ���,��ô���������ֱ�ʶ�޸�Ϊfalse
					}
				}
			}
		}
		this.setSelectedOrderBody(selectedOrderBodyRow);
		this.m_sPK_InvBasDoc=selectedOrderBodyRow.getCbaseid();
	}
	public InvDetailDialog421(java.awt.Container parent) {
		super(parent);
		initDialog();
		}

	/**
	 * @function ��ʼ���Ի���
	 * 
	 * @author MeiChao
	 * 
	 * @return void
	 * 
	 * @date 2010-8-20 ����10:13:42
	 */
	private void initDialog() {

		setName("InvDetail");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("������ϸ");
		setBounds(37, 563, 1000, 400);
		setResizable(false);
		// ������ʾ����
		setContentPane(getUIDialogContentPane());
		// �޸Ĳ���,ֻ��ʾӦ˰�����־Ϊ'Y'�Ĵ��
//		UIRefPane refPane = (UIRefPane) getBillListPanel().getHeadItem("costcode").getComponent();
//		nc.ui.bd.ref.busi.InvmandocDefaultRefModel refModel = (InvmandocDefaultRefModel) refPane.getRefModel();
//		refModel.setWherePart(" bd_invbasdoc.laborflag = 'Y' and bd_invmandoc.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"'");
		
		//add by ouyangzhb 2011-03-22   ��ʾ�ϼ���
		this.getBillListPanel().getParentListPanel().setTatolRowShow(true);
		

	}

	/**
	 * @function �õ���ʾ�������
	 * 
	 * @author MeiChao
	 * 
	 * @return javax.swing.JPanel
	 * 
	 * @date 2010-8-20 ����10:14:10
	 */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (m_dialogContentPane == null) {
			m_dialogContentPane = new javax.swing.JPanel();
			m_dialogContentPane.setName("UIDialogContentPane");
			m_dialogContentPane.setLayout(new java.awt.BorderLayout());
			// ��ťpanel
			m_dialogContentPane.add(getPnlSouth(), "South");
			// ��˾panel
			m_dialogContentPane.add(getBillListPanel(), "Center");
			// �߽����panel
			m_dialogContentPane.add(getPnlFillSpaceWest(), "West");
			m_dialogContentPane.add(getPnlFillSpaceEast(), "East");
		}
		return m_dialogContentPane;
	}

	/**
	 * @function ��ð�ť���
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 ����10:15:33
	 */
	private nc.ui.pub.beans.UIPanel getPnlSouth() {
		if (m_panelSouth == null) {
			m_panelSouth = new nc.ui.pub.beans.UIPanel();
			m_panelSouth.setName("PnlSouth");
			// ����
			m_panelSouth.add(getBtnAdd(), getBtnAdd().getName());
			// ɾ��
			m_panelSouth.add(getBtnDel(), getBtnDel().getName());
			//���� 2011��1��18�� ����ȫ��ȡ��,��������ֳ����� ���ظð�ť
			//m_panelSouth.add(getBtnCalculate(),getBtnCalculate().getName());
			// ȷ��
			m_panelSouth.add(getBtnOK(), getBtnOK().getName());
			// ȡ��
			m_panelSouth.add(getBtnCancel(), getBtnCancel().getName());
			//�޸�
			m_panelSouth.add(getBtnMod(),getBtnMod().getName());
			//����
			m_panelSouth.add(getBtnInput(),getBtnInput().getName());
		}
		return m_panelSouth;
	}

	/**
	 * @function �õ�ȷ����ť
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:19:17
	 */
	private nc.ui.pub.beans.UIButton getBtnOK() {
		if (m_btnOK == null) {
			m_btnOK = new nc.ui.pub.beans.UIButton();
			m_btnOK.setName("BtnOK");
			HotkeyUtil.setHotkeyAndText(m_btnOK, 'O', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000044"));/* @res "ȷ��" */
			m_btnOK.addActionListener(this);
		}
		return m_btnOK;
	}

	/**
	 * ��ȡ "����" ��ť
	 *@author MeiChao
	 *@date 2010-12-24 
	 */
	private nc.ui.pub.beans.UIButton getBtnCalculate() {
		if (this.m_btnCalculate== null) {
			m_btnCalculate = new nc.ui.pub.beans.UIButton();
			m_btnCalculate.setName("����");
			HotkeyUtil.setHotkeyAndText(m_btnCalculate, 'C', "����");/* @res "����" */
			m_btnCalculate.addActionListener(this);
		}
		return m_btnCalculate;
	}
	
	/**
	 * ��ȡ "����" ��ť--����excel���͵ĸֳ�����
	 *@author MeiChao
	 *@date 2011-01-06 
	 */
	private nc.ui.pub.beans.UIButton getBtnInput() {
		if (this.m_btnInput== null) {
			m_btnInput = new nc.ui.pub.beans.UIButton();
			m_btnInput.setName("����");
			HotkeyUtil.setHotkeyAndText(m_btnInput, 'I', "����");/* @res "����excel" */
			m_btnInput.addActionListener(this);
		}
		return m_btnInput;
	}
	
	
	/**
	 * @function �õ�ɾ����ť
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:19:39
	 */
	private nc.ui.pub.beans.UIButton getBtnDel() {
		if (m_btnDel == null) {

			m_btnDel = new nc.ui.pub.beans.UIButton("ɾ��");
			m_btnDel.setName("BtnDel");

			m_btnDel.addActionListener(this);
		}
		return m_btnDel;
	}

	/**
	 * @function �õ����Ӱ�ť
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:20:49
	 */
	private nc.ui.pub.beans.UIButton getBtnAdd() {
		if (m_btnAdd == null) {

			m_btnAdd = new nc.ui.pub.beans.UIButton("����");
			m_btnAdd.setName("BtnAdd");

			m_btnAdd.addActionListener(this);
		}
		return m_btnAdd;
	}
	/**
	 * @function �õ��޸İ�ť
	 *
	 * @author MeiChao
	 *
	 * @return nc.ui.pub.beans.UIButton
	 *
	 * @date 2010-9-25 ����09:49:41
	 */
	private nc.ui.pub.beans.UIButton getBtnMod() {
		if (m_btnMod == null) {

			m_btnMod = new nc.ui.pub.beans.UIButton("�޸�");
			m_btnMod.setName("BtnMod");

			m_btnMod.addActionListener(this);
		}
		return m_btnMod;
	}

	/**
	 * @function �õ�ȡ����ť
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 * 
	 * @date 2010-8-20 ����10:22:01
	 */
	private nc.ui.pub.beans.UIButton getBtnCancel() {
		if (m_btnCancel == null) {
			m_btnCancel = new nc.ui.pub.beans.UIButton();
			m_btnCancel.setName("BtnCancel");
			HotkeyUtil.setHotkeyAndText(m_btnCancel, 'C', nc.ui.ml.NCLangRes
					.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,
							"UC001-0000008"));/* @res "ȡ��" */
			m_btnCancel.addActionListener(this);
		}
		return m_btnCancel;
	}

	/**
	 * @function �õ�����������Ķ����
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 ����10:22:15
	 */
	private nc.ui.pub.beans.UIPanel getPnlFillSpaceEast() {
		if (m_pnlFillSpaceEast == null) {
			m_pnlFillSpaceEast = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceEast.setName("PnlFillSpaceEast");
			m_pnlFillSpaceEast.setPreferredSize(new java.awt.Dimension(15, 10));
		}
		return m_pnlFillSpaceEast;
	}

	/**
	 * @function �õ�����������������
	 * 
	 * @author MeiChao
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 * 
	 * @date 2010-8-20 ����10:22:47
	 */
	private nc.ui.pub.beans.UIPanel getPnlFillSpaceWest() {
		if (m_pnlFillSpaceWest == null) {
			m_pnlFillSpaceWest = new nc.ui.pub.beans.UIPanel();
			m_pnlFillSpaceWest.setName("PnlFillSpaceWest");
			m_pnlFillSpaceWest.setPreferredSize(new java.awt.Dimension(15, 16));
		}
		return m_pnlFillSpaceWest;
	}

	/**
	 * @function �õ������б����
	 * 
	 * @author MeiChao
	 * 
	 * @return BillListPanel
	 * 
	 * @date 2010-8-20 ����10:23:09
	 */
	private BillListPanel getBillListPanel() {
		if (billListPanel == null) {
			billListPanel = new BillListPanel();
			// ����ģ��
			billListPanel.loadTemplet("JJ001", "", this.m_sOperator, this.m_sLoginCorp);	
			//billListPanel.getHeadBillModel().setEnabledAllItems(true);
			billListPanel.addEditListener(this);
		}
		return billListPanel;
	}

	/**
	 * @function ��ť��������
	 * 
	 * @author MeiChao
	 * 
	 * @param e
	 * 
	 * @date 2010-8-20 ����10:41:37
	 */
	public void actionPerformed(ActionEvent e) {
		// ȡ����ť�Ķ���
		if (e.getSource() == this.getBtnCancel()) {
			this.m_closeMark = false;
			this.closeCancel();
		}
		// ȷ����ť����
		else if (e.getSource() == this.getBtnOK()) {
			//�ж��Ƿ��Ѽ���() 2011��1��18�� ����ȫ��ȡ��,��������ֳ����� ע�͵����ж�
//			if(!this.isCalculate){
//				if(!this.isLengthAllNumber){
//					MessageDialog.showErrorDlg(this,"��ʾ","���ֶ�����ֳ�����.");
//					return;
//				}else{
//					MessageDialog.showHintDlg(this,"��ʾ","����ʹ��\"����\"��ť����ֳ�����!");
//					return;
//				}
//			}
			invDetailVOs = (InvDetailVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InvDetailVO.class.getName());
			if( invDetailVOs == null || invDetailVOs.length == 0){
				this.m_closeMark = true;
				this.closeOK();
			}
               String message = voCheck(invDetailVOs).trim();
			if(!message.equals("")){
				MessageDialog.showHintDlg(this, "��ʾ",message);
				return;
			}
			if(!this.isLengthAllNumber){
				/**
				 * ���ȼ����ϸ�Ƿ���ά�����: ��ϸ֧��=��ѡ�ж�������
				 */
				//��ȡ��ϸVO����
				//invDetailVOs = (InvDetailVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InvDetailVO.class.getName());
				//��ϸ���������ܺ�
				UFDouble detailSumnumber=new UFDouble(0);
				//��ϸ�ֳ������ܺ�
				UFDouble detailSumweight=new UFDouble(0);
				for(int i=0;i<invDetailVOs.length;i++){
					detailSumnumber=detailSumnumber.add(invDetailVOs[i].getArrivenumber());
					detailSumweight=detailSumweight.add(invDetailVOs[i].getContractweight());
				}
				this.setInvdetailWeightSum(detailSumweight);
				//��ϸ�����ܺ�,��������֮��
				UFDouble diffNuma=detailSumnumber.sub(this.selectedBody.getNassistnum());
				//��ϸ�ֳ������ܺ�,�����ֳ�����֮��
				UFDouble diffNumb=detailSumweight.sub(this.selectedBody.getNordernum());
				if(diffNuma.doubleValue()>0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
					MessageDialog.showHintDlg(this,"����","�����ϸ֧���ܺͳ����������,\n�������Ϊ:"+this.selectedBody.getNassistnum()+"\n ��ǰ��ϸ����Ϊ:"+detailSumnumber.setScale(3,UFDouble.ROUND_HALF_UP)+"\n����!");
					return;
				}else if(diffNuma.doubleValue()<0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
					MessageDialog.showHintDlg(this,"����","�����ϸ֧���ܺ�С�ڱ������,\n�������Ϊ:"+this.selectedBody.getNassistnum()+"\n ��ǰ��ϸ����Ϊ:"+detailSumnumber.setScale(3,UFDouble.ROUND_HALF_UP)+"\n����!");
					return;
				}
				//2010-12-27 MeiChao ��� �����к�����ĸ,��ô��Ҫ���ֳ��������Ƿ��뵽�������������.
//				if(diffNumb.doubleValue()>0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
//					MessageDialog.showHintDlg(this,"����","�����ϸ�ֳ������ܺͳ�����������,\n��������Ϊ:"+this.selectedBody.getNordernum()+"\n ��ǰ��ϸ������Ϊ:"+detailSumweight.setScale(3,UFDouble.ROUND_HALF_UP)+"\n����!");
//					return;
//				}else if(diffNumb.doubleValue()<0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
//					MessageDialog.showHintDlg(this,"����","�����ϸ�ֳ������ܺ�С�ڱ�������,\n��������Ϊ:"+this.selectedBody.getNordernum()+"\n ��ǰ��ϸ������Ϊ:"+detailSumweight.setScale(3,UFDouble.ROUND_HALF_UP)+"\n����!");
//					return;
//				}
			}
			int invDetailNum=this.getBillListPanel().getHeadBillModel().getRowCount();
			//��ϸ�����ܺ�
			UFDouble detailSum=new UFDouble(0);
			//��ϸ�ֳ������ܺ�
			UFDouble detailSumweight=new UFDouble(0);
			//��ϸVO����,���г����Ѵ��ڵ���ϸVO��,�������˱�ɾ������ϸVO
			List<InvDetailVO> allInvDetailList=new ArrayList<InvDetailVO>();
			for(int i=0;i<invDetailVOs.length;i++){//������ϸVO��ע��һЩϵͳ��Ϣ.
				detailSumweight=detailSumweight.add(invDetailVOs[i].getContractweight());//�ۼӸֳ�����
				if(invDetailVOs[i].getPk_invdetail()==null){//�����ϸ����Ϊ��,˵���϶��������ļ�¼
				//invDetailVOs[i].setCarriveorder_bid(this.selectedBody.getCarriveorder_bid());
				invDetailVOs[i].setCorder_bid(this.selectedBody.getCorder_bid());
				invDetailVOs[i].setCreatedate(new UFDate(new Date()));
				invDetailVOs[i].setCreateoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());
				invDetailVOs[i].setCreatetime(new UFDateTime(new Date()));
				invDetailVOs[i].setPk_invbasdoc(this.selectedBody.getCbaseid());
				invDetailVOs[i].setPk_corp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
				detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());//�ۼ��ܼ���
				invDetailVOs[i].setDr(0);//��ʼ��ɾ�����
				invDetailVOs[i].setStatus(VOStatus.NEW);//VO״̬����Ϊ ����
				allInvDetailList.add((InvDetailVO)invDetailVOs[i].clone());
				}else if(invDetailVOs[i].getPk_invdetail()!=null){//�����ϸ������Ϊ��,��ô�ж��Ƿ����޸ļ�¼
					for(int j=0;j<this.oldInvDetailVOs.length;j++){
							if(oldInvDetailVOs[j].getPk_invdetail().equals(invDetailVOs[i].getPk_invdetail())){
								if(oldInvDetailVOs[j].getStatus()==VOStatus.UNCHANGED){
									invDetailVOs[i].setStatus(VOStatus.UNCHANGED);//VO״̬����Ϊ δ�ı�
									allInvDetailList.add((InvDetailVO)invDetailVOs[i].clone());
									detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());//�ۼ��ܼ���
								}else if(oldInvDetailVOs[j].getStatus()==VOStatus.UPDATED){
									invDetailVOs[i].setDr(0);
									invDetailVOs[i].setModifyoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());
									invDetailVOs[i].setModifydate(new UFDate(new Date()));
									invDetailVOs[i].setModifytime(new UFDateTime(new Date()));
									invDetailVOs[i].setStatus(VOStatus.UPDATED);//VO״̬����Ϊ �Ѹı�
									allInvDetailList.add((InvDetailVO)invDetailVOs[i].clone());
									detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());//�ۼ��ܼ���
								}
							}else{
								if(j==oldInvDetailVOs.length){//�����������,��Ȼû���ھ�VO�������ҵ���Ӧ�ļ�¼,��ô��Ȼ���ǹ�������...
									MessageDialog.showHintDlg(this,"��ô����?","�������˰ɸ���?!������ô������???");
								}
							}
						}
					}
				}
			this.setInvdetailWeightSum(detailSumweight);//���ֳ�����������ȫ�ֱ�����
			//�ٱ���һ�ξ�VO����,����ɾ���ľ���ϸVOҲ���뵽������
			for(int j=0;j<this.oldInvDetailVOs.length;j++){
				if(oldInvDetailVOs[j].getStatus()==VOStatus.DELETED){
					oldInvDetailVOs[j].setDr(1);
					oldInvDetailVOs[j].setModifyoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());
					oldInvDetailVOs[j].setModifydate(new UFDate(new Date()));
					oldInvDetailVOs[j].setModifytime(new UFDateTime(new Date()));
					oldInvDetailVOs[j].setStatus(VOStatus.UPDATED);//��ɾ�������޸�DR�ֶεĸ���,��VO���Ϊ �Ѹı�
					allInvDetailList.add((InvDetailVO)oldInvDetailVOs[j].clone());//����ɾ������ϸ��ӽ��б���
				}
			}
			//��ϸ�ܺ�,��������֮��
			UFDouble diffNum=detailSum.sub(this.selectedBody.getNassistnum());
			if(diffNum.doubleValue()>0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
				MessageDialog.showHintDlg(this,"����","�����ϸ֧���ܺͳ������嵽������,\n�������Ϊ:"+this.selectedBody.getNassistnum()+"\n ��ǰ��ϸ����Ϊ:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n����!");
				return;
			}else if(diffNum.doubleValue()<0&&this.getBillListPanel().getHeadBillModel().getRowCount()>0){
				MessageDialog.showHintDlg(this,"����","�����ϸ֧���ܺ�С�ڱ��嵽������,\n�������Ϊ:"+this.selectedBody.getNassistnum()+"\n ��ǰ��ϸ����Ϊ:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n����!");
				return;
			}
			//���ͨ����ϸ�����������������ļ����ô�������е����м�¼ת�������鸳ֵ��invDetailVos���ڷ���
			this.invDetailVOs=new InvDetailVO[allInvDetailList.size()];
			this.invDetailVOs=allInvDetailList.toArray(this.invDetailVOs);
			this.m_closeMark = true;
			this.closeOK();
		}
		// ���Ӱ�ť����
		else if (e.getSource() == this.m_btnAdd) {
			getBillListPanel().getHeadBillModel().addLine();
			int selectedHeadRow=this.getBillListPanel().getHeadTable().getSelectedRow();
			//���ô������,���� PK
			this.getBillListPanel().getHeadBillModel().setValueAt(this.selectedBody.getCbaseid(),this.getBillListPanel().getHeadTable().getRowCount()-1, "pk_invbasdoc");
			this.getBillListPanel().getHeadBillModel().setValueAt(this.selectedBody.getCmangid(),this.getBillListPanel().getHeadTable().getRowCount()-1, "pk_invmandoc");
			this.getBillListPanel().getHeadBillModel().setValueAt(this.selectedBody.getCmangid(),this.getBillListPanel().getHeadTable().getRowCount()-1, "pk_invmandoc");
			this.getBillListPanel().getHeadBillModel().execLoadFormula();
			//setValueAt(this.selectedArriveBody.getCmangid(), this.getBillListPanel().getHeadBillModel().getRowCount()-1, "invname");
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
			//���ô������,�ֳ����,�ֳ�����,���պ��,��������,��������,������,�������ֶ��޸�.
			getBillListPanel().getHeadBillModel().getItemByKey("invname").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("contractthick").setEnabled(false);
			//getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(false);//2011-01-18 ȡ���ֳ�����������
			getBillListPanel().getHeadBillModel().getItemByKey("arrivethick").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("conversionrates").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivewidth").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivelength").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivemeter").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arriveweight").setEnabled(false);
			this.isCalculate=false;//���Ƿ��Ѽ��㰴ť�趨Ϊfalse
//			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
//				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//���ֳ�����ȫ���ÿ�-2011-01-18�Ѳ���Ҫ����ֳ�����,����ȡ���ÿ�
//			}
		}
		// ɾ����ť����
		else if (e.getSource() == this.m_btnDel) {
			int[] delRows = getBillListPanel().getHeadTable().getSelectedRows();
			for(int i=0;i<delRows.length;i++){//ֻ���Ѵ��ڵ��п��Դ���Normal��Modification״̬,�������н���Զ����
				//��ѡ���е�״̬
				int selectedRowState=this.getBillListPanel().getHeadBillModel().getRowState(delRows[i]);
				if(selectedRowState==BillModel.ADD){
					//�������Ϊ������,��ô��������
				}else if(selectedRowState==BillModel.NORMAL||selectedRowState==BillModel.MODIFICATION||selectedRowState==BillModel.DELETE){
					for(int j=0;j<oldInvDetailVOs.length;j++){
						if(oldInvDetailVOs[j].getPk_invdetail().equals(this.getBillListPanel().getHeadBillModel().getValueAt(delRows[i], "pk_invdetail"))){
							//�������Ϊ��ͨ �� �޸�״̬��,��ô������ϸ�����ж�Ӧ��VO״̬����Ϊ��ɾ��
							this.oldInvDetailVOs[j].setStatus(VOStatus.DELETED);
							//2010-12-22 MeiChao ���� :�ж��Ƿ������������˴����ϸ,����,������ɾ������. begin
							String checkPK=this.oldInvDetailVOs[j].getPk_invdetail();
							String checkSQL="select count(t.*) from scm_invdetail_c t where t.pk_invdetail='"+checkPK+"' and t.dr=0";
							IUAPQueryBS queryService=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
							try {
								Object checkResult=queryService.executeQuery(checkSQL,new ColumnProcessor());
								if(checkResult!=null){
									MessageDialog.showErrorDlg(this,"����","�Բ���,��ѡ��ĵ�"+(j+1)+"�м�¼�ѱ�����,������ɾ��.");
									return;
								}
							} catch (BusinessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//2010-12-22 MeiChao end
						}
					}
				}
			}
			getBillListPanel().getHeadBillModel().delLine(delRows);
			this.isCalculate=false;//���Ƿ��Ѽ��㰴ť�趨Ϊfalse
//			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
//				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//���ֳ�����ȫ���ÿ�
//			}
			if(this.getBillListPanel().getHeadBillModel().getRowCount()==0){//��������������ϸ.
				this.isCalculate=true;
				this.isLengthAllNumber=true;
			}
			
		}
		// �޸İ�ť����
		else if (e.getSource() == this.m_btnMod) {
			/**
			 * 2011-01-06 MeiChao ����ɹ����������,�������޸�
			 */
			String selectedOrderpk=selectedBody.getCorderid();
			//����
			
			
			getBillListPanel().getHeadBillModel().setEnabledAllItems(true);
			//���ô������,�ֳ����,�ֳ�����,���պ��,��������,��������,������,�������ֶ��޸�.
			getBillListPanel().getHeadBillModel().getItemByKey("invname").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("contractthick").setEnabled(false);
			//getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivethick").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("conversionrates").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivewidth").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivelength").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arrivemeter").setEnabled(false);
			getBillListPanel().getHeadBillModel().getItemByKey("arriveweight").setEnabled(false);
//			if(!this.isLengthAllNumber){//������Ȳ�Ϊȫ����
//				this.getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(true);//���ֳ���������Ϊ��������
//			}
		}
		//2010-12-24 MeiChao add ���� ��ť��Ӧ
		else if(e.getSource() == this.m_btnCalculate){
			/**
			 * ���ȼ����ϸ�Ƿ���ά�����: ��ϸ֧��=��ѡ�е�������
			 */
			//��ȡ��ϸVO����
			invDetailVOs = (InvDetailVO[])getBillListPanel().getHeadBillModel().getBodyValueVOs(InvDetailVO.class.getName());
			//��ϸ�����ܺ�
			UFDouble detailSum=new UFDouble(0);
			for(int i=0;i<invDetailVOs.length;i++){
				detailSum=detailSum.add(invDetailVOs[i].getArrivenumber());
			}
			//��ϸ�ܺ�,��������֮��
			UFDouble diffNum=detailSum.sub(this.selectedBody.getNassistnum());
			if(diffNum.doubleValue()>0){
				MessageDialog.showHintDlg(this,"����","�����ϸ֧���ܺͳ������嵽������,\n�������Ϊ:"+this.selectedBody.getNassistnum()+"\n ��ǰ��ϸ����Ϊ:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n����!");
				return;
			}else if(diffNum.doubleValue()<0){
				MessageDialog.showHintDlg(this,"����","�����ϸ֧���ܺ�С�ڱ��嵽������,\n�������Ϊ:"+this.selectedBody.getNassistnum()+"\n ��ǰ��ϸ����Ϊ:"+detailSum.setScale(2,UFDouble.ROUND_HALF_UP)+"\n����!");
				return;
			}
			/**
			 * ���ͨ����ϸ�����������������ļ��,��ô��ʼ����ֳ�����
			 */
			//2010-12-27 MeiChao����ǰ����Ҫ���һ�¸ֳ������ֶ��Ƿ������ĸ
			//for(int i=0;i<invDetailVOs.length;i++){
				//if(invDetailVOs[i].getContractlength()!=null&&!this.isDoueric(invDetailVOs[i].getContractlength())){
				if(!this.isLengthAllNumber){
					MessageDialog.showErrorDlg(this,"����", "\"����\"�ֶδ��ڷ���ֵ������,��֧���Զ���̯����,���ֹ�����ֳ�����!");
					return;
				}
			//}
			//��*��*�����ܺ� �� ����*�������ܺ�
			UFDouble invTotal=new UFDouble(0);
			try {
				for(int i=0;i<invDetailVOs.length;i++){
					if(invDetailVOs[0].getContractmeter()!=null){//ֻ�жϵ�һ�м�¼,��ȷ���ǰ�������㻹����������.��Ϊ��ǰ���������ϸ����һ���ļ�����ʽ.
						invTotal=invTotal.add(new UFDouble(invDetailVOs[i].getContractmeter()).multiply(invDetailVOs[i].getArrivenumber()));
					}else{
						invTotal=invTotal.add(new UFDouble(invDetailVOs[i].getContractwidth()).multiply(new UFDouble(invDetailVOs[i].getContractlength())).multiply(invDetailVOs[i].getArrivenumber()));
					}
				}
			} catch (NumberFormatException e1) {
				MessageDialog.showErrorDlg(this,"��ʾ","��������ȷ������!");
				e1.printStackTrace();
				return;
			}
			//�ڶ��α���,�����ܺ���Ϊ����ֵ,���ձ�������ֳ�����,��һ�α�������Ҫtry/catch.
			//��ǰ��ѡ�����ĸֳ������� ,�� ��������
			UFDouble contractWeight=this.selectedBody.getNordernum();
			//�ѷ�������,Ҫ��֤�������������������,�ʽ�Ҫ�����1�н���һ��������-�ѷ������� ��ʣ�����������·���
			UFDouble isCalculatedWeight=new UFDouble(0);
			for(int i=0;i<invDetailVOs.length;i++){
				if(invDetailVOs[0].getContractmeter()!=null){//����ֻ�жϵ�һ�м�¼,��ȷ���ǰ�������㻹����������.��Ϊ��ǰ���������ϸ����һ���ļ�����ʽ.
					this.getBillListPanel().getHeadBillModel()
							.setValueAt(
									((new UFDouble(invDetailVOs[i]
											.getContractmeter())
											.multiply(invDetailVOs[i]
													.getArrivenumber())
											.multiply(contractWeight))
											.div(invTotal)).setScale(2, UFDouble.ROUND_HALF_UP), i,
									"contractweight");
					if(i==invDetailVOs.length-1){
						this.getBillListPanel().getHeadBillModel()
						.setValueAt(
								(contractWeight.sub(isCalculatedWeight)), i,
								"contractweight");
					}else{
						isCalculatedWeight=isCalculatedWeight.add((new UFDouble(invDetailVOs[i].getContractmeter()).multiply(invDetailVOs[i].getArrivenumber()).multiply(contractWeight)).div(invTotal).setScale(2, UFDouble.ROUND_HALF_UP));
					}
				}else{
					this.getBillListPanel().getHeadBillModel()
							.setValueAt(
									(new UFDouble(invDetailVOs[i]
											.getContractwidth()).multiply(
											new UFDouble(invDetailVOs[i]
													.getContractlength()))
											.multiply(
													invDetailVOs[i]
															.getArrivenumber())
											.multiply(contractWeight)
											.div(invTotal)).setScale(2, UFDouble.ROUND_HALF_UP), i,
									"contractweight");
					if(i==invDetailVOs.length-1){
						this.getBillListPanel().getHeadBillModel()
						.setValueAt(
								(contractWeight.sub(isCalculatedWeight)), i,
								"contractweight");
					}else{
						isCalculatedWeight=isCalculatedWeight.add
						(new UFDouble(invDetailVOs[i].getContractwidth()).multiply(
												new UFDouble(invDetailVOs[i]
														.getContractlength()))
												.multiply(
														invDetailVOs[i]
																.getArrivenumber())
												.multiply(contractWeight)
												.div(invTotal).setScale(2, UFDouble.ROUND_HALF_UP));
					}
				}
			}
			this.isCalculate=true;
		}
		//���밴ť��Ӧ����
		else if(e.getSource() == this.m_btnInput){
			// �����ļ�ѡ������
			JFileChooser fc = new JFileChooser();
			// ����Ĭ�ϴ�ѡ��·��, �����ǳ����Ŀ¼
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			// �趨�ļ�ѡ������
			fc.setDialogTitle("��ѡ������ϸExcel�ļ�");
			// ��ʾ�ļ�ѡ�����ѡ��󽫽�����浽returnVal������
			int returnVal = fc.showOpenDialog(null);
			// ����û�ѡ�����ļ����������"Open/��"��ť����ʾ�û�ѡ����ļ�ȫ��·����
			// ����û����"Close/�ر�"��ť���Լ�������ʽ�˳��ļ�ѡ�����ʲôҲ������
			if (returnVal == JFileChooser.CANCEL_OPTION) { // ѡ��ȡ��
				System.out.println("�û�ȡ��ѡ���ļ�");
			}
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				System.out.println(file.getPath());
				if (file.getPath().endsWith(".xls")) {//�Ƿ�excel�ļ�
					List factoryDetailList=new ArrayList();
					//������ѡ�д��,����,���,����,����
					String invbasid=this.selectedBody.getCbaseid();
					String getInvCheckInfo="select graphid,invspec,invtype,invname from bd_invbasdoc where pk_invbasdoc='"+invbasid+"'";
					IUAPQueryBS queryService=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					String invname=null;
					String invspec=null;
					String invtype=null;
					String invplace=null;
					try {
						Object[] invInfo=(Object[])queryService.executeQuery(getInvCheckInfo, new ArrayProcessor());
						invname=invInfo[0].toString();
						invspec=invInfo[1].toString();
						invtype=invInfo[2].toString();
						invplace=invInfo[3].toString();
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					/**
					 * ������ѡ�ļ�
					 */
					int rownumber=0;//����,����try...catch֮��Ĵ���
					checkErrorInfo="";//��ʼ��У��󷵻صĴ�����Ϣ
					try {
						// ����Workbook����, ֻ��Workbook����
						// ֱ�Ӵӱ����ļ�����Workbook
						// ������������Workbook
						InputStream is = new FileInputStream(file.getPath());
						jxl.Workbook rwb = Workbook.getWorkbook(is);
						// һ��������Workbook�����ǾͿ���ͨ����������Excel Sheet(���������)���ο�����Ĵ���Ƭ�Σ�
						int sheetnumber = rwb.getNumberOfSheets();
						for (int i = 0; i < 1; i++) {//ʹi<1 ����ֻѭ��1��
							// ��ȡ��i��Sheet��
							Sheet rs = (Sheet) rwb.getSheet(i);
							// ���Ǽȿ���ͨ��Sheet����������������Ҳ����ͨ���±��������������ͨ���±������ʵĻ���
							// Ҫע���һ�����±��0��ʼ����������һ����
							int columnsnumber = rs.getColumns();// �г�
							int rowsnumber = rs.getRows();// �п�
							rownumber=rowsnumber;
							// һ���õ���Sheet�����ǾͿ���ͨ����������Excel Cell(�����Ԫ��)���ο�����Ĵ���Ƭ�Σ�
							for (int row = 1; row < rowsnumber; row++) {//�±��1��ʼ,���˵���1�б�ͷ
								Map oneRowDetail=new HashMap();
								boolean iswronginv=false;//��ǰ���Ƿ񲻷���Ҫ��Ĵ��
								for (int col = 0; col < columnsnumber; col++) {
									// ��ȡ��row�У���col�е�ֵ
									Cell c = ((jxl.Sheet) rs).getCell(col, row);
									String strc = this.toDBC(c.getContents()).trim();//ʵ��ȫ��ת��ǲ���
									switch(col){
										case 0 :
											if(strc==null||!strc.equals(invname)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("invname", strc);
											break;
										case 1 :
											String[] newstrc=strc.split("\\*");
											if(newstrc[0]==null||!newstrc[0].equals(invspec)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("thick", newstrc[0]);
											//add by ouyangzhb 2011-03-22 �������û��*��ʱ������Ҫ���пո����ֵ��֤
											if(strc.indexOf("*")!=-1){
												if(!this.checkStringSpace(newstrc[1], row,col, checkErrorInfo)){
													iswronginv=true;
													break;
												}
												if(!this.checkString(newstrc[1], row,col, checkErrorInfo)){
													iswronginv=true;
													break;
												}
												oneRowDetail.put("contractwidth", newstrc[1]);
												if(newstrc.length==3){
													if(!this.checkStringSpace(newstrc[2], row,col, checkErrorInfo)){
														iswronginv=true;
														break;
													}
													oneRowDetail.put("contractlength", newstrc[2]);
												}else{
													oneRowDetail.put("contractlength", null);
												}
											}
											break;
										case 2 :
											if(strc==null||!strc.equals(invtype)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("material", strc);
											break;
										case 3 :
											if(strc==null||!strc.equals(invplace)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("invplace", strc);
											break;
										
										case 4 :
											if(!this.checkStringSpace(strc, row,col, checkErrorInfo)){
												iswronginv=true;
												break;
											}
											if(!this.checkString(strc, row,col, checkErrorInfo)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("contractmeter", strc);
											break;
										case 5 :
											if(!this.checkStringSpace(strc, row,col, checkErrorInfo)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("vdef1", strc);
											break;
										case 6 :
											if(!this.checkStringSpace(strc, row,col, checkErrorInfo)){
												iswronginv=true;
												break;
											}
											if(!this.checkString(strc, row,col, checkErrorInfo)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("arrivenumber", strc);
											break;
										case 7 :
											if(!this.checkStringSpace(strc, row,col, checkErrorInfo)){
												iswronginv=true;
												break;
											}
											if(!this.checkString(strc, row,col, checkErrorInfo)){
												iswronginv=true;
												break;
											}
											oneRowDetail.put("contractweight", strc);
											break;
										case 8 :
											;
										case 9 :
											;
										case 10 :
											;
										case 11 :
											;
									}
									if(iswronginv){
										break;
									}
									System.out.print(strc + " ");
								}
								if(!iswronginv){
								factoryDetailList.add(oneRowDetail);
								}
								System.out.println();
							}
						}
						rwb.close();//�ر�������
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					/**
					 * �����ɹ���,���û�ѡ��.,�� ��д��ҳ��,�� ��������
					 */
					int isagree=MessageDialog.showOkCancelDlg(this, "��ʾ","�ɹ���ȡ"+factoryDetailList.size()+"������,������"+(rownumber-factoryDetailList.size()-1)+"��,"+(checkErrorInfo.equals("")?"":("����"+checkErrorInfo))+"�Ƿ�д��(������ԭ������)?");
					if(isagree==MessageDialog.OK_CANCEL_OPTION){
						//�Ƴ�������
						for(int m=0;m<this.getBillListPanel().getHeadBillModel().getRowCount();m++){
							this.getBillListPanel().getHeadBillModel().removeRow(m);
						}
						this.getBillListPanel().updateUI();
						//����������д�����
						for(int m=0;m<factoryDetailList.size();m++){
							ActionEvent ae=new ActionEvent(m_btnAdd,1001,m_btnAdd.getText());
							this.actionPerformed(ae);
							//��ǰ������
							int nowRow=this.getBillListPanel().getHeadBillModel().getRowCount()-1;
							Map oneRowDataMap=(Map)factoryDetailList.get(m);
							Set keys=oneRowDataMap.keySet();
							for(Object key: keys){
								if(key.toString().equals("contractwidth")
										||key.toString().equals("contractlength")
										||key.toString().equals("contractmeter")
										||key.toString().equals("arrivenumber")
										||key.toString().equals("vdef1")
										||key.toString().equals("contractweight")){
								this.getBillListPanel().getHeadBillModel().setValueAt(oneRowDataMap.get(key), nowRow, key.toString());
								BillEditEvent be = new BillEditEvent(getBillListPanel()//�����ͷ�༭�¼�
										.getHeadItem(key.toString()).getComponent(),
										getBillListPanel().getHeadBillModel().getValueAt(nowRow, key.toString()),key.toString(),nowRow);
								this.afterEdit(be);
								if(oneRowDataMap.get(key)!=null&&!"".equals(oneRowDataMap.get(key))){//������ݲ�Ϊ��,��ôִ�и����Ӧ�ı༭��ʽ
									this.getBillListPanel().getHeadBillModel().execEditFormulasByKey(nowRow, key.toString());
								}
								}
//								if(key.toString().equals("contractwidth")){//��
//									
//								}else if(key.toString().equals("contractlength")){//��
//									
//								}else if(key.toString().equals("contractmeter")){//����
//									
//								}else if(key.toString().equals("arrivenumber")){//����
//									
//								}else if(key.toString().equals("vdef1")){//¯����
//									
//								}else if(key.toString().equals("price")){
//									
//								}else if(key.toString().equals("note")){
//									
//								}else if(key.toString().equals("")){
//									
//								}
								
							}
						}
						this.setTitle("������ϸ"+file.getPath());
					}
				} else {
					MessageDialog.showErrorDlg(this,"����","��֧�ֵ��ļ�����!");
				}
				// filePath = file.getPath();
			}
		}
	}

	/**
	 * @function �رձ�־
	 * 
	 * @author MeiChao
	 * 
	 * @return boolean
	 * 
	 * @date 2010-8-20 ����10:24:20
	 */
	public boolean isCloseOK() {
		return m_closeMark;
	}

	/**
	 * @function ��ȡ�����ϸ
	 * 
	 * @author MeiChao
	 * 
	 * @return InvDetailVO[]
	 * 
	 * @date 2010-12-14 ����10:24:46
	 */
	public InvDetailVO[] getinvDetailVOs() {
		return invDetailVOs;
	}
	private String voCheck(InvDetailVO[] invDetailVOs) {

	    for (int i = 0; i < invDetailVOs.length; i++) {
	    	
		}
	    return "";
	}

	public void afterEdit(BillEditEvent e) {
		//�κ��޸ĺ󽫶�Ӧ��״̬�޸ı��Ϊ���޸�
		int nowRowState=this.getBillListPanel().getHeadBillModel().getRowState(e.getRow());
		if(nowRowState==BillModel.ADD){
			//���Ϊ����,��������.
		}else if(nowRowState==BillModel.NORMAL||nowRowState==BillModel.MODIFICATION){//���Ϊ��ͨ״̬,��ô���֮Ϊ�޸�״̬.
			for(int i=0;i<oldInvDetailVOs.length;i++){
				if(oldInvDetailVOs[i].getPk_invdetail().equals(this.getBillListPanel().getHeadBillModel().getValueAt(e.getRow(), "pk_invdetail"))){
					this.getBillListPanel().getHeadBillModel().setRowState(e.getRow(),BillModel.MODIFICATION);
					this.oldInvDetailVOs[i].setStatus(VOStatus.UPDATED);//����Ӧ��VO����Ϊ���޸�״̬.
				}
			}
		}
		if(e.getKey().equals("ismny")){
			if(	(Boolean)e.getValue()){
			   getBillListPanel().getHeadItem("noriginalcurprice").setValue(null);
			   getBillListPanel().getHeadItem("noriginalcurprice").setEdit(false);
			   getBillListPanel().getHeadItem("noriginalcurmny").setEdit(true);
			}else{
				   getBillListPanel().getHeadItem("noriginalcurmny").setValue(null);
				   getBillListPanel().getHeadItem("noriginalcurmny").setEdit(false);
				   getBillListPanel().getHeadItem("noriginalcurprice").setEdit(true);
				}
			this.isCalculate=false;//�κ��޸ĺ�,���Ƿ��Ѽ��㰴ť�趨Ϊfalse
//			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
//				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//���ֳ�����ȫ���ÿ�
//			}
		}
		else if(e.getKey().equals("contractlength")){//2010-12-28 MeiChao
			String curcontractlength=this.getBillListPanel().getHeadBillModel().getValueAt(e.getRow(), "contractlength").toString();	
			if(!this.isDoueric(curcontractlength)){
				if(this.isLengthAllNumber){
					MessageDialog.showHintDlg(this,"��ʾ","�������˷Ǵ��������͵Ĵ������,��������Զ����㽫������,���ֶ�����!");
					this.isLengthAllNumber=false;
					this.getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(true);//���ֳ���������Ϊ��������
				}
			}	
			if(!this.isLengthAllNumber){//���this.isLengthAllNumber���Ϊfalse,�����ж��Ƿ����д�����Ⱦ�Ϊ����.
				for(int i=0;i<this.getBillListPanel().getHeadBillModel().getRowCount();i++){
					curcontractlength=this.getBillListPanel().getHeadBillModel().getValueAt(i, "contractlength").toString();
					if(!this.isDoueric(curcontractlength)){
						break;
					}else if(i==this.getBillListPanel().getHeadBillModel().getRowCount()-1){
						this.isLengthAllNumber=true;
						this.getBillListPanel().getHeadBillModel().getItemByKey("contractweight").setEnabled(false);//���ֳ���������Ϊ����������
						MessageDialog.showHintDlg(this,"��ʾ","�����Զ�����ֳ�����.");
					}
				}
			}
			this.isCalculate=false;//�κ��޸ĺ�,���Ƿ��Ѽ��㰴ť�趨Ϊfalse
//			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
//				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//���ֳ�����ȫ���ÿ�
//			}
		}else if(e.getKey().equals("contractweight")){//�޸��˸ֳ�����
			String curcontractweight=this.getBillListPanel().getHeadBillModel().getValueAt(e.getRow(), "contractweight").toString();
			if(!this.isDoueric(curcontractweight)){
				MessageDialog.showHintDlg(this, "��ʾ", "�ֳ��������������������!");
				this.getBillListPanel().getHeadBillModel().setValueAt(null, e.getRow(),"contractweight");
				return;
			}
			for(int i=0;i<this.getBillListPanel().getHeadBillModel().getRowCount();i++){
				curcontractweight=this.getBillListPanel().getHeadBillModel().getValueAt(i, "contractweight")==null?null:this.getBillListPanel().getHeadBillModel().getValueAt(i, "contractweight").toString();
				if(!this.isDoueric(curcontractweight)){
					break;
				}else if(i==this.getBillListPanel().getHeadBillModel().getRowCount()-1){
					this.isCalculate=true;
				}
			}
		}else{
			this.isCalculate=false;//�κ��������޸ĺ�,���Ƿ��Ѽ��㰴ť�趨Ϊfalse
//			for(int i=0;i<this.getBillListPanel().getHeadTable().getRowCount();i++){
//				this.getBillListPanel().getHeadBillModel().setValueAt(null, i, "contractweight");//���ֳ�����ȫ���ÿ�
//			}
		}
		
	}

	

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * �жϲ�������Ƿ�Ϊȫ����(Double).  
	 * MeiChao
	 */
	public static boolean isDoueric(String str) {
    
	Pattern pattern = Pattern.compile("^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$");
	if(str==null)
		return false;
	else
		return pattern.matcher(str).matches();

	}
	
	/**
	 * �жϴ��/���̱�������Ƿ�Ϊȫ����.  
	 * 
	 */
	public static boolean isNumeric(String str) {
    
	Pattern pattern = Pattern.compile("[0-9]*.*[0-9]*");

	return pattern.matcher(str).matches();

	}
	
	/**  
	  * ȫ��ת���  
	  * @date 2011-01-13
	  * @param input 
	  * @return  
	  */  
	 public static String toDBC(String input) {   
	  char[] c = input.toCharArray();   
	  for (int i = 0; i < c.length; i++) {   
	   if (c[i] == 12288) {   
	    c[i] = (char) 32;   
	    continue;   
	   }   
	   if (c[i] > 65280 && c[i] < 65375)   
	    c[i] = (char) (c[i] - 65248);   
	  }   
	  return new String(c);   
	 }   
	 /**
	  * �ж�����׼ȷ��,�����ж��Ƿ���ڿո�
	  */
	 public boolean checkStringSpace(String checkstring,int row,int col,String returnString) {
		 if(checkstring==null){
			return true; 
		 }
		  char[] c = checkstring.toCharArray();   
		  for (int i = 0; i < c.length; i++) {   
		   if (c[i] == 32) {  
			   this.checkErrorInfo=returnString+"\n��"+row+"�е�"+col+"�д��ڿո�\n";
		    return false;  
		   	} 
		  }
		  return true;   
	 }
	 /**
	  * �ж�����׼ȷ��,�����ж���ȫ����
	  */
	 public boolean checkString(String checkstring,int row,int col,String returnString) {
		 if(checkstring==null){
				return true; 
			 }
		   if (!isNumeric(checkstring)) {  
			   this.checkErrorInfo=returnString+"\n��"+row+"�е�"+col+"�д��ڷ�������\n";
		    return false;  
		  }
		  return true;   
	 }

}
