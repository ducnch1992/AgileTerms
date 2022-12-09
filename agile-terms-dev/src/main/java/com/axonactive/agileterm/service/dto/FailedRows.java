package com.axonactive.agileterm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedRows {
    private List<Integer> termIsNull = new ArrayList<>();

    private List<Integer> invalidTermLength = new ArrayList<>();

    private List<Integer> invalidDescriptionLength = new ArrayList<>();

    private List<Integer> duplicatedDescriptionInFile = new ArrayList<>();

    private List<Integer> existedInTheDatabase = new ArrayList<>();
}
