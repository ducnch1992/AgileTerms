package com.axonactive.agileterm.rest.api;


import com.axonactive.agileterm.rest.client.model.Term;
import com.axonactive.agileterm.rest.client.model.TermName;
import com.axonactive.agileterm.rest.model.ResponseForUploadFile;
import com.axonactive.agileterm.rest.model.TermDetailDto;
import com.axonactive.agileterm.rest.model.TermDto;
import com.axonactive.agileterm.rest.model.TopicDto;
import com.axonactive.agileterm.service.TermService;
import com.axonactive.agileterm.service.TermTopicService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping(TermResource.PATH)
@Slf4j
@Tag(name = "Term", description = "APIs to manipulate Term in Agile-term")
public class TermResource {
    public static final String PATH = "/terms";

    @Autowired
    private TermService termService;
    @Autowired
    private TermTopicService termTopicService;

    @Operation(summary = "Get all terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of all terms", content = @Content(schema = @Schema(implementation = TermDto.class))),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TermDto>> getAll() {
        return ResponseEntity.ok(termService.getAllQuery());
    }

    @Operation(summary = "Find term by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a term", content = @Content(schema = @Schema(implementation = TermDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Term not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TermDto> findTermById(@Parameter(description = "ID of term to return", required = true) @PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(termService.findTermByTermId(id));
    }

    @Operation(summary = "Find topic by term id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a topic", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Term id not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/{id}/topic")
    public ResponseEntity<List<TopicDto>> findTopicByTermId(@Parameter(description = "ID of term to return its topics", required = true) @PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(termTopicService.findTopicByTermId(id));
    }

    @Operation(summary = "Add a new term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new term ", content = @Content(schema = @Schema(implementation = TermDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TermDto> add(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Agile term to be created", required = true) @RequestBody Term term) {
        TermDto createdTerm = termService.save(term);
        return ResponseEntity.created(URI.create(TermResource.PATH + "/" + createdTerm.getEncodedId())).body(createdTerm);
    }

    @Operation(summary = "Update a term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a new term", content = @Content(schema = @Schema(implementation = TermDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TermDto> update(@Parameter(description = "ID of term to be updated", required = true) @PathVariable(value = "id") Integer id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "An updated term in Agile Term") @RequestBody TermName termNameRequest) {
        return ResponseEntity.ok(termService.update(id, termNameRequest));
    }

    @Operation(summary = "Find term, its related descriptions and topics by term encoded id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return an encoded Term's id", content = @Content(schema = @Schema(implementation = TermDetailDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Encoded term's id not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/{encodedTermId}/details")
    public ResponseEntity<TermDto> getTermDetailById(@Parameter(description = "Encoded ID of a term", required = true) @PathVariable(value = "encodedTermId") String encodedId) {
        return ResponseEntity.ok(termTopicService.findTermDetailById(encodedId));
    }

    @Operation(summary = "Upload an excel file of terms and descriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a multipart file", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "413", description = "File size exceed limit", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload-file", consumes = "multipart/form-data")
    public ResponseEntity<ResponseForUploadFile> uploadTermWithExcelFile(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data"), description = "File to be uploaded with list of terms and theirs descriptions") @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(termService.uploadTermAndDescriptionExcelFile(file));
    }

    @Operation(summary = "Validate term name not exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "413", description = "File size exceed limit", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("existed-validate")
    public ResponseEntity<Void> termExistValidate(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Term name need to be checked if exist in database from user's input", required = true) @RequestBody TermName userInput) {
        termService.validateNewTermName(userInput.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Looking for term name contain a given word")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "413", description = "File size exceed limit", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("contain")
    public ResponseEntity<List<String>> get5TermNameLike(@RequestParam("q") String term) {
        return ResponseEntity.ok(termService.get5TermNameLike(term));
    }

    @Operation(summary = "Get 10 recent terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of 10 recent terms", content = @Content(schema = @Schema(implementation = TermDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Encoded term's id not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("recent")
    public ResponseEntity<List<TermDto>> get10RecentTerms() {
        return ResponseEntity.ok(termService.find10RecentTerms());
    }

    @Operation(summary = "Get 10 popular terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of 10 popular term", content = @Content(schema = @Schema(implementation = TermDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Encoded term's id not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("popular")
    public ResponseEntity<List<TermDto>> get10PopularTerms() {
        return ResponseEntity.ok(termService.getTop10PopularTerms());
    }


}
