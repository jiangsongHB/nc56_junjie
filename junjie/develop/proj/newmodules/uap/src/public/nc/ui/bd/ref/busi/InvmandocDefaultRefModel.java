package nc.ui.bd.ref.busi;

import nc.bs.logging.Logger;
import nc.ui.bd.ref.AbstractRefGridTreeBigDataModel;
import nc.ui.bd.ref.RefPubUtil;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * �������
 * <p>
 * <strong>�ṩ�ߣ�</strong>UAP
 * <p>
 * <strong>ʹ���ߣ�</strong>
 * 
 * <p>
 * <strong>���״̬��</strong>��ϸ���
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class InvmandocDefaultRefModel extends AbstractRefGridTreeBigDataModel {

	public InvmandocDefaultRefModel(String refNodeName) {
		setRefNodeName(refNodeName);
		// TODO �Զ����ɹ��캯�����
		//2010-11-25 MeiChao begin����������е�"ͼ��"�޸���ʾΪ "�������"  ,"�������"�޸���ʾΪ"����"
		String[] filecode=this.getFieldCode();
		String[] filename=this.getFieldName();
		for(int i=0;i<filecode.length;i++){
			if(filecode[i].toString().equals("bd_invbasdoc.graphid")){//
			filename[i]="�������";
			}
			if(filecode[i].toString().equals("bd_invbasdoc.invname")){
				filename[i]="����";
				}
				if(filecode[i].toString().equals("bd_invbasdoc.invtype")){//"�ͺ�" ��ʾΪ "����"
					filename[i]="����";
					}
		}
		//2010-11-25 MeiChao end����������е�"ͼ��"�޸���ʾΪ "�������"  ,"�������"�޸���ʾΪ"����"  �ͺ�: ����
	}

	public void setRefNodeName(String refNodeName) {
		m_strRefNodeName = refNodeName;
		// ���Դ���
		setRootName(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC000-0001443")/* @res "�������" */);
		setClassFieldCode(new String[] { "invclasscode", "invclassname",
				"invclasslev", "endflag", "pk_invcl" });
		setClassJoinField("pk_invcl");
		setClassTableName("bd_invcl");
		try {// ewei+ ��ѯģ������Ȩ�޺�̨���ô����׿�ָ��
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
		// v56��������ƥ�䲻֧�ֹ����ͺ���
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
