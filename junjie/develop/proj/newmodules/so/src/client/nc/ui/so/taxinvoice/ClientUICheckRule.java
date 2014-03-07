package nc.ui.so.taxinvoice;

import java.awt.Container;

import nc.ui.ml.NCLangRes;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pf.pub.IDapType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.checkrule.CheckRule;
import nc.vo.trade.checkrule.CompareRule;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;
import nc.vo.trade.checkrule.UniqueRule;
import nc.vo.trade.checkrule.VOChecker;
import java.io.Serializable;
 
/**
 * <b> 前台校验类 </b><br>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */

public class ClientUICheckRule extends BeforeActionCHK  implements ICheckRules,IUniqueRules,ICheckRules2,Serializable{

	/** 
	 * 返回表头唯一规则，仅用于后台检查。
	 */
	public IUniqueRule[] getHeadUniqueRules() { 
		return new IUniqueRule[]{
				new UniqueRule("发票号不能重复，请检查。",new String[]{"vinvoiceno"})
			};
	}

	/**
	 * 返回表体唯一规则，仅用于前台检查。
	 */
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		IUniqueRule[] unique = null;		

						 
					return unique;	
						
	}
	
	/**
	 * 返回特殊检查类。 如果VOChecker不能满足要求，可以用此类来检查。
	 */
	public ISpecialChecker getSpecialChecker() {
		return null;
	}
	
	/**
	 * 是否允许表体为空
	 */
	public boolean isAllowEmptyBody(String tablecode) {
		if(tablecode.equals("$childtable"))
			return true;
		else 
			return false;
	}
	
		public ICheckRule[] getHeadCheckRules() {
			return new CheckRule[]{
					new CheckRule("发票号", "vinvoiceno", false, null, null),
		            new CheckRule("发票日期", "dinvoicedate", false, null, null),
					new CheckRule("供应商", "cinvoicemamid", false, null, null),
		            new CheckRule("票到日期", "dreceivedate", false, null, null),
					new CheckRule("发票类型", "cinvoicetype", false, null, null),
		            new CheckRule("币种", "ccurrencyid", false, null, null),
					new CheckRule("汇率", "nroe", false, null, null),
		        };
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		return new CheckRule[]{
				new CheckRule("发票项目", "cinvname", false, null, null),
	            new CheckRule("价税合计", "nsummny", false, null, null),
	            new CheckRule("数量", "nnumber", false, null, null),
	        };
	}

	public ICompareRule[] getHeadCompareRules() {
		return null;
	}

	public String[] getHeadIntegerField() {
		return null;
	}

	public String[] getHeadUFDoubleField() {
		return null;
	}

	public ICompareRule[] getItemCompareRules(String tablecode) {
        return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		return new String[] {
				"nnumber","nprice","nmny","ntaxman","ntaxprice","ntaxrate",
		};
	}
	
	public void runBatchClass(Container parent, String billType, String actionName, AggregatedValueObject[] vos, Object[] obj) throws Exception {
	}


	public void runClass(Container parent, String billType, String actionName, AggregatedValueObject vo, Object obj) throws Exception {
		if(actionName.equals(IPFACTION.SAVE))
		{	
			if(!VOChecker.check(vo,this ))
				throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());
		}
		
	}


}
