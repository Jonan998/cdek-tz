package ru.cdek.TaskTimeTracker.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

  @Id @GeneratedValue @UuidGenerator private UUID id;

  @Column(name = "Username", nullable = false, unique = true)
  private String username;

  @Column(name = "Password", nullable = false)
  private String password;

  @Column(name = "Role", nullable = false)
  private String role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Task> tasks = new ArrayList<>();
}
