package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * Ӧ���� ->  �ɹ���Ʊ
 * 2010-10-21
 * δ�޸� ���ֶ�ӳ�䷴������
 * @author ������
 *
 */
public class CHGF1TO25 extends VOConversionUI {
	
	public CHGF1TO25(){
		super();
	}
	
	public String getAfterClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] { 
"H_pk_corp->H_dwbm",//��˾����->��λ����			  
"H_coperator->SYSOPERATOR",//�Ƶ���ID -> ϵͳ��ǰ�û�
"H_cvendorbaseid->B_hbbm",//��Ӧ�̻�����ϢID 				   
"H_ctermprotocolid->B_sfkxyh",//����Э��
"H_vmemo->H_scomment",//��ע
"H_cdeptid->B_deptid",//����ID 
"H_cemployeeid->B_ywybm",//ҵ��ԱID
"H_cbiztype->H_xslxbm",//ҵ����������ID

"H_ctermprotocolid->B_sfkxyh",//����Э��ID -> �ո���Э���


"B_cbaseid->B_cinventoryid",//�������ID -> �����������pk ��ȷ

"B_ccostsubjid->B_szxmid",//��֧��ĿID -> ��֧��ĿID
"B_ccurrencytypeid->B_bzbm",//ԭ�ұ���ID -> ���ֱ���

"B_cprojectphaseid->B_pk_jobobjpha",//��Ŀ�׶�ID ->��Ŀ�׶ι�����id
"B_csourcebillid->B_ddh",//��Դ����ID ->������
"B_csourcebillrowid->B_ddhid",//��Դ������ID-> ������id 
"B_cupsourcebillid->B_vouchid",//�ϲ���Դ����ID -> ��������
"B_cupsourcebillrowid->B_fb_oid",//�ϲ���Դ������ID ->���ݸ���id
"B_cupsourcebilltype->H_djlxbm",//�ϲ���Դ�������� ->�������ͱ���
"B_idiscounttaxtype->B_kslb",//��˰��� -> ��˰���

"B_ninvoicenum->B_dfshl",//��Ʊ���� -> ��������

"B_nsummny->B_dfbbje",//���Ҽ�˰�ϼ� -> �������ҽ�� 
"B_ntaxmny->B_dfbbsj",//->����������˰���
"B_nmoney->B_dfbbwsje",//���ҽ�� -> ����������˰���
"B_noriginalsummny->B_dffbje", //���Ҽ�˰�ϼ� ->�������ҽ��
"B_nassisttaxmny->B_dffbsj",//����˰��->��������˰��
"B_noriginalsummny->B_dfybje",//��� -> ����ԭ�ҽ��
"B_noriginaltaxmny->B_dfybsj",//˰�� ->����ԭ��˰��
"B_noriginalcurmny->B_dfybwsje",//ԭ����˰��� -> ����ԭ����˰���
"B_noriginalcurprice->B_dj",//���� -> ����
"B_nexchangeotoarate->B_fbhl",//���һ���
"B_nassistsummny->B_fbye",//�������		
"B_norgnettaxprice->B_hsdj",//��˰����	
"B_idiscounttaxtype->B_kslb",//��˰���
"B_vproducenum->B_seqnum",//���κ� -> ���κ�1 				
"B_noriginalsummny->B_ybye",//ԭ�Ҽ�˰�ϼ� -> ԭ�����
"B_nsummny->B_bbye",//���Ҽ�˰�ϼ� -> �������				
"B_nexchangeotobrate->B_bbhl",//�۱����� -> ���һ���			
"B_ntaxrate->B_sl",//˰�� -> ˰��
"B_ninvoicenum->B_shlye",//��Ʊ���� -> ������� 
"B_vmemo->B_zy",//��ע -> ժҪ	
//"B_vdef18->B_zyx18",//�Ƿ�Ϊ�����ݹ��ı�ʶ ʹ���Զ�����18
"H_cvendormangid->B_zyx19"//��Ӧ�������Զ�������ȡ�� ���̹���id ʹ��Ӧ�������Զ�����19
				};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_cbilltype->\"25\"",//�������ͱ��� ��ʽ
				"H_darrivedate->SYSDATE",
				"H_dinvoicedate->SYSDATE",
				"H_dr->int(0)",//��ʽ ɾ����־
				"H_finitflag->int(0)",//��ʽ  �����־
				"H_ibillstatus->int(0)",//��ʽ ����״̬
				"H_iinvoicetype->int(0)",//��ʽ ��Ʊ����
				"H_vdef20->\"Y\"",//2010-10-30 MeiChao  ����ͷ���Զ����ֶ�20����ΪY ��ʾ��ǰ��Ʊ���ɷ����ݹ�Ӧ�������ɵ�,Ϊ�ɹ����÷�Ʊ.
				
				"B_cmangid->getColValue(bd_invmandoc,pk_invmandoc,pk_invbasdoc,B_cbaseid,pk_corp,H_pk_corp)",//�������ID 
				
				"B_dr->int(0)",//��ʽ ɾ����־
				"B_cprojectid->getColValue(bd_jobmngfil,pk_jobmngfil,pk_jobbasfil,B_jobid)",// ��ʽ ��ĿID
				};
	}

	public String getOtherClassName() {
		return null;
	}

}