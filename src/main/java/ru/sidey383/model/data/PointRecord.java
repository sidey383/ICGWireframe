package ru.sidey383.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PointRecord(
        @JsonProperty("X") double x,
        @JsonProperty("Y") double y
) implements Point {

    public PointRecord(Point p) {
        this(p.x(), p.y());
    }

}
