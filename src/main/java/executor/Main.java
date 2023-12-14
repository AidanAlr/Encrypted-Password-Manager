package executor;

import Backend.DatabaseManager;
import Frontend.UXSwing;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Password Manager");
        frame.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        DatabaseManager db = new DatabaseManager();
//        db.clearPasswords();
        UXSwing ux = new UXSwing();
        frame.setContentPane(ux.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
    }



