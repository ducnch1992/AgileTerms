package com.axonactive.agileterm.rest.api;

import com.axonactive.agileterm.rest.client.model.JwtRequest;
import com.axonactive.agileterm.rest.model.JwtResponse;
import com.axonactive.agileterm.rest.model.UserDto;
import com.axonactive.agileterm.service.UserService;
import com.axonactive.agileterm.service.security.impl.UserDetailsImpl;
import com.axonactive.agileterm.utility.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(maxAge = 3600,origins = "*")
@RequestMapping(AuthResource.PATH )
@Tag(name = "Authentication" , description = "APIs to manipulate Sign-up and Log-in features in Agile-term")
public class AuthResource {
    public static final String PATH = "/auth" ;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;


    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in ", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser (@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Information to login", required = true)@Valid @RequestBody JwtRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                roles,
                userDetails.isActivated()));
    }

    @Operation(summary = "Verify User by verification code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a verification code ", content = @Content (schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Request sent to the server is invalid or corrupted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Sign-in required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
            @ApiResponse(responseCode = "404", description = "Verification code not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Request cannot be fulfilled through browsers due to server-side problems.", content = @Content)
    })
    @GetMapping("/verify/{verifyCode}")
    public ResponseEntity<UserDto> verifyUser (@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Verification code to verify user", required = true)@PathVariable(name = "verifyCode")String verifyCode){
        return ResponseEntity.ok(userService.verifyUser(verifyCode));
    }

    @GetMapping("/verify-token-session/{token}")
    public ResponseEntity<Void> verifySessionToken(@Parameter(description = "JWT Token",name = "token",required = true) @PathVariable("token") String token){
        jwtUtils.isTokenExpired(token);
        return ResponseEntity.ok().build();
    }
}
