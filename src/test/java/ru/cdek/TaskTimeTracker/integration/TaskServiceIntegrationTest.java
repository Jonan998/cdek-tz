package ru.cdek.TaskTimeTracker.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;
import ru.cdek.TaskTimeTracker.entity.User;
import ru.cdek.TaskTimeTracker.exception.TaskAccessDeniedException;
import ru.cdek.TaskTimeTracker.repository.TaskRepository;
import ru.cdek.TaskTimeTracker.repository.UserRepository;
import ru.cdek.TaskTimeTracker.service.TaskService;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class TaskServiceIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16")
          .withDatabaseName("test_db")
          .withUsername("postgres")
          .withPassword("postgres");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    registry.add("spring.flyway.enabled", () -> true);
  }

  @Autowired private TaskService taskService;

  @Autowired private UserRepository userRepository;

  @Autowired private TaskRepository taskRepository;

  @BeforeEach
  void cleanDatabase() {
    taskRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void createTask_andGetTask_shouldWorkCorrectly() {
    User user =
        User.builder()
            .username("integration_user")
            .password("encoded_password")
            .role("ROLE_USER")
            .build();

    User savedUser = userRepository.save(user);

    TaskDto createDto =
        TaskDto.builder()
            .title("Integration task")
            .description("Integration description")
            .status(TaskStatus.NEW)
            .build();

    taskService.createTask(savedUser.getId(), createDto);

    List<Task> savedTasks = taskRepository.findAll();
    assertEquals(1, savedTasks.size());

    Task savedTask = savedTasks.get(0);
    assertEquals("Integration task", savedTask.getTitle());
    assertEquals("Integration description", savedTask.getDescription());
    assertEquals(TaskStatus.NEW, savedTask.getStatus());
    assertEquals(savedUser.getId(), savedTask.getUser().getId());

    TaskDto result = taskService.getTask(savedUser.getId(), savedTask.getId());

    assertNotNull(result);
    assertEquals(savedTask.getId(), result.getId());
    assertEquals("Integration task", result.getTitle());
    assertEquals("Integration description", result.getDescription());
    assertEquals(TaskStatus.NEW, result.getStatus());
  }

  @Test
  void getTask_shouldThrow_whenAnotherUserTriesToAccessTask() {
    User owner =
        User.builder()
            .username("owner_user")
            .password("encoded_password")
            .role("ROLE_USER")
            .build();

    User stranger =
        User.builder()
            .username("stranger_user")
            .password("encoded_password")
            .role("ROLE_USER")
            .build();

    User savedOwner = userRepository.save(owner);
    User savedStranger = userRepository.save(stranger);

    TaskDto createDto =
        TaskDto.builder()
            .title("Private task")
            .description("Private description")
            .status(TaskStatus.NEW)
            .build();

    taskService.createTask(savedOwner.getId(), createDto);

    List<Task> tasks = taskRepository.findAll();
    assertEquals(1, tasks.size());

    Task savedTask = tasks.get(0);

    assertThrows(
        TaskAccessDeniedException.class,
        () -> taskService.getTask(savedStranger.getId(), savedTask.getId()));
  }

  @Test
  void updateTaskStatus_shouldPersistNewStatus() {
    User user =
        User.builder()
            .username("status_user_" + UUID.randomUUID())
            .password("encoded_password")
            .role("ROLE_USER")
            .build();

    User savedUser = userRepository.save(user);

    TaskDto createDto =
        TaskDto.builder()
            .title("Status task")
            .description("Status description")
            .status(TaskStatus.NEW)
            .build();

    taskService.createTask(savedUser.getId(), createDto);

    List<Task> tasks = taskRepository.findAll();
    assertEquals(1, tasks.size());

    Task savedTask = tasks.get(0);

    taskService.updateTaskStatus(savedUser.getId(), savedTask.getId(), TaskStatus.DONE);

    Task updatedTask = taskRepository.findById(savedTask.getId()).orElseThrow();

    assertEquals(TaskStatus.DONE, updatedTask.getStatus());
  }
}
