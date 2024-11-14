package sfera.tsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.tsm.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
