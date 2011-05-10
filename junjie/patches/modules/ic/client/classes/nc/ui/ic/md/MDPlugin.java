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
 * 供应链插件
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
	 * 检查存货是否维护了码单信息,,主要针对删除单据,或删除单据时校验是否维护了码单信息
	 * 
	 * @param items
	 * @throws BusinessException
	 *             如果该行存货存在码单信息时则抛出该异常
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
			throw new BusinessException("\n表体第" + toString(list)
					+ "行\n存在码单信息,不能执行该操作.");
		}
	}

	/**
	 * 检查需要维护码单信息的存货行,是否维护了码单信息
	 * 
	 * @param items
	 *            存货行VO
	 * @throws BusinessException
	 *             如果必须维护码单信息的VO没有维护,则抛出该异常
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
			throw new BusinessException("\n表体第" + toString(list)
					+ "行\n存货必须维护码单信息");
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
	 * 检查码单,是否被引用.
	 * 
	 * @param cgeneralbid
	 * @return 如果被引用 返回ture,否则返回false
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
	 * 检查存货是否需要维护码单信息
	 * 
	 * @param cinvbasid
	 * @return 如果需要返回true
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
			//add by ouyangzhb 2011-05-06 问题0000178: 直调业务的采购入库单签字时提示“签字出错 BEGIN
			String iszd = (String) avo.getParentVO().getAttributeValue("vuserdef20");
			//add by ouyangzhb 2011-05-10 当为出库单时，不需要维护码单
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
