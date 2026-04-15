package ru.cdek.TaskTimeTracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Ответ с описанием ошибки")
public class ErrorResponse {
  @Schema(description = "Код ошибки", example = "invalid_credentials")
  private String code;

  @Schema(description = "Текст ошибки", example = "Неверный логин или пароль")
  private String message;
}
