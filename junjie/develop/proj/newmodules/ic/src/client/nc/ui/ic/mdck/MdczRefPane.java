package nc.ui.ic.mdck;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;

public class MdczRefPane extends nc.ui.pub.beans.UIRefPane implements ValueChangedListener {
	
	private UITextField UITextField1 = null;

	public MdczRefPane() {
		super();
		init();
	}
	
	public MdczRefPane(java.awt.LayoutManager p0) {
		super(p0);
		init();
	}
	/**
	 * MyNCBatchRefPane 构造子注解。
	 * @param p0 java.awt.LayoutManager
	 * @param p1 boolean
	 */
	public MdczRefPane(java.awt.LayoutManager p0, boolean p1) {
		super(p0, p1);
		init();
	}
	/**
	 * MyNCBatchRefPane 构造子注解。
	 * @param p0 boolean
	 */
	public MdczRefPane(boolean p0) {
		super(p0);
		init();
	}
	public void init(){
		setRefName("件编号");
		
		setIsCustomDefined(true);
		setRefModel(new nc.ui.ic.mdck.MdczRef());
		setSealedDataButtonShow(true);
		getParent();
		
		//this.add(getUITextField1(), getUITextField1().getName());
		//this.addValueChangedListener(this);
		
		//getRefUI();
		
	}

	private UITextField getUITextField1() {
		if (UITextField1 == null) {
			UITextField1 = new UITextField();
			UITextField1.setBounds(new Rectangle(352, 4, 75, 20));
			UITextField1.setText("0");
			UITextField1.setEnabled(true);
			UITextField1.setName("合计");
		}
		return UITextField1;
	}
	
	private void MdczRefPane() {
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(1024, 1000));


	}

	public void valueChanged(ValueChangedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

}
