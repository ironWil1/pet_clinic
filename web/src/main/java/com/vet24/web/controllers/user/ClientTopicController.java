package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.TopicDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.user.TopicMapper;
import com.vet24.models.user.Topic;
import com.vet24.models.user.User;
import com.vet24.service.user.ClientService;
import com.vet24.service.user.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/client/topic")
@Tag(name = "Topic controller", description = "operation with topic")
public class ClientTopicController {

    private final ClientService clientService;
    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @Autowired
    public ClientTopicController(ClientService clientService, TopicService topicService,TopicMapper topicMapper) {
        this.clientService = clientService;
        this.topicService = topicService;
        this.topicMapper = topicMapper;
    }

    @Operation(summary = "get all topics from base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful receipt of topics from base",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "database is empty")
    })
    @GetMapping("/allTopics")
    public ResponseEntity<List<TopicDto>> getAllTopics(){
        List<TopicDto> topicDtoList = topicMapper.toDto(topicService.getAll());
        if (topicDtoList.isEmpty()) {
            throw new NullPointerException("database is empty");
        }
        return new ResponseEntity<>(topicDtoList, HttpStatus.OK);
    }

    @Operation(summary = "get all topics from current client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful receipt of topics from current client",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "topics are not found")
    })
    @GetMapping("/yourTopics")
    public ResponseEntity<List<TopicDto>> getAllClientTopic () {
        return new ResponseEntity<>(
                topicMapper.toDto(topicService.getTopicByClientId(clientService.getCurrentClient().getId())), HttpStatus.OK
        );
    }

    @Operation(summary = "get one topic from current client by topic id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful getting topic",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable("topicId") Long topicId) {
        if (!topicService.isExistByKey(topicId)) {
            throw new NotFoundException("topic not found");
        }
        return new ResponseEntity<>(topicMapper.toDto(topicService.getByKey(topicId)), HttpStatus.OK);
    }

    @Operation(summary = "create topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is create",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "title or content can't null")
    })

    @PostMapping()
    public ResponseEntity<TopicDto> createTopic(@RequestParam String title,
                                                @RequestParam String content){
        if (title.trim().equals("") || content.trim().equals("")) {
            throw new BadRequestException("title or content can't null");
        }
        User user = clientService.getCurrentClient();
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setTopicStarter(user);
        topicService.persist(topic);
        return new ResponseEntity<>(topicMapper.toDto(topicService.getByTitleAndClientId(title, clientService.getCurrentClient().getId())), HttpStatus.CREATED);
    }

    @Operation(summary = "update info from topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "403", description = "it's not you topic"),
            @ApiResponse(responseCode = "404", description = "topic not found")
    })
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicDto> updateTopic(@RequestParam(required = false) String title,
                                                @RequestParam(required = false) String content,
                                                @PathVariable("topicId") Long topicId){
        if (!topicService.isExistByKey(topicId)) {
            throw new NotFoundException("topic not found");
        }
        User user = clientService.getCurrentClient();
        Topic topic = topicService.getByKey(topicId);

        if (!topic.getTopicStarter().equals(user)) {
            throw new NotFoundException("it's not your topic");
        }

        if (!(title == null || title.equals(""))) {
            topic.setTitle(title);
        }
        if (!(content == null || content.equals(""))) {
            topic.setContent(content);
        }
        topicService.update(topic);
        return new ResponseEntity<>(topicMapper.toDto(topic), HttpStatus.OK);
    }

    @Operation(summary = "delete topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "topic is delete"),
            @ApiResponse(responseCode = "404", description = "it's not you topic"),
            @ApiResponse(responseCode = "404", description = "topic is not found")
    })
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("topicId") Long topicId){
        if (!topicService.isExistByKey(topicId)) {
            throw new NotFoundException("topic is not found");
        }
        User user = clientService.getCurrentClient();
        Topic topic = topicService.getByKey(topicId);
        if (!topic.getTopicStarter().equals(user)) {
            throw new NotFoundException("it's not you topic");
        }
        topicService.delete(topic);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
