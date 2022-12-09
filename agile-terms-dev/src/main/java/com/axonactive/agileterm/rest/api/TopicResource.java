package com.axonactive.agileterm.rest.api;

import com.axonactive.agileterm.rest.client.model.Topic;
import com.axonactive.agileterm.rest.model.TopicDto;
import com.axonactive.agileterm.service.TopicService;
import com.axonactive.agileterm.service.mapper.TopicMapper;
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

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@Validated
@Slf4j
@Tag(name = "Topic", description = "APIs to manipulate Topic in Agile-term")
@RequestMapping(TopicResource.PATH)
public class TopicResource {
    public static final String PATH = "/topics";
    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicMapper topicMapper;

    @Operation(summary = "Get all topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of all topics ", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "401", description = "Sign-in required ", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TopicDto>> getAll() {
        return ResponseEntity.ok().body(topicService.getAll());
    }

    @Operation(summary = "Find Topic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a topic ", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> findById(@Parameter(description = "ID of topic to return", required = true) @PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok().body(topicService.findById(id));
    }

    @Operation(summary = "Add a new topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new topic", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TopicDto> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "A new topic to be created") @RequestBody Topic input) {
        TopicDto createdTopic = topicService.save(input);
        return ResponseEntity.created(URI.create(TopicResource.PATH+"/"+createdTopic.getId())).body(createdTopic);
    }

    @Operation(summary = "Update a Topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a new topic", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> update(@Parameter(description = "ID of topic to be updated", required = true) @PathVariable("id") Integer id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "An updated topic in Agile Term") @RequestBody Topic input) {
        return ResponseEntity.ok().body(topicService.update(id, input));
    }


    @Operation(summary = "Delete a topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of topic to be deleted ", required = true) @PathVariable(value = "id") Integer id) {
        topicService.findById(id);
        topicService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get 10 most topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of all topics ", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "401", description = "Sign-in required ", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/popular")
    public ResponseEntity<List<TopicDto>> getListOfPopularTopic(){
        return ResponseEntity.ok().body(topicMapper.toDtos(topicService.getPopularTopic()));
    }



}
