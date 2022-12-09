package com.axonactive.agileterm.rest.api;

import com.axonactive.agileterm.rest.model.DescriptionDto;
import com.axonactive.agileterm.service.DescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@Validated
@Slf4j
@Tag(name = "Description", description = "APIs to manipulate Description in Agile-term")
@RequestMapping(DescriptionResource.PATH)
public class DescriptionResource {
    public static final String PATH = "/descriptions";
    @Autowired
    private DescriptionService descriptionService;

    @Operation(summary = "Get all descriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of descriptions ", content = @Content(schema = @Schema(implementation = DescriptionDto.class))),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<DescriptionDto>> getAll() {
        return ResponseEntity.ok(descriptionService.getAll());
    }


    @Operation(summary = "Find description by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a description", content = @Content(schema = @Schema(implementation = DescriptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Description not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DescriptionDto> findDescriptionById(@Parameter(description = "ID of description to return", required = true) @PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(descriptionService.findById(id));
    }

    @Operation(summary = "Delete a description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted a new description", content = @Content),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Description not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDescriptionById(@Parameter(description = "ID of description to be deleted", required = true) @PathVariable(value = "id") Integer id) {
        descriptionService.findById(id);
        descriptionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
