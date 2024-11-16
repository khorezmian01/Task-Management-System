package sfera.tsm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfera.tsm.dto.TaskDto;
import sfera.tsm.dto.res.ResPageable;
import sfera.tsm.entity.enums.Priority;
import sfera.tsm.entity.enums.Status;
import sfera.tsm.service.TaskService;


@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Long> createTask(@RequestBody TaskDto taskDto,
                                       @RequestParam Priority priority){
        Long task = taskService.createTask(taskDto, priority);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable("id") Long id){
        TaskDto taskById = taskService.getTaskById(id);
        return ResponseEntity.ok(taskById);
    }

    @GetMapping("/page")
    public ResponseEntity<ResPageable> getAllTasks(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "5") int size){
        ResPageable allTasks = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(allTasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable("id") Long id, @RequestBody TaskDto taskDto){
        Long taskId = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(taskId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id){
        Long l = taskService.deleteTask(id);
        return ResponseEntity.ok(l);
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<String> changeStatusAndPriority(
            @PathVariable("id") Long id,
            @RequestParam("status") Status status,
            @RequestParam("priority") Priority priority){
        String s = taskService.changeStatusAndPriority(id, status, priority);
        return ResponseEntity.ok(s);
    }
}
