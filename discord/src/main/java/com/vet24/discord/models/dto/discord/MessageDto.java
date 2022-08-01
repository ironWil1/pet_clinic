package com.vet24.discord.models.dto.discord;

import com.vet24.discord.models.dto.discord.embed.EmbedDto;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode
public class MessageDto {

    private Long id;  // id сообщения

    private Integer type;   // тип сообщения ( по умолчанию 0 ) ;

    private String content;  // текст сообщения ( до 2000 символов)

    private Long channel_id;  // id канала

    private UserDto author;  // автор сообщения

    private AttachmentDto[] attachment; // вложенные файлы

    private EmbedDto[] embeds; // Embed блок в сообщении

    private UserDto[] mentions; // упоминания юзеров в сообщении

    private Long[] mention_roles;   // упоминания ролей в сообщении

    private boolean pinned; // является ли сообщение закрепленным

    private boolean mention_everyone; // упомянуты ли в сообщении все

    private boolean tts; // озвучить ли сообщение после отправки

    private String timestamp; // время публикации сообщения

    private String edited_timestamp; // время редактирования сообщения ( если было изменено, если нет то будет Null)

    private Long webhook_id; // id webhook через который было отправлено сообщение


}
