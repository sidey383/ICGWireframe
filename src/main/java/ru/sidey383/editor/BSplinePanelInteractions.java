package ru.sidey383.editor;

import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * ЛКМ - двигать (точку или себя), выбрать точку
 * ПКМ - добавить точку
 * Shift + ЛКМ - Двигать фигуру
 * Shift + ПКМ - удалить точку
 **/
public class BSplinePanelInteractions extends MouseAdapter {

    private enum ClickStatus {
        NONE, MOVE_SELECTED, MOVE_VIEW, MOVE_FIGURE
    }

    private final BSplineDrawPanel bSplineDrawPanel;

    @NotNull
    private ClickStatus clickStatus = ClickStatus.NONE;

    private double viewMouseXSelected;

    private double viewMouseYSelected;

    private int lastX;

    private int lastY;

    private final ErrorDialog cantRemoveDotDialog = new ErrorDialog(() -> "Error", () -> "The formative cannot have less than 4 points");

    public BSplinePanelInteractions(BSplineDrawPanel bSplineDrawPanel) {
        this.bSplineDrawPanel = bSplineDrawPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int circleDistanceSquare = bSplineDrawPanel.getCircleSize();
        circleDistanceSquare = circleDistanceSquare * circleDistanceSquare;
        EditedFigure.Point clicked = null;
        for (EditedFigure.Point p : bSplineDrawPanel.getEditedFigure().points()) {
            if (distanceSquare(p, e.getX(), e.getY()) < circleDistanceSquare) {
                clicked = p;
                break;
            }
        }
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> {
                if (clicked != null) {
                    bSplineDrawPanel.getEditedFigure().setSelected(clicked);
                    if (e.isShiftDown()) {
                        clickStatus = ClickStatus.MOVE_FIGURE;
                    } else {
                        clickStatus = ClickStatus.MOVE_SELECTED;
                    }
                } else {
                    clickStatus = ClickStatus.MOVE_VIEW;
                    viewMouseXSelected = bSplineDrawPanel.getPointX(e.getX());
                    viewMouseYSelected = bSplineDrawPanel.getPointY(e.getY());
                }
            }
            case MouseEvent.BUTTON3 -> {
                if (e.isShiftDown()) {
                    if (clicked != null) {
                        try {
                            bSplineDrawPanel.getEditedFigure().removePoint(clicked);
                        } catch (IllegalStateException ex) {
                            cantRemoveDotDialog.show();
                        }
                    }
                } else {
                    bSplineDrawPanel.getEditedFigure().addPoint(
                            bSplineDrawPanel.getPointX(e.getX()),
                            bSplineDrawPanel.getPointY(e.getY())
                    );
                }
            }
        }
        lastX = e.getX();
        lastY = e.getY();
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        bSplineDrawPanel.setScale(bSplineDrawPanel.getScale() - e.getWheelRotation() * 4);
        updateMousePose(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updateMousePose(e.getX(), e.getY());
    }

    public void updateMousePose(int x, int y) {
        switch (clickStatus) {
            case NONE -> {
            }
            case MOVE_VIEW -> {
                int pixelXDif = bSplineDrawPanel.getInteractionWidth() / 2 - x;
                int pixelYDif = bSplineDrawPanel.getInteractionHeight() / 2 - y;
                double valueXDiff = pixelXDif / bSplineDrawPanel.getScale();
                double valueYDiff = pixelYDif / bSplineDrawPanel.getScale();
                bSplineDrawPanel.setCenter(viewMouseXSelected + valueXDiff, viewMouseYSelected - valueYDiff);
            }
            case MOVE_FIGURE -> {
                double valueXDiff = (x - lastX) / bSplineDrawPanel.getScale();
                double valueYDiff = (lastY - y) / bSplineDrawPanel.getScale();
                bSplineDrawPanel.getEditedFigure().move(valueXDiff, valueYDiff);
            }
            case MOVE_SELECTED -> {
                double xP = bSplineDrawPanel.getPointX(x);
                double yP = bSplineDrawPanel.getPointY(y);
                EditedFigure.Point p = bSplineDrawPanel.getEditedFigure().getSelected();
                if (p != null) {
                    p.set(xP, yP);
                }
            }
        }

        lastX = x;
        lastY = y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickStatus = ClickStatus.NONE;
        updateMousePose(e.getX(), e.getY());
    }

    private int distanceSquare(EditedFigure.Point point, int x, int y) {
        int dx = bSplineDrawPanel.getScreenX(point.x()) - x;
        int dy = bSplineDrawPanel.getScreenY(point.y()) - y;
        return dx * dx + dy * dy;
    }
}
