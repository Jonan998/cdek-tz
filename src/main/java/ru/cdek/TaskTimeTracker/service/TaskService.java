package ru.cdek.TaskTimeTracker.service;

import ru.cdek.TaskTimeTracker.dto.TaskDto;

public interface TaskService {
  void createTask(Long userId, TaskDto task);
}
