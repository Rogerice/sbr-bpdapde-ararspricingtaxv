package com.santander.bp.exception;

import com.santander.bp.model.Error;
import com.santander.bp.model.Error.LevelEnum;
import com.santander.bp.model.Errors;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  public GlobalExceptionHandler() {
    logger.info("GlobalExceptionHandler initialized");
  }

  @ExceptionHandler(CosmosDbException.class)
  public ResponseEntity<Errors> handleCosmosDbException(CosmosDbException ex) {
    Errors errorResponse =
        Errors.builder()
            .errors(
                List.of(
                    Error.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .level(LevelEnum.ERROR)
                        .description(ex.getDescription())
                        .build()))
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(AltairException.class)
  public ResponseEntity<Errors> handleAltairException(AltairException ex) {
    Errors errorResponse =
        Errors.builder()
            .errors(
                List.of(
                    Error.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .level(LevelEnum.ERROR)
                        .description(ex.getDescription())
                        .build()))
            .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Errors> handleGenericException(Exception ex) {
    Errors errorResponse =
        Errors.builder()
            .errors(
                List.of(
                    Error.builder()
                        .code("500")
                        .message("An unexpected error occurred")
                        .level(LevelEnum.ERROR)
                        .description(ex.getMessage())
                        .build()))
            .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
