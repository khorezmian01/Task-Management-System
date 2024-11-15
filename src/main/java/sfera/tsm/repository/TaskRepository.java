package sfera.tsm.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfera.tsm.dto.res.ResponseTask;
import sfera.tsm.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> getAllTasks(Pageable pageable);

    Page<Task> getAllTasksByAuthorId(Long authorId, Pageable pageable);
}
