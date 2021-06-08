package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity

public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    private Client client;

    @Column
    private String content;

    @Column
    private LocalDate dateTime;

    @ManyToOne (fetch = FetchType.LAZY)
    private Doctor doctor;



    public Comment(Client client, String content, LocalDate dateTime, Doctor doctor) {
        this.client = client;
        this.content = content;
        this.dateTime = dateTime;
        this.doctor = doctor;
    }
}
