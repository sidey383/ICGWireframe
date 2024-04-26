package ru.sidey383.model.math;

import lombok.Getter;

public class PerspectiveProjectionMatrix implements Matrix {

    private final double[][] values;


    @Getter
    private final double fovy;
    @Getter
    private final double aspects;
    @Getter
    private final double n;
    @Getter
    private final double f;

    /**
     * @param fovy - Угол обзора в радианах
     * @param aspects - Соотношение сторон weight / height
     * @param n - Расстояние до ближней плоскости отсечения
     * @param f - Расстояние до дальней плоскости отсечения
     * **/
    public PerspectiveProjectionMatrix(double fovy, double aspects, double n, double f) {
        this.fovy = fovy;
        this.aspects = aspects;
        this.n = n;
        this.f = f;
        values = new double[4][4];
        values[0][0] = 1 / (Math.tan(fovy /2) * aspects);
        values[1][1] = 1 / Math.tan(fovy /2);
        values[2][2] = (f + n) / (f - n);
        values[2][3] = (-2 * f * n) / (f - n);
        values[3][2] = 1;
    }

    private PerspectiveProjectionMatrix(double[][] values, double fovy, double aspects, double n, double f) {
        this.values = values;
        this.fovy = fovy;
        this.aspects = aspects;
        this.n = n;
        this.f = f;
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
    public double get(int i, int j) {
        return values[i][j];
    }

    public PerspectiveProjectionMatrix addBefore(Matrix matrix) {
        if (matrix.columns() != 4 || matrix.rows() != 4)
            throw new IllegalArgumentException("Wrong matrix size, actual (" + matrix.columns() + "," + matrix.rows() + "), but expect (4,4)");
        double[][] newMatrix = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int r = 0; r < 4; r++) {
                    newMatrix[i][j] +=   get(i, r) * matrix.get(r, j);
                }
            }
        }
        return new PerspectiveProjectionMatrix(newMatrix, fovy, aspects, n, f);
    }

    public Vector toScreen(Vector v) throws IllegalArgumentException {
        if (v.size() != 4)
            throw new IllegalArgumentException("Wrong vector size, actual " + v.size() + ", but expect " + columns());
        double[] result = new double[v.size()];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i] += v.get(j) * values[i][j];
            }
        }
        if (result[3] == 0)
            return new VectorRecord(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        return new VectorRecord(result[0] / result[3], result[1] / result[3], result[2] / result[3]);
    }

}
