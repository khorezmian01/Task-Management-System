package sfera.tsm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sfera.tsm.dto.TaskDto;
import sfera.tsm.entity.Task;

@Mapper(uses = CommentMapper.class, componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "comments", target = "comments")
    TaskDto toDto(Task task);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "priority", target = "priority")
    @Mapping(target = "comments", ignore = true)
    TaskDto toOneTaskDto(Task task);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "priority", target = "priority")
    Task toTask(TaskDto taskDto);
}
