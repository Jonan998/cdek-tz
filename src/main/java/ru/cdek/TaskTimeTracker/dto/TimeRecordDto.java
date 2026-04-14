package ru.cdek.TaskTimeTracker.dto;

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
  private Long employeeId;
  private UUID taskId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String description;
}
