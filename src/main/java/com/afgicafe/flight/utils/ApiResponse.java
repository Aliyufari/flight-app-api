package com.afgicafe.flight.utils;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time_stamp",
        "status",
        "status_code",
        "message",
        "data"
})
public class ApiResponse<T> {
    @Getter
    private String status;
    @Getter
    @JsonProperty("status_code")
    private int statusCode;
    @Getter
    private String message;
    private final Map<String, T> data = new HashMap<>();
    @Getter
    @JsonProperty("time_stamp")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
    private LocalDateTime timeStamp;

    public ApiResponse(
            String status,
            HttpStatus statusCode,
            String message,
            LocalDateTime timeStamp,
            String key,
            T value
    ){
        this.status = status;
        this.statusCode = statusCode.value();
        this.message = message;
        if (key != null){this.data.put(key, value);}
        this.timeStamp = timeStamp;
    }

    public static <T> ApiResponse<T> success(
            HttpStatus statusCode,
            String msg,
            String key,
            T value
    ){
        return new ApiResponse<>(
                statusCode.name(),
                statusCode,
                msg,
                LocalDateTime.now(),
                key,
                value
        );
    }

    public static <T> ApiResponse<T> ok( String msg){
        return new ApiResponse<>(
                HttpStatus.OK.name(),
                HttpStatus.OK,
                msg,
                LocalDateTime.now(),
                null,
                null
        );
    }

    public static <T> ApiResponse<T> error(
            HttpStatus statusCode,
            String msg,
            String key,
            T value
    ){
        return new ApiResponse<>(
                statusCode.name(),
                statusCode,
                msg,
                LocalDateTime.now(),
                key,
                value
        );
    }


    public static <T> ApiResponse<T> error(HttpStatus statusCode, String msg){
        return new ApiResponse<>(
                statusCode.name(),
                statusCode,
                msg,
                LocalDateTime.now(),
                null,
                null
        );
    }

    public static <T> ApiResponse<T> error(String msg){
        return new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                msg,
                LocalDateTime.now(),
                null,
                null
        );
    }

    @JsonAnyGetter
    public Map<String, T> getData() {
        return data;
    }

}

