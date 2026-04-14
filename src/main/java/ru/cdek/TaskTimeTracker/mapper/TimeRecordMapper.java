package ru.cdek.TaskTimeTracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cdek.TaskTimeTracker.dto.TimeRecordDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.entity.TimeRecord;

@Mapper(componentModel = "spring")
public interface TimeRecordMapper {

  @Mapping(target = "taskId", source = "task.id")
  TimeRecordDto toDto(TimeRecord entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "employeeId", source = "dto.employeeId")
  @Mapping(target = "startTime", source = "dto.startTime")
  @Mapping(target = "endTime", source = "dto.endTime")
  @Mapping(target = "description", source = "dto.description")
  @Mapping(target = "task", source = "task")
  TimeRecord toEntity(TimeRecordDto dto, Task task);
}
