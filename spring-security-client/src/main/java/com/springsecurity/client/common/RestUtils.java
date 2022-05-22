package com.springsecurity.client.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class RestUtils {
    private RestUtils() {
        throw new IllegalStateException("Can not instantiated");
    }

    public static <T> ResponseEntity<RestResponse<T>> successResponse(final T data, final HttpStatus httpStatus, final String message) {
        return new ResponseEntity<RestResponse<T>>(new RestResponse<T>(message, null, data), httpStatus);
    }

    public static <T> ResponseEntity<RestResponse<T>> successResponse(final T data, final String message) {
        return successResponse(data, HttpStatus.OK, message);
    }

    public static <T> ResponseEntity<RestResponse<T>> successResponse(final T data, HttpStatus httpStatus) {
        return new ResponseEntity<RestResponse<T>>(new RestResponse<T>(data), httpStatus);
    }

    public static <T> ResponseEntity<RestResponse<T>> successResponse(final T data) {
        return successResponse(data, HttpStatus.OK);

    }

    public static <T> ResponseEntity<RestResponse<?>> errorResponse(final String message, final String errorCode, final HttpStatus httpStatus) {
        return new ResponseEntity<RestResponse<?>>(new RestResponse<T>(message, errorCode, null), httpStatus);
    }

    public static <T> ResponseEntity<RestResponse<?>> errorResponseWithPayload(final String errorMessage,
                                                                               final String errorCode, final HttpStatus statusCode, T payload) {
        return new ResponseEntity<RestResponse<?>>(new RestResponse<T>(errorCode, errorMessage, payload), statusCode);
    }
}
