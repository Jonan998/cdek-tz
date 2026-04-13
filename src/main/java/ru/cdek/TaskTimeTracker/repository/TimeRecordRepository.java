package ru.cdek.TaskTimeTracker.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cdek.TaskTimeTracker.entity.TimeRecord;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, UUID> {}
