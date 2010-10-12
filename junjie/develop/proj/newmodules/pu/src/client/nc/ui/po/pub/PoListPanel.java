package nc.ui.po.pub;

////////JAVA
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.ui.po.oper.PoToftPanel;
import nc.ui.pu.jj.JJPuScmPubHelper;
import nc.ui.pu.pub.POPubSetUI;
import nc.ui.pu.pub.POPubSetUI2;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTotalListener;
import nc.ui.pub.bill.IBillModelSortPrepareListener;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.pu.jjvo.InformationCostVO;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.Timer;
import nc.vo.scm.pu.VariableConst;
import nc.vo.scm.pub.SCMEnv;

public class PoListPanel extends BillListPanel
implements	ListSelectionListener,
		IBillModelSortPrepareListener 
		//since v55, ��Ʒ���ƺϼ�(�����۶�������һ��)
		,BillTotalListener
		{	
	//�����ߣ��õ�������ʵ�ֽӿ�nc.ui.po.pub.PoListPanelInterface
	private	Container	m_conInvoker = null ;
	//ģ��ID
	private	String		m_sTemplateId = null ;
	
	//�ɽ����ഫ�ݹ����Ļ��ʾ������ù���ʵ��
    private POPubSetUI2 m_listPoPubSetUI2 = null;
    
    //V31SP1��Ŀ����,�����洢��Ӧ������롢���ƣ���ʶ�Ƿ���Ҫ����Ӧ�̴����ϵ��
    private boolean m_bLoadVendCodeName = false;

    private BillModel m_billmodel = null;

/**
 * ���ߣ���ӡ��
 * ���ܣ�������ע��
 * ������Container	sInvoker		�����ߣ���ʵ�ֽӿ�nc.ui.po.pub.PoListPanelInterface
 *		String	sCorp				��˾
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-3-13 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public PoListPanel(Container	sInvoker,String	sTemplateId){
    
    m_listPoPubSetUI2 = new POPubSetUI2();
	
    setContainer(sInvoker) ;
	setCorp(PoPublicUIClass.getLoginPk_corp()) ;
	setTemplateId(sTemplateId) ;
	setBillType(null) ;

	initi() ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ�������ע��
 * ������Container	sInvoker		�����ߣ���ʵ�ֽӿ�nc.ui.po.pub.PoListPanelInterface
 *		String	sCorp				��˾
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-3-13 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public PoListPanel(Container	sInvoker,String	sCorp,String sBillType,POPubSetUI2 setUi){
    if(setUi != null){
        m_listPoPubSetUI2 = setUi;
    }else{
        m_listPoPubSetUI2 = new POPubSetUI2();
    }
	setContainer(sInvoker) ;
	setCorp(sCorp) ;
	setBillType(sBillType) ;
	setTemplateId(null) ;
	initi() ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ���ʾ�б����
 * ������int iUIPos		�����ϵĶ���λ��
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-3-13 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 * 2005-06-17 V31 ��־ƽ �����Ƿ�Ҫ��*�ű�־
 * 
 * 2006-05-18 V31SP1 ��־ƽ ����Ҫ��洢��Ӧ������롢��Ӧ�������
 */
public void displayCurVO(int	iUIPos, boolean bMarkFirstRow) {
	
	displayCurVO(iUIPos, bMarkFirstRow ,false);
}
/**
 * ����Ҫ��洢��Ӧ������롢��Ӧ�������
 * @param iUIPos
 * @param bMarkFirstRow
 * @author czp
 */
public void displayCurVO(int	iUIPos, boolean bMarkFirstRow, boolean bLoadVendCodeName) {
	Timer time = new Timer();
	time.start();
	time.addExecutePhase("*******************��Ƭ�л��б���ر�ͷ����������������������������������������");
	//�Ƿ���ض�Ӧ��Ӧ�̴�����롢���Ʊ�־
	m_bLoadVendCodeName = bLoadVendCodeName;


	nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
	timeDebug.start();

	//�����������
	getHeadBillModel().clearBodyData();
	getBodyBillModel().clearBodyData();
	timeDebug.addExecutePhase("�����������");/*-=notranslate=-*/
	time.addExecutePhase("*******************�ӻ�����ȡ���ݣ���������������������������������������");
	OrderHeaderVO[]		voaHead = getContainer().getOrderViewHVOs() ;
	if ( voaHead==null ) {
		getContainer().setButtonsStateList() ;
		return;
	}
	//�õ���ͷ����
	timeDebug.addExecutePhase("�õ���ͷ����");/*-=notranslate=-*/
	time.addExecutePhase("*******************��Ƭ�л��б���ر�ͷ���������������ñ�ͷ�����������������������������������������");
	//���ñ�ͷVO
//	setHeaderValueVO(voaHead);
	if(m_conInvoker instanceof PoToftPanel){
	 for (int i = 0; i < ((PoToftPanel)m_conInvoker).getBufferVOManager().getLength(); i++) {
	      // ����
	      getHeadBillModel().addLine();
	      getHeadBillModel().setBodyRowVO(((PoToftPanel)m_conInvoker).getBufferVOManager().getHeadVOAt(i), i);

	    }
	}else{
		setHeaderValueVO(voaHead);
	}
	timeDebug.addExecutePhase("���ñ�ͷVO");/*-=notranslate=-*/
	time.addExecutePhase("*******************��Ƭ�л��б���ر�ͷ��������������ִ�й�ʽ����������������������������������������");
	//��ͷ��	getHeadBillModel().execLoadFormula();
	getHeadBillModel().execLoadFormula();
	
	timeDebug.addExecutePhase("��ͷ��ʽ");/*-=notranslate=-*/

	//װ�ر���
	if (getHeadTable().getRowCount()!=0) {
	    if(bMarkFirstRow){
	        getHeadBillModel().setRowState(iUIPos, BillModel.SELECTED);
	    }
		getHeadTable().setRowSelectionInterval(iUIPos,iUIPos) ;
	}
//	timeDebug.showAllExecutePhase("�ɹ������б���ر�ͷ");/*-=notranslate=-*/
	time.showAllExecutePhase("*******************��Ƭ�л��б���ر�ͷ����������������������������������������");
}

/**
 * ���ߣ���ӡ��
 * ���ܣ����б����ʵ����ʾָ��λ�õĲɹ�����
 * ������int pos		����λ�ã�Ϊ����λ��
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-6-5 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public void displayCurVOBody(int		iUIPos) {
	nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
	timeDebug.start();

	try {
		//ȡ�ñ���
		OrderVO	voViewCur = getContainer().getOrderViewVOAt( getVOPos(iUIPos) ) ;
		if(voViewCur.getHeadVO().getCvendormangid()==null){
			for(OrderItemVO voBody : voViewCur.getBodyVO()){
				voBody.setCcurrencytypeid(null);
			}
		}
		String	sCurPk_corp = voViewCur.getHeadVO().getPk_corp() ;
		timeDebug.addExecutePhase("ȡ�ñ���");/*-=notranslate=-*/

		OrderItemVO[]	voaItem = voViewCur.getBodyVO() ;
		if (voaItem==null || voaItem.length==0) {
			getBodyBillModel().clearBodyData() ;
			return ;
		}
		if(m_conInvoker instanceof PoToftPanel){
			((PoToftPanel)m_conInvoker).getInvokeEventProxy().beforeSetBillVOsToListBody(voaItem);
		}
		//���þ���
		setBodyDigits_CorpRelated(sCurPk_corp) ;
		timeDebug.addExecutePhase("���þ���");/*-=notranslate=-*/
		//�ȹرպϼƿ���
		boolean needTotal=getBillModel().isNeedCalculate();
		getBillModel().setNeedCalculate(false);
		//����VO
		setBodyValueVO(voaItem);
		timeDebug.addExecutePhase("����VO");/*-=notranslate=-*/

		//�������
		PoCardPanel.resetBodyValueRelated_Curr(sCurPk_corp,voViewCur.getHeadVO().getCcurrencytypeid(),getBodyBillModel(),new BusinessCurrencyRateUtil(getCorp()),getBodyBillModel().getRowCount(),m_listPoPubSetUI2);
		timeDebug.addExecutePhase("���������ּ�ֵ");/*-=notranslate=-*/

		//���幫ʽ
		getBodyBillModel().execLoadFormula();
		timeDebug.addExecutePhase("���幫ʽ");/*-=notranslate=-*/

		//����������
		PoPublicUIClass.setFreeColValue(getBodyBillModel(),"vfree");
		timeDebug.addExecutePhase("����������");/*-=notranslate=-*/

		//���û�����
		PuTool.setBillModelConvertRateAndAssNum(getBodyBillModel(),new	String[]{"cbaseid","cassistunit","nordernum","nassistnum","nconvertrate"},4, -1) ;
		timeDebug.addExecutePhase("���û�����");/*-=notranslate=-*/

		//������Դ�������͡���Դ���ݺ�
		PuTool.loadSourceInfoAll(this,BillTypeConst.PO_ORDER);
		timeDebug.addExecutePhase("������Դ�������͡���Դ���ݺ�");/*-=notranslate=-*/

		//����޼�
		PoPublicUIClass.loadListMaxPrice(this,iUIPos,m_listPoPubSetUI2) ;
		timeDebug.addExecutePhase("����޼�");/*-=notranslate=-*/

		//�����ڵ㣺�����۳�������޼�ʱ����������ɫ
		if (BillTypeConst.PO_AUDIT.equals(getBillType())) {
			setColor() ;
		}
	    //��ʾ��Ӧ�̵Ĵ�����뼰����
		if(m_bLoadVendCodeName){	
	        String strVendorId = voViewCur.getHeadVO().getCvendormangid();  
	        String[] saMangId = new String[voViewCur.getBodyVO().length];
	        for (int i = 0; i < saMangId.length; i++) {
	            saMangId[i] = voViewCur.getBodyVO()[i].getCmangid();
	        }
	        PuTool.loadVendorInvInfos(strVendorId,saMangId,getBodyBillModel(),0,saMangId.length -1);
		}
	  //�ٴ򿪺ϼƿ���
	  getBillModel().setNeedCalculate(needTotal);
	  //add by QuSida 2010-9-11 (��ɽ����) --- begin
	  //function����ѯ����Ӧ�ķ�����Ϣ
	  String pk = voViewCur.getHeadVO().getCorderid();
		String sql = "cbillid = '"+pk+"' and dr = 0 ";
		InformationCostVO[] vos = null;	
		 vos = (InformationCostVO[])JJPuScmPubHelper.querySmartVOs(InformationCostVO.class, null, sql);
	if(vos!=null&&vos.length!=0){
		 getBillListData().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(vos);
		 getBillListData().getBodyBillModel("jj_scm_informationcost").execLoadFormula();
	}else{
		//20101010-22-20  MeiChao ����Ϊ��ʱ,�����ʷ������Ϣ.
		getBillListData().getBodyBillModel("jj_scm_informationcost").setBodyDataVO(null);
	}
	
	 //add by QuSida 2010-9-11 (��ɽ����) --- end
	} catch (Exception ex) {
		PuTool.outException(this,ex) ;
	}

	timeDebug.showAllExecutePhase("�ɹ������б���ر���");/*-=notranslate=-*/

}

/**
 * ���ߣ���ӡ��
 * ���ܣ��б��������ѯ
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-05-22 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private IPoListPanel getContainer() {
	return	(IPoListPanel)m_conInvoker;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��б��������ѯ
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-05-22 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public int getHeadRowCount() {

	return getHeadBillModel().getRowCount() ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��õ��б�����±�ͷѡ�е���
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-4-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2003-03-10	wyf		�޸�ȡѡ�е��еķ���
 */
public	int	getHeadRowState(int	iRow){
	return	getHeadBillModel().getRowState(iRow) ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��õ��б�����±�ͷѡ�е���
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-4-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2003-03-10	wyf		�޸�ȡѡ�е��еķ���
 */
public	int	getHeadSelectedCount(){
	if (getHeadBillModel().getRowCount()==0) {
		return	0 ;
	}

	int[] iaSelectedRow = getHeadTable().getSelectedRows();
	if (iaSelectedRow==null ){
		return	0 ;
	}else{
		return	 iaSelectedRow.length ;
	}
}

/**
 * ���ߣ���ӡ��
 * ���ܣ�
 * ��������
 * ���أ������ϵ�ѡ��λ��
 * ���⣺��
 * ���ڣ�(2001-05-22 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public int getHeadSelectedRow() {

	return getHeadTable().getSelectedRow();
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��õ��б�����±�ͷѡ�е���
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-4-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2003-03-10	wyf		�޸�ȡѡ�е��еķ���
 */
public	int[]	getHeadSelectedRows(){
	return	getHeadTable().getSelectedRows() ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��õ��б�����±�ͷѡ�е�������Ӧ������VO��λ�ã����Ѱ�VO�е�λ�ô�С����
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-11-05 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public	Integer[]	getHeadSelectedVOPoses(){

	//�����ϵĶ���
	HashMap	htmapIndex = new HashMap();

	int[]		iaSelectedRowCount = getHeadSelectedRows() ;
	int			iLen = iaSelectedRowCount.length ;
	for (int i = 0; i < iLen; i++) {
		int 		iVOPos = getVOPos(iaSelectedRowCount[i]);
		htmapIndex.put(new	Integer(iVOPos),"") ;
	}

	TreeMap	trmapIndex = new	TreeMap(htmapIndex) ;
	Integer[]	iaIndex = (Integer[])trmapIndex.keySet().toArray(new	Integer[iLen]) ;

	return	iaIndex ;
}

/**
 * ���ߣ�WYF
 * ���ܣ��ӿ�IBillModelSortPrepareListener ��ʵ�ַ���
 * ������String sItemKey		ITEMKEY
 * ���أ���
 * ���⣺��
 * ���ڣ�(2004-03-24  11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public int getSortTypeByBillItemKey(String sItemKey){

	if ("crowno".equals(sItemKey)
		|| "csourcebillrowno".equals(sItemKey)
		|| "cancestorbillrowno".equals(sItemKey)) {
		return BillItem.DECIMAL;
	}
	return	getBodyBillModel().getItemByKey(sItemKey).getDataType() ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��õ�ģ��ID
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-9-24 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private	String	getTemplateId(){
	return	m_sTemplateId ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ����ݸ��������� ����VO�������±�
 * ������int		iUIRow		������
 * ���أ�int				������Ӧ��VO�±꣬�༴����ǰ���±�
 * ���⣺��
 * ���ڣ�(2001-05-22 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public int getVOPos(int		iUIRow) {

	return PuTool.getIndexBeforeSort(this,iUIRow) ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ���ʼ��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-8-26 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2002-08-26	wyf	����ǧ��λ������
 * 2002-09-20	wyf	�޸��Զ�����ĵ���
 */
private void initi() { //throws Exception{

	nc.vo.scm.pu.Timer timeDebug = new nc.vo.scm.pu.Timer();
	timeDebug.start();

	// ����ģ��
	try {
	  if (m_conInvoker instanceof PoToftPanel) {
	    setListData(new BillListData(((PoToftPanel) m_conInvoker).getBillTempletVo()));
	  }else	if (getTemplateId() == null) {
			loadTemplet(getBillType(), null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), getCorp());
		} else {
			loadTemplet(getTemplateId());
		}
	} catch (Exception e) {
		SCMEnv.out(e);
		MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000270")/*@res "��ʾ"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("40040200","UPP40040200-000047")/*@res "��ǰ������û�п��õ�ģ��"*/);
		return;
	}
	timeDebug.addExecutePhase("����ģ��");/*-=notranslate=-*/

	//������
	initiHideItems();
	timeDebug.addExecutePhase("������");/*-=notranslate=-*/
	//������
	initiComboBox();
	timeDebug.addExecutePhase("������");/*-=notranslate=-*/

	int iDigit = POPubSetUI.getCCurrFinanceDecimal(getCorp());

	//�Զ�����
	nc.ui.scm.pub.def.DefSetTool.updateBillListPanelUserDef(this,
	//��ǰģ��
	getCorp(), //��˾����
  ScmConst.PO_Order, //��������
	"vdef", //����ģ���е���ͷ���Զ�����ǰ׺
	"vdef" //����ģ���е�������Զ�����ǰ׺
	);
	setListData(getBillListData());
	//������FALSE
	setEnabled(false);
	timeDebug.addExecutePhase("�Զ�����");/*-=notranslate=-*/
	//���ñ�ͷ����
	setHeadDigits(iDigit);
	timeDebug.addExecutePhase("���ñ�ͷ����");/*-=notranslate=-*/
	//���ʻ�
	PuTool.setTranslateRender(this);
	//ǧ��λ
	getParentListPanel().setShowThMark(true);
	getChildListPanel().setShowThMark(true);
	//���ʻ�
	initiListener(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	//����ϼ���
	//	getChildListPanel().setTatolRowShow(true);
	getChildListPanel().setTotalRowShow(true);
	timeDebug.addExecutePhase("����");/*-=notranslate=-*/
	
	//V55��֧������ѡ��
	PuTool.setLineSelectedList(this);
	
	updateUI();
	//
	timeDebug.showAllExecutePhase("�б�������");/*-=notranslate=-*/
	return;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ����ñ����е�ComboBox
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-8-26 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initiComboBox() {
	////��ͷ��ComboBox��������ʾ
	//BillItem[] hitems = getBillListData().getHeadItems();
	//for (int i = 0; i < hitems.length; i++) {
		//if (hitems[i].getDataType() == BillItem.COMBO) {
			//hitems[i].setWithIndex(true);
		//}
	//}
	//������ComboBox��������ʾ
	//BillItem[] bitems = getBillListData().getBodyItems();
	//for (int i = 0; i < bitems.length; i++) {
		//if (bitems[i].getDataType() == BillItem.COMBO) {
			//bitems[i].setWithIndex(true);
		//}
	//}

	getBodyItem("idiscounttaxtype").setWithIndex(true) ;
	//�б����
	UIComboBox cbbBType =	(UIComboBox) (getBodyItem("idiscounttaxtype").getComponent());
	cbbBType.setFont(this.getFont());
	cbbBType.setBackground(this.getBackground());

	cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_INNER);
	cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_OUTTER);
	cbbBType.addItem(VariableConst.IDISCOUNTTAXTYPE_NAME_NOCOUNT);
	cbbBType.setSelectedIndex(1);

	cbbBType.setTranslate(true) ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ���ʼ�����ص��ֶΣ���Щ�ֶ������أ���LISTSHOWFLAG������
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-8-26 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void initiHideItems() {
	hideHeadTableCol("pk_corp");
	hideHeadTableCol("cbiztype");
	hideHeadTableCol("cpurorganization");
	//V5 Del:
	//hideHeadTableCol("cstoreorganization");
	hideHeadTableCol("cvendormangid");
	hideHeadTableCol("cfreecustid");

	hideHeadTableCol("caccountbankid");
//	hideHeadTableCol("cdeliveraddress");
	hideHeadTableCol("cemployeeid");
	hideHeadTableCol("cdeptid");
	hideHeadTableCol("creciever");
	hideHeadTableCol("cgiveinvoicevendor");

	hideHeadTableCol("ctransmodeid");
	hideHeadTableCol("ctermprotocolid");

	return ;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ������б��ͷ�������������д�÷���
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-4-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected void initiListener(int	iSelectionModel) {
	//��ѡ����
	getHeadTable().setCellSelectionEnabled(false);
	//getHeadTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	getHeadTable().setSelectionMode(iSelectionModel);
	getHeadTable().getSelectionModel().addListSelectionListener(this) ;
	//�к��������
	getBodyBillModel().setSortPrepareListener(this) ;
	//since v55
	getBodyBillModel().addTotalListener(this);

}

/**
 * �����б������ȫ��ȡ��
 * @param
 * @return
 * @exception
 * @see
 * @since		2001-04-28
*/
public void onActionDeselectAll() {

	int		iLen = getHeadBillModel().getRowCount();
	//��Ϊȫ��ȡ��ѡ��
	getHeadTable().removeRowSelectionInterval(0, iLen-1);
}

/**
 * ���ߣ���ӡ��
 * ���ܣ������б������ȫ��ѡ��
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-4-18 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 2002-06-05	��ӡ��	�����ȫѡ��ȫ����ť�Ŀ���
 */
public void onActionSelectAll() {

	//��Ϊȫ��ѡ��
	getHeadTable().setRowSelectionInterval(0, getHeadBillModel().getRowCount()-1);
}

/**
 * ���ߣ���ӡ��
 * ���ܣ����þ���
 * ������ActionEvent e		�¼�
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-4-22 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void setBodyDigits_CorpRelated(String pk_corp){

	PoCardPanel.setBodyDigits_CorpRelated(pk_corp,getBodyBillModel()) ;

	//Ϊ����->��������ⵥʹ��
	int[] iaDigit = PoPublicUIClass.getShowDigits( pk_corp ) ;
	if (getBodyItem("nnotarrvnum")!=null) {
		getBodyItem("nnotarrvnum").setDecimalDigits(iaDigit[0]);
	}
	if (getBodyItem("nnotstorenum")!=null) {
		getBodyItem("nnotstorenum").setDecimalDigits(iaDigit[0]);
	}

	//ת��ʱ�ĵ���
	if (getBodyItem("nprice")!=null) {
		getBodyItem("nprice").setDecimalDigits(iaDigit[2]);
	}
	if (getBodyItem("ntaxprice")!=null) {
		getBodyItem("ntaxprice").setDecimalDigits(iaDigit[2]);
	}
}

/**
* ���ߣ���ӡ��
* ���ܣ���������(����)������ߵ���ʱ����������ɫ
* ��������
* ���أ���
* ���⣺��
* ���ڣ�(2004-5-17 11:39:21)
* �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
*/
private void setColor() {
	if (getBodyItem("nmoney") == null
		|| getBodyItem("nordernum") == null
		|| getBodyItem("nmaxprice") == null) {
		return;
	}

	//�����۳�������޼۽��б�ʶ
	ArrayList listRow = new ArrayList();

	int iBodyCount = getBodyBillModel().getRowCount();

	//���ҽ�����������޼ۡ�����
	UFDouble dMoney = null;
	UFDouble dNum = null;
	UFDouble dMaxprice = null;
	UFDouble dPrice = null;
	for (int i = 0; i < iBodyCount; i++) {

	    dMaxprice = PuPubVO.getUFDouble_ValueAsValue( getBodyBillModel().getValueAt(i, "nmaxprice") );
	    if (dMaxprice==null) {
	    	continue ;
	    }
	    dMoney = PuPubVO.getUFDouble_ValueAsValue( getBodyBillModel().getValueAt(i, "nmoney"));
	    dNum = PuPubVO.getUFDouble_ValueAsValue( getBodyBillModel().getValueAt(i, "nordernum"));
	    if (dMoney==null || dNum==null) {
	    	continue ;
	    }
	    dPrice = dMoney.div(dNum) ;

	    if ( dPrice.compareTo(dMaxprice)>0 ) {
	    	listRow.add(new Integer(i));
	    }
	}

	if (listRow.size() > 0) {
		int k[] = new int[listRow.size()];
		for (int i = 0; i < listRow.size(); i++)
			k[i] = ((Integer) listRow.get(i)).intValue();
		nc.ui.scm.pub.BillTools.changeRowNOColor(getBodyTable(), k);
	} else {
		nc.ui.scm.pub.BillTools.changeRowNOColor(getBodyTable(), null);
	}
}

/**
 * ���ߣ���ӡ��
 * ���ܣ����õ�ǰ������
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2001-05-22 13:24:16)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void setContainer(Container	newCon) {
	m_conInvoker = newCon;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ����ñ�ͷ����
 * ������String pk_corp	��˾ID
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-9-4 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private void setHeadDigits(int iDigit){
	//Ԥ�����޶Ԥ����
	//int		iDigit =2 ;
	//try {
		//iDigit = POPubSetUI.getMoneyDigitByCurr_Finance(
			//POPubSetUI.getCurrArith_Finance(pk_corp).getLocalCurrPK()
			//) ;
	//} catch (Exception	e) {
		//PuTool.reportException(this,e);
	//}

	//���Ҳ��񾫶Ƚ����
	int		iLen = OrderHeaderVO.getDbMnyFields_Local_Finance().length ;
	for (int i = 0; i < iLen; i++){
		BillItem	item = getHeadItem(OrderHeaderVO.getDbMnyFields_Local_Finance()[i]) ;
		if (item!=null) {
			item.setDecimalDigits(iDigit);
		}
	}

	//�汾
	BillItem	item = getHeadItem("nversion");
	if(item != null){
		item.setDecimalDigits(1) ;
	}
}

/**
 * ���ߣ���ӡ��
 * ���ܣ��õ�ģ��ID
 * ��������
 * ���أ���
 * ���⣺��
 * ���ڣ�(2003-9-24 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
private	void	setTemplateId(String	sValue){
	m_sTemplateId = sValue;
}

/**
 * ���ߣ���ӡ��
 * ���ܣ�ʵ��ListSelectionListener�ļ�������
 * ������ListSelectionEvent e	�����¼�
 * ���أ���
 * ���⣺��
 * ���ڣ�(2002-6-27 11:39:21)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public void valueChanged(ListSelectionEvent e) {
	getBodyTabbedPane().setSelectedIndex(0);
	Timer time = new Timer();
	time.start();
	time.addExecutePhase("*******************��Ƭ�л��б���ر���ǰ������������������������������������������������������");
	if (e.getValueIsAdjusting()==true) {
		return ;
	}

	int iCount = getHeadTable().getRowCount();
	for (int i = 0; i < iCount; i++) {
		getHeadBillModel().setRowState(i, BillModel.NORMAL);
	}

	//�õ���ѡ�е���
	int[] iaSelectedRow = getHeadTable().getSelectedRows();
	if ( iaSelectedRow!=null ){
		iCount = iaSelectedRow.length ;
		//ѡ�е��б�ʾΪ�򣪺�
		for (int i = 0; i < iCount; i++) {
			getHeadBillModel().setRowState(iaSelectedRow[i], BillModel.SELECTED);
		}
	}
	time.addExecutePhase("*******************��Ƭ�л��б�ʼ���ر��売��������������������������������������������������");
	if ( getHeadSelectedCount()!=1 ) {
		setBodyValueVO(null) ;
	}else{
		//getContainer().setVOPos( getVOPos(getHeadSelectedRow()) ) ;
		displayCurVOBody( getHeadSelectedRow() );
	}

	//��ť�仯
	getContainer().setButtonsStateList() ;
	time.addExecutePhase("*******************��Ƭ�л��б���ر������������������������������������������������������������������");
	time.showAllExecutePhase("���ˣ�������������������������������������������");
	time.showExecuteTime("���ر���ʱ��");
}

/**
 * ��ȡ���ʾ������ù���
 * */
public POPubSetUI2 getPoPubSetUi2(){
    if(m_listPoPubSetUI2 == null){
        m_listPoPubSetUI2 = new POPubSetUI2();
    }
    return m_listPoPubSetUI2;
}
//since v55, ��Ʒ���ƺϼ�(�����۶�������һ��)
public UFDouble calcurateTotal(String key) {
	UFDouble total = new UFDouble(0.0);
	UFBoolean blargessflag = null;
	int iLen = getBillModel().getRowCount();
	for (int i = 0; i < iLen; i++) {
		blargessflag = PuPubVO.getUFBoolean_NullAs(getBillModel().getValueAt(i, "blargess"), UFBoolean.FALSE);
		if (OrderItemVO.isPriceOrMny(key) && blargessflag.booleanValue()){
			continue;
		}
		total = total.add(PuPubVO.getUFDouble_NullAsZero(getBillModel().getValueAt(i, key)));
	}
//	getBillModel().setNeedCalculate(doCalculate)
	return total;
}
public BillModel getBillModel(){
  if(m_billmodel == null){
    m_billmodel = getBodyBillModel();
  }
  return m_billmodel;
}
}