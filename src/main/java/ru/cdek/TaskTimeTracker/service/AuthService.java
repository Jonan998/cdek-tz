package ru.cdek.TaskTimeTracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cdek.TaskTimeTracker.config.JwtUtil;
import ru.cdek.TaskTimeTracker.dto.AuthResponse;
import ru.cdek.TaskTimeTracker.dto.LoginRequest;
import ru.cdek.TaskTimeTracker.dto.RegisterRequest;
import ru.cdek.TaskTimeTracker.entity.User;
import ru.cdek.TaskTimeTracker.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthResponse register(RegisterRequest request) {

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    userRepository.save(user);

    String token = jwtUtil.generateToken(user.getUsername(), user.getId());
    return new AuthResponse(token);
  }

  public AuthResponse login(LoginRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

    String token = jwtUtil.generateToken(user.getUsername(), user.getId());
    return new AuthResponse(token);
  }
}
