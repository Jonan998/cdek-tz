package ru.cdek.TaskTimeTracker.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;
import ru.cdek.TaskTimeTracker.security.UserPrincipal;
import ru.cdek.TaskTimeTracker.service.TaskService;

@RestController
@RequestMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskController {
  private static final String CREATE_PATH = "/create";
  private static final String GET_PATH = "/get/{taskId}";
  private static final String UPDATE_PATH = "/{taskId}/status";

  private final TaskService taskService;

  @PostMapping(value = CREATE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void createTask(
      @AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody TaskDto taskDto) {
    taskService.createTask(user.getId(), taskDto);
  }

  @GetMapping(value = GET_PATH)
  public TaskDto getTask(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID taskId) {
    return taskService.getTask(user.getId(), taskId);
  }

  @PatchMapping(value = UPDATE_PATH)
  public void updateTaskStatus(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable UUID taskId,
      @RequestParam TaskStatus status) {
    taskService.updateTaskStatus(user.getId(), taskId, status);
  }
}
