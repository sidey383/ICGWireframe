package ru.sidey383;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public MyFrame(String title) {
        super(title);
        setSizeAndPosition();
    }

    private void setSizeAndPosition() {
        setMinimumSize(new Dimension(640, 480));
        setPreferredSize(new Dimension(800, 800));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - 400, screenSize.height / 2 - 400);
    }

}
