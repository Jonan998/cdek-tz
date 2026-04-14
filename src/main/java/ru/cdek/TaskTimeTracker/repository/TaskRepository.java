package ru.cdek.TaskTimeTracker.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cdek.TaskTimeTracker.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {}
