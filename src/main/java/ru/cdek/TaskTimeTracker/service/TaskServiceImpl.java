package ru.cdek.TaskTimeTracker.service;

import org.springframework.stereotype.Service;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.mapper.TaskMapper;
import ru.cdek.TaskTimeTracker.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
  private final Task task;
  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  public TaskServiceImpl(Task task, TaskRepository taskRepository, TaskMapper taskMapper) {
    this.task = task;
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
  }

  @Override
  public void createTask(Long userId, TaskDto task) {
    Task newTask = taskMapper.toEntity(task);
    taskRepository.save(newTask);
  }
}
