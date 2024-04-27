package ru.sidey383.show.painter;

import ru.sidey383.model.data.Pair;
import ru.sidey383.model.math.*;

import java.awt.*;

public class AxisPainter {


    private final int axisSize;

    private final PerspectiveProjectionMatrix projection = new PerspectiveProjectionMatrix(Math.PI / 8, 1, 5, 20).addBefore(
            MatrixTransformation.getTransposition(0, 0, 10)
    );

    private final Pair<Vector> xAxis = new Pair<>(new VectorRecord(0, 0, 0, 1), new VectorRecord(1, 0, 0, 1));

    private final Pair<Vector> yAxis = new Pair<>(new VectorRecord(0, 0, 0, 1), new VectorRecord(0, 1, 0, 1));

    private final Pair<Vector> zAxis = new Pair<>(new VectorRecord(0, 0, 0, 1), new VectorRecord(0, 0, 1, 1));

    public AxisPainter(int axisSize) {
        this.axisSize = axisSize;
    }

    public void drawAxis(Graphics g, Matrix rotation) {
        PerspectiveProjectionMatrix projectionMatrix = projection.addBefore(rotation);
        g.setColor(Color.RED);
        Pair<Point> points = xAxis.apply((v) -> axisTransform(v, projectionMatrix));
        g.drawLine(points.first().x, axisSize - points.first().y, points.second().x, axisSize - points.second().y);
        g.setColor(Color.GREEN);
        points = yAxis.apply((v) -> axisTransform(v, projectionMatrix));
        g.drawLine(points.first().x, axisSize - points.first().y, points.second().x, axisSize - points.second().y);
        g.setColor(Color.BLUE);
        points = zAxis.apply((v) -> axisTransform(v, projectionMatrix));
        g.drawLine(points.first().x, axisSize - points.first().y, points.second().x, axisSize - points.second().y);
    }

    private Point axisTransform(Vector v, PerspectiveProjectionMatrix transform) {
        Vector rv = transform.toScreen(v);
        return new Point((int) (rv.get(0) * axisSize / 2 + axisSize / 2), (int) (rv.get(1) * axisSize / 2 + axisSize / 2));
    }

}
