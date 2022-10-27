package com.vet24.web.controllers.user.Mocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vet24.discord.feign.DiscordClient;
import com.vet24.discord.models.dto.discord.MessageDto;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class DiscordClientMock implements DiscordClient {

    @Override
    public ResponseEntity<MessageDto> getMessageToId(Long message_id, Long thread_id) throws JsonProcessingException {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message_id);
        messageDto.setChannel_id(thread_id);
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MessageDto> send(MessageDto message, Long thread_id, boolean wait) throws JsonProcessingException {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MessageDto> updateMessage(Long message_id, Long thread_id, MessageDto message) throws JsonProcessingException {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Override
    public void deleteMessageToId(Long message_id) throws JsonProcessingException {

    }
}
