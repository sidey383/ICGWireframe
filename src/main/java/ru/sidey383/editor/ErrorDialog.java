package ru.sidey383.editor;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class ErrorDialog {

    private JDialog dialog;

    private final Supplier<String> title;

    private final Supplier<String> text;

    public ErrorDialog(Supplier<String> title, Supplier<String> text) {
        this.title = title;
        this.text = text;
    }

    public void show() {
        if (dialog != null && dialog.isShowing())
            return;
        dialog = new JDialog();
        dialog.setTitle(title.get());
        dialog.setPreferredSize(new Dimension(400, 100));
        dialog.setMinimumSize(new Dimension(200, 100));
        dialog.add(new JLabel(text.get()), BorderLayout.CENTER);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

}
