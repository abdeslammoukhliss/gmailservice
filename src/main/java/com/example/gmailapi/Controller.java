package com.example.gmailapi;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import entiites.AccountCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import entiites.EmailRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;


@RestController
public class Controller {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EmailService emailService;

    @Value("${gmail.client.id}")
    private String clientId;
    @Value("${gmail.client.clientSecret}")
    private String clientSecret;
    @Value("${gmail.scopes}")
    private String[] SCOPES;
    @Value("${gmail.redirect.path}")
    private String redirectPath;
    @Value("${gmail.auth.url}")
    private String authorizationUrl;

    @PostMapping("/send")
    public String send(@RequestBody EmailRequest emailRequest) throws GeneralSecurityException, IOException {
        GmailServiceConfig gmailServiceConfig = applicationContext.getBean(GmailServiceConfig.class);
        Gmail gmailService = gmailServiceConfig.gmailService((new AccountCredential(emailRequest.getToken())));
        emailService.setGmailService(gmailService);
        try {
            for (int i = 0; i < emailRequest.getTo().length; i++) {
                System.out.println(emailRequest.getAttachments().get(0).getData());
                emailService.sendEmail(emailRequest.getTo()[i], emailRequest.getSubject(), emailRequest.getMessage(),emailRequest.getAttachments());
            }
            return "sent  Successfully";
            }
                  catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
      @GetMapping("/oauth2callback")
    public String oauthCallback(@RequestParam(value = "code", required = false) String code) throws IOException {
        GoogleTokenResponse tokenResponse = emailService.getToken(code,clientId,clientSecret,redirectPath);
        return tokenResponse.toString();
    }
    @GetMapping("/authorize")
    public RedirectView authorize() {
            try {
                return emailService.authorize(authorizationUrl,clientId,redirectPath,SCOPES);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
