package ru.sidey383.model.data;

import java.util.List;

public interface BSplineDots<P extends Point> {

    /**
     * Must contain at least 4 dots
     * **/
    List<P> points();

}
