package Frontend;

import Backend.DatabaseManager;
import Backend.EncryptionHelper;
import Backend.Record;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyTablePanel extends JPanel {
    private final DatabaseManager db = new DatabaseManager();

    public JTable myTable;
    public MyTablePanel() throws SQLException {
        super();
        // Try to create and add the table to the main panel
        try {
            setLayout(new BorderLayout());
            myTable = getjTable();
            JScrollPane scrollPane = new JScrollPane(myTable);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            // Show error message if there's an issue loading data
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTable getjTable() throws SQLException {
        JTable table = new JTable();
        table.setModel(getTableModel());

        // Add a selection listener to the table
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the selected account and username
                    String accountValue = (String) table.getValueAt(selectedRow, 0);
                    String userNameValue = (String) table.getValueAt(selectedRow, 1);
                    try {
                        // Fetch and display the password of the selected record
                        Record r = db.getRecord(accountValue, userNameValue);
                        String oldPassword = r.getPassword();

                        String newAccount = JOptionPane.showInputDialog("Current Account:" + r.getAccount() + (" (leave blank if no change)"));
                        if (newAccount.isBlank()) {
                            newAccount = accountValue;
                        }
                        db.updateAccount(accountValue, newAccount);

                        String newUsername = JOptionPane.showInputDialog("Current Username:" + r.getUserName() + (" (leave blank if no change)"));
                        if (newUsername.isBlank()) {
                            newUsername = userNameValue;
                        }
                        db.updateUsername(newAccount, newUsername);

                        String newPassword = JOptionPane.showInputDialog("Current Password: " + oldPassword + (" (leave blank if no change)"));
                        if (newPassword.isBlank()) {
                            newPassword = EncryptionHelper.encrypt(oldPassword);
                        }
                        db.updatePassword(newAccount, newPassword);

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

    public DefaultTableModel getTableModel() {
        try {
            // Fetch updated records from the database
            ArrayList<Record> recordList = db.getAllRecords();
            System.out.println(recordList);
            String[][] data = new String[recordList.size()][3];
            for (int i = 0; i < recordList.size(); i++) {
                data[i][0] = recordList.get(i).getAccount();
                data[i][1] = recordList.get(i).getUserName();
                data[i][2] = "*".repeat((recordList.get(i).getPassword()).length());
            }

            // Create a custom DefaultTableModel to make cells non-editable
            DefaultTableModel model = new DefaultTableModel(data, new String[] {"Account", "Username", "Password"}) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            return model;
        } catch (SQLException ex) {
            // Show error message if there's an issue refreshing the data
            JOptionPane.showMessageDialog(this, "Error refreshing data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
