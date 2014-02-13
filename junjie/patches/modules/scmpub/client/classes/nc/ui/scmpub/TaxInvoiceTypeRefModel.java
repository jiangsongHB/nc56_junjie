package nc.ui.scmpub;

import nc.ui.bd.ref.AbstractRefModel;

public class TaxInvoiceTypeRefModel extends AbstractRefModel {

	public TaxInvoiceTypeRefModel() {
		super();
		init();
	}
	
	private void init(){
	
		setRefNodeName("˰��Ʊ����");
		setRefTitle("˰��Ʊ����");
		setFieldCode(new String[] {
		"pk_corp",
		"code",
		"name",
		"creator",
		"creationtime",
		"modifier",
		"modifiedtime",
		"approver",
		"approvedtime",
		"iffee",
		"isdefault",
		"isseal"
				});
		setFieldName(new String[] {
		"��֯����",
		"����",
		"����",
		"������",
		"��������",
		"�޸���",
		"�޸�����",
		"�����",
		"�������",
		"�Ƿ��������",
		"�Ƿ�Ĭ��",
		"�Ƿ���"
				});
		setHiddenFieldCode(new String[] {
		"pk_taxinvoicetype"
			});
		setPkFieldCode("pk_taxinvoicetype");
        setWherePart("isnull(dr,0)=0");
		setTableName("bd_taxinvoicetype");
		setRefCodeField("code");
		setRefNameField("name");
	
	}
	
}