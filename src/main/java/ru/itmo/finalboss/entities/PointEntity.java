package ru.itmo.finalboss.entities;

//import jakarta.persistence.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double x;
    private Double y;
    private Double r;
    private Boolean result;

    private LocalDateTime executedAt;
    private Long executionTime;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public PointEntity() {
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
