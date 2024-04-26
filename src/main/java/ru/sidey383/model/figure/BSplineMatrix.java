package ru.sidey383.model.figure;

import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.math.Vector;
import ru.sidey383.model.math.VectorRecord;

class BSplineMatrix implements Matrix {

    private static final int[][] matrix = new int[][]{
            {-1, 3, -3, 1},
            {3, -6, 3, 0},
            {-3, 0, 3, 0},
            {1, 4, 1, 0}
    };

    private static final int divider = 6;

    @Override
    public Vector multiply(Vector v) throws IllegalArgumentException {
        if (v.size() != columns())
            throw new IllegalArgumentException("Wrong vector size, actual " + v.size() + ", but expect " + columns());
        double[] result = new double[v.size()];
        for (int i = 0; i < rows(); i++) {
            for (int j = 0; j < columns(); j++) {
                result[i] += v.get(j) * matrix[i][j];
            }
            result[i] /= divider;
        }
        return new VectorRecord(result);
    }

    @Override
    public int rows() {
        return 4;
    }

    @Override
    public int columns() {
        return 4;
    }

    @Override
    public double get(int i, int j) throws IllegalArgumentException {
        return (double) matrix[i][j] / divider;
    }
}
