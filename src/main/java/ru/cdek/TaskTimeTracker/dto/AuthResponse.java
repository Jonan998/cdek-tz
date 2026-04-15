package ru.cdek.TaskTimeTracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Ответ с JWT-токеном")
public class AuthResponse {

  @Schema(description = "JWT токен доступа", example = "eyJhbGciOiJIUzI1NiJ9...")
  private String token;
}
