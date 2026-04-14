package ru.cdek.TaskTimeTracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;
import ru.cdek.TaskTimeTracker.service.TimeRecordService;

@WebMvcTest(TimeRecordController.class)
class TimeRecordControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TimeRecordService timeRecordService;

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

  @Test
  void createTimeRecord_shouldReturnCreated() throws Exception {
    String requestBody =
        """
                {
                  "employeeId": 123,
                  "taskId": "52a7860b-2e0c-49de-800d-1e349905ac56",
                  "startTime": "2026-04-15T10:00:00",
                  "endTime": "2026-04-15T12:30:00",
                  "description": "Реализовал создание задачи"
                }
                """;

    mockMvc
        .perform(
            post("/time-records/create")
                .with(csrf())
                .with(
                    authentication(
                        new UsernamePasswordAuthenticationToken(
                            "test_user", null, Collections.emptyList())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isCreated());

    verify(timeRecordService).createTimeRecord(any(TimeRecordDto.class));
  }

  @Test
  void getEmployeeTimeRecordsByPeriod_shouldReturnOk() throws Exception {
    UUID id = UUID.randomUUID();
    UUID taskId = UUID.randomUUID();

    TimeRecordDto dto = new TimeRecordDto();
    dto.setId(id);
    dto.setEmployeeId(123L);
    dto.setTaskId(taskId);
    dto.setStartTime(LocalDateTime.of(2026, 4, 15, 10, 0));
    dto.setEndTime(LocalDateTime.of(2026, 4, 15, 12, 30));
    dto.setDescription("Реализовал создание задачи");

    when(timeRecordService.getTimeRecord(
            eq(123L),
            eq(LocalDateTime.of(2026, 4, 1, 0, 0)),
            eq(LocalDateTime.of(2026, 4, 30, 23, 59, 59))))
        .thenReturn(List.of(dto));

    mockMvc
        .perform(
            get("/time-records/get")
                .with(
                    authentication(
                        new UsernamePasswordAuthenticationToken(
                            "test_user", null, Collections.emptyList())))
                .param("employeeId", "123")
                .param("start", "2026-04-01T00:00:00")
                .param("end", "2026-04-30T23:59:59"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(id.toString()))
        .andExpect(jsonPath("$[0].employeeId").value(123))
        .andExpect(jsonPath("$[0].taskId").value(taskId.toString()))
        .andExpect(jsonPath("$[0].startTime").value("2026-04-15T10:00:00"))
        .andExpect(jsonPath("$[0].endTime").value("2026-04-15T12:30:00"))
        .andExpect(jsonPath("$[0].description").value("Реализовал создание задачи"));

    verify(timeRecordService)
        .getTimeRecord(
            123L, LocalDateTime.of(2026, 4, 1, 0, 0), LocalDateTime.of(2026, 4, 30, 23, 59, 59));
  }
}
