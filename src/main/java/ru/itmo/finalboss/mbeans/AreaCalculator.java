package ru.itmo.finalboss.mbeans;

import ru.itmo.finalboss.entities.PointEntity;
import ru.itmo.finalboss.mbeans.AreaCalculatorMBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AreaCalculator implements AreaCalculatorMBean {
    private List<PointEntity> points = new ArrayList<>();
    private double area = 0;

    @Override
    public synchronized void addPoint(PointEntity point) {
        points.add(point);
        if (points.size() == 4) {
            List<PointEntity> convexHull = findConvexHull(points);
            if (convexHull.size() == 4) {
                area = calculateArea(convexHull);
            } else {
                area = 0;
            }
        }
    }

    @Override
    public double getArea() {
        return area;
    }

    @Override
    public void resetPoints() {
        points.clear();
        area = 0;
    }

    private List<PointEntity> findConvexHull(List<PointEntity> points) {
        List<PointEntity> hull = new ArrayList<>();
        if (points.size() < 3) return hull;

        PointEntity leftmost = points.stream().min(Comparator.comparingDouble(PointEntity::getX)).orElse(null);
        PointEntity current = leftmost;

        do {
            hull.add(current);
            PointEntity next = points.get(0);
            for (PointEntity point : points) {
                if (next == current || orientation(current, next, point) == 2) {
                    next = point;
                }
            }
            current = next;
        } while (current != leftmost);

        return hull;
    }

    private int orientation(PointEntity p, PointEntity q, PointEntity r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) return 0;  // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    private double calculateArea(List<PointEntity> points) {
        if (points.size() != 4) {
            throw new IllegalArgumentException("The list of points does not form a quadrilateral.");
        }

        double x1 = points.get(0).getX();
        double y1 = points.get(0).getY();
        double x2 = points.get(1).getX();
        double y2 = points.get(1).getY();
        double x3 = points.get(2).getX();
        double y3 = points.get(2).getY();
        double x4 = points.get(3).getX();
        double y4 = points.get(3).getY();

        return 0.5 * Math.abs(x1*y2 + x2*y3 + x3*y4 + x4*y1 - (y1*x2 + y2*x3 + y3*x4 + y4*x1));
    }
}
