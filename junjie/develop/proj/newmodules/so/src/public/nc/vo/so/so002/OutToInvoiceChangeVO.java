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
 * <b>������Ҫ������¹��ܣ�</b>
 * 
 * <ul>
 *  <li>���۷�Ʊ�������۳��ⵥ����������
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>�����ʷ����ѡ����</b>
 * <p>
 * 	 XXX�汾����XXX��֧�֡�
 * <p>
 * <p>
 * @version ���汾��
 * @since ��һ�汾��
 * @author fengjb
 * @time 2009-9-8 ����07:18:30
 */
public class OutToInvoiceChangeVO extends AbstractInvoiceChangeVO {

	/**
	 * 
	 * ���෽����д
	 * 
	 * @see nc.vo.so.so002.AbstractInvoiceChangeVO#fillInvoiceData(nc.vo.so.so002.SaleinvoiceVO[])
	 */
	protected void fillInvoiceData(AggregatedValueObject[] preVos,SaleinvoiceVO[] invoicevos)
	throws BusinessException {
		//�������ദ��
		super.fillInvoiceData(preVos,invoicevos);
		
		 //�����۷�ƱVO���ݷֵ�����SO76
        invoicevos[0].getHeadVO().setAttributeValue("SO76", preVos[0].getParentVO().getAttributeValue("SO76"));
        
		ArrayList<SaleinvoiceBVO> albvo30 = new ArrayList<SaleinvoiceBVO>();
		ArrayList<SaleinvoiceBVO> albvo3U = new ArrayList<SaleinvoiceBVO>();
		
		//��Ʊ��ͷVO
        SaleVO invoiceHead = null;
        //��Ʊ����VO
        SaleinvoiceBVO[] invoiceItems = null;
        //��Դ��������
        String cfirsttype = null;
       
		for (SaleinvoiceVO invoicevo : invoicevos) {
			
			invoiceHead = invoicevo.getHeadVO();
			invoiceItems = invoicevo.getBodyVO();
            
			cfirsttype = invoiceItems[0].getCreceipttype();
			//�����������Դ��������ID
		    invoiceHead.setAttributeValue("cfirstbillhid", invoiceItems[0].getCsourcebillid()); 
			//��Դ�����۶���
			if (SaleBillType.SaleOrder.equals(cfirsttype)) {
				for (SaleinvoiceBVO bvo : invoiceItems)
					albvo30.add(bvo);
			//��Դ���˻����뵥
			} else {
				for (SaleinvoiceBVO bvo : invoiceItems)
					albvo3U.add(bvo);
			}

		}
		 String[] formulas = null;
		//������Դ�����۶����ķ�Ʊ
		if (albvo30.size() > 0) {
		
			SaleinvoiceBVO[] bodyvos = albvo30.toArray(new SaleinvoiceBVO[0]);
			
			// ���幫ʽ
			formulas = new String[] {
					//�����ͷ����
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
					//��������
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
		//������Դ���˻����뵥�ķ�Ʊ
		if (albvo3U.size() > 0) {	
			SaleinvoiceBVO[] bodyvos = albvo3U.toArray(new SaleinvoiceBVO[0]);
			// ���幫ʽ
			formulas = new String[] {
					//�����ͷ����
					"csalecorpid->getColValue(so_apply,csalecorpid,pk_apply, csourcebillid )",
					"h_creceiptcorpid->getColValue(so_apply,creceiptcorpid,pk_apply, csourcebillid)",
					"ccubasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,h_creceiptcorpid  )",
					"ccustbankid->getColValue2(bd_custbank,pk_accbank,pk_cubasdoc, ccubasid,defflag,\"Y\")",
					"vprintcustname->getColValue(bd_cubasdoc,custname,pk_cubasdoc, ccubasid  )",
					"cdeptid->getColValue(so_apply,cdeptid,pk_apply,csourcebillid)",
					"cfreecustid->getColValue(so_apply,cfreecustid,pk_apply,csourcebillid)",
					"bfreecustflag->getColValue(so_apply,bfreecustflag,pk_apply,csourcebillid)",
					"cemployeeid->getColValue( so_apply ,cemployeeid ,pk_apply , csourcebillid )", 
					//��������
					"ndiscountrate->getColValue(so_apply_b,ndiscountrate,pk_apply_b,csourcebillbodyid)",
					"nitemdiscountrate->getColValue(so_apply_b,nitemdiscountrate,pk_apply_b,csourcebillbodyid)",
					"ntaxrate->getColValue(so_apply_b,ntaxrate,pk_apply_b,csourcebillbodyid)",
					"cprolineid->getColValue(so_apply_b,pk_productline,pk_apply_b,csourcebillbodyid)" };
			if (SoVoTools.isUI())
				SoVoTools.execFormulas(formulas, bodyvos);
			else
				SoVoTools.execFormulasAtBs(formulas, bodyvos);
		}

		// ����Ҫ�����������۽������㣬��Ҫ���ö����ۿ�
		 for (SaleinvoiceVO aggvo:invoicevos) {
		   for (SaleinvoiceBVO invoicebvo : aggvo.getBodyVO()) {
			// ���ö����ۿ� = �����ۿ� * ��Ʒ�ۿ�
			UFDouble ndiscountrate = invoicebvo.getNdiscountrate() == null ?
					new UFDouble(100): invoicebvo.getNdiscountrate();
			UFDouble nitemdiscountrate = invoicebvo.getNitemdiscountrate() == null ?
					new UFDouble(100) : invoicebvo.getNitemdiscountrate();
			invoicebvo.setNorderDiscount(ndiscountrate.multiply(
					nitemdiscountrate).div(new UFDouble(100)));
		   }
		 }
	     //���������������˰�ϼ�
	     //һ�ɰ��պ�˰���ȼ��� guyan jindongmei zhongwei
	     //��˰�ϼ�=��˰����*����
	     //˰��=��˰�ϼ�*˰��/(1+˰��)
	     //��˰���=��˰�ϼ�-˰��
	     //�ۿ۶�=��˰����*����-��˰�ϼ�
	     //ÿһ�������Ҫֱ��ȡ����(�򵥷���������ͬԭ�Ҵ����ݲ�������)    		
	     //���ö����ۿ� = �����ۿ� * ��Ʒ�ۿ�
        //�����������۽�������
        if (getBTaxPrior().booleanValue()) {//��˰����
        	/**
        	��1��	��˰���� = ��˰���� / ��1+˰�ʣ�
        	��2��	��˰���� = ��˰���� / ��1+˰�ʣ�
        	��3��	��˰�ϼ� = ��˰���� * ���� �������༭��˰�ϼƣ������¼��㣩
        	��4��	˰�� = ��˰�ϼ� * ˰�� / ��1+˰�ʣ�
        	��5��	��˰��� = ��˰�ϼ� �C ˰�
        	��6��	���� * ��˰���� �C ��˰�ϼ� = �ۿ۶
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
        else{//��˰����
        	for (AggregatedValueObject aggvo:invoicevos) {
				nc.vo.scm.relacal.SCMRelationsCal.calculate(aggvo
						.getChildrenVO(), aggvo.getParentVO(), new int[] {
						RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE,
						RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE,
						RelationsCalVO.YES_LOCAL_FRAC },
						"noriginalcurnetprice", SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeysForVO());
			}
        }
        
        //wanglei 2014-04-13 ��������Ϣ
    	for (AggregatedValueObject aggvo:invoicevos) {
    		SaleinvoiceBVO[] bvos = (SaleinvoiceBVO[]) aggvo.getChildrenVO();
    		for (int i = 0 ; i < bvos.length; i++) {
    			bvos[i].setNsubcursummny(bvos[i].getNoriginalcursummny());
    			bvos[i].setNsubsummny(bvos[i].getNsummny());
    		}
		}
	}
}
