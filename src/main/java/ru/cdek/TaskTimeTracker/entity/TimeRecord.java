package ru.cdek.TaskTimeTracker.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "time_record")
@NoArgsConstructor
@Getter
@Setter
@SuppressFBWarnings(
    value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "JPA entity intentionally exposes mutable associations for ORM mapping")
public class TimeRecord {

  @Id @GeneratedValue @UuidGenerator private UUID id;

  @Column(name = "employee_id", nullable = false)
  private Long employeeId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "task_id", nullable = false)
  private Task task;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

  @Column(nullable = false)
  private String description;
}
