package com.axonactive.agileterm.rest.client.model;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermTopic {
    private Integer termId;
    private Integer topicId;
}
