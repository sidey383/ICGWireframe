package ru.sidey383;

import ru.sidey383.model.data.FigureInfo;
import ru.sidey383.model.data.FigureInfoRecord;
import ru.sidey383.model.data.PointRecord;
import ru.sidey383.model.figure.FigureVertexContainer;
import ru.sidey383.model.math.MatrixTransformation;
import ru.sidey383.show.ShowFrame;
import ru.sidey383.show.ShowFrameInteraction;
import ru.sidey383.show.painter.PerspectiveLinesPainter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends MyFrame {

    public static void main(String[] args) {
        new MainFrame();
    }

    private final ShowFrame<PerspectiveLinesPainter> showFrame;

    private final EditorFrame editorFrame;

    private FigureInfo<?> figureInfo = new FigureInfoRecord(List.of(
            new PointRecord(-2, 5),
            new PointRecord(-1, 3),
            new PointRecord(0, 0),
            new PointRecord(1, 3),
            new PointRecord(2, 5)
    ),
            10,
            5,
            3
    );

    public MainFrame() {
        super("ICGWireframe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        showFrame = new ShowFrame<>(new PerspectiveLinesPainter());
        ShowFrameInteraction showFrameInteraction = new ShowFrameInteraction(showFrame);
        showFrame.addMouseListener(showFrameInteraction);
        showFrame.addMouseMotionListener(showFrameInteraction);
        showFrame.addMouseWheelListener(showFrameInteraction);

        editorFrame = new EditorFrame(figureInfo, this::setFigureInfo);

        add(showFrame, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();

        JButton editorButton = new JButton("Editor");
        editorButton.addActionListener((a) -> openEditor());
        JButton dropAngle = new JButton("Clear angle");
        dropAngle.addActionListener((a) -> showFrame.setRotation(MatrixTransformation.createTranspoitionMatrix()));

        toolBar.add(editorButton);
        toolBar.add(dropAngle);
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        setFigureInfo(figureInfo);

        pack();
        setVisible(true);
    }

    private void setFigureInfo(FigureInfo<?> figureInfo) {
        this.figureInfo = figureInfo;
        showFrame.setLinesSupplier(new FigureVertexContainer(figureInfo));
    }

    private void openEditor() {
        if (!editorFrame.isShowing()) {
            editorFrame.setVisible(true);
            editorFrame.setFigureInfo(figureInfo);
        } else {
            editorFrame.setState(Frame.NORMAL);
            if (!editorFrame.isActive()) {
                editorFrame.setVisible(true);
            }
            editorFrame.setAlwaysOnTop(true);
            editorFrame.setAlwaysOnTop(false);
        }
    }

}
