package ru.itmo.finalboss.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.finalboss.entities.PointEntity;
import ru.itmo.finalboss.entities.UserEntity;
import ru.itmo.finalboss.exceptions.UserAlreadyExist;
import ru.itmo.finalboss.services.PointService;

@RestController
@RequestMapping("points")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Указываем домен, с которого разрешено делать запросы
public class PointController {
    @Autowired
    private PointService pointService;
    @PostMapping("/add")
    public ResponseEntity add(@RequestHeader("Authorization") String jwtToken, @RequestBody PointEntity point){
        try {
            return ResponseEntity.ok().body(pointService.addPoint(jwtToken.substring(7), point));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/allPoints")
    public ResponseEntity getPoints(){
        try {
            return ResponseEntity.ok().body(pointService.getPoints());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Что-то пошло не так");
        }
    }

    @GetMapping("/getMyPoints")
    public ResponseEntity getUsersPoints(@RequestHeader("Authorization") String jwtToken){
        try {
            return ResponseEntity.ok().body(pointService.getUserPoints(jwtToken.substring(7)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("У вас еще нет точек");
        }
    }
}
