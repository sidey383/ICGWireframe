package ru.sidey383.show.painter;

import ru.sidey383.model.math.Matrix;
import ru.sidey383.show.LinesSupplier;

import java.awt.*;

public interface LinesPainter {

    void createImage(LinesSupplier supplier, Matrix figureTransformation, Graphics2D g, int width, int height);

}
