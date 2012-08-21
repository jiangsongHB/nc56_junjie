package nc.vo.dm.model.delivbill.entity;

import nc.vo.dm.model.delivbill.meta.DelivBillHeadVOMeta;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.scm.pattern.domain.scm.enumeration.FBillStatusFlag;
import nc.vo.scm.pattern.model.entity.vo.SmartVO;
import nc.vo.scm.pattern.model.meta.entity.vo.ISmartVOMeta;
import nc.vo.scm.pattern.model.meta.entity.vo.SmartVOMetaFactory;

/**
 * 运输单表头VO
 * @author 钟鸣
 *
 * 2008-8-9 下午11:11:50
 */
public class DelivBillHeadVO extends SmartVO {

  private static final long serialVersionUID = -4903771612560088555L;

  public DelivBillHeadVO() {
  }

  public ISmartVOMeta getVOMeta() {
    DelivBillHeadVOMeta meta = (DelivBillHeadVOMeta) SmartVOMetaFactory
        .getInstance().get(DelivBillHeadVOMeta.class).getVOMeta();
    return meta;
  }
  
  public String getPrimaryKey(){
    return getCdelivbill_hid();
  }

  public UFBoolean getBapsettledflag() {
    return (UFBoolean) this.getAttributeValue("bapsettledflag");
  }

  public void setBapsettledflag(UFBoolean bapsettledflag) {
    this.setAttributeValue("bapsettledflag", bapsettledflag);
  }

  public UFBoolean getBarsettledflag() {
    return (UFBoolean) this.getAttributeValue("barsettledflag");
  }

  public void setBarsettledflag(UFBoolean barsettledflag) {
    this.setAttributeValue("barsettledflag", barsettledflag);
  }

  public UFBoolean getBmissionbillflag() {
    return (UFBoolean) this.getAttributeValue("bmissionbillflag");
  }

  public void setBmissionbillflag(UFBoolean bmissionbillflag) {
    this.setAttributeValue("bmissionbillflag", bmissionbillflag);
  }

  public String getCapcustbasid() {
    return (String) this.getAttributeValue("capcustbasid");
  }

  public void setCapcustbasid(String capcustbasid) {
    this.setAttributeValue("capcustbasid", capcustbasid);
  }

  public String getCapcustid() {
    return (String) this.getAttributeValue("capcustid");
  }

  public void setCapcustid(String capcustid) {
    this.setAttributeValue("capcustid", capcustid);
  }

  public String getCapcustname() {
    return (String) this.getAttributeValue("capcustname");
  }

  public void setCapcustname(String capcustname) {
    this.setAttributeValue("capcustname", capcustname);
  }

  public String getCauditorid() {
    return (String) this.getAttributeValue("cauditorid");
  }

  public void setCauditorid(String cauditorid) {
    this.setAttributeValue("cauditorid", cauditorid);
  }

  public String getCauditorname() {
    return (String) this.getAttributeValue("cauditorname");
  }

  public void setCauditorname(String cauditorname) {
    this.setAttributeValue("cauditorname", cauditorname);
  }

  public String getCcorpname() {
    return (String) this.getAttributeValue("ccorpname");
  }

  public void setCcorpname(String ccorpname) {
    this.setAttributeValue("ccorpname", ccorpname);
  }

  public String getCdelivbill_hid() {
    return (String) this.getAttributeValue("cdelivbill_hid");
  }

  public void setCdelivbill_hid(String cdelivbill_hid) {
    this.setAttributeValue("cdelivbill_hid", cdelivbill_hid);
  }

  public String getCdelivorgid() {
    return (String) this.getAttributeValue("cdelivorgid");
  }

  public void setCdelivorgid(String cdelivorgid) {
    this.setAttributeValue("cdelivorgid", cdelivorgid);
  }

  public String getCdelivorgname() {
    return (String) this.getAttributeValue("cdelivorgname");
  }

  public void setCdelivorgname(String cdelivorgname) {
    this.setAttributeValue("cdelivorgname", cdelivorgname);
  }

  public String getCdelivtype() {
    return (String) this.getAttributeValue("cdelivtype");
  }

  public void setCdelivtype(String cdelivtype) {
    this.setAttributeValue("cdelivtype", cdelivtype);
  }

  public String getCdelivtypename() {
    return (String) this.getAttributeValue("cdelivtypename");
  }

  public void setCdelivtypename(String cdelivtypename) {
    this.setAttributeValue("cdelivtypename", cdelivtypename);
  }

  public String getCdriverid() {
    return (String) this.getAttributeValue("cdriverid");
  }

  public void setCdriverid(String cdriverid) {
    this.setAttributeValue("cdriverid", cdriverid);
  }

  public String getCdrivername() {
    return (String) this.getAttributeValue("cdrivername");
  }

  public void setCdrivername(String cdrivername) {
    this.setAttributeValue("cdrivername", cdrivername);
  }

  public String getCfarestaddrid() {
    return (String) this.getAttributeValue("cfarestaddrid");
  }

  public void setCfarestaddrid(String cfarestaddrid) {
    this.setAttributeValue("cfarestaddrid", cfarestaddrid);
  }

  public String getCfarestaddrname() {
    return (String) this.getAttributeValue("cfarestaddrname");
  }

  public void setCfarestaddrname(String cfarestaddrname) {
    this.setAttributeValue("cfarestaddrname", cfarestaddrname);
  }

  public String getClastoperatorid() {
    return (String) this.getAttributeValue("clastoperatorid");
  }

  public void setClastoperatorid(String clastoperatorid) {
    this.setAttributeValue("clastoperatorid", clastoperatorid);
  }

  public String getClastoperatorname() {
    return (String) this.getAttributeValue("clastoperatorname");
  }

  public void setClastoperatorname(String clastoperatorname) {
    this.setAttributeValue("clastoperatorname", clastoperatorname);
  }

  public String getCoperatorid() {
    return (String) this.getAttributeValue("coperatorid");
  }

  public void setCoperatorid(String coperatorid) {
    this.setAttributeValue("coperatorid", coperatorid);
  }

  public String getCoperatorname() {
    return (String) this.getAttributeValue("coperatorname");
  }

  public void setCoperatorname(String coperatorname) {
    this.setAttributeValue("coperatorname", coperatorname);
  }

  public String getCrouteid() {
    return (String) this.getAttributeValue("crouteid");
  }

  public void setCrouteid(String crouteid) {
    this.setAttributeValue("crouteid", crouteid);
  }

  public String getCroutename() {
    return (String) this.getAttributeValue("croutename");
  }

  public void setCroutename(String croutename) {
    this.setAttributeValue("croutename", croutename);
  }

  public String getCsupercargoid() {
    return (String) this.getAttributeValue("csupercargoid");
  }

  public void setCsupercargoid(String csupercargoid) {
    this.setAttributeValue("csupercargoid", csupercargoid);
  }

  public String getCsupercargoname() {
    return (String) this.getAttributeValue("csupercargoname");
  }

  public void setCsupercargoname(String csupercargoname) {
    this.setAttributeValue("csupercargoname", csupercargoname);
  }

  public String getCsendtypeid() {
    return (String) this.getAttributeValue("csendtypeid");
  }

  public void setCsendtypeid(String csendtypeid) {
    this.setAttributeValue("csendtypeid", csendtypeid);
  }

  public String getCsendtypename() {
    return (String) this.getAttributeValue("csendtypename");
  }

  public void setCsendtypename(String csendtypename) {
    this.setAttributeValue("csendtypename", csendtypename);
  }

  public String getCshortestaddrid() {
    return (String) this.getAttributeValue("cshortestaddrid");
  }

  public void setCshortestaddrid(String cshortestaddrid) {
    this.setAttributeValue("cshortestaddrid", cshortestaddrid);
  }

  public String getCshortestaddrname() {
    return (String) this.getAttributeValue("cshortestaddrname");
  }

  public void setCshortestaddrname(String cshortestaddrname) {
    this.setAttributeValue("cshortestaddrname", cshortestaddrname);
  }

  public String getCstatusflagname() {
    return (String) this.getAttributeValue("cstatusflagname");
  }

  public void setCstatusflagname(String cstatusflagname) {
    this.setAttributeValue("cstatusflagname", cstatusflagname);
  }

  public String getCtrancustid() {
    return (String) this.getAttributeValue("ctrancustid");
  }

  public void setCtrancustid(String ctrancustid) {
    this.setAttributeValue("ctrancustid", ctrancustid);
  }

  public String getCtrancustname() {
    return (String) this.getAttributeValue("ctrancustname");
  }

  public void setCtrancustname(String ctrancustname) {
    this.setAttributeValue("ctrancustname", ctrancustname);
  }

  public String getCvehicleid() {
    return (String) this.getAttributeValue("cvehicleid");
  }

  public void setCvehicleid(String cvehicleid) {
    this.setAttributeValue("cvehicleid", cvehicleid);
  }

  public String getCvehiclename() {
    return (String) this.getAttributeValue("cvehiclename");
  }

  public void setCvehiclename(String cvehiclename) {
    this.setAttributeValue("cvehiclename", cvehiclename);
  }

  public String getCvehicletypeid() {
    return (String) this.getAttributeValue("cvehicletypeid");
  }

  public void setCvehicletypeid(String cvehicletypeid) {
    this.setAttributeValue("cvehicletypeid", cvehicletypeid);
  }

  public String getCvehicletypename() {
    return (String) this.getAttributeValue("cvehicletypename");
  }

  public void setCvehicletypename(String cvehicletypename) {
    this.setAttributeValue("cvehicletypename", cvehicletypename);
  }

  public UFDate getDauditdate() {
    return (UFDate) this.getAttributeValue("dauditdate");
  }

  public void setDauditdate(UFDate dauditdate) {
    this.setAttributeValue("dauditdate", dauditdate);
  }

  public UFDate getDbilldate() {
    return (UFDate) this.getAttributeValue("dbilldate");
  }

  public void setDbilldate(UFDate dbilldate) {
    this.setAttributeValue("dbilldate", dbilldate);
  }

  public UFDate getDdelivdate() {
    return (UFDate) this.getAttributeValue("ddelivdate");
  }

  public void setDdelivdate(UFDate ddelivdate) {
    this.setAttributeValue("ddelivdate", ddelivdate);
  }

  public UFDate getDmakedate() {
    return (UFDate) this.getAttributeValue("dmakedate");
  }

  public void setDmakedate(UFDate dmakedate) {
    this.setAttributeValue("dmakedate", dmakedate);
  }

  public FBillStatusFlag getFstatusflag() {
    return (FBillStatusFlag) this.getAttributeValue("fstatusflag");
  }

  public void setFstatusflag(FBillStatusFlag fstatusflag) {
    this.setAttributeValue("fstatusflag", fstatusflag);
  }

  public Integer getIprintcount() {
    return (Integer) this.getAttributeValue("iprintcount");
  }

  public void setIprintcount(Integer iprintcount) {
    this.setAttributeValue("iprintcount", iprintcount);
  }

  public UFDouble getNaptotalverifymny() {
    return (UFDouble) this.getAttributeValue("naptotalverifymny");
  }

  public void setNaptotalverifymny(UFDouble naptotalverifymny) {
    this.setAttributeValue("naptotalverifymny", naptotalverifymny);
  }

  public UFDouble getNartotalverifymny() {
    return (UFDouble) this.getAttributeValue("nartotalverifymny");
  }

  public void setNartotalverifymny(UFDouble nartotalverifymny) {
    this.setAttributeValue("nartotalverifymny", nartotalverifymny);
  }

  public UFDouble getNfarestmile() {
    return (UFDouble) this.getAttributeValue("nfarestmile");
  }

  public void setNfarestmile(UFDouble nfarestmile) {
    this.setAttributeValue("nfarestmile", nfarestmile);
  }

  public UFDouble getNfatestspecialmile1() {
    return (UFDouble) this.getAttributeValue("nfatestspecialmile1");
  }

  public void setNfatestspecialmile1(UFDouble nfatestspecialmile1) {
    this.setAttributeValue("nfatestspecialmile1", nfatestspecialmile1);
  }

  public UFDouble getNfatestspecialmile2() {
    return (UFDouble) this.getAttributeValue("nfatestspecialmile2");
  }

  public void setNfatestspecialmile2(UFDouble nfatestspecialmile2) {
    this.setAttributeValue("nfatestspecialmile2", nfatestspecialmile2);
  }

  public UFDouble getNfatestspecialmile3() {
    return (UFDouble) this.getAttributeValue("nfatestspecialmile3");
  }

  public void setNfatestspecialmile3(UFDouble nfatestspecialmile3) {
    this.setAttributeValue("nfatestspecialmile3", nfatestspecialmile3);
  }

  public UFDouble getNfatestspecialmile4() {
    return (UFDouble) this.getAttributeValue("nfatestspecialmile4");
  }

  public void setNfatestspecialmile4(UFDouble nfatestspecialmile4) {
    this.setAttributeValue("nfatestspecialmile4", nfatestspecialmile4);
  }

  public UFDouble getNtotalassistnum() {
    return (UFDouble) this.getAttributeValue("ntotalassistnum");
  }

  public void setNtotalassistnum(UFDouble ntotalassistnum) {
    this.setAttributeValue("ntotalassistnum", ntotalassistnum);
  }

  public UFDouble getNtotalmny() {
    return (UFDouble) this.getAttributeValue("ntotalmny");
  }

  public void setNtotalmny(UFDouble ntotalmny) {
    this.setAttributeValue("ntotalmny", ntotalmny);
  }

  public UFDouble getNtotalnum() {
    return (UFDouble) this.getAttributeValue("ntotalnum");
  }

  public void setNtotalnum(UFDouble ntotalnum) {
    this.setAttributeValue("ntotalnum", ntotalnum);
  }

  public UFDouble getNtotalpacknum() {
    return (UFDouble) this.getAttributeValue("ntotalpacknum");
  }

  public void setNtotalpacknum(UFDouble ntotalpacknum) {
    this.setAttributeValue("ntotalpacknum", ntotalpacknum);
  }

  public UFDouble getNtotalpackvolumn() {
    return (UFDouble) this.getAttributeValue("ntotalpackvolumn");
  }

  public void setNtotalpackvolumn(UFDouble ntotalpackvolumn) {
    this.setAttributeValue("ntotalpackvolumn", ntotalpackvolumn);
  }

  public UFDouble getNtotalpackweight() {
    return (UFDouble) this.getAttributeValue("ntotalpackweight");
  }

  public void setNtotalpackweight(UFDouble ntotalpackweight) {
    this.setAttributeValue("ntotalpackweight", ntotalpackweight);
  }

  public UFDouble getNtotalsignassistnum() {
    return (UFDouble) this.getAttributeValue("ntotalsignassistnum");
  }

  public void setNtotalsignassistnum(UFDouble ntotalsignassistnum) {
    this.setAttributeValue("ntotalsignassistnum", ntotalsignassistnum);
  }

  public UFDouble getNtotalsignnum() {
    return (UFDouble) this.getAttributeValue("ntotalsignnum");
  }

  public void setNtotalsignnum(UFDouble ntotalsignnum) {
    this.setAttributeValue("ntotalsignnum", ntotalsignnum);
  }

  public UFDouble getNtotalsignpacknum() {
    return (UFDouble) this.getAttributeValue("ntotalsignpacknum");
  }

  public void setNtotalsignpacknum(UFDouble ntotalsignpacknum) {
    this.setAttributeValue("ntotalsignpacknum", ntotalsignpacknum);
  }

  public UFDouble getNtotalsignpackvolumn() {
    return (UFDouble) this.getAttributeValue("ntotalsignpackvolumn");
  }

  public void setNtotalsignpackvolumn(UFDouble ntotalsignpackvolumn) {
    this.setAttributeValue("ntotalsignpackvolumn", ntotalsignpackvolumn);
  }

  public UFDouble getNtotalsignpackweight() {
    return (UFDouble) this.getAttributeValue("ntotalsignpackweight");
  }

  public void setNtotalsignpackweight(UFDouble ntotalsignpackweight) {
    this.setAttributeValue("ntotalsignpackweight", ntotalsignpackweight);
  }

  public UFDouble getNtotalsignvolumn() {
    return (UFDouble) this.getAttributeValue("ntotalsignvolumn");
  }

  public void setNtotalsignvolumn(UFDouble ntotalsignvolumn) {
    this.setAttributeValue("ntotalsignvolumn", ntotalsignvolumn);
  }

  public UFDouble getNtotalsignweight() {
    return (UFDouble) this.getAttributeValue("ntotalsignweight");
  }

  public void setNtotalsignweight(UFDouble ntotalsignweight) {
    this.setAttributeValue("ntotalsignweight", ntotalsignweight);
  }

  public UFDouble getNtotalvolumn() {
    return (UFDouble) this.getAttributeValue("ntotalvolumn");
  }

  public void setNtotalvolumn(UFDouble ntotalvolumn) {
    this.setAttributeValue("ntotalvolumn", ntotalvolumn);
  }

  public UFDouble getNtotalweight() {
    return (UFDouble) this.getAttributeValue("ntotalweight");
  }

  public void setNtotalweight(UFDouble ntotalweight) {
    this.setAttributeValue("ntotalweight", ntotalweight);
  }

  public String getPk_corp() {
    return (String) this.getAttributeValue("pk_corp");
  }

  public void setPk_corp(String pk_corp) {
    this.setAttributeValue("pk_corp", pk_corp);
  }

  public String getPk_defdoc1() {
    return (String) this.getAttributeValue("pk_defdoc1");
  }

  public void setPk_defdoc1(String pk_defdoc1) {
    this.setAttributeValue("pk_defdoc1", pk_defdoc1);
  }

  public String getPk_defdoc10() {
    return (String) this.getAttributeValue("pk_defdoc10");
  }

  public void setPk_defdoc10(String pk_defdoc10) {
    this.setAttributeValue("pk_defdoc10", pk_defdoc10);
  }

  public String getPk_defdoc11() {
    return (String) this.getAttributeValue("pk_defdoc11");
  }

  public void setPk_defdoc11(String pk_defdoc11) {
    this.setAttributeValue("pk_defdoc11", pk_defdoc11);
  }

  public String getPk_defdoc12() {
    return (String) this.getAttributeValue("pk_defdoc12");
  }

  public void setPk_defdoc12(String pk_defdoc12) {
    this.setAttributeValue("pk_defdoc12", pk_defdoc12);
  }

  public String getPk_defdoc13() {
    return (String) this.getAttributeValue("pk_defdoc13");
  }

  public void setPk_defdoc13(String pk_defdoc13) {
    this.setAttributeValue("pk_defdoc13", pk_defdoc13);
  }

  public String getPk_defdoc14() {
    return (String) this.getAttributeValue("pk_defdoc14");
  }

  public void setPk_defdoc14(String pk_defdoc14) {
    this.setAttributeValue("pk_defdoc14", pk_defdoc14);
  }

  public String getPk_defdoc15() {
    return (String) this.getAttributeValue("pk_defdoc15");
  }

  public void setPk_defdoc15(String pk_defdoc15) {
    this.setAttributeValue("pk_defdoc15", pk_defdoc15);
  }

  public String getPk_defdoc16() {
    return (String) this.getAttributeValue("pk_defdoc16");
  }

  public void setPk_defdoc16(String pk_defdoc16) {
    this.setAttributeValue("pk_defdoc16", pk_defdoc16);
  }

  public String getPk_defdoc17() {
    return (String) this.getAttributeValue("pk_defdoc17");
  }

  public void setPk_defdoc17(String pk_defdoc17) {
    this.setAttributeValue("pk_defdoc17", pk_defdoc17);
  }

  public String getPk_defdoc18() {
    return (String) this.getAttributeValue("pk_defdoc18");
  }

  public void setPk_defdoc18(String pk_defdoc18) {
    this.setAttributeValue("pk_defdoc18", pk_defdoc18);
  }

  public String getPk_defdoc19() {
    return (String) this.getAttributeValue("pk_defdoc19");
  }

  public void setPk_defdoc19(String pk_defdoc19) {
    this.setAttributeValue("pk_defdoc19", pk_defdoc19);
  }

  public String getPk_defdoc2() {
    return (String) this.getAttributeValue("pk_defdoc2");
  }

  public void setPk_defdoc2(String pk_defdoc2) {
    this.setAttributeValue("pk_defdoc2", pk_defdoc2);
  }

  public String getPk_defdoc20() {
    return (String) this.getAttributeValue("pk_defdoc20");
  }

  public void setPk_defdoc20(String pk_defdoc20) {
    this.setAttributeValue("pk_defdoc20", pk_defdoc20);
  }

  public String getPk_defdoc3() {
    return (String) this.getAttributeValue("pk_defdoc3");
  }

  public void setPk_defdoc3(String pk_defdoc3) {
    this.setAttributeValue("pk_defdoc3", pk_defdoc3);
  }

  public String getPk_defdoc4() {
    return (String) this.getAttributeValue("pk_defdoc4");
  }

  public void setPk_defdoc4(String pk_defdoc4) {
    this.setAttributeValue("pk_defdoc4", pk_defdoc4);
  }

  public String getPk_defdoc5() {
    return (String) this.getAttributeValue("pk_defdoc5");
  }

  public void setPk_defdoc5(String pk_defdoc5) {
    this.setAttributeValue("pk_defdoc5", pk_defdoc5);
  }

  public String getPk_defdoc6() {
    return (String) this.getAttributeValue("pk_defdoc6");
  }

  public void setPk_defdoc6(String pk_defdoc6) {
    this.setAttributeValue("pk_defdoc6", pk_defdoc6);
  }

  public String getPk_defdoc7() {
    return (String) this.getAttributeValue("pk_defdoc7");
  }

  public void setPk_defdoc7(String pk_defdoc7) {
    this.setAttributeValue("pk_defdoc7", pk_defdoc7);
  }

  public String getPk_defdoc8() {
    return (String) this.getAttributeValue("pk_defdoc8");
  }

  public void setPk_defdoc8(String pk_defdoc8) {
    this.setAttributeValue("pk_defdoc8", pk_defdoc8);
  }

  public String getPk_defdoc9() {
    return (String) this.getAttributeValue("pk_defdoc9");
  }

  public void setPk_defdoc9(String pk_defdoc9) {
    this.setAttributeValue("pk_defdoc9", pk_defdoc9);
  }

  public UFDateTime getTaudittime() {
    return (UFDateTime) this.getAttributeValue("taudittime");
  }

  public void setTaudittime(UFDateTime taudittime) {
    this.setAttributeValue("taudittime", taudittime);
  }

  public UFTime getTdelivtime() {
    return (UFTime) this.getAttributeValue("tdelivtime");
  }

  public void setTdelivtime(UFTime tdelivtime) {
    this.setAttributeValue("tdelivtime", tdelivtime);
  }

  public UFDateTime getTlastmaketime() {
    return (UFDateTime) this.getAttributeValue("tlastmaketime");
  }

  public void setTlastmaketime(UFDateTime tlastmaketime) {
    this.setAttributeValue("tlastmaketime", tlastmaketime);
  }

  public UFDateTime getTmaketime() {
    return (UFDateTime) this.getAttributeValue("tmaketime");
  }

  public void setTmaketime(UFDateTime tmaketime) {
    this.setAttributeValue("tmaketime", tmaketime);
  }

  public String getVbillcode() {
    return (String) this.getAttributeValue("vbillcode");
  }

  public void setVbillcode(String vbillcode) {
    this.setAttributeValue("vbillcode", vbillcode);
  }

  public String getVdef1() {
    return (String) this.getAttributeValue("vdef1");
  }

  public void setVdef1(String vdef1) {
    this.setAttributeValue("vdef1", vdef1);
  }

  public String getVdef10() {
    return (String) this.getAttributeValue("vdef10");
  }

  public void setVdef10(String vdef10) {
    this.setAttributeValue("vdef10", vdef10);
  }

  public String getVdef11() {
    return (String) this.getAttributeValue("vdef11");
  }

  public void setVdef11(String vdef11) {
    this.setAttributeValue("vdef11", vdef11);
  }

  public String getVdef12() {
    return (String) this.getAttributeValue("vdef12");
  }

  public void setVdef12(String vdef12) {
    this.setAttributeValue("vdef12", vdef12);
  }

  public String getVdef13() {
    return (String) this.getAttributeValue("vdef13");
  }

  public void setVdef13(String vdef13) {
    this.setAttributeValue("vdef13", vdef13);
  }

  public String getVdef14() {
    return (String) this.getAttributeValue("vdef14");
  }

  public void setVdef14(String vdef14) {
    this.setAttributeValue("vdef14", vdef14);
  }

  public String getVdef15() {
    return (String) this.getAttributeValue("vdef15");
  }

  public void setVdef15(String vdef15) {
    this.setAttributeValue("vdef15", vdef15);
  }

  public String getVdef16() {
    return (String) this.getAttributeValue("vdef16");
  }

  public void setVdef16(String vdef16) {
    this.setAttributeValue("vdef16", vdef16);
  }

  public String getVdef17() {
    return (String) this.getAttributeValue("vdef17");
  }

  public void setVdef17(String vdef17) {
    this.setAttributeValue("vdef17", vdef17);
  }

  public String getVdef18() {
    return (String) this.getAttributeValue("vdef18");
  }

  public void setVdef18(String vdef18) {
    this.setAttributeValue("vdef18", vdef18);
  }

  public String getVdef19() {
    return (String) this.getAttributeValue("vdef19");
  }

  public void setVdef19(String vdef19) {
    this.setAttributeValue("vdef19", vdef19);
  }

  public String getVdef2() {
    return (String) this.getAttributeValue("vdef2");
  }

  public void setVdef2(String vdef2) {
    this.setAttributeValue("vdef2", vdef2);
  }

  public String getVdef20() {
    return (String) this.getAttributeValue("vdef20");
  }

  public void setVdef20(String vdef20) {
    this.setAttributeValue("vdef20", vdef20);
  }

  public String getVdef3() {
    return (String) this.getAttributeValue("vdef3");
  }

  public void setVdef3(String vdef3) {
    this.setAttributeValue("vdef3", vdef3);
  }

  public String getVdef4() {
    return (String) this.getAttributeValue("vdef4");
  }

  public void setVdef4(String vdef4) {
    this.setAttributeValue("vdef4", vdef4);
  }

  public String getVdef5() {
    return (String) this.getAttributeValue("vdef5");
  }

  public void setVdef5(String vdef5) {
    this.setAttributeValue("vdef5", vdef5);
  }

  public String getVdef6() {
    return (String) this.getAttributeValue("vdef6");
  }

  public void setVdef6(String vdef6) {
    this.setAttributeValue("vdef6", vdef6);
  }

  public String getVdef7() {
    return (String) this.getAttributeValue("vdef7");
  }

  public void setVdef7(String vdef7) {
    this.setAttributeValue("vdef7", vdef7);
  }

  public String getVdef8() {
    return (String) this.getAttributeValue("vdef8");
  }

  public void setVdef8(String vdef8) {
    this.setAttributeValue("vdef8", vdef8);
  }

  public String getVdef9() {
    return (String) this.getAttributeValue("vdef9");
  }

  public void setVdef9(String vdef9) {
    this.setAttributeValue("vdef9", vdef9);
  }

  public String getVdriverphone() {
    return (String) this.getAttributeValue("vdriverphone");
  }

  public void setVdriverphone(String vdriverphone) {
    this.setAttributeValue("vdriverphone", vdriverphone);
  }

  public String getVnote() {
    return (String) this.getAttributeValue("vnote");
  }

  public void setVnote(String vnote) {
    this.setAttributeValue("vnote", vnote);
  }

  public String getVsupercargophone() {
    return (String) this.getAttributeValue("vsupercargophone");
  }

  public void setVsupercargophone(String vsupercargophone) {
    this.setAttributeValue("vsupercargophone", vsupercargophone);
  }

  public String getVvolumnunitname() {
    return (String) this.getAttributeValue("vvolumnunitname");
  }

  public void setVvolumnunitname(String vvolumnunitname) {
    this.setAttributeValue("vvolumnunitname", vvolumnunitname);
  }

  public String getVweightunitname() {
    return (String) this.getAttributeValue("vweightunitname");
  }

  public void setVweightunitname(String vweightunitname) {
    this.setAttributeValue("vweightunitname", vweightunitname);
  }
//lumzh 2012-08-18 是否已暂估！
  public UFBoolean getIsestimate() {
    return (UFBoolean) this.getAttributeValue("vweightunitname");
  }
  public void setIsestimate(UFBoolean isestimate) {
    this.setAttributeValue("isestimate", isestimate);
  }
  //end
  public UFDateTime getTs(){
    return (UFDateTime) this.getAttributeValue("ts");
  }
}
