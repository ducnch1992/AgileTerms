package com.axonactive.agileterm.rest.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {
    @NotBlank
    private String name;

    @NotBlank
    private String color;
}
