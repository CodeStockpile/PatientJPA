package com.patient.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableRenderer extends DefaultTableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(row%2 == 0) {
			rendererComponent.setBackground(Color.orange);
		}else {
			rendererComponent.setBackground(table.getBackground());
		}
		
		return rendererComponent;
	}

}
