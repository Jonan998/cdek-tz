package ru.cdek.TaskTimeTracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;
import ru.cdek.TaskTimeTracker.entity.TimeRecord;
import ru.cdek.TaskTimeTracker.exception.InvalidTimeRangeException;
import ru.cdek.TaskTimeTracker.exception.TaskNotFoundException;
import ru.cdek.TaskTimeTracker.mapper.TimeRecordMapper;
import ru.cdek.TaskTimeTracker.repository.TaskRepository;
import ru.cdek.TaskTimeTracker.repository.TimeRecordRepository;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceImplTest {

  @Mock private TimeRecordRepository timeRecordRepository;

  @Mock private TaskRepository taskRepository;

  @Mock private TimeRecordMapper timeRecordMapper;

  @InjectMocks private TimeRecordServiceImpl timeRecordService;

  @Test
  void createTimeRecord_shouldSaveSuccessfully() {
    UUID taskId = UUID.randomUUID();

    TimeRecordDto dto = new TimeRecordDto();
    dto.setEmployeeId(123L);
    dto.setTaskId(taskId);
    dto.setStartTime(LocalDateTime.of(2026, 4, 15, 10, 0));
    dto.setEndTime(LocalDateTime.of(2026, 4, 15, 12, 0));
    dto.setDescription("Работа по задаче");

    Task task = new Task();
    task.setId(taskId);
    task.setTitle("Task");
    task.setDescription("Desc");
    task.setStatus(TaskStatus.NEW);

    TimeRecord entity = new TimeRecord();
    entity.setEmployeeId(123L);
    entity.setTask(task);
    entity.setStartTime(dto.getStartTime());
    entity.setEndTime(dto.getEndTime());
    entity.setDescription(dto.getDescription());

    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
    when(timeRecordMapper.toEntity(dto, task)).thenReturn(entity);

    timeRecordService.createTimeRecord(dto);

    verify(taskRepository).findById(taskId);
    verify(timeRecordMapper).toEntity(dto, task);
    verify(timeRecordRepository).save(entity);
  }

  @Test
  void createTimeRecord_shouldThrowWhenTaskNotFound() {
    UUID taskId = UUID.randomUUID();

    TimeRecordDto dto = new TimeRecordDto();
    dto.setEmployeeId(123L);
    dto.setTaskId(taskId);
    dto.setStartTime(LocalDateTime.of(2026, 4, 15, 10, 0));
    dto.setEndTime(LocalDateTime.of(2026, 4, 15, 12, 0));
    dto.setDescription("Работа по задаче");

    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    TaskNotFoundException ex =
        assertThrows(TaskNotFoundException.class, () -> timeRecordService.createTimeRecord(dto));

    assertEquals("Задача не найдена", ex.getMessage());
  }

  @Test
  void createTimeRecord_shouldThrowWhenStartAfterEnd() {
    TimeRecordDto dto = new TimeRecordDto();
    dto.setEmployeeId(123L);
    dto.setTaskId(UUID.randomUUID());
    dto.setStartTime(LocalDateTime.of(2026, 4, 15, 13, 0));
    dto.setEndTime(LocalDateTime.of(2026, 4, 15, 12, 0));
    dto.setDescription("Работа по задаче");

    InvalidTimeRangeException ex =
        assertThrows(
            InvalidTimeRangeException.class, () -> timeRecordService.createTimeRecord(dto));

    assertEquals("Время начала не может быть позже времени окончания", ex.getMessage());
  }

  @Test
  void getTimeRecord_shouldReturnList() {
    LocalDateTime start = LocalDateTime.of(2026, 4, 1, 0, 0);
    LocalDateTime end = LocalDateTime.of(2026, 4, 30, 23, 59, 59);

    Task task = new Task();
    task.setId(UUID.randomUUID());

    TimeRecord entity = new TimeRecord();
    entity.setId(UUID.randomUUID());
    entity.setEmployeeId(123L);
    entity.setTask(task);
    entity.setStartTime(LocalDateTime.of(2026, 4, 15, 10, 0));
    entity.setEndTime(LocalDateTime.of(2026, 4, 15, 12, 0));
    entity.setDescription("Работа по задаче");

    TimeRecordDto dto = new TimeRecordDto();
    dto.setId(entity.getId());
    dto.setEmployeeId(123L);
    dto.setTaskId(task.getId());
    dto.setStartTime(entity.getStartTime());
    dto.setEndTime(entity.getEndTime());
    dto.setDescription(entity.getDescription());

    when(timeRecordRepository
            .findAllByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                123L, start, end))
        .thenReturn(List.of(entity));
    when(timeRecordMapper.toDto(entity)).thenReturn(dto);

    List<TimeRecordDto> result = timeRecordService.getTimeRecord(123L, start, end);

    assertEquals(1, result.size());
    assertEquals(123L, result.get(0).getEmployeeId());
    assertEquals(task.getId(), result.get(0).getTaskId());
    assertEquals("Работа по задаче", result.get(0).getDescription());
  }

  @Test
  void getTimeRecord_shouldThrowWhenStartAfterEnd() {
    LocalDateTime start = LocalDateTime.of(2026, 4, 30, 23, 59, 59);
    LocalDateTime end = LocalDateTime.of(2026, 4, 1, 0, 0);

    InvalidTimeRangeException ex =
        assertThrows(
            InvalidTimeRangeException.class,
            () -> timeRecordService.getTimeRecord(123L, start, end));

    assertEquals("Начало периода не может быть позже конца периода", ex.getMessage());
  }
}
