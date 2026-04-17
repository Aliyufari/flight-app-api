package com.afgicafe.flight.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "status",
        "status_code",
        "message",
        "data"
})
public class ApiResponse<T> {

    @JsonProperty("status")
    private final String status;

    @JsonProperty("status_code")
    private final int statusCode;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("data")
    private T data;

    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
    private final LocalDateTime timeStamp;

    private ApiResponse(HttpStatus status, String message) {
        this.timeStamp = LocalDateTime.now();
        this.status = status.name();
        this.statusCode = status.value();
        this.message = message;
    }

    private ApiResponse(HttpStatus status, String message, T data) {
        this.timeStamp = LocalDateTime.now();
        this.status = status.name();
        this.statusCode = status.value();
        this.message = message;
        this.data = data;
    }

    // HttpStatus always 200 no data only message
    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(HttpStatus.OK, message);
    }

    // Parse HttpStatus by yourself no data only message
    public static <T> ApiResponse<T> success(HttpStatus status, String message) {
        return new ApiResponse<>(status, message);
    }

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
}