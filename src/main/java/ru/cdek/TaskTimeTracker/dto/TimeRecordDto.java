package ru.cdek.TaskTimeTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TimeRecordDto {
  private UUID id;

  @NotNull(message = "Id сотрудника обязателен")
  @Positive(message = "Id сотрудника должен быть положительным")
  private Long employeeId;

  @NotNull(message = "Id задачи обязателен")
  private UUID taskId;

  @NotNull(message = "Время начала обязательно")
  private LocalDateTime startTime;

  @NotNull(message = "Время окончания обязательно")
  private LocalDateTime endTime;

  @NotBlank(message = "Описание обязательно")
  private String description;
}
