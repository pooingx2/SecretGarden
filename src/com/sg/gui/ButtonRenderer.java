package com.sg.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.sg.main.ClientLauncher;

class ButtonRenderer extends AbstractCellEditor implements TableCellRenderer,TableCellEditor {
	private final JButton button;
	public ButtonRenderer(final JTable table, final DefaultTableModel model) {
		this.button = new JButton("x");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				if (table.columnAtPoint(point) == 3) {
					fireEditingStopped();
					int row = table.rowAtPoint(point);
					if (row >= 0) {
						ClientLauncher.getFrame().getFileListPanel().getFileMngPanel().getProgressFrame().removeRow(row);
						ClientLauncher.getTaskMgr().remove(row);
//						model.removeRow(row);
					}
				}
			}
		});
	}
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		return button;
	}
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected,	int row, int column) {
		return button;
	}
	public Object getCellEditorValue() {
		return button.getText();
	}
}
