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
	 * 
	 * @return ��ť���Ƶ��б�
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
			billname.put("45", "�ɹ���ⵥ");
			billname.put("4A", "������ⵥ");
			// billname.put("4E","������ⵥ");
			billname.put("4I", "�������ⵥ");
			billname.put("4C", "���۳��ⵥ");
			// billname.put("4Y","�������ⵥ");
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
	public static MdcrkVO[] mdBJ(MdcrkVO[] vos, UFDouble sszl)
			throws BusinessException {
		sszl = sszl.abs();// ���ⵥ�˴���ֵ�Ǹ���,����ȡ����ֵ

		if (sszl == null || sszl.doubleValue() <= 0) {
			throw new BusinessException("ʵ����������С��0");
		}
		boolean isCG = false;

		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// ����*������ /��*��*������
		UFDouble count_ZL = new UFDouble(0);// ���з�̯������

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  ȡ�������� ������ ��ת����DOUBLE ��
			Double lenth =null, width=null,meter=null;
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// ��
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// ��
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// ����
			}
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("��" + (i + 1) + "�У���������С��0");
			}
			if (isCG) {
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У���������С��0");
				}
				count_LW = count_LW.add(zs.multiply(meter));
			} else {
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У�������С��0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У�����С��0");
				}
				count_LW = count_LW.add(zs.multiply(lenth*width));
			}
		}

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  ȡ�������� ������ ��ת����DOUBLE ��
			double lenth =0, width=0,meter=0;
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// ��
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// ��
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// ����
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
				//add by ouyangzhb 2011-07-28 ������������ȸ�Ϊ��λ
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
	 * �뵥����㷨
	 * 
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdLJ(MdcrkVO[] vos) throws BusinessException {

		boolean isCG = false;// �Ƿ�Ϊ�۸�
		if (vos[0].getMd_meter() != null) {
			isCG = true;
		} else if (vos[0].getDef7() == null
				|| vos[0].getDef8() == null) {
			throw new BusinessException("��1�У�����/���/��������ͬʱΪ��.");
		}
		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-25 
			Double lenth =null, width=null,meter=null;
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// ��
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// ��
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// ����
			}
			String guige = vo.getDef6();
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("��" + (i + 1) + "�У�֧������С��0");
			}
			if (isCG) {
				UFDouble lsxs = getLSXS(vos[0]);
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У���������С��0");
				}
				// ����*֧��*����ϵ��/1000
				//UFDouble zl = lsxs.multiply(zs).multiply(meter).div(1000);
				//2010-11-23 MeiChao  �۸�����ϵ�����㷽ʽ�޸�: ����*����ϵ��- (��������3λС��)* ֧��
				UFDouble zl = lsxs.multiply(meter).setScale(3, UFDouble.ROUND_HALF_UP).multiply(zs);
				//zl = zl.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_HALF_UP);
				zl = zl.setScale(3, UFDouble.ROUND_HALF_UP);
				vo.setSrkzl(zl);
			} else {
				UFDouble fjm = getFJM(vos[0]);
				if (guige == null || guige.trim().equals("")) {
					throw new BusinessException("��" + (i + 1)
							+ "�У�ϵͳ��Ϊ�ô��Ϊ ��¯��,�����񲻺ϱ�׼");
				}
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У����Ȳ���С��0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У���Ȳ���С��0");
				}
				UFDouble gui = new UFDouble(guige);
				// ����+����ֵ��*��*��*֧��*7.85 /1000000000
//				UFDouble zl = gui.add(fjm).multiply(width).multiply(lenth)
//						.multiply(zs).multiply(7.85).div(1000000000);
				//2010-11-23 MeiChao ���ȡ������,��*��*��*7.85-�������뱣��3λС���� ��/1000000000 ,����Ҳ����3λС��
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
	 * add by ouyangzhb 2012-03-21 �ֳ��������Ƶ��㷨,
	 * 
	 * @param vos
	 * @param sszl
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdGCBJ(MdcrkVO[] vos, UFDouble sszl)
			throws BusinessException {
		sszl = sszl.abs();// ���ⵥ�˴���ֵ�Ǹ���,����ȡ����ֵ
		if (sszl == null || sszl.doubleValue() <= 0) {
			throw new BusinessException("ʵ����������С��0");
		}
		boolean isCG = false;
		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// ����*������ /��*��*������
		UFDouble count_ZL = new UFDouble(0);// ���з�̯������
		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = UFDouble.ZERO_DBL;
			UFDouble width = UFDouble.ZERO_DBL;
			UFDouble meter = UFDouble.ZERO_DBL;
			lenth = vo.getMd_length();// ��
			width = vo.getMd_width();// ��
			meter = vo.getMd_meter();// ����
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("��" + (i + 1) + "�У���������С��0");
			}
			if (isCG) {
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У���������С��0");
				}
				count_LW = count_LW.add(zs.multiply(meter));
			} else {
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У�������С��0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У�����С��0");
				}
				count_LW = count_LW.add(zs.multiply(lenth.multiply(width)));
			}
		}

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = UFDouble.ZERO_DBL;
			UFDouble width = UFDouble.ZERO_DBL;
			UFDouble meter = UFDouble.ZERO_DBL;
			lenth = vo.getMd_length();// ��
			width = vo.getMd_width();// ��
			meter = vo.getMd_meter();// ����

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
				// add by ouyangzhb 2011-07-28 ������������ȸ�Ϊ��λ
				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_FLOOR);
				vo.setDef1(vga);
				count_ZL = count_ZL.add(vga, MDConstants.ZL_XSW);
			}
		}

		return vos;
	}

	/**
	 * add by ouyangzhb 2012-03-21 �뵥�ֳ���������㷨
	 * 
	 * @param vos
	 * @return
	 * @throws BusinessException
	 */
	public static MdcrkVO[] mdGCLJ(MdcrkVO[] vos) throws BusinessException {

		boolean isCG = false;// �Ƿ�Ϊ�۸�
		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			UFDouble lenth = UFDouble.ZERO_DBL;
			UFDouble width = UFDouble.ZERO_DBL;
			UFDouble meter = UFDouble.ZERO_DBL;
			lenth = vo.getMd_length();// ��
			width = vo.getMd_width();// ��
			meter = vo.getMd_meter();// ����
			String guige = vo.getDef6();
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("��" + (i + 1) + "�У�֧������С��0");
			}
			if (isCG) {
				UFDouble lsxs = getLSXS(vos[0]);
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У���������С��0");
				}
				// 2010-11-23 MeiChao �۸�����ϵ�����㷽ʽ�޸�: ����*����ϵ��- (��������3λС��)* ֧��
				UFDouble zl = lsxs.multiply(meter).setScale(3,
						UFDouble.ROUND_HALF_UP).multiply(zs);
				zl = zl.setScale(3, UFDouble.ROUND_HALF_UP);
				vo.setDef1(zl);
			} else {
				UFDouble fjm = getFJM(vos[0]);
				if (guige == null || guige.trim().equals("")) {
					throw new BusinessException("��" + (i + 1)
							+ "�У�ϵͳ��Ϊ�ô��Ϊ ��¯��,�����񲻺ϱ�׼");
				}
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У����Ȳ���С��0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У���Ȳ���С��0");
				}
				UFDouble gui = new UFDouble(guige);

				// 2010-11-23 MeiChao ���ȡ������,��*��*��*7.85-�������뱣��3λС���� ��/1000000000
				// ,����Ҳ����3λС��
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
	 * ������
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
		// ��ѯ�Ƿ񸽼�ֵ����
		boolean sffjz = queryFjzConfig(pk_invbasid);
		if (sffjz == false)
			return UFDouble.ZERO_DBL;
		tempvo.setDef10(pk_invbasid);
		List qList = new ArrayList();
		qList.add(tempvo);
		Logger.init("heyq");
		Logger.error("��ʼ��ѯ����ֵ:" + MDConstants.getCurrentTime());
//		Logger.error("����Ĳ����ǣ������������PK=" + pk_invbasid + ",����="
//				+ tempvo.getMd_length().doubleValue() + ",���="
//				+ tempvo.getMd_width().doubleValue());
		//wanglei 2011-07-26 
		Logger.error("����Ĳ����ǣ������������PK=" + pk_invbasid + ",����="
				+ tempvo.getDef7().toString() + ",���="
				+ tempvo.getDef8().toString());
		// FIXME ���ɽ�������Ϻ�,��Ҫ���Ĵ˴�����
		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
		try {
			List rsList = js.queryAdditionalvalue(qList);
			if (rsList == null)
				throw new BusinessException("��ǰ����ĸ���ֵϵ��û��ά��������ʧ�ܣ�");
			String def11 = ((MdcrkVO) rsList.get(0)).getDef11();
			if (def11 == null || def11.equals(""))
				throw new BusinessException("��ǰ����ĸ���ֵϵ��û��ά��������ʧ�ܣ�");
			rsDouble = new UFDouble(def11);
			Logger.error("ͨ���ӿڲ�ѯ������ֵ�Ľ����:" + rsDouble.doubleValue());
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("��ѯ����ֵ�쳣��" + e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		Logger.error("��ѯ����ֵ����,���صĽ����" + rsDouble.doubleValue());
		return rsDouble;
		// queryAdditionalvalue(String pk_invbasdoc) ���ݴ�����������������ֵ
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
			System.out.println(" ��ѯ���������������PK ");
			return null;
		} else
			return obj.toString();
	}

	/**
	 * ����ϵ��
	 * 
	 * @param vo
	 * @return
	 */
	static UFDouble getLSXS(MdcrkVO vo) throws BusinessException {
		UFDouble rsDouble = new UFDouble(0);
		String pk_invbasid = getInvBasidByMDVO(vo);
		if (pk_invbasid == null)
			return rsDouble;
		// FIXME ���ɽ�������Ϻ�,��Ҫ���Ĵ˴�����
		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
		try {
			Object obj = js.queryAdjustmentcoefficient(pk_invbasid);
			if (obj == null)
				throw new BusinessException("��ǰ���������ϵ��û��ά��������ʧ�ܣ�");
			rsDouble = new UFDouble((BigDecimal) obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		// queryAdditionalvalue(String pk_invbasdoc) ���ݴ�����������������ֵ
		//
		//
		// queryAdjustmentcoefficient(String pk_invbasdoc) ���ݴ�����������������ϵ��

		return rsDouble;
	}

	/**
	 * ë��ϵ��
	 * 
	 * @param cinvbasid
	 *            �����������
	 * @param pk_cubasdoc
	 *            ���̻�������
	 * @param pk_invcl
	 *            �������
	 * @param hd
	 *            ���
	 * @return
	 */
	public static UFDouble getMBXS(String cinvbasid, String pk_cubasdoc,
			UFDouble hd) throws BusinessException {
		UFDouble rsDouble = new UFDouble(0);
		if (cinvbasid == null)
			return rsDouble;
		// FIXME ���ɽ�������Ϻ�,��Ҫ���Ĵ˴�����
		IJJUAPService js = NCLocator.getInstance().lookup(IJJUAPService.class);
		try {
			Object obj = js.queryBurrcoefficient(cinvbasid);
			if (obj == null)
				throw new BusinessException("��ǰ�����ë��ϵ��û��ά��������ʧ�ܣ�");
			rsDouble = new UFDouble((BigDecimal) obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		return rsDouble;
	}

	// ��ѯ����ֵ����
	private static boolean queryFjzConfig(String cinvbasid)
			throws BusinessException {
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		String sql = "select def18 from bd_invmandoc where pk_invbasdoc='"
				+ cinvbasid + "' and pk_corp='"+ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey()+"'"; //2010-12-27 MeiChao �Ƿ񸽼�ֵ,��Ϊ�Ӵ�����������ж�.
		Object[] objs = (Object[]) iUAPQueryBS.executeQuery(sql,
				new ArrayProcessor());
		if (objs == null || objs.length == 0)
			throw new BusinessException("��ǰ����쳣���ڴ���������в����ڣ�");
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
	 * add by ouyangzhb 2012-12-06 ���µ��㷨���㣺��
	 * ��1�����������տ�Ⱥ����ճ��ȵģ������뵥�ĺ񣨹�񣩡����տ�ȡ����ճ��ȵõ�������ϼƣ�Ȼ����ÿ���뵥��ռ�������з�̯�������������һ���뵥�ϣ�
	 * ��2�����ֻ�г��ȵģ���������ϵ�������Ⱥϼƣ�Ȼ����ÿ���뵥��ռ�������з�̯�������������һ���뵥�ϡ�
	 */
	public static MdcrkVO[] mdGBBJ(MdcrkVO[] vos, UFDouble sszl)
			throws BusinessException {
		sszl = sszl.abs();// ���ⵥ�˴���ֵ�Ǹ���,����ȡ����ֵ

		if (sszl == null || sszl.doubleValue() <= 0) {
			throw new BusinessException("ʵ����������С��0");
		}
		boolean isCG = false;

		if (vos[0].getMd_meter() != null) {
			isCG = true;
		}
		UFDouble count_LW = new UFDouble(0);// ����*������ /��*��*������
		UFDouble count_ZL = new UFDouble(0);// ���з�̯������

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  ȡ�������� ������ ��ת����DOUBLE ��
			Double lenth =null, width=null,meter=null,gg = null;
			//add by ouyangzhb 2012-12-06
			if(vo.getDef6()!=null){
				gg = Double.parseDouble(vo.getDef6());// ����
			}
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// ��
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// ��
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// ����
			}
			UFDouble zs = vo.getSrkzs();
			if (zs == null || zs.doubleValue() <= 0) {
				throw new BusinessException("��" + (i + 1) + "�У���������С��0");
			}
			if (isCG) {
				if (meter == null || meter.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У���������С��0");
				}
				count_LW = count_LW.add(zs.multiply(meter));
			} else {
				if (lenth == null || lenth.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У�������С��0");
				}
				if (width == null || width.doubleValue() <= 0) {
					throw new BusinessException("��" + (i + 1) + "�У�����С��0");
				}
				count_LW = count_LW.add(zs.multiply(lenth*width*gg));
			}
		}

		for (int i = 0; i < vos.length; i++) {
			MdcrkVO vo = vos[i];
			//add by ouyangzhb 2011-02-24  ȡ�������� ������ ��ת����DOUBLE ��
			double lenth =0, width=0,meter=0, gg=0;
			if(vo.getDef6()!=null){
				gg = Double.parseDouble(vo.getDef6());// ����
			}
			if(vo.getDef7()!=null){
				lenth = Double.parseDouble(vo.getDef7());// ��
			}
			if(vo.getDef8()!=null){
				width =Double.parseDouble(vo.getDef8());// ��
			}
			if(vo.getDef9()!=null){
				meter = Double.parseDouble(vo.getDef9());// ����
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
				//add by ouyangzhb 2011-07-28 ������������ȸ�Ϊ��λ
//				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_HALF_UP);
				vga = vga.setScale(MDConstants.ZL_XSW, UFDouble.ROUND_FLOOR);
				vo.setSrkzl(vga);
				count_ZL = count_ZL.add(vga, MDConstants.ZL_XSW);
			}
		}

		return vos;
	}
	
	
	
	
}
