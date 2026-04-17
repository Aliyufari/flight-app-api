package com.afgicafe.flight.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private final String status;

    private final int statusCode;

    private final String message;

    private final Object error;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
    private final LocalDateTime timeStamp;

    public ApiErrorResponse(HttpStatus status, String message, Object error) {
        this.status = status.name();
        this.statusCode = status.value();
        this.message = message;
        this.error = error;
        this.timeStamp = LocalDateTime.now();
    }

    public static ApiErrorResponse of(HttpStatus status, String message) {
        return new ApiErrorResponse(status, message, null);
    }

    public static ApiErrorResponse of(HttpStatus status, String message, Object error) {
        return new ApiErrorResponse(status, message, error);
    }
}