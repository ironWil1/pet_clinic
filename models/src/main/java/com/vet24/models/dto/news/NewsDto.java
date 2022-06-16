package com.vet24.models.dto.news;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.NewsType;
import com.vet24.models.util.View;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class NewsDto {

    @JsonView({View.Get.class, View.Put.class})
    private Long id;

    @JsonView({View.Put.class, View.Post.class, View.Get.class})
    private NewsType type;

    @JsonView({View.Put.class, View.Post.class, View.Get.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},
              message = "Поле content не должно быть пустым")
    private String content;

    @JsonView({View.Put.class, View.Post.class, View.Get.class})
    private boolean isImportant;

    @JsonView({View.Put.class, View.Post.class, View.Get.class})
    private LocalDateTime endTime;

}
