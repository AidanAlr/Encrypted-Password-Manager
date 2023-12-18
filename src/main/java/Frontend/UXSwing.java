package Frontend;

import Backend.DatabaseManager;
import Backend.EncryptionHelper;
import Backend.Record;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static Backend.Record.ALPHABET;

public class UXSwing {

    private JPanel main;
    private JButton RNGButton;
    private JButton newButton;
    private JButton deleteButton;
    private JTable table1;
    private JLabel TitleLabel;
    private final DatabaseManager db = new DatabaseManager();


    public Container getMainPanel() {
        return main;
    }

    public UXSwing() throws SQLException {
        ImageIcon icon = new ImageIcon("src/main/resources/icon.png");
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Adjust the size (30x30) as needed
        icon = new ImageIcon(resizedImage);
        TitleLabel.setIcon(icon);

        refreshTable();
        // Lambda expression that calls the method
        RNGButton.addActionListener(this::rngAction);
        newButton.addActionListener(this::newAction);
        deleteButton.addActionListener(this::deleteAction);
        table1.getSelectionModel().addListSelectionListener(this::tableSelectionAction);

    }

    private void tableSelectionAction(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    // Get the selected account and username
                    String accountValue = (String) table1.getValueAt(selectedRow, 0);
                    String userNameValue = (String) table1.getValueAt(selectedRow, 1);
                    // Fetch and display the password of the selected record
                    Record r = db.getRecord(accountValue, userNameValue);

                    String oldPassword = r.getPassword();
                    String newAccount = JOptionPane.showInputDialog("Current Account:" + r.getAccount() + (" (leave blank if no change)"));
                    String newUsername = JOptionPane.showInputDialog("Current Username:" + r.getUserName() + (" (leave blank if no change)"));
                    String newPassword = JOptionPane.showInputDialog("Current Password: " + oldPassword + (" (leave blank if no change)"));

                    if (newAccount != null && !newAccount.isEmpty()) {
                        db.updateAccount(r.getAccount(), newAccount);
                    }
                    if (newUsername != null && !newUsername.isEmpty()) {
                        db.updateUsername(r.getAccount(), newUsername);
                    }
                    if (newPassword != null && !newPassword.isEmpty()) {
                        db.updatePassword(r.getAccount(), EncryptionHelper.encrypt(newPassword));
                    }

                    refreshTable();
                    AutoDisappearPopup popup = new AutoDisappearPopup(
                            (JFrame) SwingUtilities.getWindowAncestor(main),
                            "Update",
                            "Record updated successfully.",
                            1000
                    );
                    popup.showDialog();
                } catch (SQLException ex) {
                    // Show error message if there's an issue fetching the record
                    JOptionPane.showMessageDialog(table1, "Error fetching record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    // Method to refresh the table with data from the database
    private void refreshTable() {
        try {
            ArrayList<Record> records = db.getAllRecords(); // Assume this method exists in DatabaseManager
            String[] columnNames = {"Account", "Username", "Password"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            for (Record record : records) {
                String hiddenPassword = "*".repeat(record.getPassword().length());
                model.addRow(new Object[]{record.getAccount(), record.getUserName(), hiddenPassword});
            }
            table1.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(main, "Error retrieving records: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteAction(ActionEvent event){
        String account = JOptionPane.showInputDialog("Please enter the account you would like deleted:");
        if (account != null && !account.isEmpty()) {
            try {
                db.removeRecord(account);
                refreshTable();
                AutoDisappearPopup popup = new AutoDisappearPopup(
                        (JFrame) SwingUtilities.getWindowAncestor(main),
                        "Update",
                        "Record deleted successfully.",
                        1000
                );
                popup.showDialog();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(main, "Error deleting record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rngAction(ActionEvent e) {
        String account = JOptionPane.showInputDialog("Enter Account Name:");
        String userName = JOptionPane.showInputDialog("Enter Username:");
        int size = Integer.parseInt(JOptionPane.showInputDialog("Enter length of RNG password:"));
        if (account != null && userName != null && size != 0) {
            // Arraylist to store temp rngPassword
            String rngPassword = getRandomString(size);
            try {
                Record r = new Record(account, userName, rngPassword);
                db.addNewRecord(r);
//              // This should refresh the table
                refreshTable();
                JOptionPane.showMessageDialog(this.getMainPanel(), account + " record added, your RNG password is: " + rngPassword, "New Password", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(main, "Error creating new record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static String getRandomString(int size) {
        ArrayList<Character> temp = new ArrayList<>(size);

        // Randomly select a character from the alphabet array and add it to the temp arraylist
        Random rand = new Random();
        for(int i = 0; i < size; i++ )
            temp.add(ALPHABET[rand.nextInt(ALPHABET.length)]);

        // StringBuilder to construct the string
        StringBuilder stringBuilder = new StringBuilder();

        // Append each character from the ArrayList to the StringBuilder
        for (Character ch : temp) {
            stringBuilder.append(ch);
        }
        // Convert StringBuilder to String
        return stringBuilder.toString();
    }

    private void newAction(ActionEvent e) {
        // Example: Getting data from input fields or dialog boxes
        String account = JOptionPane.showInputDialog("Enter Account Name:");
        String username = JOptionPane.showInputDialog("Enter Username:");
        String password = JOptionPane.showInputDialog("Enter Password:");

        if (account != null && username != null && password != null) {
            try {
                Record newRecord = new Record(account, username, password);
                db.addNewRecord(newRecord);
                refreshTable();
                AutoDisappearPopup popup = new AutoDisappearPopup(
                        (JFrame) SwingUtilities.getWindowAncestor(main),
                        "Update",
                        "Record added successfully.",
                        1000
                );
                popup.showDialog();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(main, "Error creating new record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}
