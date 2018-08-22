package pl.ncdchot.foosball.database.repository;

import org.springframework.data.repository.CrudRepository;

import pl.ncdchot.foosball.database.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByExternalID(long externalID);
}
