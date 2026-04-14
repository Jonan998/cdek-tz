package ru.cdek.TaskTimeTracker.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.entity.TimeRecord;
import ru.cdek.TaskTimeTracker.exception.InvalidTimeRangeException;
import ru.cdek.TaskTimeTracker.exception.TaskNotFoundException;
import ru.cdek.TaskTimeTracker.mapper.TimeRecordMapper;
import ru.cdek.TaskTimeTracker.repository.TaskRepository;
import ru.cdek.TaskTimeTracker.repository.TimeRecordRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeRecordServiceImpl implements TimeRecordService {

  private final TimeRecordRepository timeRecordRepository;
  private final TaskRepository taskRepository;
  private final TimeRecordMapper timeRecordMapper;

  @Override
  public void createTimeRecord(TimeRecordDto timeRecordDto) {
    log.info(
        "Создание записи времени: employeeId={}, taskId={}, startTime={}, endTime={}",
        timeRecordDto.getEmployeeId(),
        timeRecordDto.getTaskId(),
        timeRecordDto.getStartTime(),
        timeRecordDto.getEndTime());

    if (timeRecordDto.getStartTime().isAfter(timeRecordDto.getEndTime())) {
      log.warn(
          "Некорректный интервал времени: employeeId={}, taskId={}, startTime={}, endTime={}",
          timeRecordDto.getEmployeeId(),
          timeRecordDto.getTaskId(),
          timeRecordDto.getStartTime(),
          timeRecordDto.getEndTime());
      throw new InvalidTimeRangeException("Время начала не может быть позже времени окончания");
    }

    Task task =
        taskRepository
            .findById(timeRecordDto.getTaskId())
            .orElseThrow(
                () -> {
                  log.warn("Задача не найдена: taskId={}", timeRecordDto.getTaskId());
                  return new TaskNotFoundException("Задача не найдена");
                });

    TimeRecord timeRecord = timeRecordMapper.toEntity(timeRecordDto, task);
    timeRecordRepository.save(timeRecord);

    log.info(
        "Запись времени успешно создана: employeeId={}, taskId={}",
        timeRecordDto.getEmployeeId(),
        timeRecordDto.getTaskId());
  }

  @Override
  public List<TimeRecordDto> getTimeRecord(
      Long employeeId, LocalDateTime start, LocalDateTime end) {
    log.info("Получение записей времени: employeeId={}, start={}, end={}", employeeId, start, end);

    if (start.isAfter(end)) {
      log.warn(
          "Некорректный период запроса: employeeId={}, start={}, end={}", employeeId, start, end);
      throw new InvalidTimeRangeException("Начало периода не может быть позже конца периода");
    }

    List<TimeRecordDto> result =
        timeRecordRepository
            .findAllByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                employeeId, start, end)
            .stream()
            .map(timeRecordMapper::toDto)
            .toList();

    log.info("Записи времени получены: employeeId={}, count={}", employeeId, result.size());

    return result;
  }
}
