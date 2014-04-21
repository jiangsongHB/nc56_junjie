package nc.vo.so.so002;

import java.util.ArrayList;

import nc.ui.scm.so.SaleBillType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.so.so016.SoVoTools;

/**
 * 
 * <p>
 * <b>本类主要完成以下功能：</b>
 * 
 * <ul>
 *  <li>销售发票参照销售出库单后续处理类
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * 	 XXX版本增加XXX的支持。
 * <p>
 * <p>
 * @version 本版本号
 * @since 上一版本号
 * @author fengjb
 * @time 2009-9-8 下午07:18:30
 */
public class OutToInvoiceChangeVO extends AbstractInvoiceChangeVO {

	/**
	 * 
	 * 父类方法重写
	 * 
	 * @see nc.vo.so.so002.AbstractInvoiceChangeVO#fillInvoiceData(nc.vo.so.so002.SaleinvoiceVO[])
	 */
	protected void fillInvoiceData(AggregatedValueObject[] preVos,SaleinvoiceVO[] invoicevos)
	throws BusinessException {
		//经过父类处理
		super.fillInvoiceData(preVos,invoicevos);
		
		 //向销售发票VO传递分单参数SO76
        invoicevos[0].getHeadVO().setAttributeValue("SO76", preVos[0].getParentVO().getAttributeValue("SO76"));
        
		ArrayList<SaleinvoiceBVO> albvo30 = new ArrayList<SaleinvoiceBVO>();
		ArrayList<SaleinvoiceBVO> albvo3U = new ArrayList<SaleinvoiceBVO>();
		
		//发票表头VO
        SaleVO invoiceHead = null;
        //发票表体VO
        SaleinvoiceBVO[] invoiceItems = null;
        //来源单据类型
        String cfirsttype = null;
       
		for (SaleinvoiceVO invoicevo : invoicevos) {
			
			invoiceHead = invoicevo.getHeadVO();
			invoiceItems = invoicevo.getBodyVO();
            
			cfirsttype = invoiceItems[0].getCreceipttype();
			//设置主表的来源单据主表ID
		    invoiceHead.setAttributeValue("cfirstbillhid", invoiceItems[0].getCsourcebillid()); 
			//来源于销售订单
			if (SaleBillType.SaleOrder.equals(cfirsttype)) {
				for (SaleinvoiceBVO bvo : invoiceItems)
					albvo30.add(bvo);
			//来源于退货申请单
			} else {
				for (SaleinvoiceBVO bvo : invoiceItems)
					albvo3U.add(bvo);
			}

		}
		 String[] formulas = null;
		//存在来源于销售订单的发票
		if (albvo30.size() > 0) {
		
			SaleinvoiceBVO[] bodyvos = albvo30.toArray(new SaleinvoiceBVO[0]);
			
			// 表体公式
			formulas = new String[] {
					//缓存表头数据
					"csalecorpid->getColValue(so_sale,csalecorpid,csaleid, csourcebillid )",
					"h_creceiptcorpid->getColValue(so_sale,creceiptcorpid,csaleid, csourcebillid )",
					"ccubasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,h_creceiptcorpid  )",
					"ccustbankid->getColValue2(bd_custbank,pk_accbank,pk_cubasdoc,ccubasid,defflag,\"Y\")",
					"vprintcustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc, ccubasid  )",
					"cdeptid->getColValue(so_sale,cdeptid,csaleid,csourcebillid)",
					"bfreecustflag->getColValue(so_sale,bfreecustflag,csaleid,csourcebillid)",
					"cfreecustid->getColValue(so_sale,cfreecustid,csaleid,csourcebillid)",
					"cemployeeid->getColValue( so_sale ,cemployeeid ,csaleid , csourcebillid )",
					"ctermprotocolid->getColValue(so_sale, ctermprotocolid, csaleid,csourcebillid)",
					//表体数据
					"ndiscountrate->getColValue(so_saleorder_b,ndiscountrate,corder_bid,csourcebillbodyid)",
					"nitemdiscountrate->getColValue(so_saleorder_b,nitemdiscountrate,corder_bid,csourcebillbodyid)",
					"ntaxrate->getColValue(so_saleorder_b,ntaxrate,corder_bid,csourcebillbodyid)",
					"cprolineid->getColValue(so_saleorder_b,cprolineid,corder_bid,csourcebillbodyid)",
					"ct_manageid->getColValue(so_saleorder_b,ct_manageid,corder_bid,csourcebillbodyid)" };

			if (SoVoTools.isUI())
				SoVoTools.execFormulas(formulas, bodyvos);
			else {
				SoVoTools.execFormulasAtBs(formulas, bodyvos);
			}

		}
		//存在来源于退货申请单的发票
		if (albvo3U.size() > 0) {	
			SaleinvoiceBVO[] bodyvos = albvo3U.toArray(new SaleinvoiceBVO[0]);
			// 表体公式
			formulas = new String[] {
					//缓存表头数据
					"csalecorpid->getColValue(so_apply,csalecorpid,pk_apply, csourcebillid )",
					"h_creceiptcorpid->getColValue(so_apply,creceiptcorpid,pk_apply, csourcebillid)",
					"ccubasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,h_creceiptcorpid  )",
					"ccustbankid->getColValue2(bd_custbank,pk_accbank,pk_cubasdoc, ccubasid,defflag,\"Y\")",
					"vprintcustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc, ccubasid  )",
					"cdeptid->getColValue(so_apply,cdeptid,pk_apply,csourcebillid)",
					"cfreecustid->getColValue(so_apply,cfreecustid,pk_apply,csourcebillid)",
					"bfreecustflag->getColValue(so_apply,bfreecustflag,pk_apply,csourcebillid)",
					"cemployeeid->getColValue( so_apply ,cemployeeid ,pk_apply , csourcebillid )", 
					//表体数据
					"ndiscountrate->getColValue(so_apply_b,ndiscountrate,pk_apply_b,csourcebillbodyid)",
					"nitemdiscountrate->getColValue(so_apply_b,nitemdiscountrate,pk_apply_b,csourcebillbodyid)",
					"ntaxrate->getColValue(so_apply_b,ntaxrate,pk_apply_b,csourcebillbodyid)",
					"cprolineid->getColValue(so_apply_b,pk_productline,pk_apply_b,csourcebillbodyid)" };
			if (SoVoTools.isUI())
				SoVoTools.execFormulas(formulas, bodyvos);
			else
				SoVoTools.execFormulasAtBs(formulas, bodyvos);
		}

		// 由于要进行数量单价金额的运算，需要设置订单折扣
		 for (SaleinvoiceVO aggvo:invoicevos) {
		   for (SaleinvoiceBVO invoicebvo : aggvo.getBodyVO()) {
			// 设置订单折扣 = 整单折扣 * 单品折扣
			UFDouble ndiscountrate = invoicebvo.getNdiscountrate() == null ?
					new UFDouble(100): invoicebvo.getNdiscountrate();
			UFDouble nitemdiscountrate = invoicebvo.getNitemdiscountrate() == null ?
					new UFDouble(100) : invoicebvo.getNitemdiscountrate();
			invoicebvo.setNorderDiscount(ndiscountrate.multiply(
					nitemdiscountrate).div(new UFDouble(100)));
		   }
		 }
	     //根据数量计算金额，价税合计
	     //一律按照含税优先计算 guyan jindongmei zhongwei
	     //价税合计=含税净价*数量
	     //税额=价税合计*税率/(1+税率)
	     //无税金额=价税合计-税额
	     //折扣额=含税单价*数量-价税合计
	     //每一步计算后要直接取精度(简单方法：本币同原币处理，暂不处理辅币)    		
	     //设置订单折扣 = 整单折扣 * 单品折扣
        //调用数量单价金额公共方法
        if (getBTaxPrior().booleanValue()) {//含税优先
        	/**
        	（1）	无税单价 = 含税单价 / （1+税率）
        	（2）	无税净价 = 含税净价 / （1+税率）
        	（3）	价税合计 = 含税净价 * 数量 （主动编辑价税合计，则不重新计算）
        	（4）	税额 = 价税合计 * 税率 / （1+税率）
        	（5）	无税金额 = 价税合计 – 税额。
        	（6）	数量 * 含税单价 – 价税合计 = 折扣额；
        	*/
       for (AggregatedValueObject aggvo:invoicevos) {
				nc.vo.scm.relacal.SCMRelationsCal
						.calculate(aggvo.getChildrenVO(), aggvo
								.getParentVO(), new int[] {
								RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE,
								RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE,
								RelationsCalVO.YES_LOCAL_FRAC },
								"noriginalcurtaxnetprice", SaleinvoiceBVO.getDescriptions(),
								SaleinvoiceBVO.getKeysForVO());
			}
		}
        else{//无税优先
        	for (AggregatedValueObject aggvo:invoicevos) {
				nc.vo.scm.relacal.SCMRelationsCal.calculate(aggvo
						.getChildrenVO(), aggvo.getParentVO(), new int[] {
						RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE,
						RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE,
						RelationsCalVO.YES_LOCAL_FRAC },
						"noriginalcurnetprice", SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeysForVO());
			}
        }
        
        //wanglei 2014-04-13 补充冲减信息
    	for (AggregatedValueObject aggvo:invoicevos) {
    		SaleinvoiceBVO[] bvos = (SaleinvoiceBVO[]) aggvo.getChildrenVO();
    		for (int i = 0 ; i < bvos.length; i++) {
    			bvos[i].setNsubcursummny(bvos[i].getNoriginalcursummny());
    			bvos[i].setNsubsummny(bvos[i].getNsummny());
    		}
		}
	}
}
