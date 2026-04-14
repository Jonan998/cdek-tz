package ru.cdek.TaskTimeTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.*;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskDto {
  private UUID id;

  @NotBlank(message = "Название задачи обязательно")
  private String title;

  @NotBlank(message = "Описание задачи обязательно")
  private String description;

  @NotNull(message = "Статус задачи обязателен")
  private TaskStatus status;

  public TaskDto(String title, String description, TaskStatus status) {
    this.title = title;
    this.description = description;
    this.status = status;
  }
}
