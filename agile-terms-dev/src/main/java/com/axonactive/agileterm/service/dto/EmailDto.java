package com.axonactive.agileterm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmailDto {

    private String to = "";
    private String cc = "";
    private String bcc = "";
    private String subject = "";
    private String content = "";
    private String signature = "";
    private List<String> attachments = new ArrayList<>();
    private String tokenKey = "";
    private String replyTo = "";
}
