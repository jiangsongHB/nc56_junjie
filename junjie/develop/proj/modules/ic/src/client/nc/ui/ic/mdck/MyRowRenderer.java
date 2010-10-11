package nc.ui.ic.mdck;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyRowRenderer extends DefaultTableCellRenderer {

	public MyRowRenderer() {
	}

	public Component getTableCellRendererComponent(JTable t, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// setBackground(Color.getHSBColor(230, 230, 200));
		setBackground(Color.YELLOW);
		return super.getTableCellRendererComponent(t, value, isSelected,
				hasFocus, row, column);
	}

}
