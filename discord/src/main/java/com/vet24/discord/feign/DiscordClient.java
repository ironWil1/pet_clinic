package com.vet24.discord.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vet24.discord.models.dto.discord.MessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "api-service",url = "https://discord.com/api/webhooks/993487572003213342/LV3qfF2IcKhsKIQQrv4TPD6w180ALKTXJh0gmJrlO1pg1JLfM1NRzLb3rl1VaQSOKIRG")
public interface DiscordClient {

    // получить сообщение по id
    @RequestMapping( value = "/messages/{message_id}",
            produces = "application/json",
            method = RequestMethod.GET)
    ResponseEntity<MessageDto> getMessageToId(@PathVariable Long message_id,
                                              @RequestParam(required = false) Long thread_id) throws JsonProcessingException;

    // отправить сообщение
    @RequestMapping(
            consumes = "application/json",
            method = RequestMethod.POST)
    ResponseEntity<MessageDto> send(@RequestBody MessageDto message,
                                    @RequestParam(required = false) Long thread_id,
                                    @RequestParam(defaultValue = "true") boolean wait) throws JsonProcessingException;

    // редактировать сообщение по id
    @RequestMapping( value = "/messages/{message_id}",
            consumes = "application/json",
            produces = "application/json",
            method = RequestMethod.PATCH)
    ResponseEntity<MessageDto> updateMessage(@PathVariable Long message_id,
                                             @RequestParam(required = false) Long thread_id,
                                             @RequestBody MessageDto message) throws JsonProcessingException;
    // удалить сообщение по id
    @RequestMapping( value = "/messages/{message_id}",
            method = RequestMethod.DELETE)
    void deleteMessageToId(@PathVariable Long message_id,
                           @RequestParam(required = false) Long thread_id) throws JsonProcessingException;
}
