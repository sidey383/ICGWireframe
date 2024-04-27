package ru.sidey383.show.painter;

import lombok.Getter;
import ru.sidey383.model.data.Pair;
import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.math.MatrixTransformation;
import ru.sidey383.model.math.PerspectiveProjectionMatrix;
import ru.sidey383.model.math.Vector;
import ru.sidey383.show.LinesSupplier;

import java.awt.*;
import java.util.List;

public class PerspectiveLinesPainter implements LinesPainter {


    private final Matrix cameraMoveMatrix = MatrixTransformation.getTransposition(0, 0, 8);

    private PerspectiveProjectionMatrix projection;

    private Matrix totalMatrix;

    @Getter
    private final double n = 5;

    @Getter
    private final double f = 20;

    @Getter
    private double fov = Math.PI / 8;

    @Override
    public void createImage(LinesSupplier supplier, Matrix figureTransformation, Graphics2D g, int width, int height) {
        List<Pair<Vector>> lines = supplier.createLines(getProjection(width, height).multiply(figureTransformation));
        GradientCreator gradientCreator = new GradientCreator(
                lines.stream().mapMultiToDouble((v, c) -> {
                    c.accept(v.first().get(2) / v.first().get(3));
                    c.accept(v.second().get(2) / v.second().get(3));
                }).toArray()
        );
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        for (Pair<Vector> p : lines) {
            Vector v1 = p.first();
            Vector v2 = p.second();
            double x1Pose = v1.get(0) / (2 * v1.get(3));
            double y1Pose = v1.get(1) / (2 * v1.get(3));
            double x2Pose = v2.get(0) / (2 * v2.get(3));
            double y2Pose = v2.get(1) / (2 * v2.get(3));
            int x1 = (int) (width * x1Pose) + width / 2;
            int y1 = (height / 2) - (int) (height * y1Pose);
            int x2 = (int) (width * x2Pose) + width / 2;
            int y2 = (height / 2) - (int) (height * y2Pose);
            g.setPaint(
                    new GradientPaint(
                            x1, y1, gradientCreator.getDotColor(v1.get(2) / v1.get(3)),
                            x2, y2, gradientCreator.getDotColor(v2.get(2) / v2.get(3))
                    )
            );
            g.setStroke(new BasicStroke(2));
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private Matrix getProjection(int width, int height) {
        synchronized (this) {
            if (projection == null || projection.getAspects() != (double) width / height) {
                projection = new PerspectiveProjectionMatrix(fov, (double) width / height, n, f);
                totalMatrix = projection.multiply(cameraMoveMatrix);
            }
        }
        return totalMatrix;
    }

    public void setFov(double fov) {
        fov = Math.min(Math.PI / 4, Math.max(Math.PI / 32, fov));
        if (this.fov != fov) {
            synchronized (this) {
                this.fov = fov;
                projection = null;
            }
        }
    }

}
