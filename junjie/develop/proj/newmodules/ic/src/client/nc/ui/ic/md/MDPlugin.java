package nc.ui.ic.md;

import java.util.ArrayList;

import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.ic.md.MdcrkVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;

/**
 * ��Ӧ�����
 * 
 * @author ThinkPad
 * @since 2010-09-20 11:23:25
 */
public class MDPlugin extends SCMBasePlugin {

	@Override
	public void beforDelte(AggregatedValueObject[] arg1)
			throws BusinessException {
		for (AggregatedValueObject avo : arg1) {
			refMD((GeneralBillItemVO[]) avo.getChildrenVO());
		}
	}

	@Override
	public void beforSave(AggregatedValueObject[] arg1)
			throws BusinessException {
		for (AggregatedValueObject avo : arg1) {
			ArrayList<GeneralBillItemVO> list = new ArrayList<GeneralBillItemVO>();
			GeneralBillItemVO[] items = (GeneralBillItemVO[]) avo
					.getChildrenVO();
			for (GeneralBillItemVO item : items) {
				if (item.getStatus() == VOStatus.DELETED) {
					list.add(item);
				}
			}
			if (!list.isEmpty()) {
				refMD(list.toArray(new GeneralBillItemVO[list.size()]));
			}
		}
	}

	/**
	 * ������Ƿ�ά�����뵥��Ϣ,,��Ҫ���ɾ������,��ɾ������ʱУ���Ƿ�ά�����뵥��Ϣ
	 * 
	 * @param items
	 * @throws BusinessException
	 *             ������д�������뵥��Ϣʱ���׳����쳣
	 */
	void refMD(GeneralBillItemVO[] items) throws BusinessException {
		ArrayList<String> list = new ArrayList<String>();
		for (GeneralBillItemVO item : items) {
			String no = item.getCrowno();
			String cgeneralbid = item.getCgeneralbid();
			boolean result = checkRefMD(cgeneralbid);
			if (result) {
				list.add(no);
			}
		}
		if (!list.isEmpty()) {
			toString(list);
			throw new BusinessException("\n�����" + toString(list)
					+ "��\n�����뵥��Ϣ,����ִ�иò���.");
		}
	}

	/**
	 * �����Ҫά���뵥��Ϣ�Ĵ����,�Ƿ�ά�����뵥��Ϣ
	 * 
	 * @param items
	 *            �����VO
	 * @throws BusinessException
	 *             �������ά���뵥��Ϣ��VOû��ά��,���׳����쳣
	 */
	void notrefMD(GeneralBillItemVO[] items) throws BusinessException {
		ArrayList<String> list = new ArrayList<String>();
		for (GeneralBillItemVO item : items) {
			String no = item.getCrowno();
			item.getCinvbasid();

			String cgeneralbid = item.getCgeneralbid();
			boolean result = checkRefMD(cgeneralbid);
			if (!result) {
				list.add(no);
			}
		}
		if (!list.isEmpty()) {
			toString(list);
			throw new BusinessException("\n�����" + toString(list)
					+ "��\n�������ά���뵥��Ϣ");
		}
	}

	String toString(ArrayList<String> list) {
		String res = "";
		for (String str : list) {
			res = res + str + ",";
		}
		return res;
	}

	/**
	 * ����뵥,�Ƿ�����.
	 * 
	 * @param cgeneralbid
	 * @return ��������� ����ture,���򷵻�false
	 */
	boolean checkRefMD(String cgeneralbid) {
		try {
			SuperVO[] vo = HYPubBO_Client.queryByCondition(MdcrkVO.class,
					" isnull(dr,0)=0 and cgeneralbid = '" + cgeneralbid + "' ");
			if (vo != null && vo.length > 0) {
				return true;
			}
		} catch (UifException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * ������Ƿ���Ҫά���뵥��Ϣ
	 * 
	 * @param cinvbasid
	 * @return �����Ҫ����true
	 */
	boolean checkNeedMD(String cinvbasid) {
		try {
			SuperVO[] vo = HYPubBO_Client
					.queryByCondition(
							InvbasdocVO.class,
							" isnull(dr,0)=0 and (isnull(def20,'N')='Y' or isnull(def20,'N')='y')  and pk_invbasdoc = '"
									+ cinvbasid + "' ");
			if (vo != null && vo.length > 0) {
				return true;
			}
		} catch (UifException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void beforDeleteLine(GeneralBillItemVO[] items)
			throws BusinessException {
		// refMD(items);
	}

	@Override
	public void beforAudit(AggregatedValueObject[] arg1)
			throws BusinessException {
		
		for (AggregatedValueObject avo : arg1) {
			//add by ouyangzhb 2011-05-06 ����0000178: ֱ��ҵ��Ĳɹ���ⵥǩ��ʱ��ʾ��ǩ�ֳ��� BEGIN
			String iszd = (String) avo.getParentVO().getAttributeValue("vuserdef20");
			//add by ouyangzhb 2011-05-10 ��Ϊ���ⵥʱ������Ҫά���뵥
			String cbilltypecode = (String) avo.getParentVO().getAttributeValue("cbilltypecode");
			if(cbilltypecode==null||cbilltypecode.equals("4C")||cbilltypecode.equals("4I")){
				return;
			}
			if(iszd==null||!iszd.equals("Y")){
				ArrayList<GeneralBillItemVO> list = new ArrayList<GeneralBillItemVO>();
				GeneralBillItemVO[] items = (GeneralBillItemVO[]) avo
						.getChildrenVO();
				for (GeneralBillItemVO item : items) {
					if (checkNeedMD(item.getCinvbasid())) {
						list.add(item);
					}
				}
				notrefMD(list.toArray(new GeneralBillItemVO[list.size()]));
			}
			//add end 
			
		}
	}

}
