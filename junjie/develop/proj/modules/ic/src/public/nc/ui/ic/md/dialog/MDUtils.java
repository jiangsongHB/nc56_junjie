package nc.ui.ic.md.dialog;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.UIButton;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.pub.BusinessException;
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
	 * @return 按钮名称的列表
	 * @see #EDIT_BUTTON
	 * @see #ADDLINE_BUTTON
	 * @see #DELLINE_BUTTON
	 * @see #CALC_BUTTON
	 * @see #SAVE_BUTTON
	 * @see #CANCEL_BUTTON
	 */
	public static final String[] getButtons(){
		return new String[]{EDIT_BUTTON,ADDLINE_BUTTON,DELLINE_BUTTON,CALC_BUTTON,SAVE_BUTTON,CANCEL_BUTTON};
	}
	
	public static final String[] getEdgeButtons(){
		return new String[]{CALC_BUTTON,SAVE_BUTTON,CANCEL_BUTTON};
	}
	
	
	public static String getBillNameByBilltype(String billtype){
		if(billname==null){
			billname= new HashMap<String, String>();
			billname.put("45","采购入库单");
			billname.put("4A","其他入库单");
//			billname.put("4E","调拨入库单");
			billname.put("4I","其他出库单");
			billname.put("4C","销售出库单");
//			billname.put("4Y","调拨出库单");
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
	public static MdcrkVO[] mdBJ(MdcrkVO[] vos,UFDouble sszl) throws BusinessException{
		sszl = sszl.abs();//出库单此处的值是负的,所以取绝对值
		
		if(sszl==null||sszl.doubleValue()<=0){
			throw new BusinessException("实收重量不能小于0");
		}
		boolean isCG = false;
		
		if(vos[0].getMd_meter()!=null){
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// 米数*件数和 /长*宽*米数和
		UFDouble count_ZL = new UFDouble(0);// 所有分摊重量和
		
		for (int i=0 ;i<vos.length;i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = vo.getMd_length();// 长
			UFDouble width = vo.getMd_width( );//宽
			UFDouble meter = vo.getMd_meter();//米数
			UFDouble zs = vo.getSrkzs();
			if (zs==null||zs.doubleValue()<=0) {
				throw new BusinessException("第"+(i+1)+"行，件数不能小于0");
			}
			if(isCG){
				if (meter==null||meter.doubleValue()<=0) {
					throw new BusinessException("第"+(i+1)+"行，米数不能小于0");
				}
				count_LW =  count_LW.add(meter.multiply(zs));
			}else{
				if (lenth==null||lenth.doubleValue()<=0) {
					throw new BusinessException("第"+(i+1)+"行，长不能小于0");
				}
				if (width==null||width.doubleValue()<=0) {
					throw new BusinessException("第"+(i+1)+"行，宽不能小于0");
				}
				count_LW =  count_LW.add(lenth.multiply(width).multiply(zs));
			}
		}
		
		for (int i=0 ;i<vos.length;i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = vo.getMd_length();// 长
			UFDouble width = vo.getMd_width( );//宽
			UFDouble meter = vo.getMd_meter();//米数
			UFDouble zs = vo.getSrkzs();
			if(i == vos.length-1){
				vo.setSrkzl(sszl.sub(count_ZL));
			}else{
				UFDouble vga = UFDouble.ZERO_DBL;
				if(isCG){
					vga = sszl.multiply(meter).multiply(zs).div(count_LW);
				}else{
					vga = sszl.multiply(width).multiply(lenth).multiply(zs).div(count_LW);
				}
				vga = vga.setScale(4,UFDouble.ROUND_HALF_UP);
				
				vo.setSrkzl(vga);
				count_ZL = count_ZL.add(vga);
			}
//			System.out.println("########"+vo.getSrkzl());
		}
		
		return vos;
	}
	
	/**
	 * 码单理计算法
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdLJ(MdcrkVO[] vos)  throws BusinessException{
		
		boolean isCG = false;// 是否为槽钢
		
		if(vos[0].getMd_meter()!=null){
			isCG = true;
		}else if(vos[0].getMd_length()==null||vos[0].getMd_width()==null){
			throw new BusinessException("第1行，长度/宽度/米数不能同时为空.");
		}
		
		UFDouble fjm = getFJM(vos[0]);
		UFDouble lsxs = getLSXS(vos[0]);
		
		for (int i=0 ;i<vos.length;i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = vo.getMd_length();// 长
			UFDouble width = vo.getMd_width( );//宽
			UFDouble meter = vo.getMd_meter();//米数
			String guige = vo.getDef6();
			UFDouble zs = vo.getSrkzs();
			if (zs==null||zs.doubleValue()<=0) {
				throw new BusinessException("第"+(i+1)+"行，支数不能小于0");
			}
			if(isCG){
				if (meter==null||meter.doubleValue()<=0) {
					throw new BusinessException("第"+(i+1)+"行，米数不能小于0");
				}
				//	米数*支数*理算系数/1000
				vo.setSrkzl(lsxs.multiply(zs).multiply(meter).div(1000));
			}else{
				if(guige==null||guige.trim().equals("")){
					throw new BusinessException("第"+(i+1)+"行，系统认为该存货为 锅炉板,但其规格不合标准");
				}
				if (lenth==null||lenth.doubleValue()<=0) {
					throw new BusinessException("第"+(i+1)+"行，长度不能小于0");
				}
				if (width==null||width.doubleValue()<=0) {
					throw new BusinessException("第"+(i+1)+"行，宽度不能小于0");
				}
				
				UFDouble gui = new UFDouble(guige);
				//（厚+附加值）*宽*长*支数*7.85 /1000000000
				vo.setSrkzl((gui.add(fjm)).multiply(width).multiply(lenth).multiply(zs).multiply(7.85).div(1000000000));
			}
		}
		
		return vos;
	}
	
	/**
	 * 付加码
	 * @param vo
	 * @return
	 */
	static UFDouble getFJM(MdcrkVO vo){
		String pk_invbasid = getInvBasidByMDVO(vo);
		if(pk_invbasid==null){
			return UFDouble.ZERO_DBL;
		}
		// FIXME 与佛山代码整合后,需要更改此处代码
//		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
//		js.queryAdditionalvalue(pk_invbasid);
//		queryAdditionalvalue(String pk_invbasdoc)      根据存货基本档案查出附加值
		return UFDouble.ZERO_DBL;
	}
	
	static String getInvBasidByMDVO(MdcrkVO vo){
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj = null;
		try {
			obj = bs.executeQuery("select cinvbasid from ic_general_b where cgeneralbid = '"+vo.getCgeneralbid()+"' ", new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(obj==null){
			System.out.println(" 查询不到存货基本档案PK ");
			return null;
		}else 
			return obj.toString();
	}
	
	/**
	 * 理算系数
	 * @param vo
	 * @return
	 */
	static UFDouble getLSXS(MdcrkVO vo){
		String pk_invbasid = getInvBasidByMDVO(vo);
		if(pk_invbasid==null){
			return new UFDouble(1);
		}
		//FIXME 与佛山代码整合后,需要更改此处代码
//		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
//		js.queryAdjustmentcoefficient(pk_invbasid);
//		queryAdditionalvalue(String pk_invbasdoc)      根据存货基本档案查出附加值
//
//
//		queryAdjustmentcoefficient(String pk_invbasdoc) 根据存货基本档案查出理算系数

		return new UFDouble(1);
	}
	
	/**
	 * 毛边系数
	 * @param cinvbasid 存货基本档案
	 * @param pk_cubasdoc 客商基本档案
	 * @param pk_invcl 存货分类
	 * @param hd 厚度
	 * @return
	 */
	public static UFDouble getMBXS(String cinvbasid,String pk_cubasdoc,UFDouble hd){
		// FIXME 与佛山代码整合后,需要更改此处代码
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		Object obj = null;
		try {
			obj = bs.executeQuery("select pk_invcl from bd_invbasdoc where pk_invbasdoc = '"+cinvbasid+"' ", new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return new UFDouble(0.5);
	}
	
}
