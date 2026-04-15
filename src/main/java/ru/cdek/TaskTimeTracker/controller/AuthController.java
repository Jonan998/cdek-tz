package ru.cdek.TaskTimeTracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdek.TaskTimeTracker.dto.AuthResponse;
import ru.cdek.TaskTimeTracker.dto.LoginRequest;
import ru.cdek.TaskTimeTracker.dto.RegisterRequest;
import ru.cdek.TaskTimeTracker.service.AuthServiceImpl;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Аутентификация и регистрация")
public class AuthController {
  private static final String REGISTER_PATH = "/register";
  private static final String LOGIN_PATH = "/login";

  private final AuthServiceImpl authService;

  @PostMapping(value = REGISTER_PATH)
  @Operation(summary = "Регистрация пользователя")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
    @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
  })
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @PostMapping(value = LOGIN_PATH)
  @Operation(summary = "Авторизация пользователя")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
    @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
  })
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }
}
