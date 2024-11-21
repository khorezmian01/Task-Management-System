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
    @Operation(summary = "Админ может создавать задачи",
            description = "Параметры запроса:\n" +
                    "\n" +
                    "title (String, обязательное): Название задачи, не должно быть пустым.\n" +
                    "description (String, необязательное): Описание задачи.\n" +
                    "priority (Enum, обязательное): Приоритет задачи. Значения: LOW, MEDIUM, HIGH.\n" +
                    "Ответы:\n" +
                    "\n" +
                    "200 OK: Возвращает ID задачи, если она была успешно создана.\n" +
                    "400 Bad Request: Если поле title пустое, возвращает сообщение \"Поле не должно быть пустым\"." )
    @PostMapping("/create")
    public ResponseEntity<Long> createTask(@RequestBody @Valid TaskDto taskDto,
                                           @RequestParam Priority priority,
                                           @AuthenticationPrincipal User user){
        Long task = taskService.createTask(taskDto, priority, user);
        return ResponseEntity.ok(task);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "получить задание по id", description ="Параметры запроса:\n" +
            "\n" +
            "id (Long, обязательное): ID задачи.\n" +
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает объект задачи с полями:\n" +
            "title (String): Название задачи.\n" +
            "description (String, может быть null): Описание задачи.\n" +
            "status (String, по умолчанию \"WAITING\"): Статус задачи.\n" +
            "priority (String): Приоритет задачи.\n" +
            "404 Not Found: Если задача с указанным ID не найдена, возвращается сообщение: \"Задача с указанным ID не найдена\".")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable("id") Long id){
        TaskDto taskById = taskService.getTaskById(id);
        return ResponseEntity.ok(taskById);
    }

    @Operation(summary = "Админ может получить всех список задач с пагинацией", description = "Параметры запроса:\n"+
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает список задач с пагинацией:\n" +
            "title (String): Название задачи.\n" +
            "description (String, может быть null): Описание задачи.\n" +
            "status (String, по умолчанию \"WAITING\"): Статус задачи.\n" +
            "priority (String): Приоритет задачи.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/page")
    public ResponseEntity<ResPageable> getAllTasks(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size){
        ResPageable allTasks = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(allTasks);
    }

    @Operation(summary = "Админ может обновить задачу по её ID", description = "Параметры запроса:\n" +
            "\n" +
            "id (Long, обязательное): ID задачи.\n" +
            "taskDto (объект, обязательное): DTO с обновленными данными задачи.\n" +
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает ID обновленной задачи.\n" +
            "404 Not Found: Если задача с указанным ID не найдена, возвращается сообщение: \"Задача с указанным ID не найдена\".")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable("id") Long id, @RequestBody TaskDto taskDto){
        Long taskId = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(taskId);
    }

    @Operation(summary = "Админ  может удалить задачу по её ID, включая удаление всех комментариев, " +
            "связанных с этой задачей, а также снимается с пользователей",
    description = "Параметры запроса:\n" +
            "\n" +
            "id (Long, обязательное): ID задачи.\n" +
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает ID удаленной задачи." +
            "404 Not Found: Если задача с указанным ID не найдена, возвращается сообщение: \"Задача с указанным ID не найдена\".")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id){
        Long l = taskService.deleteTask(id);
        return ResponseEntity.ok(l);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Пользователь может изменить статус своей задачи", description = "Параметры запроса:\n" +
            "\n" +
            "taskId (Long, обязательное): ID задачи.\n" +
            "status (String, обязательное): Новый статус задачи. Возможные значения: WAITING, IN_PROGRESS, COMPLETED\n" +
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает объект задачи с полями:\\n\" +\n" +
            "            \"title (String): Название задачи.\\n\" +\n" +
            "            \"description (String, может быть null): Описание задачи.\\n\" +\n" +
            "            \"status (String, по умолчанию \\\"WAITING\\\"): Статус задачи.\\n\" +\n" +
            "            \"priority (String): Приоритет задачи.\\n" +
            "404 Not Found: Если задача с указанным ID не найдена, возвращается сообщение: \"Задача с указанным ID не найдена\"." +
            "400 Bad Request: Если задача с указанным ID не принадлежит вам, возвращается сообщение: \"Задача с указанным ID не принадлежит вам\"")
    @PutMapping("/change-status/{taskId}")
    public ResponseEntity<TaskDto> changeStatus(@PathVariable("taskId") Long taskId,
                                               @RequestParam Status status,
                                               @AuthenticationPrincipal User user){
        TaskDto taskDto = taskService.changeStatus(taskId, status, user);
        return ResponseEntity.ok(taskDto);
    }

    @Operation(summary = "Админ может изменить статус и приоритет", description = "Параметры запроса:\n" +
            "\n" +
            "id (Long, обязательное): ID задачи.\n" +
            "status (Enum, обязательное): Новый статус задачи.\n" +
            "priority (Enum, обязательное): Новый приоритет задачи. Возможные значения: LOW, MEDIUM, HIGH.\n" +
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает объект задачи с полями:\\n\" +\n" +
            "            \"title (String): Название задачи.\\n\" +\n" +
            "            \"description (String, может быть null): Описание задачи.\\n\" +\n" +
            "            \"status (String, по умолчанию \\\"WAITING\\\"): Статус задачи.\\n\" +\n" +
            "            \"priority (String): Приоритет задачи.\\n" +
            "404 Not Found: Если задача с указанным ID не найдена, возвращается сообщение: \"Задача с указанным ID не найдена\".")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change-status-or-priority/{id}")
    public ResponseEntity<TaskDto> changeStatusAndPriority(
            @PathVariable("id") Long id,
            @RequestParam("status") Status status,
            @RequestParam("priority") Priority priority){
        TaskDto taskDto = taskService.changeStatusAndPriority(id, status, priority);
        return ResponseEntity.ok(taskDto);
    }

    @Operation(summary = "Админ может назначит исполнителей задач", description =
            "Параметры запроса:\n" +
            "\n" +
            "taskId (Long, обязательное): ID задачи.\n" +
            "reqExecutors (список объектов c ID, обязательное): Список исполнителей, которые будут назначены на задачу. " +
                    "Каждый объект включает ID исполнителя.\n" +
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает ID задачи если успешно назначен.\n" +
                    "404 Not Found: Если один из указанных пользователей не найден, возвращает сообщение: \"Пользователь с указанным ID не найден\" ")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/assign-task-to-executors/{taskId}")
    public ResponseEntity<Long> assignTaskToExecutors(@PathVariable("taskId") Long taskId,
                                                        @RequestBody List<ReqExecutors> reqExecutors){
        Long id = taskService.assignTaskExecutors(reqExecutors, taskId);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "админ может оставлять комментарии к задаче", description = "Параметры запроса:\n" +
            "\n" +
            "commentDto (String text, обязательное." +
            "Long taskId, обязательное): DTO комментария, содержащий текст комментария и ID задачи\n"+
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает ID комментарии." +
            "404 Not Found: Если задача с указанным ID не найдена, возвращается сообщение: \"Задача с указанным ID не найдена\".")
    @PostMapping("/add-comment-to-task")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> addCommentToTask(@RequestBody CommentDto commentDto,
                                                   @AuthenticationPrincipal User user){
        Long id = taskService.adminAddCommentToTask(commentDto, user);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "админ может получить список задач по ID автора или исполнителя с пагинацией",
    description = "Параметры запроса:\n" +
            "\n" +
            "authorId (Long, необязательное): ID автора задачи.\n" +
            "executorId (Long, необязательное): ID исполнителя задачи.\n" +
            "вы должны указать либо authorId или executorId и не можете указать одновременно authorId и executorId"+
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает список задач с пагинацией." +
            "")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("task-by-author-or-executor")
    public ResponseEntity<ResPageable> getAllTaskByAuthorIdOrExecutorId(@RequestParam(value = "authorId", required = false) Long authorId,
                                                                        @RequestParam(value = "executorId", required = false) Long executorId,
                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size){
        ResPageable allTaskByAuthorIdOrExecutorId = taskService.getAllTaskByAuthorIdOrExecutorId(authorId, executorId, page, size);
        return ResponseEntity.ok(allTaskByAuthorIdOrExecutorId);
    }

    @Operation(summary = "пользователь может оставлять комментарий своим задачам", description ="Параметры запроса:\n" +
            "\n" +
            "commentDto (String text, обязательное." +
            "Long taskId, обязательное): DTO комментария, содержащий текст комментария и ID задачи\n"+
            "Ответы:\n" +
            "\n" +
            "200 OK: Возвращает ID комментарии."+"\n" +
            "404 Not Found: Если задача с указанным ID не найдена, возвращается сообщение: \"Задача с указанным ID не найдена\"." )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add-comment")
    public ResponseEntity<String> userAddCommentToTask(@RequestBody CommentDto commentDto,
                                                       @AuthenticationPrincipal User user){
        String string = taskService.userAddCommitToTask(commentDto, user);
        return ResponseEntity.ok(string);

    }

    @Operation(summary = "Пользователь получает свои задачи с пагинацией")
    @GetMapping("/my-tasks")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResPageable> getMyTasks(@AuthenticationPrincipal User user,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size){
        ResPageable resPageable = taskService.myTasks(user, page, size);
        return ResponseEntity.ok(resPageable);
    }

}
