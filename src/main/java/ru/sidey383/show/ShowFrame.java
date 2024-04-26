package ru.sidey383.show;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.math.MatrixTransformation;
import ru.sidey383.show.painter.AxisPainter;
import ru.sidey383.show.painter.LinesPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShowFrame<T extends LinesPainter> extends JPanel {

    @Nullable
    private LinesSupplier linesSupplier;

    private final AxisPainter axisPainter = new AxisPainter(80);

    @Nullable
    private Matrix rotation;

    @NotNull
    private final T painter;

    public ShowFrame(@NotNull T painter) {
        this.painter = painter;
        this.rotation = MatrixTransformation.createTranspoitionMatrix();
        setBackground(Color.WHITE);
    }

    public void setLinesSupplier(@Nullable LinesSupplier linesSupplier) {
        this.linesSupplier = linesSupplier;
        repaint();
    }

    public void setRotation(@Nullable Matrix rotation) {
        this.rotation = rotation;
        repaint();
    }

    public void addTransformation(@NotNull Matrix rotation) {
        if (this.rotation == null)
            return;
        this.rotation = rotation.multiply(this.rotation);
        repaint();
    }

    public @NotNull T getPainter() {
        return painter;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = getVisibleRect().getSize();
        if (size.height == 0 || size.width == 0)
            return;
        drawFigure(g, size.width, size.height);
        axisPainter.drawAxis(g, rotation);
    }

    private void drawFigure(Graphics g, int width, int height) {
        if (linesSupplier == null || rotation == null)
            return;
        if (g instanceof  Graphics2D g2) {
            painter.createImage(linesSupplier, rotation, g2, width, height);
        } else {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2 = image.createGraphics();
            painter.createImage(linesSupplier, rotation, g2, width, height);
            g2.dispose();
            g.drawImage(image, 0, 0, null);
        }
    }

    public int getScreenWidth() {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.width <= 0)
            return 1;
        return dimension.width;
    }

    public int getScreenHeight() {
        Dimension dimension = getVisibleRect().getSize();
        if (dimension.height <= 0)
            return 1;
        return dimension.height;
    }

}
