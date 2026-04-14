package ru.cdek.TaskTimeTracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

  @Mock private TaskRepository taskRepository;

  @Mock private UserRepository userRepository;

  @Mock private TaskMapper taskMapper;

  @InjectMocks private TaskServiceImpl taskService;

  private UUID userId;
  private UUID anotherUserId;
  private UUID taskId;
  private User user;
  private User anotherUser;
  private Task task;
  private TaskDto taskDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    anotherUserId = UUID.randomUUID();
    taskId = UUID.randomUUID();

    user = new User();
    user.setId(userId);
    user.setUsername("test_user");
    user.setPassword("encoded_password");
    user.setRole("ROLE_USER");

    anotherUser = new User();
    anotherUser.setId(anotherUserId);
    anotherUser.setUsername("another_user");
    anotherUser.setPassword("encoded_password");
    anotherUser.setRole("ROLE_USER");

    task = new Task();
    task.setId(taskId);
    task.setTitle("Test task");
    task.setDescription("Test description");
    task.setStatus(TaskStatus.NEW);
    task.setUser(user);

    taskDto =
        TaskDto.builder()
            .id(taskId)
            .title("Test task")
            .description("Test description")
            .status(TaskStatus.NEW)
            .build();
  }

  @Test
  void createTask_shouldSaveTask_whenUserExists() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(taskMapper.toEntity(taskDto)).thenReturn(task);
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    taskService.createTask(userId, taskDto);

    verify(userRepository).findById(userId);
    verify(taskMapper).toEntity(taskDto);
    verify(taskRepository).save(task);

    assertEquals(user, task.getUser());
  }

  @Test
  void createTask_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> taskService.createTask(userId, taskDto));

    verify(userRepository).findById(userId);
    verify(taskMapper, never()).toEntity(any());
    verify(taskRepository, never()).save(any());
  }

  @Test
  void getTask_shouldReturnTaskDto_whenTaskExistsAndBelongsToUser() {
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    TaskDto result = taskService.getTask(userId, taskId);

    assertNotNull(result);
    assertEquals(taskDto, result);

    verify(taskRepository).findById(taskId);
    verify(taskMapper).toDto(task);
  }

  @Test
  void getTask_shouldThrowTaskNotFoundException_whenTaskDoesNotExist() {
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.getTask(userId, taskId));

    verify(taskRepository).findById(taskId);
    verify(taskMapper, never()).toDto(any());
  }

  @Test
  void getTask_shouldThrowTaskAccessDeniedException_whenTaskBelongsToAnotherUser() {
    task.setUser(anotherUser);
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

    assertThrows(TaskAccessDeniedException.class, () -> taskService.getTask(userId, taskId));

    verify(taskRepository).findById(taskId);
    verify(taskMapper, never()).toDto(any());
  }

  @Test
  void updateTaskStatus_shouldUpdateStatus_whenTaskBelongsToUser() {
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    taskService.updateTaskStatus(userId, taskId, TaskStatus.DONE);

    assertEquals(TaskStatus.DONE, task.getStatus());

    verify(taskRepository).findById(taskId);
    verify(taskRepository).save(task);
  }

  @Test
  void updateTaskStatus_shouldThrowTaskNotFoundException_whenTaskDoesNotExist() {
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    assertThrows(
        TaskNotFoundException.class,
        () -> taskService.updateTaskStatus(userId, taskId, TaskStatus.DONE));

    verify(taskRepository).findById(taskId);
    verify(taskRepository, never()).save(any());
  }

  @Test
  void updateTaskStatus_shouldThrowTaskAccessDeniedException_whenTaskBelongsToAnotherUser() {
    task.setUser(anotherUser);
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

    assertThrows(
        TaskAccessDeniedException.class,
        () -> taskService.updateTaskStatus(userId, taskId, TaskStatus.DONE));

    verify(taskRepository).findById(taskId);
    verify(taskRepository, never()).save(any());
  }
}
