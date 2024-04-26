package ru.sidey383;

import ru.sidey383.editor.FigureEditor;
import ru.sidey383.editor.FigureEditorPanel;
import ru.sidey383.model.data.FigureInfo;
import ru.sidey383.model.data.PointRecord;

import java.awt.*;
import java.util.function.Consumer;

public class EditorFrame extends MyFrame {

    private final FigureEditor figureEditor;

    public EditorFrame(FigureInfo<?> figure, Consumer<FigureInfo<PointRecord>> figureInfoConsumer) {
        super("ICGWireframe editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setFocusable(true);
        this.figureEditor = new FigureEditorPanel(figureInfoConsumer, figure, () -> setVisible(false));
        add(figureEditor.showComponent(), BorderLayout.CENTER);
        pack();
    }

    public void setFigureInfo(FigureInfo<?> figureInfo) {
        figureEditor.setFigure(figureInfo);
    }

}
