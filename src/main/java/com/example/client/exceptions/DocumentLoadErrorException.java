package com.example.client.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DocumentLoadErrorException extends  RuntimeException{

    public DocumentLoadErrorException() {
    }

    public DocumentLoadErrorException(String message) {
        super(message);
    }

    public DocumentLoadErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentLoadErrorException(Throwable cause) {
        super(cause);
    }
}
