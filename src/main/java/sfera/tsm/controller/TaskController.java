package sfera.tsm.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sfera.tsm.dto.CommentDto;
import sfera.tsm.dto.TaskDto;
import sfera.tsm.dto.req.ReqExecutors;
import sfera.tsm.dto.res.ResPageable;
import sfera.tsm.entity.User;
import sfera.tsm.entity.enums.Priority;
import sfera.tsm.entity.enums.Status;
import sfera.tsm.service.TaskService;

import java.util.List;


@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Админ может создавать задания")
    @PostMapping("/create")
    public ResponseEntity<Long> createTask(@RequestBody @Valid TaskDto taskDto,
                                           @RequestParam Priority priority,
                                           @AuthenticationPrincipal User user){
        Long task = taskService.createTask(taskDto, priority, user);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "получить задание по id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable("id") Long id){
        TaskDto taskById = taskService.getTaskById(id);
        return ResponseEntity.ok(taskById);
    }

    @Operation(summary = "Админ может получить все задания")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/page")
    public ResponseEntity<ResPageable> getAllTasks(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "5") int size){
        ResPageable allTasks = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(allTasks);
    }

    @Operation(summary = "Админ изменяет задание по id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable("id") Long id, @RequestBody TaskDto taskDto){
        Long taskId = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(taskId);
    }

    @Operation(summary = "Админ удаляет задание по айди также удаляет все комменты которые принадлежат к этому таску")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id){
        Long l = taskService.deleteTask(id);
        return ResponseEntity.ok(l);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "пользователь может менять статус своего задания")
    @PutMapping("/change-status/{taskId}")
    public ResponseEntity<String> changeStatus(@PathVariable("taskId") Long taskId,
                                               @RequestParam Status status,
                                               @AuthenticationPrincipal User user){
        String string = taskService.changeStatus(taskId, status, user);
        return ResponseEntity.ok(string);
    }

    @Operation(summary = "Админ может изменить статус и приоритет")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change-status-or-priority/{id}")
    public ResponseEntity<String> changeStatusAndPriority(
            @PathVariable("id") Long id,
            @RequestParam("status") Status status,
            @RequestParam("priority") Priority priority){
        String s = taskService.changeStatusAndPriority(id, status, priority);
        return ResponseEntity.ok(s);
    }

    @Operation(summary = "Админ прикрепляет задание пользователям")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/assign-task-to-executors/{taskId}")
    public ResponseEntity<String> assignTaskToExecutors(@PathVariable("taskId") Long taskId,
                                                        @RequestBody List<ReqExecutors> reqExecutors){
        String string = taskService.assignTaskExecutors(reqExecutors, taskId);
        return ResponseEntity.ok(string);
    }

    @Operation(summary = "админ и пользователь могут оставлять комменты таску")
    @PostMapping("/add-comment-to-task")
    public ResponseEntity<String> addCommentToTask(@RequestBody CommentDto commentDto,
                                                   @AuthenticationPrincipal User user){
        String string = taskService.addCommentToTask(commentDto, user);
        return ResponseEntity.ok(string);
    }

    @Operation(summary = "Получать задания конкретного автора или исполнотилея")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("task-by-author-or-executor")
    public ResponseEntity<ResPageable> getAllTaskByAuthorIdOrExecutorId(@RequestParam(value = "authorId", required = false) Long authorId,
                                                                        @RequestParam(value = "executorId", required = false) Long executorId,
                                                                        @RequestParam(value = "page") int page,
                                                                        @RequestParam(value = "size") int size){
        ResPageable allTaskByAuthorIdOrExecutorId = taskService.getAllTaskByAuthorIdOrExecutorId(authorId, executorId, page, size);
        return ResponseEntity.ok(allTaskByAuthorIdOrExecutorId);
    }

}
