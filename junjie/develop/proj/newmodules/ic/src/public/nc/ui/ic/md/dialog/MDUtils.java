package nc.ui.ic.md.dialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pub.jj.IJJUAPService;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ic.mdck.MDConstants;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 码单入库单基本属性及常量字段
 * 
 * @author zhoujie
 * @since 2010-09-04 15:57:47
 */
public class MDUtils {

	public final static String SAVE_BUTTON = "保存";

	public final static String EDIT_BUTTON = "编辑";

	public final static String ADDLINE_BUTTON = "增行";

	public final static String DELLINE_BUTTON = "删行";

	public final static String CALC_BUTTON = "计算";

	public final static String CANCEL_BUTTON = "取消";

	public final static String MDINFO_BUTTON = "码单维护";

	public final static String MBJS_BUTTON = "毛边计算";

	public final static UIButton BLANK_BUTTON = new UIButton();

	/**
	 * 入库
	 */
	public final static int KC_IN = 0;

	/**
	 * 出库
	 */
	public final static int KC_OUT = 1;

	/**
	 * 初始状态
	 */
	public final static int INIT = 0x1;

	/**
	 * 初始化后,可以编辑状态
	 */
	public final static int INIT_CANEDIT = 0x2;

	/**
	 * 编辑ing 状态
	 */
	public final static int EDITING = 0x4;

	private static HashMap<String, String> billname;

	/**
	 * 取得按钮名字的列表
	 * 
	 * @return 按钮名称的列表
	 * @see #EDIT_BUTTON
	 * @see #ADDLINE_BUTTON
	 * @see #DELLINE_BUTTON
	 * @see #CALC_BUTTON
	 * @see #SAVE_BUTTON
	 * @see #CANCEL_BUTTON
	 */
	public static final String[] getButtons() {
		return new String[] { EDIT_BUTTON, ADDLINE_BUTTON, DELLINE_BUTTON,
				CALC_BUTTON, SAVE_BUTTON, CANCEL_BUTTON };
	}

	public static final String[] getEdgeButtons() {
		return new String[] { CALC_BUTTON, SAVE_BUTTON, CANCEL_BUTTON };
	}

	public static String getBillNameByBilltype(String billtype) {
		if (billname == null) {
			billname = new HashMap<String, String>();
			billname.put("45", "采购入库单");
			billname.put("4A", "其他入库单");
			// billname.put("4E","调拨入库单");
			billname.put("4I", "其他出库单");
			billname.put("4C", "销售出库单");
			// billname.put("4Y","调拨出库单");
		}
		return billname.get(billtype);
	}

	/**
	 * 磅计的算法,
	 * 
	 * @param vos
	 * @param sszl
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdBJ(MdcrkVO[] vos, UFDouble sszl)
			throws BusinessException {
		sszl = sszl.abs();// 出库单此处的值是负的,所以取绝对值

		if (sszl == null || sszl.doubleValue() <= 0) {
			throw new BusinessException("实收重量不能小于0");
		}
		boolean isCG = false;

		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// 米数*件数和 /长*宽*米数和
		UFDouble count_ZL = new UFDouble(0);// 所有分摊重量和

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  取出长、宽 、米数 并转换成DOUBLE 型
			Double lenth =null, width=null,meter=null;
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// 长
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// 宽
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// 米数
			}
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("第" + (i + 1) + "行，件数不能小于0");
			}
			if (isCG) {
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，米数不能小于0");
				}
				count_LW = count_LW.add(zs.multiply(meter));
			} else {
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，长不能小于0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，宽不能小于0");
				}
				count_LW = count_LW.add(zs.multiply(lenth*width));
			}
		}

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  取出长、宽 、米数 并转换成DOUBLE 型
			double lenth =0, width=0,meter=0;
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// 长
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// 宽
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// 米数
			}
			UFDouble zs = vo.getSrkzs();
			if (i == vos.length - 1) {
				vo.setSrkzl(sszl.sub(count_ZL));
			} else {
				UFDouble vga = UFDouble.ZERO_DBL;
				if (isCG) {
					vga = sszl.multiply(meter).multiply(zs).div(count_LW);
				} else {
					vga = sszl.multiply(width).multiply(lenth).multiply(zs)
							.div(count_LW);
				}
				//add by ouyangzhb 2011-07-28 需求调整，精度改为三位
//				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_HALF_UP);
				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_FLOOR);
				vo.setSrkzl(vga);
				count_ZL = count_ZL.add(vga, MDConstants.ZL_XSW);
			}
			// System.out.println("########"+vo.getSrkzl());
		}

		return vos;
	}

	/**
	 * 码单理计算法
	 * 
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdLJ(MdcrkVO[] vos) throws BusinessException {

		boolean isCG = false;// 是否为槽钢
		if (vos[0].getMd_meter() != null) {
			isCG = true;
		} else if (vos[0].getDef7() == null
				|| vos[0].getDef8() == null) {
			throw new BusinessException("第1行，长度/宽度/米数不能同时为空.");
		}
		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-25 
			Double lenth =null, width=null,meter=null;
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// 长
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// 宽
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// 米数
			}
			String guige = vo.getDef6();
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("第" + (i + 1) + "行，支数不能小于0");
			}
			if (isCG) {
				UFDouble lsxs = getLSXS(vos[0]);
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，米数不能小于0");
				}
				// 米数*支数*理算系数/1000
				//UFDouble zl = lsxs.multiply(zs).multiply(meter).div(1000);
				//2010-11-23 MeiChao  槽钢理算系数计算方式修改: 米数*理算系数- (四舍五入3位小数)* 支数
				UFDouble zl = lsxs.multiply(meter).setScale(3, UFDouble.ROUND_HALF_UP).multiply(zs);
				//zl = zl.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_HALF_UP);
				zl = zl.setScale(3, UFDouble.ROUND_HALF_UP);
				vo.setSrkzl(zl);
			} else {
				UFDouble fjm = getFJM(vos[0]);
				if (guige == null || guige.trim().equals("")) {
					throw new BusinessException("第" + (i + 1)
							+ "行，系统认为该存货为 锅炉板,但其规格不合标准");
				}
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，长度不能小于0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，宽度不能小于0");
				}
				UFDouble gui = new UFDouble(guige);
				// （厚+附加值）*宽*长*支数*7.85 /1000000000
//				UFDouble zl = gui.add(fjm).multiply(width).multiply(lenth)
//						.multiply(zs).multiply(7.85).div(1000000000);
				//2010-11-23 MeiChao 理计取数调整,厚*宽*长*7.85-四舍五入保留3位小数后 再/1000000000 ,其结果也保留3位小数
				UFDouble zl = gui.add(fjm).multiply(width).multiply(lenth)
				.multiply(zs).multiply(7.85).setScale(3, UFDouble.ROUND_HALF_UP).div(1000000000);
				//zl = zl.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_HALF_UP);
				zl = zl.setScale(3, UFDouble.ROUND_HALF_UP);
				vo.setSrkzl(zl);
			}
		}

		return vos;
	}
	
	
	/**
	 * add by ouyangzhb 2012-03-21 钢厂重量磅计的算法,
	 * 
	 * @param vos
	 * @param sszl
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdGCBJ(MdcrkVO[] vos, UFDouble sszl)
			throws BusinessException {
		sszl = sszl.abs();// 出库单此处的值是负的,所以取绝对值
		if (sszl == null || sszl.doubleValue() <= 0) {
			throw new BusinessException("实收重量不能小于0");
		}
		boolean isCG = false;
		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// 米数*件数和 /长*宽*米数和
		UFDouble count_ZL = new UFDouble(0);// 所有分摊重量和
		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = UFDouble.ZERO_DBL;
			UFDouble width = UFDouble.ZERO_DBL;
			UFDouble meter = UFDouble.ZERO_DBL;
			lenth = vo.getMd_length();// 长
			width = vo.getMd_width();// 宽
			meter = vo.getMd_meter();// 米数
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("第" + (i + 1) + "行，件数不能小于0");
			}
			if (isCG) {
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，米数不能小于0");
				}
				count_LW = count_LW.add(zs.multiply(meter));
			} else {
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，长不能小于0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，宽不能小于0");
				}
				count_LW = count_LW.add(zs.multiply(lenth.multiply(width)));
			}
		}

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = UFDouble.ZERO_DBL;
			UFDouble width = UFDouble.ZERO_DBL;
			UFDouble meter = UFDouble.ZERO_DBL;
			lenth = vo.getMd_length();// 长
			width = vo.getMd_width();// 宽
			meter = vo.getMd_meter();// 米数

			UFDouble zs = vo.getSrkzs();
			if (i == vos.length - 1) {
				vo.setDef1(sszl.sub(count_ZL));
			} else {
				UFDouble vga = UFDouble.ZERO_DBL;
				if (isCG) {
					vga = sszl.multiply(meter).multiply(zs).div(count_LW);
				} else {
					vga = sszl.multiply(width).multiply(lenth).multiply(zs)
							.div(count_LW);
				}
				// add by ouyangzhb 2011-07-28 需求调整，精度改为三位
				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_FLOOR);
				vo.setDef1(vga);
				count_ZL = count_ZL.add(vga, MDConstants.ZL_XSW);
			}
		}

		return vos;
	}

	/**
	 * add by ouyangzhb 2012-03-21 码单钢厂重量理计算法
	 * 
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdGCLJ(MdcrkVO[] vos) throws BusinessException {

		boolean isCG = false;// 是否为槽钢
		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = UFDouble.ZERO_DBL;
			UFDouble width = UFDouble.ZERO_DBL;
			UFDouble meter = UFDouble.ZERO_DBL;
			lenth = vo.getMd_length();// 长
			width = vo.getMd_width();// 宽
			meter = vo.getMd_meter();// 米数
			String guige = vo.getDef6();
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("第" + (i + 1) + "行，支数不能小于0");
			}
			if (isCG) {
				UFDouble lsxs = getLSXS(vos[0]);
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，米数不能小于0");
				}
				// 2010-11-23 MeiChao 槽钢理算系数计算方式修改: 米数*理算系数- (四舍五入3位小数)* 支数
				UFDouble zl = lsxs.multiply(meter).setScale(3,
						UFDouble.ROUND_HALF_UP).multiply(zs);
				zl = zl.setScale(3, UFDouble.ROUND_HALF_UP);
				vo.setDef1(zl);
			} else {
				UFDouble fjm = getFJM(vos[0]);
				if (guige == null || guige.trim().equals("")) {
					throw new BusinessException("第" + (i + 1)
							+ "行，系统认为该存货为 锅炉板,但其规格不合标准");
				}
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，长度不能小于0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，宽度不能小于0");
				}
				UFDouble gui = new UFDouble(guige);

				// 2010-11-23 MeiChao 理计取数调整,厚*宽*长*7.85-四舍五入保留3位小数后 再/1000000000
				// ,其结果也保留3位小数
				UFDouble zl = gui.add(fjm).multiply(width).multiply(lenth)
						.multiply(zs).multiply(7.85).setScale(3,
								UFDouble.ROUND_HALF_UP).div(1000000000);
				zl = zl.setScale(3, UFDouble.ROUND_HALF_UP);
				vo.setDef1(zl);
			}
		}

		return vos;
	}
	
	/**
	 * 付加码
	 * 
	 * @param vo
	 * @return
	 */
	static UFDouble getFJM(MdcrkVO vo) throws BusinessException {
		MdcrkVO tempvo = (MdcrkVO) vo.clone();
		UFDouble rsDouble = new UFDouble(0);
		String pk_invbasid = getInvBasidByMDVO(vo);
		if (pk_invbasid == null)
			return UFDouble.ZERO_DBL;
		// 查询是否附加值配置
		boolean sffjz = queryFjzConfig(pk_invbasid);
		if (sffjz == false)
			return UFDouble.ZERO_DBL;
		tempvo.setDef10(pk_invbasid);
		List qList = new ArrayList();
		qList.add(tempvo);
		Logger.init("heyq");
		Logger.error("开始查询附加值:" + MDConstants.getCurrentTime());
//		Logger.error("传入的参数是：存货基本档案PK=" + pk_invbasid + ",长度="
//				+ tempvo.getMd_length().doubleValue() + ",宽度="
//				+ tempvo.getMd_width().doubleValue());
		//wanglei 2011-07-26 
		Logger.error("传入的参数是：存货基本档案PK=" + pk_invbasid + ",长度="
				+ tempvo.getDef7().toString() + ",宽度="
				+ tempvo.getDef8().toString());
		// FIXME 与佛山代码整合后,需要更改此处代码
		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
		try {
			List rsList = js.queryAdditionalvalue(qList);
			if (rsList == null)
				throw new BusinessException("当前存货的附加值系数没有维护，计算失败！");
			String def11 = ((MdcrkVO) rsList.get(0)).getDef11();
			if (def11 == null || def11.equals(""))
				throw new BusinessException("当前存货的附加值系数没有维护，计算失败！");
			rsDouble = new UFDouble(def11);
			Logger.error("通过接口查询到附加值的结果是:" + rsDouble.doubleValue());
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("查询附加值异常：" + e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		Logger.error("查询附加值结束,返回的结果是" + rsDouble.doubleValue());
		return rsDouble;
		// queryAdditionalvalue(String pk_invbasdoc) 根据存货基本档案查出附加值
		// return UFDouble.ZERO_DBL;
	}

	static String getInvBasidByMDVO(MdcrkVO vo) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj = null;
		try {
			obj = bs
					.executeQuery(
							"select cinvbasid from ic_general_b where cgeneralbid = '"
									+ vo.getCgeneralbid() + "' ",
							new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if (obj == null) {
			System.out.println(" 查询不到存货基本档案PK ");
			return null;
		} else
			return obj.toString();
	}

	/**
	 * 理算系数
	 * 
	 * @param vo
	 * @return
	 */
	static UFDouble getLSXS(MdcrkVO vo) throws BusinessException {
		UFDouble rsDouble = new UFDouble(0);
		String pk_invbasid = getInvBasidByMDVO(vo);
		if (pk_invbasid == null)
			return rsDouble;
		// FIXME 与佛山代码整合后,需要更改此处代码
		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
		try {
			Object obj = js.queryAdjustmentcoefficient(pk_invbasid);
			if (obj == null)
				throw new BusinessException("当前存货的理算系数没有维护，计算失败！");
			rsDouble = new UFDouble((BigDecimal) obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		// queryAdditionalvalue(String pk_invbasdoc) 根据存货基本档案查出附加值
		//
		//
		// queryAdjustmentcoefficient(String pk_invbasdoc) 根据存货基本档案查出理算系数

		return rsDouble;
	}

	/**
	 * 毛边系数
	 * 
	 * @param cinvbasid
	 *            存货基本档案
	 * @param pk_cubasdoc
	 *            客商基本档案
	 * @param pk_invcl
	 *            存货分类
	 * @param hd
	 *            厚度
	 * @return
	 */
	public static UFDouble getMBXS(String cinvbasid, String pk_cubasdoc,
			UFDouble hd) throws BusinessException {
		UFDouble rsDouble = new UFDouble(0);
		if (cinvbasid == null)
			return rsDouble;
		// FIXME 与佛山代码整合后,需要更改此处代码
		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
		try {
			Object obj = js.queryBurrcoefficient(cinvbasid);
			if (obj == null)
				throw new BusinessException("当前存货的毛边系数没有维护，计算失败！");
			rsDouble = new UFDouble((BigDecimal) obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		return rsDouble;
	}

	// 查询附加值配置
	private static boolean queryFjzConfig(String cinvbasid)
			throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def18 from bd_invmandoc where pk_invbasdoc='"
				+ cinvbasid + "' and pk_corp='"+ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey()+"'"; //2010-12-27 MeiChao 是否附加值,改为从存货管理档案中判断.
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("当前存货异常，在存货管理档案中不存在！");
		if (objs[0] == null)
			return false;
		else {
			String bz = (String) objs[0];
			bz = bz.toUpperCase();
			if (bz.equals("Y"))
				return true;
			else
				return false;
		}
	}
	
	/**
	 * add by ouyangzhb 2012-12-06 用新的算法计算：，
	 * （1）对于有验收宽度和验收长度的，按照码单的厚（规格）×验收宽度×验收长度得到体积来合计，然后按照每个码单所占比例进行分摊，余数放在最后一个码单上；
	 * （2）如果只有长度的，则按照理算系数×长度合计，然后按照每个码单所占比例进行分摊，余数放在最后一个码单上。
	 */
	public static MdcrkVO[] mdGBBJ(MdcrkVO[] vos, UFDouble sszl)
			throws BusinessException {
		sszl = sszl.abs();// 出库单此处的值是负的,所以取绝对值

		if (sszl == null || sszl.doubleValue() <= 0) {
			throw new BusinessException("实收重量不能小于0");
		}
		boolean isCG = false;

		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// 米数*件数和 /长*宽*米数和
		UFDouble count_ZL = new UFDouble(0);// 所有分摊重量和

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  取出长、宽 、米数 并转换成DOUBLE 型
			Double lenth =null, width=null,meter=null,gg = null;
			//add by ouyangzhb 2012-12-06
			if(vo.getDef6()!=null){
				gg = Double.parseDouble(vo.getDef6());// 米数
			}
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// 长
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// 宽
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// 米数
			}
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("第" + (i + 1) + "行，件数不能小于0");
			}
			if (isCG) {
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，米数不能小于0");
				}
				count_LW = count_LW.add(zs.multiply(meter));
			} else {
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，长不能小于0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("第" + (i + 1) + "行，宽不能小于0");
				}
				count_LW = count_LW.add(zs.multiply(lenth*width*gg));
			}
		}

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  取出长、宽 、米数 并转换成DOUBLE 型
			double lenth =0, width=0,meter=0, gg=0;
			if(vo.getDef6()!=null){
				gg = Double.parseDouble(vo.getDef6());// 米数
			}
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// 长
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// 宽
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// 米数
			}
			UFDouble zs = vo.getSrkzs();
			if (i == vos.length - 1) {
				vo.setSrkzl(sszl.sub(count_ZL));
			} else {
				UFDouble vga = UFDouble.ZERO_DBL;
				if (isCG) {
					vga = sszl.multiply(meter).multiply(zs).div(count_LW);
				} else {
					vga = sszl.multiply(width).multiply(lenth).multiply(gg).multiply(zs)
							.div(count_LW);
				}
				//add by ouyangzhb 2011-07-28 需求调整，精度改为三位
//				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_HALF_UP);
				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_FLOOR);
				vo.setSrkzl(vga);
				count_ZL = count_ZL.add(vga, MDConstants.ZL_XSW);
			}
		}

		return vos;
	}
	
	
	
	
}
