package ru.cdek.TaskTimeTracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tasks", description = "Операции с задачами")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {
  private static final String CREATE_PATH = "/create";
  private static final String GET_PATH = "/get/{taskId}";
  private static final String UPDATE_PATH = "/{taskId}/status";

  private final TaskService taskService;

  @PostMapping(value = CREATE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Создать задачу",
      description = "Создаёт новую задачу для текущего пользователя")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
    @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
  })
  public TaskDto createTask(
      @AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody TaskDto taskDto) {
    return taskService.createTask(user.getId(), taskDto);
  }

  @GetMapping(value = GET_PATH)
  @Operation(
      summary = "Получить задачу по id",
      description = "Возвращает информацию о задаче текущего пользователя")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Задача найдена"),
    @ApiResponse(responseCode = "403", description = "Нет доступа к задаче"),
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public TaskDto getTask(@AuthenticationPrincipal UserPrincipal user, @PathVariable UUID taskId) {
    return taskService.getTask(user.getId(), taskId);
  }

  @PatchMapping(value = UPDATE_PATH)
  @Operation(
      summary = "Изменить статус задачи",
      description = "Обновляет статус задачи текущего пользователя")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Статус успешно обновлён"),
    @ApiResponse(responseCode = "403", description = "Нет доступа к задаче"),
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public void updateTaskStatus(
      @AuthenticationPrincipal UserPrincipal user,
      @PathVariable UUID taskId,
      @RequestParam TaskStatus status) {
    taskService.updateTaskStatus(user.getId(), taskId, status);
  }
}
