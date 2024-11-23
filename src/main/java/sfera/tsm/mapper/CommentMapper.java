package sfera.tsm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sfera.tsm.dto.CommentDto;
import sfera.tsm.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdBy.email", target = "userEmail")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "text", target = "text")
    CommentDto toDto(Comment comment);

}
