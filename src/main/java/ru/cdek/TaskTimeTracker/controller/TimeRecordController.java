package ru.cdek.TaskTimeTracker.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;
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
  public void createTimeRecord(@Valid @RequestBody TimeRecordDto timeRecordDto) {
    timeRecordService.createTimeRecord(timeRecordDto);
  }

  @GetMapping(value = GET_PATH)
  public List<TimeRecordDto> getEmployeeTimeRecordsByPeriod(
      @RequestParam Long employeeId,
      @RequestParam LocalDateTime start,
      @RequestParam LocalDateTime end) {
    return timeRecordService.getTimeRecord(employeeId, start, end);
  }
}
