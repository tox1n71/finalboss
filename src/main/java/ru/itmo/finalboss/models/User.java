package ru.itmo.finalboss.models;

import ru.itmo.finalboss.entities.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class User {
    private long id;
    private String username;
    private List<Point> points;




    public static User UserEntityToModel(UserEntity entity){
        User model = new User();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setPoints(entity.getPoints().stream().map(Point::PointEntitytoModel).collect(Collectors.toList()));
        return model;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
