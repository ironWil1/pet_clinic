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
@Tag(name = "client news rest controller", description = "clientNewsRestController operations")
public class ClientNewsRestController {
    private final NewsService newsService;

    @Autowired
    public ClientNewsRestController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "get all news from base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful receipt of all news from base",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientNewsResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "news was not found")
    })
    @GetMapping("")
    public ResponseEntity<List<ClientNewsResponseDto>> getAllNews() {
        List<ClientNewsResponseDto> clientNewsResponseDtoList = newsService.getClientNewsResponseDto();
        return new ResponseEntity<>(clientNewsResponseDtoList, HttpStatus.OK);
    }
}
