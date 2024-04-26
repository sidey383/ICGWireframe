package ru.sidey383.model.figure;

import ru.sidey383.model.data.BSplineInfo;
import ru.sidey383.model.data.Point;
import ru.sidey383.model.data.PointRecord;
import ru.sidey383.model.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class BSplinePointsSupplier {

    private static final BSplineMatrix bSplineMatrix = new BSplineMatrix();

    private final BSplineInfo<? extends Point> info;

    private final BSplineHelper helper;

    public BSplinePointsSupplier(BSplineInfo<? extends Point> info) {
        this.info = info;
        this.helper = new BSplineHelper(info);
    }

    public List<PointRecord> calculatePoints() {
        List<PointRecord> points = new ArrayList<>();
        TVector tVector = new TVector();
        for (int part = 0; part < helper.partCount(); part++) {
            Vector xVector = bSplineMatrix.multiply(helper.getBaseVectorX(part));
            Vector yVector = bSplineMatrix.multiply(helper.getBaseVectorY(part));
            for (int n = 0; n < info.n(); n++) {
                tVector.setT((double) n / info.n());
                points.add(new PointRecord(xVector.dot(tVector), yVector.dot(tVector)));
            }
            if (part == helper.partCount() - 1)  {
                tVector.setT(1);
                points.add(new PointRecord(xVector.dot(tVector), yVector.dot(tVector)));
            }
        }
        return points;
    }

}
