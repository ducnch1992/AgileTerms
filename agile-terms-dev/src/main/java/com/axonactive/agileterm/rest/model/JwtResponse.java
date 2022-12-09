package com.axonactive.agileterm.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;

    private String type = "Bearer";
    private String username;
    private List<String> roles;

    private Boolean isActive;

    public JwtResponse(String jwt,String username, List<String> roles,Boolean isActive){
        this.roles = roles;
        this.token = jwt;
        this.username = username;
        this.isActive = isActive;
    }
}
