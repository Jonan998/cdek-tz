package ru.cdek.TaskTimeTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
  @NotBlank(message = "Логин обязателен")
  @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
  private String username;

  @NotBlank(message = "Пароль обязателен")
  @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
  private String password;
}
