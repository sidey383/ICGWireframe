package ru.sidey383.editor;

import ru.sidey383.model.data.FigureInfo;
import ru.sidey383.model.data.FigureInfoRecord;
import ru.sidey383.model.data.Point;
import ru.sidey383.model.data.PointRecord;

import javax.swing.*;
import java.awt.*;
import java.util.DoubleSummaryStatistics;
import java.util.function.Consumer;

public class FigureEditorPanel extends JPanel implements FigureEditor {

    private final Consumer<FigureInfo<PointRecord>> applyCallback;

    private final BSplineDrawPanel bSplineDrawPanel;

    private final ControlPanel controlPanel;

    private final EditedFigure editedFigure;

    public FigureEditorPanel(Consumer<FigureInfo<PointRecord>> applyCallback, FigureInfo<?> figureInfo, Runnable closeSelf) {
        this.applyCallback = applyCallback;
        setLayout(new BorderLayout());
        editedFigure = new EditedFigure(figureInfo, this::onSplineUpdate, this::onViewUpdate);
        bSplineDrawPanel = new BSplineDrawPanel(editedFigure);
        BSplinePanelInteractions bSplinePanelInteractions = new BSplinePanelInteractions(bSplineDrawPanel);
        bSplineDrawPanel.addMouseListener(bSplinePanelInteractions);
        bSplineDrawPanel.addMouseWheelListener(bSplinePanelInteractions);
        bSplineDrawPanel.addMouseMotionListener(bSplinePanelInteractions);
        controlPanel = new ControlPanel(editedFigure, bSplineDrawPanel, this::applySpline, closeSelf, () -> updateViewPose(editedFigure));
        add(bSplineDrawPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void onSplineUpdate() {
        bSplineDrawPanel.repaint();
        controlPanel.onSplineUpdate();
        if (controlPanel.isAutoChange())
            applySpline();
    }

    private void onViewUpdate() {
        bSplineDrawPanel.repaint();
    }

    private void applySpline() {
        FigureInfo<PointRecord> info = new FigureInfoRecord(editedFigure);
        applyCallback.accept(info);
    }

    @Override
    public JComponent showComponent() {
        return this;
    }

    @Override
    public void setFigure(FigureInfo<?> info) {
        editedFigure.setSpline(info);
        updateViewPose(info);
    }

    private void updateViewPose(FigureInfo<?> info) {
        int width = bSplineDrawPanel.getInteractionWidth();
        int height = bSplineDrawPanel.getInteractionHeight();
        if (width == 0 || height == 0)
            return;
        DoubleSummaryStatistics xStat = info.points().stream().mapToDouble(Point::x).summaryStatistics();
        DoubleSummaryStatistics yStat = info.points().stream().mapToDouble(Point::y).summaryStatistics();
        double xScale = width / ((xStat.getMax() - xStat.getMin()) * 1.2);
        double yMin = Math.min(0, yStat.getMin());
        double yMax = Math.max(Double.MIN_NORMAL, yStat.getMax());
        double yScale = height / ((yMax - yMin) * 1.2);
        bSplineDrawPanel.setScale(Math.min(xScale, yScale));
        bSplineDrawPanel.setCenter(xStat.getAverage(), (yMax + yMin) / 2);
    }

}
