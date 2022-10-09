package com.vet24.models.mappers.news;

import com.vet24.discord.models.dto.discord.AttachmentDto;
import com.vet24.models.enums.NewsType;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import com.vet24.discord.models.dto.discord.MessageDto;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMessageDTOMapper extends EntityMapper<MessageDto, News> {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", constant = "1")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "attachment", source = "pictures")
    @Mapping(target = "pinned", constant = "false")
    @Mapping(target = "mention_everyone", constant = "false")
    @Mapping(target = "tts", constant = "false")
    @Mapping(target = "webhook_id", constant = "993487572003213342L")
    MessageDto toDto(News news);

    List<AttachmentDto> map(List<String> pictures);

    default AttachmentDto map(String picture) {
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setFilename(picture);

        return attachmentDto;
    }

    default NewsType map(Integer type) {
        return NewsType.DISCOUNTS;
    }
}
