package Frontend;

import Backend.DatabaseManager;
import Backend.Record;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyTablePanel extends JPanel {

    private final DatabaseManager db = new DatabaseManager();
    public MyTablePanel() throws SQLException {
        super();
        // Try to create and add the table to the main panel
        try {
            setLayout(new BorderLayout());
            JTable table = getjTable();
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            // Show error message if there's an issue loading data
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTable getjTable() throws SQLException {
        JTable table = new JTable(refreshTableData());

        // Add a selection listener to the table
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the selected account and username
                    String accountValue = (String) table.getValueAt(selectedRow, 0);
                    String userNameValue = (String) table.getValueAt(selectedRow, 1);
                    String passWordValue = (String) table.getValueAt(selectedRow, 2);
                    try {
                        // Fetch and display the password of the selected record
                        Record r = db.getRecord(accountValue, userNameValue);

                        String newAccount = JOptionPane.showInputDialog("Current Account:" + r.getAccount()+ (" (leave blank if no change)"));
                        if (!newAccount.isBlank()){
                           accountValue = newAccount;
                        }

                        String newUsername = JOptionPane.showInputDialog("Current Username:" + r.getUserName() + (" (leave blank if no change)"));
                        if (!newUsername.isBlank()){
                            userNameValue = newUsername;
                        }

                        String newPassword = JOptionPane.showInputDialog("Current Password: " + r.getPassword() + (" (leave blank if no change)"));
                        if (!newPassword.isBlank()){
                            passWordValue = newPassword;
                        }

                        db.updateRecord(new Record(accountValue, userNameValue, passWordValue));
                        JOptionPane.showMessageDialog(this, "Record updated successfully.", "Update", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        // Show error message if there's an issue fetching the record
                        JOptionPane.showMessageDialog(this, "Error fetching record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        return table;
    }

    // Method to refresh the data in the table
    public DefaultTableModel refreshTableData() {
        try {
            // Fetch updated records from the database
            ArrayList<Record> recordList = db.getAllRecords();
            String[][] data;
            data = new String[recordList.size()][3];
            for (int i = 0; i < recordList.size(); i++) {
                data[i][0] = recordList.get(i).getAccount();
                data[i][1] = recordList.get(i).getUserName();
                data[i][2] = "*".repeat((recordList.get(i).getPassword()).length());

            }
            // Update the table model with new data
            String[] columnNames = {"Account", "Username", "Password"};
            return new DefaultTableModel(data, columnNames);

        } catch (SQLException ex) {
            // Show error message if there's an issue refreshing the data
            JOptionPane.showMessageDialog(this, "Error refreshing data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
