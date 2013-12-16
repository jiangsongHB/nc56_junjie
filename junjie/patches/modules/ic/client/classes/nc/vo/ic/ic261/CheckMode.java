package nc.vo.ic.ic261;

/**
 * 创建者：顾焱
 * 创建日期：(2001-5-22 11:35:51)
 * 功能：
 * 修改日期，修改人，修改原因，注释标志：
 */
public class CheckMode {
	final static public int WholeWh= 0; //整仓盘点
	final static public int Space= 1; //货位盘点
	final static public int Goods= 2; //存货盘点
	final static public int Circle= 3; //周期盘点
	final static public int Keeper= 4; //保管员盘点
	final static public int Minus= 5; //负库存盘点
	final static public int NActive= 6; //无动态盘点
	final static public int Term= 7; //保质期盘点
	final static public int Dynamic = 8;//动态盘点

	final static public int Check= 9; //选中
	final static public int UnCheck= 10; //未选中
	final static public int md= 11; //码单盘点


	public static String[] getCheckMode(){
		return new String[]{
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000167")/*@res "整仓盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000164")/*@res "货位盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000162")/*@res "存货盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000160")/*@res "周期盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000166")/*@res "保管员盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000163")/*@res "负库存盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000165")/*@res "无动态盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000161")/*@res "保质期盘点"*/,
				nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008spec","UPP4008spec-000547")/*@res "动态盘点"*/,
				"",
				"",
				"码单盘点"//add by ouyangzhb 2012-04-17 增加码单按钮
				};
	}
/**
 * CheckMode 构造子注解。
 */
public CheckMode() {
	super();

}
}