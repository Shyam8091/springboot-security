package com.springsecurity.client.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springsecurity.client.common.RestResponse;
import com.springsecurity.client.common.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<RestResponse<?>> handleResourceNotFoundException(ResourceException exception) {
        return RestUtils.errorResponse(exception.getMessage(), exception.getErrorCode(), HttpStatus.OK);

    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<RestResponse<?>> handleJsonProcessingException(JsonProcessingException exception) {
        return RestUtils.errorResponse("Json processing  Exception", "US-414", HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<?>> handleGlobalException(Exception exception) {
        log.info("Exception : " + Arrays.toString(exception.getStackTrace()));
        return RestUtils.errorResponse(exception.toString(), "US-415", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
