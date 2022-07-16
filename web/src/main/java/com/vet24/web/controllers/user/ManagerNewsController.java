package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.user.ManagerNewsRequestDto;
import com.vet24.models.dto.user.ManagerNewsResponseDto;
import com.vet24.models.mappers.user.ManagerNewsRequestMapper;
import com.vet24.models.mappers.user.ManagerNewsResponseMapper;
import com.vet24.models.news.News;
import com.vet24.models.util.View;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.util.List;


@RestController
@RequestMapping(value = "api/manager/news")
@Tag(name = "manager news controller", description = "managerNewsController operations")
public class ManagerNewsController {

    private final NewsService newsService;
    private final ManagerNewsResponseMapper managerNewsResponseMapper;
    private final ManagerNewsRequestMapper managerNewsRequestMapper;
    private static final String NEWS_NOT_FOUND = "news not found";


    @Autowired
    public ManagerNewsController(NewsService newsService, ManagerNewsRequestMapper managerNewsRequestMapper, ManagerNewsResponseMapper managerNewsResponseMapper) {
        this.newsService = newsService;
        this.managerNewsRequestMapper = managerNewsRequestMapper;
        this.managerNewsResponseMapper = managerNewsResponseMapper;
    }


    @Operation(summary = "get all news from base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful receipt of all news from base",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerNewsResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<List<ManagerNewsResponseDto>> getAllNews() {
        List<ManagerNewsResponseDto> newsResponseDtoList = managerNewsResponseMapper.toDto(newsService.getAll());
        return new ResponseEntity<>(newsResponseDtoList, HttpStatus.OK);
    }


    @Operation(summary = "get news by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful getting news by id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerNewsResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "News by id are not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ManagerNewsResponseDto> getNewsById(@PathVariable("id") Long newsId) {

        if (!newsService.isExistByKey(newsId)) {
            throw new NotFoundException(NEWS_NOT_FOUND);
        }
        return new ResponseEntity<>(managerNewsResponseMapper.toDto(newsService
                .getByKey(newsId)),
                HttpStatus.OK);
    }

    @Operation(summary = "persist the new News")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully persisted the new News",
                    content = @Content(schema = @Schema(implementation = ManagerNewsResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "news are not persisted")
    })
    @PostMapping("")
    public ResponseEntity<ManagerNewsResponseDto> persistNews(@Validated(OnCreate.class)
                                                              @JsonView(View.Post.class)
                                                              @RequestBody ManagerNewsRequestDto newsDto) {

        News news = managerNewsRequestMapper.toEntity(newsDto);
        newsService.persist(news);
        return ResponseEntity.ok(managerNewsResponseMapper.toDto(news));
    }

    @Operation(summary = "update news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "news are update",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ManagerNewsResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "news are not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ManagerNewsResponseDto> updateNewsById(@PathVariable("id") Long newsId,
                                                                 @Validated(OnUpdate.class)
                                                                 @JsonView(View.Put.class)
                                                                 @RequestBody ManagerNewsRequestDto newsDto) {
        News news = newsService.getByKey(newsId);
        if (!newsService.isExistByKey(newsId)) {
            throw new NotFoundException(NEWS_NOT_FOUND);
        }
        managerNewsRequestMapper.updateEntity(newsDto, news);
        newsService.update(news);
        return ResponseEntity.ok(managerNewsResponseMapper.toDto(news));
    }

    @Operation(summary = "delete the news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "news by id has been removed"),
            @ApiResponse(responseCode = "404", description = "news by id are not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteNewsById(@PathVariable("id") Long newsId) {

        News news = newsService.getByKey(newsId);
        if (!newsService.isExistByKey(newsId)) {
            throw new NotFoundException(NEWS_NOT_FOUND);
        }

        newsService.delete(news);
        return ResponseEntity.ok().build();
    }
}
