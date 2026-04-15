package ru.cdek.TaskTimeTracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {

  @NotBlank(message = "Логин обязателен")
  @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
  @Schema(description = "Имя пользователя", example = "maksim")
  private String username;

  @NotBlank(message = "Пароль обязателен")
  @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
  @Schema(description = "Пароль пользователя", example = "password123")
  private String password;
}
