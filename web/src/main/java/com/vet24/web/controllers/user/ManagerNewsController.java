package com.vet24.web.controllers.user;

import com.vet24.models.dto.user.ManagerNewsRequestDto;
import com.vet24.models.dto.user.ManagerNewsResponseDto;
import com.vet24.models.mappers.user.ManagerNewsRequestMapper;
import com.vet24.models.mappers.user.ManagerNewsResponseMapper;
import com.vet24.models.news.News;
import com.vet24.service.news.NewsService;
import com.vet24.web.controllers.annotations.CheckExist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "api/manager/news")
@Tag(name = "manager news controller", description = "managerNewsController operations")
public class ManagerNewsController {

    private final NewsService newsService;
    private final ManagerNewsResponseMapper responseMapper;
    private final ManagerNewsRequestMapper requestMapper;

    @Autowired
    public ManagerNewsController(NewsService newsService,
                                 ManagerNewsResponseMapper responseMapper,
                                 ManagerNewsRequestMapper requestMapper) {
        this.newsService = newsService;
        this.responseMapper = responseMapper;
        this.requestMapper = requestMapper;
    }


    @Operation(summary = "Получение всех Новостей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все Новости были получены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerNewsResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<List<ManagerNewsResponseDto>> getAllNews() {
        List<ManagerNewsResponseDto> newsDto = responseMapper.toDto(newsService.getAll());
        return new ResponseEntity<>(newsDto, HttpStatus.OK);
    }

    @Operation(summary = "Получение Новости по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость получена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerNewsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Новость с таким ID не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ManagerNewsResponseDto> getNewsById(@CheckExist (entityClass = News.class) @PathVariable("id") Long newsId) {
        return new ResponseEntity<>(responseMapper.toDto(newsService.getByKey(newsId)), HttpStatus.OK);
    }

    @Operation(summary = "Создание Новости")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Новость была создана",
                    content = @Content(schema = @Schema(implementation = ManagerNewsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "При создании Новости возникла ошибка")
    })
    @PostMapping("")
    public ResponseEntity<ManagerNewsResponseDto> persistNews(@Valid @RequestBody ManagerNewsRequestDto newsDto) {

        News news = requestMapper.toEntity(newsDto);
        newsService.persist(news);
        return ResponseEntity.ok(responseMapper.toDto(news));
    }

    @Operation(summary = "Изменение Новости по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость обновлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerNewsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Новость с таким ID не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ManagerNewsResponseDto> updateNewsById(@CheckExist (entityClass = News.class) @PathVariable("id") Long newsId,
                                                                 @Valid @RequestBody ManagerNewsRequestDto newsDto) {
        News news = newsService.getByKey(newsId);
        requestMapper.updateEntity(newsDto, news);
        newsService.update(news);
        return ResponseEntity.ok(responseMapper.toDto(news));
    }

    @Operation(summary = "Добавление картинок к Новостям")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Картинки добавлены",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}/pictures/")
    public ResponseEntity<Void> addNewsPicture(@RequestBody List<String> pictures, @PathVariable Long id) {
        newsService.addNewsPicturesById(id, pictures);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Публикация Новостей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новости опубликованы",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Новости не найдены")
    })
    @PutMapping("/publish")
    public ResponseEntity<Map<Long, String>> publishNews (@RequestBody List<Long> newsId) {
        return ResponseEntity.ok(newsService.publishNews(newsId));
    }

    @Operation(summary = "Отмена публикации Новостей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Публикация Новостей отменена",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Новости не найдены")
    })
    @PutMapping("/unpublish")
    public ResponseEntity<Map<Long, String>> unpublishNews(@RequestBody List<Long> newsId) {
        return ResponseEntity.ok(newsService.unpublishNews(newsId));
    }

    @Operation(summary = "Удаление Новостей по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость удалена"),
            @ApiResponse(responseCode = "400", description = "Новость не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsById(@CheckExist (entityClass = News.class) @PathVariable("id") Long newsId) {

        News news = newsService.getByKey(newsId);

        newsService.delete(news);
        return ResponseEntity.ok().build();
    }
}
