package ru.cdek.TaskTimeTracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Данные задачи")
public class TaskDto {
  @Schema(description = "Идентификатор задачи", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @NotBlank(message = "Название задачи обязательно")
  @Schema(description = "Название задачи", example = "Подготовить отчёт")
  private String title;

  @NotBlank(message = "Описание задачи обязательно")
  @Schema(description = "Описание задачи", example = "Собрать статистику по проекту")
  private String description;

  @NotNull(message = "Статус задачи обязателен")
  @Schema(description = "Статус задачи", example = "NEW")
  private TaskStatus status;

  public TaskDto(String title, String description, TaskStatus status) {
    this.title = title;
    this.description = description;
    this.status = status;
  }
}
