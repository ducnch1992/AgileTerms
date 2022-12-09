package com.axonactive.agileterm.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TermTopicDto {
    private Integer termId;
    private String termName;
    private Integer topicId;
    private String topicName;
    private String topicColor;
}
