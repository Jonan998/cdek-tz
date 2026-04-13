package ru.cdek.TaskTimeTracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "TimeRecord")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TimeRecord {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @Column(name = "EmployeeId", nullable = false)
  private Long employeeId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "taskId", nullable = false)
  private Task task;

  @Column(name = "StartTime", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "EndTime", nullable = false)
  private LocalDateTime endTime;

  @Column(name = "Description", nullable = false)
  private String description;
}
