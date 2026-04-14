package ru.cdek.TaskTimeTracker.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cdek.TaskTimeTracker.entity.TimeRecord;

@Repository
public interface TimeRecordRepository extends JpaRepository<TimeRecord, UUID> {
  List<TimeRecord> findAllByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
      Long employeeId, LocalDateTime start, LocalDateTime end);
}
