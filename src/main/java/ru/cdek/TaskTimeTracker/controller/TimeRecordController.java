package ru.cdek.TaskTimeTracker.controller;

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
public class TimeRecordController {
  private static final String CREATE_PATH = "/create";
  private static final String GET_PATH = "/get";

  private final TimeRecordService timeRecordService;

  @PostMapping(value = CREATE_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void createTimeRecord(
      @AuthenticationPrincipal UserPrincipal user,
      @Valid @RequestBody TimeRecordDto timeRecordDto) {
    timeRecordService.createTimeRecord(user.getId(), timeRecordDto);
  }

  @GetMapping(value = GET_PATH)
  public List<TimeRecordDto> getEmployeeTimeRecordsByPeriod(
      @AuthenticationPrincipal UserPrincipal user,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
    return timeRecordService.getTimeRecord(user.getId(), start, end);
  }
}
