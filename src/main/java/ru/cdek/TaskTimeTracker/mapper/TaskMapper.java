package ru.cdek.TaskTimeTracker.mapper;

import org.springframework.stereotype.Component;
import ru.cdek.TaskTimeTracker.dto.TaskDto;
import ru.cdek.TaskTimeTracker.entity.Task;
import ru.cdek.TaskTimeTracker.entity.TaskStatus;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }

        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build();
    }

    public Task toEntity(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.NEW);
        return task;
    }
}