package nc.ui.ic.jjpanel;

import nc.ui.bd.ref.AbstractRefModel;

public class InvDetailRef extends AbstractRefModel {

	public InvDetailRef() {
	}

	public String getTableName() {
		String table = "(select t.*," +
				" f.graphid as invname," +
				" t.arrivenumber - nvl(m.storagenumber, 0) as unstoragenumber," +
				" t.arriveweight - nvl(m.storageweight, 0) as unstorageweight" +
				" from scm_invdetail t left join (select sum(t.def1) as storageweight," +
				" sum(t.srkzs) as storagenumber," +
				" m.pk_invdetail" +
				" from nc_mdcrk t, scm_invdetail_c m" +
				" where t.pk_mdcrk = m.pk_mdcrk" +
				" and t.dr = 0" +
				" and m.dr = 0" +
				" group by m.pk_invdetail) m on t.pk_invdetail=m.pk_invdetail" +
				" left join  bd_invbasdoc f on" +
				" t.pk_invbasdoc = f.pk_invbasdoc" +
				" and t.dr <> 1 and f.dr=0)";
		return table;
	}

	public String getPkFieldCode() {
		return "pk_invdetail";
	}

	public String[] getFieldCode() {
		return new String[] { "invname","contractthick","contractwidth","contractlength","contractmeter","unstoragenumber","unstorageweight" };
	}

	public String[] getFieldName() {
		return new String[] { "存货名称","钢厂厚度","钢厂宽度","钢厂长度","钢厂米数","到货未入库件数","到货未入库重量" };
	}

	public String getRefTitle() {
		return "存货明细";
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[] { "pk_invdetail" };
	}

	
	@Override
	public int getDefaultFieldCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public String getRefCodeField() {
		// TODO Auto-generated method stub
		return "invname";
	}

	@Override
	public String getWherePart() {

		return super.getWherePart();
	}
	
}
