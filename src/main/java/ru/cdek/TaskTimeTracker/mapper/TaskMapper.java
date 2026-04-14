package ru.cdek.TaskTimeTracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;

@Mapper(componentModel = "spring", imports = TaskStatus.class)
public interface TaskMapper {

    TaskDto toDto(Task entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "status", expression = "java(TaskStatus.NEW)")
    @Mapping(target = "timeRecords", ignore = true)
    Task toEntity(TaskDto dto);
}