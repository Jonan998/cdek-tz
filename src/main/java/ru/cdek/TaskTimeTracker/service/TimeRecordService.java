package ru.cdek.TaskTimeTracker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;

public interface TimeRecordService {

  void createTimeRecord(UUID employeeId, TimeRecordDto timeRecordDto);

  List<TimeRecordDto> getTimeRecord(UUID employeeId, LocalDateTime start, LocalDateTime end);
}
