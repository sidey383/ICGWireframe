package ru.sidey383.show.painter;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

public class GradientCreator {
    private final double min;
    private final double max;

    public GradientCreator(double[] distances) {
        max = Arrays.stream(distances).max().orElse(0);
        min = Arrays.stream(distances).min().orElse(1);
    }

    public Color getDotColor(double value) {
        double distance = (value - min) / (max - min);
        return new Color((int) (255 / (2 - distance)), (int) (255 / (1 + distance)), 128);
    }

}