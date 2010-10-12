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
 * �뵥��ⵥ�������Լ������ֶ�
 * 
 * @author zhoujie
 * @since 2010-09-04 15:57:47
 */
public class MDUtils {

	public final static String SAVE_BUTTON = "����";
	public final static String EDIT_BUTTON = "�༭";
	public final static String ADDLINE_BUTTON = "����";
	public final static String DELLINE_BUTTON = "ɾ��";
	public final static String CALC_BUTTON = "����";
	public final static String CANCEL_BUTTON = "ȡ��";
	public final static String MDINFO_BUTTON = "�뵥ά��";
	public final static String MBJS_BUTTON = "ë�߼���";
	
	public final static UIButton BLANK_BUTTON = new UIButton();
	
	/**
	 * ���
	 */
	public final static int KC_IN = 0;
	
	/**
	 * ����
	 */
	public final static int KC_OUT = 1;
	
	
	/**
	 * ��ʼ״̬
	 */
	public final static int INIT = 0x1;
	
	/**
	 * ��ʼ����,���Ա༭״̬
	 */
	public final static int INIT_CANEDIT = 0x2;
	
	/**
	 * �༭ing ״̬
	 */
	public final static int EDITING = 0x4;
	
	
	private static HashMap<String, String> billname;
	
	/**
	 * ȡ�ð�ť���ֵ��б�
	 * @return ��ť���Ƶ��б�
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
			billname.put("45","�ɹ���ⵥ");
			billname.put("4A","������ⵥ");
//			billname.put("4E","������ⵥ");
			billname.put("4I","�������ⵥ");
			billname.put("4C","���۳��ⵥ");
//			billname.put("4Y","�������ⵥ");
		}
		return billname.get(billtype);
	}
	
	/**
	 * ���Ƶ��㷨,
	 * 
	 * @param vos
	 * @param sszl
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdBJ(MdcrkVO[] vos,UFDouble sszl) throws BusinessException{
		sszl = sszl.abs();//���ⵥ�˴���ֵ�Ǹ���,����ȡ����ֵ
		
		if(sszl==null||sszl.doubleValue()<=0){
			throw new BusinessException("ʵ����������С��0");
		}
		boolean isCG = false;
		
		if(vos[0].getMd_meter()!=null){
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// ����*������ /��*��*������
		UFDouble count_ZL = new UFDouble(0);// ���з�̯������
		
		for (int i=0 ;i<vos.length;i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = vo.getMd_length();// ��
			UFDouble width = vo.getMd_width( );//��
			UFDouble meter = vo.getMd_meter();//����
			UFDouble zs = vo.getSrkzs();
			if (zs==null||zs.doubleValue()<=0) {
				throw new BusinessException("��"+(i+1)+"�У���������С��0");
			}
			if(isCG){
				if (meter==null||meter.doubleValue()<=0) {
					throw new BusinessException("��"+(i+1)+"�У���������С��0");
				}
				count_LW =  count_LW.add(meter.multiply(zs));
			}else{
				if (lenth==null||lenth.doubleValue()<=0) {
					throw new BusinessException("��"+(i+1)+"�У�������С��0");
				}
				if (width==null||width.doubleValue()<=0) {
					throw new BusinessException("��"+(i+1)+"�У�����С��0");
				}
				count_LW =  count_LW.add(lenth.multiply(width).multiply(zs));
			}
		}
		
		for (int i=0 ;i<vos.length;i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = vo.getMd_length();// ��
			UFDouble width = vo.getMd_width( );//��
			UFDouble meter = vo.getMd_meter();//����
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
	 * �뵥����㷨
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdLJ(MdcrkVO[] vos)  throws BusinessException{
		
		boolean isCG = false;// �Ƿ�Ϊ�۸�
		
		if(vos[0].getMd_meter()!=null){
			isCG = true;
		}else if(vos[0].getMd_length()==null||vos[0].getMd_width()==null){
			throw new BusinessException("��1�У�����/���/��������ͬʱΪ��.");
		}
		
		UFDouble fjm = getFJM(vos[0]);
		UFDouble lsxs = getLSXS(vos[0]);
		
		for (int i=0 ;i<vos.length;i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = vo.getMd_length();// ��
			UFDouble width = vo.getMd_width( );//��
			UFDouble meter = vo.getMd_meter();//����
			String guige = vo.getDef6();
			UFDouble zs = vo.getSrkzs();
			if (zs==null||zs.doubleValue()<=0) {
				throw new BusinessException("��"+(i+1)+"�У�֧������С��0");
			}
			if(isCG){
				if (meter==null||meter.doubleValue()<=0) {
					throw new BusinessException("��"+(i+1)+"�У���������С��0");
				}
				//	����*֧��*����ϵ��/1000
				vo.setSrkzl(lsxs.multiply(zs).multiply(meter).div(1000));
			}else{
				if(guige==null||guige.trim().equals("")){
					throw new BusinessException("��"+(i+1)+"�У�ϵͳ��Ϊ�ô��Ϊ ��¯��,�����񲻺ϱ�׼");
				}
				if (lenth==null||lenth.doubleValue()<=0) {
					throw new BusinessException("��"+(i+1)+"�У����Ȳ���С��0");
				}
				if (width==null||width.doubleValue()<=0) {
					throw new BusinessException("��"+(i+1)+"�У���Ȳ���С��0");
				}
				
				UFDouble gui = new UFDouble(guige);
				//����+����ֵ��*��*��*֧��*7.85 /1000000000
				vo.setSrkzl((gui.add(fjm)).multiply(width).multiply(lenth).multiply(zs).multiply(7.85).div(1000000000));
			}
		}
		
		return vos;
	}
	
	/**
	 * ������
	 * @param vo
	 * @return
	 */
	static UFDouble getFJM(MdcrkVO vo){
		String pk_invbasid = getInvBasidByMDVO(vo);
		if(pk_invbasid==null){
			return UFDouble.ZERO_DBL;
		}
		// FIXME ���ɽ�������Ϻ�,��Ҫ���Ĵ˴�����
//		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
//		js.queryAdditionalvalue(pk_invbasid);
//		queryAdditionalvalue(String pk_invbasdoc)      ���ݴ�����������������ֵ
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
			System.out.println(" ��ѯ���������������PK ");
			return null;
		}else 
			return obj.toString();
	}
	
	/**
	 * ����ϵ��
	 * @param vo
	 * @return
	 */
	static UFDouble getLSXS(MdcrkVO vo){
		String pk_invbasid = getInvBasidByMDVO(vo);
		if(pk_invbasid==null){
			return new UFDouble(1);
		}
		//FIXME ���ɽ�������Ϻ�,��Ҫ���Ĵ˴�����
//		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
//		js.queryAdjustmentcoefficient(pk_invbasid);
//		queryAdditionalvalue(String pk_invbasdoc)      ���ݴ�����������������ֵ
//
//
//		queryAdjustmentcoefficient(String pk_invbasdoc) ���ݴ�����������������ϵ��

		return new UFDouble(1);
	}
	
	/**
	 * ë��ϵ��
	 * @param cinvbasid �����������
	 * @param pk_cubasdoc ���̻�������
	 * @param pk_invcl �������
	 * @param hd ���
	 * @return
	 */
	public static UFDouble getMBXS(String cinvbasid,String pk_cubasdoc,UFDouble hd){
		// FIXME ���ɽ�������Ϻ�,��Ҫ���Ĵ˴�����
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
