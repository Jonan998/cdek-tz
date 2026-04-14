package ru.cdek.TaskTimeTracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.cdek.TaskTimeTracker.config.JwtFilter;
import ru.cdek.TaskTimeTracker.dto.AuthResponse;
import ru.cdek.TaskTimeTracker.dto.LoginRequest;
import ru.cdek.TaskTimeTracker.dto.RegisterRequest;
import ru.cdek.TaskTimeTracker.service.AuthServiceImpl;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private AuthServiceImpl authService;

  @MockBean private JwtFilter jwtFilter;

  @Test
  void registerShouldReturnToken() throws Exception {
    RegisterRequest request = new RegisterRequest();
    request.setUsername("alice");
    request.setPassword("123456");

    when(authService.register(any(RegisterRequest.class)))
        .thenReturn(new AuthResponse("test-register-token"));

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("test-register-token"));
  }

  @Test
  void loginShouldReturnToken() throws Exception {
    LoginRequest request = new LoginRequest();
    request.setUsername("alice");
    request.setPassword("123456");

    when(authService.login(any(LoginRequest.class)))
        .thenReturn(new AuthResponse("test-login-token"));

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("test-login-token"));
  }
}
