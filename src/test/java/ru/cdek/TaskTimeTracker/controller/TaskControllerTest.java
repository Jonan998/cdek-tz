package ru.cdek.TaskTimeTracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import ru.cdek.TaskTimeTracker.config.JwtFilter;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;
import ru.cdek.TaskTimeTracker.security.UserPrincipal;
import ru.cdek.TaskTimeTracker.service.TaskService;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TaskService taskService;

  @MockBean private JwtFilter jwtFilter;

  @BeforeEach
  void setUp() throws Exception {
    doAnswer(
            invocation -> {
              ServletRequest request = invocation.getArgument(0);
              ServletResponse response = invocation.getArgument(1);
              FilterChain chain = invocation.getArgument(2);
              chain.doFilter(request, response);
              return null;
            })
        .when(jwtFilter)
        .doFilter(any(), any(), any());
  }

  private UserPrincipal buildPrincipal(UUID userId) {
    UserPrincipal principal = mock(UserPrincipal.class);
    when(principal.getId()).thenReturn(userId);
    when(principal.getUsername()).thenReturn("test_user");
    when(principal.getAuthorities()).thenReturn(Collections.emptyList());
    return principal;
  }

  @Test
  void createTask_shouldReturnCreated() throws Exception {
    UUID userId = UUID.randomUUID();
    UserPrincipal principal = buildPrincipal(userId);

    String requestBody =
        """
                {
                  "title": "Task title",
                  "description": "Task description",
                  "status": "NEW"
                }
                """;

    mockMvc
        .perform(
            post("/tasks/create")
                .with(csrf())
                .with(
                    authentication(
                        new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated());

    verify(taskService).createTask(eq(userId), any(TaskDto.class));
  }

  @Test
  void getTask_shouldReturnTask() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID taskId = UUID.randomUUID();
    UserPrincipal principal = buildPrincipal(userId);

    TaskDto response = new TaskDto();
    response.setTitle("Task title");
    response.setDescription("Task description");
    response.setStatus(TaskStatus.NEW);

    when(taskService.getTask(userId, taskId)).thenReturn(response);

    mockMvc
        .perform(
            get("/tasks/get/{taskId}", taskId)
                .with(
                    authentication(
                        new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities()))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Task title"))
        .andExpect(jsonPath("$.description").value("Task description"))
        .andExpect(jsonPath("$.status").value("NEW"));

    verify(taskService).getTask(userId, taskId);
  }

  @Test
  void updateTaskStatus_shouldReturnOk() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID taskId = UUID.randomUUID();
    UserPrincipal principal = buildPrincipal(userId);

    mockMvc
        .perform(
            patch("/tasks/{taskId}/status", taskId)
                .param("status", "DONE")
                .with(csrf())
                .with(
                    authentication(
                        new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities()))))
        .andExpect(status().isOk());

    verify(taskService).updateTaskStatus(userId, taskId, TaskStatus.DONE);
  }
}
