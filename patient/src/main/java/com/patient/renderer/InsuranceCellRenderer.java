package com.patient.renderer;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class InsuranceCellRenderer extends DefaultTableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		JCheckBox checkBox = new JCheckBox();
		//checkBox.setSelected((boolean)value);
		
		if(checkBox.isSelected()) {
			setText("Yes");
		}else {
			setText("No");
		}
		return checkBox;
	}
	

}
