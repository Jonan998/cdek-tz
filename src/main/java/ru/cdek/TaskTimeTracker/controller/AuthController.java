package ru.cdek.TaskTimeTracker.controller;

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
public class AuthController {
  private static final String REGISTER_PATH = "/register";
  private static final String LOGIN_PATH = "/login";

  private final AuthServiceImpl authService;

  @PostMapping(value = REGISTER_PATH)
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request);
  }

  @PostMapping(value = LOGIN_PATH)
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }
}
