package executor;

import Backend.DatabaseManager;
import Backend.TextReader;
import Frontend.UXSwing;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Encrypted Password Manager by Aidan");
        UXSwing ux = new UXSwing();
        frame.setContentPane(ux.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 400);
        frame.setVisible(true);
    }
    }













