package nc.md.model.access;

import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.access.javamap.AggVOStyle;

abstract class AbstractAttributeAccessor implements IAttributeAccessor {
	
	private static final long serialVersionUID = 35466L;
	
	private String nameInClass = null;
	
	public AbstractAttributeAccessor(String nameInClass)
	{
		this.nameInClass = nameInClass;
	}
	
	public AbstractAttributeAccessor()
	{
		
	}
	
	public abstract Object getValue(Object ownedObject, IAttribute attr);

	public abstract void setValue(Object ownedObject, IAttribute attr, Object value);

	public String getNameInClass() {
		return nameInClass;
	}

	public void setNameInClass(String nameInClass) {
		this.nameInClass = nameInClass;
	}
	
	protected String getFieldName(IAttribute attr) {
		String fieldname = getNameInClass();
		if(fieldname == null) fieldname = attr.getName();
		return fieldname;
	}
	
	protected Object getObjectByStructure(Object ownedObject, IAttribute attr)
	{
		IBean be = (IBean)attr.getOwnerBean();
		Object ob = null;
		if ( be.getBeanStyle() instanceof AggVOStyle ) 
			return  be.getBeanStyle().getValue(ownedObject);
		else 
			return  be.getBeanStyle().getValue(ownedObject);
			
	}
}
