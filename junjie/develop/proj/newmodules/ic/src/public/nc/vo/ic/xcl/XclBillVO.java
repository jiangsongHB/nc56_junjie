package nc.vo.ic.xcl;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

/**
 * 销售单 聚合VO
 * 
 * @author heyq
 * 
 */
public class XclBillVO extends HYBillVO {
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (MdxclBVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (MdxclVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if (children == null || children.length == 0) {
			super.setChildrenVO(null);
		} else {
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays
					.asList(children).toArray(new MdxclBVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((MdxclVO) parent);
	}

}
