package executor;

import Backend.DatabaseManager;
import Backend.Record;
import Frontend.UXSwing;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        DatabaseManager db = new DatabaseManager();

        Record pass = new Record("test", "test", "test");
        Record pass2 = new Record("test2", "test2", "test2");

        db.addNewPassword(pass);
        db.addNewPassword(pass2);


        pass.setPassword("Password");
        db.updatePassword(pass);

        JFrame frame = new JFrame("Password Manager");
        UXSwing ux = new UXSwing();

        frame.setContentPane(ux.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 500);
    }



    }



