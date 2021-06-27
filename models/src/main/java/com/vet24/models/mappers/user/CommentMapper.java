package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.CommentDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper extends DtoMapper<Comment, CommentDto>, EntityMapper<CommentDto, Comment> {

    @Override
    @Mapping(source = "user.id", target = "userId")
    CommentDto toDto(Comment comment);
}
