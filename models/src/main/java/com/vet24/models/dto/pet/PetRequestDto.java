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

    @NotBlank(groups = {Post.class}, message = "name не должно быть пустым")
    private String name;

    private String avatar;

    @NotNull(groups = {Post.class}, message = "birthDay не должно быть пустым")
    @PastOrPresent(groups = {Post.class, Put.class}, message = "birthDay должно быть меньше или равно текущей дате")
    private LocalDate birthDay;

    @NotNull(groups = {Post.class}, message = "petType не должно быть пустым")
    private PetType petType;

    @NotBlank(groups = {Post.class}, message = "breed не должно быть пустым")
    private String breed;

    @NotNull(groups = {Post.class}, message = "gender не должно быть пустым")
    private Gender gender;

    @NotBlank(groups = {Post.class}, message = "color не должно быть пустым")
    private String color;

    private PetSize size;

    @Positive(groups = {Post.class, Put.class}, message = "weight должно быть больше 0")
    private Double weight;

    private String description;
}
