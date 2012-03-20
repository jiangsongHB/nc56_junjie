/**    Create By NCPlugin beta 0.1.   **/
package nc.vo.ic.md;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * �뵥������ nc_mdcrk ���ɵ�VO����
 * 
 * @author ThinkPad
 * @createtime 2010-09-06 17:53:35
 */
public class MdcrkVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	/** �����ֶ�5 */
	public UFBoolean def5;

	/** ���� */
	public UFDouble md_meter;

	/** ��˾ */
	public String pk_corp;

	/** �뵥�ִ�������PK */
	public String pk_mdxcl_b;

	/** �������� */
	public String cbodybilltypecode;

	/** �ʱ�֤��� */
	public String md_zlzsh;

	/** ����ⷽ�� */
	public Integer crkfx;

	/** �Ƶ����� */
	public UFDate dmakedate;

	/** ���� */
	public UFDouble md_length;

	/** ֧�� */
	public UFDouble srkzs = new UFDouble(0);

	/** dr */
	public Integer dr;

	/** �Ƶ��� */
	public String voperatorid;

	/** �����ֶ�11 */
	public String def11;

	/** �����ֶ�14 */
	public String def14;

	/** �����ֶ�7 */
	public String def7;

	/** �����ֶ�6 */
	public String def6;

	/** ts */
	public UFDateTime ts;

	/** ʵ���*��*�� */
	public String md_note;

	/** �����ֶ�15 */
	public String def15;

	/** ��� */
	public UFDouble md_width;

	/** ����ⵥ����PK */
	public String cgeneralbid;

	/** ���ݷ��� */
	public Integer djfx;

	/** �����ֶ�10 */
	public String def10;

	/** ����� */
	public String jbh;

	/** ��Դ�� */
	public String md_zyh;

	/** ¯���� */
	public String md_lph;

	/** �����ֶ�13 */
	public String def13;

	/** ���� */
	public UFDouble srkzl = new UFDouble(0);

	/** �����ֶ�8 */
	public String def8;

	/** �����ֶ�1 */
	public UFDouble def1;

	/** ��ע */
	public String remark;

	/** ��λPK */
	public String cspaceid;

	/** �����ֶ�12 */
	public String def12;

	/** �����ֶ�2 */
	public UFDouble def2;

	/** ���� */
	public String pk_mdcrk;

	/** �Ƿ���� */
	public UFBoolean sfbj;

	/** �����ֶ�4 */
	public UFBoolean def4;

	/** �ֿ�PK */
	public String cwarehouseidb;

	/** �����֯ */
	public String ccalbodyidb;

	/** �����ֶ�3 */
	public UFDouble def3;

	/** �����ֶ�9 */
	public String def9;
	
	/**add by ouyangzhb 2012-03-20�Զ�����*/
	/** �����ֶ�16 */
	public String def16;
	/** �����ֶ�17 */
	public String def17;
	/** �����ֶ�18 */
	public String def18;
	/** �����ֶ�19 */
	public String def19;
	/** �����ֶ�20 */
	public String def20;
	/** �Ƿ���� */
	public UFBoolean sfgczl;
	
	public static final String SFGCZL = "sfgczl";
	public static final String DEF16 = "def16";
	public static final String DEF17 = "def17";
	public static final String DEF18= "def18";
	public static final String DEF19 = "def19";
	public static final String DEF20 = "def20";
	/**add by ouyangzhb 2012-03-20�Զ�����*/

	public static final String DEF5 = "def5";

	public static final String MD_METER = "md_meter";

	public static final String PK_CORP = "pk_corp";

	public static final String PK_MDXCL_B = "pk_mdxcl_b";

	public static final String CBODYBILLTYPECODE = "cbodybilltypecode";

	public static final String MD_ZLZSH = "md_zlzsh";

	public static final String CRKFX = "crkfx";

	public static final String DMAKEDATE = "dmakedate";

	public static final String MD_LENGTH = "md_length";

	public static final String SRKZS = "srkzs";

	public static final String DR = "dr";

	public static final String VOPERATORID = "voperatorid";

	public static final String DEF11 = "def11";

	public static final String DEF14 = "def14";

	public static final String DEF7 = "def7";

	public static final String DEF6 = "def6";

	public static final String TS = "ts";

	public static final String MD_NOTE = "md_note";

	public static final String DEF15 = "def15";

	public static final String MD_WIDTH = "md_width";

	public static final String CGENERALBID = "cgeneralbid";

	public static final String DJFX = "djfx";

	public static final String DEF10 = "def10";

	public static final String JBH = "jbh";

	public static final String MD_ZYH = "md_zyh";

	public static final String MD_LPH = "md_lph";

	public static final String DEF13 = "def13";

	public static final String SRKZL = "srkzl";

	public static final String DEF8 = "def8";

	public static final String DEF1 = "def1";

	public static final String REMARK = "remark";

	public static final String CSPACEID = "cspaceid";

	public static final String DEF12 = "def12";

	public static final String DEF2 = "def2";

	public static final String PK_MDCRK = "pk_mdcrk";

	public static final String SFBJ = "sfbj";

	public static final String DEF4 = "def4";

	public static final String CWAREHOUSEIDB = "cwarehouseidb";

	public static final String CCALBODYIDB = "ccalbodyidb";

	public static final String DEF3 = "def3";

	public static final String DEF9 = "def9";

	public MdcrkVO() {
		super();
	}

	
	/**�����ֶ�*/
	
	public String getDef16() {
		return def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef17() {
		return def17;
	}

	public void setDef17(String def17) {
		this.def17 = def17;
	}

	public String getDef18() {
		return def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
	}

	public String getDef19() {
		return def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef20() {
		return def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
	}

	public UFBoolean getSfgczl() {
		return sfgczl;
	}

	public void setSfgczl(UFBoolean sfgczl) {
		this.sfgczl = sfgczl;
	}
	/**�����ֶ�*/



	/**
	 * ���� �����ֶ�5 ��setter����.
	 * 
	 * @param UFBoolean
	 *            def5
	 */
	public void setDef5(UFBoolean def5) {
		this.def5 = def5;
	}

	/**
	 * ���� �����ֶ�5 ��getter����.
	 * 
	 * @return UFBoolean def5
	 */
	public UFBoolean getDef5() {
		return def5;
	}

	/**
	 * ���� ���� ��setter����.
	 * 
	 * @param UFDouble
	 *            md_meter
	 */
	public void setMd_meter(UFDouble md_meter) {
		this.md_meter = md_meter;
	}

	/**
	 * ���� ���� ��getter����.
	 * 
	 * @return UFDouble md_meter
	 */
	public UFDouble getMd_meter() {
		return md_meter;
	}

	/**
	 * ���� ��˾ ��setter����.
	 * 
	 * @param String
	 *            pk_corp
	 */
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	/**
	 * ���� ��˾ ��getter����.
	 * 
	 * @return String pk_corp
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * ���� �뵥�ִ�������PK ��setter����.
	 * 
	 * @param String
	 *            pk_mdxcl_b
	 */
	public void setPk_mdxcl_b(String pk_mdxcl_b) {
		this.pk_mdxcl_b = pk_mdxcl_b;
	}

	/**
	 * ���� �뵥�ִ�������PK ��getter����.
	 * 
	 * @return String pk_mdxcl_b
	 */
	public String getPk_mdxcl_b() {
		return pk_mdxcl_b;
	}

	/**
	 * ���� �������� ��setter����.
	 * 
	 * @param String
	 *            cbodybilltypecode
	 */
	public void setCbodybilltypecode(String cbodybilltypecode) {
		this.cbodybilltypecode = cbodybilltypecode;
	}

	/**
	 * ���� �������� ��getter����.
	 * 
	 * @return String cbodybilltypecode
	 */
	public String getCbodybilltypecode() {
		return cbodybilltypecode;
	}

	/**
	 * ���� �ʱ�֤��� ��setter����.
	 * 
	 * @param String
	 *            md_zlzsh
	 */
	public void setMd_zlzsh(String md_zlzsh) {
		this.md_zlzsh = md_zlzsh;
	}

	/**
	 * ���� �ʱ�֤��� ��getter����.
	 * 
	 * @return String md_zlzsh
	 */
	public String getMd_zlzsh() {
		return md_zlzsh;
	}

	/**
	 * ���� ����ⷽ�� ��setter����.
	 * 
	 * @param Integer
	 *            crkfx
	 */
	public void setCrkfx(Integer crkfx) {
		this.crkfx = crkfx;
	}

	/**
	 * ���� ����ⷽ�� ��getter����.
	 * 
	 * @return Integer crkfx
	 */
	public Integer getCrkfx() {
		return crkfx;
	}

	/**
	 * ���� �Ƶ����� ��setter����.
	 * 
	 * @param UFDate
	 *            dmakedate
	 */
	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	/**
	 * ���� �Ƶ����� ��getter����.
	 * 
	 * @return UFDate dmakedate
	 */
	public UFDate getDmakedate() {
		return dmakedate;
	}

	/**
	 * ���� ���� ��setter����.
	 * 
	 * @param UFDouble
	 *            md_length
	 */
	public void setMd_length(UFDouble md_length) {
		this.md_length = md_length;
	}

	/**
	 * ���� ���� ��getter����.
	 * 
	 * @return UFDouble md_length
	 */
	public UFDouble getMd_length() {
		return md_length;
	}

	/**
	 * ���� ֧�� ��setter����.
	 * 
	 * @param UFDouble
	 *            srkzs
	 */
	public void setSrkzs(UFDouble srkzs) {
		this.srkzs = srkzs;
	}

	/**
	 * ���� ֧�� ��getter����.
	 * 
	 * @return UFDouble srkzs
	 */
	public UFDouble getSrkzs() {
		return srkzs;
	}

	/**
	 * ���� dr ��setter����.
	 * 
	 * @param Integer
	 *            dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}

	/**
	 * ���� dr ��getter����.
	 * 
	 * @return Integer dr
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * ���� �Ƶ��� ��setter����.
	 * 
	 * @param String
	 *            voperatorid
	 */
	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	/**
	 * ���� �Ƶ��� ��getter����.
	 * 
	 * @return String voperatorid
	 */
	public String getVoperatorid() {
		return voperatorid;
	}

	/**
	 * ���� �����ֶ�11 ��setter����.
	 * 
	 * @param String
	 *            def11
	 */
	public void setDef11(String def11) {
		this.def11 = def11;
	}

	/**
	 * ���� �����ֶ�11 ��getter����.
	 * 
	 * @return String def11
	 */
	public String getDef11() {
		return def11;
	}

	/**
	 * ���� �����ֶ�14 ��setter����.
	 * 
	 * @param String
	 *            def14
	 */
	public void setDef14(String def14) {
		this.def14 = def14;
	}

	/**
	 * ���� �����ֶ�14 ��getter����.
	 * 
	 * @return String def14
	 */
	public String getDef14() {
		return def14;
	}

	/**
	 * ���� �����ֶ�7 ��setter����.
	 * 
	 * @param String
	 *            def7
	 */
	public void setDef7(String def7) {
		this.def7 = def7;
	}

	/**
	 * ���� �����ֶ�7 ��getter����.
	 * 
	 * @return String def7
	 */
	public String getDef7() {
		return def7;
	}

	/**
	 * ���� �����ֶ�6 ��setter����.
	 * 
	 * @param String
	 *            def6
	 */
	public void setDef6(String def6) {
		this.def6 = def6;
	}

	/**
	 * ���� �����ֶ�6 ��getter����.
	 * 
	 * @return String def6
	 */
	public String getDef6() {
		return def6;
	}

	/**
	 * ���� ts ��setter����.
	 * 
	 * @param UFDateTime
	 *            ts
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	/**
	 * ���� ts ��getter����.
	 * 
	 * @return UFDateTime ts
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * ���� ʵ���*��*�� ��setter����.
	 * 
	 * @param String
	 *            md_note
	 */
	public void setMd_note(String md_note) {
		this.md_note = md_note;
	}

	/**
	 * ���� ʵ���*��*�� ��getter����.
	 * 
	 * @return String md_note
	 */
	public String getMd_note() {
		return md_note;
	}

	/**
	 * ���� �����ֶ�15 ��setter����.
	 * 
	 * @param String
	 *            def15
	 */
	public void setDef15(String def15) {
		this.def15 = def15;
	}

	/**
	 * ���� �����ֶ�15 ��getter����.
	 * 
	 * @return String def15
	 */
	public String getDef15() {
		return def15;
	}

	/**
	 * ���� ��� ��setter����.
	 * 
	 * @param UFDouble
	 *            md_width
	 */
	public void setMd_width(UFDouble md_width) {
		this.md_width = md_width;
	}

	/**
	 * ���� ��� ��getter����.
	 * 
	 * @return UFDouble md_width
	 */
	public UFDouble getMd_width() {
		return md_width;
	}

	/**
	 * ���� ����ⵥ����PK ��setter����.
	 * 
	 * @param String
	 *            cgeneralbid
	 */
	public void setCgeneralbid(String cgeneralbid) {
		this.cgeneralbid = cgeneralbid;
	}

	/**
	 * ���� ����ⵥ����PK ��getter����.
	 * 
	 * @return String cgeneralbid
	 */
	public String getCgeneralbid() {
		return cgeneralbid;
	}

	/**
	 * ���� ���ݷ��� ��setter����.
	 * 
	 * @param Integer
	 *            djfx
	 */
	public void setDjfx(Integer djfx) {
		this.djfx = djfx;
	}

	/**
	 * ���� ���ݷ��� ��getter����.
	 * 
	 * @return Integer djfx
	 */
	public Integer getDjfx() {
		return djfx;
	}

	/**
	 * ���� �����ֶ�10 ��setter����.
	 * 
	 * @param String
	 *            def10
	 */
	public void setDef10(String def10) {
		this.def10 = def10;
	}

	/**
	 * ���� �����ֶ�10 ��getter����.
	 * 
	 * @return String def10
	 */
	public String getDef10() {
		return def10;
	}

	/**
	 * ���� ����� ��setter����.
	 * 
	 * @param String
	 *            jbh
	 */
	public void setJbh(String jbh) {
		this.jbh = jbh;
	}

	/**
	 * ���� ����� ��getter����.
	 * 
	 * @return String jbh
	 */
	public String getJbh() {
		return jbh;
	}

	/**
	 * ���� ��Դ�� ��setter����.
	 * 
	 * @param String
	 *            md_zyh
	 */
	public void setMd_zyh(String md_zyh) {
		this.md_zyh = md_zyh;
	}

	/**
	 * ���� ��Դ�� ��getter����.
	 * 
	 * @return String md_zyh
	 */
	public String getMd_zyh() {
		return md_zyh;
	}

	/**
	 * ���� ¯���� ��setter����.
	 * 
	 * @param String
	 *            md_lph
	 */
	public void setMd_lph(String md_lph) {
		this.md_lph = md_lph;
	}

	/**
	 * ���� ¯���� ��getter����.
	 * 
	 * @return String md_lph
	 */
	public String getMd_lph() {
		return md_lph;
	}

	/**
	 * ���� �����ֶ�13 ��setter����.
	 * 
	 * @param String
	 *            def13
	 */
	public void setDef13(String def13) {
		this.def13 = def13;
	}

	/**
	 * ���� �����ֶ�13 ��getter����.
	 * 
	 * @return String def13
	 */
	public String getDef13() {
		return def13;
	}

	/**
	 * ���� ���� ��setter����.
	 * 
	 * @param UFDouble
	 *            srkzl
	 */
	public void setSrkzl(UFDouble srkzl) {
		this.srkzl = srkzl;
	}

	/**
	 * ���� ���� ��getter����.
	 * 
	 * @return UFDouble srkzl
	 */
	public UFDouble getSrkzl() {
		return srkzl;
	}

	/**
	 * ���� �����ֶ�8 ��setter����.
	 * 
	 * @param String
	 *            def8
	 */
	public void setDef8(String def8) {
		this.def8 = def8;
	}

	/**
	 * ���� �����ֶ�8 ��getter����.
	 * 
	 * @return String def8
	 */
	public String getDef8() {
		return def8;
	}

	/**
	 * ���� �����ֶ�1 ��setter����.
	 * 
	 * @param UFDouble
	 *            def1
	 */
	public void setDef1(UFDouble def1) {
		this.def1 = def1;
	}

	/**
	 * ���� �����ֶ�1 ��getter����.
	 * 
	 * @return UFDouble def1
	 */
	public UFDouble getDef1() {
		return def1;
	}

	/**
	 * ���� ��ע ��setter����.
	 * 
	 * @param String
	 *            remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * ���� ��ע ��getter����.
	 * 
	 * @return String remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * ���� ��λPK ��setter����.
	 * 
	 * @param String
	 *            cspaceid
	 */
	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
	}

	/**
	 * ���� ��λPK ��getter����.
	 * 
	 * @return String cspaceid
	 */
	public String getCspaceid() {
		return cspaceid;
	}

	/**
	 * ���� �����ֶ�12 ��setter����.
	 * 
	 * @param String
	 *            def12
	 */
	public void setDef12(String def12) {
		this.def12 = def12;
	}

	/**
	 * ���� �����ֶ�12 ��getter����.
	 * 
	 * @return String def12
	 */
	public String getDef12() {
		return def12;
	}

	/**
	 * ���� �����ֶ�2 ��setter����.
	 * 
	 * @param UFDouble
	 *            def2
	 */
	public void setDef2(UFDouble def2) {
		this.def2 = def2;
	}

	/**
	 * ���� �����ֶ�2 ��getter����.
	 * 
	 * @return UFDouble def2
	 */
	public UFDouble getDef2() {
		return def2;
	}

	/**
	 * ���� ���� ��setter����.
	 * 
	 * @param String
	 *            pk_mdcrk
	 */
	public void setPk_mdcrk(String pk_mdcrk) {
		this.pk_mdcrk = pk_mdcrk;
	}

	/**
	 * ���� ���� ��getter����.
	 * 
	 * @return String pk_mdcrk
	 */
	public String getPk_mdcrk() {
		return pk_mdcrk;
	}

	/**
	 * ���� �Ƿ���� ��setter����.
	 * 
	 * @param UFBoolean
	 *            sfbj
	 */
	public void setSfbj(UFBoolean sfbj) {
		this.sfbj = sfbj;
	}

	/**
	 * ���� �Ƿ���� ��getter����.
	 * 
	 * @return UFBoolean sfbj
	 */
	public UFBoolean getSfbj() {
		return sfbj;
	}

	/**
	 * ���� �����ֶ�4 ��setter����.
	 * 
	 * @param UFBoolean
	 *            def4
	 */
	public void setDef4(UFBoolean def4) {
		this.def4 = def4;
	}

	/**
	 * ���� �����ֶ�4 ��getter����.
	 * 
	 * @return UFBoolean def4
	 */
	public UFBoolean getDef4() {
		return def4;
	}

	/**
	 * ���� �ֿ�PK ��setter����.
	 * 
	 * @param String
	 *            cwarehouseidb
	 */
	public void setCwarehouseidb(String cwarehouseidb) {
		this.cwarehouseidb = cwarehouseidb;
	}

	/**
	 * ���� �ֿ�PK ��getter����.
	 * 
	 * @return String cwarehouseidb
	 */
	public String getCwarehouseidb() {
		return cwarehouseidb;
	}

	/**
	 * ���� �����֯ ��setter����.
	 * 
	 * @param String
	 *            ccalbodyidb
	 */
	public void setCcalbodyidb(String ccalbodyidb) {
		this.ccalbodyidb = ccalbodyidb;
	}

	/**
	 * ���� �����֯ ��getter����.
	 * 
	 * @return String ccalbodyidb
	 */
	public String getCcalbodyidb() {
		return ccalbodyidb;
	}

	/**
	 * ���� �����ֶ�3 ��setter����.
	 * 
	 * @param UFDouble
	 *            def3
	 */
	public void setDef3(UFDouble def3) {
		this.def3 = def3;
	}

	/**
	 * ���� �����ֶ�3 ��getter����.
	 * 
	 * @return UFDouble def3
	 */
	public UFDouble getDef3() {
		return def3;
	}

	/**
	 * ���� �����ֶ�9 ��setter����.
	 * 
	 * @param String
	 *            def9
	 */
	public void setDef9(String def9) {
		this.def9 = def9;
	}

	/**
	 * ���� �����ֶ�9 ��getter����.
	 * 
	 * @return String def9
	 */
	public String getDef9() {
		return def9;
	}

	public void validate() throws ValidationException {
	}

	/**
	 * ȡ�ø�VO�����ֶ�.
	 * 
	 * @return java.lang.String
	 */
	public String getParentPKFieldName() {

		return null;

	}

	/**
	 * ȡ�ñ�����.
	 * 
	 * @return String
	 */
	public String getPKFieldName() {

		return "pk_mdcrk";

	}

	/**
	 * ���ر�����.
	 * 
	 * @return java.lang.String tableName
	 */
	public String getTableName() {
		return "nc_mdcrk";
	}

	/**
	 * ������ֵ�������ʾ����.
	 */
	public String getEntityName() {
		return "nc_mdcrk";
	}

	public MdcrkVO(String pk_mdcrk) {
		// Ϊ�����ֶθ�ֵ:
		this.pk_mdcrk = pk_mdcrk;
	}

	public String getPrimaryKey() {
		return pk_mdcrk;
	}

	public void setPrimaryKey(String pk_mdcrk) {
		this.pk_mdcrk = pk_mdcrk;
	}

}
