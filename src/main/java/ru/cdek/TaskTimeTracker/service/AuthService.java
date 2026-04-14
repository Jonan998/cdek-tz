package ru.cdek.TaskTimeTracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cdek.TaskTimeTracker.config.JwtUtil;
import ru.cdek.TaskTimeTracker.dto.AuthResponse;
import ru.cdek.TaskTimeTracker.dto.LoginRequest;
import ru.cdek.TaskTimeTracker.dto.RegisterRequest;
import ru.cdek.TaskTimeTracker.entity.User;
import ru.cdek.TaskTimeTracker.exception.InvalidCredentialsException;
import ru.cdek.TaskTimeTracker.exception.UserAlreadyExistsException;
import ru.cdek.TaskTimeTracker.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthResponse register(RegisterRequest request) {
    log.info("Попытка регистрации для пользователя={}", request.getUsername());

    if (userRepository.existsByUsername(request.getUsername())) {
      log.warn("Регистрация прервана: username={} занято", request.getUsername());
      throw new UserAlreadyExistsException("Пользователь с таким именем существует");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole("ROLE_USER");

    userRepository.save(user);

    log.info("Пользователь {} успешно зарегистрирован", user.getUsername());

    String token = jwtUtil.generateToken(user.getUsername(), user.getId());
    return new AuthResponse(token);
  }

  public AuthResponse login(LoginRequest request) {
    log.info("Попытка авторизации для пользователя={}", request.getUsername());

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (BadCredentialsException ex) {
      log.warn("Неуспешная авторизация для пользователя={}", request.getUsername());
      throw new InvalidCredentialsException("Неверный логин или пароль");
    }

    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new InvalidCredentialsException("Неверный логин или пароль"));

    log.info("Пользователь {} успешно авторизован", user.getUsername());

    String token = jwtUtil.generateToken(user.getUsername(), user.getId());
    return new AuthResponse(token);
  }
}
