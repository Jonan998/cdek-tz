package ru.cdek.TaskTimeTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "Task")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Task {
  @Id @GeneratedValue @UuidGenerator private UUID id;

  @Column(name = "Title", nullable = false)
  private String title;

  @Column(name = "Description", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "Status", nullable = false)
  private TaskStatus status;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  @Builder.Default
  private List<TimeRecord> timeRecords = new ArrayList<>();
}
