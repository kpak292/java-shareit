package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.entity.Comment;

@Mapper(componentModel = "itemServiceV1")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment getComment(CommentDto commentDto);

    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    CommentDto getCommentDto(Comment comment);
}
