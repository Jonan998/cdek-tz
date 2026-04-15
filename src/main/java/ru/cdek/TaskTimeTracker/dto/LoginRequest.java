package ru.cdek.TaskTimeTracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на авторизацию")
public class LoginRequest {

  @NotBlank(message = "Логин обязателен")
  @Schema(description = "Имя пользователя", example = "maksim")
  private String username;

  @NotBlank(message = "Пароль обязателен")
  @Schema(description = "Пароль пользователя", example = "password123")
  private String password;
}
