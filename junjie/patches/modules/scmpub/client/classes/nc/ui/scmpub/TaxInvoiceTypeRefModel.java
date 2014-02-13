package nc.ui.scmpub;

import nc.ui.bd.ref.AbstractRefModel;

public class TaxInvoiceTypeRefModel extends AbstractRefModel {

	public TaxInvoiceTypeRefModel() {
		super();
		init();
	}
	
	private void init(){
	
		setRefNodeName("税务发票类型");
		setRefTitle("税务发票类型");
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
		"组织主键",
		"编码",
		"名称",
		"创建人",
		"创建日期",
		"修改人",
		"修改日期",
		"审核人",
		"审核日期",
		"是否费用类型",
		"是否默认",
		"是否封存"
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