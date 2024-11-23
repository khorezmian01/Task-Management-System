package sfera.tsm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import sfera.tsm.dto.TaskDto;
import sfera.tsm.entity.Task;
import sfera.tsm.entity.enums.Priority;
import sfera.tsm.entity.enums.Status;
import sfera.tsm.exception.NotFoundException;
import sfera.tsm.mapper.TaskMapper;
import sfera.tsm.repository.CommentRepository;
import sfera.tsm.repository.TaskRepository;
import sfera.tsm.util.DataUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskService taskService;


    @Test
    @DisplayName("Save task functionality")
    public void givenTaskToSave_whenSaveTask_thenRepositoryIsCalled() {
        //given
        Task task1Transient = DataUtils.getTask1Transient();
        BDDMockito.given(taskRepository.save(any(Task.class)))
                .willReturn(DataUtils.getTask1Persisted());
        //when
        Task save = taskRepository.save(task1Transient);
        //then
        assertThat(save).isNotNull();

    }

    @Test
    @DisplayName("update task functionality")
    public void givenTaskToUpdate_whenUpdateTask_thenRepositoryIsCalled() {
        //given
        Task task1Persisted = DataUtils.getTask1Persisted();
        Long taskId = task1Persisted.getId();
        BDDMockito.given(taskRepository.findById(taskId)).willReturn(Optional.of(task1Persisted));
        TaskDto taskDto = TaskDto.builder()
                .title(task1Persisted.getTitle())
                .description(task1Persisted.getDescription())
                .priority(task1Persisted.getPriority().name())
                .build();
        //when
        Long l = taskService.updateTask(taskId, taskDto);
        //then
        assertThat(l).isEqualTo(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("get task by id functionality")
    public void givenTaskId_whenGetById_thenRepositoryIsCalled(){
        //given
        Long taskId = 1L;
        Task task = DataUtils.getTask1Persisted();
        TaskDto taskDto = TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .build();
        BDDMockito.given(taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        BDDMockito.given(taskMapper.toOneTaskDto(task)).willReturn(taskDto);
        //when
        TaskDto taskById = taskService.getTaskById(taskId);
        //then
        assertThat(taskById).isNotNull();
    }

    @Test
    @DisplayName("get task by id functionality")
    public void givenIncorrectId_whenGetById_thenExceptionIsThrown(){
        //given
        BDDMockito.given(taskRepository.findById(anyLong()))
                .willThrow(NotFoundException.class);
        //when
        assertThrows(NotFoundException.class, () -> taskService.getTaskById(1L));
        //then
    }

    @Test
    @DisplayName("get all tasks functionality")
    public void givenTasks_whenGetAll_thenTasksAreReturned(){
        //given
        Task task1 = DataUtils.getTask1Persisted();
        Task task2 = DataUtils.getTask2Persisted();
        Task task3 = DataUtils.getTask3Persisted();
        List<Task> tasks = List.of(task1, task2, task3);
        Page<Task> taskDtos = new PageImpl<>(tasks);
        PageRequest pageRequest = PageRequest.of(0, 3);
        BDDMockito.given(taskRepository.findAll(pageRequest))
                .willReturn(taskDtos);
        //when
        Page<TaskDto> allTasks = taskService.getAllTasks(0, 3);
        //then
        assertThat(allTasks.getSize()).isEqualTo(3);
        assertThat(allTasks.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("delete task by id functionality")
    public void givenCorrectId_whenDeleteById_thenRepositoryIsCalled(){
        //given
        BDDMockito.given(taskRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(DataUtils.getTask1Persisted()));
        //when
        taskService.deleteTask(1L);
        //then
        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("delete task by id functionality")
    public void givenInCorrectId_whenDeleteById_thenRepositoryIsCalled(){
        //given
        BDDMockito.given(taskRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        assertThrows(NotFoundException.class, () -> taskService.deleteTask(1L));
        //then
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("update status and priority functionality")
    public void givenCorrectId_whenUpdateStatusAndPriority_thenRepoIsCalled(){
        //given
        Long taskId = 1L;
        Status newStatus = Status.COMPLETED;
        Priority newPriority = Priority.HIGH;
        Task task1 = DataUtils.getTask1Persisted();
        Task updatedTask = Task.builder()
                .title(task1.getTitle())
                .description(task1.getDescription())
                .priority(newPriority)
                .status(newStatus)
                .build();
        TaskDto taskDto = TaskDto.builder()
                .title(task1.getTitle())
                .description(task1.getDescription())
                .priority(newPriority.name())
                .status(newStatus.name())
                .build();
        BDDMockito.given(taskRepository.findById(taskId))
                .willReturn(Optional.ofNullable(task1));
        BDDMockito.given(taskRepository.save(any(Task.class))).willReturn(updatedTask);
        BDDMockito.given(taskMapper.toOneTaskDto(any(Task.class))).willReturn(taskDto);
        //when
        TaskDto taskDto2 = taskService.changeStatusAndPriority(taskId, newStatus, newPriority);
        //then
        verify(taskRepository, times(1)).save(any(Task.class));
        assertThat(taskDto2.getPriority()).isEqualTo(newPriority.name());
        assertThat(taskDto2.getStatus()).isEqualTo(newStatus.name());
    }
}
