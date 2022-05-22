package com.springsecurity.client.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    private String message;
    private String errorCode;
    private T data;

    public RestResponse(T data) {
        this(null, null, data);
    }

}
