package nc.vo.dm.model.delivbill.meta;

import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pattern.data.type.SmartDomain;
import nc.vo.scm.pattern.domain.scm.basedoc.BD_DELIVORG;
import nc.vo.scm.pattern.domain.scm.basedoc.BD_ROUTE;
import nc.vo.scm.pattern.domain.scm.basedoc.DM_DRIVER;
import nc.vo.scm.pattern.domain.scm.basedoc.DM_TRANCUST;
import nc.vo.scm.pattern.domain.scm.basedoc.DM_VEHICLE;
import nc.vo.scm.pattern.domain.scm.basedoc.DM_VEHICLETYPE;
import nc.vo.scm.pattern.domain.scm.enumeration.FBillStatusFlag;
import nc.vo.scm.pattern.domain.uap.basedoc.BD_ADDRESS;
import nc.vo.scm.pattern.domain.uap.basedoc.BD_BILLTYPE;
import nc.vo.scm.pattern.domain.uap.basedoc.BD_CORP;
import nc.vo.scm.pattern.domain.uap.basedoc.BD_CUBASDOC;
import nc.vo.scm.pattern.domain.uap.basedoc.BD_CUMANDOC;
import nc.vo.scm.pattern.domain.uap.basedoc.BD_PSNDOC;
import nc.vo.scm.pattern.domain.uap.basedoc.BD_SENDTYPE;
import nc.vo.scm.pattern.domain.uap.basedoc.SM_USER;
import nc.vo.scm.pattern.model.annotation.table.NumValueRange;
import nc.vo.scm.pattern.model.annotation.table.ReferenceDocID;
import nc.vo.scm.pattern.model.meta.table.FieldMeta;
import nc.vo.scm.pattern.model.meta.table.IFieldMeta;
import nc.vo.scm.pattern.model.meta.table.TableMeta;

/**
 * 运输单主表
 * @author 钟鸣
 *
 * 2008-8-8 下午01:18:20
 */
public class DM_DELIVBILL extends TableMeta {

  public final IFieldMeta CDELIVBILL_HID = new FieldMeta(this,
      "cdelivbill_hid", "运输单主键", null, null, SmartDomain.UFPK, false, null);

  public final IFieldMeta VBILLCODE = new FieldMeta(this, "vbillcode", "单据号",
      null, null, SmartDomain.UFBillCode, false, null);

  public final IFieldMeta DBILLDATE = new FieldMeta(this, "dbilldate", "单据日期",
      null, null, SmartDomain.UFDate, false, null);

  @ReferenceDocID(table = BD_DELIVORG.class, field = "pk_delivorg")
  public final IFieldMeta CDELIVORGID = new FieldMeta(this, "cdelivorgid",
      "运输组织主键", null, null, SmartDomain.UFDocID, false, null);

  @ReferenceDocID(table = BD_CORP.class, field = "pk_corp")
  public final IFieldMeta PK_CORP = new FieldMeta(this, "pk_corp", "公司主键",
      null, null, SmartDomain.UFPkcorp, false, null);

  @ReferenceDocID(table = BD_SENDTYPE.class, field = "pk_sendtype")
  public final IFieldMeta CSENDTYPEID = new FieldMeta(this, "csendtypeid",
      "运输方式", null, null, SmartDomain.UFDocID, false, null);

  @ReferenceDocID(table = BD_ROUTE.class, field = "pk_route")
  public final IFieldMeta CROUTEID = new FieldMeta(this, "crouteid", "运输路线",
      null, null, SmartDomain.UFDocID, false, null);

  @ReferenceDocID(table = BD_BILLTYPE.class, field = "pk_billtypecode")
  public final IFieldMeta CDELIVTYPE = new FieldMeta(this, "cdelivtype",
      "运输类型", null, null, SmartDomain.UFTransactionType, false, null);

  @ReferenceDocID(table = DM_TRANCUST.class, field = "pk_trancust")
  public final IFieldMeta CTRANCUSTID = new FieldMeta(this, "ctrancustid",
      "承运商", null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = BD_CUMANDOC.class, field = "pk_cumandoc")
  public final IFieldMeta CAPCUSTID = new FieldMeta(this, "capcustid", "应付开票方",
      null, null, SmartDomain.UFPK, true, null);

  @ReferenceDocID(table = BD_CUBASDOC.class, field = "pk_cubasdoc")
  public final IFieldMeta CAPCUSTBASID = new FieldMeta(this, "capcustbasid",
      "应付开票方基本档案", null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta VNOTE = new FieldMeta(this, "vnote", "备注", null,
      null, SmartDomain.UFNote, true, null);

  @NumValueRange(value = FBillStatusFlag.class)
  public final IFieldMeta FSTATUSFLAG = new FieldMeta(this, "fstatusflag",
      "单据状态", null, null, SmartDomain.UFFlag, false, FBillStatusFlag.FREE);

  @ReferenceDocID(table = SM_USER.class, field = "cuserid")
  public final IFieldMeta COPERATORID = new FieldMeta(this, "coperatorid",
      "制单人", null, null, SmartDomain.UFDocID, false, null);

  public final IFieldMeta DMAKEDATE = new FieldMeta(this, "dmakedate", "制单日期",
      null, null, SmartDomain.UFDate, false, null);

  public final IFieldMeta TMAKETIME = new FieldMeta(this, "tmaketime", "制单时间",
      null, null, SmartDomain.UFStamp, false, null);

  @ReferenceDocID(table = SM_USER.class, field = "cuserid")
  public final IFieldMeta CAUDITORID = new FieldMeta(this, "cauditorid", "审批人",
      null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta DAUDITDATE = new FieldMeta(this, "dauditdate",
      "审批日期", null, null, SmartDomain.UFDate, true, null);

  public final IFieldMeta TAUDITTIME = new FieldMeta(this, "taudittime",
      "审批时间", null, null, SmartDomain.UFStamp, true, null);

  @ReferenceDocID(table = SM_USER.class, field = "cuserid")
  public final IFieldMeta CLASTOPERATORID = new FieldMeta(this,
      "clastoperatorid", "最后修改人", null, null, SmartDomain.UFDocID, false, null);

  public final IFieldMeta TLASTMAKETIME = new FieldMeta(this, "tlastmaketime",
      "最后修改时间", null, null, SmartDomain.UFStamp, false, null);

  public final IFieldMeta IPRINTCOUNT = new FieldMeta(this, "iprintcount",
      "打印次数", null, null, SmartDomain.UFInt, true, Integer.valueOf(0));

  @ReferenceDocID(table = DM_VEHICLE.class, field = "pk_vehicle")
  public final IFieldMeta CVEHICLEID = new FieldMeta(this, "cvehicleid", "车辆",
      null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = DM_VEHICLETYPE.class, field = "pk_vehicletype")
  public final IFieldMeta CVEHICLETYPEID = new FieldMeta(this,
      "cvehicletypeid", "车型", null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = DM_DRIVER.class, field = "pk_driver")
  public final IFieldMeta CDRIVERID = new FieldMeta(this, "cdriverid", "司机",
      null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta VDRIVERPHONE = new FieldMeta(this, "vdriverphone",
      "司机电话", null, null, SmartDomain.createStringDomain(28), true, null);

  @ReferenceDocID(table = BD_PSNDOC.class, field = "pk_psndoc")
  public final IFieldMeta CSUPERCARGOID = new FieldMeta(this, "csupercargoid", "押运员", null,
      null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta VSUPERCARGOPHONE = new FieldMeta(this, "vsupercargophone",
      "押运员电话", null, null, SmartDomain.createStringDomain(28), true, null);

  public final IFieldMeta DDELIVDATE = new FieldMeta(this, "ddelivdate",
      "运输日期", null, null, SmartDomain.UFDate, false, null);

  public final IFieldMeta TDELIVTIME = new FieldMeta(this, "tdelivtime",
      "运输时间", null, null, SmartDomain.UFTime, true, null);

  public final IFieldMeta NTOTALNUM = new FieldMeta(this, "ntotalnum", "合计数量",
      null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALASSISTNUM = new FieldMeta(this,
      "ntotalassistnum", "合计辅数量", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALWEIGHT = new FieldMeta(this, "ntotalweight",
      "合计重量", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALVOLUMN = new FieldMeta(this, "ntotalvolumn",
      "合计体积", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALPACKNUM = new FieldMeta(this, "ntotalpacknum",
      "合计包装件数", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALPACKWEIGHT = new FieldMeta(this,
      "ntotalpackweight", "合计包装重量", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALPACKVOLUMN = new FieldMeta(this,
      "ntotalpackvolumn", "合计包装体积", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNNUM = new FieldMeta(this, "ntotalsignnum",
      "合计签收数量", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALSIGNASSISTNUM = new FieldMeta(this,
      "ntotalsignassistnum", "合计签收辅数量", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNWEIGHT = new FieldMeta(this,
      "ntotalsignweight", "合计签收重量", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNVOLUMN = new FieldMeta(this,
      "ntotalsignvolumn", "合计签收体积", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNPACKNUM = new FieldMeta(this,
      "ntotalsignpacknum", "合计签收包装件数", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNPACKWEIGHT = new FieldMeta(this,
      "ntotalsignpackweight", "合计签收包装重量", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NTOTALSIGNPACKVOLUMN = new FieldMeta(this,
      "ntotalsignpackvolumn", "合计签收包装体积", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NTOTALMNY = new FieldMeta(this, "ntotalmny", "合计金额",
      null, null, SmartDomain.UFMoney, true, null);

  @ReferenceDocID(table = BD_ADDRESS.class, field = "pk_address")
  public final IFieldMeta CSHORTESTADDRID = new FieldMeta(this,
      "cshortestaddrid", "最近发货地点", null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = BD_ADDRESS.class, field = "pk_address")
  public final IFieldMeta CFARESTADDRID = new FieldMeta(this, "cfarestaddrid",
      "最远到货地点", null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta NFARESTMILE = new FieldMeta(this, "nfarestmile",
      "最远运输里程", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NFATESTSPECIALMILE1 = new FieldMeta(this,
      "nfatestspecialmile1", "特殊路线1最远运输里程", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NFATESTSPECIALMILE2 = new FieldMeta(this,
      "nfatestspecialmile2", "特殊路线2最远运输里程", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NFATESTSPECIALMILE3 = new FieldMeta(this,
      "nfatestspecialmile3", "特殊路线3最远运输里程", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NFATESTSPECIALMILE4 = new FieldMeta(this,
      "nfatestspecialmile4", "特殊路线4最远运输里程", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta VWEIGHTUNITNAME = new FieldMeta(this,
      "vweightunitname", "重量单位", null, null,
      SmartDomain.createStringDomain(30), true, null);

  public final IFieldMeta VVOLUMNUNITNAME = new FieldMeta(this,
      "vvolumnunitname", "体积单位", null, null,
      SmartDomain.createStringDomain(30), true, null);

  public final IFieldMeta BAPSETTLEDFLAG = new FieldMeta(this,
      "bapsettledflag", "已生成应付运费结算单", null, null, SmartDomain.UFBoolean, false,
      UFBoolean.FALSE);

  public final IFieldMeta BARSETTLEDFLAG = new FieldMeta(this,
      "barsettledflag", "已生成应收运费结算单", null, null, SmartDomain.UFBoolean, false,
      UFBoolean.FALSE);

  public final IFieldMeta BMISSIONBILLFLAG = new FieldMeta(this,
      "bmissionbillflag", "已生成任务单", null, null, SmartDomain.UFBoolean, false,
      UFBoolean.FALSE);

  public final IFieldMeta NAPTOTALVERIFYMNY = new FieldMeta(this,
      "naptotalverifymny", "累计应付核销金额", null, null, SmartDomain.UFMoney, true,
      null);

  public final IFieldMeta NARTOTALVERIFYMNY = new FieldMeta(this,
      "nartotalverifymny", "累计应收核销金额", null, null, SmartDomain.UFMoney, true,
      null);

  public final IFieldMeta VDEF1 = new FieldMeta(this, "vdef1", "自定义项1", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF2 = new FieldMeta(this, "vdef2", "自定义项2", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF3 = new FieldMeta(this, "vdef3", "自定义项3", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF4 = new FieldMeta(this, "vdef4", "自定义项4", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF5 = new FieldMeta(this, "vdef5", "自定义项5", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF6 = new FieldMeta(this, "vdef6", "自定义项6", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF7 = new FieldMeta(this, "vdef7", "自定义项7", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF8 = new FieldMeta(this, "vdef8", "自定义项8", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF9 = new FieldMeta(this, "vdef9", "自定义项9", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF10 = new FieldMeta(this, "vdef10", "自定义项10",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF11 = new FieldMeta(this, "vdef11", "自定义项11",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF12 = new FieldMeta(this, "vdef12", "自定义项12",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF13 = new FieldMeta(this, "vdef13", "自定义项13",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF14 = new FieldMeta(this, "vdef14", "自定义项14",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF15 = new FieldMeta(this, "vdef15", "自定义项15",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF16 = new FieldMeta(this, "vdef16", "自定义项16",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF17 = new FieldMeta(this, "vdef17", "自定义项17",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF18 = new FieldMeta(this, "vdef18", "自定义项18",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF19 = new FieldMeta(this, "vdef19", "自定义项19",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF20 = new FieldMeta(this, "vdef20", "自定义项20",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta PK_DEFDOC1 = new FieldMeta(this, "pk_defdoc1",
      "自定义项主键1", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC2 = new FieldMeta(this, "pk_defdoc2",
      "自定义项主键2", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC3 = new FieldMeta(this, "pk_defdoc3",
      "自定义项主键3", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC4 = new FieldMeta(this, "pk_defdoc4",
      "自定义项主键4", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC5 = new FieldMeta(this, "pk_defdoc5",
      "自定义项主键5", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC6 = new FieldMeta(this, "pk_defdoc6",
      "自定义项主键6", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC7 = new FieldMeta(this, "pk_defdoc7",
      "自定义项主键7", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC8 = new FieldMeta(this, "pk_defdoc8",
      "自定义项主键8", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC9 = new FieldMeta(this, "pk_defdoc9",
      "自定义项主键9", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC10 = new FieldMeta(this, "pk_defdoc10",
      "自定义项主键10", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC11 = new FieldMeta(this, "pk_defdoc11",
      "自定义项主键11", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC12 = new FieldMeta(this, "pk_defdoc12",
      "自定义项主键12", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC13 = new FieldMeta(this, "pk_defdoc13",
      "自定义项主键13", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC14 = new FieldMeta(this, "pk_defdoc14",
      "自定义项主键14", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC15 = new FieldMeta(this, "pk_defdoc15",
      "自定义项主键15", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC16 = new FieldMeta(this, "pk_defdoc16",
      "自定义项主键16", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC17 = new FieldMeta(this, "pk_defdoc17",
      "自定义项主键17", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC18 = new FieldMeta(this, "pk_defdoc18",
      "自定义项主键18", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC19 = new FieldMeta(this, "pk_defdoc19",
      "自定义项主键19", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC20 = new FieldMeta(this, "pk_defdoc20",
      "自定义项主键20", null, null, SmartDomain.UFDefPK, true, null);
  //lumzh 2012-08-18 增加是否已暂估字段
  public final IFieldMeta ISESTIMATE = new FieldMeta(this,
	      "isestimate", "是否已暂估", null, null, SmartDomain.UFBoolean, true,
	      UFBoolean.TRUE);
  public DM_DELIVBILL() {
  }

  public String getName() {
    return "dm_delivbill";
  }

  public IFieldMeta getPrimaryKey() {
    return this.CDELIVBILL_HID;
  }

}
