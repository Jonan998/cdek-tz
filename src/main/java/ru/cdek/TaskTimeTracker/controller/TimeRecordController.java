package ru.cdek.TaskTimeTracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;
import ru.cdek.TaskTimeTracker.security.UserPrincipal;
import ru.cdek.TaskTimeTracker.service.TimeRecordService;

@RestController
@RequestMapping(value = "/time-records", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Time Records", description = "Операции с учётом затраченного времени")
@SecurityRequirement(name = "bearerAuth")
public class TimeRecordController {
  private static final String CREATE_PATH = "/create";
  private static final String GET_PATH = "/get";

  private final TimeRecordService timeRecordService;

  @PostMapping(value = CREATE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Создать запись о затраченном времени",
      description = "Создаёт запись о времени, затраченном текущим пользователем на задачу")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Запись успешно создана"),
    @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public void createTimeRecord(
      @AuthenticationPrincipal UserPrincipal user,
      @Valid @RequestBody TimeRecordDto timeRecordDto) {
    timeRecordService.createTimeRecord(user.getId(), timeRecordDto);
  }

  @GetMapping(value = GET_PATH)
  @Operation(
      summary = "Получить записи о времени за период",
      description = "Возвращает записи текущего пользователя за указанный период времени")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Записи успешно получены"),
    @ApiResponse(responseCode = "400", description = "Некорректный период"),
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
  })
  public List<TimeRecordDto> getEmployeeTimeRecordsByPeriod(
      @AuthenticationPrincipal UserPrincipal user,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
    return timeRecordService.getTimeRecord(user.getId(), start, end);
  }
}
