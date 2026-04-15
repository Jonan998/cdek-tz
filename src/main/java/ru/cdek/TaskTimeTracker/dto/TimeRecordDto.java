package ru.cdek.TaskTimeTracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "DTO записи о затраченном времени")
public class TimeRecordDto {

  @Schema(
      description = "Идентификатор записи времени",
      example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @NotNull(message = "Id задачи обязателен")
  @Schema(
      description = "Идентификатор задачи",
      example = "550e8400-e29b-41d4-a716-446655440001",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private UUID taskId;

  @NotNull(message = "Время начала обязательно")
  @Schema(
      description = "Время начала работы",
      example = "2026-04-14T10:00:00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime startTime;

  @NotNull(message = "Время окончания обязательно")
  @Schema(
      description = "Время окончания работы",
      example = "2026-04-14T12:30:00",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime endTime;

  @NotBlank(message = "Описание обязательно")
  @Schema(
      description = "Описание выполненной работы",
      example = "Реализована авторизация и покрытие unit-тестами",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String description;
}
