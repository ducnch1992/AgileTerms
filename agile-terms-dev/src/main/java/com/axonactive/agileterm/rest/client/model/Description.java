package com.axonactive.agileterm.rest.client.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Description {
    private String content;
    private String userName;
    private Integer votePoint;
}
