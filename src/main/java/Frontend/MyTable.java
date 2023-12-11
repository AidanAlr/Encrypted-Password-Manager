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
                {"Account1", "Username1", "Password1", "what"},
                {"Account2", "Username2", "Password2", "what"},
                {"Account3", "Username3", "Password3", "what"}
                // Add more rows as needed
        };

        // Column names
        String[] columnNames = {"Account", "Username", "Password", "Edit"};

        // Create a default table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        JTable table = new JTable(model);
        // Add "Edit" button column
        TableColumn editColumn = new TableColumn();
        editColumn.setCellRenderer(new MyEditButton());
        table.addColumn(editColumn);
        // Create the table using the default table model
        return table;
    }

}

