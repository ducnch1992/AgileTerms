package com.axonactive.agileterm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowResultDto {

    private List<TermImportDto> validRowExistedTermInDataBase = new ArrayList<>();

    private List<TermImportDto> validRowNotExistedTermInDataBase = new ArrayList<>();

    private FailedRows invalidRows = new FailedRows();
}