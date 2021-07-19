package com.gabia.alarmdistribution.exception;

import com.gabia.alarmdistribution.dto.response.APIResponse;
import com.gabia.alarmdistribution.dto.response.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BindException.class)
    public ResponseEntity<APIResponse> handleBindException(BindException bindException, Locale locale) {
        log.error(bindException.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.withMessageAndResult("BindException", ValidationResult.create(bindException, messageSource, locale)));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<APIResponse> missingRequestHeaderExceptionHandler(MissingRequestHeaderException e){
        log.error(e.getMessage());

        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("required_header_name", e.getHeaderName());
        errorBody.put("error_message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.withMessageAndResult("MissingRequestHeaderException", errorBody));
    }

}