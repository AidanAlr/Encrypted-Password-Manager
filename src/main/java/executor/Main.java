package executor;

import Backend.EncryptionHelper;
import Frontend.UXSwing;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Password Manager");
        UXSwing ux = new UXSwing();
        frame.setContentPane(ux.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
    }



