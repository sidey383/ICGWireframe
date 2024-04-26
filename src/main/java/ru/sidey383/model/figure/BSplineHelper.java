package ru.sidey383.model.figure;

import ru.sidey383.model.data.BSplineDots;
import ru.sidey383.model.math.Vector;

record BSplineHelper(BSplineDots<?> points) {

    public int partCount() {
        return points.points().size() - 3;
    }

    public Vector getBaseVectorX(int splineNumber) {
        if (splineNumber >= partCount())
            throw new IllegalArgumentException("Spline number out of range");
        return new Vector() {
            @Override
            public int size() {
                return 4;
            }

            @Override
            public double get(int i) {
                return points.points().get(splineNumber + i).x();
            }
        };
    }

    public Vector getBaseVectorY(int splineNumber) {
        if (splineNumber >= partCount())
            throw new IllegalArgumentException("Spline number out of range");
        return new Vector() {
            @Override
            public int size() {
                return 4;
            }

            @Override
            public double get(int i) {
                return points.points().get(splineNumber + i).y();
            }
        };
    }

}
