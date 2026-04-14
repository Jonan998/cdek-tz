package ru.cdek.TaskTimeTracker.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cdek.TaskTimeTracker.dto.ErrorResponse;
import ru.cdek.TaskTimeTracker.exception.InvalidCredentialsException;
import ru.cdek.TaskTimeTracker.exception.TaskAccessDeniedException;
import ru.cdek.TaskTimeTracker.exception.UserAlreadyExistsException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("Bad request: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("bad_request", ex.getMessage()));
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
    log.warn("Conflict: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("user_already_exists", ex.getMessage()));
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
    log.warn("Unauthorized: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorResponse("invalid_credentials", ex.getMessage()));
  }

  @ExceptionHandler(TaskAccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleTaskAccessDenied(TaskAccessDeniedException ex) {
    log.warn("Forbidden: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorResponse("forbidden", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Некорректные данные запроса");

    return new ErrorResponse("bad_request", message);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAnyException(Exception ex) {
    log.error("Unexpected error", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ErrorResponse(
                "server_error", "Произошла непредвиденная ошибка. Мы уже разбираемся."));
  }
}
