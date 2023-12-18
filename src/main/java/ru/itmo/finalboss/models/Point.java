package ru.itmo.finalboss.models;

import ru.itmo.finalboss.entities.PointEntity;

import java.time.LocalDateTime;

public class Point {
    private Long id;
    private Double x;
    private Double y;
    private Double r;
    private Boolean result;

    private LocalDateTime executedAt;
    private Long executionTime;
    private String owner;
    public static Point PointEntitytoModel(PointEntity pointEntity){
        Point point = new Point();
        point.setId(pointEntity.getId());
        point.setX(pointEntity.getX());
        point.setY(pointEntity.getY());
        point.setR(pointEntity.getR());
        point.setResult(pointEntity.getResult());
        point.setExecutedAt(pointEntity.getExecutedAt());
        point.setExecutionTime(pointEntity.getExecutionTime());
        point.setOwner(pointEntity.getUser().getUsername());
        return point;
    }
    public Point() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }
}
