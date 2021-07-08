package com.vet24.models.mappers.user;

import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.mappers.DtoMapper;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.user.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommentMapper.class ,UserInfoMapper.class})
public interface TopicMapper extends DtoMapper <Topic, TopicDto>, EntityMapper <TopicDto, Topic> {

    @Override
    @Mapping(source = "comments", target = "commentDtoList")
    TopicDto toDto(Topic topic);

    @Override
    @Mapping(source = "commentDtoList", target = "comments")
    Topic toEntity (TopicDto topicDto);
}
