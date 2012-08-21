package nc.bs.pf.changedir;

import nc.bs.pf.change.VOConversion;


/**
 * 采购订单到运输单
 * @author 钟鸣
 *
 * 2008-9-11 下午07:24:07
 */
public class CHG21TO4804 extends VOConversion{
  public CHG21TO4804() {
  }
  
  @Override
  public String[] getField() {
    return new String[] {
      //表头自定义项
      "H_vdef1->H_vdef1",
      "H_vdef2->H_vdef2",
      "H_vdef3->H_vdef3",
      "H_vdef4->H_vdef4",
      "H_vdef5->H_vdef5",
      "H_vdef6->H_vdef6",
      "H_vdef7->H_vdef7",
      "H_vdef8->H_vdef8",
      "H_vdef9->H_vdef9",
      "H_vdef10->H_vdef10",
      "H_vdef11->H_vdef11",
      "H_vdef12->H_vdef12",
      "H_vdef13->H_vdef13",
      "H_vdef14->H_vdef14",
      "H_vdef15->H_vdef15",
      "H_vdef16->H_vdef16",
      "H_vdef17->H_vdef17",
      "H_vdef18->H_vdef18",
      "H_vdef19->H_vdef19",
      "H_vdef20->H_vdef20",

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
      "H_csendtypeid->H_ctransmodeid",
      //备注
      "H_vnote->H_vmemo",

      //表体自定义项
      "B_vbdef1->B_vdef1",
      "B_vbdef2->B_vdef2",
      "B_vbdef3->B_vdef3",
      "B_vbdef4->B_vdef4",
      "B_vbdef5->B_vdef5",
      "B_vbdef6->B_vdef6",
      "B_vbdef7->B_vdef7",
      "B_vbdef8->B_vdef8",
      "B_vbdef9->B_vdef9",
      "B_vbdef10->B_vdef10",
      "B_vbdef11->B_vdef11",
      "B_vbdef12->B_vdef12",
      "B_vbdef13->B_vdef13",
      "B_vbdef14->B_vdef14",
      "B_vbdef15->B_vdef15",
      "B_vbdef16->B_vdef16",
      "B_vbdef17->B_vdef17",
      "B_vbdef18->B_vdef18",
      "B_vbdef19->B_vdef19",
      "B_vbdef20->B_vdef20",

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
      "B_cinventoryid->B_cmangid",
      "B_cinvbasid->B_cbaseid",
      "B_vfree1->B_vfree1",
      "B_vfree2->B_vfree2", 
      "B_vfree3->B_vfree3",
      "B_vfree4->B_vfree4",
      "B_vfree5->B_vfree5",

      //数量以及辅数量
      "B_castunitid->B_cassistunit",

      "B_vbatch->B_vproducenum",
      "B_nprice->B_npricestore",

      //生产制造相关信息
      "B_cprojectid->B_cprojectid",
      "B_cprojectphase->B_cprojectphaseid",

      //表体运输信息——发货信息
      "B_csendareaid->B_cvenddevareaid",
      "B_csendaddrid->B_cvenddevaddrid",
      "B_csendvendorid->H_cvendormangid",
      "B_vsendaddress->B_vvenddevaddr",
      
      //表体运输信息——收货信息
      "B_creceivecorp->B_pk_arrvcorp",
      "B_creceivestoreorgid->B_pk_arrvstoorg",
      "B_creceivestoreid->B_cwarehouseid",
      "B_creceiveareaid->B_cdevareaid",
      "B_creceiveaddrid->B_cdevaddrid",
      "B_vreceiveaddress->B_vreceiveaddress",
      "B_drequiredate->B_dplanarrvdate",
      
      "B_cbiztypeid->H_cbiztype",

      //源头信息
      "B_vfirstbillcode->H_vordercode",
      "B_cfirstbillid->B_corderid",
      "B_cfirstbillitemid->B_corder_bid",
      "B_vfirstrowno->B_crowno",

      //来源信息
      "B_vsourcebillcode->H_vordercode",
      "B_csourcebillid->B_corderid",
      "B_csourcebillitemid->B_corder_bid",
      "B_vsourcerowno->B_crowno",
      "B_cpuorder_bb1id->B_corder_bb1id",
      "B_vpuplancode->B_vplancode",
      "B_sourceTS->H_ts",
      "B_sourceBTS->B_ts",
      "B_sourceBBTS->B_corder_bb1ts",
      
      //来源单据所属公司
      "B_csourcecorpid->H_pk_corp",
    };
  }
  
  @Override
  public String[] getFormulas() {
    return new String[] {
      "B_cfirstbilltypecode->\"21\"",
      "B_csourcebilltypecode->\"21\"",
      "B_nnumber->B_nordernum-B_naccumdevnum",
      "B_nchangerate->B_nordernum/B_nassistnum",
      "H_isestimate->\"Y\"",//采购订单将是否已暂估设置为Y
   };
  }

}
