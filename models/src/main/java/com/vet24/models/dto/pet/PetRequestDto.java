package com.vet24.models.dto.pet;

import com.vet24.models.enums.Gender;
import com.vet24.models.enums.PetSize;
import com.vet24.models.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestDto {

    public interface Post {

    }

    public interface Put {

    }

    @NotBlank(groups = {Post.class})
    private String name;

    private String avatar;

    @NotNull(groups = {Post.class})
    @PastOrPresent(groups = {Post.class, Put.class})
    private LocalDate birthDay;

    @NotNull(groups = {Post.class})
    private PetType petType;

    @NotBlank(groups = {Post.class})
    private String breed;

    @NotNull(groups = {Post.class})
    private Gender gender;

    @NotBlank(groups = {Post.class})
    private String color;

    private PetSize size;

    @Positive(groups = {Post.class, Put.class})
    private Double weight;

    private String description;
}
