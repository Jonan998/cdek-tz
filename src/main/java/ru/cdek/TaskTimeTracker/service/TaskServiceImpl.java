package ru.cdek.TaskTimeTracker.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;
import ru.cdek.TaskTimeTracker.entity.User;
import ru.cdek.TaskTimeTracker.exception.TaskAccessDeniedException;
import ru.cdek.TaskTimeTracker.exception.TaskNotFoundException;
import ru.cdek.TaskTimeTracker.exception.UserNotFoundException;
import ru.cdek.TaskTimeTracker.mapper.TaskMapper;
import ru.cdek.TaskTimeTracker.repository.TaskRepository;
import ru.cdek.TaskTimeTracker.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final TaskMapper taskMapper;

  @Override
  public TaskDto createTask(UUID userId, TaskDto taskDto) {
    log.info("Создание задачи: userId={}, title={}", userId, taskDto.getTitle());
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> {
                  log.warn("Пользователь не найден: userId={}", userId);
                  return new UserNotFoundException("Пользователь не найден");
                });

    Task task = taskMapper.toEntity(taskDto);

    task.setUser(user);

    Task savedTask = taskRepository.save(task);

    log.info("Задача успешно создана: taskId={}, userId={}", savedTask.getId(), userId);

    return taskMapper.toDto(savedTask);
  }

  @Override
  public TaskDto getTask(UUID userId, UUID taskId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(
                () -> {
                  log.warn("Задача не найдена: taskId={}", taskId);
                  return new TaskNotFoundException("Задача не найдена");
                });

    if (!task.getUser().getId().equals(userId)) {
      log.warn("Попытка доступа к чужой задаче: userId={}, taskId={}", userId, taskId);
      throw new TaskAccessDeniedException("Нет доступа к задаче");
    }

    log.info("Задача получена: userId={}, taskId={}", userId, taskId);

    return taskMapper.toDto(task);
  }

  @Override
  public void updateTaskStatus(UUID userId, UUID taskId, TaskStatus newStatus) {

    log.info(
        "Обновление статуса задачи: userId={}, taskId={}, newStatus={}", userId, taskId, newStatus);

    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(
                () -> {
                  log.warn("Задача не найдена: taskId={}", taskId);
                  return new TaskNotFoundException("Задача не найдена");
                });

    if (!task.getUser().getId().equals(userId)) {
      log.warn("Попытка изменить чужую задачу: userId={}, taskId={}", userId, taskId);
      throw new TaskAccessDeniedException("Нет доступа к задаче");
    }

    task.setStatus(newStatus);

    taskRepository.save(task);

    log.info("Статус задачи обновлён: taskId={}, newStatus={}", taskId, newStatus);
  }
}
