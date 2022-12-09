package com.axonactive.agileterm.rest.model;

import com.axonactive.agileterm.service.dto.FailedRows;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseForUploadFile {

    private FailedRows failedRows;

    private Map<String,Integer> successfulCases = new HashMap<>();

    private int rowStopCounting;
}
