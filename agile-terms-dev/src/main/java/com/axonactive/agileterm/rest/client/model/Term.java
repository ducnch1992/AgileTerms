package com.axonactive.agileterm.rest.client.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Term {
    @NotBlank
    @NotNull
    private String name;

    private List<Description> descriptionList;

    private List<Integer> topicIdList;

    private List<Integer> relatedTermIdList;
}
