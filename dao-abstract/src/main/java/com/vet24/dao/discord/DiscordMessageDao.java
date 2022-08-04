package com.vet24.dao.discord;


import com.vet24.dao.ReadWriteDao;
import com.vet24.models.discord.DiscordMessage;

public interface DiscordMessageDao extends ReadWriteDao<Long, DiscordMessage> {
    void deleteByDiscordMessageId(Long discordMessageId);
    DiscordMessage getByDiscordMessageId(Long discordMessageId);
}