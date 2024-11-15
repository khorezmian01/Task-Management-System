package sfera.tsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfera.tsm.entity.Task;
import sfera.tsm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query(value = "select u from User u where u.id=?1 and u.role=?2")
    Optional<User> findByIdAndRole(Long id, String role);

}
