package com.a_olumide.todo_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TodoAlreadyExistsException extends RuntimeException {
    public  TodoAlreadyExistsException(String message) {super(message);}
}
