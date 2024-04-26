package ru.sidey383.editor;

import ru.sidey383.editor.fields.IntField;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final JRadioButton autoChangeButton = new JRadioButton("auto change");

    private final IntField textFieldK;
    private final IntField textFieldN;
    private final IntField textFieldM;
    private final IntField textFieldM1;
    private final EditedFigure editedFigure;

    public ControlPanel(EditedFigure editedFigure, BSplineDrawPanel bSplineDrawPanel, Runnable apply, Runnable close, Runnable updateDotsViewPose) {
        this.editedFigure = editedFigure;
        this.textFieldK = IntField.createIntField(editedFigure.k(), 4, 1000, editedFigure::k);
        this.textFieldN = IntField.createIntField(editedFigure.n(), 1, 1000, editedFigure::n);
        this.textFieldM = IntField.createIntField(editedFigure.m(), 2, 1000, editedFigure::m);
        this.textFieldM1 = IntField.createIntField(editedFigure.m1(), 1, 1000, editedFigure::m1);

        JButton okButton = new JButton("OK");

        okButton.addActionListener((a) -> {
                    close.run();
                    apply.run();
                }
        );

        JButton applyButton = new JButton("Apply");

        applyButton.addActionListener((a) -> apply.run());

        JButton zoomInButton = new JButton("Zoom in");
        JButton zoomOutButton = new JButton("Zoom out");

        JButton normalizeFigure = new JButton("Normalize figure");
        JButton normalizeView = new JButton("Normalize view");
        normalizeView.addActionListener((a) -> updateDotsViewPose.run());
        normalizeFigure.addActionListener((a) -> editedFigure.normalize());

        zoomInButton.addActionListener((a) -> bSplineDrawPanel.setScale(bSplineDrawPanel.getScale() * 1.2));
        zoomOutButton.addActionListener((a) -> bSplineDrawPanel.setScale(bSplineDrawPanel.getScale() * 0.8));
        autoChangeButton.addActionListener((a) -> {
            if (autoChangeButton.isSelected()) {
                apply.run();
            }
        });

        setLayout(new GridLayout(3, 5, 5, 5));
        add(new JLabel("K", JLabel.RIGHT));
        add(textFieldK);
        add(new JLabel("N", JLabel.RIGHT));
        add(textFieldN);
        add(okButton);
        add(new JLabel("M", JLabel.RIGHT));
        add(textFieldM);
        add(new JLabel("M1", JLabel.RIGHT));
        add(textFieldM1);
        add(applyButton);
        add(autoChangeButton);
        add(zoomInButton);
        add(zoomOutButton);
        add(normalizeFigure);
        add(normalizeView);
    }

    public void onSplineUpdate() {
        textFieldK.setValue(editedFigure.k());
        textFieldN.setValue(editedFigure.n());
        textFieldM.setValue(editedFigure.m());
        textFieldM1.setValue(editedFigure.m1());
    }

    public boolean isAutoChange() {
        return autoChangeButton.isSelected();
    }

}
