package com.vet24.models.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DoctorReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Comment.class)
    private Comment comment;
    @OneToOne(targetEntity = Doctor.class)
    private Doctor doctor;

    public DoctorReview(Comment comment, Doctor doctor) {
        this.comment = comment;
        this.doctor = doctor;
    }
}
