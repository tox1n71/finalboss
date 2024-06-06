package ru.itmo.finalboss.services;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.finalboss.entities.PointEntity;
import ru.itmo.finalboss.entities.UserEntity;
import ru.itmo.finalboss.exceptions.IncorrectCoordinatesException;
import ru.itmo.finalboss.mbeans.AreaCalculatorMBean;
import ru.itmo.finalboss.mbeans.PointCounterMBean;
import ru.itmo.finalboss.models.Point;
import ru.itmo.finalboss.repositories.PointRepo;
import ru.itmo.finalboss.repositories.UserRepo;
import ru.itmo.finalboss.security.jwt.JwtService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.Math.*;
import static ru.itmo.finalboss.models.Point.PointEntitytoModel;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepo pointRepo;

    private final UserRepo userRepo;
    private final JwtService jwtService;

    @Autowired
    private PointCounterMBean pointCounterMBean;

    @Autowired
    private AreaCalculatorMBean areaCalculatorMBean;

    public Point addPoint(String jwtToken, PointEntity point) throws IncorrectCoordinatesException {
        Long userId = Long.valueOf(jwtService.extractUserId(jwtToken));
        if (!validateCoordinates(point.getX(), point.getY(), point.getR())){
            throw new IncorrectCoordinatesException("Некорректные координаты!");
        }
        long startExec = System.nanoTime();
        boolean result = checkArea(point.getX(), point.getY(), point.getR());
        point.setExecutedAt(LocalDateTime.now());
        long endExec = System.nanoTime();
        long executionTime = endExec - startExec;
        point.setResult(result);
        point.setExecutionTime(executionTime);
        UserEntity userEntity = userRepo.findById(userId).get();
        point.setUser(userEntity);
        pointRepo.save(point);

        //Если что падает, то из-за этой хуйни
        pointCounterMBean.incrementTotalPoints();
        if (!result) {
            pointCounterMBean.incrementMissedPoints();
            areaCalculatorMBean.addPoint(point);
        } else {
            pointCounterMBean.resetConsecutiveMisses();
            areaCalculatorMBean.resetPoints();
        }

        return PointEntitytoModel(point);

    }


    public List<Point> getPoints() {
        Iterator<PointEntity> iterator = pointRepo.findAll().iterator();
        List<Point> points = new LinkedList<>();
        while (iterator.hasNext()) {
            Point point = PointEntitytoModel(iterator.next());
            points.add(point);
        }
        return points;
    }
    public List<Point> getUserPoints(String jwtToken) {
        Long id = Long.valueOf(jwtService.extractUserId(jwtToken));
        UserEntity user = userRepo.findById(id).orElse(null);
        if (user != null) {
            return user.getPoints().stream()
                    .map(Point::PointEntitytoModel)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public boolean checkArea(double x, double y, double r) {
        return checkCircle(x, y, r) || checkTriangle(x, y, r) || checkSquare(x, y, r);
    }

    public boolean checkSquare(double x, double y, double r) {
        return (x <= 0 && y <= 0 && abs(x) <= r && abs(y) <= r);
    }

    public boolean checkCircle(double x, double y, double r) {
        return (x >= 0 && y <= 0 && (pow(abs(x), 2) + pow(abs(y),2) <= pow(r / 2, 2)));
    }

    public boolean checkTriangle(double x, double y, double r) {
        return (x >= 0 && y >= 0 && x + 2 * y <= r);
    }

    public boolean validateCoordinates(double x, double y, double r){
        double[] rArray = {0.5, 1, 1.5, 2};
        boolean validX = -2 <= x && x <= 2;
        boolean validY = -5 <= y && y <= 5;
        boolean validR = false;
        for (double v : rArray) {
            if (v == r) {
                validR = true;
                break;
            }
        }
        return validX && validY && validR;

    }
}
