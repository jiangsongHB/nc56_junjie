package nc.ui.ic.mdck;

import nc.ui.bd.ref.AbstractRefModel;

public class MdczRef extends AbstractRefModel {

	public MdczRef() {
	}

	public String getTableName() {
		String table = "(select ttt.* from (select t1.PK_MDXCL_B,"
				+ " t1.dr,"
				+ " t4.cscode as cspaceid,"
				+ " t1.jbh,"
				+ " t1.md_width,"
				+ " t1.md_length,"
				+ " t1.md_meter,"
				+ " t1.md_note,"
				+ " t1.md_lph,"
				+ " t1.md_zyh,"
				+ " t1.md_zlzsh,"
				+ " t1.remark,"
				+ " t1.zhishu,"
				+ " (t1.zhishu-nvl(b.sdzs,0)) as kyzs,"
				+ " t1.zhongliang,"
				+ " t2.pk_corp,"
				+ " t2.cwarehouseidb,"
				+ " t2.ccalbodyidb,"
				+ " t2.cinvbasid,"
				+ " t2.cinventoryidb,"
				+ " t3.invspec"
				+ " from nc_mdxcl_b t1"
				+ " left join nc_mdxcl t2 on t1.pk_mdxcl = t2.pk_mdxcl"
				+ " left join bd_invbasdoc t3 on t2.cinvbasid = t3.pk_invbasdoc"
				+ " left join bd_cargdoc t4 on t1.cspaceid = t4.pk_cargdoc"
				+ " left join (select pk_mdxcl_b, sum(to_number(sdzs)) as sdzs"
				+ " from nc_mdsd"
				+ " left join so_saleorder_b on so_saleorder_b.corder_bid= nc_mdsd.xsddbt_pk"
				+ " where sfsx='0' and so_saleorder_b.dr=0 and to_date(sxrq, 'yyyy-mm-dd') > sysdate"
				+ " group by pk_mdxcl_b) b on b.pk_mdxcl_b=t1.PK_MDXCL_B) ttt where ttt.dr=0 and ttt.kyzs>0)";
		return table;

	}

	public String getPkFieldCode() {
		return "pk_mdxcl_b";
	}

	public String[] getFieldCode() {
		return new String[] { "cspaceid", "jbh", "zhishu", "zhongliang",
				"kyzs", "md_width", "md_length", "invspec", "md_meter",
				"md_note", "md_lph", "md_zyh", "md_zlzsh", "remark" };
	}

	public String[] getFieldName() {
		return new String[] { "货位", "件编号", "现存支数", "现存重量", "可用支数", "宽度", "长度",
				"规格（厚度）", "米数", "实测厚*宽*长", "炉批号", "资源号", "质保证书号", "备注" };
	}

	public String getRefTitle() {
		return "码单参照";
	}

	@Override
	public String[] getHiddenFieldCode() {
		// TODO Auto-generated method stub
		return new String[] { "pk_mdxcl_b" };
	}

	@Override
	public String getRefCodeField() {
		// TODO Auto-generated method stub
		return "jbh";
	}
}
