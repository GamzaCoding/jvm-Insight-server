package com.gamzacoding.jvm_Insight_server.error;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobeExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handelApiException(ApiException e, HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiError(
                        errorCode.name(),
                        e.getMessage(),
                        List.of(),
                        request.getRequestURI(),
                        Instant.now()
                ));
    }
}
