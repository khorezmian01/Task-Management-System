package sfera.tsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sfera.tsm.dto.CommentDto;
import sfera.tsm.dto.TaskDto;
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

    public Long createTask(TaskDto taskDto, Priority priority) {
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .priority(priority)
                .status(Status.WAITING)
                .build();
        Task save = taskRepository.save(task);
        return save.getId();
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: "+id+"not found"));
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
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

    public Long deleteTask(Long id) {
        Task task1 = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id: " + id + "not found"));
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

    public String assignTaskExecutors(List<Long> userIds, Long taskId){
        Task task1 = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id: " + taskId + "not found"));
        for (Long userId : userIds) {
            Optional<User> byId = userRepository.findById(userId);
            if(byId.isPresent()) {
                User user = byId.get();
                user.setTasks(List.of(task1));
                userRepository.save(user);
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
            User author = userRepository.findByIdAndRole(authorId, ERole.ROLE_ADMIN.name())
                    .orElseThrow(() -> new NotFoundException("User with id: " + authorId + "not found"));
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Task> allTasksByAuthorId = taskRepository.getAllTasksByAuthorId(author.getId(), pageRequest);
            List<ResponseTask> responseTasks = new ArrayList<>();
            for (Task task : allTasksByAuthorId.getContent()) {
                ResponseTask responseTask = ResponseTask.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .comments(commentRepository.findAllByTaskId(task.getId()).stream()
                                .map(comment -> CommentDto.builder()
                                        .id(comment.getId())
                                        .text(comment.getText())
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
        else if(authorId == null && executorId != null) {
            User executor = userRepository.findByIdAndRole(executorId, ERole.ROLE_USER.name())
                    .orElseThrow(() -> new NotFoundException("User with id: " + executorId + "not found"));


        }
    }


}
