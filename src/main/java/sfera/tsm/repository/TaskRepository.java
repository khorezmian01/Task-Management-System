package sfera.tsm.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.tsm.dto.res.ResponseTask;
import sfera.tsm.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {


    Page<Task> getAllTasksByAuthorId(Long authorId, Pageable pageable);

    @Query(value = "select t.* from task t join users_tasks ut on t.id=ut.tasks_id where ut.user_id=:executorId", nativeQuery = true)
    Page<Task> getAllTasksByExecutorId(@Param("executorId") Long executorId, Pageable pageable);
}
