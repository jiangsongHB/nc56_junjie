package nc.ui.scmpub.taxinvoicetype;

import java.awt.Container;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.checkrule.CheckRule;
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
		return 	  new IUniqueRule[]{
				new UniqueRule("����",new String[]{"code"}),
				new UniqueRule("����",new String[]{"name"})
				};	
		
	}

	/**
	 * ���ر���Ψһ���򣬽�����ǰ̨��顣
	 */
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		return 	  null;	
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
			return true;
	}
	
	public ICheckRule[] getHeadCheckRules() {
			return new CheckRule[]{
					new CheckRule("����", "code", false, null, null),
		            new CheckRule("����", "name", false, null, null),
		        };
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		return null;
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
		return null;
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
