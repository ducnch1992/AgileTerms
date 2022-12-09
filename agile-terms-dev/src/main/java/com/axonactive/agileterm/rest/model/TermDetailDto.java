package com.axonactive.agileterm.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermDetailDto {
    private TermDto termDto;
    private List<TopicDto> topicDtoList;
}
