package ru.sidey383.model.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public interface FigureInfo<P extends Point> extends BSplineInfo<P> {

    @JsonGetter
    @JsonProperty("N")
    int n();

    @JsonGetter
    @JsonProperty("M")
    int m();

    @JsonGetter
    @JsonProperty("M1")
    int m1();

    @JsonGetter
    @JsonProperty("Points")
    List<P> points();

}
