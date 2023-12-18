package ru.itmo.finalboss.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.itmo.finalboss.entities.UserEntity;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
