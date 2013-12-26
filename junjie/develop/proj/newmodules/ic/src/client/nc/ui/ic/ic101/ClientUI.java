package nc.ui.ic.ic101;


import nc.ui.pub.FramePanel;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.ValidationException;
import nc.vo.scm.constant.ic.InOutFlag;
/**
 * ��Ĺ��ܡ���;���ִ�BUG���Լ��������˿��ܸ���Ȥ�Ľ��ܡ�
 * ���ߣ����Ӣ
 * @version	����޸�����(2001-11-30 8:57:51)
 * @see		��Ҫ�μ���������
 * @since		�Ӳ�Ʒ����һ���汾�����౻��ӽ���������ѡ��
 * �޸��� + �޸�����
 * �޸�˵��
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {

/**
 * ClientUI ������ע�⡣
 */
public ClientUI() {
	super();
	initialize();
}

/**
 * ClientUI ������ע�⡣
 * add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
 */
public ClientUI(FramePanel fp) {
 super(fp);
 initialize();
}
/**
 * ClientUI ������ע�⡣
 * nc 2.2 �ṩ�ĵ������鹦�ܹ����ӡ�
 *
 */
public ClientUI(
	String pk_corp,
	String billType,
	String businessType,
	String operator,
	String billID) {
	super(pk_corp, billType, businessType, operator, billID);

}
/**
 * �����ߣ����˾�
 * ���ܣ����ݱ༭����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
@Override
protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
	//����ҵ������
	if (e.getKey().equals(getEnvironment().getNumItemKey()) || e.getKey().equals(getEnvironment().getAssistNumItemKey())) {
		if ((getBillCardPanel().getBodyValueAt(e.getRow(), "dbizdate") == null
			|| getBillCardPanel()
				.getBodyValueAt(e.getRow(), "dbizdate")
				.toString()
				.trim()
				.length()
				== 0)
			&& m_sStartDate != null) {
			nc.vo.pub.lang.UFDate dstart = new nc.vo.pub.lang.UFDate(m_sStartDate);
			nc.vo.pub.lang.UFDate dbiz = dstart.getDateBefore(1);
			getBillCardPanel().setBodyValueAt(dbiz.toString(), e.getRow(), "dbizdate");
		}
	}
}
/**
 * �����ߣ����˾�
 * ���ܣ���������ѡ��ı�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
@Override
protected void afterBillItemSelChg(int iRow, int iCol) {
		////��λ����
	//String sItemKey=getBillCardPanel().getBillModel().getBodyKeyByCol(iCol);

	//if (sItemKey.equals("vspacename")) {
		//filterSpace(iRow);
	//}


}
/**
 * �����ߣ����˾�
 * ���ܣ����ݱ���༭�¼�ǰ��������
 * ������e���ݱ༭�¼�
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-8 19:08:05)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
@Override
public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
	return false;
}
/**
 * �����ߣ����˾�
 * ���ܣ���������ѡ��ı�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
@Override
protected void beforeBillItemSelChg(int iRow, int iCol) {}
/**
  * �����ߣ����˾�
  * ���ܣ����󷽷�������ǰ��VO���
  * �����������浥��
  * ���أ�
  * ���⣺
  * ���ڣ�(2001-5-24 ���� 5:17)
  * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
  */
protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
	if (checkVO()) {

		try {

			VOCheck.checkStartDate(getM_voBill(), "dbizdate", nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC000-0000380")/*@res "�������"*/, m_sStartDate);
		} catch (NullFieldException e) {
			showErrorMessage(e.getHint());
			return false;
		} catch (ValidationException e) {
			nc.vo.scm.pub.SCMEnv.out("У���쳣������δ֪����...");
			showErrorMessage(e.getMessage());
			handleException(e);
			return false;
		}

		return true;
	} else
		return false;
}
/**
 * ����ʵ�ָ÷���������ҵ�����ı��⡣
 * @version (00-6-6 13:33:25)
 *
 * @return java.lang.String
 */
public String getTitle() {
	return  super.getTitle();
}
/**
 * �����ߣ����˾�
 * ���ܣ���ʼ��ϵͳ����
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void initPanel() {
	//��Ҫ���ݲ���
	super.setNeedBillRef(false);
	//�����˵�����
	////--- 1  �ڡ�����ά�����˵��� ���ӡ���ȳ��⡱ ----
	super.m_bIsInitBill=true;
}

public String getBillType() {
	return nc.vo.ic.pub.BillTypeConst.m_initIn;
}

public String getFunctionNode() {
	return "40080402";
}

public int getInOutFlag() {
	return InOutFlag.IN;
}

/**
 * �����ߣ����˾�
 * ���ܣ����б�ʽ��ѡ��һ�ŵ���
 * ������ ������alListData�е�����
 * ���أ���
 * ���⣺
 * ���ڣ�(2001-11-23 18:11:18)
 *  �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
protected void selectBillOnListPanel(int iBillIndex) {
	//dispSpace(iBillIndex);
}
/**
 * �����ߣ����˾�
 * ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2001-5-9 9:23:32)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 *
 *
 *
 */
protected void setButtonsStatus(int iBillMode) {}
}