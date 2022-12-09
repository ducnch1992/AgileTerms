package com.axonactive.agileterm.rest.api;

import com.axonactive.agileterm.rest.client.model.User;
import com.axonactive.agileterm.rest.model.UserDto;
import com.axonactive.agileterm.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@Slf4j
@RequestMapping(UserResource.PATH)
@Tag(name = "User", description = "APIs to manipulate User in Agile-term")
public class UserResource {
    public static final String PATH = "/users";
    @Autowired
    UserService userService;
    @Operation(summary = "Get all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of all user", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }



    @Operation(summary = "Check if username exist in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Server has successfully fulfilled the request and that there is no content to send in the response payload body.", content = @Content ),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/validate-username/{username}")
    public ResponseEntity<Void> usernameExistValidate(@Parameter(description = "username want to check", required = true) @PathVariable(name = "username")String username){
        userService.validateUserName(username);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Check if email exist in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Server has successfully fulfilled the request and that there is no content to send in the response payload body.", content = @Content ),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/validate-email/{email}")
    public ResponseEntity<Void> emailExistValidate(@Parameter(description = "email want to check", required = true) @PathVariable(name = "email")String email){
        userService.validateEmail(email);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Sign up new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully signed up ", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signupUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Information to sign up", required = true) @RequestBody @Valid User userRequest) throws MessagingException, UnsupportedEncodingException {
        UserDto createdUser = userService.save(userRequest);
        return ResponseEntity.created(URI.create(AuthResource.PATH + "/" + createdUser.getId())).body(createdUser);
    }



}
