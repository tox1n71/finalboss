package ru.itmo.finalboss.mbeans;

import ru.itmo.finalboss.entities.PointEntity;

public interface AreaCalculatorMBean {
    double getArea();
    void addPoint(PointEntity point);
    void resetPoints(); // Метод для сброса точек, если нужно
}
