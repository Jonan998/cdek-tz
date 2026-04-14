package ru.cdek.TaskTimeTracker.service;

import java.util.UUID;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;

public interface TaskService {
  void createTask(UUID userId, TaskDto task);

  TaskDto getTask(UUID userId, UUID taskId);

  void updateTaskStatus(UUID userId, UUID taskId, TaskStatus newStatus);
}
