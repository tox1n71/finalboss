package ru.itmo.finalboss.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.itmo.finalboss.entities.PointEntity;

public interface PointRepo extends CrudRepository<PointEntity, Long>{
}
