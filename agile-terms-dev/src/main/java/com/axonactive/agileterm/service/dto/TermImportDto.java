package com.axonactive.agileterm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TermImportDto {
    private int rowIndex;

    private String termString;

    private String descriptionString;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermImportDto that = (TermImportDto) o;
        return termString.equalsIgnoreCase(that.termString) &&
                descriptionString.equalsIgnoreCase(that.descriptionString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(termString.toLowerCase(), descriptionString.toLowerCase());
    }
}

