package ca.ubc.cs304.ui;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // or a condition at your choice with row and column
    }
}
