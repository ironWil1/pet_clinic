package com.vet24.service.discord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vet24.dao.discord.DiscordMessageDao;
import com.vet24.discord.feign.DiscordClient;

import com.vet24.discord.models.dto.discord.MessageDto;
import com.vet24.models.discord.DiscordMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
public class DiscordServiceImpl implements DiscordService {
    private final DiscordClient discordClient;
    private final DiscordMessageDao discordMessageDao;


    @Autowired
    public DiscordServiceImpl(DiscordClient discordClient, DiscordMessageDao discordMessageDao) {
        this.discordClient = discordClient;
        this.discordMessageDao = discordMessageDao;
    }

    @Transactional
    @Override
    public DiscordMessage sendMessage(MessageDto dto) {
        DiscordMessage discordMessage = new DiscordMessage();
        try {
            ResponseEntity<MessageDto> response = discordClient.send(dto, dto.getChannel_id(), true);
            discordMessage.setDiscordMsgId(response.getBody().getId());
            if (response.getBody().getChannel_id() != null) {
                discordMessage.setChannelId(response.getBody().getChannel_id());
            }
            discordMessageDao.persist(discordMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return discordMessage;
    }

    @Transactional
    @Override
    public void deleteMessage(Long discordMessageId) {
        try {
            discordClient.deleteMessageToId(discordMessageId);
            discordMessageDao.deleteByDiscordMessageId(discordMessageId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void editMessage(Long discordMessageId, MessageDto dto) {
        try {
            discordClient.updateMessage(discordMessageId, dto.getChannel_id(), dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}