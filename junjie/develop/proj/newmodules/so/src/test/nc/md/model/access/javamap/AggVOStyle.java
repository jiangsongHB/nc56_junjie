package nc.md.model.access.javamap;

import nc.bs.logging.Logger;
import nc.md.model.MetaDataRuntimeException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 聚合VO的访问模式,适用于NC大多数的业务单据VO类
 * @author uap
 *
 */
public class AggVOStyle extends AbstractBeanStyle  {
	
	private static final long serialVersionUID = 35466L;
	
	// 聚合VO类的全名,信息来自对象元数据的Java-Mapping定义
	protected String m_aggregatedVOClass = null;
	
	public AggVOStyle(String aggregatedVOClass,String headVOClass) {
		super(headVOClass);
		m_aggregatedVOClass = aggregatedVOClass;
	}
	
	public String getAggVOClassName()
	{
		return m_aggregatedVOClass;
	}

	public Object newInstance(){
		try {
			AggregatedValueObject aggVO = 
				(AggregatedValueObject) Class.forName(m_aggregatedVOClass).newInstance();
			
			CircularlyAccessibleValueObject headVO = 
				(CircularlyAccessibleValueObject) Class.forName(m_entityClass).newInstance();
			
			aggVO.setParentVO(headVO);
			
			return aggVO;
			
		} catch (Exception e){
			Logger.error(e.getMessage());
			throw new MetaDataRuntimeException("实例化访问器出错!请检查访问器里的类是否正确!主表VO:"+m_aggregatedVOClass+",子表VO:"+m_entityClass);
		}
	}
	
	public Object getValue(Object ownerobject)
	{
		return ((AggregatedValueObject)ownerobject).getParentVO();
	}

	public BeanStyleEnum getStyle() {
		return BeanStyleEnum.AGGVO_HEAD;
	}

}
