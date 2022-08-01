package com.vet24.discord.models.dto.discord;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class UserDto {
    // является ли ботом
    private boolean bot;

    private Long id;

    private String username;

    private String avatar;
    // 4х-значный идентификатор пользователя на сервере
    private String discriminator;

    private String email;

}
