package ru.sidey383.editor.fields;

import ru.sidey383.editor.ErrorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.IntConsumer;

public class IntField extends JFormattedTextField {

    private final NumberFormat numberFormat;

    private final int defaultValue;

    private final int min;

    private final int max;

    private final IntConsumer applyValue;

    private final ErrorDialog wrongDataDialog;

    public IntField(NumberFormat numberFormat, int value, int min, int max, IntConsumer applyValue) {
        super(numberFormat);
        this.wrongDataDialog = new ErrorDialog(() -> "Wrong data", () -> "Only integer values from " + min + " to " + max + " are available");
        this.applyValue = applyValue;
        this.defaultValue = value;
        this.numberFormat = numberFormat;
        this.min = min;
        this.max = max;
        setText(numberFormat.format(value));
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                textFieldUpdate();
            }
        });
        addActionListener(this::updateField);
    }

    public static IntField createIntField(int value, int min, int max, IntConsumer applyValue) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setParseIntegerOnly(true);
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumIntegerDigits(4);
        return new IntField(numberFormat, value, min, max, applyValue);
    }

    private void updateField(ActionEvent e) {
        textFieldUpdate();
    }

    private void textFieldUpdate() {
        applyValue(getTextValue());
    }

    private void applyValue(int value) {
        applyValue.accept(value);
    }


    public void setValue(int value) {
        setText(numberFormat.format(value));
    }

    public int getTextValue() {
        String text = getText();
        try {
            int val = numberFormat.parse(text).intValue();
            if (val < min) {
                setText(numberFormat.format(min));
                wrongDataDialog.show();
                val = min;
            }
            if (val > max) {
                setText(numberFormat.format(max));
                wrongDataDialog.show();
                val = max;
            }
            return val;
        } catch (ParseException ex) {
            setText(numberFormat.format(defaultValue));
            wrongDataDialog.show();
            return defaultValue;
        }
    }


}
