package nc.ui.bd.ref.busi;

import nc.bs.logging.Logger;
import nc.ui.bd.ref.AbstractRefGridTreeBigDataModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 存货档案
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class InvmandocDefaultRefModel extends AbstractRefGridTreeBigDataModel {

	public InvmandocDefaultRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
		// TODO 自动生成构造函数存根
		//2010-11-25 MeiChao begin将存货编码中的"图号"修改显示为 "存货名称"  ,"存货名称"修改显示为"产地"
		String[] filecode=this.getFieldCode();
		String[] filename=this.getFieldName();
		for(int i=0;i<filecode.length;i++){
			if(filecode[i].toString().equals("bd_invbasdoc.graphid")){//
			filename[i]="存货名称";
			}
			if(filecode[i].toString().equals("bd_invbasdoc.invname")){
				filename[i]="产地";
				}
				if(filecode[i].toString().equals("bd_invbasdoc.invtype")){//"型号" 显示为 "材质"
					filename[i]="材质";
					}
		}
		//2010-11-25 MeiChao end将存货编码中的"图号"修改显示为 "存货名称"  ,"存货名称"修改显示为"产地"  型号: 材质
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// 测试代码
		setRootName(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC000-0001443")/* @res "存货分类" */);
		setClassFieldCode(new String[] { "invclasscode", "invclassname",
				"invclasslev", "endflag", "pk_invcl" });
		setClassJoinField("pk_invcl");
		setClassTableName("bd_invcl");
		try {// ewei+ 查询模板数据权限后台调该处会抛空指针
			setCodingRule(RefPubUtil.getCodeRuleFromPara("BD101", getPk_corp()));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
		setClassDefaultFieldCount(2);
		setClassDataPower(true);
		// sxj 2003-02-20
		setClassWherePart(" (pk_corp='" + getPk_corp() + "' or pk_corp= '"
				+ "0001" + "') and sealdate is null ");
		setConfig(refNodeName);
		setHiddenFieldCode(new String[] { "bd_invmandoc.pk_invbasdoc",
				"bd_invmandoc.pk_invmandoc", "bd_invbasdoc.pk_invcl" });
		setPkFieldCode("bd_invmandoc.pk_invmandoc");
		setTableName("bd_invmandoc inner join bd_invbasdoc on bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc ");
		if (isGroupAssignData()) {
			setWherePart(" bd_invmandoc.pk_corp='"
					+ getPk_corp()
					+ "' and bd_invmandoc.pk_invbasdoc in (select pk_invbasdoc from bd_invbasdoc where pk_corp='0001')");
		} else {
			setWherePart(" bd_invmandoc.pk_corp='" + getPk_corp() + "'");
		}

		setDocJoinField("bd_invbasdoc.pk_invcl");
		setRefCodeField("bd_invbasdoc.invcode");
		setRefNameField("bd_invbasdoc.invname");
		// v56，助记码匹配不支持规格和型号了
		setMnecode(new String[] { "bd_invbasdoc.invmnecode" });

		setRefQueryDlgClaseName("nc.ui.bd.b16.QueryDlg");
		String strFomula = "getColValue(bd_measdoc, measname, pk_measdoc ,bd_invbasdoc.pk_measdoc)";

		setFormulas(new String[][] { { "bd_invbasdoc.pk_measdoc", strFomula } });
		setSealedWherePart(" bd_invmandoc.sealflag='N' or bd_invmandoc.sealflag is null");
		resetFieldName();
		setCommonDataTableName("bd_invmandoc");
		setCommonDataBasDocPkField("bd_invmandoc.pk_invbasdoc");
		setCommonDataBasDocTableName("bd_invbasdoc");
		setLeafOfClassTreeFetchData(true);

	}

	@Override
	protected String getCommonDataCompositeTableName() {

		if (canUseDB()) {
			return getTableName();
		} else {
			return " bd_invmandoc_c inner join bd_invmandoc on bd_invmandoc_c.pk_doc = bd_invmandoc.pk_invmandoc inner join bd_invbasdoc on bd_invmandoc.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc ";
		}
		// TODO Auto-generated method stub

	}

}
