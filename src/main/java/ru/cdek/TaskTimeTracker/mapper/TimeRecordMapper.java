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
  @Mapping(target = "task", source = "task")
  TimeRecord toEntity(TimeRecordDto dto, Task task);
}
