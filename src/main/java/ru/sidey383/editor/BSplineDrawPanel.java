package ru.sidey383.editor;

import lombok.Getter;
import ru.sidey383.model.data.PointRecord;
import ru.sidey383.model.figure.BSplinePointsSupplier;

import javax.swing.*;
import java.awt.*;

@Getter
public class BSplineDrawPanel extends JPanel {

    private static final Color background_color = Color.BLACK;

    private static final Color axis_color = Color.WHITE;

    private static final Color markup_color = Color.DARK_GRAY;

    private static final Color simple_points_color = Color.BLUE;

    private static final Color selected_points_color = Color.GREEN;

    private static final Color b_splie_color = Color.YELLOW;

    private final int circleSize = 20;

    private double scale = 100; // pixel / value

    private double centerX = 0;

    private double centerY = 2;

    private final EditedFigure editedFigure;

    private final BSplinePointsSupplier bSplinePointsSupplier;

    public BSplineDrawPanel(EditedFigure editedFigure) {
        this.editedFigure = editedFigure;
        this.bSplinePointsSupplier = new BSplinePointsSupplier(editedFigure);
        setBackground(background_color);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.width <= 0 || dimension.height <= 0)
            return;
        drawAxis(g);
        drawDots(g);
        drawBSpline(g);
    }

    private void drawDots(Graphics g) {
        g.setColor(simple_points_color);
        EditedFigure.Point last = null;
        for (EditedFigure.Point p : editedFigure.points()) {
            if (last != null)
                g.drawLine(getScreenX(p.x()), getScreenY(p.y()), getScreenX(last.x()), getScreenY(last.y()));
            boolean isSelected = editedFigure.isSelected(p);
            if (isSelected)
                g.setColor(selected_points_color);
            g.drawOval(getScreenX(p.x()) - circleSize / 2, getScreenY(p.y()) - circleSize / 2, circleSize, circleSize);
            if (isSelected)
                g.setColor(simple_points_color);
            last = p;
        }
    }

    private void drawBSpline(Graphics g) {
        g.setColor(b_splie_color);
        PointRecord last = null;
        for (PointRecord p : bSplinePointsSupplier.calculatePoints()) {
            if (last != null)
                g.drawLine(getScreenX(p.x()), getScreenY(p.y()), getScreenX(last.x()), getScreenY(last.y()));
            last = p;
        }
    }

    private void drawAxis(Graphics g) {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.width <= 0 || dimension.height <= 0)
            return;
        int width = dimension.width;
        int height = dimension.height;
        int uAxis = getScreenX(0);
        int vAxis = getScreenY(0);
        for (int u = (int) Math.floor(centerX - width / scale); u < Math.ceil(centerX + width / scale); u++) {
            g.setColor(markup_color);
            g.drawLine(getScreenX(u), 0, getScreenX(u), height);
            g.setColor(axis_color);
            g.drawLine(getScreenX(u), vAxis - 4, getScreenX(u), vAxis + 4);
        }

        for (int v = (int) Math.floor(centerY - height / scale); v < Math.ceil(centerY + height / scale); v++) {
            g.setColor(markup_color);
            g.drawLine(0, getScreenY(v), width, getScreenY(v));
            g.setColor(axis_color);
            g.drawLine(uAxis - 4, getScreenY(v), uAxis + 4, getScreenY(v));
        }
        g.setColor(axis_color);
        g.drawLine(uAxis, 0, uAxis, height);
        g.drawLine(0, vAxis, width, vAxis);


    }

    public int getScreenX(double x) {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.width == 0)
            return 0;
        int width = dimension.width;
        return (int) ((x - centerX) * scale) + width / 2 ;
    }

    public int getScreenY(double y) {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.height == 0)
            return 0;
        int height = dimension.height;
        return height / 2 - (int) ((y - centerY) * scale);
    }

    public double getPointX(int x) {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.width == 0)
            return 0;
        int width = dimension.width;
        return (double) (x - width / 2) / scale + centerX;
    }

    public double getPointY(int y) {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.height == 0)
            return 0;
        int height = dimension.height;
        return (double) (-y + height / 2 ) / scale + centerY;
    }

    public void setScale(double scale) {
        this.scale = Math.min(200, Math.max(20, scale));
        repaint();
    }

    public void setCenter(double x, double y) {
        this.centerX = x;
        this.centerY = y;
        repaint();
    }

    public int getInteractionWidth() {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.width <= 0)
            return 1;
        return dimension.width;
    }

    public int getInteractionHeight() {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.height <= 0)
            return 1;
        return dimension.height;
    }

}
