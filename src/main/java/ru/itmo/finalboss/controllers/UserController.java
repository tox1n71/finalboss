package ru.itmo.finalboss.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.finalboss.entities.UserEntity;
import ru.itmo.finalboss.exceptions.UserAlreadyExist;
import ru.itmo.finalboss.models.UserRequest;
import ru.itmo.finalboss.models.UserResponse;
import ru.itmo.finalboss.services.UserService;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*") // Указываем домен, с которого разрешено делать запросы
public class UserController {
    private final UserService userService;
    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody UserEntity user){
        try {
            UserResponse userResponse = userService.registration(user);
            return ResponseEntity.ok().body(userResponse);
        }
        catch (UserAlreadyExist e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserEntity user){
        try {
            UserResponse loginResponse = userService.login(user);
            return ResponseEntity.ok().body( loginResponse);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/allusers")
    public ResponseEntity getUsers(){
        try {
            return ResponseEntity.ok().body(userService.getUsers());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Понабирают всяких");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("Пользователь c id " + id + " удалён");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }


}
