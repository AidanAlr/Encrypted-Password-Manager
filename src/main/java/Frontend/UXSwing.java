package Frontend;

import Backend.DatabaseManager;
import Backend.Record;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static Backend.Record.ALPHABET;

public class UXSwing {

    private JPanel main;
    private JButton submitButton;
    private MyTablePanel myTablePanel;
    private JButton RNGButton;
    private JButton newButton;
    private JButton deleteButton;
    private final DatabaseManager db = new DatabaseManager();

    private void createUIComponents() throws SQLException {
        myTablePanel = new MyTablePanel();
    }

    public Container getMainPanel() {
        return main;
    }

    public UXSwing() throws SQLException {
        try {
            createUIComponents();
            // Lambda expression that calls the method
            RNGButton.addActionListener(this::rngAction);
            newButton.addActionListener(this::newAction);
            deleteButton.addActionListener(this::deleteAction);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(main, "Error initializing UI components: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAction(ActionEvent event){
        String account = JOptionPane.showInputDialog("Please enter the account you would like deleted:");
        if (account != null && !account.isEmpty()) {
            try {
                db.removeRecord(account);
                myTablePanel.myTable.setModel(myTablePanel.getTableModel());
                JOptionPane.showMessageDialog(this.getMainPanel(), "Record deleted successfully.", "Delete", JOptionPane.INFORMATION_MESSAGE);
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
            // Arraylist to store temp password
            ArrayList<Character> temp = new ArrayList<Character>(size);
            
            // Randomly select a character from the alphabet array and add it to the temp arraylist
            Random rand = new Random();
            for( int i = 0; i < size; i++ )
                temp.add(ALPHABET[rand.nextInt(ALPHABET.length)]);
            
            // StringBuilder to construct the string
            StringBuilder stringBuilder = new StringBuilder();

            // Append each character from the ArrayList to the StringBuilder
            for (Character ch : temp) {
                stringBuilder.append(ch);
            }
            // Convert StringBuilder to String
            String str = stringBuilder.toString();
            // Output the string
            System.out.println(str);

            try {
                Record r = new Record(account, userName, str);
                db.addNewRecord(r);
//              // This should refresh the table
                myTablePanel.myTable.setModel(myTablePanel.getTableModel());
                JOptionPane.showMessageDialog(this.getMainPanel(), "Record updated successfully.", "Update", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(main, "Error creating new record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
                myTablePanel.myTable.setModel(myTablePanel.getTableModel());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(main, "Error creating new record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
