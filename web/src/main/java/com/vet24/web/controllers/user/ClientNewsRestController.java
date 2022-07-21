package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.ClientNewsResponseDto;
import com.vet24.service.news.NewsService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/client/news")
@Tag(name = "client news rest controller", description = "операции контроллера clientNewsRestController")
public class ClientNewsRestController {
    private final NewsService newsService;

    @Autowired
    public ClientNewsRestController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "получить все опубликованные предстоящие новости из базы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "новости успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientNewsResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<List<ClientNewsResponseDto>> getAllNews() {
        return new ResponseEntity<>(newsService.getClientNewsResponseDto(), HttpStatus.OK);
    }
}
