package ru.itmo.finalboss.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.finalboss.entities.UserEntity;
import ru.itmo.finalboss.exceptions.IncorrectPasswordException;
import ru.itmo.finalboss.exceptions.UserAlreadyExist;
import ru.itmo.finalboss.exceptions.UserNotFound;
import ru.itmo.finalboss.models.Role;
import ru.itmo.finalboss.models.UserResponse;
import ru.itmo.finalboss.models.User;
import ru.itmo.finalboss.repositories.UserRepo;
import ru.itmo.finalboss.security.jwt.JwtService;

import java.util.*;

import static ru.itmo.finalboss.models.User.UserEntityToModel;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    public UserResponse registration(UserEntity user) throws UserAlreadyExist {

        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExist("Пользователь с таким именем уже существует! ٩(╬ʘ益ʘ╬)۶");
        }
//        UserResponse registrationResponse = new UserResponse();
//        registrationResponse.setMessage("Пользователь успешно сохранен <3");
//        registrationResponse.setUserId(user.getId());
        var registerUser = UserEntity.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.USER)
                .build();
        userRepo.save(registerUser);
        var jwtToken = jwtService.generateToken(registerUser.getId());

        return UserResponse.builder()
                .message("Пользователь успешно сохранен <3")
                .token(jwtToken)
                .build();

    }

    public UserResponse login(UserEntity user) throws UserNotFound, IncorrectPasswordException {
        UserEntity loginUser = userRepo.findByUsername(user.getUsername());
        if (loginUser == null){
            throw new UserNotFound("Пользователь не найден");
        }
//        UserResponse loginResponse = new UserResponse();
//        loginResponse.setUserId(loginUser.getId());
//        loginResponse.setMessage("Успешная авторизация SUIII");
//        return loginResponse;
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new IncorrectPasswordException("Неверный пароль");
        }
        var jwtToken = jwtService.generateToken(loginUser.getId());

        return UserResponse.builder()
                .message("Успешная авторизация SUIII")
                .token(jwtToken)
                .build();
    }
    public Long deleteUser(Long id){
        userRepo.deleteById(id);
        return id;
    }
    public List<User> getUsers(){
        Iterator<UserEntity> iterator = userRepo.findAll().iterator();
        List<User> users = new LinkedList<>();
        while (iterator.hasNext()){
            User user = UserEntityToModel(iterator.next());
            users.add(user);
        }
        return users;
    }



}
