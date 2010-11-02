package nc.ui.pf.changedir;

import nc.ui.pf.change.VOConversionUI;
/**
 * 应付单 ->  采购发票
 * 2010-10-21
 * 未修改 将字段映射反过来先
 * @author 付世超
 *
 */
public class CHGF1TO25 extends VOConversionUI {
	
	public CHGF1TO25(){
		super();
	}
	
	public String getAfterClassName() {
		return null;
	}

	public String[] getField() {
		return new String[] { 
"H_pk_corp->H_dwbm",//公司主键->单位编码			  
"H_coperator->SYSOPERATOR",//制单人ID -> 系统当前用户
"H_cvendorbaseid->B_hbbm",//供应商基本信息ID 				   
"H_ctermprotocolid->B_sfkxyh",//付款协议
"H_vmemo->H_scomment",//备注
"H_cdeptid->B_deptid",//部门ID 
"H_cemployeeid->B_ywybm",//业务员ID
"H_cbiztype->H_xslxbm",//业务流程类型ID

"H_ctermprotocolid->B_sfkxyh",//付款协议ID -> 收付款协议号


"B_cbaseid->B_cinventoryid",//存货基础ID -> 存货基本档案pk 正确

"B_ccostsubjid->B_szxmid",//收支项目ID -> 收支项目ID
"B_ccurrencytypeid->B_bzbm",//原币币种ID -> 币种编码

"B_cprojectphaseid->B_pk_jobobjpha",//项目阶段ID ->项目阶段管理档案id
"B_csourcebillid->B_ddh",//来源单据ID ->订单号
"B_csourcebillrowid->B_ddhid",//来源单据行ID-> 订单行id 
"B_cupsourcebillid->B_vouchid",//上层来源单据ID -> 单据主键
"B_cupsourcebillrowid->B_fb_oid",//上层来源单据行ID ->单据辅表id
"B_cupsourcebilltype->H_djlxbm",//上层来源单据类型 ->单据类型编码
"B_idiscounttaxtype->B_kslb",//扣税类别 -> 扣税类别

"B_ninvoicenum->B_dfshl",//发票数量 -> 贷方数量

"B_nsummny->B_dfbbje",//本币价税合计 -> 贷方本币金额 
"B_ntaxmny->B_dfbbsj",//->贷方本币无税金额
"B_nmoney->B_dfbbwsje",//本币金额 -> 贷方本币无税金额
"B_noriginalsummny->B_dffbje", //辅币价税合计 ->贷方辅币金额
"B_nassisttaxmny->B_dffbsj",//辅币税额->贷方辅币税金
"B_noriginalsummny->B_dfybje",//金额 -> 贷方原币金额
"B_noriginaltaxmny->B_dfybsj",//税额 ->贷方原币税金
"B_noriginalcurmny->B_dfybwsje",//原币无税金额 -> 贷方原币无税金额
"B_noriginalcurprice->B_dj",//单价 -> 单价
"B_nexchangeotoarate->B_fbhl",//辅币汇率
"B_nassistsummny->B_fbye",//辅币余额		
"B_norgnettaxprice->B_hsdj",//含税单价	
"B_idiscounttaxtype->B_kslb",//扣税类别
"B_vproducenum->B_seqnum",//批次号 -> 批次号1 				
"B_noriginalsummny->B_ybye",//原币价税合计 -> 原币余额
"B_nsummny->B_bbye",//本币价税合计 -> 本币余额				
"B_nexchangeotobrate->B_bbhl",//折本汇率 -> 本币汇率			
"B_ntaxrate->B_sl",//税率 -> 税率
"B_ninvoicenum->B_shlye",//发票数量 -> 数量余额 
"B_vmemo->B_zy",//备注 -> 摘要	
//"B_vdef18->B_zyx18",//是否为费用暂估的标识 使用自定义项18
"H_cvendormangid->B_zyx19"//从应付单的自定义项中取出 客商管理id 使用应付单的自定义项19
				};
	}

	public String[] getFormulas() {
		return new String[] {
				"H_cbilltype->\"25\"",//单据类型编码 公式
				"H_darrivedate->SYSDATE",
				"H_dinvoicedate->SYSDATE",
				"H_dr->int(0)",//公式 删除标志
				"H_finitflag->int(0)",//公式  起初标志
				"H_ibillstatus->int(0)",//公式 单据状态
				"H_iinvoicetype->int(0)",//公式 发票类型
				"H_vdef20->\"Y\"",//2010-10-30 MeiChao  将表头的自定义字段20设置为Y 表示当前发票是由费用暂估应付单生成的,为采购费用发票.
				
				"B_cmangid->getColValue(bd_invmandoc,pk_invmandoc,pk_invbasdoc,B_cbaseid,pk_corp,H_pk_corp)",//存货管理ID 
				
				"B_dr->int(0)",//公式 删除标志
				"B_cprojectid->getColValue(bd_jobmngfil,pk_jobmngfil,pk_jobbasfil,B_jobid)",// 公式 项目ID
				};
	}

	public String getOtherClassName() {
		return null;
	}

}