package Frontend;

import Backend.DatabaseManager;
import Backend.Record;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyTablePanel extends JPanel {

    JTextField userName_tf = new JTextField("Username...");
    JTextField account_tf = new JTextField("Account...");

    public MyTablePanel() throws SQLException {
        super();

        // Set the layout manager to BorderLayout
        setLayout(new BorderLayout());

        // Create a panel for the text fields
        JPanel textFieldsPanel = new JPanel();
        textFieldsPanel.setLayout(new FlowLayout()); // FlowLayout for side-by-side alignment
        textFieldsPanel.add(account_tf);
        textFieldsPanel.add(userName_tf);

        // Add the text fields panel to the top (North) of the main panel
        add(textFieldsPanel, BorderLayout.NORTH);

        // Create the table
        JTable table = getjTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll panel to the center of the main panel
        add(scrollPane, BorderLayout.CENTER);
    }
    private JTable getjTable() throws SQLException {
        // Getting the records from the db
        // connection is static, so you are able to instantiate db managers wherever
        DatabaseManager db = new DatabaseManager();
        ArrayList<Record> recordList = db.getAllRecords();
        System.out.print(recordList);

        // Creating our 2d array for the table
        String[][] data = new String[recordList.size()][2];
        // Populate the 2D array with Account and Usernames from record
        for (int i = 0; i < recordList.size(); i++) {
            data[i][0] = recordList.get(i).getAccount();
            data[i][1] = recordList.get(i).getUserName();
            System.out.println(recordList.get(i).getAccount());
        }

        // Column names
        String[] columnNames = {"Account", "Username"};

        // Create a custom table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create the table using the default table model
        JTable table = new JTable(model);

        // Creating the selection listener that will refer the clicked row
        ListSelectionListener ls = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String accountValue = (String) table.getValueAt(selectedRow, 0);
                        String userNameValue = (String) table.getValueAt(selectedRow, 1);
                        System.out.println("Selected Account: " + accountValue);
                        System.out.println("Selected Username: " + userNameValue);
                        account_tf.setText(accountValue);
                        userName_tf.setText(userNameValue);

                    }
                }
            }
        };

        // Add a ListSelectionListener to the table
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(ls);
        return table;
    }


}
