package Frontend;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;



public class UXSwing {

    private JPanel main;
    private JTextField accountTextField;
    private JButton submitButton;
    private MyTablePanel custom_table_panel;
    private JButton RNGButton;
    private JButton newButton;


    private void createUIComponents() throws SQLException {
        custom_table_panel = new MyTablePanel();
    }

    public Container getMainPanel() {
        return main;
    }

    public JTextField getAccountTextField(){return accountTextField;}

}

