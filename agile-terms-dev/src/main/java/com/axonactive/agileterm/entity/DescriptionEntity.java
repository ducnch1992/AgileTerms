package com.axonactive.agileterm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "description")
public class DescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(length = 1000)
    private String content;

    @NotNull
    private LocalDate createDate = LocalDate.now();

    @JoinColumn(name = "term_id")
    @ManyToOne
    private TermEntity term;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserEntity userEntity;

    private Integer votePoint = 0;
}
