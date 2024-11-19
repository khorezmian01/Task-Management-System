package sfera.tsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sfera.tsm.dto.CommentDto;
import sfera.tsm.dto.TaskDto;
import sfera.tsm.dto.req.ReqExecutors;
import sfera.tsm.dto.res.ResPageable;
import sfera.tsm.dto.res.ResponseTask;
import sfera.tsm.entity.Comment;
import sfera.tsm.entity.Task;
import sfera.tsm.entity.User;
import sfera.tsm.entity.enums.ERole;
import sfera.tsm.entity.enums.Priority;
import sfera.tsm.entity.enums.Status;
import sfera.tsm.exception.NotFoundException;
import sfera.tsm.repository.CommentRepository;
import sfera.tsm.repository.TaskRepository;
import sfera.tsm.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @CachePut(value = "tasks", key = "#taskDto.id")
    public Long createTask(TaskDto taskDto, Priority priority, User user) {
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .priority(priority)
                .status(Status.WAITING)
                .author(user)
                .build();
        Task save = taskRepository.save(task);
        return save.getId();
    }

    @Cacheable(value = "tasks", key = "#id")
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: "+id+" not found"));
        return TaskDto.builder()
                .id(id)
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .status(task.getStatus().name())
                .build();
    }

    public ResPageable getAllTasks(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findAll(pageRequest);
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : tasks.getContent()) {
            TaskDto taskDto = TaskDto.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .priority(task.getPriority().name())
                    .status(task.getStatus().name())
                    .comments(commentRepository.findAllByTaskId(task.getId()).stream()
                            .map(comment -> CommentDto.builder()
                                    .id(comment.getId())
                                    .text(comment.getText())
                                    .build())
                            .toList())
                    .build();
            taskDtos.add(taskDto);
        }
        return ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(tasks.getTotalElements())
                .totalPage(tasks.getTotalPages())
                .data(taskDtos)
                .build();
    }

    public Long updateTask(Long id, TaskDto taskDto) {
        Task task1 = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + "not found"));
        task1.setTitle(taskDto.getTitle());
        task1.setDescription(taskDto.getDescription());
        taskRepository.save(task1);
        return task1.getId();
    }

    @CacheEvict(value = "tasks", key = "#id")
    public Long deleteTask(Long id) {
        Task task1 = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + "not found"));
        taskRepository.deleteTaskFromUser(id);
        taskRepository.deleteById(task1.getId());
        return task1.getId();
    }

    public String changeStatusAndPriority(Long id, Status status, Priority priority) {
        Task task1 = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + "not found"));
        task1.setStatus(status);
        task1.setPriority(priority);
        taskRepository.save(task1);
        return "Task successfully changed";
    }

    public String changeStatus(Long taskId, Status status, User user){
        List<Long> taskIdByUserId = taskRepository.getTaskIdByUserId(user.getId());
        Task task1 = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id: " + taskId + "not found"));
        boolean isCorrectId=false;
        for (Long id: taskIdByUserId){
            if(task1.getId().equals(id)){
                isCorrectId=true;
                break;
            }
        }
        if(isCorrectId) {
            task1.setStatus(status);
            return "Task status successfully changed";
        }
        else
            return "This task is not your";
    }

    public String assignTaskExecutors(List<ReqExecutors> userIds, Long taskId){
        Task task1 = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id: " + taskId + "not found"));
        for (ReqExecutors userId : userIds) {
            Optional<User> byId = userRepository.findById(userId.getUserId());
            if(byId.isPresent()) {
                User user = byId.get();
                if (!user.getTasks().contains(task1)) {
                    user.getTasks().add(task1);
                    userRepository.save(user);
                }
            }
            else {
                throw new NotFoundException("User with id: " + userId.getUserId() + " not found");
            }
        }
        return "Task successfully assigned";
    }

    public String addCommentToTask(CommentDto commentDto, User user) {
        Task task1 = taskRepository.findById(commentDto.getTaskId())
                .orElseThrow(() -> new NotFoundException("Task with id: " + commentDto.getTaskId() + "not found"));
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .createdBy(user)
                .task(task1)
                .build();
        commentRepository.save(comment);
        return "Comment successfully added";
    }

    public ResPageable getAllTaskByAuthorIdOrExecutorId(Long authorId, Long executorId, int page, int size) {
        if(authorId != null && executorId == null) {
            User author = userRepository.findByIdAndRole(authorId, ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new NotFoundException("User with id: " + authorId + "not found"));
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Task> allTasksByAuthorId = taskRepository.getAllTasksByAuthorId(author.getId(), pageRequest);
            return getResPageable(page, size, allTasksByAuthorId);
        }
        else if(authorId == null && executorId != null) {
            User executor = userRepository.findByIdAndRole(executorId, ERole.ROLE_USER)
                    .orElseThrow(() -> new NotFoundException("User with id: " + executorId + "not found"));
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Task> allTasksByExecutorId = taskRepository.getAllTasksByExecutorId(executor.getId(), pageRequest);
            return getResPageable(page, size, allTasksByExecutorId);
        }else
            throw new RuntimeException("вы не можете указать одновременно authorId и executorId");
    }

    private ResPageable getResPageable(int page, int size, Page<Task> allTasksByAuthorId) {
        List<ResponseTask> responseTasks = new ArrayList<>();
        for (Task task : allTasksByAuthorId.getContent()) {
            ResponseTask responseTask = ResponseTask.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus().name())
                    .priority(task.getPriority().name())
                    .comments(commentRepository.findAllByTaskId(task.getId()).stream()
                            .map(comment -> CommentDto.builder()
                                    .id(comment.getId())
                                    .text(comment.getText())
                                    .userEmail(comment.getCreatedBy().getEmail())
                                    .createdAt(comment.getCreatedAt())
                                    .build())
                            .toList())
                    .build();
            responseTasks.add(responseTask);
        }
        return ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(allTasksByAuthorId.getTotalElements())
                .totalPage(allTasksByAuthorId.getTotalPages())
                .data(responseTasks)
                .build();
    }


}
