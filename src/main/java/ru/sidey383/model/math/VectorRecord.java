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
    public String toString() {
        return "VectorRecord{" +
               "values=" + Arrays.toString(values) +
               '}';
    }

}
