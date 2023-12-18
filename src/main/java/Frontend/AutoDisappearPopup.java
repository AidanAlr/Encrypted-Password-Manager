package Frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoDisappearPopup extends JComponent {
    private JDialog dialog;
    private JLabel messageLabel;
    private final int displayTime;

    public AutoDisappearPopup(JFrame parentFrame, String title, String Message, int width, int height, int displayTimeInMillis) {
        this.displayTime = displayTimeInMillis;
        initializeDialog(parentFrame, title, Message, width, height);
    }

    private void initializeDialog(JFrame parentFrame, String title, String initialMessage, int width, int height) {
        dialog = new JDialog(parentFrame, title, false);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(parentFrame);

        // Initialize and add the message label
        messageLabel = new JLabel(initialMessage, SwingConstants.CENTER);
        dialog.add(messageLabel);
    }

    public void showDialog() {
        Timer timer = new Timer(displayTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

}
