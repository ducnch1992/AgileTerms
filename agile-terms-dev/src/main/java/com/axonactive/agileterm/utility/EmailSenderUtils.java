package com.axonactive.agileterm.utility;

import com.axonactive.agileterm.entity.UserEntity;
import com.axonactive.agileterm.service.dto.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Slf4j
@Component
public class EmailSenderUtils {

    @Value("${generate.token.url}")
    private String generateTokenUrl;

    @Value("${email.sender.url}")
    private String emailSenderUrl;

    @Value("${verification.URL}")
    private String verifyURL;

    private String emailSubject = "[AGILE TERMS] Please Verify Your Account Registration";
    public Void emailSender(UserEntity userEntity){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<EmailDto> request = new HttpEntity<>(activateEmail(userEntity),headers);
        return restTemplate.postForObject(emailSenderUrl, request, Void.class);
    }

    private EmailDto activateEmail(UserEntity userEntity){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> request = restTemplate.exchange(
                generateTokenUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        String tokenJSon = request.getBody();
        String token = null;
        if ( null != tokenJSon ){
        token = tokenJSon.replace("[","").replace("]","");
        token = token.substring(1,token.length()-1);
        }

        EmailDto activateEmail = new EmailDto();
        String activeUserURL = verifyURL + userEntity.getVerificationTokenEntity().getVerificationCode();
        String subject = emailSubject;
        String template = emailTemplate();
        template = template.replace("[[name]]",userEntity.getUsername());
        template = template.replace("[[URL]]",activeUserURL);


        activateEmail.setTo(Base64.getEncoder().encodeToString(userEntity.getEmail().getBytes(StandardCharsets.UTF_8)));
        activateEmail.setSubject(Base64.getEncoder().encodeToString(subject.getBytes(StandardCharsets.UTF_8)));
        activateEmail.setContent(Base64.getEncoder().encodeToString(template.getBytes(StandardCharsets.UTF_8)));
        activateEmail.setTokenKey(token);
        return activateEmail;
    }

    private String emailTemplate() {
        return """
                <head>
                    <meta charset="utf-8">
                    <meta http-equiv="x-ua-compatible" content="ie=edge">
                    <title>Email Confirmation</title>
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style type="text/css">
                    @media screen {
                      @font-face {
                        font-family: 'Source Sans Pro';
                        font-style: normal;
                        font-weight: 400;
                        src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');
                      }
                      @font-face {
                        font-family: 'Source Sans Pro';
                        font-style: normal;
                        font-weight: 700;
                        src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');
                      }
                    }
                    body,
                    table,
                    td,
                    a {
                      -ms-text-size-adjust: 100%;
                      -webkit-text-size-adjust: 100%;
                    }
                    table,
                    td {
                      mso-table-rspace: 0pt;
                      mso-table-lspace: 0pt;
                    }
                    img {
                      -ms-interpolation-mode: bicubic;
                    }
                    a[x-apple-data-detectors] {
                      font-family: inherit !important;
                      font-size: inherit !important;
                      font-weight: inherit !important;
                      line-height: inherit !important;
                      color: inherit !important;
                      text-decoration: none !important;
                    }
                    div[style*="margin: 16px 0;"] {
                      margin: 0 !important;
                    }
                    body {
                      width: 100% !important;
                      height: 100% !important;
                      padding: 0 !important;
                      margin: 0 !important;
                    }
                    table {
                      border-collapse: collapse !important;
                    }
                    a {
                      color: #1a82e2;
                    }
                    img {
                      height: auto;
                      line-height: 100%;
                      text-decoration: none;
                      border: 0;
                      outline: none;
                    }
                    </style>
                  </head>
                  <body style="background-color: #e9ecef;">
                    <div class="preheader" style="display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;">
                    </div>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                      <tr>
                        <td style="height: 50px;"></td>
                      </tr>
                      <tr>
                        <td align="center" bgcolor="#e9ecef">
                          <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                            <tr>
                              <td align="left" bgcolor="#ffffff" style="padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;">
                                <h1 style="margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;">Confirm Your Email Address</h1>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td align="center" bgcolor="#e9ecef">
                          <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                            <tr>
                              <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;">
                                <p><b>Dear [[name]],</b></p>
                                <p style="margin: 0;">Tap the button below to confirm your email address.</p>
                              </td>
                            </tr>
                            <tr>
                              <td align="left" bgcolor="#ffffff">
                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                  <tr>
                                    <td align="center" bgcolor="#ffffff" style="padding: 12px 12px 12px 12px;">
                                      <table border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                          <td align="center" bgcolor="#1a82e2" style="border-radius: 6px;">
                                            <a href="[[URL]]" target="_blank" style="display: inline-block; border: solid 16px #1a82e2; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; color: #ffffff; text-decoration: none; border-radius: 6px;">Verify Your Account</a>
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;">
                                <p style="margin: 0;">If that doesn't work, copy and paste the following link in your browser:</p>
                                <p style="margin: 0;"><a href="[[URL]]" target="_blank">[[URL]]</a></p>
                                <p style="margin: 0; font-size: 16px; color: #666;"> If you didn't create an Agile Terms account with this email account , you can safely delete this email.</p>
                              </td>
                            </tr>
                            <tr>
                              <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-bottom: 3px solid #d4dadf">
                                <p style="margin: 0;">Best Regard,<br>Agile Terms Development Team.</p>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td align="center" bgcolor="#e9ecef" style="padding: 10px;">
                          <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                            <tr>
                              <td align="center" bgcolor="#e9ecef" style="padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;">
                                <p style="margin: 0;">Agile Terms - Axon Active.</p>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                """;
    }
}
