package sfera.tsm.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sfera.tsm.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {


    Page<Task> getAllTasksByAuthorId(Long authorId, Pageable pageable);

    @Query(value = "select t.* from task t join users_tasks ut on t.id=ut.tasks_id where ut.user_id=:executorId",
            nativeQuery = true)
    Page<Task> getAllTasksByExecutorId(@Param("executorId") Long executorId, Pageable pageable);

    @Query(value = "select id from task as t join users_tasks ut on t.id = ut.tasks_id where ut.user_id=?1",
            nativeQuery = true)
    List<Long> getTaskIdByUserId(Long userId);

    @Transactional
    @Modifying
    @Query(value = "delete from users_tasks where tasks_id=?1", nativeQuery = true)
    void deleteTaskFromUser(Long taskId);
}
