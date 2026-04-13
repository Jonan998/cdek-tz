package ru.cdek.TaskTimeTracker.dto;

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
  private String title;
  private String description;
  private TaskStatus status;

  public TaskDto(String title, String description, TaskStatus status) {
    this.title = title;
    this.description = description;
    this.status = status;
  }
}
