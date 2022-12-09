package com.axonactive.agileterm.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionDto {
    private Integer id;
    private String content;
    private LocalDate createDate;
    private String authorName;
    private Integer votePoint;
}
