package ru.cdek.TaskTimeTracker.exception;

public class InvalidTimeRangeException extends RuntimeException {
  public InvalidTimeRangeException(String message) {
    super(message);
  }
}
