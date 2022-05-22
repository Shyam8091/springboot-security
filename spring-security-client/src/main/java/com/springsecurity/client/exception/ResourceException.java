package com.springsecurity.client.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
public class ResourceException extends RuntimeException {
    final Object data;
    final String errorCode;
    final String message;
    final HttpStatus httpStatus;

}
