package ru.cdek.TaskTimeTracker.service;

import java.time.LocalDateTime;
import java.util.List;
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;

public interface TimeRecordService {
  void createTimeRecord(TimeRecordDto timeRecordDto);

  List<TimeRecordDto> getTimeRecord(Long employeeId, LocalDateTime start, LocalDateTime end);
}
