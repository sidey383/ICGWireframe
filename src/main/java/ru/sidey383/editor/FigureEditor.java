package ru.sidey383.editor;

import ru.sidey383.model.data.FigureInfo;

import javax.swing.*;

public interface FigureEditor {

    JComponent showComponent();

    void setFigure(FigureInfo<?> info);

}
