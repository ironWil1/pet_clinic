package com.vet24.models.mappers.news;

import com.vet24.discord.models.dto.discord.embed.EmbedDto;
import com.vet24.discord.models.dto.discord.embed.EmbedImageDto;
import com.vet24.models.mappers.EntityMapper;
import com.vet24.models.news.News;
import org.mapstruct.Mapper;
import com.vet24.discord.models.dto.discord.MessageDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class NewsMessageDTOMapper implements EntityMapper<MessageDto, News> {

    private final MessageDto messageDto = new MessageDto();
    public MessageDto toDto(News news) {
        switch (news.getType()) {
            case DISCOUNTS:
                messageDto.setContent("ВНИМАНИЕ, СКИДКИ!!!");
                break;
            case UPDATING:
                messageDto.setContent("ОБЪЯВЛЕНИЕ");
                break;
            case ADVERTISING_ACTIONS:
                messageDto.setContent("Реклама");
                break;
            case PROMOTION:
                messageDto.setContent("!!!ПРОМОАКЦИЯ!!!");
                break;
        }

        messageDto.setId(news.getId());
        messageDto.setType(1);
        messageDto.setPinned(false);
        messageDto.setMention_everyone(false);
        messageDto.setTts(false);
        messageDto.setWebhook_id(993487572003213342L);

        EmbedDto[] embedDtos = this.setUrlOfPicture(news.getPictures());

        EmbedDto mainEmbedDto = embedDtos[0];
        mainEmbedDto.setDescription(news.getContent());
        mainEmbedDto.setTitle(news.getTitle());
        messageDto.setEmbeds(embedDtos);
        return messageDto;
    }

    public EmbedDto[] setUrlOfPicture (List<String> pictureList) {
        if (pictureList.size() > 0) {
            EmbedDto[] embedDtos = new EmbedDto[pictureList.size()];
            for (int n = 0; n < embedDtos.length; n++) {
                embedDtos[n] = new EmbedDto();
                embedDtos[n].setImage(new EmbedImageDto());
                embedDtos[n].getImage().setUrl(pictureList.get(n));
            }
            return embedDtos;
        } else {
            return new EmbedDto[]{new EmbedDto()};
        }
    }

    @Override
    @Mapping(target = "type", ignore = true)
    public void updateEntity(MessageDto dto, @MappingTarget News entity) {}
}
