/**    Create By NCPlugin beta 0.1.   **/
package nc.vo.ic.md;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * 码单出入库表 nc_mdcrk 生成的VO对象
 * 
 * @author ThinkPad
 * @createtime 2010-09-06 17:53:35
 */
public class MdcrkVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	/** 备用字段5 */
	public UFBoolean def5;

	/** 米数 */
	public UFDouble md_meter;

	/** 公司 */
	public String pk_corp;

	/** 码单现存量表体PK */
	public String pk_mdxcl_b;

	/** 单据类型 */
	public String cbodybilltypecode;

	/** 质保证书号 */
	public String md_zlzsh;

	/** 出入库方向 */
	public Integer crkfx;

	/** 制单日期 */
	public UFDate dmakedate;

	/** 长度 */
	public UFDouble md_length;

	/** 支数 */
	public UFDouble srkzs = new UFDouble(0);

	/** dr */
	public Integer dr;

	/** 制单人 */
	public String voperatorid;

	/** 备用字段11 */
	public String def11;

	/** 备用字段14 */
	public String def14;

	/** 备用字段7 */
	public String def7;

	/** 备用字段6 */
	public String def6;

	/** ts */
	public UFDateTime ts;

	/** 实测厚*宽*长 */
	public String md_note;

	/** 备用字段15 */
	public String def15;

	/** 宽度 */
	public UFDouble md_width;

	/** 出入库单表体PK */
	public String cgeneralbid;

	/** 单据方向 */
	public Integer djfx;

	/** 备用字段10 */
	public String def10;

	/** 件编号 */
	public String jbh;

	/** 资源号 */
	public String md_zyh;

	/** 炉批号 */
	public String md_lph;

	/** 备用字段13 */
	public String def13;

	/** 重量 */
	public UFDouble srkzl = new UFDouble(0);

	/** 备用字段8 */
	public String def8;

	/** 备用字段1 */
	public UFDouble def1;

	/** 备注 */
	public String remark;

	/** 货位PK */
	public String cspaceid;

	/** 备用字段12 */
	public String def12;

	/** 备用字段2 */
	public UFDouble def2;

	/** 主键 */
	public String pk_mdcrk;

	/** 是否磅计 */
	public UFBoolean sfbj;

	/** 备用字段4 */
	public UFBoolean def4;

	/** 仓库PK */
	public String cwarehouseidb;

	/** 库存组织 */
	public String ccalbodyidb;

	/** 备用字段3 */
	public UFDouble def3;

	/** 备用字段9 */
	public String def9;
	
	/**add by ouyangzhb 2012-03-20自定义项*/
	/** 备用字段16 */
	public String def16;
	/** 备用字段17 */
	public String def17;
	/** 备用字段18 */
	public String def18;
	/** 备用字段19 */
	public String def19;
	/** 备用字段20 */
	public String def20;
	/** 是否磅计 */
	public UFBoolean sfgczl;
	
	public static final String SFGCZL = "sfgczl";
	public static final String DEF16 = "def16";
	public static final String DEF17 = "def17";
	public static final String DEF18= "def18";
	public static final String DEF19 = "def19";
	public static final String DEF20 = "def20";
	/**add by ouyangzhb 2012-03-20自定义项*/

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

	
	/**新增字段*/
	
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
	/**新增字段*/



	/**
	 * 属性 备用字段5 的setter方法.
	 * 
	 * @param UFBoolean
	 *            def5
	 */
	public void setDef5(UFBoolean def5) {
		this.def5 = def5;
	}

	/**
	 * 属性 备用字段5 的getter方法.
	 * 
	 * @return UFBoolean def5
	 */
	public UFBoolean getDef5() {
		return def5;
	}

	/**
	 * 属性 米数 的setter方法.
	 * 
	 * @param UFDouble
	 *            md_meter
	 */
	public void setMd_meter(UFDouble md_meter) {
		this.md_meter = md_meter;
	}

	/**
	 * 属性 米数 的getter方法.
	 * 
	 * @return UFDouble md_meter
	 */
	public UFDouble getMd_meter() {
		return md_meter;
	}

	/**
	 * 属性 公司 的setter方法.
	 * 
	 * @param String
	 *            pk_corp
	 */
	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	/**
	 * 属性 公司 的getter方法.
	 * 
	 * @return String pk_corp
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * 属性 码单现存量表体PK 的setter方法.
	 * 
	 * @param String
	 *            pk_mdxcl_b
	 */
	public void setPk_mdxcl_b(String pk_mdxcl_b) {
		this.pk_mdxcl_b = pk_mdxcl_b;
	}

	/**
	 * 属性 码单现存量表体PK 的getter方法.
	 * 
	 * @return String pk_mdxcl_b
	 */
	public String getPk_mdxcl_b() {
		return pk_mdxcl_b;
	}

	/**
	 * 属性 单据类型 的setter方法.
	 * 
	 * @param String
	 *            cbodybilltypecode
	 */
	public void setCbodybilltypecode(String cbodybilltypecode) {
		this.cbodybilltypecode = cbodybilltypecode;
	}

	/**
	 * 属性 单据类型 的getter方法.
	 * 
	 * @return String cbodybilltypecode
	 */
	public String getCbodybilltypecode() {
		return cbodybilltypecode;
	}

	/**
	 * 属性 质保证书号 的setter方法.
	 * 
	 * @param String
	 *            md_zlzsh
	 */
	public void setMd_zlzsh(String md_zlzsh) {
		this.md_zlzsh = md_zlzsh;
	}

	/**
	 * 属性 质保证书号 的getter方法.
	 * 
	 * @return String md_zlzsh
	 */
	public String getMd_zlzsh() {
		return md_zlzsh;
	}

	/**
	 * 属性 出入库方向 的setter方法.
	 * 
	 * @param Integer
	 *            crkfx
	 */
	public void setCrkfx(Integer crkfx) {
		this.crkfx = crkfx;
	}

	/**
	 * 属性 出入库方向 的getter方法.
	 * 
	 * @return Integer crkfx
	 */
	public Integer getCrkfx() {
		return crkfx;
	}

	/**
	 * 属性 制单日期 的setter方法.
	 * 
	 * @param UFDate
	 *            dmakedate
	 */
	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	/**
	 * 属性 制单日期 的getter方法.
	 * 
	 * @return UFDate dmakedate
	 */
	public UFDate getDmakedate() {
		return dmakedate;
	}

	/**
	 * 属性 长度 的setter方法.
	 * 
	 * @param UFDouble
	 *            md_length
	 */
	public void setMd_length(UFDouble md_length) {
		this.md_length = md_length;
	}

	/**
	 * 属性 长度 的getter方法.
	 * 
	 * @return UFDouble md_length
	 */
	public UFDouble getMd_length() {
		return md_length;
	}

	/**
	 * 属性 支数 的setter方法.
	 * 
	 * @param UFDouble
	 *            srkzs
	 */
	public void setSrkzs(UFDouble srkzs) {
		this.srkzs = srkzs;
	}

	/**
	 * 属性 支数 的getter方法.
	 * 
	 * @return UFDouble srkzs
	 */
	public UFDouble getSrkzs() {
		return srkzs;
	}

	/**
	 * 属性 dr 的setter方法.
	 * 
	 * @param Integer
	 *            dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}

	/**
	 * 属性 dr 的getter方法.
	 * 
	 * @return Integer dr
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性 制单人 的setter方法.
	 * 
	 * @param String
	 *            voperatorid
	 */
	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	/**
	 * 属性 制单人 的getter方法.
	 * 
	 * @return String voperatorid
	 */
	public String getVoperatorid() {
		return voperatorid;
	}

	/**
	 * 属性 备用字段11 的setter方法.
	 * 
	 * @param String
	 *            def11
	 */
	public void setDef11(String def11) {
		this.def11 = def11;
	}

	/**
	 * 属性 备用字段11 的getter方法.
	 * 
	 * @return String def11
	 */
	public String getDef11() {
		return def11;
	}

	/**
	 * 属性 备用字段14 的setter方法.
	 * 
	 * @param String
	 *            def14
	 */
	public void setDef14(String def14) {
		this.def14 = def14;
	}

	/**
	 * 属性 备用字段14 的getter方法.
	 * 
	 * @return String def14
	 */
	public String getDef14() {
		return def14;
	}

	/**
	 * 属性 备用字段7 的setter方法.
	 * 
	 * @param String
	 *            def7
	 */
	public void setDef7(String def7) {
		this.def7 = def7;
	}

	/**
	 * 属性 备用字段7 的getter方法.
	 * 
	 * @return String def7
	 */
	public String getDef7() {
		return def7;
	}

	/**
	 * 属性 备用字段6 的setter方法.
	 * 
	 * @param String
	 *            def6
	 */
	public void setDef6(String def6) {
		this.def6 = def6;
	}

	/**
	 * 属性 备用字段6 的getter方法.
	 * 
	 * @return String def6
	 */
	public String getDef6() {
		return def6;
	}

	/**
	 * 属性 ts 的setter方法.
	 * 
	 * @param UFDateTime
	 *            ts
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	/**
	 * 属性 ts 的getter方法.
	 * 
	 * @return UFDateTime ts
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性 实测厚*宽*长 的setter方法.
	 * 
	 * @param String
	 *            md_note
	 */
	public void setMd_note(String md_note) {
		this.md_note = md_note;
	}

	/**
	 * 属性 实测厚*宽*长 的getter方法.
	 * 
	 * @return String md_note
	 */
	public String getMd_note() {
		return md_note;
	}

	/**
	 * 属性 备用字段15 的setter方法.
	 * 
	 * @param String
	 *            def15
	 */
	public void setDef15(String def15) {
		this.def15 = def15;
	}

	/**
	 * 属性 备用字段15 的getter方法.
	 * 
	 * @return String def15
	 */
	public String getDef15() {
		return def15;
	}

	/**
	 * 属性 宽度 的setter方法.
	 * 
	 * @param UFDouble
	 *            md_width
	 */
	public void setMd_width(UFDouble md_width) {
		this.md_width = md_width;
	}

	/**
	 * 属性 宽度 的getter方法.
	 * 
	 * @return UFDouble md_width
	 */
	public UFDouble getMd_width() {
		return md_width;
	}

	/**
	 * 属性 出入库单表体PK 的setter方法.
	 * 
	 * @param String
	 *            cgeneralbid
	 */
	public void setCgeneralbid(String cgeneralbid) {
		this.cgeneralbid = cgeneralbid;
	}

	/**
	 * 属性 出入库单表体PK 的getter方法.
	 * 
	 * @return String cgeneralbid
	 */
	public String getCgeneralbid() {
		return cgeneralbid;
	}

	/**
	 * 属性 单据方向 的setter方法.
	 * 
	 * @param Integer
	 *            djfx
	 */
	public void setDjfx(Integer djfx) {
		this.djfx = djfx;
	}

	/**
	 * 属性 单据方向 的getter方法.
	 * 
	 * @return Integer djfx
	 */
	public Integer getDjfx() {
		return djfx;
	}

	/**
	 * 属性 备用字段10 的setter方法.
	 * 
	 * @param String
	 *            def10
	 */
	public void setDef10(String def10) {
		this.def10 = def10;
	}

	/**
	 * 属性 备用字段10 的getter方法.
	 * 
	 * @return String def10
	 */
	public String getDef10() {
		return def10;
	}

	/**
	 * 属性 件编号 的setter方法.
	 * 
	 * @param String
	 *            jbh
	 */
	public void setJbh(String jbh) {
		this.jbh = jbh;
	}

	/**
	 * 属性 件编号 的getter方法.
	 * 
	 * @return String jbh
	 */
	public String getJbh() {
		return jbh;
	}

	/**
	 * 属性 资源号 的setter方法.
	 * 
	 * @param String
	 *            md_zyh
	 */
	public void setMd_zyh(String md_zyh) {
		this.md_zyh = md_zyh;
	}

	/**
	 * 属性 资源号 的getter方法.
	 * 
	 * @return String md_zyh
	 */
	public String getMd_zyh() {
		return md_zyh;
	}

	/**
	 * 属性 炉批号 的setter方法.
	 * 
	 * @param String
	 *            md_lph
	 */
	public void setMd_lph(String md_lph) {
		this.md_lph = md_lph;
	}

	/**
	 * 属性 炉批号 的getter方法.
	 * 
	 * @return String md_lph
	 */
	public String getMd_lph() {
		return md_lph;
	}

	/**
	 * 属性 备用字段13 的setter方法.
	 * 
	 * @param String
	 *            def13
	 */
	public void setDef13(String def13) {
		this.def13 = def13;
	}

	/**
	 * 属性 备用字段13 的getter方法.
	 * 
	 * @return String def13
	 */
	public String getDef13() {
		return def13;
	}

	/**
	 * 属性 重量 的setter方法.
	 * 
	 * @param UFDouble
	 *            srkzl
	 */
	public void setSrkzl(UFDouble srkzl) {
		this.srkzl = srkzl;
	}

	/**
	 * 属性 重量 的getter方法.
	 * 
	 * @return UFDouble srkzl
	 */
	public UFDouble getSrkzl() {
		return srkzl;
	}

	/**
	 * 属性 备用字段8 的setter方法.
	 * 
	 * @param String
	 *            def8
	 */
	public void setDef8(String def8) {
		this.def8 = def8;
	}

	/**
	 * 属性 备用字段8 的getter方法.
	 * 
	 * @return String def8
	 */
	public String getDef8() {
		return def8;
	}

	/**
	 * 属性 备用字段1 的setter方法.
	 * 
	 * @param UFDouble
	 *            def1
	 */
	public void setDef1(UFDouble def1) {
		this.def1 = def1;
	}

	/**
	 * 属性 备用字段1 的getter方法.
	 * 
	 * @return UFDouble def1
	 */
	public UFDouble getDef1() {
		return def1;
	}

	/**
	 * 属性 备注 的setter方法.
	 * 
	 * @param String
	 *            remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 属性 备注 的getter方法.
	 * 
	 * @return String remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 属性 货位PK 的setter方法.
	 * 
	 * @param String
	 *            cspaceid
	 */
	public void setCspaceid(String cspaceid) {
		this.cspaceid = cspaceid;
	}

	/**
	 * 属性 货位PK 的getter方法.
	 * 
	 * @return String cspaceid
	 */
	public String getCspaceid() {
		return cspaceid;
	}

	/**
	 * 属性 备用字段12 的setter方法.
	 * 
	 * @param String
	 *            def12
	 */
	public void setDef12(String def12) {
		this.def12 = def12;
	}

	/**
	 * 属性 备用字段12 的getter方法.
	 * 
	 * @return String def12
	 */
	public String getDef12() {
		return def12;
	}

	/**
	 * 属性 备用字段2 的setter方法.
	 * 
	 * @param UFDouble
	 *            def2
	 */
	public void setDef2(UFDouble def2) {
		this.def2 = def2;
	}

	/**
	 * 属性 备用字段2 的getter方法.
	 * 
	 * @return UFDouble def2
	 */
	public UFDouble getDef2() {
		return def2;
	}

	/**
	 * 属性 主键 的setter方法.
	 * 
	 * @param String
	 *            pk_mdcrk
	 */
	public void setPk_mdcrk(String pk_mdcrk) {
		this.pk_mdcrk = pk_mdcrk;
	}

	/**
	 * 属性 主键 的getter方法.
	 * 
	 * @return String pk_mdcrk
	 */
	public String getPk_mdcrk() {
		return pk_mdcrk;
	}

	/**
	 * 属性 是否磅计 的setter方法.
	 * 
	 * @param UFBoolean
	 *            sfbj
	 */
	public void setSfbj(UFBoolean sfbj) {
		this.sfbj = sfbj;
	}

	/**
	 * 属性 是否磅计 的getter方法.
	 * 
	 * @return UFBoolean sfbj
	 */
	public UFBoolean getSfbj() {
		return sfbj;
	}

	/**
	 * 属性 备用字段4 的setter方法.
	 * 
	 * @param UFBoolean
	 *            def4
	 */
	public void setDef4(UFBoolean def4) {
		this.def4 = def4;
	}

	/**
	 * 属性 备用字段4 的getter方法.
	 * 
	 * @return UFBoolean def4
	 */
	public UFBoolean getDef4() {
		return def4;
	}

	/**
	 * 属性 仓库PK 的setter方法.
	 * 
	 * @param String
	 *            cwarehouseidb
	 */
	public void setCwarehouseidb(String cwarehouseidb) {
		this.cwarehouseidb = cwarehouseidb;
	}

	/**
	 * 属性 仓库PK 的getter方法.
	 * 
	 * @return String cwarehouseidb
	 */
	public String getCwarehouseidb() {
		return cwarehouseidb;
	}

	/**
	 * 属性 库存组织 的setter方法.
	 * 
	 * @param String
	 *            ccalbodyidb
	 */
	public void setCcalbodyidb(String ccalbodyidb) {
		this.ccalbodyidb = ccalbodyidb;
	}

	/**
	 * 属性 库存组织 的getter方法.
	 * 
	 * @return String ccalbodyidb
	 */
	public String getCcalbodyidb() {
		return ccalbodyidb;
	}

	/**
	 * 属性 备用字段3 的setter方法.
	 * 
	 * @param UFDouble
	 *            def3
	 */
	public void setDef3(UFDouble def3) {
		this.def3 = def3;
	}

	/**
	 * 属性 备用字段3 的getter方法.
	 * 
	 * @return UFDouble def3
	 */
	public UFDouble getDef3() {
		return def3;
	}

	/**
	 * 属性 备用字段9 的setter方法.
	 * 
	 * @param String
	 *            def9
	 */
	public void setDef9(String def9) {
		this.def9 = def9;
	}

	/**
	 * 属性 备用字段9 的getter方法.
	 * 
	 * @return String def9
	 */
	public String getDef9() {
		return def9;
	}

	public void validate() throws ValidationException {
	}

	/**
	 * 取得父VO主键字段.
	 * 
	 * @return java.lang.String
	 */
	public String getParentPKFieldName() {

		return null;

	}

	/**
	 * 取得表主键.
	 * 
	 * @return String
	 */
	public String getPKFieldName() {

		return "pk_mdcrk";

	}

	/**
	 * 返回表名称.
	 * 
	 * @return java.lang.String tableName
	 */
	public String getTableName() {
		return "nc_mdcrk";
	}

	/**
	 * 返回数值对象的显示名称.
	 */
	public String getEntityName() {
		return "nc_mdcrk";
	}

	public MdcrkVO(String pk_mdcrk) {
		// 为主键字段赋值:
		this.pk_mdcrk = pk_mdcrk;
	}

	public String getPrimaryKey() {
		return pk_mdcrk;
	}

	public void setPrimaryKey(String pk_mdcrk) {
		this.pk_mdcrk = pk_mdcrk;
	}

}
