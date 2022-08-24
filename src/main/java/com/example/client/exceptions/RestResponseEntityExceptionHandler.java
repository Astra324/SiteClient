package com.example.client.exceptions;

import com.example.client.exceptions.UserNotFoundException;
import com.example.client.repo.HtmlTemplatesRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    HtmlTemplatesRepo htmlTemplatesRepo;

    @ExceptionHandler(value
            = { UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = htmlTemplatesRepo.getTemplate("user-not-found-resolve-form", (body)->{
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
        String bodyOfResponse = htmlTemplatesRepo.getTemplate("user-not-found-resolve-form", (body)->{
            String message = "<div class=\"alert\"><h4 class=\"text-muted\">" + ex.getMessage() + "</h4><div>";
            return message;
        });
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }



}
