package ru.sidey383.model.figure;

import ru.sidey383.model.math.Vector;

class TVector implements Vector {

    private double t = 0;

    TVector() {
    }

    public void setT(double t) {
        this.t = t;
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public double get(int i) {
        return switch (i) {
            case 0 -> t * t * t;
            case 1 -> t * t;
            case 2 -> t;
            case 3 -> 1;
            default -> throw new IllegalArgumentException("Out of range");
        };
    }

}