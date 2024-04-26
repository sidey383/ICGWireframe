package ru.sidey383.show.painter;

import ru.sidey383.model.data.Pair;
import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.math.Vector;
import ru.sidey383.show.LinesSupplier;

import java.awt.*;
import java.util.List;

public class SimpleLinesPainter implements LinesPainter {

    @Override
    public void createImage(LinesSupplier supplier, Matrix figureTransformation, Graphics2D g, int width, int height) {
        List<Pair<Vector>> lines = supplier.createLines(figureTransformation);
        GradientCreator gradientCreator = new GradientCreator(
                lines.stream().mapMultiToDouble((v, c) -> {
                    c.accept(v.first().get(2));
                    c.accept(v.second().get(2));
                }).toArray()
        );
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        int normSize = Math.min(width, height) / 2;
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        for (Pair<Vector> p : lines) {
            int x1 = (int) (normSize * p.first().get(0)) + width / 2;
            int y1 =  (height / 2) - (int) (normSize * p.first().get(1));
            int x2 = (int) (normSize * p.second().get(0)) + width / 2;
            int y2 =  (height / 2) - (int) (normSize * p.second().get(1));
            g.setPaint(new GradientPaint(x1, y1, gradientCreator.getDotColor(p.first().get(2)), x2, y2, gradientCreator.getDotColor(p.first().get(2))));
            g.drawLine(x1, y1, x2, y2);
        }
    }

}
