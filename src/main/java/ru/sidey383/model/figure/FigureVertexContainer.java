package ru.sidey383.model.figure;

import ru.sidey383.model.data.*;
import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.math.MatrixTransformation;
import ru.sidey383.model.math.Vector;
import ru.sidey383.model.math.VectorRecord;
import ru.sidey383.show.LinesSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FigureVertexContainer implements LinesSupplier {

    private final Vector[][] vectors;

    private final int partCount;

    private final int partSize;

    private final int m;

    private final int m1;

    private FigureVertexContainer(int partCount, int partSize, int m, int m1) {
        this.partSize = partSize;
        this.partCount = partCount;
        this.m = m;
        this.m1 = m1;
        vectors = new Vector[m * m1][];
        for (int i = 0; i < m * m1; i++) {
            vectors[i] = i % m1 == 0 ? new Vector[partCount * partSize + 1] : new Vector[partCount + 1];
        }
    }

    public FigureVertexContainer(FigureInfo<? extends Point> figureInfo) {
        this(figureInfo.points().size() - 3, figureInfo.n(), figureInfo.m(), figureInfo.m1());
        calculateBaseVectors(figureInfo);
        normalizeVectors();
    }

    public FigureVertexContainer applyMatrix(Matrix matrix) {
        FigureVertexContainer transformed = new FigureVertexContainer(partCount, partSize, m, m1);
        for (int rot = 0; rot < m1 * m; rot++) {
            if (rot % m1 == 0) {
                for (int i = 0; i <= partCount * partSize; i++) {
                    transformed.setValue(rot, i, matrix.multiply(getValue(rot, i)));
                }
            } else {
                for (int i = 0; i <= partCount; i++) {
                    transformed.setValue(rot, i * partSize, matrix.multiply(getValue(rot, i * partSize)));
                }
            }
        }
        return transformed;
    }

    @Override
    public List<Pair<Vector>> createLines(Matrix transformation) {
        return applyMatrix(transformation).createLines();
    }

    public List<Pair<Vector>> createLines() {
        List<Pair<Vector>> lines = new ArrayList<>();
        for (int num = 0; num < this.getFormativeCount(); num ++) {
            for (int i = 1; i < this.getFormativeSize(); i++) {
                lines.add(new Pair<>(
                        getFormativePoint(num, i - 1),
                        getFormativePoint(num, i)
                ));
            }
        }
        for (int num = 0; num < getCircleCount(); num ++) {
            for (int i = 0; i <= getCircleSize(); i++) {
                lines.add(new Pair<>(
                        getCirclePoint(num, i),
                        getCirclePoint(num, i + 1)
                ));
            }
        }
        return lines;
    }

    public int getCircleCount() {
        return partCount + 1;
    }

    public int getCircleSize() {
        return m * m1;
    }

    public Vector getCirclePoint(int num, int i) {
        return getValue(i, num * partSize);
    }

    public int getFormativeCount() {
        return m;
    }

    public int getFormativeSize() {
        return partCount * partSize + 1;
    }

    public Vector getFormativePoint(int num, int i) {
        return getValue(num * m1, i);
    }

    private void calculateBaseVectors(BSplineInfo<? extends Point> splineInfo) {
        List<PointRecord> points = new BSplinePointsSupplier(splineInfo).calculatePoints();
        for (int angle = 0; angle < m * m1; angle++) {
            Matrix rotation = MatrixTransformation.X.createRotationMatrix(angle * Math.PI * 2 / (m * m1));
            if (angle % m1 == 0) {
                for (int part = 0; part < partCount; part++) {
                    for (int num = 0; num < partSize; num++) {
                        setValue(
                                angle,
                                part * partSize + num,
                                rotation.multiply(
                                        new VectorRecord(
                                                points.get(part * partSize + num).x(),
                                                points.get(part * partSize + num).y(),
                                                0,
                                                1
                                        )
                                )
                        );
                    }
                    if (part == partCount - 1) {
                        setValue(
                                angle,
                                partCount * partSize,
                                rotation.multiply(
                                        new VectorRecord(
                                                points.get(partCount * partSize).x(),
                                                points.get(partCount * partSize).y(),
                                                0,
                                                1
                                        )
                                )
                        );
                    }
                }
            } else {
                for (int part = 0; part <= partCount; part++) {
                    setValue(
                            angle,
                            part * partSize,
                            rotation.multiply(
                                    new VectorRecord(
                                            points.get(part * partSize).x(),
                                            points.get(part * partSize).y(),
                                            0,
                                            1
                                    )
                            )
                    );
                }
            }
        }
    }

    private void normalizeVectors() {
        double[] extreme = getExtremeCoordinates();
        double[] center = new double[] {
                (extreme[0] + extreme[3]) / 2,
                (extreme[1] + extreme[4]) / 2,
                (extreme[2] + extreme[5]) / 2
        };
        final double devider = Math.max(extreme[0] - center[0], Math.max(extreme[1] - center[1], extreme[2] - center[2]));
        for (int angle = 0; angle < m * m1; angle++) {
            if (angle % m1 == 0) {
                for (int part = 0; part <= partCount * partSize; part++) {
                    Vector v = vectors[angle][part];
                    vectors[angle][part] = new VectorRecord(
                            (v.get(0) - center[0]) / devider,
                            (v.get(1) - center[1]) / devider,
                            (v.get(2) - center[2]) / devider,
                            v.get(3)
                    );
                }
            } else {
                for (int part = 0; part <= partCount; part++) {
                    Vector v = vectors[angle][part];
                    vectors[angle][part] = new VectorRecord(
                            (v.get(0) - center[0]) / devider,
                            (v.get(1) - center[1]) / devider,
                            (v.get(2) - center[2]) / devider,
                            v.get(3)
                    );
                }
            }
        }
    }

    private double[] getExtremeCoordinates() {
        double[] extreme = new double[] {
                vectors[0][0].get(0),
                vectors[0][0].get(1),
                vectors[0][0].get(2),
                vectors[0][0].get(0),
                vectors[0][0].get(1),
                vectors[0][0].get(2)
        };
        for (int angle = 0; angle < m * m1; angle++) {
            if (angle % m1 == 0) {
                for (int part = angle == 0 ? 1 : 0; part <= partCount * partSize; part++) {
                    extremeConsumeVector(extreme, angle, part);
                }
            } else {
                for (int part = 0; part <= partCount; part++) {
                    extremeConsumeVector(extreme, angle, part);
                }
            }
        }
        return extreme;
    }

    private void extremeConsumeVector(double[] extreme, int angle, int part) {
        Vector v = vectors[angle][part];
        extreme[0] = Math.max(extreme[0], v.get(0));
        extreme[1] = Math.max(extreme[1], v.get(1));
        extreme[2] = Math.max(extreme[2], v.get(2));
        extreme[3] = Math.min(extreme[3], v.get(0));
        extreme[4] = Math.min(extreme[4], v.get(1));
        extreme[5] = Math.min(extreme[5], v.get(2));
    }

    private Vector getValue(int rot, int pos) {
        return vectors[rot % (m1 * m)][getArrayPose(rot, pos)];
    }

    private void setValue(int rot, int pos, Vector vector) {
        int i = rot % (m1 * m);
        int j = getArrayPose(rot, pos);
        Vector[] l = vectors[i];
        l[j] = vector;
    }

    private int getArrayPose(int rot, int pos) {
        if (rot % m1 == 0) {
            if (pos > partCount * partSize)
                throw new IllegalArgumentException("Index out of range");
            return pos;
        } else {
            pos = pos / partSize;
            if (pos > partCount)
                throw new IllegalArgumentException("Index out of range");
            return pos;
        }
    }

}
