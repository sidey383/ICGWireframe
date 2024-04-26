package ru.sidey383.model.math;

import java.util.Arrays;

public record VectorRecord(double... values) implements Vector {
    @Override
    public int size() {
        return values.length;
    }

    @Override
    public double get(int i) {
        return values[i];
    }

    @Override
    public Vector resize(int size, Double... modifications) {
        double[] newValues = Arrays.copyOf(values, size);
        for (int i = 0; i < modifications.length; i++) {
            if (modifications[i] == null)
                continue;
            newValues[i] = modifications[i];
        }
        return new VectorRecord(newValues);
    }

    @Override
    public String toString() {
        return "VectorRecord{" +
               "values=" + Arrays.toString(values) +
               '}';
    }
}
