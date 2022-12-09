package com.axonactive.agileterm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="term_topic")
public class TermTopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "term_id")
    @NotNull
    private TermEntity term;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    @NotNull
    private TopicEntity topic;
}
