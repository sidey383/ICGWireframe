package ru.sidey383.editor;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.sidey383.model.data.FigureInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class EditedFigure implements FigureInfo<EditedFigure.Point> {

    private final List<Point> points;

    @Getter
    private Point selected;

    private final Runnable splineUpdate;

    private final Runnable viewUpdate;

    private int n;
    private int m;
    private int m1;

    public EditedFigure(FigureInfo<?> figureInfo, Runnable splineUpdate, Runnable viewUpdate) {
        points = figureInfo.points().stream().map(Point::new).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        this.n = figureInfo.n();
        this.m = figureInfo.m();
        this.m1 = figureInfo.m1();
        this.splineUpdate = splineUpdate;
        this.viewUpdate = viewUpdate;
    }

    public void setSpline(FigureInfo<?> figureInfo) {
        this.selected = null;
        int toRemove = this.points.size();
        this.points.addAll(figureInfo.points().stream().map(Point::new).toList());
        this.points.subList(0, toRemove).clear();
        this.n = figureInfo.n();
        this.m = figureInfo.m();
        this.m1 = figureInfo.m1();
        splineUpdate.run();
    }

    public void removePoint(Point point) {
        if (points.size() <= 4)
            throw new IllegalStateException("There must be at least 4 points");
        points.remove(point);
        if (isSelected(point))
            selected = null;
        splineUpdate.run();
    }

    public void normalize() {
        DoubleSummaryStatistics xStat = points.stream().mapToDouble(ru.sidey383.model.data.Point::x).summaryStatistics();
        DoubleSummaryStatistics yStat = points.stream().mapToDouble(ru.sidey383.model.data.Point::y).summaryStatistics();
        double xCenter = xStat.getAverage();
        double divider = Math.max(
                Math.max(xStat.getMax() - xStat.getAverage(), xStat.getAverage() - xStat.getMin()),
                Math.max(
                        Math.max(0, -yStat.getMin()),
                        Math.max(0, yStat.getMax())
                )
        );
        double finalDivider = divider / 8;
        points.forEach(p -> p.setSilent((p.x - xCenter) / finalDivider, p.y / finalDivider));
        splineUpdate.run();
    }

    @Override
    public List<Point> points() {
        return Collections.unmodifiableList(points);
    }

    public boolean isSelected(Point point) {
        return point == selected;
    }

    public void move(double x, double y) {
        points.forEach(p -> p.setSilent(p.x + x, p.y + y));
        splineUpdate.run();
    }

    public void setSelected(Point point) {
        if (!points.contains(point))
            return;
        selected = point;
        viewUpdate.run();
    }

    public int k() {
        return points.size();
    }

    public void k(int k) {
        if (this.points.size() == k)
            return;
        if (k < points.size()) {
            points.subList(k, points.size()).clear();
        } else {
            while (points.size() < k) {
                points.add(new Point(0, 0));
            }
        }
        splineUpdate.run();
    }

    @Override
    public int n() {
        return n;
    }

    public void n(int n) {
        if (this.n == n)
            return;
        this.n = n;
        splineUpdate.run();
    }

    @Override
    public int m() {
        return m;
    }

    public void m(int m) {
        if (n == m)
            return;
        this.m = m;
        splineUpdate.run();
    }

    @Override
    public int m1() {
        return m1;
    }

    public void m1(int m1) {
        if (this.m1 == m1)
            return;
        this.m1 = m1;
        splineUpdate.run();
    }

    @Getter
    @Accessors(fluent = true)
    public class Point implements ru.sidey383.model.data.Point {

        private double x;

        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point(ru.sidey383.model.data.Point p) {
            this.x = p.x();
            this.y = p.y();
        }

        public void set(double x, double y) {
            this.x = x;
            this.y = y;
            splineUpdate.run();
        }

        private void setSilent(double x, double y) {
            this.x = x;
            this.y = y;
        }

    }

}
