package ru.sidey383.show;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;

public class HelpDialog extends JDialog {

    public HelpDialog() {
        super();
        setTitle("Help");
        setSizeAndPosition();
        String html;
        try (InputStream is = Objects.requireNonNull(HelpDialog.class.getResource("/help.html")).openStream()) {
            html = new String(is.readAllBytes());
        } catch (Exception e) {
            html = "Load error";
        }
        add(new JLabel(html));
        pack();
    }

    public void showHelp() {
        if (!isShowing()) {
            setVisible(true);
        } else {
            setAlwaysOnTop(true);
            setAlwaysOnTop(false);
        }
    }

    private void setSizeAndPosition() {
        setMinimumSize(new Dimension(300, 200));
        setPreferredSize(new Dimension(400, 300));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - 400, screenSize.height / 2 - 400);
    }

}
