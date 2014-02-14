package nc.md.model.access.javamap;

import nc.bs.logging.Logger;
import nc.md.model.MetaDataRuntimeException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * �ۺ�VO�ķ���ģʽ,������NC�������ҵ�񵥾�VO��
 * @author uap
 *
 */
public class AggVOStyle extends AbstractBeanStyle  {
	
	private static final long serialVersionUID = 35466L;
	
	// �ۺ�VO���ȫ��,��Ϣ���Զ���Ԫ���ݵ�Java-Mapping����
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
			throw new MetaDataRuntimeException("ʵ��������������!���������������Ƿ���ȷ!����VO:"+m_aggregatedVOClass+",�ӱ�VO:"+m_entityClass);
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
