package ru.cdek.TaskTimeTracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.*;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "DTO обновления статуса задачи")
public class TaskUpdateDto {

  @Schema(description = "Идентификатор задачи", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID taskId;

  @Schema(description = "Новый статус задачи", example = "IN_PROGRESS")
  private TaskStatus status;
}
