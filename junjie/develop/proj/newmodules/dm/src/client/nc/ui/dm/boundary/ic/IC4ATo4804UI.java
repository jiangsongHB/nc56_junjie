package nc.ui.dm.boundary.ic;

import java.awt.Container;

import nc.ui.dm.dm220.ref.ctrl.AbstractSourceUI;
import nc.vo.scm.pattern.domain.uap.enumeration.NCBillType;

public class IC4ATo4804UI extends AbstractSourceUI{
	 public IC4ATo4804UI(Container container){
		    super("nc.ui.ic.pub.pf.IC4ATo4804UI",container);
		  }

		  public String getCbilltypecode() {
		    return NCBillType.IC_4A.value();
		  }

		  public String getTitle() {
		    return NCBillType.IC_4A.getName();
		  }
}
