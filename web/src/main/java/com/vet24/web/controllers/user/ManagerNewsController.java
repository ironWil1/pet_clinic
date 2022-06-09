package com.vet24.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.dto.exception.ExceptionDto;
import com.vet24.models.dto.news.AbstractNewNewsDto;
import com.vet24.models.dto.news.NewsDto;
import com.vet24.models.exception.BadRequestException;
import com.vet24.models.mappers.news.AbstractNewNewsMapper;
import com.vet24.models.mappers.news.NewsMapper;
import com.vet24.models.news.News;
import com.vet24.models.util.View;
import com.vet24.service.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.webjars.NotFoundException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "api/manager/news")
@Tag(name = "manager news controller", description = "managerNewsController operations")
public class ManagerNewsController {

    private final NewsService newsService;
    private final NewsMapper newsMapper;
    private final AbstractNewNewsMapper newNewsMapper;
    private static final String NEWS_NOT_FOUND = "news not found";
    private static final String LIST_NEWS_EMPTY = "list of news are empty";

    @Autowired
    public ManagerNewsController(NewsService newsService, NewsMapper newsMapper, AbstractNewNewsMapper newNewsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
        this.newNewsMapper = newNewsMapper;
    }


    @Operation(summary = "get all news from base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful receipt of all news from base",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<List<NewsDto>> getAllNews() {
        List<NewsDto> newsDtoList = newsMapper.toDto(newsService.getAll());

        if (newsDtoList.isEmpty()) {
            log.info("The list of news are empty {}", newsDtoList);
            throw new NotFoundException(LIST_NEWS_EMPTY);
        }
        log.info("We have this list of news {}", newsDtoList);
        return new ResponseEntity<>(newsDtoList, HttpStatus.OK);
    }


    @Operation(summary = "get news by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful getting news by id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "News by id are not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable("id") Long newsId) {

        if (!newsService.isExistByKey(newsId)) {
            log.info(NEWS_NOT_FOUND + newsId);
            throw new NotFoundException(NEWS_NOT_FOUND);
        }
        return new ResponseEntity<>(newsMapper.toDto(newsService
                                                   .getByKey(newsId)),
                                                    HttpStatus.OK);
    }

    @Operation(summary = "persist the new News")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Successfully persisted the new News",
                    content = @Content(schema = @Schema(implementation = NewsDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "news are not persisted",
                    content = @Content(schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("")
    public ResponseEntity<NewsDto> persistNews(@JsonView(View.Post.class)
                                                   @Validated(OnCreate.class)
                                                   @RequestBody AbstractNewNewsDto newNewsDto) {

        if (newNewsDto
                     .getContent()
                     .trim().equals("")) {
            log.info("Content {} can't null", newNewsDto);
            throw new BadRequestException("Content can't null");
        }
        News news = newNewsMapper.toEntity(newNewsDto);
        newsService.persist(news);
        log.info("We persisted new news {}", newNewsDto.getType());
        return new ResponseEntity<>(newsMapper.toDto(news), HttpStatus.CREATED);
    }


    @Operation(summary = "update news")
    @ApiResponses(value = {
                  @ApiResponse(responseCode = "200",
                               description = "news are update",
                               content = @Content(mediaType = "application/json",
                               schema = @Schema(implementation = NewsDto.class))),
                  @ApiResponse(responseCode = "404",
                               description = "news are not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsDto> updateNewsById(@PathVariable("id") Long newsId,
                                                  @Validated(OnUpdate.class)
                                                  @JsonView(View.Put.class)
                                                  @RequestBody NewsDto newsDto) {
        News news = newsService.getByKey(newsId);
        if (!newsService.isExistByKey(newsId)) {
            log.info(NEWS_NOT_FOUND + newsId);
            throw new NotFoundException(NEWS_NOT_FOUND);
        }
        newsMapper.updateEntity(newsDto, news);
        news.setId(newsId);
        newsService.update(news);
        log.info("We updated news with this id {}",news.getId());
        return ResponseEntity.ok(newsMapper.toDto(news));
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
            log.info(NEWS_NOT_FOUND + newsId);
            throw new NotFoundException(NEWS_NOT_FOUND);
        }

        newsService.delete(news);
        log.info("We deleted news with this id {}",news.getId());
        return ResponseEntity.ok().build();
    }
}