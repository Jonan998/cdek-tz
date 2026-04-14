package ru.cdek.TaskTimeTracker.dto;

import java.util.UUID;
import lombok.*;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskUpdateDto {
  private UUID taskId;
  private TaskStatus status;
}
