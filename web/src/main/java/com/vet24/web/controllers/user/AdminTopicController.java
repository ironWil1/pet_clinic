package com.vet24.web.controllers.user;

import com.vet24.models.user.Topic;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin/topic")
@Tag(name = "admin topic controller")
public class AdminTopicController {

    private final TopicService topicService;

    @Autowired
    public AdminTopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Operation(summary = "Deleting a topic")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "topic removed"))
    @DeleteMapping("{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable("topicId") Long id) {
        Topic topic = topicService.getTopicWithCommentsById(id);
        if (topic != null) topicService.delete(topic);
        return topic != null ? new ResponseEntity<>(HttpStatus.ACCEPTED) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Closing an open topic")
    @PutMapping("{topicId}/close")
    public ResponseEntity<Void> closeTopic(@PathVariable("topicId") Long id) {
        Topic topic = topicService.getTopicWithCommentsById(id);
        if (topic!= null && !topic.isClosed())topic.setClosed(true);
        return topic!=null && !topic.isClosed() ? new ResponseEntity<>(HttpStatus.ACCEPTED):
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
