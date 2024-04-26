package ru.sidey383.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FigureInfoRecord(
        @JsonProperty("Points") List<PointRecord> points,
        @JsonProperty("N") int n,
        @JsonProperty("M") int m,
        @JsonProperty("M1") int m1
) implements FigureInfo<PointRecord> {

    public FigureInfoRecord(List<PointRecord> points, int n, int m, int m1) {
        if (points.size() < 4)
            throw new IllegalArgumentException("There must be at least 4 points");
        if (n < 1)
            throw new IllegalArgumentException("The number of segments (N) must be at least 1");
        if (m < 1)
            throw new IllegalArgumentException("The number of formatives (M) must be at least 1");
        if (m1 < 1)
            throw new IllegalArgumentException("The number of circle lines (M1) must be at least 1");
        this.points = List.copyOf(points);
        this.n = n;
        this.m = m;
        this.m1 = m1;
    }

    public FigureInfoRecord(FigureInfo<? extends Point> info) {
        this(info.points().stream().map(PointRecord::new).toList(), info.n(), info.m(), info.m1());
    }

}
