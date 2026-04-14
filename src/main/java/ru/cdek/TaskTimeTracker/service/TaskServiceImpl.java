package ru.cdek.TaskTimeTracker.service;

import org.springframework.stereotype.Service;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.mapper.TaskMapper;
import ru.cdek.TaskTimeTracker.repository.TaskRepository;

@Service
// @SuppressFBWarnings(
//    value = "EI_EXPOSE_REP2",
//    justification = "Spring injects shared mapper bean intentionally")
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
  }

  @Override
  public void createTask(Long userId, TaskDto task) {
    Task newTask = taskMapper.toEntity(task);
    taskRepository.save(newTask);
  }
}
