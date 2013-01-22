package nc.ui.pf.changedir;

//import nc.bs.pf.change.VOConversion;
//运输单参照其他入库
public class CHG4ATO4804  extends  nc.ui.pf.change.VOConversionUI {
	
	  @Override
	  public String[] getField() {
	    return new String[] {
	      //表头自定义项
	      "H_vdef1->H_vuserdef1",
	      "H_vdef2->H_vuserdef2",
	      "H_vdef3->H_vuserdef3",
	      "H_vdef4->H_vuserdef4",
	      "H_vdef5->H_vuserdef5",
	      "H_vdef6->H_vuserdef6",
	      "H_vdef7->H_vuserdef7",
	      "H_vdef8->H_vuserdef8",
	      "H_vdef9->H_vuserdef9",
	      "H_vdef10->H_vuserdef10",
	      "H_vdef11->H_vuserdef11",
	      "H_vdef12->H_vuserdef12",
	      "H_vdef13->H_vuserdef13",
	      "H_vdef14->H_vuserdef14",
	      "H_vdef15->H_vuserdef15",
	      "H_vdef16->H_vuserdef16",
	      "H_vdef17->H_vuserdef17",
	      "H_vdef18->H_vuserdef18",
	      "H_vdef19->H_vuserdef19",
	      "H_vdef20->H_vuserdef20",

	      //表头自定义项PK
	      "H_pk_defdoc1->H_pk_defdoc1",
	      "H_pk_defdoc2->H_pk_defdoc2",
	      "H_pk_defdoc3->H_pk_defdoc3",
	      "H_pk_defdoc4->H_pk_defdoc4",
	      "H_pk_defdoc5->H_pk_defdoc5",
	      "H_pk_defdoc6->H_pk_defdoc6",
	      "H_pk_defdoc7->H_pk_defdoc7",
	      "H_pk_defdoc8->H_pk_defdoc8",
	      "H_pk_defdoc9->H_pk_defdoc9",
	      "H_pk_defdoc10->H_pk_defdoc10",
	      "H_pk_defdoc11->H_pk_defdoc11",
	      "H_pk_defdoc12->H_pk_defdoc12",
	      "H_pk_defdoc13->H_pk_defdoc13",
	      "H_pk_defdoc14->H_pk_defdoc14",
	      "H_pk_defdoc15->H_pk_defdoc15",
	      "H_pk_defdoc16->H_pk_defdoc16",
	      "H_pk_defdoc17->H_pk_defdoc17",
	      "H_pk_defdoc18->H_pk_defdoc18",
	      "H_pk_defdoc19->H_pk_defdoc19",
	      "H_pk_defdoc20->H_pk_defdoc20",

	      //表头运输信息
	      //"H_cdelivorgid->H_ctransportcorpid",   
	      "H_csendtypeid->H_cdilivertypeid",
	      //"H_crouteid->H_ctransportrouteid",
	      "H_ctrancustid->H_ctrancustid",
	      //"H_cvehicletypeid->H_cvehicletypeid",
	      //"H_cvehicleid->H_cvehicleid",
	      //"H_csupercargoid->H_csupercargoid",

	      //表体自定义项
	      "B_vbdef1->B_vuserdef1",
	      "B_vbdef2->B_vuserdef2",
	      "B_vbdef3->B_vuserdef3",
	      "B_vbdef4->B_vuserdef4",
	      "B_vbdef5->B_vuserdef5",
	      "B_vbdef6->B_vuserdef6",
	      "B_vbdef7->B_vuserdef7",
	      "B_vbdef8->B_vuserdef8",
	      "B_vbdef9->B_vuserdef9",
	      "B_vbdef10->B_vuserdef10",
	      "B_vbdef11->B_vuserdef11",
	      "B_vbdef12->B_vuserdef12",
	      "B_vbdef13->B_vuserdef13",
	      "B_vbdef14->B_vuserdef14",
	      "B_vbdef15->B_vuserdef15",
	      "B_vbdef16->B_vuserdef16",
	      "B_vbdef17->B_vuserdef17",
	      "B_vbdef18->B_vuserdef18",
	      "B_vbdef19->B_vuserdef19",
	      "B_vbdef20->B_vuserdef20",

	      //表体自定义项PK
	      "B_pk_bdefdoc1->B_pk_defdoc1",
	      "B_pk_bdefdoc2->B_pk_defdoc2",
	      "B_pk_bdefdoc3->B_pk_defdoc3",
	      "B_pk_bdefdoc4->B_pk_defdoc4",
	      "B_pk_bdefdoc5->B_pk_defdoc5",
	      "B_pk_bdefdoc6->B_pk_defdoc6",
	      "B_pk_bdefdoc7->B_pk_defdoc7",
	      "B_pk_bdefdoc8->B_pk_defdoc8",
	      "B_pk_bdefdoc9->B_pk_defdoc9",
	      "B_pk_bdefdoc10->B_pk_defdoc10",
	      "B_pk_bdefdoc11->B_pk_defdoc11",
	      "B_pk_bdefdoc12->B_pk_defdoc12",
	      "B_pk_bdefdoc13->B_pk_defdoc13",
	      "B_pk_bdefdoc14->B_pk_defdoc14",
	      "B_pk_bdefdoc15->B_pk_defdoc15",
	      "B_pk_bdefdoc16->B_pk_defdoc16",
	      "B_pk_bdefdoc17->B_pk_defdoc17",
	      "B_pk_bdefdoc18->B_pk_defdoc18",
	      "B_pk_bdefdoc19->B_pk_defdoc19",
	      "B_pk_bdefdoc20->B_pk_defdoc20",

	      //存货及自由项信息
	      "B_cinventoryid->B_cinventoryid",
	      "B_cinvbasid->B_cinvbasid",
	      "B_vfree1->B_vfree1",
	      "B_vfree2->B_vfree2", 
	      "B_vfree3->B_vfree3",
	      "B_vfree4->B_vfree4",
	      "B_vfree5->B_vfree5",

	      //数量以及辅数量
	      "B_castunitid->B_castunitid",
	      "B_nchangerate->B_hsl",

	      "B_vbatch->B_vbatchcode",
	      "B_nnumber->B_ninnum",
	      "B_nassistnum->B_ninassistnum",
	      "B_nprice->B_ntaxprice",
	      "B_nweight->B_noutgrossnum",

	      

	      //生产制造相关信息
	      "B_cprojectid->B_cprojectid",
	      "B_cprojectphase->B_cprojectphaseid",

	      //表体运输信息——发货信息
	      "B_csendcorp->H_pk_corp",
	      "B_csendstoreorgid->H_pk_calbody",
	      "B_csendstoreid->H_cwarehouseid",
	      "B_dsenddate->B_dbizdate",
	      
	      //表体运输信息——收货信息
	      "B_creceivecustid->B_creceieveid",
	      "B_creceiveareaid->B_creceiveareaid",
	      "B_creceiveaddrid->B_creceivepointid",
	      "B_vreceiveaddress->B_vreceiveaddress",
	      "B_drequiredate->B_ddeliverdate",
	      
	      "B_cbiztypeid->H_cbiztype",

	      //源头信息
	      "B_cfirstbilltypecode->B_cfirsttype",
	      "B_vfirstbillcode->B_vfirstbillcode",
	      "B_cfirstbillid->B_cfirstbillhid",
	      "B_cfirstbillitemid->B_cfirstbillbid",
	      "B_vfirstrowno->B_vfirstrowno",

	      //来源信息
	      "B_vsourcebillcode->H_vbillcode",
	      "B_csourcebillid->B_cgeneralhid",
	      "B_csourcebillitemid->B_cgeneralbid",
	      "B_vsourcerowno->B_crowno",
	      "B_sourceTS->H_ts",
	      "B_sourceBTS->B_ts",
	      
	      //来源单据所属公司
	      "B_csourcecorpid->H_pk_corp"
	    };
	  }
	  @Override
	  public String[] getFormulas() {
	    return new String[] {
	      "B_csourcebilltypecode->\"4A\"",
//	      "B_nnumber->B_noutnum-B_ntotaltrannum",//add by ouyangzhb 2012-12-14 数量应该为入库数量
	      "H_isestimate->\"Y\""//采购订单将是否已暂估设置为Y
	    };
	  }

}
