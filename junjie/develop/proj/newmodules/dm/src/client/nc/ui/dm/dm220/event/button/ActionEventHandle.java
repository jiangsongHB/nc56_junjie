package nc.ui.dm.dm220.event.button;

import java.awt.Color;
import java.awt.Container;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.bs.ml.NCLangResOnserver;
import nc.itf.arap.pub.IArapBillPublic;
import nc.itf.ia.bill.IBill;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.dm.dm010.ShowDelivOrg;
import nc.ui.dm.dm220.ClientHelper;
import nc.ui.dm.dm220.DelivBillVirtualFiledLoader;
import nc.ui.dm.dm220.NodeContext;
import nc.ui.dm.dm220.event.CollectDlg;
import nc.ui.dm.dm220.print.PrintDataSource;
import nc.ui.dm.dm220.print.SplitPrintDataSource;
import nc.ui.dm.dm220.print.SplitPrintParametersHelper;
import nc.ui.dm.dm220.print.SplitPrintTool;
import nc.ui.dm.pub.ValidateFormulaExecuter;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.pattern.buffer.IBillBuffer;
import nc.ui.scm.pattern.context.IUIContext;
import nc.ui.scm.pattern.ctrl.card.CardCtrl;
import nc.ui.scm.pattern.ctrl.card.CardStatus;
import nc.ui.scm.pattern.ctrl.card.auxiliary.CardHelper;
import nc.ui.scm.pattern.ctrl.list.ListCtrl;
import nc.ui.scm.pattern.ctrl.node.FunctionStatus;
import nc.ui.scm.pattern.ctrl.node.NodeStatus;
import nc.ui.scm.pattern.event.node.ActionEvent;
import nc.ui.scm.pattern.event.node.SwitchEvent;
import nc.ui.scm.pattern.listcard.ListCardNode;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.ISaveSplitParams;
import nc.ui.scm.print.SplitParams;
import nc.ui.scm.print.SplitPrintParamDlg;
import nc.ui.scm.pub.panel.SetColor;
import nc.ui.scm.pub.report.OrientDialog;
import nc.ui.scm.sourcebill.SourceBillFlowDlg;
import nc.vo.dm.model.delivbill.entity.DelivBillHeadVO;
import nc.vo.dm.model.delivbill.entity.DelivBillItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillPackItemVO;
import nc.vo.dm.model.delivbill.entity.DelivBillVO;
import nc.vo.dm.model.delivbill.meta.DelivBillHeadVOMeta;
import nc.vo.dm.model.delivbill.meta.DelivBillItemVOMeta;
import nc.vo.dm.model.delivbill.meta.DelivBillPackItemVOMeta;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.ia.bill.BillHeaderVO;
import nc.vo.ia.bill.BillItemVO;
import nc.vo.ia.bill.BillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.pattern.constant.pub.ValueConstant;
import nc.vo.scm.pattern.context.IParameter;
import nc.vo.scm.pattern.domain.scm.enumeration.FBillStatusFlag;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.scm.pattern.exception.SCMBusinessException;
import nc.vo.scm.pattern.model.entity.bill.ISmartBill;
import nc.vo.scm.pattern.model.entity.vo.SmartVO;
import nc.vo.scm.pattern.pub.SqlBuilder;
import nc.vo.scm.print.PrintConst;
import nc.vo.scm.print.ScmPrintlogVO;
import nc.vo.scm.pub.ctrl.GenMsgCtrl;
import nc.vo.scm.pub.session.ClientLink;

/**
 * @author ���� 2008-8-18 ����01:58:53
 */
public class ActionEventHandle {
  private PrintEntry m_print = null;

  private NewQuery m_QueryDLG = null;

  private PrintDataSource m_dataSource = null;

  private ListCardNode node = null;

  private NodeContext nodeContext = null;

  private SelfActionEventHandle handle = null;
  
  private SplitPrintParamDlg splitPrintDlg = null;
  public ActionEventHandle(
      ListCardNode node, NodeContext nodeContext) {
    this.node = node;
    this.nodeContext = nodeContext;
    this.handle = new SelfActionEventHandle(this.node, this.nodeContext);
  }

  public void doEvent(ActionEvent event) {
    String code = event.getButtonObject().getCode();

    if (code.equals("����")) {
      this.onNew();
    }
    else if (code.equals("����")) {
      this.onSave();
    }
    else if (code.equals("�޸�")) {
      this.onModify();
    }
    else if (code.equals("ȡ��")) {
      this.onCancel();
    }
    else if (code.equals("ɾ��")) {
      this.onDelete();
    }
    else if (code.equals("����ת��")) {
      this.onCancleTransferBill();
    }
    else if (code.equals("����")) {
      this.onAddLine();
    }
    else if (code.equals("ɾ��")) {
      this.onDeleteLine();
    }
    else if (code.equals("������")) {
      this.onInsertLine();
    }
    else if (code.equals("������")) {
      this.onCopyLine();
    }
    else if (code.equals("ճ����")) {
      this.onPasteLine();
    }
    else if (code.equals("ճ���е���β")) {
      this.onPasteLineToTail();
    }
    else if (code.equals("��Ƭ�༭")) {
      this.onCardRowEdit();
    }
    else if (code.equals("�����к�")) {
      this.resetRowNo();
    }
    else if (code.equals("��ѯ")) {
      this.onQuery();
    }
    else if (code.equals("ˢ��")) {
      this.onRefresh();
    }
    else if (code.equals("��λ")) {
      this.onLocate();
    }
    else if (code.equals("��ҳ")) {
      this.onFirst();
    }
    else if (code.equals("��ҳ")) {
      this.onPrevious();
    }
    else if (code.equals("��ҳ")) {
      this.onNext();
    }
    else if (code.equals("ĩҳ")) {
      this.onLast();
    }
    else if (code.equals("ȫѡ")) {
      this.onSelectAll();
    }
    else if (code.equals("ȫ��")) {
      this.onUnSelectAll();
    }
    else if (code.equals("�ϲ���ʾ")) {
      this.onCombinView();
    }
    else if (code.equals("Ԥ��")) {
      this.onPrintPreview();
    }
    else if (code.equals("��ӡ")) {
      this.onPrint();
    }
    else if (code.equals("�ֵ���ӡ")) {
      this.onSplitPrint();
    }
    else if (code.equals("����")) {
      this.onRelationView();
    }
    else if(code.equals("�����ݹ�")){
    	estimateLoadFee();
    }else if (event.getButtonObject().isCheckboxGroup()) {
      event.getButtonObject().setSelected(true);
      this.nodeContext.setTransactionType(event.getButtonObject().getTag());
    }
    else {
      this.handle.doEvent(event);
    }
  }
  /**
   * �����۳������ɵ����䵥���ɷ����ݹ���
   * �������䵥�ķ�����Ϣ,�����ݹ�Ӧ����
   * @author lumzh
   * @param null
   * @since 2012-08-16 �Ӳ�Ʒ�������Ƴ���Ϊһ�������ķ���.
   * @return boolean true:��Ӧ���ɹ� false: һ���ĳ��ʧ��. ʧ��ԭ����μ��쳣��Ϣ.
   */
  protected boolean estimateLoadFee(){	  
	  DelivBillVO delivbillvo=(DelivBillVO) this.node.getCardPanel().getCard().getBill();
		List<DJZBVO> estimationTempVOs=new Vector<DJZBVO>();
	  if(delivbillvo==null){
			//����޷�����Ϣ��,�����κβ���.
			return true;
		}else{
			/**
			 * ��ʼ���������Ϣ.
			 * ���˷�����Ϣ,�����̷���.
			 */
			DelivBillHeadVO delivbillheadvo=delivbillvo.getHead();
			DelivBillItemVO[] delivbillitemvo=delivbillvo.getInvBodys();
			DelivBillPackItemVO[] delivbillpackitemvo=delivbillvo.getPackBodys();
			/**
			 * ��ʼ��֯�ݹ�Ӧ����VO
			 */
			ClientEnvironment ce=ClientEnvironment.getInstance();//��ʼ����������
			UFDouble nmoneytotal=new UFDouble(0);//���䵥�����ܽ��
			
			if(delivbillheadvo.getAttributeValue("isestimate")!=null && delivbillheadvo.getAttributeValue("isestimate").toString().equals("Y"))return false;
			if(delivbillitemvo!=null && delivbillitemvo.length>0){
             for(int j=0;j<delivbillitemvo.length;j++){
            	 DelivBillItemVO delivbillitem= delivbillitemvo[j];
            	 nmoneytotal=nmoneytotal.add(delivbillitem.getNmoney()); 
             }
			}
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);//ʵ�����ͻ��˲�ѯ�ӿ�

				DJZBVO oneAPVO=new DJZBVO();//ʵ����һ��Ӧ����VO
				DJZBHeaderVO head=new DJZBHeaderVO();//ʵ����һ���ݹ�Ӧ������ͷVO.
				DJZBItemVO[] bodyVOs=new DJZBItemVO[1];//��ʼ������VO����

			
					DJZBItemVO body=new DJZBItemVO();//ʵ����һ���ݹ�Ӧ��������VO
					body.setBbhl(new UFDouble(1.0));//���һ���
					body.setBbye(nmoneytotal);//�������--��˰���
					body.setBilldate(new UFDate());//����
					body.setBzbm("00010000000000000001");//���ֱ���--����
					//body.setcheckflag ���˱��
					nc.vo.pub.para.SysInitVO DM015 = null;
					try {
						DM015 = nc.ui.pub.para.SysInitBO_Client.queryByParaCode(ce.getCorporation().getPk_corp(),
						"DM015");
					if(DM015!=null && DM015.getPkvalue()!=null){
						
						String dmsql="select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+DM015.getPkvalue()+"'";
						String pk_inbasdoc=null;
						try {//�����ǰ������ⵥ�����������ֶ�Ϊ��,��ô��ʼ��ѯͨ���ո����̵�ҵ�����ͱ���.
							pk_inbasdoc= (String) query.executeQuery(dmsql, new ColumnProcessor());
						} catch (BusinessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}	
						body.setCinventoryid(pk_inbasdoc);
					}//�����������ID--���ô����������id
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}   
					body.setCksqsh(delivbillitemvo[0].getCfirstbillitemid());//Դͷ������id--������Ϣ��id
					body.setDdhh(delivbillitemvo[0].getCdelivbill_bid());//�ϲ���Դ����id--������Ϣ��id
					body.setDdlx(delivbillitemvo[0].getCdelivbill_hid());//�ϲ���Դ����id--������ⵥID
					String deptsql="select cdptid from ic_general_h where cgeneralhid='"+delivbillitemvo[0].getCdelivbill_hid()+"'";
					String dept=null;
					try {//�����ǰ������ⵥ�����������ֶ�Ϊ��,��ô��ʼ��ѯͨ���ո����̵�ҵ�����ͱ���.
						dept= (String) query.executeQuery(deptsql, new ColumnProcessor());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					
					body.setDeptid(dept);//����pk-������ⵥ�еĲ���PK
					body.setDfbbje(nmoneytotal);//�������ҽ��--��˰���
					body.setDfbbsj(new UFDouble(new Double(0.0)));//��������˰��--0
					body.setDfbbwsje(nmoneytotal);//����������˰���--��˰���
				//	body.setDfshl(new UFDouble(oneExpense.getNnumber()));//��������--����
					body.setDfybje(nmoneytotal);//����ԭ�ҽ��--��˰���
					body.setDfybsj(new UFDouble(new Double(0.0)));//����ԭ��˰��--0
					body.setDfybwsje(nmoneytotal);//����ԭ����˰���--��˰���
				//	body.setDj(new UFDouble(oneExpense.getNoriginalcurprice()));//����--����
					body.setDjdl("yf");//���ݴ���--yf
					body.setDjlxbm("D1");//�������ͱ���--D1
					body.setDr(0);
					body.setDwbm(ce.getCorporation().getPk_corp());//��˾pk--��ǰ��½��˾id
					body.setFbye(new UFDouble(new Double(0.0)));//�������--0
					body.setFlbh(0);//��¼���--���к�
					body.setFx(-1);//����
					body.setHbbm(delivbillitemvo[0].getCtakefeebasid());//������--���̹���id
				//	body.setHsdj(new UFDouble(oneExpense.getNoriginalcurprice()));//��˰����--����
					body.setIsSFKXYChanged(new UFBoolean(false));//�ո���Э���Ƿ����仯--N
					body.setIsverifyfinished(new UFBoolean(false));//�Ƿ�������--N
					body.setJsfsbm("4804");//�ϲ���Դ��������--4A ������ⵥ
					body.setKslb(1);//��˰���--1
					body.setOld_flag(new UFBoolean(false));
					body.setOld_sys_flag(new UFBoolean(false));
					body.setPausetransact(new UFBoolean(false));//�����־--N
					body.setPh("4C");//Դͷ��������--4A
					body.setpjdirection("none");//Ʊ�ݷ���--none
					body.setQxrq(new UFDate());//��Ч����--��ǰ����
					body.setSfbz("3");//�ո���־--"3"
				//	body.setShlye(new UFDouble(oneExpense.getNnumber()));//�������--����
					body.setSl(new UFDouble(new Double(0.0)));//˰��--0
					body.setVerifyfinisheddate(new UFDate("3000-01-01"));//�����������--Ĭ��3000-01-01
					body.setWldx(1);//���������־--1
					body.setXgbh(-1);//���ʱ�־ ---   -1
					body.setXyzh(delivbillitemvo[0].getCfirstbillid());//Դͷ����id--������ⵥid
					body.setYbye(nmoneytotal);//ԭ�����--��˰���
					body.setYwbm("0001AA10000000006MFZ");//��������PK--�̶�0001AA10000000006MFZ
					String cbizid="select cbizid from ic_general_h where cgeneralhid='"+delivbillitemvo[0].getCdelivbill_hid()+"'";
					String busiid=null;
					try {//�����ǰ������ⵥ�����������ֶ�Ϊ��,��ô��ʼ��ѯͨ���ո����̵�ҵ�����ͱ���.
						busiid= (String) query.executeQuery(cbizid, new ColumnProcessor());
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					
					body.setYwybm(busiid);//ҵ��ԱPK--������ⵥҵ��Աid
					
					/**
					 * �����־: �Զ�����18 19
					 */
					body.setZyx18("tureFree");//2010-11-07 "�����ݹ�Ӧ��"��־,������: �ݹ�����,See:EstimateImpl Լ9181�� �������ɲɹ���Ʊʱ�Ĵ���
					body.setZyx19(body.getHbbm());//2010-11-07 "���̹���ID" ������: �ݹ����� ,See:EstimateImpl Լ9423�� �������ɲɹ���Ʊʱ�Ĵ���
					bodyVOs[0]=body;
				
				head.setBbje(new UFDouble(nmoneytotal));//���ҽ��--�����ۼӽ��
				head.setDjdl("yf");//���ݴ���--yf
				head.setDjkjnd(ce.getAccountYear());//������--��ǰϵͳ�Ļ�����
				head.setDjkjqj(ce.getAccountMonth());//����ڼ�--��ǰϵͳ����ڼ�
				head.setDjlxbm("D1");//�������ͱ���--D1
				head.setDjrq(new UFDate());//��������--��ǰϵͳ����
				head.setDjzt(2);//����״̬--1 ��ʾ�ѱ��� 2��ʾ����Ч
				head.setDr(0);
				head.setDwbm(ce.getCorporation().getPk_corp());//��λ����--��˾ID
				head.setEffectdate(new UFDate());//��Ч����--��ǰϵͳ����
				head.setHzbz("-1");//���˱�־--  -1 ��ʾ���ǻ���
				head.setIsjszxzf(new UFBoolean(false));//�Ƿ��������֧��--��
				head.setIsnetready(new UFBoolean(false));//�Ƿ��Ѿ���¼--��
				//head.setIspaid(new UFBoolean(false));//�Ƿ񸶿�
				head.setIsreded(new UFBoolean(false));//�Ƿ���
				head.setIsselectedpay(1);//ѡ�񸶿�--1
				head.setLrr(ce.getUser().getPrimaryKey());//¼����--��ǰ��½�û�id
				head.setLybz(4);//��Դ��־--4 ��ʾϵͳ����, 1 ��ʾ����
				head.setPrepay(new UFBoolean(false));//Ԥ�տ��־--N
				head.setPzglh(1);//ϵͳ��־--1
				head.setQcbz(new UFBoolean(false));//�ڳ���־--N
				head.setSpzt("1");//Ϊ��,��ʾδ����
				head.setShr(ce.getUser().getPrimaryKey());//�����,��ǰ��½�û�
				head.setShkjnd(ce.getAccountYear());//��˻�����
				head.setShkjqj(ce.getAccountMonth());//��˻���ڼ�
				head.setShrq(ce.getDate());//�������
				head.setSxbz(10);//��Ч��־--0��ʾδ��Ч  10 ��ʾ����Ч
				head.setSxkjnd(ce.getAccountYear());//��Ч������
				head.setSxkjqj(ce.getAccountMonth());//��Ч����ڼ�
				head.setSxr(ce.getUser().getPrimaryKey());//��Ч��
				head.setSxrq(ce.getDate());//��Ч����
				String queryBusitype="select t.pk_busitype from bd_busitype t where t.busicode='arap' and t.businame='�ո�ͨ������'";
				Object busitype=null;
				try {//�����ǰ������ⵥ�����������ֶ�Ϊ��,��ô��ʼ��ѯͨ���ո����̵�ҵ�����ͱ���.
					busitype= query.executeQuery(queryBusitype, new ColumnProcessor());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				head.setXslxbm(busitype==null?"00011110000000002RGT":busitype.toString());//�������ͱ���--������ⵥ��ҵ������(ҵ������)����
				head.setYbje(new UFDouble(nmoneytotal));//���ҽ��--�����ۼӽ��
				head.setYwbm("0001AA10000000006MFZ");//��������--Ĭ��0001AA10000000006MFZ
				head.setZgyf(1);//�ݹ�Ӧ����־--1��ʾ�ݹ�Ӧ�� 0��ʾ���ݹ�Ӧ��
				head.setZzzt(0);//֧��״̬--0
				head.setZyx20("Y");//2010-11-07  MeiChao ���ں������ô�����Ҫ,�����ֵ,�в�����������.
				
				oneAPVO.setParentVO(head);//�����ͷ
				oneAPVO.setChildrenVO(bodyVOs);//�������
				estimationTempVOs.add(oneAPVO);//��VO����������.
		
			//��ȡӦ��Ӧ���Ķ�������ӿ�
			IArapBillPublic iARAP=(IArapBillPublic)NCLocator.getInstance().lookup(IArapBillPublic.class.getName());
			try {
				DJZBVO[] apVOs=new DJZBVO[estimationTempVOs.size()];
				iARAP.saveArapBills(estimationTempVOs.toArray(apVOs));
				writeBackOff(delivbillvo);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MessageDialog.showErrorDlg(this.node.getCardPanel().getPanel(),"����","���ݹ�Ӧ��ʧ��!");
				return false;
			}
		}
	  return true;
  }
  //lumzh 2012 ��д���䵥״̬:���䵥���ݹ�Ӧ���ɹ��󽫣�vdef1�Ƿ����ݹ���ֵ����ΪY
  private void writeBackOff(DelivBillVO delivbillheadvo){
		DelivBillVO delivbillheadvo1=ClientHelper.getInstance().writebackoffdelivbill(delivbillheadvo);
        this.onUpdate(delivbillheadvo1);
  }
  private void onNew() {
    NodeStatus status = this.node.getNodeStatus();
    if (status == NodeStatus.List) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.Card);
      this.node.fireSwitchEvent(event);
    }

    CardCtrl card = this.node.getCardPanel().getCard();
    
    CardHelper helper = new CardHelper(card);
    helper.addNewBill();

    // ����������֯
    String cdelivorgid = this.nodeContext.getCdelivorgid();
    helper.setHeadTailValue("cdelivorgid", cdelivorgid);
    String cdelivorgname = this.nodeContext.getCdelivorgname();
    helper.setHeadTailValue("cdelivorgname", cdelivorgname);

    // ������������
    String transactionType = this.nodeContext.getCurrentTransactionType();
    helper.setHeadTailValue("cdelivtype", transactionType);
    String transactionTypeName = this.nodeContext
        .getTransactionTypeName(transactionType);
    helper.setHeadTailValue("cdelivtypename", transactionTypeName);

    // ���ù�˾
    IUIContext context = this.node.getContext();
    String pk_corp = context.getPk_corp();
    helper.setHeadTailValue("pk_corp", pk_corp);
    String formula = "ccorpname->getColValue(bd_corp,unitname,pk_corp,pk_corp)";
    card.getBillCard().execHeadFormula(formula);

    // ��������ʱ��
    helper.setHeadTailValue("ddelivdate", context.getDate());
    UFDateTime time = new UFDateTime(System.currentTimeMillis());
    helper.setHeadTailValue("tdelivtime", time.getUFTime());

    // ���������������λ
    IParameter parameter = context.getParameter(ValueConstant.GROUPCORP);
    // ���ſͻ��������������������ġ�������λ��
    String vweightunitname = parameter.getParameter("BD203");
    // ���ſͻ��������������������ġ��洢��λ��
    String vvolumnunitname = parameter.getParameter("BD201");
    helper.setHeadTailValue("vweightunitname", vweightunitname);
    helper.setHeadTailValue("vvolumnunitname", vvolumnunitname);

    helper.transferFocusToHead();

  }
  private void onCancel() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    FunctionStatus functionStatus = this.node.getFunctionStatus();
    if (functionStatus == FunctionStatus.Common) {
     
      helper.cancelUpdate();

      IBillBuffer billBuffer = this.node.getCommonBillBuffer();
      int cursor = billBuffer.getCurrentRow();
      if (cursor != -1) {
        ISmartBill bill = billBuffer.getBill(cursor);
        helper.show(bill);
      }
      // �ָ�װ��¼���ģ��״̬
      if (this.nodeContext.isLoadEditing()) {
        this.handle.getLoadEditEventHandle().cancelEdit();
      }
      // ������շ�����Ϣ�Ŀ�������
      this.handle.getCopyPasteOperator().reset();
    }
    else if (functionStatus == FunctionStatus.TransferBill) {
      String message = NCLangResOnserver.getInstance().getStrByID("40142020",
          "UPP40142020-000075")/* @res ""��ǰת�����ݱ�ȡ���󽫴�ת���б��б�ɾ�����Ƿ�ȷ��Ҫȡ��"" */;
      int value = MessageDialog.showOkCancelDlg(this.node.getAbstractUI(),
          null, message, UIDialog.ID_OK);
      if (value != UIDialog.ID_OK) {
        return;
      }
      // ����Ƭ�ϵĵ�����Ϣ������������ظ�����
      helper.show(null);
      IBillBuffer billBuffer = this.node.getTransferBillBuffer();
      int index = billBuffer.getCurrentRow();
      billBuffer.remove(index);
      int size = billBuffer.size();
      if (size == 0) {
        this.node.setFunctionStatus(FunctionStatus.Common);
      }
      else {
        billBuffer.setCurrentRow(0);
      }
      SwitchEvent switchEvent = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(switchEvent);
    }
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000076")/* @res "ȡ���ɹ�" */);
  }

  private void resetRowNo() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.resetRowNo();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000077")/* @res "�����к�" */);
  }

  private void onCopyLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.copyLine();
  }

  private void onAddLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.addLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000078")/* @res "����һ�з�¼" */);
  }

  private void onInsertLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.insertLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000079")/* @res "����һ�з�¼" */);
  }

  private void onDeleteLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.deleteLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000080")/* @res "ɾ��һ�з�¼" */);
  }

  private void onPasteLine() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.pasteLine();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000081")/* @res "ճ��һ�з�¼" */);
  }

  private void onPasteLineToTail() {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.pasteLineToTail();
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000082")/* @res "ճ���е�ĩβ" */);
  }

  private void onRelationView() {
    IBillBuffer billBuffer = this.node.getCommonBillBuffer();
    int index = billBuffer.getCurrentRow();
    if (index == -1) {
      return;
    }
    DelivBillVO bill = (DelivBillVO) billBuffer.getBill(index);
    String cbillid = bill.getHead().getCdelivbill_hid();
    String cbilltypecode = "4804";
    String cuserid = this.node.getContext().getUserID();
    //String pk_corp = this.node.getContext().getPk_corp();
    String cbiztypeid = null;
    DelivBillHeadVO head = (DelivBillHeadVO) bill.getParentVO();
    SourceBillFlowDlg relationDlg = new SourceBillFlowDlg(this.node
        .getAbstractUI(), cbilltypecode, cbillid, cbiztypeid, cuserid, head
        .getVbillcode());
    relationDlg.showModal();
  }

  private void onCombinView() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.List) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.Card);
      this.node.fireSwitchEvent(event);
    }
    BillCardPanel bc = this.node.getCardPanel().getCard().getBillCard();
    String stablecode = bc.getCurrentBodyTableCode();
    Boolean flag = true;
    ArrayList<BillItem> list = new ArrayList<BillItem>();

    IBillBuffer buffer = this.node.getCommonBillBuffer();
    //BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    newPrintEntry();
    DelivBillVO bill = new DelivBillVO();
    DelivBillItemVO[] itemvo = null;
    DelivBillPackItemVO[] packvo = null;
    /*
     * if (nodeStatus == NodeStatus.List) { int[] selectedIndexs =
     * listPanel.getHeadTable().getSelectedRows();
     * if(selectedIndexs==null||selectedIndexs.length<=0){
     * this.node.getAbstractUI().showErrorMessage(NCLangResOnserver.getInstance().getStrByID(
     * "40142020", "UPP40142020-000083")@res "��ѡ��ϲ�������"); return; } bill =
     * (DelivBillVO) buffer.getBill(selectedIndexs[0]); } if (nodeStatus ==
     * NodeStatus.Card) {
     */
    int index = buffer.getCurrentRow();
    if (index == -1) {
      this.node.getAbstractUI().showErrorMessage(
          NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000083")/* @res "��ѡ��ϲ�������" */);
      return;
    }
    bill = (DelivBillVO) buffer.getBill(index);
    // }
    BillItem[] item = null;
    // �жϵ�ǰ��ҳǩ�ǻ�����Ϣ/�����շ�����Ϣ�����ǰ�װ��Ϣ/��װ�շ�����Ϣ
    if (stablecode.equalsIgnoreCase("dm_delivbill_b")
        || stablecode.equalsIgnoreCase("invinfo")) {
      itemvo = bill.getInvBodys();
      if (itemvo == null || itemvo.length <= 0) {
        this.node.getAbstractUI().showErrorMessage(
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-0000824")/* @res "û�кϲ�������" */);
        return;
      }
      item = bc.getBillModel("dm_delivbill_b").getBodyItems();
      BillItem[] item1 = bc.getBillModel("invinfo").getBodyItems();

      for (int i = 0, loop = item.length; i < loop; i++) {
        item[i].setShareTableCode("dm_delivbill_b");
        // if(item[i].isShow()){
        list.add(item[i]);
        // }
      }

      for (int i = 0, loop = item.length; i < loop; i++) {
        item1[i].setShareTableCode("invinfo");
        // item1[i].isShow()&&
        if (list.contains(item1[i]) && item1[i].isShow() && !item[i].isShow()) {
          list.get(i).setShow(item1[i].isShow());
        }
        if (!list.contains(item1[i])) {
          list.add(item1[i]);
        }
      }
    }
    else if (stablecode.equalsIgnoreCase("dm_packbill")
        || stablecode.equalsIgnoreCase("packinfo")) {
      packvo = bill.getPackBodys();
      if (packvo == null || packvo.length <= 0) {
        this.node.getAbstractUI().showErrorMessage(
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000084")/* @res "û�кϲ�������" */);
        return;
      }
      item = bc.getBillModel("dm_packbill").getBodyItems();
      BillItem[] item1 = bc.getBillModel("packinfo").getBodyItems();

      for (int i = 0, loop = item.length; i < loop; i++) {
        item[i].setShareTableCode("dm_packbill");
        list.add(item[i]);
      }

      for (int i = 0, loop = item.length; i < loop; i++) {
        item1[i].setShareTableCode("packinfo");
        if (list.contains(item1[i]) && item1[i].isShow() != item[i].isShow()) {
          list.get(i).setShow(item1[i].isShow());
        }
        if (!list.contains(item1[i])) {
          list.add(item1[i]);
        }
      }
      flag = false;
    }
    BillItem[] billitem = new BillItem[list.size()];
    for (int i = 0, loop = list.size(); i < loop; i++) {
      billitem[i] = list.get(i);
    }
    CollectDlg dlg = new CollectDlg(this.node.getAbstractUI(), "�ϲ���ʾ");
    // �̶�������:�˷ѳе���λ�������ص㡢�ջ��ص㡢������ࡢ������롢��װ����
    String[] fixGroupItems = new String[0];
    // ȱʡ������
    String[] defaultGroupItems = new String[0];
    String[] sumItems = new String[0];

    // ��ƽ����
    String[] avgItems = new String[0];
    // ���Ȩƽ����
    String[] proAvgItems = new String[0];
    // ������
    String numItem = null;
    BillData data = bc.getBillData();
    if (flag) {
      dlg.initData(node, bc, billitem, fixGroupItems, defaultGroupItems, sumItems,
          avgItems, proAvgItems, numItem, null,
          (CircularlyAccessibleValueObject[]) itemvo, bill, data, flag);
    }
    else {
      dlg.initData(node, bc, billitem, fixGroupItems, defaultGroupItems, sumItems,
          avgItems, proAvgItems, numItem, null,
          (CircularlyAccessibleValueObject[]) packvo, bill, data, flag);
    }

    dlg.showModal();
    // �����

    /*
     * CollectDlg dlg = new CollectDlg(this.node.getAbstractUI(), "�ϲ���ʾ");
     * BillCardPanel bc = this.node.getCardPanel().getCard().getBillCard(); //
     * �̶������� String[] fixGroupItems = new String[0]; // ȱʡ������ String[]
     * defaultGroupItems = new String[0]; // ����� String[] sumItems = new
     * String[0]; // ��ƽ���� String[] avgItems = new String[0]; // ���Ȩƽ���� String[]
     * proAvgItems = new String[0]; // ������ String numItem = null; //
     * ����ҳǩ�����ڶ�ҳǩ���� String[] stablecode =
     * this.node.getCardPanel().getCard().getBillCard().getBillData().getTableCodes(IBillItem.BODY);//new
     * String[0]; dlg.initData(bc, fixGroupItems, defaultGroupItems, sumItems,
     * avgItems, proAvgItems, numItem, stablecode); dlg.showModal();
     */
  }

  private void onFirst() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = 0;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onLast() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.size() - 1;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onPrevious() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow() - 1;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onNext() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow() + 1;
    buffer.setCurrentRow(cursor);
    ISmartBill bill = buffer.getBill(cursor);
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
  }

  private void onLocate() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.List) {
      ListCtrl list = this.node.getListPanel().getList();
      BillListPanel panel = list.getBillList();
      Container parent = this.node.getAbstractUI();
      BillModel billModel = panel.getBillListData().getHeadBillModel();
      BillItem[] items = panel.getBillListData().getHeadItems();
      UITable table = panel.getHeadTable();
      OrientDialog dialog = new OrientDialog(parent, billModel, items, table);
      dialog.showModal();
      Integer[] rows = dialog.getSelectOrientRows();
      if (rows != null) {
        panel.getHeadTable().setRowSelectionInterval(rows[0].intValue(),
            rows[0].intValue());
      }
      else {
        OrientDialog.clearOrientColor(panel.getHeadTable());
      }
      panel.updateUI();
    }
    else if (nodeStatus == NodeStatus.Card) {
      CardCtrl card = this.node.getCardPanel().getCard();
      BillCardPanel panel = card.getBillCard();
      Container parent = this.node.getAbstractUI();
      BillModel billModel = panel.getBillModel();
      BillItem[] items = panel.getBodyItems();
      String stablecode = panel.getCurrentBodyTableCode();
      //���ǵ�ǰ��ǩ�ǰ�װ�У���Ҫ�õ���װ�е�����item
      if (stablecode.equalsIgnoreCase("dm_packbill")
          || stablecode.equalsIgnoreCase("packinfo")) {
        items = panel.getBillModel("dm_packbill").getBodyItems();
      }
      for (BillItem item : items) {
        item.setShareTableCode(panel.getCurrentBodyTableCode());
      }

      UITable table = panel.getBillTable();
      OrientDialog dialog = new OrientDialog(parent, billModel, items, table);
      dialog.showModal();
      Integer[] rows = dialog.getSelectOrientRows();
      if (rows != null) {
        panel.getBillTable().setRowSelectionInterval(rows[0].intValue(),
            rows[0].intValue());
      }
      else {
        // OrientDialog.clearOrientColor(panel.getBillTable());
      }
    }
  }

  private void onRefresh() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    buffer.clear();
    String sql = this.node.getQuery().getConditionSQL();
    ISmartBill[] bills = this.query(sql);
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.Card) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(event);
    }
    if (bills == null || bills.length == 0) {
      buffer.clear();
      this.node.getListPanel().getList().getListHelper().showBills(buffer, -1);
      this.node.getAbstractUI().showHintMessage(
          NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000085")/* @res "û�в鵽������Ҫ�ļ�¼" */);
      return;
    }
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000086")/* @res "�鵽" */
            + bills.length
            + NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000087")/* @res "�ŵ���" */);
    for (ISmartBill bill : bills) {
      buffer.add(bill);
    }
    buffer.setCurrentRow(0);
    this.node.getListPanel().getList().getListHelper().showBills(buffer, 0);
  }

  private void onPrint() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    newPrintEntry();
    DelivBillVO bill = new DelivBillVO();
    DelivBillHeadVO headerVO = new DelivBillHeadVO();
    if (nodeStatus == NodeStatus.List) {
      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
      if (selectedIndexs == null || selectedIndexs.length <= 0) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }
    if (nodeStatus == NodeStatus.Card) {
      int index = buffer.getCurrentRow();
      if (index == -1) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(index);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }

    // ����PringLogClient�Լ�����PrintInfo
    ScmPrintlogVO voSpl = new ScmPrintlogVO();
    voSpl = new ScmPrintlogVO();
    voSpl.setCbillid(headerVO.getCdelivbill_hid()); // ���������ID
    voSpl.setTs(headerVO.getTs().toString()); // ��������ʱ���
    voSpl.setVbillcode(headerVO.getVbillcode());// �������䵥�ţ�������ʾ��
    voSpl.setCbilltypecode("4804");
    voSpl.setCoperatorid(ClientEnvironment.getInstance().getUser()
        .getPrimaryKey());
    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
    voSpl.setPk_corp(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey());

    nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
    plc.setPrintEntry(getPrintEntry());
    // ���õ�����Ϣ
    plc.setPrintInfo(voSpl);
    
    final DelivBillVO delivBillVO = bill;
    plc.addFreshTsListener(
        new IFreshTsListener() {
          public void freshTs(String sBillID, String sTS, Integer iPrintCount) {     
            delivBillVO.getHead().setAttributeValue("ts", sTS);
            delivBillVO.getHead().setAttributeValue("iprintcount", iPrintCount);

            BillCardPanel cardPanel = ActionEventHandle.this.node.getCardPanel()
                .getCard().getBillCard();
            BillListPanel listPanel = ActionEventHandle.this.node.getListPanel()
                .getList().getBillList();
            NodeStatus nodeStatus = ActionEventHandle.this.node.getNodeStatus();
            if (nodeStatus == NodeStatus.Card) {
              cardPanel.getTailItem("iprintcount").setValue(iPrintCount);
            }
            else {
              listPanel.getHeadBillModel().setValueAt(iPrintCount, 
                  ActionEventHandle.this.node.getCommonBillBuffer()
                  .getCurrentRow(), "iprintcount");
            }
          }
        }
    );

    // ���ô�ӡ����
    getPrintEntry().setPrintListener(plc);

    // ��ӡ
    getDataSource().setBillVO(bill);
    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().print();
  }

  private void onPrintPreview() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    //ArrayList<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    //BillCardPanel cardPanel = this.node.getCardPanel().getCard().getBillCard();
    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    //String cbilltypecode = null;
    newPrintEntry();
    DelivBillVO bill = new DelivBillVO();
    DelivBillHeadVO headerVO = new DelivBillHeadVO();
    if (nodeStatus == NodeStatus.List) {

      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }
    if (nodeStatus == NodeStatus.Card) {
      int index = buffer.getCurrentRow();
      if (index == -1) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(index);
      headerVO = (DelivBillHeadVO) bill.getParentVO();
    }

    // ����PringLogClient�Լ�����PrintInfo
    ScmPrintlogVO voSpl = new ScmPrintlogVO();
    voSpl = new ScmPrintlogVO();
    voSpl.setCbillid(headerVO.getCdelivbill_hid()); // ���������ID
    voSpl.setTs(headerVO.getTs().toString()); // ��������ʱ���
    voSpl.setVbillcode(headerVO.getVbillcode());// �������䵥�ţ�������ʾ��
    voSpl.setCbilltypecode("4804");
    voSpl.setCoperatorid(ClientEnvironment.getInstance().getUser()
        .getPrimaryKey());
    voSpl.setIoperatetype(new Integer(PrintConst.PAT_OK));// �̶�
    voSpl.setPk_corp(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey());

    nc.ui.scm.print.PrintLogClient plc = new nc.ui.scm.print.PrintLogClient();
    plc.setPrintEntry(getPrintEntry());
    // ���õ�����Ϣ
    plc.setPrintInfo(voSpl);

    // ���ô�ӡ����
    getPrintEntry().setPrintListener(plc);

    // ��ӡԤ��
    getDataSource().setBillVO(bill);
    getPrintEntry().setDataSource(getDataSource());
    getPrintEntry().preview();

    /*
     * for (ISmartBill bill : bills) { if (bill instanceof
     * AggregatedValueObject) { list.add((AggregatedValueObject) bill); } else {
     * list.add(new TraditionalBillVO(bill)); } } try { BillPrintTool tool =
     * this.createBillPrintTool(list); tool.onBatchPrintPreview(listPanel,
     * cbilltypecode); } catch (BusinessException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); } catch
     * (InstantiationException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); } catch
     * (IllegalAccessException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); } catch
     * (InterruptedException ex) {
     * ExceptionUtils.getInstance().wrappException(ex); }
     */
  }
  
  /**
   * �ֵ���ӡ
   */
  private void onSplitPrint() {
    if (getSplitPrintDlg().showModal() != QueryConditionClient.ID_OK) {
      return;
    }

    NodeStatus nodeStatus = this.node.getNodeStatus();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillCardPanel cardPanel = this.node.getCardPanel().getCard().getBillCard();
    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
    newPrintEntry();
    DelivBillVO bill = null;
    if (nodeStatus == NodeStatus.List) {
      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
      if (selectedIndexs == null || selectedIndexs.length <= 0) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
    }
    if (nodeStatus == NodeStatus.Card) {
      int index = buffer.getCurrentRow();
      if (index == -1) {
        return;
      }
      bill = (DelivBillVO) buffer.getBill(index);
    }
    
    ArrayList<DelivBillVO> alPrintVO = new ArrayList<DelivBillVO>( 1 );
    alPrintVO.add( bill );
    try {
      SplitPrintTool printTool = new SplitPrintTool(this.node.getUIPanel(),
          "40142020", alPrintVO, cardPanel.getBillData(), 
          SplitPrintDataSource.class, 
          ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), 
          ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
          "vbillcode", "cdelivbill_hid");
      printTool.onSplitCardPrintPreview(cardPanel, listPanel, "4804", 
          getSplitPrintDlg().getSplitParams());
    }
    catch (Exception e) {
      GenMsgCtrl.printErr(e.getMessage());
    }
  }
  
  private SplitPrintParamDlg getSplitPrintDlg() {
    if (splitPrintDlg == null) {
      SplitPrintParametersHelper helper = SplitPrintParametersHelper
          .getInstance();
      SplitParams[] paramvos = helper.getSplitPrintParameters();
      ISaveSplitParams paraSaveImpl = helper.getSaveSplitParasImpl();
      splitPrintDlg = new SplitPrintParamDlg(this.node.getAbstractUI(), 
          paramvos, paraSaveImpl);
    }

    return splitPrintDlg;
  }

  private PrintDataSource getDataSource() {
//    NodeStatus nodeStatus = this.node.getNodeStatus();
//    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillCardPanel cardPanel = this.node.getCardPanel().getCard().getBillCard();
//    BillListPanel listPanel = this.node.getListPanel().getList().getBillList();
//    DelivBillVO bill = new DelivBillVO();
//    if (nodeStatus == NodeStatus.List) {
//
//      int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
//      bill = (DelivBillVO) buffer.getBill(selectedIndexs[0]);
//    }
//    if (nodeStatus == NodeStatus.Card) {
//      int index = buffer.getCurrentRow();
//      bill = (DelivBillVO) buffer.getBill(index);
//    }
//    int[] selectedIndexs = listPanel.getHeadTable().getSelectedRows();
    
    if (null == m_dataSource) {
      m_dataSource = new PrintDataSource();
      BillData bd = cardPanel.getBillData();
      m_dataSource.setBillData(bd);
      m_dataSource.setTotalLinesInOnePage(getPrintEntry().getBreakPos());
    }
    return m_dataSource;
  }

  private nc.ui.pub.print.PrintEntry getPrintEntry() {
    if (null == m_print) {
      m_print = new nc.ui.pub.print.PrintEntry(null, null);
      m_print.setTemplateID(ClientEnvironment.getInstance().getCorporation()
          .getPrimaryKey(), this.node.getNodeInfo().getNodeCode(),
          ClientEnvironment.getInstance().getUser().getPrimaryKey(), null);
    }
    return m_print;
  }

  private void newPrintEntry() {
    m_print = new PrintEntry(null, null);

    m_print.setTemplateID(ClientEnvironment.getInstance().getCorporation()
        .getPrimaryKey(), this.node.getNodeInfo().getNodeCode(),
        ClientEnvironment.getInstance().getUser().getPrimaryKey(), null);
  }

/*  private BillPrintTool createBillPrintTool(
      ArrayList<AggregatedValueObject> list) {
    String nodecode = null;
    BillData bd = null;
    Class specialDataSource = null;
    String pk_corp = null;
    String cuserid = null;
    String vbillcodeName = null;
    String cbillidName = null;
    BillPrintTool tool = null;
    try {
      tool = new BillPrintTool(nodecode, list, bd, specialDataSource, pk_corp,
          cuserid, vbillcodeName, cbillidName);
    }
    catch (BusinessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    catch (InstantiationException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    catch (IllegalAccessException ex) {
      ExceptionUtils.getInstance().wrappException(ex);
    }
    return tool;
  }*/

  private NewQuery getQueryCondDlg() {
    if (m_QueryDLG == null) {
      TemplateInfo tempinfo = new TemplateInfo();
      tempinfo.setPk_Org(this.node.getContext().getPk_corp());
      tempinfo.setCurrentCorpPk(this.node.getContext().getPk_corp());
      tempinfo.setFunNode(this.node.getNodeInfo().getNodeCode());
      tempinfo.setUserid(this.node.getContext().getUserID());
      m_QueryDLG = new NewQuery(this.node.getAbstractUI(), null, tempinfo);
    }

    return m_QueryDLG;
  }

  private void onQuery() {
    int ret = getQueryCondDlg().showModal();
    if (ret != UIDialog.ID_OK) {
      return;
    }
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    buffer.clear();
    ArrayList list = getQueryCondDlg().getExtendQryCond();
    String extendqrysql = (String) list.get(0);
    String desendate = (String) list.get(1);
    String sql = getQueryCondDlg().getWhereSQL();
    String sqlwhere = null;

    /*
     * if(sql==null&&extendqrysql==null){ SqlBuilder buf = new SqlBuilder();
     * buf.append("select cdelivbill_hid from dm_delivbill where dr=0 ");
     * buf.append(" and cdelivorgid", ShowDelivOrg.getDelivOrgPK()); sqlwhere =
     * buf.toString(); } else
     */if (sql == null && extendqrysql != null) {
      sqlwhere = extendqrysql;
    }
    else if (sql != null && extendqrysql == null) {
      sqlwhere = sql;
    }
    else if (sql != null && extendqrysql != null) {
      sqlwhere = sql + "and" + extendqrysql;
    }

    sqlwhere = queryaction(sqlwhere, desendate);
    this.node.getQuery().setConstionSQL(sqlwhere);

    ISmartBill[] bills = this.query(sqlwhere);
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.Card) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(event);
    }
    if (bills == null || bills.length == 0) {
      buffer.clear();
      this.node.getListPanel().getList().getListHelper().showBills(buffer, -1);
      this.node.getAbstractUI().showHintMessage(
          NCLangResOnserver.getInstance().getStrByID("40142020",
              "UPP40142020-000085")/* @res "û�в鵽������Ҫ�ļ�¼" */);
      return;
    }
    this.node.getAbstractUI().showHintMessage(
        NCLangResOnserver.getInstance().getStrByID("40142020",
            "UPP40142020-000086")/* @res "�鵽" */
            + bills.length
            + NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000087")/* @res "�ŵ���" */);
    for (ISmartBill bill : bills) {
      buffer.add(bill);
    }
    buffer.setCurrentRow(0);
    this.node.getListPanel().getList().getListHelper().showBills(buffer, 0);

  }

  /**
   * ��������������ͨ����ѯ��������ƴ��sql��� <b>����˵��</b>
   * 
   * @param sqlwhere
   * @return
   * @author liyu
   * @time 2008-11-12 ����05:04:35
   */

  private String queryaction(String sqlwhere, String desendate) {
    String sql = null;
    SqlBuilder buf = new SqlBuilder();
    int index = 0;
    if (desendate != null) {
      index = desendate.indexOf("dsenddate");
    }
    // ������ȫ����Ϊ��
    if (sqlwhere == null && desendate == null) {
      buf
          .append("select distinct cdelivbill_hid from dm_delivbill where dr=0 ");
      buf.append(" and cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      sql = buf.toString();
      return sql;
    }
    // ������������Ϊ�գ��������ڲ�Ϊ�գ���ֱ��ѯ���䵥����--���������䵥����--��װ��֮��������
    if (sqlwhere == null && desendate != null) {
      buf
          .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_delivbill_b where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_delivbill_b.cdelivbill_hid ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(desendate.substring(0, index));
      buf.append("dm_delivbill_b.");
      buf.append(desendate.substring(index, desendate.length()));
      buf.append(" union ");
      buf
          .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_packbill where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_packbill.cdelivbill_hid ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(desendate.substring(0, index));
      buf.append("dm_packbill.");
      buf.append(desendate.substring(index, desendate.length()));
      sql = buf.toString();
      return sql;
    }
    int deitemindex = sqlwhere.indexOf("dm_delivbill_b");
    int packitemindex = sqlwhere.indexOf("dm_packbill");
    if (deitemindex == -1 && packitemindex == -1) {
      if (desendate == null) {
        buf
            .append("select distinct cdelivbill_hid from dm_delivbill where dr=0 ");
        buf.append(" and cdelivorgid", ShowDelivOrg.getDelivOrgPK());
        buf.append(" and ");
        buf.append(sqlwhere);
      }
      else {
        buf
            .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_delivbill_b where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_delivbill_b.cdelivbill_hid ");
        buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg
            .getDelivOrgPK());
        buf.append(" and ");
        buf.append(sqlwhere);
        buf.append(desendate.substring(0, index));
        buf.append("dm_delivbill_b.");
        buf.append(desendate.substring(index, desendate.length()));
        buf.append(" union ");
        buf
            .append("select distinct dm_delivbill.cdelivbill_hid from dm_delivbill,dm_packbill where dm_delivbill.dr=0  and dm_delivbill.cdelivbill_hid = dm_packbill.cdelivbill_hid ");
        buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg
            .getDelivOrgPK());
        buf.append(" and ");
        buf.append(sqlwhere);
        buf.append(desendate.substring(0, index));
        buf.append("dm_packbill.");
        buf.append(desendate.substring(index, desendate.length()));
      }
      sql = buf.toString();
    }
    else if (deitemindex != -1 && packitemindex == -1) {
      buf.append(" select distinct dm_delivbill.cdelivbill_hid from dm_delivbill ");
      buf.append(" left join dm_delivbill_b on dm_delivbill_b.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" where dm_delivbill.dr=0 ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(" and ");
      buf.append(sqlwhere);
      if (index != 0) {
        buf.append(desendate.substring(0, index));
        buf.append("dm_delivbill_b.");
        buf.append(desendate.substring(index, desendate.length()));
      }
      sql = buf.toString();
    }
    else if (deitemindex == -1 && packitemindex != -1) {
      buf.append(" select distinct dm_delivbill.cdelivbill_hid from dm_delivbill ");
      buf.append(" left join dm_packbill on dm_packbill.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" where dm_delivbill.dr=0 ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(" and ");
      buf.append(sqlwhere);
      sql = buf.toString();
      if (index != 0) {
        buf.append(desendate.substring(0, index));
        buf.append("dm_packbill.");
        buf.append(desendate.substring(index, desendate.length()));
      }
    }
    else if (deitemindex != -1 && packitemindex != -1) {
      buf.append(" select distinct dm_delivbill.cdelivbill_hid ");
      buf.append(" from dm_delivbill ");
      buf.append(" left join dm_delivbill_b on dm_delivbill_b.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" left join dm_packbill on dm_packbill.cdelivbill_hid = dm_delivbill.cdelivbill_hid ");
      buf.append(" where dm_delivbill.dr=0 ");
      buf.append(" and dm_delivbill.cdelivorgid", ShowDelivOrg.getDelivOrgPK());
      buf.append(" and ");
      buf.append(sqlwhere);
      if (index != 0) {
        buf.append(desendate.substring(0, index));
        buf.append("dm_packbill.");
        buf.append(desendate.substring(index, desendate.length()));
        buf.append(desendate.substring(0, index));
        buf.append("dm_delivbill_b.");
        buf.append(desendate.substring(index, desendate.length()));
      }
      sql = buf.toString();
    }
    return sql;
  }
  private ISmartBill[] query(String sql) {
    DelivBillVO[] bills = ClientHelper.getInstance().queryDelivBill(sql);
    DelivBillVirtualFiledLoader loader = new DelivBillVirtualFiledLoader(
        this.nodeContext);
    loader.setCardCtrl(this.node.getCardPanel().getCard());
    loader.load(bills);
    return bills;
  }
  
  private void fillDelivOrgInfo(DelivBillVO bill) {
    DelivBillHeadVO head = bill.getHead();
    String delivOrgId = head.getCdelivorgid();
    DelivBillItemVO[] invs = bill.getInvBodys();
    DelivBillPackItemVO[] pkgs = bill.getPackBodys();
    for (DelivBillItemVO inv : invs) {
      inv.setCdelivorgid(delivOrgId);
    }
    for (DelivBillPackItemVO pkg : pkgs) {
      pkg.setCdelivorgid(delivOrgId);
    }
  }

  private void onSave() {
    if (this.nodeContext.isLoadEditing()) {
      this.handle.getLoadEditEventHandle().endEdit();
      return;
    }
    CardCtrl card = this.node.getCardPanel().getCard();
    
    ValidateFormulaExecuter executor = new ValidateFormulaExecuter(card
        .getBillCard());
    if (!executor.execute())
      return;
    
    CardHelper helper = new CardHelper(card);
    
    DelivBillVO srcBill = (DelivBillVO) helper.getBill();
    fillDelivOrgInfo(srcBill);
    boolean updateFlag = true;
    if (srcBill.getPrimaryKey()== null) {
      updateFlag = false;
    }
    DelivBillVO bill = null;
    try{
      bill = ClientHelper.getInstance().saveDelivBill(srcBill);
    }
    catch(RuntimeException ex){
      Throwable th = ExceptionUtils.getInstance().unmarsh(ex);
      if( !(th instanceof SCMBusinessException )){
        throw ex;
      }
      SCMBusinessException e = (SCMBusinessException) th;
      SmartVO billvo = e.getBillVO();
      ArrayList<Integer> selectedRow = e.getSelectedRow();
      if(selectedRow!=null&&selectedRow.size()>0&& billvo!=null){
        String voname = billvo.getEntityName();
        if(voname.equalsIgnoreCase(DelivBillItemVOMeta.class.getName())){
          SetColor.setRowColor(card.getBillCard().getBodyPanel("dm_delivbill_b"), selectedRow, Color.YELLOW);
        }
        if(voname.equalsIgnoreCase(DelivBillPackItemVOMeta.class.getName())){
          SetColor.setRowColor(card.getBillCard().getBodyPanel("dm_packbill"), selectedRow, Color.YELLOW);
        }
      }
      ExceptionUtils.getInstance().wrappException(ex);
    }
    DelivBillVirtualFiledLoader loader = new DelivBillVirtualFiledLoader(
        this.nodeContext);
    loader.setCardCtrl(this.node.getCardPanel().getCard());
    loader.load(new DelivBillVO[] {srcBill}, new DelivBillVO[] {bill});
    FunctionStatus functionStatus = this.node.getFunctionStatus();
    if (functionStatus == FunctionStatus.Common) {
      if (updateFlag) {
        this.onUpdate(bill);
      }
      else {
        this.onInsert(bill);
      }
    }
    else if (functionStatus == FunctionStatus.TransferBill) {
      IBillBuffer billBuffer = this.node.getTransferBillBuffer();
      int index = billBuffer.getCurrentRow();
      billBuffer.remove(index);
      int size = billBuffer.size();
      if (size == 0) {
        this.node.setFunctionStatus(FunctionStatus.Common);
      }
      else {
        billBuffer.setCurrentRow(0);
      }
      billBuffer = this.node.getCommonBillBuffer();
      billBuffer.add(bill);
      size = billBuffer.size();
      // ���õ�ǰ���α굽���һ�ŵ���
      billBuffer.setCurrentRow(size - 1);
      SwitchEvent switchEvent = new SwitchEvent(this.node, NodeStatus.List);
      this.node.fireSwitchEvent(switchEvent);
    }
    // ������շ�����Ϣ�Ŀ�������
    this.handle.getCopyPasteOperator().reset();
  }

  private void onInsert(ISmartBill bill) {
  	CardCtrl card = this.node.getCardPanel().getCard();
  	CardHelper helper = new CardHelper(card);
  	helper.show(bill);
  	helper.endUpdate();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    buffer.add(bill);
    int size = buffer.size();
    buffer.setCurrentRow(size - 1);
  }

  private void onUpdate(ISmartBill bill) {
    CardCtrl card = this.node.getCardPanel().getCard();
    CardHelper helper = new CardHelper(card);
    helper.show(bill);
    helper.endUpdate();
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow();
    buffer.setBill(cursor, bill);
  }

  private void onDelete() {
    NodeStatus nodeStatus = this.node.getNodeStatus();
    if (nodeStatus == NodeStatus.List) {
      this.deleteForList();
    }
    else if (nodeStatus == NodeStatus.Card) {
      this.deleteForCard();
    }
  }

  private void deleteForList() {
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    BillListPanel list = this.node.getListPanel().getList().getBillList();
    int[] rows = list.getHeadTable().getSelectedRows();
    DelivBillVO[] bills = (DelivBillVO[]) buffer.getBills(rows);

    int length = rows.length;
    List<Integer> rowList = new ArrayList<Integer>();
    List<DelivBillVO> billList = new ArrayList<DelivBillVO>();
    for (int i = 0; i < length; i++) {
      FBillStatusFlag status = bills[i].getHead().getFstatusflag();
      if (status == FBillStatusFlag.FREE) {
        billList.add(bills[i]);
        rowList.add(Integer.valueOf(rows[i]));
      }
    }
    int size = billList.size();
    if (size == 0) {
      return;
    }
    bills = new DelivBillVO[size];
    bills = billList.toArray(bills);
    rows = new int[size];
    for (int i = 0; i < size; i++) {
      rows[i] = rowList.get(i).intValue();
    }
    int ret = MessageDialog
        .showOkCancelDlg(this.node.getAbstractUI(),
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000088")/* @res "ȷ��" */, NCLangResOnserver
                .getInstance().getStrByID("40142020", "UPP40142020-000089")/*
                                                                             * @res
                                                                             * "ȷ��ɾ����ǰ��ѡ������"
                                                                             */);
    if (ret == UIDialog.ID_CANCEL) {
      return;
    }
    this.delete(bills);

    size = rows.length - 1;
    for (int i = size; i >= 0; i--) {
      buffer.remove(rows[i]);
    }
    this.setBufferIndex(buffer, rows[0]);
  }

  private void deleteForCard() {
    int ret = MessageDialog
        .showOkCancelDlg(this.node.getAbstractUI(),
            NCLangResOnserver.getInstance().getStrByID("40142020",
                "UPP40142020-000088")/* @res "ȷ��" */, NCLangResOnserver
                .getInstance().getStrByID("40142020", "UPP40142020-000089")/*
                                                                             * @res
                                                                             * "ȷ��ɾ����ǰ��ѡ������"
                                                                             */);
    if (ret == UIDialog.ID_CANCEL) {
      return;
    }
    IBillBuffer buffer = this.node.getCommonBillBuffer();
    int cursor = buffer.getCurrentRow();
    ISmartBill bill = buffer.getBill(cursor);
    this.delete(new ISmartBill[] {
      bill
    });
    buffer.remove(cursor);
    this.setBufferIndex(buffer, cursor);
  }

  private void delete(ISmartBill[] bills) {
    int length = bills.length;
    DelivBillVO[] vos = new DelivBillVO[length];
    for (int i = 0; i < length; i++) {
      vos[i] = (DelivBillVO) bills[i];
    }
    ClientHelper.getInstance().deleteDelivBill(vos);
  }

  private void setBufferIndex(IBillBuffer buffer, int cursor) {
    int index = cursor;
    NodeStatus nodeStatus = this.node.getNodeStatus();
    int size = buffer.size();
    if (size == 0) {
      if (nodeStatus == NodeStatus.List) {
        this.node.getListPanel().getList().getListHelper()
            .showBills(buffer, -1);
      }
      else if (nodeStatus == NodeStatus.Card) {
        CardCtrl card = this.node.getCardPanel().getCard();
        CardHelper helper = new CardHelper(card);
        helper.show(null);
      }
      return;
    }
    if (cursor >= size) {
      index = size - 1;
    }
    buffer.setCurrentRow(index);
    if (nodeStatus == NodeStatus.List) {
      this.node.getListPanel().getList().getListHelper().showBills(buffer,
          index);
      BillListPanel list = this.node.getListPanel().getList().getBillList();
      list.getHeadTable().setRowSelectionInterval(index, index);
    }
    else if (nodeStatus == NodeStatus.Card) {
      ISmartBill bill = buffer.getBill(index);
      CardCtrl card = this.node.getCardPanel().getCard();
  	  CardHelper helper = new CardHelper(card);
  	 helper.show(bill);
    }
  }

  private void onModify() {
    NodeStatus status = this.node.getNodeStatus();
    if (status == NodeStatus.List) {
      SwitchEvent event = new SwitchEvent(this.node, NodeStatus.Card);
      this.node.fireSwitchEvent(event);
    }
    CardCtrl card = this.node.getCardPanel().getCard();
    card.getBillCard().setEnabled(true);
    FunctionStatus fstatus = this.node.getFunctionStatus();
    //�����ת��������ǰΪ��������
    if (fstatus == FunctionStatus.TransferBill) {
      card.setStatus(CardStatus.New);
    }
    //�������޸Ľ���
    else{
      card.setStatus(CardStatus.Update);
      //���䵥�������񵥺����䵥�ĵ��ݺŲ������޸�
      if (card.getBillCard().getHeadItem("bmissionbillflag").getValueObject().equals("true"))
    	  card.getBillCard().getHeadItem("vbillcode").setEdit(false);
      else
    	  card.getBillCard().getHeadItem("vbillcode").setEdit(true);
    }
    card.getBillCard().transferFocusTo(IBillItem.HEAD);
  }

  private void onSelectAll() {
    ListCtrl list = this.node.getListPanel().getList();
    UITable table = list.getBillList().getHeadTable();
    int count = table.getRowCount();
    if (count == 0) {
      return;
    }
    table.selectAll();
    int cursor = table.getSelectedRow();
    this.node.getCommonBillBuffer().setCurrentRow(cursor);
  }

  private void onUnSelectAll() {
    ListCtrl list = this.node.getListPanel().getList();
    UITable table = list.getBillList().getHeadTable();
    int count = table.getRowCount();
    if (count > 0) {
      table.removeRowSelectionInterval(0, count - 1);
      this.node.getCommonBillBuffer().setCurrentRow(-1);
    }
  }

  private void onCancleTransferBill() {
    String message = NCLangResOnserver.getInstance().getStrByID("40142020",
        "UPP40142020-000090")/* @res "�Ƿ�ȷ��Ҫȡ����ǰ�������ɵ��ݲ���" */;
    int value = MessageDialog.showOkCancelDlg(this.node.getAbstractUI(), null,
        message, UIDialog.ID_OK);
    if (value != UIDialog.ID_OK) {
      return;
    }
    IBillBuffer buffer = this.node.getTransferBillBuffer();
    buffer.clear();
    this.node.setFunctionStatus(FunctionStatus.Common);
    buffer = this.node.getCommonBillBuffer();
    if (buffer.size() > 0) {
      this.node.getListPanel().getList().getListHelper().showBills(buffer, 0);
    }
    else {
      this.node.getListPanel().getList().getListHelper().showBills(buffer, -1);
    }
  }

  private void onCardRowEdit() {
    this.node.getCardPanel().getCard().getBillCard().startRowCardEdit();
  }
}
