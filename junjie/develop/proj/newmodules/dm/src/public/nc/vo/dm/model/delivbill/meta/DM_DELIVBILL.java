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
 * ���䵥����
 * @author ����
 *
 * 2008-8-8 ����01:18:20
 */
public class DM_DELIVBILL extends TableMeta {

  public final IFieldMeta CDELIVBILL_HID = new FieldMeta(this,
      "cdelivbill_hid", "���䵥����", null, null, SmartDomain.UFPK, false, null);

  public final IFieldMeta VBILLCODE = new FieldMeta(this, "vbillcode", "���ݺ�",
      null, null, SmartDomain.UFBillCode, false, null);

  public final IFieldMeta DBILLDATE = new FieldMeta(this, "dbilldate", "��������",
      null, null, SmartDomain.UFDate, false, null);

  @ReferenceDocID(table = BD_DELIVORG.class, field = "pk_delivorg")
  public final IFieldMeta CDELIVORGID = new FieldMeta(this, "cdelivorgid",
      "������֯����", null, null, SmartDomain.UFDocID, false, null);

  @ReferenceDocID(table = BD_CORP.class, field = "pk_corp")
  public final IFieldMeta PK_CORP = new FieldMeta(this, "pk_corp", "��˾����",
      null, null, SmartDomain.UFPkcorp, false, null);

  @ReferenceDocID(table = BD_SENDTYPE.class, field = "pk_sendtype")
  public final IFieldMeta CSENDTYPEID = new FieldMeta(this, "csendtypeid",
      "���䷽ʽ", null, null, SmartDomain.UFDocID, false, null);

  @ReferenceDocID(table = BD_ROUTE.class, field = "pk_route")
  public final IFieldMeta CROUTEID = new FieldMeta(this, "crouteid", "����·��",
      null, null, SmartDomain.UFDocID, false, null);

  @ReferenceDocID(table = BD_BILLTYPE.class, field = "pk_billtypecode")
  public final IFieldMeta CDELIVTYPE = new FieldMeta(this, "cdelivtype",
      "��������", null, null, SmartDomain.UFTransactionType, false, null);

  @ReferenceDocID(table = DM_TRANCUST.class, field = "pk_trancust")
  public final IFieldMeta CTRANCUSTID = new FieldMeta(this, "ctrancustid",
      "������", null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = BD_CUMANDOC.class, field = "pk_cumandoc")
  public final IFieldMeta CAPCUSTID = new FieldMeta(this, "capcustid", "Ӧ����Ʊ��",
      null, null, SmartDomain.UFPK, true, null);

  @ReferenceDocID(table = BD_CUBASDOC.class, field = "pk_cubasdoc")
  public final IFieldMeta CAPCUSTBASID = new FieldMeta(this, "capcustbasid",
      "Ӧ����Ʊ����������", null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta VNOTE = new FieldMeta(this, "vnote", "��ע", null,
      null, SmartDomain.UFNote, true, null);

  @NumValueRange(value = FBillStatusFlag.class)
  public final IFieldMeta FSTATUSFLAG = new FieldMeta(this, "fstatusflag",
      "����״̬", null, null, SmartDomain.UFFlag, false, FBillStatusFlag.FREE);

  @ReferenceDocID(table = SM_USER.class, field = "cuserid")
  public final IFieldMeta COPERATORID = new FieldMeta(this, "coperatorid",
      "�Ƶ���", null, null, SmartDomain.UFDocID, false, null);

  public final IFieldMeta DMAKEDATE = new FieldMeta(this, "dmakedate", "�Ƶ�����",
      null, null, SmartDomain.UFDate, false, null);

  public final IFieldMeta TMAKETIME = new FieldMeta(this, "tmaketime", "�Ƶ�ʱ��",
      null, null, SmartDomain.UFStamp, false, null);

  @ReferenceDocID(table = SM_USER.class, field = "cuserid")
  public final IFieldMeta CAUDITORID = new FieldMeta(this, "cauditorid", "������",
      null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta DAUDITDATE = new FieldMeta(this, "dauditdate",
      "��������", null, null, SmartDomain.UFDate, true, null);

  public final IFieldMeta TAUDITTIME = new FieldMeta(this, "taudittime",
      "����ʱ��", null, null, SmartDomain.UFStamp, true, null);

  @ReferenceDocID(table = SM_USER.class, field = "cuserid")
  public final IFieldMeta CLASTOPERATORID = new FieldMeta(this,
      "clastoperatorid", "����޸���", null, null, SmartDomain.UFDocID, false, null);

  public final IFieldMeta TLASTMAKETIME = new FieldMeta(this, "tlastmaketime",
      "����޸�ʱ��", null, null, SmartDomain.UFStamp, false, null);

  public final IFieldMeta IPRINTCOUNT = new FieldMeta(this, "iprintcount",
      "��ӡ����", null, null, SmartDomain.UFInt, true, Integer.valueOf(0));

  @ReferenceDocID(table = DM_VEHICLE.class, field = "pk_vehicle")
  public final IFieldMeta CVEHICLEID = new FieldMeta(this, "cvehicleid", "����",
      null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = DM_VEHICLETYPE.class, field = "pk_vehicletype")
  public final IFieldMeta CVEHICLETYPEID = new FieldMeta(this,
      "cvehicletypeid", "����", null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = DM_DRIVER.class, field = "pk_driver")
  public final IFieldMeta CDRIVERID = new FieldMeta(this, "cdriverid", "˾��",
      null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta VDRIVERPHONE = new FieldMeta(this, "vdriverphone",
      "˾���绰", null, null, SmartDomain.createStringDomain(28), true, null);

  @ReferenceDocID(table = BD_PSNDOC.class, field = "pk_psndoc")
  public final IFieldMeta CSUPERCARGOID = new FieldMeta(this, "csupercargoid", "Ѻ��Ա", null,
      null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta VSUPERCARGOPHONE = new FieldMeta(this, "vsupercargophone",
      "Ѻ��Ա�绰", null, null, SmartDomain.createStringDomain(28), true, null);

  public final IFieldMeta DDELIVDATE = new FieldMeta(this, "ddelivdate",
      "��������", null, null, SmartDomain.UFDate, false, null);

  public final IFieldMeta TDELIVTIME = new FieldMeta(this, "tdelivtime",
      "����ʱ��", null, null, SmartDomain.UFTime, true, null);

  public final IFieldMeta NTOTALNUM = new FieldMeta(this, "ntotalnum", "�ϼ�����",
      null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALASSISTNUM = new FieldMeta(this,
      "ntotalassistnum", "�ϼƸ�����", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALWEIGHT = new FieldMeta(this, "ntotalweight",
      "�ϼ�����", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALVOLUMN = new FieldMeta(this, "ntotalvolumn",
      "�ϼ����", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALPACKNUM = new FieldMeta(this, "ntotalpacknum",
      "�ϼư�װ����", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALPACKWEIGHT = new FieldMeta(this,
      "ntotalpackweight", "�ϼư�װ����", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALPACKVOLUMN = new FieldMeta(this,
      "ntotalpackvolumn", "�ϼư�װ���", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNNUM = new FieldMeta(this, "ntotalsignnum",
      "�ϼ�ǩ������", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NTOTALSIGNASSISTNUM = new FieldMeta(this,
      "ntotalsignassistnum", "�ϼ�ǩ�ո�����", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNWEIGHT = new FieldMeta(this,
      "ntotalsignweight", "�ϼ�ǩ������", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNVOLUMN = new FieldMeta(this,
      "ntotalsignvolumn", "�ϼ�ǩ�����", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNPACKNUM = new FieldMeta(this,
      "ntotalsignpacknum", "�ϼ�ǩ�հ�װ����", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NTOTALSIGNPACKWEIGHT = new FieldMeta(this,
      "ntotalsignpackweight", "�ϼ�ǩ�հ�װ����", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NTOTALSIGNPACKVOLUMN = new FieldMeta(this,
      "ntotalsignpackvolumn", "�ϼ�ǩ�հ�װ���", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NTOTALMNY = new FieldMeta(this, "ntotalmny", "�ϼƽ��",
      null, null, SmartDomain.UFMoney, true, null);

  @ReferenceDocID(table = BD_ADDRESS.class, field = "pk_address")
  public final IFieldMeta CSHORTESTADDRID = new FieldMeta(this,
      "cshortestaddrid", "��������ص�", null, null, SmartDomain.UFDocID, true, null);

  @ReferenceDocID(table = BD_ADDRESS.class, field = "pk_address")
  public final IFieldMeta CFARESTADDRID = new FieldMeta(this, "cfarestaddrid",
      "��Զ�����ص�", null, null, SmartDomain.UFDocID, true, null);

  public final IFieldMeta NFARESTMILE = new FieldMeta(this, "nfarestmile",
      "��Զ�������", null, null, SmartDomain.UFNumber, true, null);

  public final IFieldMeta NFATESTSPECIALMILE1 = new FieldMeta(this,
      "nfatestspecialmile1", "����·��1��Զ�������", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NFATESTSPECIALMILE2 = new FieldMeta(this,
      "nfatestspecialmile2", "����·��2��Զ�������", null, null, SmartDomain.UFNumber, true,
      null);

  public final IFieldMeta NFATESTSPECIALMILE3 = new FieldMeta(this,
      "nfatestspecialmile3", "����·��3��Զ�������", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta NFATESTSPECIALMILE4 = new FieldMeta(this,
      "nfatestspecialmile4", "����·��4��Զ�������", null, null, SmartDomain.UFNumber,
      true, null);

  public final IFieldMeta VWEIGHTUNITNAME = new FieldMeta(this,
      "vweightunitname", "������λ", null, null,
      SmartDomain.createStringDomain(30), true, null);

  public final IFieldMeta VVOLUMNUNITNAME = new FieldMeta(this,
      "vvolumnunitname", "�����λ", null, null,
      SmartDomain.createStringDomain(30), true, null);

  public final IFieldMeta BAPSETTLEDFLAG = new FieldMeta(this,
      "bapsettledflag", "������Ӧ���˷ѽ��㵥", null, null, SmartDomain.UFBoolean, false,
      UFBoolean.FALSE);

  public final IFieldMeta BARSETTLEDFLAG = new FieldMeta(this,
      "barsettledflag", "������Ӧ���˷ѽ��㵥", null, null, SmartDomain.UFBoolean, false,
      UFBoolean.FALSE);

  public final IFieldMeta BMISSIONBILLFLAG = new FieldMeta(this,
      "bmissionbillflag", "����������", null, null, SmartDomain.UFBoolean, false,
      UFBoolean.FALSE);

  public final IFieldMeta NAPTOTALVERIFYMNY = new FieldMeta(this,
      "naptotalverifymny", "�ۼ�Ӧ���������", null, null, SmartDomain.UFMoney, true,
      null);

  public final IFieldMeta NARTOTALVERIFYMNY = new FieldMeta(this,
      "nartotalverifymny", "�ۼ�Ӧ�պ������", null, null, SmartDomain.UFMoney, true,
      null);

  public final IFieldMeta VDEF1 = new FieldMeta(this, "vdef1", "�Զ�����1", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF2 = new FieldMeta(this, "vdef2", "�Զ�����2", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF3 = new FieldMeta(this, "vdef3", "�Զ�����3", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF4 = new FieldMeta(this, "vdef4", "�Զ�����4", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF5 = new FieldMeta(this, "vdef5", "�Զ�����5", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF6 = new FieldMeta(this, "vdef6", "�Զ�����6", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF7 = new FieldMeta(this, "vdef7", "�Զ�����7", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF8 = new FieldMeta(this, "vdef8", "�Զ�����8", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF9 = new FieldMeta(this, "vdef9", "�Զ�����9", null,
      null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF10 = new FieldMeta(this, "vdef10", "�Զ�����10",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF11 = new FieldMeta(this, "vdef11", "�Զ�����11",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF12 = new FieldMeta(this, "vdef12", "�Զ�����12",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF13 = new FieldMeta(this, "vdef13", "�Զ�����13",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF14 = new FieldMeta(this, "vdef14", "�Զ�����14",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF15 = new FieldMeta(this, "vdef15", "�Զ�����15",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF16 = new FieldMeta(this, "vdef16", "�Զ�����16",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF17 = new FieldMeta(this, "vdef17", "�Զ�����17",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF18 = new FieldMeta(this, "vdef18", "�Զ�����18",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF19 = new FieldMeta(this, "vdef19", "�Զ�����19",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta VDEF20 = new FieldMeta(this, "vdef20", "�Զ�����20",
      null, null, SmartDomain.UFDef, true, null);

  public final IFieldMeta PK_DEFDOC1 = new FieldMeta(this, "pk_defdoc1",
      "�Զ���������1", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC2 = new FieldMeta(this, "pk_defdoc2",
      "�Զ���������2", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC3 = new FieldMeta(this, "pk_defdoc3",
      "�Զ���������3", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC4 = new FieldMeta(this, "pk_defdoc4",
      "�Զ���������4", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC5 = new FieldMeta(this, "pk_defdoc5",
      "�Զ���������5", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC6 = new FieldMeta(this, "pk_defdoc6",
      "�Զ���������6", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC7 = new FieldMeta(this, "pk_defdoc7",
      "�Զ���������7", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC8 = new FieldMeta(this, "pk_defdoc8",
      "�Զ���������8", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC9 = new FieldMeta(this, "pk_defdoc9",
      "�Զ���������9", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC10 = new FieldMeta(this, "pk_defdoc10",
      "�Զ���������10", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC11 = new FieldMeta(this, "pk_defdoc11",
      "�Զ���������11", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC12 = new FieldMeta(this, "pk_defdoc12",
      "�Զ���������12", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC13 = new FieldMeta(this, "pk_defdoc13",
      "�Զ���������13", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC14 = new FieldMeta(this, "pk_defdoc14",
      "�Զ���������14", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC15 = new FieldMeta(this, "pk_defdoc15",
      "�Զ���������15", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC16 = new FieldMeta(this, "pk_defdoc16",
      "�Զ���������16", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC17 = new FieldMeta(this, "pk_defdoc17",
      "�Զ���������17", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC18 = new FieldMeta(this, "pk_defdoc18",
      "�Զ���������18", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC19 = new FieldMeta(this, "pk_defdoc19",
      "�Զ���������19", null, null, SmartDomain.UFDefPK, true, null);

  public final IFieldMeta PK_DEFDOC20 = new FieldMeta(this, "pk_defdoc20",
      "�Զ���������20", null, null, SmartDomain.UFDefPK, true, null);
  //lumzh 2012-08-18 �����Ƿ����ݹ��ֶ�
  public final IFieldMeta ISESTIMATE = new FieldMeta(this,
	      "isestimate", "�Ƿ����ݹ�", null, null, SmartDomain.UFBoolean, true,
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
