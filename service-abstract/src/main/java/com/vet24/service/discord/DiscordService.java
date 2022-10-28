package com.vet24.service.discord;

import com.vet24.discord.models.dto.discord.MessageDto;
import com.vet24.models.discord.DiscordMessage;

public interface DiscordService {
    DiscordMessage sendMessage(MessageDto dto);
    void deleteMessage(Long discordMessageId);
    void editMessage(Long discordMessageId, MessageDto dto);
}
