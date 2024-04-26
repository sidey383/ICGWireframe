package ru.sidey383.show;

import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.math.MatrixTransformation;
import ru.sidey383.show.painter.PerspectiveLinesPainter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class ShowFrameInteraction extends MouseAdapter {

    private enum MoveMode {
        NONE, XYRotate, ZRotate
    }

    private final ShowFrame<PerspectiveLinesPainter> frame;

    private MoveMode mode = MoveMode.NONE;

    private int lastX = 0;

    private int lastY = 0;

    public ShowFrameInteraction(ShowFrame<PerspectiveLinesPainter> frame) {
        this.frame = frame;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            mode = MoveMode.XYRotate;
        if (e.getButton() == MouseEvent.BUTTON3)
            mode = MoveMode.ZRotate;
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (mode) {
            case XYRotate -> {
                int dx = e.getX() - lastX;
                int dy = e.getY() - lastY;
                Matrix xRot = null;
                Matrix yRot = null;
                if (dx != 0)
                    xRot = MatrixTransformation.Y.createRotationMatrix(-Math.PI * dx / frame.getScreenWidth());
                if (dy != 0)
                    yRot = MatrixTransformation.X.createRotationMatrix(-Math.PI * dy / frame.getScreenHeight());
                if (xRot != null) {
                    if (yRot != null)
                        frame.addTransformation(xRot.multiply(yRot));
                    else
                        frame.addTransformation(xRot);
                } else {
                    if (yRot != null)
                        frame.addTransformation(yRot);
                }
            }
            case ZRotate -> {
                int width = frame.getWidth();
                int height = frame.getHeight();
                int dx = e.getX() - lastX;
                int dy = e.getY() - lastY;
                int px = e.getX() - width / 2;
                int py = e.getY() - height / 2;
                if (px == 0 && py == 0)
                    break;
                double rot = (double) (py * dx - px * dy) / (px * px + py * py) ;
                if (rot != 0)
                    frame.addTransformation(MatrixTransformation.Z.createRotationMatrix(rot));
            }
            default -> {}
        }

        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mode = MoveMode.NONE;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        frame.getPainter().setFov(frame.getPainter().getFov() - e.getWheelRotation() * 0.05);
        frame.repaint();
    }
}
