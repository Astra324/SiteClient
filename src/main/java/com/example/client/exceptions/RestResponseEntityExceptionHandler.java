package com.example.client.exceptions;

import com.example.client.repo.HtmlTemplatesRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice

public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${appHost}")
    private  String appHost;
    @Autowired
    HtmlTemplatesRepo htmlTemplatesRepo;



    @ExceptionHandler(value
            = { UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = htmlTemplatesRepo.getTemplateWithHeaderBody("user-not-found-resolve-form", (body)->{
            String message = "<div class=\"alert\"><h4 class=\"text-muted\">" + ex.getMessage() + "</h4><div>";
            return message + body;
        });
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value
            = { DocumentLoadErrorException.class })
    protected ResponseEntity<Object> handleDocumentLoadError(
            RuntimeException ex, WebRequest request) {
        String description = request.getDescription(false);
        String uri = description.substring(description.indexOf("=") + 1);
        System.out.println(uri);
        String bodyOfResponse = htmlTemplatesRepo.getTemplateWithHeaderBody("article-reload", (body)->{
            String message = "<div class=\"alert\"><h4 class=\"text-muted\"><a href='" + appHost + uri + "' >Try again</a></h4><div>";

            return message;
        });
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }



}
