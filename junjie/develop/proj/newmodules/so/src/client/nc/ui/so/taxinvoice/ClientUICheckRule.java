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
 * <b> ǰ̨У���� </b><br>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */

public class ClientUICheckRule extends BeforeActionCHK  implements ICheckRules,IUniqueRules,ICheckRules2,Serializable{

	/** 
	 * ���ر�ͷΨһ���򣬽����ں�̨��顣
	 */
	public IUniqueRule[] getHeadUniqueRules() { 
		return new IUniqueRule[]{
				new UniqueRule("��Ʊ�Ų����ظ������顣",new String[]{"vinvoiceno"})
			};
	}

	/**
	 * ���ر���Ψһ���򣬽�����ǰ̨��顣
	 */
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		IUniqueRule[] unique = null;		

						 
					return unique;	
						
	}
	
	/**
	 * �����������ࡣ ���VOChecker��������Ҫ�󣬿����ô�������顣
	 */
	public ISpecialChecker getSpecialChecker() {
		return null;
	}
	
	/**
	 * �Ƿ��������Ϊ��
	 */
	public boolean isAllowEmptyBody(String tablecode) {
		if(tablecode.equals("$childtable"))
			return true;
		else 
			return false;
	}
	
		public ICheckRule[] getHeadCheckRules() {
			return new CheckRule[]{
					new CheckRule("��Ʊ��", "vinvoiceno", false, null, null),
		            new CheckRule("��Ʊ����", "dinvoicedate", false, null, null),
					new CheckRule("��Ӧ��", "cinvoicemamid", false, null, null),
		            new CheckRule("Ʊ������", "dreceivedate", false, null, null),
					new CheckRule("��Ʊ����", "cinvoicetype", false, null, null),
		            new CheckRule("����", "ccurrencyid", false, null, null),
					new CheckRule("����", "nroe", false, null, null),
		        };
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		return new CheckRule[]{
				new CheckRule("��Ʊ��Ŀ", "cinvname", false, null, null),
	            new CheckRule("��˰�ϼ�", "nsummny", false, null, null),
	            new CheckRule("����", "nnumber", false, null, null),
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
