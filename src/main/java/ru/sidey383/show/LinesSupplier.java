package ru.sidey383.show;

import ru.sidey383.model.math.Matrix;
import ru.sidey383.model.data.Pair;
import ru.sidey383.model.math.Vector;

import java.util.List;

public interface LinesSupplier {

    /**
     * @return list of pairs
     * **/
    List<Pair<Vector>> createLines(Matrix transformation);

    List<Pair<Vector>> createLines();

    /**
     * @return Apply matrix to line supplier
     * **/
    LinesSupplier applyMatrix(Matrix matrix);

}
