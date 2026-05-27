package com.basscode.employee_leave_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
    private final HttpStatus status;

    public GlobalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static GlobalException badRequestException(String message) {
        return  new GlobalException(message,HttpStatus.BAD_REQUEST);
    }

    public static GlobalException notFoundException(String message) {
        return new GlobalException(message, HttpStatus.NOT_FOUND);
    }

    public static GlobalException forbiddenException(String message) {
        return new GlobalException(message, HttpStatus.FORBIDDEN);
    }
}
