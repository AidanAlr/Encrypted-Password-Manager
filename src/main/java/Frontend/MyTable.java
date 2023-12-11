package Frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class MyTable extends JPanel {

    public MyTable() {
        super();
        JTable table = getjTable();
        JScrollPane scrollPane = new JScrollPane(table);
        // Add the scrollpanel to the panel
        add(scrollPane);
    }

    private static JTable getjTable() {
        Object[][] data = {
                {"Account1", "Username1"},
                {"Account2", "Username2"},
                {"Account3", "Username3"}
                // Add more rows as needed
        };

        // Column names
        String[] columnNames = {"Account", "Username"};

        // Create a custom table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells not editable for the second column (Age)
                return false;
            }
        };


        JTable table = new JTable(model);
        // Add "Edit" button column
        TableColumn editColumn = new TableColumn();
        editColumn.setCellRenderer(new MyEditButton());
        table.addColumn(editColumn);
        // Create the table using the default table model
        return table;
    }

}

